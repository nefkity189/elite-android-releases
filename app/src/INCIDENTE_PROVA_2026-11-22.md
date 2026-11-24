# Incidente na prova 2,5K das 08:00 (MS), 22/11/2026

## Conclusão

O defeito de validação GPS está reproduzido: com a regra publicada, uma escrita de `gpsPrivate` com 10 pontos retorna `PERMISSION_DENIED` e `maximum of 1000 expressions to evaluate has been reached` no Firebase Emulator. O aplicativo envia os últimos 10 pontos e, no progresso normal, grava `players` e `gpsPrivate` no mesmo batch. Quando a regra rejeita o GPS, o batch inteiro falha. Não foi possível recuperar o payload da primeira tentativa rejeitada às ~08:03:42; portanto, não é comprovado que ela já tivesse 10 pontos. O servidor não conserva escritas rejeitadas.

## Fontes

- Regras publicadas, copiadas do console em 22/11: [firestore.published.rules](../../firestore.published.rules).
- Regras locais diferentes da produção: [firestore.rules](../../firestore.rules).
- Proposta de regra, **não publicada**: [firestore.proposed.rules](../../firestore.proposed.rules).
- Teste local com os sete pontos reais aceitos: [competition.incident.test.mjs](../../functions/tests/competition.incident.test.mjs).
- Código de envio: [CompetitionSync.kt](./main/java/com/elite/sch/app/data/competition/CompetitionSync.kt).
- Tela: [CompetitionFlowScreen.kt](./main/java/com/elite/sch/app/ui/screens/competition/CompetitionFlowScreen.kt).

## Produção: fatos verificados

Conta da Firebase CLI: `rodrigoschmoeller18@gmail.com`; único projeto listado: `aisler-runner`, número `536701798948`. O console mostra a regra das 05:47 de 22/11 como ativa e a das 05:21 como anterior. A regra ativa é a cópia em `firestore.published.rules`; contém `allow read: if signedIn()` em `missions` e `players`, sem o isolamento por UF dessas leituras presente no arquivo local. A regra publicada não foi alterada.

Histórico exibido no console: 22/11 às 05:47 e 05:21; 21/11 às 19:18, 16:05, 15:43, 15:17, 13:52, 13:50, 13:30, 12:16, 12:15, 12:09, 11:54, 11:46 (duas versões), 11:12, 11:08, 10:42, 10:40 e 10:02 (duas versões). Horários exibidos pelo navegador em UTC-4.

Documento `missions/ms_2_20261122_1200_utc`: `distanceKm=2.5`, `minPlayers=2`, `playerCount=2`, `state=MS`, `timeZone=America/Campo_Grande`, `startsAt=08:00 UTC-4`, sem `winnerUid`.

| Jogador | UID | startedAt | progressKm | timeSec | lastUpdatedAt | finishedAt | withdrawnAt |
|---|---|---|---:|---:|---|---|---|
| Rodrigo | `484UAnbpVZgxJUWAWObDbP8Znej2` | 08:00:08 | 0,01689610187223658 | 212 | 08:03:32 | null | 08:19:50 |
| Noemi | `xt2VohxckpgywoiQdIQGbUfxYzu2` | 08:00:09 | 0 | 638 | 08:10:38 | null | 08:13:10 |

Rodrigo: `sessionId=e6c82294-b0bb-434e-b289-4a8d879afcfd`, `paceSecPerKm=12547.2728326973`. O documento privado tem 7 pontos, capturados às 08:00:19, 08:01:35, 08:01:46, 08:03:08, 08:03:23, 08:03:29 e 08:03:31; precisão entre 1,3 e 16,4 m, `updatedAt=08:03:32`. As coordenadas usadas no teste estão no arquivo do teste.

Noemi: `sessionId=018e5a84-3f08-4655-8ed2-20eed986b164`. Seu único ponto foi **capturado às 08:00:09.199**, com precisão 11,674 m; o documento foi **atualizado às 08:10:38**. O relato confundia esses dois horários.

Consulta no Cloud Logging para 08:03–08:20 UTC-4: **0 resultados**. Não há nesse período um registro acessível indicando a linha da regra rejeitada. Os números de métricas 309 erros / 94 negações e de uso 912 gravações / 2,7 mil leituras vieram do relato e não foram confirmados independentemente nesta análise.

## Diferenças entre regra publicada e local

O arquivo local acrescenta checagens de `profileState()` nas leituras de `missions` e `players`, em `activePlayer()`, nos três ramos de atualização de `players` e na escrita de `gpsPrivate`. O restante do bloco de GPS e o limite de 10 pontos são iguais. Não publique o arquivo local inteiro como correção deste incidente.

## Reprodução local: projeto `demo-elite-incident`

O teste inicial com os sete pontos reais, `progressKm=0.01689610187223658` e `timeSec=212`, passou contra a regra publicada e a local. No emulador, com a regra publicada:

- `points_1` a `points_9`: `PASS`.
- `points_10`: `FAIL permission-denied`, com `maximum of 1000 expressions to evaluate has been reached`.
- `gps_only_10`: mesmo erro de 1.000 expressões. Separar as duas escritas sem diminuir a janela GPS não resolve.
- `player_only`: `PASS`.
- Atualização isolada de `players` com distância menor, `timeSec` adiantado, velocidade acima de 7 m/s ou campo desconhecido: `FAIL permission-denied`, como esperado. Distância e tempo zero: `PASS`. Essas recusas não provam qual condição ocorreu na corrida real.
- Chegada com 7 pontos, `progressKm=2.5`, `timeSec=400`, `finishedAt=request.time`: `PASS` em batch de jogador + GPS, com largada simulada há 600 s.

Com a proposta local limitada a 5 pontos: `points_1` a `points_5` passaram; acima de 5 foram rejeitados; `finish_2_5km` e `winner_transaction_5_points` passaram. O emulador confirma o risco de 1.000 expressões, mas não revela o conteúdo das tentativas reais rejeitadas.

`./gradlew.bat :app:assembleDebug --offline --no-daemon --console=plain`: `BUILD SUCCESSFUL in 1m 7s` — 41 tarefas, 1 executada e 40 atualizadas. Esta compilação é do código atual, ainda sem correção.

## Outros problemas verificados

1. [CompetitionFlowScreen.kt](./main/java/com/elite/sch/app/ui/screens/competition/CompetitionFlowScreen.kt) mostra, na linha `Você` do “Placar ao vivo”, a distância **local** (`distanceKm`) e o tempo **do servidor** (`player.timeSec`). Por isso a distância chegou a 2,50 km na tela enquanto o tempo ficou em 03:32 e o Firestore em 0,0169 km. “Sua posição” está fixada em “—”.
2. [CompetitionSync.kt](./main/java/com/elite/sch/app/data/competition/CompetitionSync.kt) grava a intenção de chegada localmente, mas a opção “Desistir” ainda pode registrar `withdrawnAt` e limpar o estado; a regra então impede a chegada. A tela de resultado só abre após confirmação do servidor, por isso não abriu neste incidente.
3. O ritmo de prova em `raceState` usa o tempo desde a largada; inclui a espera pela fixação GPS. Pode ser correto como tempo oficial, mas não como “pace atual”.
4. Não há Cloud Functions implantadas (`firebase functions:list` retornou `[]`). O cliente atribui `winnerUid`; a regra verifica `progressKm`, `timeSec` e a presença de **um** ponto GPS, mas não verifica a rota. Não há computação de Elo no servidor em produção.
5. Com 100 participantes, 18 min e duas gravações a cada 10 s, a projeção é 21.600 gravações apenas nessa sala. Listeners que acompanham todos os jogadores multiplicam leituras por participantes. É uma estimativa matemática, não medição da prova.

## Proposta para aprovação, sem aplicação

1. Partir da regra **publicada** e aplicar apenas o diff do bloco `gpsPrivate` em [firestore.proposed.rules](../../firestore.proposed.rules): limite de 10 para 5 pontos e validar índices 0–4. Não aplicar as demais diferenças do arquivo local neste incidente.
2. No app, mudar `routePoints.takeLast(10)` para `takeLast(5)`. Progresso normal: gravar `players` separadamente; `gpsPrivate` pode ser enviado com menor frequência. Na chegada, manter jogador + 5 pontos GPS + eventual `winnerUid` na mesma transação, pois a regra exige evidência com o mesmo `request.time`.
3. Persistir localmente uma chegada pendente antes da transação, retentar após reinício/conexão e impedir “Desistir” de descartar essa pendência. Mostrar estado “resultado pendente” até confirmação.
4. No placar, usar `player.progressKm` para todos. Mostrar distância local em campo distinto e com rótulo próprio. Registrar no Logcat, com tag `CompetitionSync`, caminho da escrita, número de pontos, distância, tempo e código da exceção; apresentar erro útil sem expor coordenadas.
5. Em validação posterior, executar duas contas em prova real por mais de 5 min, incluindo perda de conexão, 10+ pontos capturados, chegada e reabertura do app. Conferir Firestore, placar e resultado. Estimar custo antes de aumentar o número de participantes.

Reversão prevista, se a proposta for aprovada e aplicada: restaurar a cópia em [firestore.published.rules](../../firestore.published.rules), devolver `takeLast(10)` no APK e restaurar o manifesto da release anterior. Não apagar documentos. Essa reversão também restauraria o defeito de 10 pontos; usá-la só se a nova versão apresentar falha mais grave.

**Nenhum dado, regra, índice, função ou APK de produção foi alterado nesta investigação.** O resultado perdido de hoje não pode ser atribuído automaticamente: o servidor não recebeu a rota após 08:03:32.

## Correção aprovada em 22/11/2026

- Regra publicada no console às 10:38 (UTC-4): somente `gpsPrivate` mudou de 10 para 5 pontos e a validação dos índices foi reduzida a 0–4. A cópia anterior permanece em `../../firestore.published.rules`; a nova está em `../../firestore.proposed.rules`. O arquivo local `../../firestore.rules` recebeu o mesmo limite, mas contém outras diferenças antigas e **não deve ser publicado inteiro** como parte deste incidente.
- O app envia `players` a cada 10 segundos e o documento GPS privado com até 5 pontos a cada 60 segundos quando há progresso confirmado. Na chegada, grava localmente distância, tempo e evidência antes da transação atômica de jogador, GPS e vencedor. Tenta novamente após falha, reinício ou retorno à conta. A desistência é bloqueada enquanto a chegada estiver pendente.
- O placar mostra a distância confirmada pelo servidor; a distância local permanece na barra de progresso. Logcat usa a tag `CompetitionSync` e registra documento, distância, tempo e número de pontos sem coordenadas.
- O emulador do Firestore aceitou progresso, chegada e transação de vencedor com 5 pontos; recusou 6. O teste em celulares reais, com dois corredores e perda de conexão, continua pendente. Nenhum resultado antigo foi alterado.
- Release preparada com `versionCode=7`, `versionName=1.0.6`. Não rodar `firebase deploy` para este incidente. Publicar uma versão para usuários somente após o teste físico.
- Atenção durante o intervalo: o site e o manifesto de atualização ainda apontam para 1.0.5; essa versão pode falhar após o sexto ponto sob a regra nova. Instalar a 1.0.6 nos participantes antes da próxima prova real e só então atualizar a distribuição.
