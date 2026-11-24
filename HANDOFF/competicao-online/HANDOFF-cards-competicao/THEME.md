# THEME.md - identidade dos 6 cards de compartilhamento (1080x1350 px, 4:5 Instagram)

1 dp do protótipo = 1 px do Bitmap (CompetitionShareCardGenerator.WIDTH/HEIGHT). Posições e tamanhos exatos: spec/card-*.md. Referência visual: screens/card-*.png.

## Marca (tirada do próprio app)
- Símbolo: `res/drawable/ic_elite_mark.xml` (E inclinado bone #ECE8DA + barra do meio vermelha #D12B2B, viewport 64). Desenhar com os mesmos paths no Canvas; nunca tingir a barra vermelha. Aparece no cabeçalho (64x64 em x=72,y=72) e como marca d'água recortada pela borda (mesmo path, uma cor só, alfa 4% a 7%).
- Palavra: "ELITE" em Saira Stencil One (EliteStencilFont), tamanho 44, espaçamento 0,24 em, bone, x=160, y=76. No protótipo foi usado Barlow Condensed espaçado como substituto, porque a Saira não está instalada aqui. No app usar a Saira real.
- Corredor: a figura da splash (EliteSplash.kt, SmallRunner, pose parada: cycle < 0). Reusar a mesma função com escala maior (Participação: 280 px de altura; Sem app: 400 px). Corpo bone, perna/braço de trás #6F735E, tênis #D12B2B e #8E1F1F.
- Assinatura gráfica: inclinação de -12 graus (SKEW = -0,21256 da splash) em números, títulos, blocos e botões. No Canvas: canvas.save(); canvas.skew(-0.21256f, 0f) com o pivô na base-esquerda do bloco; restaurar depois. A barra vermelha curta (12 px de altura, também inclinada) é o único acento vermelho de cada card.
- Tagline do rodapé: "Missão dada é missão cumprida" (mesma da splash, sem caixa-alta).

## Cores (ShareCardColors; prata e bronze são as das ligas em CompetitionComponents)
ink #0A0B09 | card #161911 | elev #1D2116 | bone #ECE8DA | muted #8F9384 | dim #5E6255 | olive #A3B565 | on-olive #0E1306 | red #D12B2B | brass #C9A24B | silver #B8BCB0 | bronze #A8703E

## Tipografia
Barlow Condensed Bold (números e títulos, sempre inclinados), Barlow Condensed Medium (nome), Inter SemiBold/Regular (rótulos). Fundo liso ink, sem painel nem contorno, sem sombra, sem blur, sem gradiente.

## Cabeçalho e rodapé comuns
Cabeçalho: símbolo, palavra, e à direita o contexto (Inter SemiBold 26 muted, alinhado à direita em x=1008, y=86). Rodapé: barra vermelha 64x10 em (72,1232), tagline em y=1262 (Inter SemiBold 24 muted), "Competição online" à direita em bone.

## ATENÇÃO: fontes quebradas no projeto
`res/font/inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf` e `saira_stencil_one_regular.ttf` são páginas HTML de um download que falhou. Esta pasta traz `fonts/Inter-Regular.otf` e `Inter-SemiBold.otf` (válidos): apague os .ttf do Inter e copie estes para res/font com os nomes `inter_regular.otf` e `inter_semibold.otf` (mesmos nomes de recurso, extensão .otf; remova os .ttf para não duplicar). A Saira Stencil One precisa ser baixada do Google Fonts (ofl, "Saira Stencil One", Regular).

## Conversão caixa -> baseline (Canvas)
Use Paint.fontMetrics: baseline = topo da caixa + (altura da linha - (descent - ascent)) / 2 - ascent. O `line()` atual diminui o texto até caber em 920 px; manter para nomes.
