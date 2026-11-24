import fs from 'node:fs';
import assert from 'node:assert/strict';
import { initializeTestEnvironment } from '@firebase/rules-unit-testing';
import { doc, setDoc, updateDoc, writeBatch, runTransaction, serverTimestamp } from 'firebase/firestore';

const [host, port = '8080'] = (process.env.FIRESTORE_EMULATOR_HOST || '127.0.0.1:8080').split(':');
const ruleFile = process.env.RULE_FILE || '../../firestore.published.rules';
const env = await initializeTestEnvironment({
  projectId: 'demo-elite-incident',
  firestore: { host, port: Number(port), rules: fs.readFileSync(new URL(ruleFile, import.meta.url), 'utf8').replace(/^\uFEFF/, '') }
});
const points = [
  [-22.1557763, -53.7719493, 16.400999069213867, 1795348819301],
  [-22.155671, -53.7720614, 3.575000047683716, 1795348895784],
  [-22.1556785, -53.7720967, 3.5190000534057617, 1795348906147],
  [-22.1556845, -53.7721314, 3.0290000438690186, 1795348988183],
  [-22.1556467, -53.772145, 1.399999976158142, 1795349003142],
  [-22.15562, -53.7721083, 1.3329999446868896, 1795349009145],
  [-22.1555933, -53.7720833, 1.2999999523162842, 1795349011142]
].map(([latitude, longitude, accuracy, capturedAtMs]) => ({latitude, longitude, accuracy, capturedAtMs}));
const paddedPoints = Array.from({length: 10}, (_, index) => index < 7 ? points[index] : {...points[6], capturedAtMs: points[6].capturedAtMs + index - 6});
const session = 'e6c82294-b0bb-434e-b289-4a8d879afcfd';
const db = env.authenticatedContext('484UAnbpVZgxJUWAWObDbP8Znej2').firestore();
const results = [];

async function seed(id, progress = 0, timeSec = 0, startsAgo = 300_000) {
  await env.withSecurityRulesDisabled(async ctx => {
    const admin = ctx.firestore();
    const startsAt = new Date(Date.now() - startsAgo);
    await setDoc(doc(admin, 'users/484UAnbpVZgxJUWAWObDbP8Znej2'), {state:'MS'});
    await setDoc(doc(admin, 'missions/' + id), {distanceKm:2.5, startsAt, minPlayers:2, playerCount:2, state:'MS', timeZone:'America/Campo_Grande'});
    await setDoc(doc(admin, 'missions/' + id + '/players/484UAnbpVZgxJUWAWObDbP8Znej2'), {
      name:'Rodrigo', state:'MS', joinedAt:new Date(startsAt.getTime()-60_000), progressKm:progress,
      finishedAt:null, timeSec, eloDelta:0, sessionId:session, startedAt:startsAt,
      startPeerUid:'other', readyAt:startsAt
    });
  });
}

async function attempt(label, count, progressKm, timeSec, options = {}) {
  const id = 'case_' + results.length;
  await seed(id, options.initialProgress || 0, options.initialTime || 0, options.startsAgo || 300_000);
  const batch = writeBatch(db);
  batch.update(doc(db, 'missions/' + id + '/players/484UAnbpVZgxJUWAWObDbP8Znej2'), {
    sessionId:session, progressKm, timeSec, lastUpdatedAt:serverTimestamp(),
    clientElapsedRealtimeMs:1805309106, paceSecPerKm: timeSec / Math.max(progressKm, 0.001),
    gpsAccuracyMeters:1.3, ...options.extraPlayer
  });
  batch.set(doc(db, 'missions/' + id + '/gpsPrivate/484UAnbpVZgxJUWAWObDbP8Znej2'), {
    points:paddedPoints.slice(0,count), updatedAt:serverTimestamp(), ...options.extraGps
  });
  try { await batch.commit(); results.push({label, status:'PASS'}); }
  catch(error) { results.push({label, status:'FAIL', code:error.code, message:error.message.slice(0,180)}); }
}

async function singleWrite(label, count, kind) {
  const id = 'single_' + results.length;
  await seed(id);
  const ref = doc(db, 'missions/' + id + '/' +
    (kind === 'gps' ? 'gpsPrivate' : 'players') + '/484UAnbpVZgxJUWAWObDbP8Znej2');
  try {
    if (kind === 'gps') await setDoc(ref, {points:paddedPoints.slice(0,count), updatedAt:serverTimestamp()});
    else await updateDoc(ref, {sessionId:session, progressKm:0.01689610187223658, timeSec:212,
      lastUpdatedAt:serverTimestamp(), clientElapsedRealtimeMs:1805309106});
    results.push({label, status:'PASS'});
  } catch(error) {
    results.push({label, status:'FAIL', code:error.code, message:error.message.slice(0,180)});
  }
}

async function singleProgress(label, progressKm, timeSec, options = {}) {
  const id = 'progress_' + results.length;
  await seed(id, options.initialProgress || 0, options.initialTime || 0);
  try {
    await updateDoc(doc(db, 'missions/' + id + '/players/484UAnbpVZgxJUWAWObDbP8Znej2'), {
      sessionId:session, progressKm, timeSec, lastUpdatedAt:serverTimestamp(),
      clientElapsedRealtimeMs:1805309106, ...options.extra
    });
    results.push({label, status:'PASS'});
  } catch(error) {
    results.push({label, status:'FAIL', code:error.code, message:error.message.slice(0,180)});
  }
}

async function winnerTransaction(count) {
  const label = 'winner_transaction_' + count + '_points';
  const id = 'winner_' + results.length;
  await seed(id, 0, 0, 600_000);
  const mission = doc(db, 'missions/' + id);
  const player = doc(db, 'missions/' + id + '/players/484UAnbpVZgxJUWAWObDbP8Znej2');
  const gps = doc(db, 'missions/' + id + '/gpsPrivate/484UAnbpVZgxJUWAWObDbP8Znej2');
  try {
    await runTransaction(db, async tx => {
      await tx.get(mission);
      await tx.get(player);
      tx.update(player, {sessionId:session, progressKm:2.5, timeSec:400,
        lastUpdatedAt:serverTimestamp(), finishedAt:serverTimestamp(),
        clientElapsedRealtimeMs:1805309106});
      tx.set(gps, {points:paddedPoints.slice(0,count), updatedAt:serverTimestamp()});
      tx.update(mission, {winnerUid:'484UAnbpVZgxJUWAWObDbP8Znej2',
        winnerFinishedAt:serverTimestamp(), winnerTimeSec:400});
    });
    results.push({label, status:'PASS'});
  } catch(error) {
    results.push({label, status:'FAIL', code:error.code, message:error.message.slice(0,180)});
  }
}

try {
  for (let count=1; count<=10; count++) await attempt('points_'+count, count, 0.01689610187223658, 212);
  await singleWrite('gps_only_10', 10, 'gps');
  await singleWrite('player_only', 10, 'player');
  await singleProgress('player_decreased_only', 0.04, 222, {initialProgress:0.05, initialTime:212});
  await singleProgress('player_time_ahead_only', 0.017, 400);
  await singleProgress('player_speed_over_7mps_only', 2.5, 212);
  await singleProgress('player_unknown_field_only', 0.017, 212, {extra:{unexpected:true}});
  await singleProgress('player_zero_time_only', 0, 0);
  await attempt('progress_decreased', 7, 0.04, 222, {initialProgress:0.05, initialTime:212});
  await attempt('time_ahead', 7, 0.017, 400);
  await attempt('speed_over_7mps', 7, 2.5, 212);
  await attempt('unknown_player_field', 7, 0.017, 212, {extraPlayer:{unexpected:true}});
  await attempt('zero_time_zero_distance', 7, 0, 0);
  await attempt('finish_2_5km', Number(process.env.FINISH_POINT_COUNT || 7), 2.5, 400, {startsAgo:600_000, extraPlayer:{finishedAt:serverTimestamp()}});
  await winnerTransaction(Number(process.env.FINISH_POINT_COUNT || 7));
  if (ruleFile.endsWith('firestore.proposed.rules') && process.env.FINISH_POINT_COUNT === '5') {
    for (const [label, status] of [['points_5', 'PASS'], ['points_6', 'FAIL'],
      ['player_only', 'PASS'], ['finish_2_5km', 'PASS'], ['winner_transaction_5_points', 'PASS']]) {
      assert.equal(results.find(item => item.label === label)?.status, status, label);
    }
  }
  for (const item of results) console.log(JSON.stringify(item));
} finally { await env.cleanup(); }
