# ELITE — Bíblia do projeto e guia de manutenção

**Atualizado em:** 22/11/2026  
**Projeto Firebase:** `aisler-runner`  
**Pacote Android:** `com.elite.sch.app`  
**Versão documentada:** `1.0.9` (`versionCode 10`)

Este arquivo é o mapa operacional do ELITE. Ele explica onde está cada parte, como os dados passam pelo sistema, como publicar uma versão e quais limites ainda existem. O código fonte do aplicativo está na raiz deste projeto; os protótipos, handoffs e automações ficam em `HANDOFF/`.

## 1. Mapa rápido

| Parte | Local | Função |
|---|---|---|
| APK Android | `app/` | Aplicativo nativo Kotlin + Jetpack Compose |
| Entrada do app | `app/src/main/java/com/elite/sch/app/MainActivity.kt` | Navegação, permissões, atualização e telas principais |
| Serviço de corrida | `app/src/main/java/com/elite/sch/app/service/RunTrackingService.kt` | GPS em primeiro plano, tempo, distância, ritmo e notificação |
| Conta | `app/src/main/java/com/elite/sch/app/account/` | Google/email, sessão Firebase e perfil do usuário |
| Competição | `app/src/main/java/com/elite/sch/app/data/competition/` e `ui/competition/` | Salas, inscrições, largada, GPS, abandono, chegada e ranking |
| Firebase | `google-services.json`, `firebase.json`, `firestore.rules`, `firestore.indexes.json` | Auth, Firestore, Hosting e regras |
| Site | `HANDOFF/Site/site/` | Página pública de download e informações |
| Releases | GitHub `nefkity189/elite-android-releases` | Hospedagem pública do APK assinado |
| Robô de ranking | `HANDOFF/ranking-robot/ranking-robot/ranking-robot/` | GitHub Actions a cada cinco minutos; atualiza pontos no Firestore |
| Handoffs visuais | `HANDOFF/competicao-online/` | Especificações, protótipos e auditorias de layout |

Links públicos:

- Site: <https://elitesch.web.app/>
- Projeto Firebase: <https://console.firebase.google.com/project/aisler-runner/overview>
- Firestore: <https://console.firebase.google.com/project/aisler-runner/firestore/databases/-default-/data>
- Repositório de releases: <https://github.com/nefkity189/elite-android-releases>
- Robô de ranking: <https://github.com/nefkity189/elite-ranking-robot>

## 2. Como o aplicativo funciona

### Inicialização e autenticação

1. `EliteApplication` inicializa Firebase e App Check de desenvolvimento.
2. `MainActivity` monta o app Compose, verifica a sessão e solicita permissões necessárias.
3. `AccountRepository` usa Firebase Authentication. A conta Google é vinculada ao UID do Firebase; o e-mail não é usado como chave de documento.
4. O perfil fica em `users/{uid}`. Ao trocar de conta, a sessão Firebase é a fonte de verdade e os listeners da competição são cancelados e recriados.

### Corrida comum

Planos, treinos, histórico local e rotas completas usam Room em `app/src/main/java/com/elite/sch/app/data/local/`. O `RunTrackingService` continua ativo quando a tela é bloqueada e publica uma notificação de corrida. O GPS é filtrado por precisão e a distância/ritmo são calculados no aparelho para o modo de treino.

### Competição online

O fluxo é controlado por `CompetitionDemoState` e `CompetitionFlowScreen`:

1. O app lê a UF no perfil e o fuso oficial da UF.
2. `OnlineCompetitionRepository.observeMissions` consulta as salas do dia para a distância escolhida (2,5K, 5K ou 10K).
3. A inscrição usa transação: cria/atualiza `missions/{missionId}` e `players/{uid}`, mantendo `playerCount` consistente.
4. A sala exige pelo menos dois participantes para iniciar. A largada é baseada em `startsAt` do servidor; a tela mostra a contagem de 3 segundos.
5. `CompetitionSync` conecta a prova ao `RunTrackingService`. O serviço envia progresso em intervalos controlados, e não a cada leitura de GPS.
6. Cada corredor pode desistir. A desistência grava `withdrawnAt`/estado de abandono e para o rastreamento da prova.
7. Ao atingir a distância, o jogador é marcado como concluído. O resultado usa tempo, pace, posição de chegada e participantes recebidos do Firestore.
8. O resultado é gravado no histórico cloud. O robô de ranking atribui os pontos depois, de modo idempotente.

Estados usados no jogador: `joined`, `ready`, `running`, `finished`, `dnf`/abandono e `timeout`. Uma prova não deve continuar enviando GPS depois de concluída ou abandonada.

## 3. Firebase: projeto e configuração

### Serviços usados

- **Authentication:** Google e e-mail/senha.
- **Cloud Firestore:** banco principal, região `southamerica-east1`.
- **Hosting:** publica o site estático.
- **App Check:** configuração de desenvolvimento está presente no APK; produção deve usar o provedor adequado antes de exigir enforcement.
- **Cloud Functions/Cloud Storage:** não são usados na arquitetura gratuita atual.

Há uma pasta [functions](../../functions) no projeto porque o `firebase.json` ainda conserva a configuração padrão do Firebase CLI. Ela não é o mecanismo ativo do ranking no Spark; não presumir que um deploy de Hosting também publique Functions.

Arquivos de configuração locais:

- [google-services.json](../../app/google-services.json) — identificação Android do projeto; não publicar uma cópia com segredos auxiliares.
- [firebase.json](../../firebase.json) — Hosting, regras, índices e configuração de Functions legada.
- [firestore.rules](../../firestore.rules) — segurança atual publicada.
- [firestore.indexes.json](../../firestore.indexes.json) — índices compostos.

### Modelo de dados

```text
config/season
  id, name, endsAt, ligas

users/{uid}
  name, state, avatarSeed, createdAt, isPremium
  seasonId, elo, elo25, elo5, elo10
  lastScoredMissionId

users/{uid}/competitionHistory/{missionId}
  points, distanceKm, finishedAt, seasonId

users/{uid}/achievements/{id}
  unlockedAt, progress

missions/{missionId}
  distanceKm, startsAt, minPlayers, playerCount, state, timeZone
  winnerUid, winnerFinishedAt, winnerTimeSec, rankingProcessedAt

missions/{missionId}/players/{uid}
  name, state, joinedAt, readyAt, startedAt, sessionId
  progressKm, finishedAt, timeSec, paceSecPerKm, eloDelta
  withdrawnAt, lastUpdatedAt, gpsAccuracyMeters

missions/{missionId}/gpsPrivate/{uid}
  points: [{ latitude, longitude, accuracy, capturedAtMs }]
  updatedAt

_timeSync/{uid}
  serverTime
```

`gpsPrivate` é separado do placar: o trajeto não é público para outros corredores. A fila de escrita do Firestore no Android permite repetir envios quando a conexão volta; a rota completa permanece local para o treino, enquanto a competição envia uma janela limitada de evidência.

### Regras atuais

As regras validam autenticação, dono do documento, temporada, transições de jogador, progresso crescente, velocidade máxima plausível, evidência GPS e gravação idempotente de histórico. O servidor do Firestore (`request.time`) é usado nas transações e nos timestamps.

Em 22/11/2026, a leitura de salas e placar foi liberada para usuários autenticados porque o Firestore não conseguia provar, durante uma consulta por coleção, que o valor `state` do filtro era igual ao valor carregado dinamicamente em `users/{uid}`. O aplicativo continua filtrando as salas pela UF do perfil. GPS e escritas permanecem protegidos.

Depois de alterar regras:

```powershell
firebase deploy --only firestore:rules --project aisler-runner
```

Depois de alterar índices:

```powershell
firebase deploy --only firestore:indexes --project aisler-runner
```

Não cole chaves de conta de serviço em código, commit, issue ou chat. O segredo do robô existe somente em GitHub Actions como `FIREBASE_SERVICE_ACCOUNT`.

## 4. Robô de ranking (GitHub Actions)

O plano Spark não oferece processamento confiável por Cloud Functions. O robô substitui a atualização periódica do ranking:

- Código local: `HANDOFF/ranking-robot/ranking-robot/ranking-robot/`.
- Repositório: <https://github.com/nefkity189/elite-ranking-robot>.
- Workflow: `.github/workflows/ranking.yml`.
- Agendamento: `*/5 * * * *` (o GitHub pode atrasar execuções).
- Node: 20; dependência: `firebase-admin`.
- Segredo: `FIREBASE_SERVICE_ACCOUNT` em Settings → Secrets and variables → Actions.
- Execução manual pode usar `dry_run`; o agendamento grava normalmente.

O script `scripts/update-ranking.mjs` lê missões das últimas seis horas, espera pelo menos oito minutos após a largada, ignora provas com menos de dois corredores que largaram e processa quando todos terminaram/desistiram ou quando atingiu o limite de aproximadamente 3h10. O vencedor recebe 30 pontos; os demais concluídos recebem 20. O histórico `users/{uid}/competitionHistory/{missionId}` funciona como trava idempotente; `rankingProcessedAt` marca a prova processada.

O robô é um pós-processador. Ele não arbitra o primeiro vencedor no instante exato da chegada; essa decisão ocorre no cliente transacionando com `request.time`. O GitHub também não garante execução exatamente de cinco em cinco minutos.

## 5. Site e distribuição do APK

O site é estático em [HANDOFF/Site/site](../Site/site) e é publicado no Firebase Hosting do projeto `aisler-runner`. O site não armazena o APK no Firebase: no Spark, o APK deve permanecer no GitHub Releases.

Arquivos principais:

- `site/index.html` — página, detecção da versão e botão de download.
- `site/latest-release.json` — fallback sem API; atualmente aponta para `1.0.9`.
- `site/privacidade.html` e `site/termos.html` — páginas legais.
- `site/fonts/` e `site/assets/` — fontes e identidade visual locais.

O site consulta a API pública do GitHub e usa `latest-release.json` quando a API falha. O APK atual documentado é `elite-1.0.9.apk`, release `v1.0.9`, SHA-256:

```text
b7121bbd4d7771d812f7b870848d1d4a6f65ec94efb33b51bac1136f0fd328fb
```

Publicação do Hosting:

```powershell
firebase deploy --only hosting --project aisler-runner
```

O domínio gratuito principal é `elitesch.web.app`. Um domínio personalizado como `elite.sch.app` depende de DNS e propriedade do domínio; não é criado automaticamente pelo Firebase.

## 6. Gerar uma nova versão do APK

1. Alterar `versionCode` e `versionName` em `app/build.gradle.kts`.
2. Conferir o `applicationId`: `com.elite.sch.app`.
3. Usar a mesma keystore de release (`my-upload-key.jks`/`keystore.properties`). Se o certificado mudar, o Android não atualiza a instalação existente.
4. Compilar:

```powershell
./gradlew.bat assembleRelease
```

5. Conferir o APK em `app/build/outputs/apk/release/` e calcular SHA-256:

```powershell
Get-FileHash .\app\build\outputs\apk\release\app-release.apk -Algorithm SHA256
```

6. Criar no repositório `nefkity189/elite-android-releases` uma tag `v<versionName>` e anexar o arquivo. O site atual aceita o asset versionado `elite-<versão>.apk`; mantenha a URL registrada no `latest-release.json`.
7. Atualizar `HANDOFF/Site/site/latest-release.json` com versão, código, tamanho, SHA-256, `downloadUrl` e `releaseUrl`.
8. Publicar o Hosting.

O APK consulta a versão do Release na abertura, com limite de uma consulta por instalação a cada seis horas. Instalação fora da Play Store exige confirmação do Android; o site não instala silenciosamente.

Permissões relevantes declaradas no APK: localização fina e aproximada, serviço em primeiro plano de localização, notificações, vibração, `WAKE_LOCK` e inicialização após boot. O sistema pode exigir que a pessoa permita localização “sempre” e retire as restrições de bateria para que o GPS continue com a tela bloqueada.

## 7. Testes e diagnóstico

### Emulador/dispositivo

```powershell
adb devices
adb install -r .\app\build\outputs\apk\release\app-release.apk
adb logcat | Select-String 'com.elite.sch.app|CompetitionSync|OnlineCompetition'
```

Para uma sala real, verificar no Console Firestore:

1. o documento da missão tem `state`, `timeZone`, `startsAt`, `distanceKm`, `minPlayers` e `playerCount`;
2. o documento do usuário tem a UF correta e `seasonId` atual;
3. `players/{uid}` muda de `joined`/`ready` para `running` e depois `finished` ou abandono;
4. `gpsPrivate/{uid}` recebe apenas pontos válidos;
5. `competitionHistory` e os campos `elo*` aparecem após a execução do robô.

Erros comuns:

- `PERMISSION_DENIED`: conferir autenticação, regra publicada e campos obrigatórios da missão; fechar e reabrir o app após atualizar regras.
- `FAILED_PRECONDITION`: normalmente índice composto ausente; publicar `firestore.indexes.json`.
- salas de outra UF: conferir `users/{uid}.state`, `timeZone` e a consulta do app.
- resultado sem pontos: esperar a próxima execução do robô e conferir `lastScoredMissionId`, histórico e logs do workflow.
- atualização não instala: conferir assinatura, nome do asset, `versionCode` maior e URL em `latest-release.json`.

## 8. Limitações conhecidas e próximos cuidados

- O robô de cinco minutos é eventual; não substitui uma função de servidor em tempo real.
- O cálculo de pace e parte da distância ainda nasce no aparelho; as regras limitam valores absurdos, mas não transformam o cliente em uma fonte antifraude perfeita.
- O ranking só muda depois do robô confirmar uma chegada. O app deve mostrar estado provisório enquanto isso.
- O Firebase Spark tem cotas diárias de Firestore/Hosting. Evitar listeners de coleções inteiras e envios de GPS a cada segundo.
- A leitura autenticada de metadados do placar não expõe `gpsPrivate`, mas permite que um cliente autenticado consulte salas de outras UFs se tentar alterar a consulta manualmente.
- APK não deve ser colocado no Hosting ou no Cloud Storage do Firebase nesta arquitetura.
- Chaves privadas e arquivos JSON de conta de serviço nunca devem entrar no Git.

## 9. Arquivos de referência

- `app/src/LEIAME_FIREBASE_COMPETICAO.md` — histórico da integração do Firestore.
- `app/src/FIREBASE_SPARK_RASTREAMENTO.md` — rastreamento e limites do Spark.
- `app/src/AUDITORIA_COMPETICAO_ONLINE_2026-11-21.md` — auditoria do fluxo.
- `app/src/INCIDENTE_PROVA_2026-11-22.md` — incidente e correções da prova.
- `HANDOFF/Site/README.md` — operação detalhada do site.
- `HANDOFF/competicao-online/CORRIDA ONLINE 2/HANDOFF.md` — estados e especificação da competição.
- `HANDOFF/ranking-robot/leiame.txt` — decisão e limites do robô.
- `SECURITY.md` — cuidados de segurança do projeto.

O código do robô e o código do APK são projetos diferentes: o APK não importa o pacote Node do robô. A integração entre eles é o contrato dos documentos do Firestore (`missions`, `players`, `users` e `competitionHistory`).

## 10. Regra de manutenção

Antes de mudar qualquer parte online, seguir esta ordem: ler o código que escreve, ler a regra que autoriza, conferir o índice da consulta, testar com uma conta autenticada e confirmar o documento no Console Firestore. Depois atualizar este arquivo se mudar uma URL, coleção, regra, workflow, versão ou processo de publicação.

## 11. Login após o splash

A tela inicial pós-splash está em `app/src/main/java/com/elite/sch/app/ui/screens/LoginScreen.kt`. Ela replica o handoff `HANDOFF/TelaInicial Apos (SPLASH)/TelaInicial`: quatro páginas de apresentação, login Google, login por e-mail em bottom sheet e acesso sem conta. Os ícones usados pela tela ficam em `app/src/main/res/drawable/ic_login_topo.xml` e `app/src/main/res/drawable/ic_google_g.xml`.

`MainActivity` aguarda a restauração do Firebase Auth por até quatro segundos antes de mostrar o login. O estado de “continuar sem conta” fica em `LoginPrefs`, separado do `AccountStore`, para não ser apagado quando o usuário sai da conta. A tela só aparece quando não há usuário autenticado e ainda não foi dispensada.

Para validar visualmente: instalar `:app:installDebug`, limpar os dados somente do emulador, abrir o app e conceder localização/notificações quando o Android solicitar. A primeira execução mostra o login depois do splash; Google e e-mail continuam usando `MainViewModel`/`FirebaseAccountRepository` existentes.
