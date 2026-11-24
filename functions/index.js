const { onSchedule } = require('firebase-functions/v2/scheduler');
const { onDocumentWritten } = require('firebase-functions/v2/firestore');
const { initializeApp } = require('firebase-admin/app');
const { getFirestore, FieldValue } = require('firebase-admin/firestore');

initializeApp();
const db = getFirestore();
const seasonRef = db.doc('config/season');
const seasonCollection = db.collection('seasons');
const USERS_PAGE_SIZE = 400;

function seasonTemplate(year, source) {
  const startsAt = new Date(Date.UTC(year, 0, 1, 3, 1, 0));
  const endsAt = new Date(Date.UTC(year + 1, 0, 1, 3, 1, 0));
  return {
    id: String(year),
    year,
    name: `Temporada ${year}`,
    timezone: source.timezone || 'America/Sao_Paulo',
    startsAt,
    endsAt,
    ligas: source.ligas || {},
    promotionPoints: source.promotionPoints || {},
    updatedAt: FieldValue.serverTimestamp()
  };
}

// Keeps every future season's rules synchronized with the active season.
exports.replicateSeasonPolicy = onDocumentWritten('config/season', async (event) => {
  const after = event.data && event.data.after;
  if (!after || !after.exists) return;
  const current = after.data();
  const currentYear = Number(current.year || current.id || new Date().getUTCFullYear());
  const {
    name: _name, id: _id, year: _year, startsAt: _startsAt, endsAt: _endsAt,
    active: _active, activatedAt: _activatedAt, ...policy
  } = current;
  const future = await seasonCollection.where('year', '>', currentYear).get();
  if (future.empty) return;
  const batch = db.batch();
  future.docs.forEach((doc) => {
    batch.set(doc.ref, {
      ...policy,
      policyVersion: current.policyVersion || doc.data().policyVersion || 1,
      updatedAt: FieldValue.serverTimestamp()
    }, { merge: true });
  });
  await batch.commit();
});

// Runs at 00:01 in Sao Paulo every January 1st. It activates the next season,
// creates future templates, and resets the active-season elo fields atomically
// per user page without deleting historical mission results.
exports.rotateSeason = onSchedule({
  schedule: '1 0 1 1 *',
  timeZone: 'America/Sao_Paulo',
  region: 'southamerica-east1'
}, async () => {
  const activeSnap = await seasonRef.get();
  const active = activeSnap.exists ? activeSnap.data() : {};
  // Target the calendar year, never active+1: scheduled events can be retried.
  const targetYear = Number(new Intl.DateTimeFormat('en', {timeZone:'America/Sao_Paulo', year:'numeric'}).format(new Date()));
  if (Number(active.year || active.id) > targetYear) throw new Error('Active season is in the future');
  const nextRef = seasonCollection.doc(String(targetYear));
  const nextSnap = await nextRef.get();
  const next = nextSnap.exists ? nextSnap.data() : seasonTemplate(targetYear, active);
  await nextRef.set(next, {merge:true});
  await seasonRef.set({...next, active:true, activatedAt:FieldValue.serverTimestamp()}, {merge:true});
  for (let future = targetYear + 1; future <= targetYear + 3; future++) {
    const ref = seasonCollection.doc(String(future));
    await db.runTransaction(async tx => {
      const snap = await tx.get(ref);
      if (!snap.exists) tx.create(ref, seasonTemplate(future, active));
    });
  }

  let last;
  do {
    let query = db.collection('users').orderBy('__name__').limit(USERS_PAGE_SIZE);
    if (last) query = query.startAfter(last);
    const page = await query.get();
    if (page.empty) break;
    for (const user of page.docs) {
      await db.runTransaction(async tx => {
        const fresh = await tx.get(user.ref);
        const data = fresh.data();
        if (!data || data.seasonId === String(targetYear)) return;
        const previousYear = String(data.seasonId || targetYear - 1);
        // Archive and reset this user atomically; retries cannot erase new points.
        tx.set(user.ref.collection('seasonHistory').doc(previousYear), {
          seasonId: previousYear,
          elo:data.elo || 0, elo25:data.elo25 || 0, elo5:data.elo5 || 0, elo10:data.elo10 || 0,
          archivedAt:FieldValue.serverTimestamp()
        });
        tx.update(user.ref, {seasonId:String(targetYear), elo:0, elo25:0, elo5:0, elo10:0,
          seasonStartedAt:FieldValue.serverTimestamp()});
      });
    }
    last = page.docs[page.docs.length - 1];
  } while(last);

});

// Keeps the missions collection limited to the current day while preserving
// every participant result under the user's permanent competition history.
exports.purgeExpiredMissions = onSchedule({
  schedule: '10 0 * * *',
  timeZone: 'America/Sao_Paulo',
  region: 'southamerica-east1'
}, async () => {
  const localDate = new Intl.DateTimeFormat('en-CA', {
    timeZone: 'America/Sao_Paulo', year: 'numeric', month: '2-digit', day: '2-digit'
  }).format(new Date());
  const startOfToday = new Date(`${localDate}T03:00:00.000Z`);
  const expired = await db.collection('missions')
    .where('startsAt', '<', startOfToday)
    .get();
  if (expired.empty) return;

  for (const mission of expired.docs) {
    // Never delete a live race. Rules cap its duration at three hours.
    if (mission.get('startsAt').toMillis() + 3 * 60 * 60 * 1000 > Date.now()) continue;
    const players = await mission.ref.collection('players').get();
    let batch = db.batch();
    let writes = 0;
    for (const player of players.docs) {
      const data = player.data();
      const historyRef = db.doc(`users/${player.id}/competitionHistory/${mission.id}`);
      batch.set(historyRef, {
        missionId: mission.id,
        distanceKm: mission.get('distanceKm') || null,
        startsAt: mission.get('startsAt') || null,
        name: data.name || null,
        state: data.state || null,
        joinedAt: data.joinedAt || null,
        progressKm: data.progressKm || 0,
        paceSecPerKm: data.paceSecPerKm || null,
        timeSec: data.timeSec || 0,
        finishedAt: data.finishedAt || null,
        eloDelta: data.eloDelta || 0,
        withdrawnAt: data.withdrawnAt || null,
        startedAt: data.startedAt || null,
        resultStatus: data.resultStatus || (data.finishedAt ? 'pending_validation' : data.withdrawnAt ? 'withdrawn' : 'dnf'),
        placement: data.placement || null,
        seasonId: data.seasonId || mission.get('seasonId') || null,
        archivedAt: FieldValue.serverTimestamp()
      }, { merge: true });
      batch.delete(player.ref);
      writes += 2;
      if (writes >= 450) {
        await batch.commit();
        batch = db.batch();
        writes = 0;
      }
    }
    await batch.commit();
    // Orphan subcollections survive parent deletion; remove all private evidence explicitly.
    let evidence;
    do {
      evidence = await mission.ref.collection('gpsPrivate').limit(400).get();
      if (!evidence.empty) {
        const cleanup = db.batch();
        evidence.docs.forEach(doc => cleanup.delete(doc.ref));
        await cleanup.commit();
      }
    } while (!evidence.empty);
    await mission.ref.delete();
  }
});

// Remove jogadores inativos de competições ao vivo (sem movimento GPS por 10 min).
// Roda a cada 10 minutos. Marca withdrawnAt e decrementa playerCount.
// NOTA: Requer plano Blaze (pago) para deploy. No Spark, a remoção por inatividade
// é feita apenas pelo app (RunTrackingService). Se o celular desligar, o jogador
// fica preso na sala até voltar online e o app chamar leaveMission().
exports.removeInactiveRunners = onSchedule({
  schedule: '*/10 * * * *',
  timeZone: 'America/Sao_Paulo',
  region: 'southamerica-east1'
}, async () => {
  const now = new Date();
  const tenMinutesAgo = new Date(now.getTime() - 10 * 60 * 1000);
  const batch = db.batch();

  const activeMissions = await db.collection('missions')
    .where('startsAt', '<=', now)
    .where('finishedAt', '==', null)
    .get();

  if (activeMissions.empty) return;

  let totalInactive = 0;

  for (const missionDoc of activeMissions.docs) {
    const missionRef = missionDoc.ref;
    const playersRef = missionRef.collection('players');
    const players = await playersRef.get();

    let inactiveCount = 0;
    const playerUpdates = [];

    for (const playerDoc of players.docs) {
      const playerData = playerDoc.data();
      if (!playerData.withdrawnAt &&
          playerData.lastGpsMovementAt &&
          playerData.lastGpsMovementAt.toMillis() < tenMinutesAgo.getTime()) {
        inactiveCount++;
        playerUpdates.push({
          playerRef: playerDoc.ref,
          withdrawnAt: FieldValue.serverTimestamp()
        });
      }
    }

    playerUpdates.forEach(update => {
      batch.update(update.playerRef, { withdrawnAt: update.withdrawnAt });
    });

    if (inactiveCount > 0) {
      const currentCount = missionDoc.get('playerCount') || 0;
      const newCount = Math.max(0, currentCount - inactiveCount);
      batch.update(missionRef, { playerCount: newCount });
      totalInactive += inactiveCount;
    }
  }

  await batch.commit();
  console.log(`removeInactiveRunners: ${totalInactive} jogador(es) removido(s) por inatividade`);
});
