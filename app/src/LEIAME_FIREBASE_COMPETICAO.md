# Guia de manutenção — Firebase e competição ELITE

Este documento registra a integração do aplicativo ELITE com Firebase e o estado conhecido da competição online. Use-o como ponto de partida para manutenção. Confirme o console e o código antes de fazer mudanças, pois a configuração remota pode mudar depois desta documentação.

## Atualização de rastreamento e proteção no Spark

Consulte [FIREBASE_SPARK_RASTREAMENTO.md](FIREBASE_SPARK_RASTREAMENTO.md) para as alterações de 21/11/2026, regras reforçadas, sincronização offline e limitações ainda existentes. Essa atualização prevalece sobre as descrições anteriores do envio pela tela.

## Estado atual

| Área | Estado conhecido |
|---|---|
| Projeto Firebase | `aisler-runner` |
| App Android registrado | `com.elite.sch.app` |
| Firestore | Banco `(default)`, região `southamerica-east1` (São Paulo) |
| Autenticação | Google e e-mail/senha usados pelo app; login Google foi verificado no telefone de depuração |
| Perfil e competição | App consulta e grava dados reais do Firestore quando autenticado |
| Regras e índices | Arquivos locais publicados no Firestore; verifique o console antes de alterar |
| Cloud Functions | Código preparado em `functions/index.js`, mas **não implantado**: o projeto estava no plano Spark e o deploy requer Blaze |
| Temporadas futuras | Modelos `2027` a `2030` foram criados manualmente na coleção `seasons`; ainda não há rotação automática ativa |
| Limpeza diária de missões | Função preparada, mas não ativa até implantar Cloud Functions |
| GPS | Distância, ritmo e tempo são enviados pelo cliente. Regras limitam valores óbvios, mas não comprovam que uma posição GPS é verdadeira |

O app não espelha automaticamente qualquer dado criado no console. Ele só recebe documentos que suas consultas/listeners leem, e apenas se o usuário estiver autenticado e as regras permitirem.

## Arquivos principais

| Arquivo | Responsabilidade |
|---|---|
| `app/google-services.json` | Identifica o app Android no projeto Firebase. Não publique cópias com credenciais ou dados de configuração em repositórios públicos sem revisar a política do projeto. |
| `app/src/main/java/com/elite/sch/app/account/AccountRepository.kt` | Login Firebase, restauração de sessão e sincronização do perfil em `users/{uid}`. |
| `app/src/main/java/com/elite/sch/app/account/AccountConfig.kt` | Exige login para a competição (`REQUIRE_LOGIN_FOR_COMPETITION = true`). |
| `app/src/main/java/com/elite/sch/app/data/competition/OnlineCompetitionRepository.kt` | Consultas em tempo real, criação/entrada/saída de sala, progresso de corrida e ranking. |
| `app/src/main/java/com/elite/sch/app/ui/competition/CompetitionDemoState.kt` | Estado e apresentação da competição alimentados pelo repositório online; não deve reintroduzir dados fictícios. |
| `app/src/main/java/com/elite/sch/app/ui/screens/competition/CompetitionFlowScreen.kt` | Fluxo/telas da competição, incluindo saída da sala e apresentação de erros de sincronização. |
| `app/src/main/java/com/elite/sch/app/ui/MainViewModel.kt` | Cria e conecta o repositório de conta Firebase à UI. |
| `app/src/main/java/com/elite/sch/app/EliteApplication.kt` | Inicialização Firebase e App Check de depuração para builds debug. |
| `firestore.rules` | Regras de acesso do Firestore. |
| `firestore.indexes.json` | Índices compostos necessários às consultas. |
| `firebase.json` | Associa regras, índices e código de Functions ao Firebase CLI. |
| `functions/index.js` | Rotação anual, replicação da política de temporadas e arquivamento/limpeza diária (ainda não implantados). |

## Login e perfil

- Firebase Authentication é a fonte de verdade da sessão. O cache local de conta serve para apresentar o estado do app, não substitui a autenticação.
- A competição exige sessão válida. Sem login, o app não deve apresentar salas online fictícias.
- No login, o app lê `config/season` e sincroniza `users/{uid}`. Para um perfil novo, grava nome, estado vazio, `avatarSeed`, `createdAt` do servidor, `isPremium=false`, elos zerados e `seasonId` da temporada ativa.
- Em um perfil existente, atualiza nome e `avatarSeed`, preservando estado e dados de ranking.
- Alterar UF grava o campo `state` no perfil do usuário autenticado.
- Login e sincronização de perfil são etapas distintas: se o Auth funcionar e a leitura/gravação do perfil falhar, a UI deve exibir erro e não tratar a sincronização como concluída.
- `deleteAccount()` atualmente encerra a sessão; não apaga a conta do Firebase Authentication nem os documentos. Não descreva essa ação como exclusão definitiva.

## Modelo de dados Firestore

```text
config/season
  Configuração da temporada ativa (id/ano, nome, fuso, datas, ligas e política).

seasons/{ano}
  Configuração/modelo de temporada; leitura pelo app, escrita administrativa/servidor.

users/{uid}
  Perfil, estado, temporada e ranking (elo, elo25, elo5, elo10).

users/{uid}/achievements/{id}
  Conquistas do próprio usuário.

users/{uid}/competitionHistory/{missionId}
  Histórico permanente por usuário; gravação prevista para a rotina de arquivamento no servidor.

missions/{missionId}
  distanceKm, startsAt, minPlayers e playerCount.

missions/{missionId}/players/{uid}
  Participante: name, state, joinedAt, progressKm, finishedAt, timeSec,
  paceSecPerKm, gpsAccuracyMeters, clientElapsedRealtimeMs e lastUpdatedAt,
  conforme os campos gravados pelo fluxo atual.
```

`config/season` é o documento ativo usado pelo app. As temporadas futuras não se tornam automaticamente ativas sem uma rotina de servidor implantada.

## Missões, sincronização e horários

- A tela consulta as missões do dia para a distância selecionada e acompanha alterações via listener do Firestore.
- Os horários disponíveis entre 02:00 e 21:00 são montados no app no fuso local do aparelho. Um horário futuro exibido pode ainda não ter documento no Firestore.
- O documento da missão e o documento do participante são criados quando o primeiro atleta entra. A criação/entrada usa transação para manter `playerCount` consistente com `players`.
- `minPlayers` é 2. A regra atual exige isso no documento, mas não define limite máximo de participantes.
- Sair antes do início remove o documento do atleta e decrementa `playerCount` na mesma transação. Após o horário de início, a regra impede a exclusão.
- A tela deve mostrar apenas missões relevantes do dia: horários futuros/disponíveis e missões encerradas que tenham participantes/resultados; missões passadas sem participantes não devem permanecer como salas ativas.
- A publicação de progresso é feita aproximadamente a cada 7 segundos durante uma corrida. São enviados distância, tempo decorrido e, quando disponíveis, ritmo e precisão GPS. `lastUpdatedAt`, `joinedAt` e `finishedAt` usam timestamps do servidor.
- A conclusão é sinalizada automaticamente ao alcançar a distância da prova; a interface oferece desistência/saída em vez de um botão manual para declarar vitória.
- O ranking lê `config/season` e consulta `users` pela temporada ativa, ordenando por `elo`, com limite de 30 e filtro opcional por estado.
- O app usa o fuso configurado/local para apresentar horários. O horário do celular pode variar por fuso; timestamps do Firestore são instantes absolutos. Para regras de início e tarefas de servidor, prefira timestamps e fuso explícitos, não concatenação de horário local ambíguo.

### Limite de integridade do GPS

As regras impedem algumas alterações inválidas (por exemplo, reduzir progresso ou ultrapassar a distância com uma margem), mas o cliente controla os dados de GPS e pode ser adulterado em um aparelho comprometido. `gpsAccuracyMeters` e `clientElapsedRealtimeMs` são dados informativos fornecidos pelo cliente, não prova criptográfica. Para validar trajetos, determinar vencedores com confiança ou atribuir elo protegido contra fraude, é necessária validação confiável no servidor (por exemplo, Cloud Functions/Cloud Run com desenho de validação e limites de frequência). Não prometa antifraude completo com as regras atuais.

## Regras e índices publicados

Resumo de `firestore.rules`:

- `config` e `seasons`: leitura autenticada; escrita de cliente bloqueada.
- `users`: leitura autenticada; criação apenas pelo próprio usuário, com elos zerados, premium falso e temporada correspondente à configuração. Atualização do próprio perfil limitada a nome, estado e avatar; o cliente não altera premium/ranking/temporada.
- `achievements`: proprietário pode ler e escrever.
- `competitionHistory`: proprietário pode ler; escrita de cliente bloqueada.
- `missions`: leitura autenticada; criação limitada às distâncias 2.5, 5 e 10 km, com horário futuro, mínimo 2 e contador inicial 1. Atualização do contador vinculada à criação/remoção do próprio participante.
- `players`: usuário só altera o próprio documento; progresso/tempo não podem diminuir; finalização precisa usar horário de servidor; saída permitida antes do início.

Índices compostos registrados em `firestore.indexes.json`:

1. `users`: `seasonId` crescente + `elo` decrescente.
2. `users`: `seasonId` crescente + `state` crescente + `elo` decrescente.
3. `missions`: `distanceKm` crescente + `startsAt` crescente.

Ao adicionar uma consulta composta, atualize o índice e as regras necessários juntos. Um erro de consulta geralmente inclui um link para criar o índice faltante.

## Temporadas e tarefas automáticas

O documento `config/season` foi configurado para a temporada 2026, com fuso `America/Sao_Paulo`; os modelos 2027–2030 foram criados manualmente. O app já usa o identificador da temporada para filtrar ranking.

`functions/index.js` contém:

- `replicateSeasonPolicy`: replica campos de política de `config/season` nos documentos de temporadas futuras quando a configuração muda.
- `rotateSeason`: agendada para 00:01 de 1º de janeiro em `America/Sao_Paulo`; ativa a próxima temporada e zera os campos Elo ativos dos usuários em páginas, preservando histórico.
- `purgeExpiredMissions`: agendada diariamente às 00:10 em `America/Sao_Paulo`; copia resultados para `competitionHistory` e remove jogadores/missões expirados.

**Essas rotinas são apenas código local e não estão implantadas.** A rotação e a limpeza não ocorrerão automaticamente no estado atual do projeto. O deploy de Cloud Functions foi bloqueado pelo plano Spark; antes de ativar, confirme o plano/custos, revise os efeitos de escrita em massa e faça um teste controlado. Após autorização administrativa para usar Blaze e custos correspondentes, o comando de implantação é:

```powershell
firebase deploy --project aisler-runner --only functions
```

Não execute o deploy sem confirmar a cobrança e revisar funções e índices. A escrita de política feita por `replicateSeasonPolicy` e o reset de elo anual também precisam ser verificados em um ambiente controlado antes da primeira execução real.

## Procedimentos de manutenção

### Alterar campos de perfil ou missão

1. Atualize o modelo de leitura/gravação em `OnlineCompetitionRepository.kt` ou `AccountRepository.kt`.
2. Atualize `firestore.rules` para permitir somente os campos e transições necessários.
3. Atualize índices se uma consulta nova exigir índice composto.
4. Considere compatibilidade com documentos antigos, que podem não ter o campo novo.
5. Compile o app; depois publique regras/índices quando estiver pronto:

```powershell
firebase deploy --project aisler-runner --only firestore:rules,firestore:indexes
```

### Compilar o aplicativo

```powershell
./gradlew.bat :app:assembleDebug --no-configuration-cache --console=plain
```

Para conferir sintaxe das funções sem implantá-las:

```powershell
node --check functions/index.js
```

### Diagnóstico rápido

- **Lista vazia:** confirme que Firebase Auth está conectado, `config/season` existe e contém `id` ou `year`, a distância selecionada é compatível e a consulta cobre o dia/fuso esperado.
- **Perfil não sincroniza:** confirme rede, usuário atual no Auth, leitura de `config/season`, regras publicadas e acesso ao documento `users/{uid}`. A sessão Auth pode existir mesmo quando a sincronização do Firestore falha.
- **Erro ao entrar/sair:** confira `playerCount` e `missions/{id}/players/{uid}` juntos; a operação depende de transação e das regras `existsAfter`.
- **Ranking vazio:** confira `seasonId` dos documentos de usuários e o id ativo em `config/season`; usuários antigos podem precisar de migração administrativa segura.
- **Erro “index required”:** crie o índice indicado e registre-o em `firestore.indexes.json` para manter o ambiente reproduzível.
- **Mudança feita no Console não aparece:** confira se a tela realmente consulta aquele caminho e mantém listener ativo; o Console não sincroniza todas as coleções para o app automaticamente.
- Para investigar o dispositivo, use Logcat e a mensagem de sincronização apresentada pela tela. Não registre senha, token OAuth, token App Check ou conteúdo de arquivos de credenciais em logs/documentação.

## Verificações concluídas nesta manutenção

- As regras Firestore foram compiladas/publicadas e o arquivo de índices foi implantado.
- A compilação Kotlin/debug do app passou e o build debug foi instalado no telefone de depuração.
- Login Google e leitura de perfil/temporada foram observados no telefone; a lista online exibiu horários reais gerados pelo fluxo.
- Não foi feito um novo ingresso em missão durante a verificação final; portanto, não trate uma gravação/saída nova como teste ponta a ponta confirmado.
- As Cloud Functions não foram implantadas. O estado delas deve ser reavaliado no Firebase antes de depender de qualquer automação.

## Segurança e dados sensíveis

Não inclua neste arquivo senhas, tokens, chaves privadas, URLs temporárias de autenticação ou tokens de depuração do App Check. Não coloque credenciais de conta de teste no código. O `google-services.json` é configuração do app Firebase, não uma chave privada de servidor; ainda assim, siga as regras de distribuição e restrição de APIs do projeto.
