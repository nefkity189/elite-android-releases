# THEME — Elite (tokens compartilhados por todas as telas)

Fonte da verdade visual: `marca-e-tema/marca-e-tema.html` e `marca-e-tema/marca-e-tema-board.png`. Ícones em `marca-e-tema/assets/icons/*.svg`.
Dispositivo alvo: 393x851 dp (faixa 360–430 de largura). Tema escuro único. Sem emojis na UI.

## Cores (nomes = variáveis CSS dos protótipos)
| Token | Valor | Uso |
|---|---|---|
| --bg | #0A0B09 | fundo das telas |
| --s1 | #10120D | barra inferior, bottom sheets |
| --card / --card2 | #161911 / #1D2116 | cards, trilhas |
| --field | #0D0F0A | campos de texto |
| --line / --line2 | rgba(236,232,218,.10) / .18 | bordas 1px |
| --text | #ECE8DA | texto principal (osso) |
| --mut / --dim | #8F9384 / #5E6255 | texto secundário / desabilitado |
| --olive | #A3B565 | seleção, Pausar, abas ativas, progresso |
| --olive-d | #5D6B34 | switch ligado, bordas de seleção |
| --olive-s | rgba(163,181,101,.14) | fundo de chip/seleção |
| --on-olive | #0E1306 | texto sobre oliva |
| --red | #D12B2B | ação principal (Começar treino, Parar) |
| --red-s | rgba(209,43,43,.15) | fundo de aviso destrutivo |
| --on-red | #FFF4F0 | texto sobre vermelho |
| --brass | #C9A24B | conquistas, patentes, destaques secundários |
| --amber | #E0A93A | avisos, treino em pausa |
| --ok | #7FB069 | sucesso |

## Mapa do app atual -> Elite (do board de marca)
RunnaBackground/AisleBackground -> #0A0B09 · RunnaGlass (82%) -> #10120D a 82% · RunnaGlassElevated -> #1D2116 a 92% ·
RunnaTeal/AisleTurquoise -> #A3B565 · RunnaTealOn -> #0E1306 · RunnaOrange/AisleActionOrange -> #D12B2B ·
RunnaOrangeOn -> #FFF4F0 · AislePurple e BrandGradient -> removidos (oliva sólido) · AisleTextAccent2 -> #C9A24B ·
RunnaTextPrimary/Secondary -> #ECE8DA / #8F9384.

## Tipografia (arquivos de fonte a baixar e colocar em res/font)
- Barlow Condensed 500, 600, 700 — títulos (caixa alta) e números tabulares (`--num`)
- Inter 400, 500, 600, 700 — texto e interface (`--ui`); corpo 14/20, rótulos 11 com 0,12em, caixa alta
- Saira Stencil One 400 — somente o logotipo ELITE (espaçamento 0,24em)
Valores exatos de tamanho/peso/altura de linha por elemento: `spec/*.md` de cada tela.

## Forma e ícones
Raios de card 14–28 dp, botões pílula, bottom sheet 28 dp no topo. Ícones 24x24, traço 2, pontas redondas (play/pause/stop preenchidos). Símbolo: escudo com lâmina "E" (`mark.svg`, `blade.svg`).

## Animações (globais)
Único elemento animado em repouso: botão central "E" da barra inferior (e o ponto pulsante do mapa na Corrida).
| Elemento | Gatilho | Detalhe |
|---|---|---|
| Borda do botão central | repouso | gradiente cônico (oliva, vermelho, latão) girando, 5 s por volta, linear |
| Brilho do botão central | repouso | respira até 35% de oliva, 3,2 s ease-in-out (glow 16 dp) |
| Toque no botão central | tap | pulo elástico 0,5 s, giro do símbolo 0,6 s (rotateY 360), onda 0,6 s (escala 1 -> 1,7) |
| Ponto no mapa | repouso | escala 0,6 -> 2,2, opacidade 0,5 -> 0, 2 s ease-out, infinito |
| Menu radial/rápido | abrir | arco 0,25 s, itens 0,28 s, cubic-bezier(.2,.8,.2,1); botão gira 90° |
| Bottom sheets | abrir/fechar | sobe 0,3 s; fundo escurece 0,2 s |
| Todas | reduzir movimento | sem animação nem transição |
