import fs from 'node:fs';
import {initializeTestEnvironment,assertSucceeds,assertFails} from '@firebase/rules-unit-testing';
import {doc,setDoc,updateDoc,serverTimestamp,writeBatch,runTransaction,getDoc} from 'firebase/firestore';
const [host, port='8080'] = (process.env.FIRESTORE_EMULATOR_HOST || '127.0.0.1:8080').split(':');
const env=await initializeTestEnvironment({projectId:'demo-elite-fixes',firestore:{host,port:Number(port),rules:fs.readFileSync(new URL('../../firestore.rules', import.meta.url),'utf8')}});
const session='device-test-session-12345';
const base={name:'Audit',state:'MS',joinedAt:new Date(),progressKm:0,finishedAt:null,timeSec:0,eloDelta:0,readyAt:new Date()};
let count=0;
async function ok(label,task,allowed=true){await (allowed?assertSucceeds(task):assertFails(task));console.log('PASS '+(++count)+' '+label);}
try {
 await env.withSecurityRulesDisabled(async ctx=>{
  const db=ctx.firestore();
  for(const id of ['a','b']) {
   await setDoc(doc(db,'users/'+id),{name:id,state:'MS',elo:0,elo25:0,elo5:0,elo10:0,isPremium:false,seasonId:'2026'});
  }
  for(const id of ['start','finish','withdraw','winner']){
   await setDoc(doc(db,'missions/'+id),{distanceKm:2.5,startsAt:new Date(Date.now()-(id==='start'?10000:600000)),minPlayers:2,playerCount:2,state:'MS',timeZone:'America/Campo_Grande'});
   await setDoc(doc(db,'missions/'+id+'/players/a'),{...base,...(id==='start'?{}:{sessionId:session,startedAt:new Date()})});
   await setDoc(doc(db,'missions/'+id+'/players/b'),{...base,...(id==='start'?{}:{sessionId:session,startedAt:new Date()})});
  }
 });
 const a=env.authenticatedContext('a').firestore(), b=env.authenticatedContext('b').firestore();
 await ok('own server clock',setDoc(doc(a,'_timeSync/a'),{serverTime:serverTimestamp()}));
 await ok('cannot spoof clock',setDoc(doc(a,'_timeSync/a'),{serverTime:new Date(0)}),false);
 await ok('cannot write another clock',setDoc(doc(b,'_timeSync/a'),{serverTime:serverTimestamp()}),false);
 await ok('abandon canonical field',updateDoc(doc(a,'missions/withdraw/players/a'),{withdrawnAt:serverTimestamp()}));
 await ok('withdrawn cannot renew presence',updateDoc(doc(a,'missions/withdraw/players/a'),{readyAt:serverTimestamp()}),false);
 const ps=doc(a,'missions/start/players/a');
 await ok('no self as start witness',updateDoc(ps,{sessionId:session,startedAt:serverTimestamp(),startPeerUid:'a'}),false);
 await ok('claim with two present runners',updateDoc(ps,{sessionId:session,startedAt:serverTimestamp(),startPeerUid:'b'}));
 await ok('other session cannot replace claim',updateDoc(ps,{sessionId:'other-device-session-xxxx',startedAt:serverTimestamp(),startPeerUid:'b'}),false);
 const pf=doc(a,'missions/finish/players/a');
 const finish={sessionId:session,progressKm:2.5,timeSec:400,lastUpdatedAt:serverTimestamp(),finishedAt:serverTimestamp()};
 await ok('finish without evidence denied',updateDoc(pf,finish),false);
 const batch=writeBatch(a);
 batch.update(pf,finish);
 batch.set(doc(a,'missions/finish/gpsPrivate/a'),{points:[{latitude:-20.4,longitude:-54.6,accuracy:5,capturedAtMs:Date.now()}],updatedAt:serverTimestamp()});
 await ok('finish with valid evidence and session',batch.commit());
 await ok('retry of application final transaction is read-only',runTransaction(a,async tx=>{
   const snap=await tx.get(pf);
   if(snap.get('finishedAt')===null) tx.update(pf,finish);
 }));
 await ok('direct second finish denied',updateDoc(pf,finish),false);
 await ok('cannot read another route',getDoc(doc(b,'missions/finish/gpsPrivate/a')),false);
 await ok('cannot self-award achievement',setDoc(doc(a,'users/a/achievements/fake'),{progress:999}),false);
 const winnerPlayer=doc(a,'missions/winner/players/a');
 const winnerBatch=writeBatch(a);
 winnerBatch.update(winnerPlayer,finish);
 winnerBatch.set(doc(a,'missions/winner/gpsPrivate/a'),{points:[{latitude:-20.4,longitude:-54.6,accuracy:5,capturedAtMs:Date.now()}],updatedAt:serverTimestamp()});
 winnerBatch.update(doc(a,'missions/winner'),{winnerUid:'a',winnerFinishedAt:serverTimestamp(),winnerTimeSec:400});
 await ok('first finish atomically claims winner',winnerBatch.commit());
 const otherFinish={sessionId:session,progressKm:2.5,timeSec:420,lastUpdatedAt:serverTimestamp(),finishedAt:serverTimestamp()};
 const otherBatch=writeBatch(b);
 otherBatch.update(doc(b,'missions/winner/players/b'),otherFinish);
 otherBatch.set(doc(b,'missions/winner/gpsPrivate/b'),{points:[{latitude:-20.4,longitude:-54.6,accuracy:5,capturedAtMs:Date.now()}],updatedAt:serverTimestamp()});
 await ok('another runner may finish without replacing winner',otherBatch.commit());
 await ok('winner cannot be overwritten',updateDoc(doc(b,'missions/winner'),{winnerUid:'b',winnerFinishedAt:serverTimestamp(),winnerTimeSec:420}),false);
 console.log('ALL '+count+' CHECKS PASSED');
} finally {await env.cleanup();}
