# THEME — Corrida ativa com mapa (4 modalidades) — tokens, componentes compartilhados e animações

Vale para as 4 telas desta pasta: `corrida-livre/`, `plano-2-5k/`, `plano-5k/`, `plano-10k/`.
Origem: `elite.zip` (pacote `com.elite.sch.app`), arquivo `ui/screens/ActiveRunScreen.kt`. Números em dp = px CSS (393×851).

## 1. Como as 4 modalidades se dividem
`isFreeRun = runState.modality == "livre"`. Só o **topo (hero)** muda; mapa, pill, leque, menu do topo, Pausar/Parar e GPS são os mesmos.
| modalidade | tela | hero |
|---|---|---|
| livre | `corrida-livre/` | `FreeRunLayout`: anel 112 + "Corrida livre" + Distância/Ritmo |
| 2,5K, 5K, 10K | `plano-2-5k/`, `plano-5k/`, `plano-10k/` | layout de treino estruturado: ícone+Corra/Caminhe, "Etapa X de Y", anel com progresso do treino, Distância/Ritmo, e card "A seguir" acima da barra de ações |
As três telas de plano têm a **mesma estrutura**; mudam só os dados das etapas (cada pasta traz os exemplos reais dos geradores).

## 2. Tokens (nome CSS = `RunnaTheme.kt` / `Color.kt`)
| token | valor | uso |
|---|---|---|
| --background | #0A0B09 | fundo; base do gradiente sobre o mapa |
| --glass | rgba(16,18,13,.82) (`Glass` 0xD110120D) | chip GPS, botões fechados, pill, ActionBar, fundo do anel |
| --glass-elevated | rgba(29,33,22,.92) | arcos dos leques |
| --line | rgba(236,232,218,.10) | bordas de 1dp |
| --text | #ECE8DA | texto principal |
| --muted | #8F9384 | rótulos, "Etapa X de Y", ícone de toggle desligado |
| --teal (= Olive) | #A3B565 | anel (livre e plano em andamento), ícones de ação, toggle ligado, botão Pausar |
| --teal-on | #0E1306 | ícone/texto sobre oliva |
| --brass | #C9A24B | ícone+rótulo "Caminhe" (qualquer etapa que não seja corrida) |
| --amber | #E0A93A | plano **pausado**: anel de progresso e tempo |
| --red / --red-on | #D12B2B / #FFF4F0 | botão Parar |
| --item-off-bg | #1D2116 | botões de leque desligados/ação |
| --card-elevated | #1D2116 | card "A seguir" (cor padrão do Card do tema = `surfaceVariant`; **confirmar no aparelho**) |
Dimensões: topo 64 · chip 32 · FAB do topo 48 · âncora do leque inferior 46 · botões de leque 44 · arco 132 · anel 112 (traço 4) · stats 64 · ActionBar 90 (padding 16/16/16/18; botões 56, raio 28; raio da barra 26 em cima).

## 3. Tipografia (`Type.kt`)
Barlow Condensed (números/títulos) e Inter (texto). Arquivos: `res/font/barlow_condensed_*.ttf`, `inter_*.ttf`.
- Cronômetro (dentro do anel): Barlow Condensed 600, 32/32, tabular.
- Distância/Ritmo: Barlow Condensed 600, 40/40; unidade "km" 20/24 600 `--muted`; rótulo Inter 600 11/16, espaçamento 1,32 `--muted`.
- "Corra"/"Caminhe" (`titleLarge`): Barlow Condensed 600, 20/23, espaçamento 0,8.
- "Etapa X de Y" (`bodyMedium`): Inter 400, 14/19,6 `--muted`.
- "Corrida livre": Inter 600 11/16, espaçamento 1,32 `--teal`.
- Pill da câmera: Inter 13/16; card "A seguir": Inter 12,5 (altura de linha automática ≈15) `--muted`.

## 4. Camadas (de trás para frente)
1. `MapViewComposable` em tela cheia (MapLibre + OpenFreeMap). 2. Gradiente `RunnaTheme.GradientStops` sobre #0A0B09: 0→.96, .30→.94, .43→.90, .53→0, .76→0, 1.0→.85.
3. Coluna: topo (chip GPS) · hero · spacer flexível (`weight(1f)`) · bloco inferior (card "A seguir" + ActionBar). 4. Scrim de tela cheia (só com um leque aberto). 5. `MapStyleFan` (inferior direito). 6. `RadialMenu` (topo direito).
Insets: o app aplica `statusBarsPadding` e `navigationBarsPadding` na raiz; os protótipos desenham dentro da área útil.

## 5. Componentes compartilhados

### 5.1 TimerRing (`TimerRing.kt`) — mudou no elite.zip
112×112. Fundo: círculo `--glass` (raio 56). Anel de fundo: círculo `--line`, raio 52, traço 4 (ocupa 50–54). Anel de progresso: mesmo raio e traço, **pontas arredondadas**, começa no topo (−90°) e anda no sentido horário; `sweep = 360 × progresso`. Texto: cronômetro 32/32, centralizado.
- Corrida livre: `progress = 0`, anel `--teal`, texto `--text`. Tempo = tempo decorrido.
- Plano: `progress = tempo decorrido ÷ duração total das etapas` (0–1); anel `--teal` e texto `--text`; **pausado**: anel e texto `--amber`. Tempo mostrado = tempo decorrido total (não o da etapa).
- Com progresso 0 e ponta arredondada, o Canvas pode desenhar um ponto no topo: confirmar no aparelho (o protótipo não desenha nada).

### 5.2 StatsRow — Distância (km, 2 casas, vírgula) e Ritmo (min/km). Divisor de 1×34. Altura 64. Igual nas 4 telas. (Plano: dentro da coluna com 16 de padding lateral → 361 de largura; livre: 393.)

### 5.3 ActionBar — EXISTENTE, manter igual (`ActionBar.kt`)
Dois botões de 56 com 12 de espaço. **Pausar** (`--teal`, texto `--teal-on`, ícone Pause 18): vira **Retomar** (ícone Play) quando pausado; ao tocar, escala 0,96 com mola (amortecimento 0,7, rigidez 300) e volta. **Parar** (`--red`, texto `--red-on`, ícone Stop 18, borda interna vermelha 30%): **segurar 1,5 s** para confirmar; durante a retenção aparece um anel de progresso de 36 (traço 3, cor `--red-on`, sentido horário a partir do topo, linear, 1500 ms) por cima do conteúdo do botão; soltar antes cancela e o anel some na hora (sem animação de volta); ao completar chama a ação de parar. **O texto continua "Parar" durante a retenção** (mudou no elite.zip; antes virava "Solte p/ cancelar"). O Parar não tem animação de escala (o estado `pressed` nunca é alterado no código).

### 5.4 RadialMenu do topo direito — EXISTENTE, manter igual (`RadialMenu.kt`)
Contêiner 160×160 no canto superior direito (padding 16/16). Botão âncora 48 (engrenagem). Aberto: âncora gira 90° em 300 ms e fica `--teal` com ícone `--teal-on`. Arco `--glass-elevated` de 132, canto superior-direito = centro da âncora; escala .2→1 + opacidade, 250 ms, origem no canto, `FastOutSlowIn`. Três botões de 44 a **80** do centro da âncora, 280 ms `FastOutSlowIn`, atraso 0/50/100 ms ao abrir (sem atraso ao fechar), escala .5→1 + opacidade:
Som (180°: −80, 0) · Vibração (135°: −56,6, +56,6) · GPS ativo (90° para baixo: 0, +80). Ligado = fundo `--teal` + ícone `--teal-on`; desligado = `--item-off-bg` + `--muted`. Botões fechados não recebem toque. Toque fora fecha (scrim). **Não alterar.** Abrir um leque fecha o outro.

### 5.5 Pill da câmera — texto muda conforme o modo escolhido em Perfil
Canto inferior esquerdo da região do mapa (left 16, bottom 14 acima do bloco inferior). Vidro, borda `--line`, raio 15, padding 7/13, Inter 13, cor `--text`. testTag `map_pill_following`.
| Perfil → "Modo de câmera padrão" | `CameraMode` | texto do pill | o que o mapa faz |
|---|---|---|---|
| Seguir você | FollowUser | **Seguindo você** | câmera segue a posição |
| Com direção | FollowUserWithBearing | **Seguindo com direção** | segue e gira o mapa pelo rumo |
| Rota completa | ShowFullRoute | **Rota completa** | enquadra toda a rota percorrida |
| (não usado hoje) | Free | **Mapa livre** | gestos livres |
O modo inicial da corrida **vem do Perfil** (no elite.zip a corrida sempre começava em "Seguir você": `MainActivity` iniciava `cameraMode` fixo). O pill passa a aparecer em todos os modos (antes sumia em "Rota completa"). Largura medida no protótipo (fonte reserva): Seguindo você ≈118, Seguindo com direção ≈164, Rota completa ≈114, Mapa livre ≈90.

### 5.6 MapStyleFan — NOVO, inferior direito (`MapStyleFan.kt`, já escrito)
Substitui o botão "Centralizar no mapa" que não tinha ação. Contêiner 160×160 na RAIZ, `align(BottomEnd)`, `padding(end = 16, bottom = alturaMedidaDoBlocoInferior + 10)`. **O bloco inferior é o card "A seguir" (quando existe) + ActionBar**: na corrida livre = 90 → bottom 100; no plano com card = 153 → bottom 163; na última etapa (sem card) = 90 → 100.
Âncora 46 (canto inferior direito do contêiner): fechada = `--glass` + ícone Layers `--teal`; aberta = `--teal` + ícone Close `--teal-on`. testTag `btn_map_recenter`. Arco 132, canto inferior-direito = centro da âncora, raio 132 no canto superior-esquerdo. Quatro botões de 44 a **96** do centro da âncora, 280 ms, atraso 0/50/100/150 ms ao abrir:
| item | ângulo | deslocamento do centro da âncora | tipo | ícone |
|---|---|---|---|---|
| Centralizar no mapa | 90° | (0, −96) | ação | MyLocation |
| Modo escuro | 120° | (−48, −83,1) | toggle | DarkMode |
| Tipo de mapa | 150° | (−83,1, −48) | ação | Map |
| Nomes de ruas | 180° | (−96, 0) | toggle | TextFields |
Comportamento: **Centralizar** volta ao modo do Perfil (`mapPreferences.toCameraMode()`) e fecha; **Modo escuro** usa o estilo `dark`; **Tipo de mapa** gira Padrão (liberty) → Claro (positron) → Contraste (bright) e desliga o escuro, com aviso "Mapa: nome"; **Nomes de ruas** liga/desliga (`hideAllLabels`). Offline: escuro e tipo ficam com opacidade .4 e avisam "Disponível só no mapa online". Trocar o estilo recarrega o mapa e recria rota/início/posição (`setStyle` descarta camadas feitas por código); a câmera não muda.

## 6. Animações (resumo)
| elemento | origem | gatilho | duração / easing | reduced-motion |
|---|---|---|---|---|
| Menu do topo (arco, 3 botões, âncora gira 90°) | EXISTENTE | tocar âncora | 250 / 280 (+0/50/100) / 300 ms, FastOutSlowIn | sem animação |
| Pausar: escala .96 | EXISTENTE | pressionar | mola (0,7 / 300) | — |
| Parar: anel de retenção | EXISTENTE | segurar | 1500 ms linear; soltar zera | — |
| Leque do mapa (arco, 4 botões) | NOVO | tocar âncora | 250 / 280 (+0/50/100/150) ms, FastOutSlowIn | 0 ms |
| Câmera (rota completa, direção) | EXISTENTE (MapLibre) | trocar modo / nova posição | ~500 ms | — |
| Troca do estilo do mapa | NOVO | tocar item do leque | recarga do estilo | — |
Em repouso **nada anima**. Botões fechados dos leques não ficam na árvore (sem foco/toque).

## 7. Divergências e pontos a confirmar (achados na leitura do código)
1. **Contagem regressiva da etapa e instrução sumiram do hero do plano** com mapa: hoje o usuário vê só "Corra/Caminhe", "Etapa X de Y" e o tempo TOTAL. O tempo restante da etapa (`stepTimeFormatted`) e a instrução da etapa atual ficaram sem uso nesse layout. Se não foi intencional, é uma regressão de usabilidade.
2. Card "A seguir": o texto é `"A seguir: " + instrução + " " + duração`, e a instrução já traz a duração ("2 min caminhada 2 min", "Série 3/8: 1 min 30 s caminhando 1 min 30 s"). Provável texto duplicado.
3. Nenhum padrão do app tem barra de etapas nesse layout agora (o `RunStepsBar` saiu do arquivo).
4. O leque do topo, ao abrir, cobre parte do hero (rótulo "Corra/Caminhe" e o topo do anel). É o comportamento atual; mantido.
5. Cor do card "A seguir" = padrão do `Card` do tema; confirmar.

## 8. Aproximações do protótipo (honestas)
Mapa ilustrativo (OpenFreeMap no app) e gradiente esconde a maior parte; comparar só topo/hero/barra/leques. Ícones de traço (no app: Material Icons preenchidos). Sem blur de fundo. Fontes não carregam no sandbox (largura dos textos pode diferir). Pressionar do Pausar usa curva cúbica no lugar da mola. Nada comparado em aparelho real; o Kotlin não foi compilado.

## 9. NÃO fazer
Não animar nada em repouso. Não mudar a lógica de GPS/pausa/rota/dados. Não alterar o menu do topo, o Pausar e o Parar. Não colocar leques dentro do spacer (toques fora do pai não chegam). Não usar emoji, blur, brilho colorido nem rótulos em caixa alta. Não chamar `setStyle` sem recriar as camadas.
