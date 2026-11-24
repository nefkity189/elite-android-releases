# Correções do layout (Claude Code não ficou igual ao protótipo)

Não havia compilador Kotlin no ambiente, então **não compilei**. Rode o build e me mande qualquer erro que eu ajusto.

## 1) FONTES (a maior diferença visual)
`Type.kt` usava `FontFamily.SansSerif` no lugar de Barlow Semi Condensed e Inter, por isso os números e títulos ficavam diferentes.
Agora ele aponta para `R.font.*`. **Antes de compilar**, baixe no Google Fonts e coloque em `app/src/main/res/font/`, com estes nomes exatos (minúsculos, sem hífen):

- barlow_semi_condensed_medium.ttf / barlow_semi_condensed_semibold.ttf / barlow_semi_condensed_bold.ttf
- inter_regular.ttf / inter_medium.ttf / inter_semibold.ttf

Se o `R` do seu app não for `com.example.R`, troque em `theme/Type.kt`.

## 2) Tela de corrida (ActiveRunScreen e componentes)
- **Mapa coberto:** o gradiente usava `linearGradient` de 1 px (virava uma camada sólida de 85% sobre o mapa). Agora usa `verticalGradient` com os 5 stops do protótipo.
- **Barra Pausar/Parar subia:** o espaço do meio estava com `weight(1f, fill = false)`; agora `weight(1f)` e a barra fica presa na base.
- **Menu radial:** reescrito (`RadialMenu.kt`). A geometria estava errada (arco deslocado, botões fora da área e dos toques). Agora o contêiner tem 160dp, o arco tem o canto no centro do FAB e os botões ficam a 80dp em 180°, 135° e 90°. Fecha ao tocar fora, e é desenhado como overlay por cima de tudo.
- **ActionBar:** reescrita. O clique disparava quando o toque era cancelado (`upOrCancel == null`); agora usa `clickable`. Barra em largura total, padding 16/16/16/18, borda de 1 px.
- **Chip de GPS:** o texto fica cinza (`Muted`) e só o ponto muda de cor.
- **Tipografia:** botões em 16sp e números em SemiBold (600), como no protótipo.

## 3) Tela "Hoje" (TodayScreen)
Ela ainda estava no layout antigo (LazyColumn, rolagem, cores `Aisle*`, gradientes, pílula roxa). Foi refeita:
- cabeçalho, faixa da semana (hoje = círculo verde-água cheio), card do treino (barra de 9 semanas, "Treino 1", gráfico de intensidade, resumo em 3 blocos, botão laranja), blocos de meta semanal, corridas recentes e rodapé;
- **sem rolagem**: o card do treino ocupa o espaço que sobra. Só quando há aviso (pausa, recuperação, sugestão, ajuste) a tela ganha rolagem de reserva;
- toda a lógica foi mantida (check-in, recuperação de corrida, sugestões, ajustes, dia de descanso).

## 4) Barra inferior e botão A (novo arquivo `components/RunnaBottomNav.kt`)
Ele não estava no zip. Use `RunnaBottomNav(selected, onSelect, onCoachClick)` no lugar da sua barra atual.
O botão A fica dentro da barra, com borda em degradê girando, brilho que respira e pulo, giro e onda ao tocar. Respeita "reduzir animações".
