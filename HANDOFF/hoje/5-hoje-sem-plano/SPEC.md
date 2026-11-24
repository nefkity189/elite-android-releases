# SPEC — 5-hoje-sem-plano (hoje)   (dp = CSS px; números exatos em `spec/5-hoje-sem-plano.md`)

## 1. Intenção
Estado vazio: nenhum plano ativo. Visual tático escuro (preto, oliva, osso, vermelho de sinalização); números e títulos em Barlow Condensed; o vermelho é só a ação principal.

## 2. Frame
393x851 dp. Tem barra inferior com botão central E. Sem rolagem. 

## 3. Tokens
Ver `../../THEME.md` (cores, fontes, mapa de tokens do app atual). Valores por elemento em `spec/5-hoje-sem-plano.md`.

## 4. Camadas e componentes
Árvore completa com posição, tamanho, padding, gap, raio, cores e texto em `spec/5-hoje-sem-plano.md` ("Element tree"). Nomes de classe do protótipo = nomes sugeridos de componente. Ícones em `../../marca-e-tema/assets/icons/`.

## 5. Animações
Em repouso anima apenas o botão central E (borda cônica spin 5 s linear + glow 3,2 s). Detalhes de tempo e curva em `../../THEME.md`. Reduzir movimento: sem animação (ver `screens/5-hoje-sem-plano--reduced-motion.png`).

## 6. Estado desta captura
CTA Criar meu plano.

## 7. Aproximações conhecidas
- Gradiente cônico girando do botão central: em Compose usar `Brush.sweepGradient` rotacionado (`rotate`) em drawBehind; brilho = sombra com blur 16 dp, alpha até .35.
- Fundo com listras diagonais sutis (repeating-linear-gradient 135°, alpha .016): pode ser omitido ou desenhado em Canvas; diferença imperceptível.
- Render de referência feito sem as fontes web (offline): larguras de texto podem diferir até instalar Barlow Condensed e Saira Stencil One.
- Mapa e dados (distância, ritmo, datas) são ilustrativos; usar os dados reais do app.

## 8. Lint (revisado)
`lint.txt` lista avisos que são falsos positivos por desenho: textos "cobertos" por scrim/sheet/diálogo (overlays), diálogos ocultos (opacity 0) presentes no DOM, barra inferior e CTA fixo sobre conteúdo rolável. Itens reais já corrigidos neste protótipo: aria-label dos botões só com ícone.

## 9. NÃO fazer
Sem gradiente teal-roxo, sem emojis, sem cores fora do THEME, sem animar nada além do listado, sem mudar lógica, dados ou rotas do app.
