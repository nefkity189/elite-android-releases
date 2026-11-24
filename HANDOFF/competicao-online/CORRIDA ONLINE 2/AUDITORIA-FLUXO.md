# Auditoria do fluxo da Competição Online (src_1.zip)

Base: leitura do código de `OnlineCompetitionRepository.kt`, `CompetitionDemoState.kt` (ViewModel), `CompetitionFlowScreen.kt`, `CompetitionSecondaryScreens.kt`, `CompetitionComponents.kt`, `RunTrackingService.kt`, `MainActivity.kt` e do seu `LEIAME_FIREBASE_COMPETICAO.md`.
**Não compilei nem rodei o app.** Tudo abaixo vem de leitura de código; onde digo "provável", é inferência. As regras do Firestore (`firestore.rules`) e o Gradle não estavam no zip, então não verifiquei as regras.

## Resumo
O Firebase está ligado (login, perfil, missões, entrada/saída de sala), mas **a prova em si ainda não funciona de ponta a ponta**: o GPS nunca é iniciado pela competição, não existe largada sincronizada, "Desistir" não desiste de verdade, o resultado é todo fixo e nenhum código altera o elo. Quase todo o resto (ranking, temporada, conquistas, avisos) mostra dados de exemplo escritos no código.

Suas 4 anotações, em uma linha cada:
1. **Desistir:** o botão existe (`CompetitionFlowScreen.kt:562`), mas só para o envio de progresso e volta para a sala. Não para o GPS, não grava abandono e a prova continua aberta. Veja C4.
2. **Emblemas animados:** hoje `LeagueBadge` é um escudo estático de 22 a 64dp, sem animação. Design novo em `screen.html`.
3. **Animação de missão cumprida:** não existe. A tela vai direto para o resultado. Design novo em `screen.html`.
4. **Lacunas no fluxo:** 8 problemas críticos, 18 altos e 16 telas/estados faltando, listados abaixo.

## Críticos (a prova não funciona sem isso)
| # | Problema | Onde | Efeito |
|---|---|---|---|
| C1 | **A competição nunca inicia o GPS.** O único `ACTION_START` do `RunTrackingService` está em `MainActivity.kt:1224` (treino). `onRace` só chama `startOnlineProgressSync` e muda de página. | `CompetitionFlowScreen.kt:179-183` | Distância fica 0, a prova nunca conclui e o progresso não é enviado (o laço só envia se `run.isRunning`). |
| C2 | **Sala sem relógio.** "COMEÇA EM" mostra `00:00` ou `18:42` fixos; "Entrar na prova" libera assim que há 2 pessoas, mesmo horas antes do horário. Não há largada sincronizada nem transição automática para "não iniciada" quando o horário passa sem o mínimo. | `CompetitionRoomScreen` | Cada pessoa começa quando quer; o "primeiro a concluir" não é comparável. |
| C3 | **Não dá para voltar a uma prova em andamento.** `joinMission` falha com "já começou" **antes** de checar se o jogador já está na sala. Voltar (back) na prova leva ao Hub sem desistir, e o GPS e o envio continuam. | `OnlineCompetitionRepository.kt:~203`; `BackHandler` | Quem sai da tela perde a prova sem saber; quem fecha e reabre o app não consegue retomar. |
| C4 | **Desistir quebrado.** `onDesist = { stopOnlineProgressSync(); go(ROOM) }`: não para o `RunTrackingService`, não grava `abandonedAt`/status no jogador, não pede confirmação, e na Sala "Sair da sala" falha após o início. O botão é o último item de uma `Column` sem rolagem, com relógio de 92sp; em tela menor ele pode ficar cortado (por isso você pode não tê-lo visto; não confirmei no aparelho). | `CompetitionFlowScreen.kt:204, 562` | Não há como sair de uma prova limpa. |
| C5 | **Chegada detectada pela tela.** `LaunchedEffect(complete)` dentro de `MissionRaceScreen` chama `markRaceFinished`. Se a tela não estiver na composição (usuário no Hub, app em segundo plano), ninguém marca a chegada. O GPS também não é parado ao concluir. Além disso, `markRaceFinished` grava `coerceAtLeast(distância da prova)`, ou seja, sempre a distância cheia. | `CompetitionFlowScreen.kt:~525`; `CompetitionDemoState.kt` | Chegada perdida; GPS gravando depois do fim. A detecção e o envio precisam sair da tela (ViewModel/serviço). |
| C6 | **Resultado 100% fixo.** "2º LUGAR", "Missão 5K · 13:00", avatar "RN", conquistas "Primeira missão cumprida" e "Pódio", ELO/PACE/GLOBAL/ESTADO = "—". A posição do usuário nunca é calculada. | `MissionResultScreen` | O resultado nunca reflete a prova real. |
| C7 | **O elo nunca muda.** Nenhum código escreve `elo`/`eloDelta` (só são criados com 0). Segundo seu LEIAME, as regras bloqueiam o cliente e as Cloud Functions não estão implantadas (plano Spark). | `AccountRepository.kt:118`; LEIAME | Ranking e ligas ficam em 0 para sempre. Precisa de decisão (seção "Decisões"). |
| C8 | **Status da missão incorreto.** `FINISHED` quando qualquer um terminou (`results.any { finishedAt != null }`). Quem ainda corre vê "ENCERRADA". Não existe "encerrada para todos" (todos terminaram/desistiram ou passou o tempo máximo). | `toDemoMission()` | Hub e resultado enganam quem ainda está correndo. |

## Altos
| # | Problema | Observação |
|---|---|---|
| A1 | **Fuso horário.** Horários e IDs (`5_20261121_1300`) usam o fuso do aparelho; o `config/season` tem `America/Sao_Paulo`. Dois fusos criam missões diferentes para o mesmo instante. | Usar um fuso único (o da temporada) para ids e exibição. |
| A2 | **Relógio do aparelho decide "já começou"** (`java.util.Date()`). | Usar a hora do servidor (offset obtido de um `serverTimestamp`). |
| A3 | **Virada de dia.** O listener do Hub fixa "hoje" no momento em que é criado e não se renova à meia-noite. | |
| A4 | **Placar ao vivo só mostra "Você".** Diz "Dados do servidor", mas não lê os outros jogadores; "SUA POSIÇÃO" = "—". | Falta ouvir `players` da missão atual. |
| A5 | **Cota gratuita.** Envio a cada 7s = cerca de 257 escritas por corredor numa prova de 30 min. Cada escrita é lida por todo ouvinte; o Hub ouve `players` de **todas** as missões do dia. Estimativa: 6 corredores e 10 pessoas no Hub = ~1.500 escritas e ~25 mil leituras por missão. O plano gratuito tem 20 mil escritas e 50 mil leituras por dia. | Hub não deve ouvir `players`; envio a cada 15 a 20s e só se mudou >= 25 m; ouvir só a missão do usuário. |
| A6 | **Textos corrompidos** (caracteres `?`): "Falha na sincroniza??o", "SUA POSI??O", "Voc?", "O p?dio ser? exibido". | Problema de codificação; corrigir os literais. |
| A7 | **Ranking.** O filtro 2,5K/5K/10K não altera a consulta (os campos `elo25/elo5/elo10` existem, mas só `elo` é usado); "Geral histórico" não faz nada; `movement` é sempre "—"; faixas de promoção/rebaixamento são decorativas; divisor "Diamante I" fixo na posição 5. | |
| A8 | **Tabela de ligas inconsistente.** `leagueFor` usa Prata >= 800, Ouro >= 1200, Elite >= 1600; a tela Temporada mostra Ouro 1200 a 1799 e Diamante I 2800 a 2999; a barra de progresso divide por 400. | Uma única tabela (proposta abaixo), lida de `config/season`. |
| A9 | **Temporada fixa:** "12d 04h", "LIGA OURO II", barra 71%, "Faltam 58 pontos". | Ler `config/season` e o usuário. |
| A10 | **Conquistas fixas** (7/24, 3/5, "NOVA CONQUISTA: Pódio"). Nada desbloqueia nem grava em `users/{uid}/achievements`. | |
| A11 | **Avisos fixos.** Sem push nem alarme local de 10 e 5 min; sem pedido de `POST_NOTIFICATIONS` na competição. | Alarmes locais (grátis) em vez de push. |
| A12 | **Convite sem link.** A tela diz "O link leva direto para esta missão", mas não há App Link nem Hosting; a mensagem compartilhada é fixa ("5K às 13:00"). | |
| A13 | **Sala com dados de exemplo.** Mostra "Você" e "Competidor online"; não lista atletas reais; contador `0/∞`. | Listar `players` com nome/UF/avatar. |
| A14 | **Jogador sem UF.** `newPlayerData` grava `state = ""`; perfil novo também começa sem estado. "Meu estado" e a posição estadual dependem disso. | Exigir UF antes de entrar numa missão. |
| A15 | **Sincronização vive na tela/ViewModel.** `viewModelScope` do ViewModel da tela; se a Activity morrer, o envio para. | Mover o envio para o `RunTrackingService`. |
| A16 | **Sem teto de jogadores** (`maxPlayers = Int.MAX_VALUE`) e sem tempo máximo de prova. | Defina limite e `maxDurationMin` por distância. |
| A17 | **`isPremium = true` fixo** e `deleteAccount()` só encerra a sessão (já registrado no LEIAME). | |
| A18 | **GPS é do cliente.** Distância/tempo vêm do aparelho (limitação já descrita no LEIAME). Sem validação de servidor, o ranking é vulnerável a trapaça. | Aceitar na v1 e dizer isso aos usuários, ou migrar para validação em servidor depois. |

## Telas e estados que faltam
| # | Falta | Quando aparece |
|---|---|---|
| T1 | **Pré-checagem** (login, UF, permissão de localização, GPS ligado, bateria) | Ao tocar "Entrar na missão" |
| T2 | **Contagem real na sala** e **largada 3-2-1** | Até o horário e no instante da largada |
| T3 | **Voltar à prova** (reconexão) | App reaberto ou usuário saiu da tela com a prova em andamento |
| T4 | **Confirmação de desistir** | Toque em "Desistir" ou back durante a prova |
| T5 | **Animação de missão cumprida** | Ao concluir a distância pelo GPS |
| T6 | **Aguardando os demais** (placar parcial) | Terminou antes dos outros |
| T7 | **Resultado de abandono (DNF)** | Após desistir |
| T8 | **Sem conexão durante a prova** (aviso, dados sincronizam depois) | Perda de rede |
| T9 | **Modal de promoção/rebaixamento de liga** com emblema animado | Após o resultado, se a liga mudou |
| T10 | **Galeria de ligas** com emblemas | Tela Temporada |
| T11 | **Histórico de missões** (`competitionHistory` existe, mas não há tela) | Menu da competição |
| T12 | **Missão cheia** / **sessão expirada** / **sem permissão** (erros tratados) | Falhas de entrada |
| T13 | **Estados vazios** (ranking, conquistas, histórico) | Início da temporada |
| T14 | **Regras e fair-play** (GPS obrigatório, abandono, pontuação) | Primeira entrada |
| T15 | **Perfil do atleta** completo (hoje só um sheet com nome) | Toque num atleta |
| T16 | **Seleção de UF** dentro da competição (a sheet do Perfil já existe no pacote anterior) | Perfil sem UF |

## Fluxo proposto (sem lacunas)
```
Hub -> [gate: login, UF, permissão GPS] -> Sala (contagem real, atletas reais)
  -> (horário e >= 2 atletas) Largada 3-2-1 -> Prova (GPS iniciado pelo app, envio pelo serviço)
       |-> concluiu a distância (GPS) -> Missão cumprida (animação) -> [aguardando demais] -> Resultado -> Liga/Promoção -> Ranking / Compartilhar
       |-> Desistir -> Confirmação -> Resultado de abandono (DNF) -> Hub
       |-> sem rede -> aviso; continua gravando; sincroniza depois
  -> (horário e < 2 atletas) Missão não iniciada -> Hub
Reabrir app com prova em andamento -> "Voltar à prova"
```
Máquina de estados do jogador (campo `status` em `missions/{id}/players/{uid}`): `joined -> ready -> running -> finished | dnf | timeout`. Estado da missão **derivado** (sem escrever): `open`, `needs_one`, `confirmed`, `live`, `closed` (todos finished/dnf/timeout, ou passou `startsAt + tempo máximo`), `not_started`. Isso evita depender de Cloud Functions.

## Decisões que só você pode tomar
1. **Elo/pontos sem servidor (C7).** Opções: (a) o cliente grava os pontos da missão com regras que limitam o valor e impedem repetir (grátis, mas trapaceável); (b) ativar o plano Blaze só para as Cloud Functions que você já escreveu (seu LEIAME diz que o deploy exige Blaze; ele tem cota gratuita, mas exige cartão); (c) adiar o ranking real e lançar a competição só com resultado da missão. Recomendo (a) para a primeira versão, dizendo claramente que é beta.
2. **Tabela de ligas.** Proposta que bate com os números que você já usa (Ouro I 1600–1799, Diamante I 2800–2999): divisões de 200 pontos, 3 por liga, cada liga com 600. Bronze 0–599, Prata 600–1199, Ouro 1200–1799, Platina 1800–2399, Diamante 2400–2999, Elite 3000+ (sem divisões). Confirma?
3. **Tempo máximo por distância** (sugestão: 2,5K = 30 min, 5K = 60 min, 10K = 120 min) e **limite de atletas por missão**.
4. **Pontos por resultado** (sugestão simples: 1º +30, 2º +20, 3º +12, demais que concluem +5, abandono −15). Valores a ajustar.
5. **Quem pode entrar com a prova em andamento** (atrasado): hoje ninguém. Sugestão: não.

## Ordem recomendada de correção
- **P0 (a prova roda):** C1, C2, C3, C4, C5, A1, A2, A6, A14, A15; telas T1 a T5.
- **P1 (resultado e liga reais):** C6, C7, C8, A4, A7, A8, A13; telas T6 a T10.
- **P2 (resto):** A3, A5, A9 a A12, A16 a A18; telas T11 a T16.
Cada item vem com seu teste (unitário do ViewModel e teste de regras no emulador) antes de seguir para o próximo.
