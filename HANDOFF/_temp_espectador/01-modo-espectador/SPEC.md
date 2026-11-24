# SPEC - Modo espectador nas competições online ("Em andamento")

Protótipo: `screen.html` (3 vistas: **hub**, **espectador** ao vivo, **encerrada**). Referências: `screens/*.png`. Medidas em dp: `spec/*.md`.
Projeto: `com.elite.sch.app`, Compose, 393 x 851 dp, tema escuro, pt-BR. Tokens/fontes: **nenhum novo** (ver seção 3). Arquivos reais lidos do seu `src_1.zip`.

## 1. O pedido
Nas competições online, uma prova **"EM ANDAMENTO"** deve poder ser **assistida por quem não está correndo**: ver a **colocação ao vivo**. Mesmo design e padrões da competição; só **um botão discreto** na linha da prova em andamento, que entra no **modo espectador**. Vale para 2,5K, 5K e 10K.

## 2. Como é hoje (diagnóstico no código)
| Fato | Onde |
|---|---|
| A lista "Missões de hoje" mostra a prova ao vivo como linha de 74 dp com pílula vermelha **EM ANDAMENTO** (`MissionStatus.LIVE`) e o ícone de compartilhar convite | `CompetitionFlowScreen.kt` `MissionCard` (~L446-495) |
| Tocar nessa linha chama `joinMission`. Quem **não** estava inscrito leva o erro **"Esta missão já começou."** (a entrada só é permitida antes do início) | `OnlineCompetitionRepository.joinMission` (~L291-360) |
| O "Placar ao vivo" já existe, mas **só na tela do corredor** (`MissionRaceScreen`), ordenado por `progressKm`, com aviso "sinal atrasado" | `CompetitionFlowScreen.kt` ~L724-790 |
| Os dados do placar **já chegam ao app de qualquer usuário logado**: `observeDay` abre um listener de `missions/{id}/players` para toda missão com `playerCount > 0` e preenche `DemoMission.results` (`progressKm`, `paceSecPerKm`, `timeSec`, `finishedAt`, `withdrawnAt`, `updatedAt`) | `OnlineCompetitionRepository.observeDay` (~L136-250) |
| Só aparecem missões da **mesma UF** do usuário (`whereEqualTo("state", ...)`) | idem |

**Conclusão:** o espectador é só uma **tela nova, somente leitura**, que reaproveita `state.selectedMission.results`. **Nenhum listener, gravação ou regra nova.**

## 3. Tokens (todos existentes; ver `ui/theme/Color.kt` e `Type.kt`)
`Background #0A0B09`, `Card #161911`, `CardElevated #1D2116`, `Line` osso 10%, `Text #ECE8DA`, `TextMuted #8F9384`, `Olive #A3B565`, `OliveDark #5D6B34`, `Red #D12B2B`, `Brass #C9A24B`. Fontes: `EliteDisplayFont` (Barlow Condensed) e `EliteBodyFont` (Inter), já em `res/font`. Componentes reaproveitados de `ui/components/competition/CompetitionComponents.kt`: `CompetitionTopBar`, `CompetitionPill`, `CompetitionCard`, `CompetitionText`, `CompetitionAction`, `SectionHeading`, `RankingPersonRow`, `CompetitionAvatar`.

## 4. Fluxo
```
Hub (Missões de hoje)
  linha EM ANDAMENTO --[botão olho, 44 dp]--> MODO ESPECTADOR (ao vivo)
                                                |  a prova termina (status deixa de ser LIVE)
                                                v
                                        MODO ESPECTADOR (encerrada) --[Ver resultado completo]--> RESULT (já existe)
  voltar (seta do topo, botão do sistema, "Sair do modo espectador") -> Hub
```
Nada disso inscreve o usuário: **não** chama `joinMission`, `updateRoomPresence`, `startRace` nem GPS.

## 5. Mudanças de layout
**5.1 Hub: linha EM ANDAMENTO** (`hub.live`). Mesma linha de 74 dp (tempo 28 | pílula da distância | pílula EM ANDAMENTO | botão). **O ícone de compartilhar dessa linha é trocado pelo botão "assistir"** (mesmo tamanho 44 dp, mesmo estilo circular: fundo `Olive` 12%, borda 1 dp `OliveDark`, ícone 22 dp `Olive`, ícone `Icons.Default.Visibility`, `contentDescription = "Assistir ao vivo"`).
Por que trocar e não somar: com os dois botões (88 dp + espaço) a pílula "EM ANDAMENTO" não cabe em 393 dp (sobra ~86 dp para ~119 dp). E convidar para uma prova já iniciada não serve: ninguém mais consegue entrar. As linhas ENCERRADA, NÃO INICIADA e os cards abertos **não mudam** (continuam com compartilhar).

**5.2 Tela do espectador ao vivo** (`esp.*`), `Column(padding horizontal 16, spacedBy 10)`, dentro do `Column` do fluxo que já aplica `statusBarsPadding` e `navigationBarsPadding`:
1. `esp.top`: `CompetitionTopBar(title = "5K · 09:00", eyebrow = "Modo espectador", onBack, trailing = { CompetitionPill("●  AO VIVO", color = Red, active = true) })`. Título = `"${mission.distance.label} · ${mission.time}"`.
2. `esp.timer`: `Column(CenterHorizontally, padding vertical 4)`: "TEMPO DE PROVA" (13 sp, bold, `TextMuted`, letterSpacing 1.1) + tempo **64 sp** display bold (`22:12`). É o tempo desde o **horário oficial da prova** (`startsAt`), não o do aparelho.
3. `esp.leader`: `CompetitionCard`: linha "LÍDER · MARINA SOUZA" (11 sp bold `TextMuted`, ls 1.0, maiúsculas); linha com `4,31` (32 sp display bold) + ` / 5,0 km` (19 sp display `TextMuted`) à esquerda e `86%` (23 sp display bold `Olive`) à direita; barra de progresso 10 dp (`CardElevated`, preenchimento `Olive`).
4. `esp.metrics`: `Row(spacedBy 10)` com dois `RaceMetric` (o composable que já existe): **CORRENDO** (n) e **CONCLUÍRAM** (n).
5. `esp.score`: `CompetitionCard(Modifier.weight(1f))` (a única região elástica): `SectionHeading("Placar ao vivo", trailing = "Dados do servidor")` + `Column(verticalScroll)` com uma `RankingPersonRow` por corredor.
6. `esp.note`: ícone `Info` 16 dp + "Você está assistindo. Nada é registrado no seu perfil." (12 sp `TextMuted`, centralizado).
7. `esp.exit`: `CompetitionAction("Sair do modo espectador", secondary = true)`, largura total, 52 dp.
Alturas fixas somam ~520 dp; o placar recebe o resto (~330 dp = **4 linhas e meia**), por isso rola. **Em telas baixas só o placar encolhe** (nada some).

**5.3 Linhas do placar** (`RankingPersonRow`, 60 dp, **sem parâmetros novos**):
| Campo | Valor |
|---|---|
| `position` | 1, 2, 3... (a posição real; diferente da tela do corredor, que passa 0) |
| `initials` / `name` | `name.take(2).uppercase()` / nome; se `uid == meu uid`: "Você" e `currentUser = true` |
| `league` | `""` (igual à tela do corredor: não há liga nos dados da prova; o escudo claro de 16 dp é o mesmo de hoje) |
| `points` | `"%.2f km"` pt-BR de `progressKm` (concluído: `5,00 km`) |
| `detail` | concluiu: `"Concluiu em mm:ss"`; envio pendente: `"Envio pendente"`; sinal parado há > 30 s (ou sem `updatedAt`): `"Sinal atrasado"`; senão `"Tempo: mm:ss"` (de `timeSec`) |
| `movement` | ritmo `m:ss /km` de `paceSecPerKm` (`"—"` se nulo ou 0); concluído: `"Concluiu"` |
Ordem: **quem concluiu primeiro (por `finishedAt`)**, depois os demais por `progressKm` desc, `timeSec` asc, `uid` (a mesma regra da tela do corredor). Quem **desistiu** (`withdrawnAt != null`) fica fora do placar ao vivo (como hoje) e aparece **no fim, com "Desistiu"**, só na vista encerrada.

**5.4 Vista encerrada** (`fin.*`), quando `mission.status != LIVE` (ou seja `FINISHED`/`NOT_STARTED`): pílula do topo vira `CompetitionPill("ENCERRADA", color = TextMuted, active = true)`; o tempo, o líder e as métricas somem; entram (a) `CompetitionCard(border = Brass)` com ícone troféu 44 dp (`Brass` 15%) + **"{Nome} venceu"** (display 20 sp bold, maiúsculas) e "5K em 21:58 · 5 de 6 concluíram" (13 sp `TextMuted`); (b) o placar (**"Resultado final"**), mesma lista; (c) `CompetitionAction("Ver resultado completo")` (vermelho, primário) -> `go(RESULT)`, e `CompetitionAction("Voltar às missões", secondary = true)` -> Hub. Sem concluintes (`NOT_STARTED`): o card diz "Prova sem resultado" e o botão primário não aparece.

## 6. Mudanças no código (na ordem; compilar após cada bloco)
**6.1 `CompetitionComponents.kt`**: novo componente ao lado de `CompetitionShareIcon`:
```kotlin
@Composable
fun CompetitionWatchIcon(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(48.dp).clip(CircleShape).background(Olive.copy(alpha = .12f))
            .border(1.dp, OliveDark, CircleShape).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { Icon(Icons.Default.Visibility, contentDescription = "Assistir ao vivo", tint = Olive, modifier = Modifier.size(22.dp)) }
}
```
(o chamador passa `Modifier.size(44.dp)`, como já se faz com o compartilhar; `Visibility` vem do `material-icons-extended`, que o projeto já usa, por exemplo em `EmojiEvents`).
**6.2 `CompetitionFlowScreen.kt`**
- `private enum class CompetitionPage { ..., NOTICES, SPECTATE }`.
- No `HUB`: novo argumento `onWatch = { mission -> model.chooseMission(mission); go(CompetitionPage.SPECTATE) }` e passá-lo a `MissionCard`. **`chooseMission` não grava nada**: só seleciona e garante o listener de `players` dessa missão.
- Novo ramo no `when (page)`: `CompetitionPage.SPECTATE -> MissionSpectatorScreen(mission = state.selectedMission, onBack = { go(CompetitionPage.HUB) }, onResult = { go(CompetitionPage.RESULT) })`. O `BackHandler` que já existe (volta ao Hub) cobre o botão do sistema.
- `MissionCard`: novo parâmetro `onWatch: () -> Unit`; no ramo da linha de 74 dp trocar `CompetitionShareIcon(onShare, Modifier.size(44.dp))` por
```kotlin
if (mission.status == MissionStatus.LIVE) CompetitionWatchIcon(onWatch, Modifier.size(44.dp).testTag("btn_assistir_missao"))
else CompetitionShareIcon(onShare, Modifier.size(44.dp))
```
- Novo `@Composable private fun MissionSpectatorScreen(mission: DemoMission, onBack: () -> Unit, onResult: () -> Unit)` ao lado de `MissionRaceScreen`, `testTag("screen_spectator")`, seguindo a seção 5. Regras: relógio `val now by produceState(ServerTimeSync.nowSync()) { while (true) { value = ServerTimeSync.nowSync(); delay(1_000L) } }` (re-renderiza o tempo e o "sinal atrasado" sem depender de novo dado do servidor); tempo de prova `((now - startsAt.toDate().time) / 1000).coerceAtLeast(0)` via `formatElapsed`; `ao vivo = mission.status == MissionStatus.LIVE`; contagens e ordenação como na seção 5.3. Reaproveitar `formatElapsed` (já privada no arquivo).
- Tags de teste: `screen_spectator`, `spectator_leader`, `spectator_score`, `btn_sair_espectador`, `btn_ver_resultado_espectador`.
**6.3 Nada em `OnlineCompetitionRepository`, `CompetitionDemoState`, `firestore.rules` nem `AndroidManifest`.** O estado `selectedMission` já é atualizado em tempo real pelo coletor de `observeDistance`.

## 7. Estados
| Estado | Onde ver | Observação |
|---|---|---|
| Hub com botão assistir | `screens/espectador--hub.png` | só a linha EM ANDAMENTO muda |
| Espectador ao vivo | `screens/espectador.png` | padrão do protótipo |
| Prova encerrada | `screens/espectador--encerrada.png` | troca automática quando o status deixa de ser LIVE |
| Sinal atrasado | linha 4 do ao vivo | texto "Sinal atrasado" (cortado se > 1 linha; é por isso que não leva "Tempo:") |
| **Só em código:** sem dados ainda (`results` vazio) | - | placar com o texto "Aguardando os primeiros dados" (`TextMuted` 14 sp) e tempo de prova normal |
| **Só em código:** sem conexão (`onlineSyncError != null`) | - | linha "Falha na sincronização..." em `Red` 12 sp acima do placar, como no Hub; o placar mantém o último dado |
| **Só em código:** prova sem resultado | - | seção 5.4 |

## 8. Motion
Nada anima em repouso. O ponto de "AO VIVO" é estático (como na tela do corredor).
| Elemento | Gatilho | Efeito | Duração | Reduzir animação |
|---|---|---|---|---|
| botão assistir | toque | o mesmo do `CompetitionShareIcon` (ripple padrão) | - | igual |
| troca ao vivo -> encerrada | status muda | `Crossfade` | 200 ms, `FastOutSlowInEasing` | `snap()` |
| entrada da tela | abrir | a mesma troca de páginas do fluxo (sem animação própria) | - | igual |
| ordem do placar | ultrapassagem | **opcional**: `Modifier.animateItem()` se a versão do Compose tiver; senão troca seca | 250 ms | sem animação |
Reduzir animação: `Settings.Global.ANIMATOR_DURATION_SCALE == 0f` (padrão do projeto, ver `RunnaBottomNav`).

## 9. Armadilhas
- **Não** usar `joinMission`/`updateRoomPresence`/`startRace` no espectador: gravariam um jogador, aumentariam `playerCount` e desencadeariam a corrida. Conferir no Firestore depois do teste: `playerCount` igual e **nenhum** `players/{uid do espectador}`.
- `chooseMission(mission)` com missão diferente da atual chama `observeDistance` de novo (reanexa listeners). É o comportamento já usado pelo Hub; não duplicar chamada.
- O tempo da prova vem de `startsAt` + `ServerTimeSync.nowSync()` (relógio do servidor), **nunca** de `System.currentTimeMillis()`.
- `RankingPersonRow` sempre desenha o escudo de liga; passar `league = ""` mantém o visual da tela do corredor. Não inventar liga.
- Texto de detalhe longo é cortado com reticências (`maxLines = 1`): por isso "Sinal atrasado" substitui "Tempo: ..." em vez de somar.
- O espectador só vê missões da **mesma UF** (limitação existente da lista).
- `CompetitionFlowScreen` tem `LaunchedEffect(state.raceFinished) { go(RESULT) }`: só dispara para uma missão que **o próprio usuário** concluiu; não afeta o espectador.
- Custo do Firebase: o espectador **não adiciona** listeners (usa os do Hub), mas a `AUDITORIA_COMPETICAO_ONLINE` (A37) já alerta que os listeners de todas as missões são o maior consumo de leituras; mais espectadores = mais gente com o Hub aberto. Não piora o desenho atual, mas vale lembrar na decisão de escala.

## 10. Aceitação
- `diff_images.py`/`compare.py` da skill: `espectador`, `espectador--hub` e `espectador--encerrada` com média <= 3.0 e pior célula <= 10.0; `compare_bounds.py` em ±2 dp.
- **Verificação funcional (obrigatória, com 3 contas da mesma UF; o HTML não prova isso)**, em **2,5K, 5K e 10K**:
  1. Contas A e B entram numa missão e a prova começa; a conta C **não entra**.
  2. Em C, o Hub mostra a linha **EM ANDAMENTO com o botão olho** (e **sem** compartilhar); as outras linhas continuam com compartilhar.
  3. Tocar no olho abre o espectador; o placar mostra A e B com km, tempo e ritmo, e **atualiza sozinho** conforme A e B correm.
  4. A/B em modo avião por > 30 s: a linha mostra "Sinal atrasado".
  5. Alguém desiste: some do placar ao vivo.
  6. A e B concluem: a tela troca para **ENCERRADA** com o vencedor correto; "Ver resultado completo" abre a tela de resultado; "Voltar às missões" volta ao Hub.
  7. Voltar (seta e botão do sistema) em qualquer momento vai ao Hub.
  8. No Firestore: `playerCount` não mudou e **não existe** `players/{uid de C}`; o ELO/ranking de C não mudou.
- Desvios aceitos: fontes do protótipo caem em Arial Narrow se não carregarem; o escudo de liga é desenho aproximado; o protótipo mostra 6 corredores.

## 11. Perguntas em aberto
1. **Tocar na linha inteira** da prova ao vivo, para quem **não** está inscrito, hoje dá o erro "Esta missão já começou." Quer que abra o espectador? (recomendo; é 1 linha: se a missão é LIVE e o usuário não está em `results`, `go(SPECTATE)` em vez de `joinMission`). **Não incluí** por não ter sido pedido.
2. Aceita a troca compartilhar -> olho na linha EM ANDAMENTO (seção 5.1)? A alternativa é manter os dois ícones reduzindo as pílulas, o que muda o padrão atual.
3. O espectador deve ficar disponível também **antes** da prova (CONFIRMADA)? Aqui só existe para EM ANDAMENTO, como pedido.
4. As linhas "L~" são do `src_1.zip` enviado; se mudou, localizar pelo nome do composable.
