# HANDOFF - Competição v2: prova, desistir, missão cumprida, resultados e ligas  (v1)

> Complementa o pacote `competicao-online` (11 telas). Leia antes `AUDITORIA-FLUXO.md`: ela explica **por que** cada tela e cada mudança abaixo existe (itens C1 a C8, A1 a A18, T1 a T16).

## 1. Objetivo
Fecha as lacunas do fluxo da prova que a auditoria encontrou e atende suas 4 anotações:
1. **Desistir** sempre visível e com confirmação, que de fato encerra a prova (para o GPS, grava abandono, vai para o resultado de abandono).
2. **Emblemas de liga animados** do Ouro para cima, no estilo dos emblemas de ranked de jogos (aura, brilho que varre o escudo, faíscas, anel e asas que giram ou batem). Bronze e Prata ficam estáticos.
3. **Animação de missão cumprida** quando o GPS completa a distância.
4. **Telas que faltavam:** largada 3-2-1, resultado parcial (terminou antes dos outros), resultado de abandono, galeria de ligas e promoção de liga.

Este pacote desenha 9 estados (seção 6). As demais telas da auditoria (pré-checagem, voltar à prova, sem conexão, histórico, regras, etc.) **não estão desenhadas aqui**; ficam para a próxima rodada e estão listadas em Perguntas em aberto.

## 2. Dispositivo
Poco X7, 393 x 851 dp, tema escuro, Jetpack Compose. Fontes: Barlow Condensed (números e títulos), Inter (texto). O HTML reserva 28dp no topo (barra de status; no app já existe `statusBarsPadding()`), 16dp nas laterais e 16dp embaixo (`navigationBarsPadding()` no app). Outros aparelhos: 360 a 412dp e `fontScale` até 1.3. Larguras fixas só em avatar, emblema, badge e botão de ícone; o resto usa `fillMaxWidth`/`weight`. Em telas menores (altura < 760dp) o **placar da prova encolhe e rola por dentro**; o botão "Desistir da prova" nunca sai da tela.

## 3. Tokens
| Variável CSS | Valor | Nome Kotlin |
|---|---|---|
| --page | #050604 | só do preview HTML |
| --bg | #0A0B09 | Background (existe) |
| --surface | #10120D | Surface (existe) |
| --card | #161911 | Card (existe) |
| --card-el | #1D2116 | CardElevated (existe) |
| --line | rgba(236,232,218,.10) = 0x1AECE8DA | Line (existe) |
| --line-strong | rgba(236,232,218,.18) = 0x2EECE8DA | LineStrong (existe) |
| --text | #ECE8DA | Text (existe) |
| --muted | #8F9384 | TextMuted (existe) |
| --olive | #A3B565 | Olive (existe) |
| --olive-dark | #5D6B34 | OliveDark (existe) |
| --olive-soft | rgba(163,181,101,.14) = 0x24A3B565 | OliveSoft (existe) |
| --on-olive | #0E1306 | OnOlive (existe) |
| --red | #D12B2B | Red (existe) |
| --red-soft | rgba(209,43,43,.15) = 0x26D12B2B | RedSoft (existe) |
| --red-line | rgba(209,43,43,.35) = 0x59D12B2B | RedLine (existe; não usar o .50 do pacote anterior) |
| --on-red | #FFF4F0 | OnRed (existe) |
| --brass | #C9A24B | Brass (existe) |
| --brass-soft | rgba(201,162,75,.16) = 0x29C9A24B | BrassSoft (reaproveitar se já criado pelo pacote anterior) |
| --amber | #E0A93A | Amber (existe) |
| --ok | #7FB069 | Ok (existe) |
| --scrim | rgba(0,0,0,.62) = 0x9E000000 | EliteScrim (reaproveitar se já existir) |
| --scrim-strong | rgba(5,6,4,.94) = 0xF0050604 | EliteScrimStrong (NOVO; fundo das animações de tela cheia) |
| --bronze / --bronze-l / --bronze-d | #B07A4A / #D9A877 / #6E4421 | LeagueBronze (hoje `private val Bronze` em CompetitionComponents.kt; tornar público) / BronzeLight / BronzeDark (NOVOS) |
| --silver / --silver-l / --silver-d | #B4BAB8 / #E3E7E6 / #6F7573 | LeagueSilver / SilverLight / SilverDark |
| --gold / --gold-l / --gold-d | #C9A24B / #F2D488 / #7A5C1B | LeagueGold (= Brass) / GoldLight / GoldDark |
| --plat / --plat-l / --plat-d | #86BDB3 / #C6F0E8 / #3F7A72 | LeaguePlatinum / PlatinumLight / PlatinumDark |
| --dia / --dia-l / --dia-d | #7FB4DE / #C3E6FF / #3A6A96 | LeagueDiamond / DiamondLight / DiamondDark |
| --elite / --elite-l / --elite-d | #E7E2D0 / #FFFBEA / #8F8970 | LeagueElite (= EliteLeague) / EliteLight / EliteDark |
| --num / --ui | Barlow Condensed / Inter | EliteDisplayFont / EliteBodyFont (existem) |

Tipografia (px = sp): tempo da prova 84/84 Barlow 700; título `t2` 20/23 Barlow 600 caixa-alta ls .8; legenda `lbl` 11/14 Inter 700 caixa-alta ls 1.1; corpo 14/19.6 Inter 400; botão 16/20 Inter 600; título da missão cumprida 44/44 Barlow 700 caixa-alta ls 2; tempo final da animação 64/64 Barlow 700; promoção 40/42 Barlow 700 ls 1.6; "DNF"/posição 78/78 Barlow 700. Formas: card r18, botão primário pill r26, botão fantasma r24, diálogo r20, pill r14.

## 4. Mapa de componentes
| data-id | Composable | Arquivo | Novo? |
|---|---|---|---|
| `race` | MissionRaceScreen | ui/screens/competition/CompetitionFlowScreen.kt | não (refeita) |
| `race.board` | LazyColumn `weight(1f)` com linhas de atletas | idem | não (passa a ler todos os atletas) |
| `race.giveup` | OutlinedButton vermelho, último item, sempre visível | idem | sim |
| `dlg`, `dlg.box` | GiveUpDialog (EliteDialog) | ui/components/competition/CompetitionDialogs.kt | sim |
| `start`, `start.n3..go` | StartCountdownOverlay | ui/components/competition/StartCountdownOverlay.kt | sim |
| `fin`, `fin.confetti` | MissionCompleteOverlay + ConfettiCanvas | ui/components/competition/MissionCompleteOverlay.kt | sim |
| `res` | MissionResultScreen (3 variantes: concluída, parcial, abandono) | CompetitionFlowScreen.kt | não (refeita, dados reais) |
| `res.league.emblem`, `liga.hero.emblem`, `promo.emblem`, `liga.tier.*`, `liga.div*` | LeagueEmblem(tier, division, size, animated) | ui/components/competition/LeagueEmblem.kt | sim (substitui `LeagueBadge`) |
| `liga` | SeasonScreen (seção Ligas) | CompetitionSecondaryScreens.kt | não (alterada) |
| `promo` | LeaguePromotionDialog | ui/components/competition/LeaguePromotionDialog.kt | sim |
| (modelo) | `LeagueTable` (única fonte das ligas) | competition/LeagueTable.kt | sim |

Cada `data-id` vira `Modifier.testTag("<data-id>")`.

## 5. Regras de layout
- **Prova (`race`):** coluna com padding 16 e `spacedBy(10)`. Medidas (dp, a 393x851): pills 32; tempo 98 (rótulo + 84sp); card de progresso 76; pace e posição lado a lado, 74 cada; **placar = único elemento elástico (`weight(1f)`)**, 403 aqui, mínimo 120 (abaixo disso rola por dentro); legenda 16; botão "Desistir da prova" 48 (y=787). O botão é o último filho da coluna, fora do placar, e **nunca é empurrado para fora**.
- **Back durante a prova** abre o mesmo diálogo de desistir (`BackHandler`); nunca sai em silêncio.
- **Diálogo de desistir:** 311dp de largura, centralizado, padding 20, gap 12; altura 279 (texto de 4 linhas). Botão "Continuar correndo" (olive, 52) vem **antes** e é a ação padrão; "Desistir" (contorno vermelho, 48) depois.
- **Largada:** tela cheia sobre a prova, número centralizado (180sp; "JÁ!" 120sp). Não há botões.
- **Missão cumprida:** tela cheia; bloco central a partir de y=150: distintivo 100, título (pode quebrar em 2 linhas), subtítulo, tempo 64sp, chip de posição 34; botão "Ver resultado" aparece embaixo (y=783).
- **Resultado:** `Column` rolável (como hoje) com topbar fixa e botões fixos embaixo. Topbar 52; hero 172; liga 91; classificação 272 (4 linhas de 52); botões 48 (y=787). Na variante abandono "2º/LUGAR" vira "DNF/ABANDONO" e a linha do usuário mostra "—" no lugar da posição.
- **Galeria de ligas:** coluna rolável. Hero 296 (emblema 150, nome 36sp, faixa, barra, nota); faixa de 6 ligas, cada uma `weight(1f)` com altura 78; lista das 3 divisões (53 cada). Para Elite a lista de divisões some (sem divisões).
- **Promoção:** tela cheia; raios 300x300 centralizados atrás do emblema de 210; título (2 linhas) abaixo; botões fixos embaixo (52 e 48).
- Telas < 760dp: nada se esconde; as telas roláveis rolam; na prova só o placar encolhe.

## 6. Estados
| Estado | Como disparar (states.json) | O que mostra |
|---|---|---|
| prova | `__setState('prova')` | Prova ao vivo com placar de 4 atletas, "Sua posição 2º" e botão Desistir visível. |
| desistir_dialog | `__setState('desistir_dialog')` | Confirmação sobre a prova (escurecida). |
| largada (3, 2, 1, JÁ!) | `__setState('largada')` + quadros 200/700/1200/2200/3200ms | Contagem regressiva de 4 tempos de 1s. |
| concluida | `__setState('concluida')` + quadros 0 a 3000ms | Animação de missão cumprida (ver Motion). |
| resultado | `__setState('resultado')` | 2º lugar, +20 pontos, Ouro II, classificação. |
| resultado_parcial | `__setState('resultado_parcial')` | "Você terminou", posição parcial, "Aguardando os demais", pontos provisórios, outros "Em andamento". |
| resultado_abandono | `__setState('resultado_abandono')` | "Você desistiu", DNF, −15 pontos, liga recua, linha do usuário sem posição. |
| liga_galeria | `__setState('liga_galeria')` + quadros | Hero com emblema animado de Ouro II; faixa das 6 ligas; divisões. |
| liga_platina, liga_diamante, liga_elite | clique em `liga.tier.*` | Mesmo hero com o emblema animado da liga tocada. |
| promocao | `__setState('promocao')` + quadros | Modal "Você subiu para Ouro III". |
| desistir_pressionado | press `race.giveup` | Escala 0.96. |

Estados do jogador no app (campo `status`): `joined, ready, running, finished, dnf, timeout`. Fase da UI: `PRE, COUNTDOWN, RUNNING, FINISHED_WAITING, FINISHED_FINAL, DNF`. A tela de resultado escolhe a variante por `status` do jogador e por existir ou não algum atleta em `running`.

## 7. Motion
Regra: só **uma** animação em repouso por tela, a "estrela". Na galeria e na promoção a estrela é o **emblema** (conjunto de camadas, ver tabela). Na prova não há animação em repouso.

| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `start.n3`, `start.n2`, `start.n1`, `start.go` | entrar em `largada` | escala, opacidade (`cd-n`) | 1.5 -> 1; 0 -> 1 (em 25%) -> 0 | 1000ms | 0, 1000, 2000, 3000ms | cubic-bezier(.2,.8,.2,1) | não | mostra só "JÁ!" |
| `fin.ring1`, `fin.ring2` | entrar em `concluida` | escala, opacidade (`fin-ring`) | 0.6 -> 2.6; .7 -> 0 | 1100ms | 0 e 160ms | ease-out | não | sem anéis |
| `fin.badge`, `fin.place` | idem | escala, opacidade (`fin-pop`) | 0.4 -> 1.12 -> 1; 0 -> 1 | 650ms (distintivo), 520ms (chip) | 0 e 800ms | cubic-bezier(.2,1.4,.4,1) | não | aparece direto |
| `fin.title`, `fin.sub`, `fin.time`, `fin.cta` | idem | translação Y, opacidade (`fin-up`) | +14dp -> 0; 0 -> 1 | 520ms (450ms no botão) | 250, 400, 550, 1500ms | ease-out | não | aparece direto |
| `fin.time` | idem | contagem 00:00 -> tempo final | `animateFloatAsState` com ease-out cúbico | 900ms | 550ms | ease-out | não | mostra o tempo final |
| `fin.confetti` | idem | queda e rotação (`cf-fall`) de 26 peças 7x12 nas cores olive, brass, red, text | Y 0 -> +900dp; giro 540deg | 2000 a 2800ms | 250 a 970ms | ease-in | não | sem confete |
| `liga.hero.emblem`, `promo.emblem` (camada aura) | emblema `animated` | escala, opacidade (`emb-pulse`) | 0.92 -> 1.06; .55 -> .95 | 2400ms | 0 | ease-in-out, vai e volta | infinito | parado |
| idem (camada brilho) | idem | translação X de uma faixa clipada no escudo (`emb-sheen`) | -40 -> 170 (unidades do desenho, 120 = largura) | 4200ms (1800ms de varredura + pausa) | 0 | ease-in-out | infinito | parado |
| idem (faíscas, 2 a 5 por liga) | idem | escala e giro 45deg, opacidade (`emb-sp`) | 0 -> 1 -> 0 | 2200ms | 0, 420, 840... ms | ease-in-out | infinito | invisíveis |
| idem (anel 1, Platina+) | idem | rotação (`emb-rot`) | 0 -> 360deg | 16000ms | 0 | linear | infinito | parado |
| idem (anel 2, Diamante e Elite) | idem | rotação inversa (`emb-rot`) | 0 -> -360deg | 22000ms | 0 | linear | infinito | parado |
| idem (asas, Ouro+) | idem | rotação em torno do ombro (`emb-wl`, `emb-wr`) | 0 -> -4deg (esq.) e 0 -> 4deg (dir.) | 3200ms | 0 | ease-in-out, vai e volta | infinito | parado |
| idem (facetas, Diamante) | idem | opacidade (`emb-fc`) | .35 -> 1 | 1800ms | 0 | ease-in-out, vai e volta | infinito | opacidade 1 |
| idem (brasas, Elite) | idem | translação Y, escala, opacidade (`emb-em`) de 5 pontos olive/red | Y 0 -> -46; escala 1 -> .4; .9 -> 0 | 2400ms | 0, 480, 960... ms | ease-out | infinito | invisíveis |
| `promo.emblem` | entrar em `promocao` | escala (`fin-pop`) | 0.4 -> 1.12 -> 1 | 800ms | 0 | cubic-bezier(.2,1.4,.4,1) | não | aparece direto |
| `promo.title` | idem | `fin-up` | +14dp -> 0 | 520ms | 500ms | ease-out | não | aparece direto |
| `promo.rays` | idem | rotação (`emb-rot`) dos 12 raios | 0 -> 360deg | 24000ms | 0 | linear | infinito | parado |
| `race.giveup`, `dlg.continue`, `dlg.confirm`, `fin.cta`, `promo.continue`, `promo.share`, `res.share`, `res.ranking` | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |

**Quando o emblema anima:** só em `animated = true` (hero da galeria, promoção, tela de resultado quando a liga mudou). Em listas, faixa de ligas e tamanhos < 44dp o emblema é estático e **sem** aura, asas, anéis nem faíscas (só escudo, símbolo central e pontos de divisão). Reduzir animação: usar o mesmo teste do app (`ANIMATOR_DURATION_SCALE == 0f`, como em `PremiumCompetitionCard`).

**Sensação extra (fora do HTML):** vibração curta em cada número da largada e dupla no "JÁ!"; vibração longa ao concluir; um som opcional por `SoundPool` se você adicionar o arquivo (não incluído).

## 8. Assets
- **Emblema (`LeagueEmblem`)** desenhado em `Canvas` num quadro de 120 x 120 unidades escalado para o tamanho. Camadas, de trás para frente: **aura** (círculo r56 em (60,62), gradiente radial cor da liga .55 -> 0); **anéis** (círculos r53 tracejado `1.5 7`, e r48 tracejado `10 6` a 60%); **asas** (caminho `M28 34C14 28 4 38 6 52C11 47 17 47 21 51C12 55 9 64 13 74C18 68 24 68 29 71Z`, espelhado em x=60; Ouro a 75% do tamanho); **escudo** (`M60 14L96 26L94 62C91 84 76 98 60 106C44 98 29 84 26 62L24 26Z`, gradiente vertical claro -> base (55%) -> escuro); **escudo interno** (`M60 22L88 32L86 62C84 80 72 91 60 97C48 91 36 80 34 62L32 32Z`, cor escura a 55%); **contorno** claro 1.6; **símbolo central** por liga; **pontos de divisão** (losangos de 8 unidades em y=84 a 94: divisão I = 3, II = 2, III = 1; Elite não tem); **brilho** (faixa branca inclinada -20deg clipada ao escudo com `clipPath`); **faíscas** (estrela de 4 pontas `M0-7L1.8-1.8L7 0L1.8 1.8L0 7L-1.8 1.8L-7 0L-1.8-1.8Z` nas posições (22,34), (98,30), (16,76), (104,72), (60,8)); **brasas** (Elite).
- **Símbolo central:** Bronze e Prata = 3 chevrons (como hoje); Ouro = sol (círculo r8 + 8 raios); Platina = hexágono com faceta; Diamante = diamante com 3 facetas que piscam; Elite = a lâmina da marca (polígono olive + faixa vermelha).
- **Intensidade por liga:** Bronze e Prata nenhuma animação; Ouro = aura + brilho + 2 faíscas + asas pequenas; Platina = + anel 1 + 3 faíscas + asas; Diamante = + anel 2 + facetas + 4 faíscas; Elite = + brasas + 5 faíscas.
- **Ícones Material:** `Flag` (missão cumprida), `ArrowBack`. Bandeira do HTML = traço simples `M5 21V4M5 4h11l-2 4 2 4H5`.
- **Fontes** (já existem em `res/font`): `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`.

## 9. Mudanças
Os itens são a implementação do que a auditoria apontou. Itens marcados **P0** tornam a prova utilizável.

| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| **P0** `competition/LeagueTable.kt` (novo) | `leagueFor()` com 800/1200/1600 e faixas fixas na Temporada | Tabela única: Bronze 0, Prata 600, Ouro 1200, Platina 1800, Diamante 2400, Elite 3000; cada liga com 3 divisões de 200 (III, II, I) | Fonte única para ranking, temporada, resultado e promoção. Idealmente lida de `config/season.ligas`, com esta tabela como padrão. Teste unitário de todas as fronteiras. |
| **P0** `service/RunTrackingService.kt` + novo `CompetitionRunController` | Competição nunca inicia o GPS (C1) | Iniciar o serviço com `modality = "competicao"` e alvo em metros na largada; ao atingir o alvo, o **serviço** conclui, para o GPS e dispara a chegada | Detecção sai da tela (C5). Garantir que a chegada dispara **uma vez** (idempotente). Não parar o serviço ao sair da tela. |
| **P0** `OnlineCompetitionRepository.kt` | `joinMission` falha se já começou, mesmo para quem já está na sala (C3) | Checar `player.exists()` primeiro: quem já está na sala pode **voltar à prova**; novo jogador não entra após o início | Adicionar `observePlayers(missionId)` e `serverTimeOffset`. |
| **P0** `OnlineCompetitionRepository.kt` | Sem abandono (C4) | `abandonMission(missionId)`: transação que grava `status = "dnf"`, `abandonedAt = serverTimestamp`, último `progressKm`; recusa se já `finished` | Regras do Firestore precisam permitir esses campos e a transição. O `firestore.rules` não estava no zip; editar o do seu repositório. |
| **P0** `OnlineCompetitionRepository.markPlayerFinished` | Força distância `coerceAtLeast(distância da prova)` | Gravar a distância **real** do GPS e `status = "finished"` | Corrige C5. |
| **P0** `CompetitionDemoState.kt` (ViewModel) | `onDesist` só para o envio; `finishRace()` local | `giveUp()` abre o diálogo; `confirmGiveUp()` chama `abandonMission` + para o serviço + vai para o resultado de abandono; estado da prova por fase (`PRE/COUNTDOWN/RUNNING/FINISHED_*/DNF`) | `BackHandler` na prova chama `giveUp()`. |
| **P0** `CompetitionFlowScreen.kt` `MissionRaceScreen` | Coluna sem rolagem, relógio 92sp, botão no fim | Layout da seção 5; placar `LazyColumn weight(1f)` lendo os atletas; botão fixo | Textos `SUA POSI??O` etc. corrigidos (A6). |
| **P0** nova `StartCountdownOverlay` | Não existe | 3, 2, 1, JÁ! sincronizado pela hora do servidor | Largada em `startsAt` (com offset do servidor), não no toque do usuário (C2). |
| **P0** nova `MissionCompleteOverlay` | Não existe (vai direto ao resultado) | Overlay animado da seção 7; fecha com "Ver resultado" | Aparece quando o serviço sinaliza a chegada, mesmo se o usuário estiver em outra tela ao concluir (ao voltar, mostra). |
| **P1** `MissionResultScreen` | Tudo fixo (C6) | Dados reais: posição, tempo, pace, pontos, liga e classificação; variantes concluída, parcial e abandono | Pace médio e tempo vêm do jogador; posição = ordem de `finishedAt`. |
| **P1** status da missão (`toDemoMission`) | `FINISHED` quando o primeiro termina (C8) | Missão `closed` só quando todos estão `finished/dnf/timeout` ou passou `startsAt + tempo máximo` | Estado derivado, sem escrita. Tempo máximo por distância em `config/season`. |
| **P1** `LeagueEmblem.kt` (novo) + `LeagueBadge` | Escudo estático 22 a 64dp | `LeagueEmblem(tier, division, size, animated)` conforme seção 8; `LeagueBadge` passa a chamá-lo em modo estático | Substitui também os usos em ranking e temporada. Pontos de divisão trocam os chevrons nas ligas novas (desvio de design consciente). |
| **P1** `SeasonScreen` | Dados fixos (A9), faixa de ligas estática | Hero com emblema animado da liga do usuário; toque numa liga mostra o emblema dela; divisões da `LeagueTable` | Remover "12d 04h", "Ouro II", "58 pontos" fixos. |
| **P1** novo `LeaguePromotionDialog` | Não existe | Modal da seção 7 ao subir (ou descer) de liga; mensagem de rebaixamento sem animação de festa | Exibir uma vez por mudança (guardar `lastSeenTier`). |
| **P1** `RankingScreen` | Ranking sem filtro por distância (A7) | Ordenar por `elo`, `elo25`, `elo5` ou `elo10` conforme o filtro; usar `LeagueTable` | Índices novos em `firestore.indexes.json`. |
| **P1** pontos e elo (C7) | Ninguém escreve | Decisão pendente (ver Perguntas): sem escrita, a liga nunca muda e a promoção nunca aparece | Bloqueia testes reais de promoção. |
| Testes | — | `LeagueTableTest`, `MissionStatusTest` (derivado), `CompetitionRunControllerTest` (chegada única, abandono, retorno), testes das regras no emulador | Nenhum teste existente pode quebrar. |

## 10. Armadilhas
- **Uma chegada só:** o serviço pode emitir a conclusão mais de uma vez (GPS oscila perto do alvo). Guardar flag `completed` e só então gravar; a gravação no Firestore também deve ser idempotente (`status` já `finished` = ignorar).
- **Abandono vs. saída da tela:** sair da tela **não** é desistir. Só `confirmGiveUp()` abandona. O serviço continua rodando se a tela fechar.
- **Largada pela hora do servidor:** usar `serverTimeOffset` (diferença medida com `serverTimestamp`), nunca `System.currentTimeMillis()` puro.
- **Placar:** não ouvir `players` de todas as missões do Hub. Ouvir só a missão atual, só durante a prova, e cancelar o listener ao sair.
- **Cota gratuita:** progresso a cada 15 a 20s e só se andou >= 25m. Ver AUDITORIA (A5).
- **Emblemas no Compose:** nada de `blur` nem `drawIntoCanvas` com filtros. Aura = `Brush.radialGradient`; brilho = faixa com `Brush.linearGradient` dentro de `clipPath(escudo)`; asas/anéis = `rotate(degrees, pivot)` em `drawScope`. Uma única `InfiniteTransition` por emblema, com vários `animateFloat`, para não criar dezenas de transições.
- **Desempenho:** só **um** emblema animado visível por vez; os demais são estáticos. Faíscas e brasas são no máximo 5 por emblema.
- **Cor CSS `rgba(r,g,b,a)`** = `Color(0xAARRGGBB)` (alpha primeiro). A tabela de tokens já está convertida.
- **`text-transform: uppercase`** = `.uppercase()` no Kotlin (cuidado com acentos: "MISSÃO", "LIGA").
- **Título da missão cumprida** quebra em duas linhas a 393dp ("MISSÃO / CUMPRIDA"); em telas estreitas use `maxLines = 2` e `autoSize` não é necessário.
- **Confete:** `Canvas` com 26 partículas e uma única animação de tempo (`Animatable`), parada ao fim; nada de 26 composables.
- **Encoding:** salvar todos os `.kt` em UTF-8 e evitar literais com `?` no lugar de acentos (A6).
- Fontes como arquivos `.ttf` em `res/font`; meça com `--fonts-dir`.

## 11. Aceitação
- Estados estáticos (`prova`, `desistir_dialog`, `resultado`, `resultado_parcial`, `resultado_abandono`, `liga_galeria` no quadro 0): diff médio <= 3.0 e pior célula <= 10.0; `compare_bounds` dentro de ±2dp (±3dp nas alturas de texto de várias linhas).
- Estados animados: comparar os **quadros** de `states.json` (largada, concluida, liga_*, promocao) por tolerância maior (diff médio <= 6.0), e conferir à mão a folha de movimento contra a tabela da seção 7.
- Teste de fluxo no aparelho: (1) entrar na missão, aguardar a largada real, correr (ou simular o GPS) até concluir -> overlay -> resultado; (2) desistir no meio -> diálogo -> resultado de abandono, GPS parado, ranking da missão com "Abandonou"; (3) tocar em voltar na prova -> abre o diálogo; (4) sair da tela sem desistir e voltar -> a prova continua; (5) fechar e reabrir o app em prova -> volta à prova.
- Largura 360dp e 412dp com `fontScale` 1.3: botão Desistir sempre visível, nada cortado, placar rola por dentro.
- `./gradlew testDebugUnitTest` passando, incluindo os testes novos.
- Desvios aceitos: sem som; emblema simplificado abaixo de 44dp; pontos de divisão no lugar dos chevrons (Ouro+); título da missão cumprida em 2 linhas.

## 12. Perguntas em aberto
- **Pontos por resultado e penalidade de abandono.** O desenho usa +30 / +20 / +12 e −15 só como exemplo. Defina os valores (ou me diga para propor uma tabela).
- **Como o elo é gravado sem servidor (C7).** Cliente com regras limitadoras (grátis, trapaceável), Cloud Functions no plano Blaze, ou adiar. Sem decisão, não há promoção de liga real.
- **Tabela de ligas.** Confirma Bronze 0, Prata 600, Ouro 1200, Platina 1800, Diamante 2400, Elite 3000 (divisões de 200)? Ela bate com os números que já aparecem nas suas telas (Ouro 1200 a 1799, Diamante I 2800 a 2999).
- **Tempo máximo da prova** (2,5K, 5K, 10K) e **limite de atletas** por missão.
- **Desistência nos primeiros segundos:** penalizar sempre, ou perdoar nos primeiros 30 s depois da largada?
- **Rebaixamento:** mostrar um modal discreto (sem festa) ou apenas um aviso?
- **Telas da auditoria ainda sem desenho:** pré-checagem (login, UF, GPS, bateria), voltar à prova, sem conexão, missão cheia, histórico de missões, regras e fair-play, perfil do atleta e estados vazios. Quer que eu desenhe no próximo pacote?
- **Som:** quer efeitos sonoros na largada e na chegada? É preciso fornecer ou escolher os arquivos.

## Changelog
- v1: primeira entrega (9 estados, emblemas animados do Ouro ao Elite, auditoria do fluxo).
