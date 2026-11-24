# Elite: aplicar o novo tema no app (Android, Jetpack Compose)

Leia `SPEC.md` primeiro. Este pacote já está na raiz do projeto (ou em `HANDOFF/elite/`). Stack: Kotlin + Jetpack Compose (premissa: confirme no `build.gradle`; se não for Compose, pare e avise).

## O que fazer, nesta ordem
1. **Fontes**: baixe Barlow Condensed (500/600/700), Inter (400/500/600/700) e Saira Stencil One e coloque em `app/src/main/res/font/` (nomes em minúsculas com underscore). Atualize `Type.kt` com a tabela da seção 4 do SPEC.
2. **Cores**: copie `theme/EliteColors.kt` para `ui/theme/` (ajuste o pacote) e troque os valores em `Color.kt` conforme o mapa de tokens da seção 3 (mantenha os NOMES atuais `Runna*`/`Aisle*`; só mude valores, para não quebrar o resto). Remova o gradiente de marca teal→roxo (use Olive sólido).
3. **Marca**: copie `brand/android/res/` para `app/src/main/res/` (vetores, ícone adaptativo, mipmaps, `app_name`) e `brand/compose/EliteLogo.kt` para `ui/components/`; leia `brand/BRAND_SPEC.md`. Troque o "A" do botão central (`RunnaBottomNav.kt`, ~linha 266) pelo drawable do "E" inclinado (`ic_elite_blade.xml`), sem tint, mantendo a borda cônica (agora Olive→Red→Brass), o brilho e as animações de toque. Troque `AisleSymbol` e o texto "Aisle" (`AisleComponents.kt`), o título da notificação (`WorkoutReminderWorker.kt`), o slogan do `ShareCardGenerator` e o `app_name`/ícone do app.
4. **Scrim da corrida**: aplique `EliteTopScrimStops` (SPEC seção 7) em `ActiveRunScreen.kt` e `FreeRunLayout.kt`. Garanta o texto do cronômetro acima do anel.
5. **Telas**: use a tabela da seção 8 do SPEC. Para cada tela: abra o PNG em `screens/` e as medidas em `spec/`, ajuste só cores, fontes, raios e textos de marca. A estrutura e os componentes já existem.

## Loop de verificação (por tela, não pule)
Compilar → rodar no emulador 393x851 dp (ou `assets` do skill, `android_shot.sh`) → capturar → comparar com o PNG de referência → corrigir. Não declare pronto sem capturar. Telas com mapa: testar também com mapa branco e conferir que Distância e Ritmo seguem legíveis e que o ponto de posição não escurece.

## Regras
- Não mexer em lógica, dados, rotas nem navegação. Não renomear classes.
- Respeitar "Aproximações conhecidas" e "NÃO fazer" do SPEC.
- Ao final, liste o que foi feito, o que NÃO foi capturado/comparado e qualquer divergência.

## Referências extras
**Tela de abertura (splash): ver `splash/SPLASH_SPEC.md`.**
`html/` tem os protótipos animados (abra no navegador). `html/elite.css.fonte.css` é o CSS de origem dos tokens e animações (dp = px).
