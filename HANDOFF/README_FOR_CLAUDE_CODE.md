# Tarefa: replicar o redesign "Elite" em Jetpack Compose (Kotlin)

Use a skill `html-to-native-replica` (em `.claude/skills/`; leia o SKILL.md dela primeiro e siga os passos). Os scripts dela ficam em `.claude/skills/html-to-native-replica/scripts/` (render, android_shot.sh, compare.py).

Material (leia ANTES de codar; olhe os PNGs com leitura de imagem):
- `THEME.md`: cores, fontes, mapa de tokens do app atual -> Elite, animações globais.
- `marca-e-tema/`: board da marca (HTML + PNG) e `assets/icons/*.svg` (ícones 24x24 e símbolo "E"/escudo).
- Uma pasta por tela em `<grupo>/<tela>/` com `SPEC.md` (intenção), `screens/*.png` (referência 393x851 dp), `spec/*.md` (medidas em dp, fontes, animações), `proto.html` e `lint.txt`.
  Telas que rolam têm também `screens/*--full-scroll.png`.

Regras
1. Mantenha a lógica, os dados e a navegação existentes do app. Mude só layout e visual.
2. Ordem de trabalho, uma etapa por vez, validando cada uma:
   a. Tema + fontes: baixe e coloque em res/font Barlow Condensed (500/600/700), Inter (400/500/600/700) e Saira Stencil One (400). Substitua os tokens antigos (Runna*/Aisle*) pelo mapa do THEME.md; remova AislePurple e BrandGradient.
   b. Componentes compartilhados: barra inferior com botão central "E" animado, botões (primário vermelho, Pausar oliva, fantasma), chips/tags, cards, switch, bottom sheet com scrim, diálogo, campo de texto, ícones.
   c. Telas, nesta ordem: primeiros-passos (7) -> hoje (6) -> plano (4) -> corrida (6) -> progresso-e-perfil (6).
   d. Navegação entre elas, como já existe no app.
3. Portar tamanhos fixos/flexíveis exatamente como em `spec/*.md`; condicionais de altura para media queries (`max-width: 430px`).
4. Animações exatamente como no THEME.md; em repouso só o botão central E (e o ponto pulsante do mapa na Corrida); respeitar reduzir movimento.
5. Verificação em cada tela: rodar no emulador 393x851 dp, tirar screenshot com `android_shot.sh`, comparar com `screens/<tela>.png` usando `compare.py` e corrigir a maior região diferente. Repetir até ~1% (ignorando mapa, hora e dados vivos) ou 5 rodadas.
6. Não invente elementos. Se faltar ícone, fonte ou medida, pergunte.
7. Relatório final: % de diferença por tela, diferenças restantes e o que não foi verificado.

Aproximações aceitas: gradiente cônico girando do botão central (sweepGradient rotacionado) e brilho (sombra blur 16 dp, alpha até .35); listras diagonais do fundo podem ser omitidas.
