# SPLASH_SPEC — Tela de abertura animada do Elite

Protótipo: `html/elite-splash.html` (abra no navegador). Quadros: `screens/splash-quadros-animacao.png` (9 quadros a cada 0,38 s) e `screens/splash-final.png` (estado final, 2x). Medidas: `splash-medidas.md`. Tela de 393x851 dp. Código: `compose/EliteSplash.kt`.

## 1. Comportamento
1. Partida a frio: o sistema mostra só o fundo #0A0B09 (sem ícone), pelo tema `Theme.Elite.Starting`. Isso evita um pulo visual, porque a animação do Compose começa do mesmo fundo.
2. A splash do Compose roda por ~2,8 s e então espera o app ficar pronto (`isReady`). Quando pronto: saída de 0,3 s (some e cresce 4%), e o app aparece por baixo.
3. Só aparece em partida a frio (uma vez por abertura do processo). Reabrir pelo recentes não repete.
4. Reduzir movimento (animações do sistema desligadas): mostra o estado final parado por 0,6 s e sai sem animar.

## 2. Composição (dp, fundo #0A0B09)
| Elemento | Medida |
|---|---|
| Logo (símbolo + "ELITE" + slogan) | bloco **centralizado** na tela (horizontal e vertical), de cima para baixo: símbolo 104x104; 22 dp; "ELITE" 40 sp Saira Stencil One, osso, letter-spacing final 0,24 em; 14 dp; slogan 9 sp Inter 600, letter-spacing 0,2 em, #8F9384, sem quebra |
| Brilho | círculo radial oliva 18% → transparente, centro no centro da tela, raio 240 dp; opacidade 0,55, "respira" até 0,85 |
| Barra de carregamento | 260 dp de largura (centrada), trilha 2 dp #1D2116, preenchimento oliva #A3B565, a 84 dp da base (barra) |
| Corredor | 30x40 dp, sobre a barra (os pés tocam a barra), centro do corpo no fim do preenchimento (offset −16 dp) |
| Porcentagem | abaixo da barra, 14 dp de distância; Barlow Condensed 13 sp SemiBold, letter-spacing 0,1 em, #8F9384, números tabulares; "0%" a "100%" |
Peças do símbolo (grade 64, antes da inclinação de −12°, ver `../brand/BRAND_SPEC.md`): coluna `14–24,2 x 8–56`; barra de cima `23,6–50 x 8–17`; barra de baixo `23,6–50 x 47–56`; barra vermelha `23,6–44 x 27–36` (#D12B2B). Sobreposição de 0,4 un. para não aparecer emenda.

## 3. O corredor pequeno (figura articulada, grade 200x150, recorte 40,10 a 140,145)
Cor: osso #ECE8DA para o tronco e os membros do lado perto; #6F735E para os do lado longe (profundidade); tênis: perto #D12B2B, longe #8E1F1F. Linhas com ponta redonda. Largura em dp = valor da grade x 0,296.
| Parte | Origem (pivô) | Comprimento | Espessura (grade) |
|---|---|---|---|
| Tronco (inclinado ~28°) | quadril (94,72) → meio (103,55) → ombro (112,38) | 2 trechos | 14 e 18 |
| Cabeça | círculo em (121,23) | raio 10 | — |
| Coxa | quadril (94,72) | 32 | perto 14,5 / longe 13 |
| Canela | joelho (fim da coxa) | 30 | perto 10,5 / longe 9,5 |
| Pé (tênis) | tornozelo, 11 à frente e 2 abaixo | 11 | 8 / 7 |
| Braço | ombro (112,38) | 22 | 10 / 9 |
| Antebraço | cotovelo (fim do braço) | 20 | 8,5 / 7,5 |
Ângulos em graus, positivos no sentido horário, relativos ao segmento anterior; repouso = segmento apontando para baixo.
Ciclo de corrida: **0,6 s**, ease-in-out entre as poses. Perna do lado longe = mesma pose atrasada em meio ciclo (0,3 s); cada braço balança oposto à perna do mesmo lado.
| Fase do ciclo | Coxa (perto) | Canela (perto) | Braço (perto) | Antebraço (perto) |
|---|---|---|---|---|
| 0% | −50 | 20 | 40 | −60 |
| 25% | −10 | 15 | (meio) | (meio) |
| 50% | 35 | 40 | −45 | −90 |
| 75% | −5 | 110 | (meio) | (meio) |
Balanço vertical do corpo: sobe e desce 3 un. da grade a cada 0,3 s. Pose estática (reduzir movimento): coxa/canela perto −48/22, longe 32/55; braço/antebraço perto 40/−62, longe −45/−88.

## 3b. Linha do tempo (ms)
| Início–fim | Elemento | Animação | Curva |
|---|---|---|---|
| 0–900 | Brilho | opacidade 0→0,55 e escala 0,6→1; depois respira 0,55↔0,85 a cada 3,2 s | ease-out |
| 200–850 | Coluna, barras de cima e de baixo do "E" | coluna desce (200–650); barras entram da esquerda (350–750 e 450–850) | (0,2; 0,8; 0,2; 1) |
| 750–1200 | Barra vermelha | escala X 0→1,12→1 (pico em 70%); cor #FF7A6E → #D12B2B em 600 ms | ease-out |
| 900–1600 | "ELITE" | fade in; letter-spacing 0,6 em → 0,24 em; (na web também desfoque 6 px→0; no Compose omitido) | (0,2; 0,8; 0,2; 1) |
| 1350–1850 | Slogan | fade in e sobe 6 dp | ease-out |
| 100–500 | Barra, corredor e porcentagem | aparecem em fade | linear |
| 200–2500 | **Progresso 0 → 100%** | o preenchimento, o corredor e o número andam juntos, com ritmo irregular: 28% em 0,5 s, 58% em 1,15 s, 86% em 1,8 s, 100% em 2,5 s (interpolação linear entre esses pontos) | linear por trecho |
| 200–2800 | Corredor | ciclo de corrida em loop até a saída | — |
| 2800–3100 | Tudo | opacidade 1→0 e escala 1→1,04 | ease-in |
Total 3,1 s (2,8 s de entrada + 0,3 s de saída). Se o app demorar para ficar pronto, a barra fica em 100% (o corredor no fim) até `isReady()` virar true; para um progresso real, trocar `loadPct(tv)` por um valor vindo do carregamento.

## 4. Integração Android
1. `build.gradle`: `implementation("androidx.core:core-splashscreen:1.0.1")`.
2. Copiar `android/res/` para `app/src/main/res/` (tema `Theme.Elite.Starting` e o ícone vazio `ic_splash_empty`). Ajustar `postSplashScreenTheme` para o tema atual do app.
3. Manifest: `android:theme="@style/Theme.Elite.Starting"` no application/activity.
4. Copiar `compose/EliteSplash.kt` (mesmo pacote de `brand/compose/EliteLogo.kt`, que fornece `EliteBrand` e `EliteStencilFont`) e seguir `compose/MainActivity_integracao.kt.txt`.
5. Fonte: colocar Saira Stencil One em `res/font/` e trocar `EliteStencilFont` em `EliteLogo.kt`.

## 5. Aproximações e limites (honestos)
- Não compilei nem rodei o Kotlin; é uma tradução do protótipo web. Testar no emulador e comparar com `screens/`.
- Desfoque do wordmark (6 px → 0) não existe no Compose antes do Android 12 sem custo; usei só fade e letter-spacing.
- O letter-spacing animado pode fazer o texto "andar" 1–2 dp; se incomodar, fixar a largura do bloco.
- Na web a fonte Saira Stencil depende do Google Fonts; no sandbox ela não carregou, então os quadros mostram a fonte reserva (sem estêncil).
- O corredor é uma figura estilizada desenhada por mim (linhas arredondadas, sem rosto, sem marca de terceiros), com só 40 dp de altura; os detalhes são pequenos. Se quiser mais refinado, substituir por uma animação Lottie/AVD profissional no mesmo tamanho.
- O progresso é simulado (0 a 100% em tempo fixo). Não reflete o carregamento real do app.
- A porcentagem animada na web usa propriedades CSS registradas (Chrome, Safari 16.4+, Firefox 128+); em navegador antigo ela fica em 100%.
- O tempo total (3,1 s) é escolha de design: uma splash longa atrasa o uso; reduza se o app abrir rápido.
- No Compose o ciclo roda num `rememberInfiniteTransition` (0,6 s); em aparelhos fracos pode perder quadros, então vale medir.

## 6. NÃO fazer
- Não usar ícone no tema de partida do sistema (causaria dois símbolos seguidos).
- Não prender o usuário além do necessário: se o app estiver pronto, sair em 2,8 s; nunca esperar mais que o app precisa.
- Não adicionar som, vibração, tremor ou brilho extra no símbolo.
