import fs from 'node:fs';
import assert from 'node:assert/strict';
import { initializeTestEnvironment } from '@firebase/rules-unit-testing';
import { collection, doc, getDocs, query, runTransaction, setDoc, updateDoc, where, orderBy } from 'firebase/firestore';

const [host, port = '8080'] = (process.env.FIRESTORE_EMULATOR_HOST || '127.0.0.1:8080').split(':');
const env = await initializeTestEnvironment({
  projectId: 'demo-elite-scoring',
  firestore: { host, port: Number(port), rules: fs.readFileSync(new URL('../../firestore.proposed.rules', import.meta.url), 'utf8') }
});
const uid = 'runner';
const client = env.authenticatedContext(uid).firestore();
const year = new Date().getUTCFullYear();
const startsAt = new Date(Date.now() - 15 * 60_000);
const finishedAt = new Date(Date.now() - 5 * 60_000);
const seasonStart = new Date(Date.UTC(year, 0, 1));
const seasonEnd = new Date(Date.UTC(year + 1, 0, 1));

async function score(id, expectedPoints) {
  const user = doc(client, 'users', uid);
  const history = doc(client, 'users', uid, 'competitionHistory', id);
  const race = doc(client, 'missions', id);
  await runTransaction(client, async tx => {
    const oldHistory = await tx.get(history);
    if (oldHistory.exists()) return;
    const mission = await tx.get(race);
    const runner = await tx.get(doc(client, 'missions', id, 'players', uid));
    const profile = await tx.get(user);
    const season = await tx.get(doc(client, 'config', 'season'));
    assert.equal(profile.data().seasonId, season.data().id);
    const points = mission.data().winnerUid === uid ? 30 : 20;
    assert.equal(points, expectedPoints);
    tx.update(user, {
      elo: profile.data().elo + points,
      elo25: profile.data().elo25 + points,
      lastScoredMissionId: id
    });
    tx.set(history, {points, distanceKm: 2.5, finishedAt: runner.data().finishedAt, seasonId: String(year)});
  });
}

try {
  await env.withSecurityRulesDisabled(async ctx => {
    const admin = ctx.firestore();
    await setDoc(doc(admin, 'config', 'season'), {id: String(year), startsAt: seasonStart, endsAt: seasonEnd});
    await setDoc(doc(admin, 'users', uid), {name: 'Runner', state: 'MS', elo: 0, elo25: 0, elo5: 0, elo10: 0});
    for (const [id, winnerUid] of [['winner', uid], ['finisher', 'other']]) {
      await setDoc(doc(admin, 'missions', id), {distanceKm: 2.5, startsAt, state: 'MS', winnerUid});
      await setDoc(doc(admin, 'missions', id, 'players', uid), {finishedAt, progressKm: 2.5});
    }
  });
  await assert.rejects(updateDoc(doc(client, 'users', uid), {seasonId: String(year + 1)}));
  await updateDoc(doc(client, 'users', uid), {seasonId: String(year)});
  await score('winner', 30);
  await score('winner', 30);
  await score('finisher', 20);
  const ranking = await getDocs(query(collection(client, 'users'), where('seasonId', '==', String(year)), orderBy('elo', 'desc')));
  assert.equal(ranking.docs[0].data().elo, 50);
  assert.equal(ranking.docs[0].data().elo25, 50);
  await assert.rejects(updateDoc(doc(client, 'users', uid), {elo: 1000}));
  console.log('PASS: legacy season repaired, winner +30, finisher +20, no duplicate, ranking updated, direct elo edit denied');
} finally {
  await env.cleanup();
}
