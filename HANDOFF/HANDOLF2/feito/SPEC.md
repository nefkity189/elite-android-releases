# SPEC — Tema Elite (todas as telas)

Números em dp (= px do CSS no protótipo), medidos em tela de 393x851 dp. Fonte da verdade visual: `screens/` (PNG 2x). Fonte dos números: `spec/` (uma medição por tela) e `html/` (protótipos animados). Tokens prontos para colar: `theme/EliteColors.kt`.

## 1. O que muda no app
1. Nome Aisle vira **Elite** (só textos visíveis; não renomear classes `Aisle*`/`Runna*`).
2. Novo tema escuro profissional inspirado em operações táticas: preto, oliva, osso, vermelho de sinalização, latão.
3. O botão central da barra inferior **não é mais o "A"**: passa a ser a lâmina "E" (`assets/android/ic_elite_blade.xml`).
4. Novo logo e novo texto de marca (seção 2).
5. Mantém toda a estrutura, lógica, fluxo e animações atuais. Mudam cores, fontes, textos de marca e o símbolo central.

## 2. Marca
- Símbolo (`ic_elite_mark.xml` / `svg/elite-mark.svg`): escudo (fundo #10120D, contorno oliva 2,5/64) com a lâmina "E" em osso e ponta vermelha.
- Lâmina sozinha (`ic_elite_blade.xml`): usada no botão central; a lâmina é oliva #A3B565 e a ponta é vermelha #D12B2B (não tingir com tint).
- Logotipo horizontal: símbolo 44x50 + gap 12 + "ELITE" em **Saira Stencil One** 26 sp, caixa alta, letter-spacing 0,24 em, cor osso. Abaixo, em 9 sp Inter 600, letter-spacing 0,2 em, cor muted: "MISSÃO DADA É MISSÃO CUMPRIDA" (sem quebra de linha). Versão vertical: símbolo 72x82, "ELITE" 38 sp.
- Onde trocar o "A" e o nome: `RunnaBottomNav.kt` linha ~266 (texto "A" com Brush), `AisleComponents.kt` (`AisleSymbol`, texto "Aisle" ~linha 354), `WorkoutReminderWorker.kt` linha ~98 (título da notificação) e `ShareCardGenerator.kt` (slogan do cartão). Conferir também o ícone do app e `app_name` nos recursos.

## 3. Tokens
| Token (`EliteColors`) | Valor | Uso | Substitui |
|---|---|---|---|
| Background | #0A0B09 | fundo das telas, base do scrim | RunnaBackground / AisleBackground |
| Surface | #10120D | barra inferior, sheets, base do vidro | RunnaGlassBg |
| Card / CardElevated | #161911 / #1D2116 | cards; arco do menu, trilhas, item inativo | Navy*, RunnaGlassElevated |
| Field | #0D0F0A | campos de texto | RunnaField |
| Line / LineStrong | 10% / 18% de #ECE8DA | bordas 1 px, divisores | RunnaLine, AisleFieldBorder |
| Text / TextMuted / TextDim | #ECE8DA / #8F9384 / #5E6255 | textos | RunnaTextPrimary/Secondary, AisleTextTertiary |
| Olive / OliveDark / OliveSoft / OnOlive | #A3B565 / #5D6B34 / 14% / #0E1306 | seleção, Pausar, abas ativas, switch ligado, texto sobre oliva | RunnaTeal, AisleTurquoise, TealSoft, RunnaTealDark |
| Red / RedSoft / OnRed | #D12B2B / 15% / #FFF4F0 | Começar treino, Parar, destrutivo | RunnaOrange, AisleActionOrange, RunnaOrangeDark |
| Brass | #C9A24B | conquistas, patente, destaques secundários | AislePurple, Violet*, CoachLilac |
| Amber | #E0A93A | avisos de saúde, pausa | Amber400 |
| Ok | #7FB069 | GPS ativo, ponto verde | Green400 |

Gradientes teal→roxo (`BrandGradient`) deixam de existir: usar oliva sólido. Contraste verificado: texto osso sobre o scrim sobre mapa branco 15:1; muted 5,5:1.

## 4. Tipografia
Fontes a embutir em `res/font/` (nomes em minúsculas): Barlow Condensed (Medium 500, SemiBold 600, Bold 700), Inter (Regular 400, Medium 500, SemiBold 600, Bold 700) e Saira Stencil One (só logotipo). No render do sandbox as fontes NÃO carregaram (sem rede); conferir larguras no aparelho.
| Estilo | Fonte | Tamanho/linha | Peso | Extras |
|---|---|---|---|---|
| Título de tela (t1) | Barlow Condensed | 30/32 | 700 | caixa alta, letter-spacing 0,03 em |
| Título de cartão/seção (t2) | Barlow Condensed | 20/23 | 600 | caixa alta, 0,04 em |
| Título de topo | Barlow Condensed | 24/24 | 700 | caixa alta, 0,04 em |
| Número grande (cronômetro) | Barlow Condensed | 32 | 600 | tabular |
| Números de estatística | Barlow Condensed | 40 (corrida), 30 (cards), 28 (resumo) | 600 | tabular |
| Rótulo (lb) | Inter | 11 | 600 | caixa alta, 0,12 em, cor muted |
| Corpo | Inter | 14/19,6 | 400 | |
| Corpo pequeno | Inter | 12 | 400 | |
| Botão | Inter | 16 (44 dp: 14) | 700 | |
| Chip / aba | Inter | 12,5 / 11 | 400 / 500 | |

## 5. Formas e medidas
- Tela: 393x851, padding horizontal 16, topo 22 (+ status bar), gap vertical 14 (corrida 8–10).
- Card: raio 18, padding 14, borda 1 px Line. Card selecionado: borda Olive, fundo degradê OliveSoft→Card, barra de 3 dp à esquerda (inset 14 em cima/baixo).
- Botão principal: altura 56, raio 28. Secundário pequeno: 44, raio 22. Fantasma: borda 1 px LineStrong.
- Chip: altura 30, raio 15, padding horizontal 12. Tag: 10 sp caps, raio 6, padding 3x8.
- Ícone em quadrado (ico): 40x40, raio 12, fundo OliveSoft, ícone Olive.
- Botão ícone: 44 (círculo, fundo Card, borda Line). Switch: 50x30, bolinha 22. Barra de progresso: altura 6, raio 3.
- Seleção segmentada: altura 44, raio 14, ativo Olive. Escala de 5: grade 5 colunas, gap 6.
- Campo: altura 52, raio 14, borda 1 px LineStrong (foco: Olive).
- Semana (strip): 7 colunas, gap 6, célula 58 altura, raio 14; hoje = Olive cheio; feito = número Olive com check.
- Bottom sheet: raio 28 nos cantos de cima, fundo Surface, borda LineStrong, alça 40x4, padding 10/16/24. Scrim 60% preto, 0,2 s.
- Diálogo: margem lateral 24, raio 24, fundo CardElevated, padding 20, gap 12.
- Barra inferior: altura 64, margem 12 laterais e 10 abaixo, raio 24, fundo Surface, borda Line. Abas: ícone 22 + rótulo 11; ativa em Olive.

## 6. Botão central (substitui o "A") e animações
Botão 48x48, raio 16, no centro da barra. Borda de 2 dp com gradiente cônico girando (Olive → Red → Brass → Olive); miolo raio 14 com radial #262B1A→#0C0E08; símbolo = lâmina "E" 26 dp.
| Animação | Gatilho | Detalhe |
|---|---|---|
| Borda cônica | repouso | uma volta a cada 5 s, linear, infinita |
| Brilho | repouso | sombra Olive de 0 a 35% e volta, 3,2 s, ease-in-out, infinita |
| Pulo | toque | escala 0,88 → 1,12 → 1, 0,5 s, overshoot |
| Giro do símbolo | toque | rotação Y 360°, 0,6 s |
| Onda | toque | contorno Olive 2 dp, escala 1 → 1,7 e opacidade 0,7 → 0, 0,6 s |
| Pulso do mapa | repouso | halo r=14: escala 0,6 → 2,2, opacidade 0,5 → 0, 2 s ease-out, infinito |
| Menu radial | abrir | arco 0,25 s + itens 0,28 s, curva (0,2; 0,8; 0,2; 1); botão gira 90° e fica Olive |
| Sheets | abrir/fechar | sobem 0,3 s, mesma curva |
| Reduzir movimento | sempre | sem animação nem transição |

## 7. Scrim sobre o mapa (corrida) — regra crítica
Degradê vertical da altura total da tela, cor Background, paradas (fração → alpha): 0,00 → 0,96 · 0,30 → 0,94 · 0,43 → 0,90 · 0,53 → 0,00 · 0,76 → 0,00 · 1,00 → 0,85 (`EliteTopScrimStops`). Desenhar acima do mapa e abaixo do conteúdo. Não esticar para baixo do ponto de posição (ele apagaria o trajeto). O texto do cronômetro fica SEMPRE acima do anel.

## 8. Telas: PNG, medidas e arquivo Kotlin
| Protótipo (`html/`) | Tela | PNG / medidas | Arquivo Kotlin |
|---|---|---|---|
| elite-onboarding | 1 Boas-vindas | `screens/elite-onboarding/01-boas-vindas.png` | WelcomeScreen.kt |
| | 2–3 Quiz (3 perguntas) | `02-quiz-pergunta-1-de-3`, `03-quiz-pergunta-3-de-3` | QuizScreens.kt |
| | 4 Objetivo | `04-escolha-seu-objetivo` | GoalSelectionScreen.kt / GoalScreen.kt |
| | 5 Dias | `05-dias-de-treino` | DaysSelectionScreen.kt |
| | 6 Duração | `06-duracao-do-plano` | DurationScreen.kt |
| | 7 Prévia | `07-previa-do-plano` | PlanPreviewScreen.kt |
| elite-hoje | 1 Hoje | `01-hoje` | TodayScreen.kt |
| | 2 Menu rápido | `02-menu-rapido-botao-central` | AisleQuickLauncherBottomSheet.kt |
| | 3 Check-in | `03-check-in-antes-do-treino` | PreWorkoutCheckinBottomSheet.kt |
| | 4 Concluído + ajuste | `04-hoje-treino-concluido` | TodayScreen.kt |
| | 5 Sem plano | `05-hoje-sem-plano` | TodayScreen.kt |
| | 6 Permissão de localização | `06-permissao-de-localizacao` | AisleDialogs.kt |
| elite-plano | 1 Plano | `01-plano-de-treino` | FullPlanScreen.kt, WeekCard.kt, WorkoutRow.kt |
| | 2 Gerenciar plano | `02-gerenciar-plano` | PlanActions.kt |
| | 3 Pausa | `03-preciso-de-uma-pausa` | PauseFlow.kt |
| | 4 Excluir | `04-excluir-plano` | AisleDialogs.kt |
| elite-corrida | 1 Corrida do plano | `01-corrida-do-plano` | ActiveRunScreen.kt |
| | 2 Corrida livre | `02-corrida-livre` | FreeRunLayout.kt |
| | 3 Em pausa | `03-treino-em-pausa` | ActiveRunScreen.kt, PlayPauseRing.kt |
| | 4 Encerrar | `04-encerrar-treino` | AisleDialogs.kt |
| | 5 Resumo | `05-resumo-do-treino` | WorkoutSummaryScreen.kt |
| | 6 Feedback | `06-como-foi-o-treino` | PostWorkoutFeedback.kt |
| elite-progresso-perfil | 1 Progresso | `01-progresso` | ProgressScreen.kt |
| | 2 Detalhe da corrida | `02-detalhe-da-corrida` | ProgressScreen.kt |
| | 3–4 Perfil | `03-perfil`, `04-perfil-dados-e-sobre` | ProfileScreen.kt, SettingsComponents.kt |
| | 5 Ajustes do plano | `05-ajustes-do-plano` | PlanAdjustmentsScreen.kt |
| | 6 Cartão de compartilhar | `06-cartao-de-compartilhamento` | ShareCardGenerator.kt |
| elite-marca | Marca, paleta, componentes | `screens/marca-e-tema.png` | Color.kt, Type.kt, Theme.kt |
Cada PNG tem o mesmo nome base em `spec/<pagina>/<nome>.md` com caixas, cores, fontes e raios medidos.

## 9. Textos que mudam
"Aisle" → "Elite"; "Bem-vindo ao Aisle" → "Bem-vindo ao Elite"; "Corredor Iniciante" → "Recruta"; "Conquistas do Plano" → "Condecorações do plano"; slogan do cartão → "Missão dada é missão cumprida". Demais textos permanecem como estão no app.

## 10. Aproximações conhecidas (honestas)
- `backdrop-filter: blur` das barras e chips não existe em Compose: usar só o fundo (Glass).
- O mapa dos protótipos é ilustrativo; no app é o mapa real (pode ser branco). Só o scrim e as posições valem.
- Dados dos protótipos são exemplos ("Alex", 2,40 km, tipos de dor, datas).
- Em telas roláveis (Plano, Progresso, Perfil, Resumo) o PNG mostra só a parte visível; o conteúdo continua rolando e o botão principal fica fixo embaixo (sticky).
- O link "Não mostrar de novo" tem 18 dp visuais: dar área de toque de 44 dp sem mudar o visual. Idem demais alvos pequenos do lint.
- Com o menu radial aberto, o item de GPS cobre o "×" do aviso (comportamento já existente).
- Nada foi comparado em aparelho real.

## 11. NÃO fazer
- Não renomear classes, pacotes, rotas nem mexer em lógica, dados ou navegação.
- Não usar gradiente teal→roxo, brilho decorativo extra, emojis, nem caveira/faca copiada de marca de terceiros: o símbolo é só o escudo com a lâmina "E".
- Não tingir a lâmina com tint (a ponta vermelha é fixa).
- Não clarear o scrim atrás de Distância e Ritmo.
