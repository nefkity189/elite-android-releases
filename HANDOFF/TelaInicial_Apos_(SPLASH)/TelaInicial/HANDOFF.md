# HANDOFF - Login após o splash (v1)

## 1. Objetivo
Tela de entrada que aparece **logo depois do splash** e antes do app, para quem ainda não entrou na conta Elite. Segue o padrão dos prints de referência (Strava): carrossel de 4 slides com um telefone ilustrado no alto, título em duas linhas, pontos de página e botões grandes embaixo. No Elite as fotos viram uma arte topográfica em vetor (sem foto, sem logotipo de terceiros) e o telefone mostra telas reais do app.

Slides: 1) Competição online (sala e contagem), 2) Ranking e ligas, 3) Plano de treino, 4) Conquistas. Textos e nomes vêm do site elitesch.web.app (nomes e pontos são exemplo ilustrativo).

Ações, ligadas ao que **já existe** no código (`AccountRepository.signIn` e `signInWithGoogle`):
- **Entrar com Google** (principal) chama `launchGoogleSignIn` da MainActivity e depois `MainViewModel.onGoogleSignIn`.
- **Entrar com e-mail** abre uma folha inferior com e-mail e senha e chama `MainViewModel.onSignInClick(login, password)`.
- **Continuar sem conta** grava que o login foi dispensado e entra no app (o Perfil continua com o `AccountCard` para entrar depois).

Mudou em relação à versão atual: hoje não existe tela de login; o app abre direto em `AppNavigation` e o login só existe no Perfil.

## 2. Dispositivo
Poco X7, 393 x 851 dp, tema escuro, Jetpack Compose, edge-to-edge (a MainActivity já chama `enableEdgeToEdge()`). Fontes do app (nomes reais do projeto, diferentes do padrão da skill): Barlow Condensed (títulos e números), Inter (texto e botões), Saira Stencil One (só o logotipo ELITE).

## 3. Tokens
Todos os tokens existentes já estão em `ui/theme/Color.kt`. Os marcados "novo" precisam ser criados.

| Variável CSS | Valor | Nome Kotlin |
|---|---|---|
| --bg | #0A0B09 | Background |
| --bg-0 | rgba(10,11,9,0) | novo: `Background.copy(alpha = 0f)` (nunca `Color.Transparent`) |
| --surface | #10120D | Surface (folha inferior) |
| --card | #161911 | Card |
| --card2 | #1D2116 | CardElevated |
| --field | #0D0F0A | Field |
| --line | rgba(236,232,218,.10) | Line |
| --line2 | rgba(236,232,218,.18) | LineStrong |
| --text | #ECE8DA | Text |
| --muted | #8F9384 | TextMuted |
| --dim | #5E6255 | TextDim |
| --olive | #A3B565 | Olive |
| --olive-dark | #5D6B34 | OliveDark |
| --olive-soft | rgba(163,181,101,.14) | OliveSoft |
| --on-olive | #0E1306 | OnOlive |
| --olive-glow | rgba(163,181,101,.20) | novo: `Olive.copy(alpha = .20f)` |
| --olive-glow0 | rgba(163,181,101,0) | novo: `Olive.copy(alpha = 0f)` |
| --red | #D12B2B | Red |
| --on-red | #FFF4F0 | OnRed |
| --brass | #C9A24B | Brass |
| --brass-soft | rgba(201,162,75,.16) | novo: `Brass.copy(alpha = .16f)` |
| --brass-50 | rgba(201,162,75,.5) | StepColors.Cooldown |
| --amber | #E0A93A | Amber |
| --amber-soft | rgba(224,169,58,.13) | novo: `Amber.copy(alpha = .13f)` (igual ao `AccountCard`) |
| --amber-line | rgba(224,169,58,.35) | novo: `Amber.copy(alpha = .35f)` (igual ao `AccountCard`) |
| --ok | #7FB069 | Ok |
| --step-rest | #4A4E40 | StepRest |
| --scrim | rgba(0,0,0,.60) | novo: `Color.Black.copy(alpha = .60f)` |
| --shadow | rgba(0,0,0,.45) | novo: `Color.Black.copy(alpha = .45f)` |
| --disp / --ui / --logo | Barlow Condensed / Inter / Saira Stencil One | EliteDisplayFont / EliteBodyFont / EliteLogoFont |

## 4. Mapa de componentes
| data-id | Composable | Arquivo | Novo? |
|---|---|---|---|
| `screen` | LoginScreen | ui/screens/LoginScreen.kt | sim |
| `login.hero` | LoginHeroBackground | ui/components/login/LoginComponents.kt | sim |
| `login.hero.art` | Image(ic_login_topo) | res/drawable/ic_login_topo.xml | sim |
| `login.hero.glow` | Box + `Brush.radialGradient` | LoginComponents.kt | sim |
| `login.hero.fade` | Box + `Brush.verticalGradient(colorStops)` | LoginComponents.kt | sim |
| `login.logo` | EliteLogoLockup | LoginComponents.kt | sim |
| `login.logo.mark` | Icon(ic_elite_mark) | res/drawable/ic_elite_mark.xml | não |
| `login.carousel` | HorizontalPager (4 páginas) | LoginScreen.kt | sim |
| `login.slide.1` a `login.slide.4` | LoginPage | LoginScreen.kt | sim |
| `login.slide.1.phone` a `login.slide.4.phone` | PhoneMockup | ui/components/login/PhoneMockups.kt | sim |
| `login.slide.1.screen` | MissionMock | PhoneMockups.kt | sim |
| `login.slide.2.screen` | RankingMock | PhoneMockups.kt | sim |
| `login.slide.3.screen` | PlanMock | PhoneMockups.kt | sim |
| `login.slide.4.screen` | AchievementsMock | PhoneMockups.kt | sim |
| `login.slide.1.title` a `login.slide.4.title` | SlideTitle | LoginScreen.kt | sim |
| `login.dots` | PagerDots | LoginComponents.kt | sim |
| `login.actions` | LoginActions | LoginComponents.kt | sim |
| `login.actions.google` | EliteButton(Primary) com `leadingIcon`, `loading` | ui/components/EliteControls.kt | existe (ganha parâmetro `contentColor`) |
| `login.actions.email` | LoginGhostButton | LoginComponents.kt | sim |
| `login.actions.skip` | TextButton (Inter 15/20, 600) | LoginComponents.kt | sim |
| `login.legal` | LegalText (2 links) | LoginComponents.kt | sim |
| `login.scrim` | scrimColor do ModalBottomSheet | LoginComponents.kt | sim |
| `login.sheet` | EmailSignInSheet (ModalBottomSheet) | LoginComponents.kt | sim |
| `login.sheet.email`, `login.sheet.password` | OutlinedTextField (sem label interno) | LoginComponents.kt | sim |
| `login.sheet.error` | banner âmbar | LoginComponents.kt (mesmo visual do `AccountCard`) | sim |
| `login.sheet.submit` | EliteButton(Primary, loading) | EliteControls.kt | existe |

Os ids internos dos telefones (`login.slide.N.mission`, `login.slide.2.rank.r1.pts`, `login.slide.4.list.a1` etc.) viram `Modifier.testTag` com o mesmo texto e servem para medir. Tudo dentro de `*.phone` é **ilustração**: não clicável.

## 5. Regras de layout
Medidas de referência (dp = px do HTML): logo y 44, h 34; telefone x 93, y 108, 207 x 404 (tela interna 195 x 392, padding 24/14/14); título y 528, h 64; pontos y 612, h 8; Google y 634 h 52; e-mail y 696 h 48; "Continuar sem conta" y 750 h 44; legal y 798 h 28; margem lateral dos botões 24 (largura 345); hero 0 a 468 com degradê de 140 no fim (y 328 a 468).

Como isso vira Compose (a soma fecha em 851):
- `LoginScreen` = `Box(fillMaxSize, background Background)`: hero atrás, depois `Column(fillMaxSize)`.
- `Column`: **pager** com `weight(1f)` (referência 600 dp) + **bloco inferior fixo**: Spacer 12, pontos 8, Spacer 14, Google 52, Spacer 10, e-mail 48, Spacer 6, skip 44, Spacer 4, legal 28, e `navigationBarsPadding()` com mínimo de 25 dp. Soma do bloco: 226 + 25 = 251 (851 - 251 = 600).
- Cada página: `Column`: Spacer 108, **telefone com `weight(1f)`** (referência 404; `heightIn(min = 300, max = 404)`, largura fixa 207), Spacer 16, título 64, Spacer 8. Soma 600.
- O logo fica fora do pager (não desliza), `Modifier.padding(top = 44.dp)`, centralizado. O hero tem altura fixa 468 e passa por trás da barra de status.
- Telas com altura < 760 dp: Spacer superior 108 passa a 88 e o logo a y 32; abaixo de 700 dp, esconder `login.legal`. Nunca esconder os três botões.
- Escala de fonte do sistema: títulos e botões respeitam até 1.15 (limitar com `Density(fontScale = min(atual, 1.15f))` só nesta tela); o conteúdo de `*.phone` ignora a escala (`fontScale = 1f`).
- Rolagem: não na tela. A folha de e-mail pode rolar se o teclado deixar pouco espaço.
- Folha de e-mail: `ModalBottomSheet`, altura pelo conteúdo (369 dp sem erro; 445 dp com o banner de erro de 2 linhas, que tem 62 dp mais 14 de margem), cantos superiores 26, `containerColor = Surface`, `windowInsets = WindowInsets(0)`, padding horizontal 24, inferior 28, mais `imePadding()`. Campos 56 dp de altura, raio 14, rótulo (12/14, TextMuted) como `Text` acima do campo, nunca o label interno do Material.
- Comportamentos sem protótipo (descritos aqui, não em states.json): botão Entrar da folha em `loading` mostra "Entrando…" com spinner; erro do Google (Toast que já existe na MainActivity) não muda o layout; toque em Entrar com campos vazios mostra "Preencha e-mail e senha." no mesmo banner âmbar.

## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| slide_1 | carga da página | Slide 1, ponto 1 alongado |
| slide_2 | eval `__go(2)` | Slide 2 (ranking), arte deslocada 28 dp |
| slide_3 | eval `__go(3)` | Slide 3 (plano), arte deslocada 56 dp |
| slide_4 | eval `__go(4)` | Slide 4 (conquistas), arte deslocada 84 dp |
| slide_3_transicao | eval `__go(3)` com frames 0, 100, 200, 350 | troca de página em andamento (ver Motion) |
| email_sheet | click `login.actions.email` | folha aberta, fundo escurecido |
| email_erro | abre a folha e clica `login.sheet.submit` | banner âmbar de erro dentro da folha |
| entrando | click `login.actions.google` (frames [0]) | botão com spinner e "Entrando…"; e-mail e skip a 40% |
| google_pressionado | press `login.actions.google` | escala .97 |
| email_pressionado | press `login.actions.email` | escala .97 |

Estados que existem só no app: restaurando sessão (login ainda não aparece, ver Armadilhas), erro de rede/Firebase (`AuthState.Error` vira o banner da folha).

## 7. Motion
| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `login.carousel.track` | trocar de página (arrastar ou programático) | translateX | -393 x (n-1) dp -> -393 x n dp | 350ms | 0 | cubic-bezier(.2,.8,.2,1) | não | instantâneo |
| `login.hero.art` | trocar de página | translateX | -28 x (n-1) dp -> -28 x n dp | 350ms | 0 | cubic-bezier(.2,.8,.2,1) | não | instantâneo |
| `login.dots.d1` a `login.dots.d4` | página ativa muda | largura e cor | 8dp TextDim -> 24dp Olive | 200ms | 0 | ease-out | não | instantâneo |
| `login.actions.google` | pressionar | escala | 1 -> .97 | 100ms | 0 | ease-out | não | instantâneo |
| `login.actions.email` | pressionar | escala | 1 -> .97 | 100ms | 0 | ease-out | não | instantâneo |
| `login.actions.skip` | pressionar | opacidade | 1 -> .6 | 100ms | 0 | ease-out | não | instantâneo |
| `login.scrim` | abrir a folha | opacidade | 0 -> 1 (preto 60%) | 300ms | 0 | cubic-bezier(.4,0,.2,1) | não | instantâneo |
| `login.sheet` | abrir a folha | translateY | 520dp -> 0 | 300ms | 0 | cubic-bezier(.4,0,.2,1) | não (fechar: 220ms) | instantâneo |
| `login.actions.spinner` (spin) | `AuthState.SigningIn` | rotação | 0 -> 360deg | 800ms | 0 | linear | infinito | arco parado |
| `login.sheet.spinner` (spin) | `AuthState.SigningIn` com a folha aberta | rotação | 0 -> 360deg | 800ms | 0 | linear | infinito | arco parado |
| `screen` (só no app) | login concluído ou "Continuar sem conta" | alpha | 1 -> 0 | 220ms | 0 | FastOutSlowIn | não | instantâneo |

Nada se mexe em repouso (sem autoplay no carrossel). O login já está composto por baixo do splash: a saída do splash (fade e escala 300 ms que já existe) é a única "entrada"; não criar animação de entrada própria. Usar `rememberReducedMotion()` e `animateFloatAsStateWithReduceMotion` de `ui/theme/Motion.kt`.

## 8. Assets
Fontes (res/font, já existem): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.otf`, `inter_medium.otf`, `inter_semibold.otf`, `saira_stencil_one_regular.ttf`.
Drawables novos (neste pacote): `ic_login_topo.xml` (arte do hero, 477 x 468, 18 traços oliva com `strokeAlpha`), `ic_google_g.xml` (G monocromático, usar com tint OnOlive). Já existe: `ic_elite_mark.xml` (logo, no `login.logo.mark`).
Ícones Material: `Mail` (`login.actions.email.icon`), `Visibility` e `VisibilityOff` (`login.sheet.eye.icon`), `Shield` (`login.slide.2.league.icon`), `EmojiEvents` e `Lock` (`login.slide.4.list.a1.icon` a `a5`). Sem imagens raster.

## 9. Mudanças
| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| ui/theme/Color.kt | sem tokens de login | adicionar os tokens "novo" da seção 3 | primeiro; só valores derivados das cores atuais |
| res/drawable/ic_login_topo.xml, ic_google_g.xml | não existem | copiar do pacote | sem edição |
| ui/components/EliteControls.kt (`EliteButton`) | texto Primary usa `EliteTextPrimary` (osso) sobre Olive, contraste 1,83:1 | parâmetro opcional `contentColor: Color? = null`; no login passar `OnOlive` (8,39:1) | não mudar o padrão: o resto do app usa o botão como está |
| account/ (novo `LoginPrefs.kt`) | não existe | guarda "login dispensado" em **SharedPreferences próprio** (`login_prefs`) | **não** usar `AccountStore` (ver Armadilhas) |
| ui/MainViewModel.kt (linha 77, `accountRepository.restore()`) | restore sem sinal de término | `restoreFinished: StateFlow<Boolean>` (true ao fim do restore ou após 4 s, `withTimeoutOrNull`); `loginDismissed` lido do `LoginPrefs`; `onSkipLogin()`; `showLogin = restoreFinished && !loginDismissed && accountState !is SignedIn` | `onSignInClick` e `onGoogleSignIn` ficam como estão |
| ui/components/login/PhoneMockups.kt | não existe | `PhoneMockup` + 4 mocks conforme `login.slide.N.screen` | tamanhos em dp/sp fixos, `fontScale = 1f` |
| ui/components/login/LoginComponents.kt | não existe | hero, logo, pontos, ações, botão fantasma, legal, folha de e-mail | segue a seção 5 |
| ui/screens/LoginScreen.kt | não existe | `LoginScreen(state: AuthState, onGoogle, onEmailSignIn(login, password), onSkip)` | `testTag` = data-id |
| MainActivity.kt (linhas 233-251) | `AppNavigation` e `EliteSplash(isReady = { true })` | ordem de empilhamento: `AppNavigation` (baixo), `LoginScreen` quando `showLogin`, `EliteSplash` (topo); `isReady = { restoreFinished }`; passar `onGoogle = { launchGoogleSignIn(viewModel::onGoogleSignIn) }` | `AppUpdatePrompt` continua depois do splash |
| ui/components/account/AccountCard.kt | login no Perfil | sem mudança | Perfil continua sendo o lugar de entrar depois |

## 10. Armadilhas
- **`AccountStore.save(null)` faz `prefs.clear()`** e o `restore()` chama isso a cada abertura sem usuário. Se a flag "login dispensado" ficar no `AccountStore`, ela some e o login volta toda vez. Por isso `LoginPrefs` separado.
- **Corrida na abertura:** o `FirebaseAccountRepository` nasce em `SignedOut` e só vira `SignedIn` depois do `restore()`. Sem o `restoreFinished`, quem já está logado veria o login piscar. O splash espera por ele (`isReady`), mas com teto de 4 s para não travar a abertura se o Firebase demorar.
- **Empilhamento:** a `LoginScreen` cobre o `AppNavigation` e precisa consumir toque (`pointerInput` vazio) para o app de baixo não receber cliques. O botão Voltar com login aberto fecha o app (a folha de e-mail trata o Voltar dela).
- **Degradês:** `verticalGradient(colorStops)` em `login.hero.fade` indo de `Background.copy(alpha = 0f)` a `Background`; com `Color.Transparent` o meio fica acinzentado. O brilho (`login.hero.glow`) é `radialGradient` centrado em x 196,5 dp, y 300 dp, raio 230 dp, de `OliveGlow` a `Olive.copy(alpha = 0f)`.
- **Arte do hero:** o vetor tem 477 dp de largura e é maior que a tela (393) de propósito; desenhar com `requiredWidth(477.dp)` alinhado ao início, recortar no hero e aplicar `graphicsLayer { translationX = -(currentPage + currentPageOffsetFraction) * 28.dp.toPx() }`. A arte segue o dedo (sem animação própria).
- **Pager:** `HorizontalPager(count 4)` com páginas transparentes; os pontos leem `pagerState.currentPage`; para animar por código usar `animateScrollToPage` com `tween(350, easing = CubicBezierEasing(.2f, .8f, .2f, 1f))`.
- **Texto do telefone:** em `CompositionLocalProvider(LocalDensity provides Density(density, fontScale = 1f))`; o telefone é desenho, não pode quebrar linha com fonte grande do sistema.
- **Títulos:** caixa alta com `.uppercase()`, quebra explícita com `\n` ("Corra contra\ngente de verdade."), `lineHeight = 32.sp`, `maxLines = 2`, Barlow Condensed Bold 30, espaçamento 0,9 sp.
- **Logotipo:** `EliteLogoFont` com `FontWeight.Normal` (o splash usa Bold, que vira negrito sintético; aqui não), 28 sp, espaçamento 6,7 sp (0,24 em).
- **Botões:** `EliteButton` já faz escala .97 em 100 ms e mínimo 52 dp; no login ele fica com o `Spacer` de 8 dp entre ícone e texto (o HTML usa 8). O fantasma (48 dp, borda 1 dp `LineStrong`, pílula, texto `Text` 15/20) é novo e precisa repetir a escala .97. Alvo de toque mínimo 44 dp em todos (olho da senha incluído).
- **Campos:** `OutlinedTextField` com `placeholder` (não `label`), cores de `eliteTextFieldColors()` mais `focusedContainerColor`/`unfocusedContainerColor = Field`, `shape = RoundedCornerShape(14.dp)`, texto Inter 16/22, `KeyboardType.Email` + `ImeAction.Next` e `KeyboardType.Password` + `ImeAction.Done` (Done envia).
- **Erro velho:** `AuthState.Error` fica no repositório depois de fechar a folha. Mostrar o banner só se o usuário já enviou nesta abertura da folha **e** o estado for `Error`.
- **Sombra do telefone:** `box-shadow` do HTML não tem equivalente exato; usar `Modifier.shadow` com `ambientColor`/`spotColor` pretos (desvio aceito).
- **Estado, não arrays:** o slide atual vem do `PagerState`; o estado da folha (`showEmailSheet`), campos e `attempted` ficam em `rememberSaveable` (a MainActivity já usa `rememberSaveable` para o splash).

## 11. Aceitação
- diff médio <= 3.0 e pior célula <= 10.0 em cada estado de states.json (slide_1 a slide_4, email_sheet, email_erro, entrando, google_pressionado, email_pressionado).
- compare_bounds: todos os data-ids dentro de ±2 dp, inclusive em `login.slide.N.phone` e nos ids internos de cada telefone.
- `slide_3_transicao`: nos quadros 0/100/200/350 ms a página só precisa terminar em 350 ms no lugar certo; a curva é conferida na folha de movimento.
- Cada linha da tabela Motion implementada e conferida na folha de movimento.
- Fluxo: primeira abertura mostra splash e depois login; entrar com Google, entrar com e-mail e "Continuar sem conta" levam ao app; reabrir o app logado ou após dispensar não mostra o login.
- Desvios aceitos: sombra do telefone; spinner do Material no lugar do spinner CSS (excluir 20 x 20 dp do `entrando` do diff); animação de abrir a folha do Material (300 ms de referência); renderização do vetor da arte (antialias); ícone G monocromático.

## 12. Perguntas em aberto
- **Cadastro por e-mail:** hoje o app só tem `signIn` (e-mail e senha de conta já existente) e Google; não existe "Criar conta" nem "Esqueci a senha". Por isso o botão principal é Google (cria a conta sozinho) e o de e-mail serve para contas já criadas. Querem cadastro e recuperação de senha? Seria um pacote à parte.
- **Quando mostrar:** assumi **só na primeira abertura** (some ao entrar ou ao tocar em "Continuar sem conta"). Se preferirem mostrar sempre que estiver deslogado, é só trocar a regra de `showLogin`.
- **Ordem com o onboarding por modalidade** (`workspaceManager`): assumi que o login vem antes dele. Não li essa tela no código.
- **Ícone do Google:** usei o G monocromático para combinar com o tema; as diretrizes de marca do Google preferem o botão padrão (G colorido). Confirmar se querem seguir à risca.
- **Selo "Premium"** no slide 1 vem do site (a competição online aparece com selo Premium); confirmar se está correto no app.
- **Fotos:** a referência usa fotos de atletas. Sem imagens licenciadas do Elite, usei arte vetorial; se houver fotos próprias, dá para trocar o hero.
- Nomes (Rafael M., Camila S., Bruno A.) e pontos são exemplo, iguais aos do site.
