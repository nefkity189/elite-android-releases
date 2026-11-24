# Auditoria da competição online — 21/11/2026

## Conclusão

**Bloqueada para competição pública com resultados oficiais. Avaliação qualitativa: 35/100.** A nota expressa prontidão de engenharia, não uma probabilidade de falha. Existem falhas comprovadas na saída, relógio, largada, chegada e ausência de processamento oficial de pontos. O Firebase atualmente valida algumas escritas; ele ainda não governa todo o ciclo da prova.

Esta entrega é uma auditoria: não modifica a lógica do aplicativo, não publica regras, não altera contas ou pontuações reais e não muda o plano de faturamento.

## Evidências e limites

- Código Kotlin atual, Manifest, regras, índices, configuração de publicação e funções locais revisados. O checkout contém muitas alterações não versionadas; esta auditoria não comprova que o APK instalado ou a versão GitHub 1.0.3 contenha exatamente este código.
- Console do projeto `aisler-runner`: plano Spark confirmado. As regras publicadas foram inspecionadas nos trechos de usuários, missões, participantes e GPS; coincidem com os contratos locais analisados. Não foi calculado hash integral entre arquivo e versão remota.
- `firebase functions:list --project aisler-runner --json`: sucesso com `result: []`. Nenhuma Cloud Function implantada foi retornada.
- `firebase firestore:indexes --project aisler-runner`: índices de missões por distância/horário e ranking por temporada/ELO e temporada/UF/ELO presentes. A existência de índices não significa que os fluxos estejam corretos.
- Gradle: 12 testes executados, 8 passaram e 4 falharam, selecionando `GpsSessionTest` e `RunStartCountdownTest`. As quatro falhas pertencem ao GPS. [Relatório Gradle](../build/reports/tests/testDebugUnitTest/index.html).
- Firestore Emulator isolado, projeto `demo-elite-audit`, carregando explicitamente as regras locais: cinco comportamentos reproduzidos, detalhados abaixo. Nenhum dado de teste foi enviado ao projeto real.
- Não houve corrida física com dois aparelhos, teste de bateria no Poco, simulação completa de troca de conta/reinício, auditoria integral de IAM, OAuth/SHA ou verificação remota de enforcement do App Check. Esses itens permanecem pendentes, não aprovados implicitamente.

### Convenções

- **P0**: bloqueia disputa confiável; corrigir antes de novo piloto competitivo.
- **P1**: risco alto de perda, injustiça, inconsistência ou indisponibilidade.
- **P2**: manutenção, experiência e operação.
- **Confirmado**: observado no código/configuração ou reproduzido em teste. Não significa reprodução física no celular.
- **Risco**: consequência plausível cuja frequência exige teste de integração/aparelho.

## 1. Relógio, ingresso, sala e largada

### A01 — P0 — Sincronização de relógio é negada pelas regras — reproduzido

[ServerTimeSync](main/java/com/elite/sch/app/data/competition/ServerTimeSync.kt) escreve `_timeSync/offset`, mas [firestore.rules](../../firestore.rules) não autoriza essa coleção. O emulador negou a operação. A exceção é escondida e retorna zero; `nowSync()` passa a usar o relógio do celular sem sinalizar que não é confiável.

**Correção:** fornecer uma fonte autorizada e isolada de tempo, medir incerteza/latência, exigir amostra recente para ingresso e largada e nunca tratar falha como sincronização bem-sucedida. Não basta liberar escrita pública no documento global.

### A02 — P0 — Primeira chamada de `now()` devolve offset, não horário — confirmado

No mesmo arquivo, `now()` sem cache retorna `sync()`, cujo resultado é apenas o deslocamento. Com a falha atual, retorna `0`, interpretado como época Unix por ingresso/saída. Com sincronização funcionando, ainda retorna um deslocamento pequeno, não a data atual.

**Efeito:** escolha incorreta entre excluir inscrição e registrar abandono; aplicativo considera prova passada como futura e recebe `PERMISSION_DENIED`. **Correção:** contrato único de instante absoluto; testes com primeiro acesso, falha e relógio ±30 minutos.

### A03 — P1 — Offset é vulnerável a mudança do relógio e concorrência — risco confirmado na estrutura

`nowSync()` usa `System.currentTimeMillis()` a cada chamada. Mudar a hora após sincronização muda a largada. A escrita usa um único documento para todos os usuários; uma futura liberação desse documento permitiria interferência entre amostras. Não há prazo de espera offline, e a medição não compensa ida/volta.

**Correção:** ancorar uma amostra validada em `elapsedRealtime`, expirar a amostra e usar documento por usuário/requisição ou endpoint apropriado. Horário exibido e decisão de negócio devem ser coisas distintas.

### A04 — P0 — Saída depois do horário usa campo incompatível — reproduzido

[OnlineCompetitionRepository](main/java/com/elite/sch/app/data/competition/OnlineCompetitionRepository.kt), `leaveMission`, grava `abandonedAt`; regras e leitura de participantes usam `withdrawnAt`. O emulador negou `abandonedAt`.

**Correção:** escolher um contrato único de abandono, migrar dados existentes se necessário e usar a mesma nomenclatura na escrita, leitura, histórico e regras. Testar antes/depois do horário e repetição da operação.

### A05 — P0 — Contagem acaba sem iniciar o rastreamento — confirmado

[CompetitionFlowScreen](main/java/com/elite/sch/app/ui/screens/competition/CompetitionFlowScreen.kt), efeito da contagem, chama `startOnlineProgressSync` diretamente. [CompetitionSync](main/java/com/elite/sch/app/data/competition/CompetitionSync.kt) exige serviço já rodando. O caminho automático não inicia o serviço. Além disso, grava `countdownMission` antes de saber se a largada foi bem-sucedida, impedindo nova tentativa automática.

**Correção:** preparar serviço/GPS antes da largada e iniciar contagem/medição com um único estado confirmado; registrar tentativa concluída somente após sucesso. Entrar na sala não pode depender de uma corrida normal previamente aberta.

### A06 — P0 — Botão manual inicia serviço e lê seu estado imediatamente — confirmado

`onRace` envia `ACTION_START` e imediatamente chama a sincronização, sem aguardar o serviço processar a intenção. O serviço acrescenta sua própria contagem de três segundos. A intenção não fornece identidade de sessão e usa `workoutId=0`/modalidade 5K por padrão, inclusive em 2,5K/10K.

**Correção:** comando explícito de competição com `missionId`, UID e sessão única; aguardar estado preparado. Evitar contagem duplicada e redefinição da distância por toques repetidos.

### A07 — P1 — Abrir/retomar sala depois da largada não retoma automaticamente — confirmado

O efeito retorna se `initialRemaining <= 0`. A confirmação exige dois `readyAt` com idade até 45 s, mas a presença só é renovada na tela ROOM. Quando o primeiro usuário entra em RACE, deixa de renovar presença; um colega atrasado pode ficar bloqueado. Atualizações da lista de resultados reiniciam o efeito da contagem.

**Correção:** congelar no servidor a lista admitida e a decisão de início; presença pré-largada não deve ser pré-requisito contínuo para recuperar uma prova em andamento.

### A08 — P0 — Não existe estado oficial de ciclo da missão — confirmado

O aplicativo deriva LIVE/FINISHED de horário, `playerCount` e lista local. As regras aceitam progresso se a contagem de inscritos for suficiente, sem exigir presença nem um início autorizado. Não há `startedAt`, lista congelada, cancelamento, limite de duração ou encerramento oficial de disputa.

**Correção:** estados persistidos `scheduled → running/cancelled → completed`, com transições atômicas, participantes elegíveis e regra explícita para atrasos. Não encerrar a coleta dos demais atletas ao primeiro vencedor; definir vencedor e fechamento da prova separadamente.

### A09 — P1 — Desistir na tela da prova não registra desistência — confirmado

`onDesist` apenas limpa `CompetitionSync` e volta à sala. O jogador continua inscrito, o serviço continua podendo rodar e uma retomada pode recalcular a base de distância. Já `confirmAbandonMission` para o serviço antes da confirmação remota: falha de rede/regras deixa o atleta parado mas ainda inscrito.

**Correção:** uma única operação de desistência com estado pendente visível, confirmação persistida e reconciliação. Definir o comportamento offline sem prometer confirmação do servidor.

### A10 — P1 — Fuso exibido e virada do dia ainda são locais/inconsistentes — confirmado

O repositório cria janelas e horários com `ZonedDateTime.now(SEASON_ZONE_ID)` e `LocalDate.now`, sem o relógio sincronizado. `toDemoMission` apresenta São Paulo em vez do fuso do dispositivo. A janela da consulta é calculada ao criar o listener e não é renovada automaticamente na virada do dia; status também depende de novos snapshots.

**Correção:** armazenar instantes UTC e um calendário oficial; mostrar em `ZoneId.systemDefault()`, mantendo o mesmo instante para todos. Recriar consultas na mudança do dia oficial e reavaliar status com fonte confiável. São Paulo 12:40 e MS 11:40 devem apontar à mesma largada.

### A11 — P1 — Agenda pode ser criada fora do calendário e duplicada — confirmado nas regras

As regras permitem que qualquer autenticado crie missão com data futura e distância válida; não validam ID canônico, hora cheia, intervalo 02h–21h ou unicidade de horário/distância. IDs antigos coexistem com novos `_utc`; a leitura apenas evita gerar um placeholder adicional, não elimina dois documentos reais duplicados.

**Correção:** contrato canônico e calendário verificável; estratégia explícita para IDs legados e uma única inscrição ativa por atleta.

## 2. Chegada, vencedor e proteção da disputa

### A12 — P0 — Chegada é gravada duas vezes; a segunda é rejeitada — reproduzido

[CompetitionSync](main/java/com/elite/sch/app/data/competition/CompetitionSync.kt) grava `finishedAt`. No sucesso, o callback de [CompetitionDemoState](main/java/com/elite/sch/app/ui/competition/CompetitionDemoState.kt) chama `markPlayerFinished` novamente, agora com distância/tempo brutos do treino. As regras bloqueiam atualização de jogador já finalizado. O teste reproduziu a rejeição da segunda escrita.

**Correção:** uma única chegada idempotente; callback apenas atualiza a interface. Reconhecer uma chegada já confirmada como sucesso, sem tentar sobrescrever o resultado.

### A13 — P0 — Falha da segunda gravação pode derrubar o fluxo e resultado não navega — confirmado

A segunda gravação está em `viewModelScope.launch` sem tratamento da exceção. `MissionRaceScreen` recebe `onCompleted`, mas não o invoca; o callback só altera `raceFinished`, sem efeito de navegação observando esse campo. O atleta pode alcançar a meta e continuar na tela da corrida.

**Correção:** fluxo de estado observado para `finishPending/finished/rejected`, erro tratado e navegação a partir de resultado confirmado, inclusive depois de recriação da tela.

### A14 — P0 — É possível enviar chegada inventada sem nenhum GPS — reproduzido

No emulador, um participante autenticado enviou de uma só vez 2,5 km em 400 s, com missão iniciada havia 10 min e contagem 2. Foi aceito sem `readyAt` e sem documento GPS. O limite de 7 m/s restringe valores absurdos, mas não prova que houve corrida.

**Correção:** separar progresso declarado de resultado validado. Processamento confiável precisa avaliar evidências e elegibilidade fora do controle do cliente. App Check e filtro de mock ajudam, mas não provam movimento real.

### A15 — P0 — Vencedor atual depende de entrega de rede — confirmado no desenho

`finishedAt` é `serverTimestamp` de recebimento; resultados são ordenados por ele no cliente. Quem cruza primeiro offline pode chegar ao banco depois de outro corredor. O horário de captura do celular também não prova sozinho quem chegou primeiro. A conversão para `Date.time` perde precisão abaixo de milissegundo e não há desempate definido.

**Correção:** definir política pública: resultado provisório, janela de reconciliação, tolerância de empate e critério para falta de sinal. Não prometer exatidão física de milissegundo com GPS e rede móvel. Um árbitro servidor deve persistir a classificação, e não cada celular calculá-la independentemente.

### A16 — P1 — Fila de chegada pode ficar presa após morte do processo — risco

`finishQueued` é salvo antes do commit; callbacks existem apenas na memória. Após reinício, o sinalizador pode impedir novos envios sem reconciliação com o documento remoto. Falha no instante entre gravar flag e enfileirar commit também é possível.

**Correção:** outbox durável com ID de operação e estados pendente/confirmado/rejeitado, reconciliada no início do serviço. Callback não pode ser a única fonte de conclusão.

### A17 — P1 — Pausa, atraso e múltiplos aparelhos não têm política única — confirmado

O tempo enviado subtrai pausas e usa uma base capturada quando a sincronização começa, não necessariamente a largada oficial. A mesma conta em dois aparelhos escreve o mesmo participante, sem lease/sessionId. Regressões serão rejeitadas pelas regras, mas os aparelhos podem ficar em estados diferentes.

**Correção:** tempo oficial desde a largada autorizada, política de pausa competitiva e um único produtor por participação; recuperação deve assumir ou rejeitar uma sessão com identidade explícita.

## 3. GPS, serviço, persistência e comunicação

### A18 — P1 — Primeiros 10 segundos de GPS são descartados — confirmado

[GpsSession](main/java/com/elite/sch/app/tracker/GpsSession.kt) reinicia aquecimento após a contagem e rejeita pontos por 10 s. O corredor já pode estar se movendo, perdendo a distância inicial; o próximo ponto vira apenas âncora.

**Correção:** preparar precisão antes da largada e capturar a âncora no início oficial. Não simplesmente eliminar filtro de aquecimento sem medir o erro.

### A19 — P1 — Filtro pode subestimar curvas e perder trechos — confirmado; magnitude pendente

Deslocamento mínimo usa a precisão inteira dos pontos (até 20 m), ligando pontos por reta; curvas ficam subestimadas. Intervalos acima de 30 s adicionam zero. Quatro testes antigos falharam porque suas expectativas de aceitação/intervalos não acompanham o filtro atual.

**Correção:** testar trajetos reais de referência, parado, curvas e túneis; alinhar especificação e testes. Não alterar as expectativas apenas para tornar a suíte verde.

### A20 — P1 — Segundo plano tem base correta, mas não garantia física — risco a validar

[RunTrackingService](main/java/com/elite/sch/app/service/RunTrackingService.kt) usa Foreground Service de localização, notificação, relógio monotônico e `START_STICKY`. São pontos positivos. Porém presença/contagem continuam na UI, não há prova de comportamento com tela bloqueada/Doze/Poco, e reinício tem restrições do Android. Permissão WAKE_LOCK declarada, por si só, não é aquisição de wake lock nem garantia de sobrevivência.

**Correção:** preparar o serviço enquanto o app está visível, tornar largada/estado independentes da tela e medir bloqueio de tela, economia de bateria, permissão revogada e morte do processo no aparelho real. Não adicionar permissões ou wake locks indiscriminadamente.

### A21 — P1 — Recuperação da corrida não recupera toda a competição — confirmado

[ActiveRunEntity](main/java/com/elite/sch/app/data/local/entities/ActiveRunEntity.kt) não contém UID/missionId/estado de chegada. O serviço procura a primeira corrida local recente por modalidade. O ViewModel limpa `CompetitionSync` no primeiro evento de autenticação ao ser recriado, mesmo que o UID seja o mesmo de uma sessão persistida.

**Correção:** sessão única durável por atleta/prova; distinguir restaurar a mesma conta de trocar conta. Restaurar a competição e o serviço a partir da mesma fonte.

### A22 — P1 — Destruição do serviço não cancela todo o escopo — confirmado

`onDestroy` cancela ticker, mas não `serviceScope` nem explicitamente `countdownJob`. Corrotinas de contagem/persistência podem sobreviver ao ciclo do serviço. `stopRun` não remove ali o registro ativo; é preciso validar o caminho externo de limpeza e risco de recuperação indevida.

**Correção:** cancelamento estruturado e estado terminal persistido, sem permitir que uma sessão abandonada seja recuperada como ativa.

### A23 — P1 — Offline é fila do SDK, não protocolo completo de prova — confirmado

O SDK pode persistir escritas, mas o código não inspeciona `isFromCache`/`hasPendingWrites` nem mantém sequência/ack por participação. Snapshot local pode aparecer como online; erro em listener fecha o fluxo sem política explícita de reanexação. Ingresso/saída em transação não funcionam offline.

**Correção:** distinguir local, pendente e confirmado; preservar tentativas com identidade; retries de falhas transitórias e mensagem de falhas definitivas. Adotar limite de validade para eventos reenviados após encerramento.

### A24 — P1 — Progresso remoto dos adversários não está ligado ao placar — confirmado

`OnlineMissionResult` não carrega distância, pace nem último recebimento. `MissionRaceScreen` apresenta apenas o usuário usando dados locais, embora o título diga “Dados do servidor”. Não é um placar remoto ao vivo de todos os corredores.

**Correção:** DTO com progresso/pace/tempo/status/atualização, listener apenas da prova aberta e indicação de sinal atrasado para cada participante.

### A25 — P1 — GPS remoto perde evidência anterior e não é validado — confirmado

Só os últimos 10 pontos aceitos são sobrescritos em `gpsPrivate`. Regras limitam o tamanho da lista, sem validar coordenadas/tipos/ordem ou vincular distância à geometria. A gravação é permitida até após retirada se o documento do participante ainda existir.

**Correção:** definir retenção mínima privada e evidência necessária para revisão, validar formato e estado. Evitar armazenar rota indefinidamente; acesso privado atual é positivo, mas não basta como antitrapaça.

### A26 — P1 — Falta confirmação de GPS realmente utilizável antes da largada — confirmado

`CompetitionSync.start` valida `isGpsRequested`, não permissão precisa, fix aceito ou idade da localização. O serviço pode iniciar sem localização autorizada; a intenção de usar GPS permanece true. `requestLocationUpdates` não trata explicitamente falhas assíncronas da Task.

**Correção:** pré-largada com estado de permissão, localização ativada, precisão/idade e erro acionável. Uma pessoa parada deve enviar tempo; uma pessoa sem localização deve ser informada de que sua distância não é válida.

### A27 — P1 — Chamadas antigas podem afetar nova conta/prova — risco

A troca de UID cancela listeners e limpa estado, mas operações já disparadas e callbacks do SDK podem concluir depois. Callback de chegada fica em singleton e não é limpo em todos os caminhos de conta/destruição. `onDone` pode selecionar uma missão da operação anterior.

**Correção:** token de geração por UID/sessão em todas as respostas e limpeza de callbacks. Testar troca durante inscrição, envio pendente e chegada.

## 4. Ranking, histórico e integridade de usuários

### A28 — P0 — Não existe processador implantado de pontuação oficial — confirmado

As regras bloqueiam alterações de ELO pelo cliente, o que é correto. Entretanto, [functions/index.js](../../functions/index.js) contém apenas temporada/limpeza, sem cálculo de ELO por chegada, e a lista remota de funções está vazia. Terminar uma prova não tem fluxo implementado de concessão oficial de pontos.

**Correção:** resultado validado → concessão idempotente de pontos → ranking da temporada. Uma prova jamais pode pontuar duas vezes. Não liberar edição direta de ELO para contornar o problema.

### A29 — P1 — Conquistas não são sincronizadas e podem ser autoatribuídas — reproduzido parcialmente

Tela usa lista vazia; não há carregamento real de conquistas. As regras permitem ao dono criar/alterar qualquer conquista. Emulador aceitou conquista arbitrária.

**Correção:** separar conquistas locais informativas de recompensas oficiais; conceder oficiais via resultado validado e servir a coleção na UI.

### A30 — P1 — Ranking por distância/histórico e posição além do top 30 incompletos — confirmado

Consulta sempre ordena `elo`, limitada a 30. `setRankingDistance`/`setRankingHistory` apenas mudam estado. A posição do usuário fora dos 30 não é obtida. Perfil individual tampouco confere `seasonId`, podendo exibir saldo antigo enquanto ranking filtra temporada nova.

**Correção:** escopos reais, índices correspondentes, paginação/desempate e posição autoritativa ou claramente indisponível. Separar totais de temporada de histórico.

### A31 — P1 — UF e identidade na inscrição não refletem o perfil — confirmado

Ingresso exige UF do documento de usuário, mas `newPlayerData` grava `state=""` e nome do Firebase Auth, não o perfil consultado. Login também sobrescreve o nome de Firestore pelo displayName do provedor.

**Correção:** origem única do perfil e snapshot da UF/nome no início da prova. Definir se mudança de UF pode alterar ranking durante temporada para evitar troca oportunista.

### A32 — P1 — Ligas são constantes no aplicativo — confirmado

`leagueFor` tem limiares fixos; não usa `config/season.ligas`. Mudar faixas no console não atualiza essas telas automaticamente.

**Correção:** política versionada da temporada, validada antes de uso, com mesma versão aplicada ao resultado e à apresentação.

### A33 — P1 — Missão pode nunca ser considerada encerrada — confirmado

FINISHED exige que `results.size >= participants` e todos tenham chegada. Contagem inclui inscrições antigas, enquanto a leitura exclui `withdrawnAt`; basta uma desistência/ausência para impedir encerramento.

**Correção:** status terminal por participante (finished/withdrawn/dns/dnf) e fechamento explícito com prazo. Contagem de inscritos não deve substituir contagem de competidores ativos.

## 5. Firebase, limites, automações e operação

### A34 — P0 — Rotação anual e limpeza diária não estão executando — confirmado remoto

Funções locais não implantadas; Spark não executa essas Cloud Functions em produção. A documentação oficial exige Blaze para implantá-las. Isso corrige a expectativa anterior de que manter o código local já automatizaria o servidor.

**Correção no orçamento atual:** explicitar limitações, usar seleção de temporada por contrato verificável quando aplicável e operação administrativa controlada; não prometer cron autônomo no Spark. Um árbitro completo de backend dentro de Cloud Functions requer rever a restrição de plano, sem fazer upgrade nesta auditoria.

### A35 — P1 — Rotação anual local não é idempotente — confirmado; ainda não implantada

`rotateSeason` calcula próxima temporada como ativa+1. Uma repetição após troca pode avançar outro ano; uma falha durante paginação deixa usuários de temporadas diferentes. Atualiza configuração ativa antes de terminar a migração, não arquiva pontos anteriores e o comentário de criar temporadas futuras não corresponde à implementação. O template herda `source.name`, podendo manter nome do ano anterior; existe intervalo entre 00:00 e 00:01 a definir.

**Correção:** chave de execução por ano oficial, lock/idempotência, temporada como namespace de pontuação e progresso de migração recuperável. Histórico deve ser preservado antes de qualquer reset.

### A36 — P1 — Limpeza local pode apagar prova ativa e deixar GPS órfão — confirmado; ainda não implantada

`purgeExpiredMissions` filtra só data de início, sem status final; pode arquivar/apagar corrida atravessando meia-noite. Apaga players e missão, mas não `gpsPrivate`; excluir documento pai não exclui subcoleções. Histórico não armazena classificação oficial nem abandono e não há leitor completo correspondente na competição.

**Correção:** encerrar/reconciliar antes de arquivar; preservar resultado completo; retenção privada e deleção explícita de subcoleções, paginada e recuperável.

### A37 — P1 — Cota gratuita acaba rapidamente com o padrão atual — cálculo a partir do código

Envio de progresso + janela GPS = 2 gravações/7 s, aproximadamente 514 gravações em 30 min por corredor. Cerca de 39 participações desse tamanho já ultrapassam 20 mil gravações, sem contar presença, inscrições e perfil. Sobrescrever documento continua consumindo gravações.

`observeMissions` abre listeners dos participantes de TODAS as missões da distância no dia. Com 20 espectadores/corredores escutando 20 participantes por 30 min, a ordem de grandeza é 20×20×257 ≈ 102.800 notificações de documentos, antes de outras leituras. É estimativa; tráfego efetivo depende de cache, tempo aberto e listeners ativos.

**Correção:** lista pública resumida, listener detalhado apenas da sala aberta, frequência/adaptação e orçamento por usuário. Sem limite de vagas não significa capacidade técnica ilimitada. Cota diária oficial: 50 mil leituras e 20 mil gravações; reset por horário do Pacífico, não meia-noite do Brasil.

### A38 — P1 — Limpeza de listeners e tratamento de erros incompletos — confirmado/risco

No repositório, remoção de listeners de players fica após `awaitClose`, fora do bloco de limpeza; cancelamento pode impedir essa linha. Erros dos players são apenas logados e a UI pode permanecer “carregando”. Qualquer sucesso de outro fluxo pode limpar a única mensagem global de erro.

**Correção:** todos os listeners no bloco `awaitClose`, estados de erro por operação e reanexação controlada. Medir leituras após trocar distância/conta repetidamente.

### A39 — P1 — App Check de release não está inicializado — confirmado no código; enforcement remoto pendente

[EliteApplication](main/java/com/elite/sch/app/EliteApplication.kt) configura apenas provedor debug. Não habilitar enforcement às cegas: builds de release podem perder acesso. Ainda com atestação, dados GPS do cliente não se tornam prova incontestável. Regras de criação de usuário e presença também precisam validação de esquema/tamanho e restrições de abuso.

**Correção:** escolher provedor compatível com distribuição fora da Play Store, medir tokens válidos em release, então avaliar enforcement. Definir limites e validar tipos sem quebrar usuários existentes.

### A40 — P2 — Publicações e diagnóstico podem divergir — confirmado/risco

[firebase.json](../../firebase.json) não declara alvo `elitesch`; o novo site foi criado por clone, sem configuração local para atualizar ambos. App instalado/GitHub/código local podem divergir. Não foi encontrada integração Crashlytics no módulo inspecionado; logs locais não substituem rastreamento de falhas em campo.

**Correção:** publicação explícita por site, manifest de versão verificado, build identificável nos registros e eventos de negócio com missionId/sessionId/UID protegido. Diagnóstico deve registrar erro e etapa, sem divulgar rotas privadas. Manter rollback de APK e regras compatíveis com clientes anteriores.

## Testes executados e resultados

### Gradle

Comando: `gradlew.bat --no-daemon :app:testDebugUnitTest --tests com.elite.sch.app.GpsSessionTest --tests com.elite.sch.app.service.RunStartCountdownTest --console=plain`.

12 testes, 4 falhas: `contadores batem com o total recebido`, `lote entregue com atraso eh aceito por inteiro`, `lote com 3 localizacoes eh processado por inteiro`, `maior intervalo sem ponto eh registrado`. São incompatibilidades reais entre especificação de testes e filtro atual; o resultado não demonstra sozinho qual filtro é ideal em campo.

### Firestore Emulator

Script temporário com `@firebase/rules-unit-testing` já instalado; projeto demo sem produção. Carregou o conteúdo de [firestore.rules](../../firestore.rules) via `initializeTestEnvironment` antes das verificações, apesar do aviso inicial de configuração padrão do processo emulador.

1. `_timeSync/offset`: negado.
2. `players/...abandonedAt`: negado.
3. Chegada 2,5 km/400 s sem evidência GPS/presença: aceita.
4. Segunda gravação de chegada: negada.
5. Autoconcessão de conquista: aceita.

## Contrato recomendado para correção

1. **Calendário:** `startsAt` UTC e `seasonId` explícitos; exibição local; servidor decide validade pelo seu relógio. Offset no app é estimativa para a animação, jamais autorização.
2. **Participação:** uma sessão por UID/prova, ingresso/saída idempotentes, lista elegível congelada e abandono terminal.
3. **Largada:** preparar GPS/serviço antes; alinhar a contagem visual ao instante oficial; recuperar a sessão após tela fechada sem reiniciar contagem/distância.
4. **Progresso:** dados locais duráveis, sequência e confirmação, distância monotônica, relógio monotônico e origem de dados visível; somente sala atual escutada.
5. **Chegada:** intenção persistente e idempotente; resultado provisório enquanto falta confirmação/evidência; regra explícita de offline/empate. Primeiro vencedor não impede demais atletas de terminar.
6. **Pontuação:** somente por resultado validado, uma concessão por prova/atleta/temporada; histórico independente da limpeza das missões.
7. **Temporada:** políticas versionadas, histórico preservado, execução idempotente e monitorada. Não usar reset destrutivo como único modelo de temporadas.

No Spark é possível melhorar bastante contrato, regras, UX e consistência. **O desenho atual não entrega validação confiável de trajetos, arbitragem autônoma e cron administrativo apenas por existir um Firestore.** Sem backend confiável, tratar resultados competitivos como provisórios/recreativos. Não relaxar regras de ELO nem expor credenciais administrativas no APK para simular servidor.

## Matriz mínima de aceitação antes da próxima release

| Cenário | Critério de aprovação |
|---|---|
| Dois aparelhos em SP/MS | Mesma missão/instante; rótulos locais corretos |
| Relógio manual ±30 min; mudança durante sala | Entrada/saída/partida seguem decisão remota |
| 0, 1, 2 presentes; saída no último segundo | Uma decisão oficial, reproduzida nos dois aparelhos |
| Contagem com tela bloqueada/retomada | Serviço preparado; uma largada; distância sem reset |
| Tocar iniciar/sair várias vezes | Uma única operação efetiva e sem contadores negativos |
| 2,5K, 5K, 10K | Mesmas garantias em todas as distâncias |
| GPS parado, ruim, desligado, aproximação, mock | Sem distância artificial; motivo de bloqueio claro |
| Sem internet por 30 s e 5 min; chegada offline | Fila recuperável; resultado provisório; política justa |
| Morte de processo antes/depois do commit final | Mesmo resultado, sem duplicidade nem sessão presa |
| Troca de conta durante operação pendente | Nenhum dado/resultado aplicado à nova conta |
| Mesma conta em dois celulares | Uma sessão escritora ou transferência explícita |
| Participante abandona; outro termina | Encerramento e histórico corretos; nenhuma espera eterna |
| Chegadas simultâneas com latência diferente | Classificação persistida e desempate documentado |
| Ranking além de 30, por UF/distância/temporada | Dados reais e política consistente |
| Virada do dia/ano com app aberto | Consulta/temporada atualizam; histórico não some |
| Falha/repetição na rotação e arquivo | Recuperação idempotente, sem avanço duplo ou perda |
| Cota esgotada/permissão revogada | Mensagem clara e recuperação, sem declarar resultado falso |
| APK release atualizado e antigo | Compatibilidade com regras e caminho de atualização |

## Ordem de correção sugerida

1. A01–A06: contrato de tempo, saída e serviço na largada.
2. A08, A12–A17: estado oficial, chegada única, sessão e política de resultado.
3. A18–A27: recuperação, GPS, fila e placar real.
4. A28–A36: ranking, conquistas, temporadas e histórico dentro das limitações de infraestrutura escolhidas.
5. A37–A40: cota, listeners, App Check e publicação/observabilidade.
6. Executar matriz em dois aparelhos reais e regras no emulador; somente então publicar nova versão competitiva.

## Referências oficiais consultadas

- [Cotas Firestore](https://firebase.google.com/docs/firestore/quotas): limites gratuitos, reset pelo Pacífico e recursos que exigem faturamento.
- [Cloud Functions: implantação exige Blaze](https://firebase.google.com/docs/functions/get-started).
- [Firestore offline e metadados de cache](https://firebase.google.com/docs/firestore/manage-data/enable-offline).
- [Restrições Android ao iniciar Foreground Service em segundo plano](https://developer.android.com/develop/background-work/services/fgs/restrictions-bg-start).

Uma auditoria não prova ausência de todos os bugs. Os itens comprovados acima e os cenários pendentes tornam o trabalho rastreável e permitem avançar por correções verificáveis.
