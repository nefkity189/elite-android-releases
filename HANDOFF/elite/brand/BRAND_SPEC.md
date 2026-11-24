# BRAND_SPEC — Marca Elite

Fonte visual: `../screens/marca-e-tema.png` e `../html/elite-marca.html`. Medidas da página: `../spec/elite-marca.md`. Tudo em dp = px do CSS.

## 1. Conceito
Um "E" inclinado para frente, feito de duas barras grossas e uma barra do meio vermelha (as "barras de velocidade"). Sem escudo, faca ou caveira, para não parecer empresa de segurança. Cores: osso, oliva e vermelho sobre preto tático. Nome do app: **Elite**. Slogan: "Missão dada é missão cumprida".

## 2. Construção do símbolo (grade 64x64)
Desenho base (sem inclinação), unidades da grade:
- Corpo (osso): polígono `(14,8) (50,8) (50,17) (24,17) (24,47) (50,47) (50,56) (14,56)`. Coluna de 10 de largura; barras de 9 de espessura (topo 8–17, base 47–56).
- Barra do meio (vermelha #D12B2B): retângulo `(24,27) (44,27) (44,36) (24,36)`, 20x9, encostada na coluna e deslocada do corpo pelo recorte entre as barras (10 de vão acima e abaixo).
- Inclinação: `x' = x + 6.8 − 0.21256·y` (skew de −12°), `y' = y`. Os pontos já inclinados estão em `compose/EliteLogo.kt` e nos vetores.
- Caixa do símbolo inclinado: x 8,9 a 55,1 e y 8 a 56 (46,2 x 48). Para exportar com folga uso a caixa 7,7 a 57,57 (50 x 50, quadrada).
Proporções úteis (H = altura do símbolo): coluna 0,21 H; barra 0,19 H; barra vermelha 0,42 H de largura.

## 3. Cores e variações
| Fundo | Versão | Barras | Barra do meio | Arquivo |
|---|---|---|---|---|
| Preto / escuro (padrão) | osso | #ECE8DA | #D12B2B | `svg/elite-mark-bone.svg`, `ic_elite_mark.xml` |
| Claro (osso, branco) ou oliva | escura | #0A0B09 | #D12B2B | `svg/elite-mark-dark.svg`, `ic_elite_mark_dark.xml` |
| Botão central e destaques | oliva | #A3B565 | #D12B2B | `svg/elite-mark-olive.svg`, `ic_elite_blade.xml` |
| Notificação, ícone temático, impressão 1 cor | monocromática | branco ou preto | mesma cor | `svg/elite-mark-mono-*.svg`, `ic_notification_elite.xml` |
Regra: a barra do meio é SEMPRE vermelha, exceto na versão monocromática. Nunca usar tint em Compose (usar `EliteMark` com `barColor`).

## 4. Área de proteção e tamanho mínimo
- Área de proteção: 0,25 H em todos os lados (a altura de uma barra mais a metade).
- Tamanho mínimo do símbolo: 16 dp (UI) e 24 dp na barra de status/notificação (monocromático).
- Lockup horizontal: altura mínima 32 dp; o slogan só aparece com o lockup de 160 dp de largura ou mais.

## 5. Logotipo (wordmark) e lockups
- Wordmark: "ELITE" em **Saira Stencil One**, caixa alta, letter-spacing 0,24 em, cor osso (#ECE8DA; #0A0B09 em fundo claro). Compensar a margem direita do último "E" com −0,24 em.
- Lockup horizontal: símbolo 46x46, gap 12, wordmark 26 sp (linha 26). Slogan abaixo: 8 sp Inter SemiBold, letter-spacing 0,13 em, cor TextMuted (#8F9384), sem quebra de linha, margem superior 6.
- Lockup vertical: símbolo 78x78, gap 10, wordmark 38 sp, slogan abaixo, tudo centralizado.
- Fonte NÃO carregou no sandbox: nos PNGs/SVGs de lockup o texto não foi exportado. Colocar `saira_stencil_one.ttf` em `res/font/` e trocar `EliteStencilFont` em `compose/EliteLogo.kt`. Sem a fonte o texto cai em SansSerif Bold (aceitável, mas sem o estêncil).

## 6. Ícone do app
- Ícone adaptativo (Android 8+): fundo `ic_launcher_background.xml` (radial #262B1A→#0A0B09, centro em 32,22; raio 110) e frente `ic_launcher_foreground.xml` (E osso com barra vermelha, 45,6 dp de largura, centrado na tela de 108 dp, dentro da zona segura de 66 dp). Ícone monocromático (Android 13+) reaproveita a frente.
- Ícone legado: `mipmap-*/ic_launcher.png` (48, 72, 96, 144, 192 px, quadrado cheio; o "round" é a mesma imagem, o sistema recorta).
- Play Store: `png/play-store-512.png`. Ícone de 1024: `png/elite-app-icon-1024.png`.
- Notificação: `ic_notification_elite.xml` (24 dp, branco, sem cor).
- Nome do app: `values/strings.xml` com `app_name = Elite`.

## 7. Botão central da barra (substitui o "A")
48x48, raio 16, borda de 2 dp em gradiente cônico girando (Olive → Red → Brass → Olive, 5 s por volta, linear), miolo raio 14 com radial #262B1A→#0C0E08 (centro 30% / 20%), símbolo oliva 26 dp centrado. Brilho respirando (0–35% de oliva, 3,2 s), toque: pulo 0,88→1,12→1 (0,5 s), giro Y 360° (0,6 s) e onda (0,6 s). Reduzir movimento: tudo desligado.

## 8. Onde a marca aparece nas telas (dp)
| Tela | Elemento | Medida |
|---|---|---|
| Boas-vindas | lockup vertical com slogan, centralizado no topo (y ≈ 56) | símbolo 78, wordmark 38 sp |
| Perfil | avatar: quadrado 64, raio 20, radial #262B1A→#0A0B09, borda 2 #5D6B34, símbolo 38 | |
| Perfil · dados e sobre | lockup horizontal sem slogan dentro de card, texto "Versão 1.0.0" abaixo | símbolo 46, wordmark 26 sp |
| Cartão de compartilhar | cabeçalho: símbolo 28 + wordmark 18 sp; rodapé com slogan | |
| Barra inferior | botão central (seção 7) | 48 |
| Splash (a criar) | símbolo osso 96 centrado sobre #0A0B09 | |

## 9. Arquivos
`svg/` fontes vetoriais · `png/` exports 1024 (+ play-store-512) · `android/res/` pronto para copiar para `app/src/main/res/` · `compose/EliteLogo.kt` (`EliteMark`, `EliteWordmark`, `EliteBrand`) para usar sem recursos.

## 10. Aproximações e limites (honestos)
- Wordmark e slogan dependem da fonte (ver seção 5); lockups completos não foram exportados em PNG por isso.
- Ícone adaptativo e vetores não foram abertos num launcher/Android Studio; revisar a zona segura num aparelho.
- Gradiente radial do fundo do ícone em `<aapt:attr>` precisa de minSdk 24; abaixo disso usar só `#0A0B09` sólido.
- Os PNGs legados `ic_launcher_round.png` são cópias do quadrado.

## 11. NÃO fazer
- Não usar escudo, caveira, faca, estrela ou qualquer elemento militar/segurança no símbolo.
- Não esticar, girar, mudar a inclinação ou trocar a cor da barra do meio.
- Não aplicar sombra, brilho ou contorno ao símbolo (o brilho existe só no botão central).
- Não usar o símbolo abaixo de 16 dp nem o slogan abaixo do limite da seção 4.
