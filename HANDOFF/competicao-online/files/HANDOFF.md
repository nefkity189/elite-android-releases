# HANDOFF - Perfil: Conta e Login  (v1)

## 1. Objetivo
Adiciona ao **Perfil** uma seção **Conta**, logo abaixo do `ProfileHeader` (e do `NameEditor`, quando aberto), com o botão **Entrar com Google** onde o Firebase Auth será ligado no futuro. Esta entrega **não usa Firebase**: ela deixa a **base pronta** (modelos, repositório, estados, diálogos e UI) rodando com um repositório de exemplo (`FakeAccountRepository`), para que a fase 2 troque só o repositório.

O que a seção cobre, em 8 estados: deslogado, entrando, erro ao entrar, logado, logado sem estado (UF), seletor de estado, confirmação de sair e confirmação de excluir conta. O **estado (UF)** existe porque o ranking "Meu estado" da Competição Online precisa dele, e o app hoje só guarda `nome`. **Excluir conta** existe porque o Google Play costuma exigir essa opção dentro do app para quem permite criar conta (confirmar a regra atual no Play Console antes de publicar).

Premissas assumidas (confirme em Perguntas em aberto): login só com **Google** na v1 (sem e-mail e senha, para evitar fluxos de senha e verificação); usar o Elite **sem conta** continua possível; o plano e os treinos nunca dependem da conta.

## 2. Dispositivo
Poco X7, 393 x 851 dp, tema escuro, Jetpack Compose. Fontes do app: Barlow Condensed (números e títulos), Inter (texto), Saira Stencil One (logotipo, não usado aqui). O HTML reserva 28dp no topo (barra de status; no app, o `statusBarsPadding()` que a tela já tem) e 16dp nas laterais (`EliteSpacing.screenHorizontal`).
Outros aparelhos: 360 a 412dp de largura e `fontScale` até 1.3. Larguras fixas só em avatar, ícone e botão de ícone; todo o resto é `fillMaxWidth`/`weight`. Textos de botão, badge e chip usam `maxLines = 1` + `Ellipsis`; o corpo do card de conta pode quebrar em várias linhas (a tela rola). A barra inferior (`RunnaBottomNav`) já existe e **não muda**; no HTML ela só ocupa o lugar (64dp + 10dp de margem).

## 3. Tokens
| Variável CSS | Valor | Nome Kotlin |
|---|---|---|
| --page | #050604 | só do preview HTML (fora do app) |
| --bg | #0A0B09 | Background (existe) |
| --surface | #10120D | Surface (existe) |
| --card | #161911 | Card (existe) |
| --card-el | #1D2116 | CardElevated (existe) |
| --field | #0D0F0A | Field (existe) |
| --line | rgba(236,232,218,.10) = 0x1AECE8DA | Line (existe) |
| --line-strong | rgba(236,232,218,.18) = 0x2EECE8DA | LineStrong (existe) |
| --text | #ECE8DA | Text (existe) |
| --muted | #8F9384 | TextMuted (existe) |
| --dim | #5E6255 | TextDim (existe) |
| --olive | #A3B565 | Olive (existe) |
| --olive-dark | #5D6B34 | OliveDark (existe) |
| --olive-soft | rgba(163,181,101,.14) = 0x24A3B565 | OliveSoft (existe) |
| --on-olive | #0E1306 | OnOlive (existe) |
| --red | #D12B2B | Red (existe) |
| --red-soft | rgba(209,43,43,.15) = 0x26D12B2B | RedSoft (existe) |
| --red-line | rgba(209,43,43,.35) = 0x59D12B2B | RedLine (existe; não usar o .50 do pacote Competição) |
| --on-red | #FFF4F0 | OnRed (existe) |
| --brass | #C9A24B | Brass (existe) |
| --amber | #E0A93A | Amber (existe) |
| --amber-soft | rgba(224,169,58,.14) = 0x24E0A93A | AmberSoft (NOVO; reaproveitar se o pacote Competição já criou) |
| --amber-notice | rgba(224,169,58,.33) = 0x55E0A93A | AmberNoticeLine (NOVO; hoje é literal em `SettingsNotice`; nome próprio para não colidir com `AmberLine` .45 do pacote Competição) |
| --ok | #7FB069 | Ok (existe) |
| --ok-soft | rgba(127,176,105,.15) = 0x267FB069 | OkSoft (NOVO; reaproveitar se já existir) |
| --scrim | rgba(0,0,0,.62) = 0x9E000000 | EliteScrim (NOVO; reaproveitar se já existir) |
| --av1 | #3B4426 | EliteAvatar.A1 (NOVO; reaproveitar se já existir) |
| --hdr-core | #262B1A | literal do `ProfileHeader` (0xFF262B1A), sem token |
| --num / --ui | Barlow Condensed / Inter | EliteDisplayFont / EliteBodyFont (existem) |

Tipografia (px = sp): título de tela 24/24 Barlow 700 caixa-alta ls .96 (`headlineLarge`); título de card `t2` 20/23 Barlow 600 caixa-alta ls .8 (`headlineMedium`); nome do usuário no card 16/20 Inter 600; corpo 14/19.6 Inter 400 TextMuted; legenda 12/16 Inter 400; eyebrow 11/14 Inter 600 caixa-alta ls 1.3 (`EliteTextStyles.Label` via `SectionTitle`); botão 16/20 Inter 600; chip 12 Inter 500. Formas: card r18, botão primário pill r28 (h56), botão fantasma r24 (h48), chip r15, badge r12, diálogo r20, sheet r26 no topo, chip de UF r12.

## 4. Mapa de componentes
| data-id | Composable | Arquivo | Novo? |
|---|---|---|---|
| `screen` | ProfileScreen | ui/screens/ProfileScreen.kt | não (recebe a seção Conta) |
| `profile.header` | ProfileHeader | ui/components/SettingsComponents.kt | não |
| `acct.title` | SectionTitle("Conta") | ui/components/SettingsComponents.kt | não |
| `acct.card` | AccountCard | ui/components/account/AccountCard.kt | sim |
| `acct.out` | SignedOutContent | ui/components/account/AccountCard.kt | sim |
| `acct.out.error` | SettingsNotice (variante sem botão) | ui/components/SettingsComponents.kt | não (ajustar: botão opcional) |
| `acct.out.google`, `.loading`, `.retry` | GoogleSignInButton (3 estados: normal, carregando, tentar de novo) | ui/components/account/GoogleSignInButton.kt | sim |
| `acct.in` | SignedInContent | ui/components/account/AccountCard.kt | sim |
| `acct.in.avatar` | InitialsAvatar (48dp, fundo por hash do uid: EliteAvatar.A1..A5) | ui/components/account/InitialsAvatar.kt | sim |
| `acct.in.badge` | ConnectedBadge | ui/components/account/AccountCard.kt | sim |
| `acct.in.uf` | ValueRow | ui/components/SettingsComponents.kt | não (usar `ValueRow`, aceitar variante de aviso) |
| `acct.in.signout`, `acct.in.delete` | OutlinedButton / TextButton vermelho | ui/components/account/AccountCard.kt | sim |
| `dlg`, `dlg.del` | EliteDialog | ui/components/EliteDialogs.kt | não |
| `uf.root`, `uf.sheet`, `uf.grid`, `uf.confirm` | StatePickerSheet (ModalBottomSheet) | ui/components/account/StatePickerSheet.kt | sim |
| `profile.nav` | RunnaBottomNav | ui/components/RunnaBottomNav.kt | não (só ocupa espaço) |
| (modelo) | UserAccount, AuthState, AccountDialog | account/AccountModels.kt | sim |
| (dados) | AccountRepository, FakeAccountRepository, AccountStore | account/*.kt | sim |

Cada `data-id` vira `Modifier.testTag("<data-id>")`.

## 5. Regras de layout
- A tela é a coluna rolável que já existe (`verticalScroll`, `spacedBy(12.dp)`). A seção Conta entra como dois filhos novos dessa coluna: `SectionTitle("Conta")` e `AccountCard`, depois do `ProfileHeader`/`NameEditor` e antes de `SectionTitle("Durante a corrida")`.
- Não há elemento elástico: o `AccountCard` tem altura do conteúdo. Medidas do HTML (dp): topbar 44, header 94, gap 12, título "Conta" 14, card começa em y=216. Altura do card: deslogado/entrando 310.8; erro 382.8; logado 264; logado sem UF 275.
- Card deslogado: padding 16, gap 14: linha (tile 44 + título), corpo (3 linhas a 393dp), chips (30dp, `FlowRow` com gap 8), aviso de erro (só no estado erro), botão (h56, largura total), legenda centralizada.
- Card logado: linha do usuário (avatar 48, nome, e-mail, badge "Conectado") com padding 16; divisor 1dp com margem horizontal 16; `ValueRow` "Estado" (min 56dp, cresce no estado sem UF); divisor; botão "Sair da conta" (h48) e "Excluir minha conta" (h44, texto vermelho), gap 4, padding 14/16.
- `acct.in.name` e `acct.in.email`: `weight(1f)`, `maxLines = 1`, `Ellipsis`; o badge nunca encolhe.
- Diálogos: `EliteDialog` centralizado na tela, largura 311dp (margem 41dp), cobrindo inclusive a barra inferior. Sheet de estado: `ModalBottomSheet` ancorado embaixo, altura do conteúdo (485dp a 393dp), grade de 5 colunas iguais (`GridCells.Fixed(5)`), chip h40, gap 8, botão Confirmar h56; se a altura passar de 90% da tela (fontScale alto), o conteúdo da sheet rola.
- Telas < 760dp: nada se esconde; a tela inteira já rola.
- Rolagem: sim (a da tela). Ao abrir o estado `erro`, não rolar sozinho.

## 6. Estados
| Estado | Como disparar (states.json) | O que muda |
|---|---|---|
| deslogado | `eval: __setState('deslogado')` | Card "Entre para competir", chips, botão olive "Entrar com Google". Header mostra "Recruta". |
| entrando | `eval: __setState('entrando')` | Botão vira cinza desabilitado com spinner e "Entrando…". Mesmo tamanho (56dp). |
| entrando_spinner | `eval: __setState('entrando')` + `frames` | Spinner gira 360° em 1000ms (quadros 0/250/500/750ms). |
| erro | `eval: __setState('erro')` | Aviso âmbar acima do botão; botão olive "Tentar de novo". Card cresce 72dp. |
| logado | `eval: __setState('logado')` | Card com avatar, nome, e-mail, badge, Estado "MT", Sair e Excluir. Header mostra o nome do usuário. |
| logado_sem_uf | `eval: __setState('logado_sem_uf')` | Linha Estado com aviso âmbar "Escolha para aparecer no ranking local" e botão-chip "Escolher". |
| uf_sheet | `eval: __setState('uf_sheet')` | Sheet de estado aberta sobre o estado `logado_sem_uf`; MT aparece escolhido como exemplo (sem escolha, "Confirmar" fica desabilitado). |
| sair_dialog | `eval: __setState('sair_dialog')` | Diálogo "Sair da conta?" com Cancelar e Sair. |
| excluir_dialog | `eval: __setState('excluir_dialog')` | Diálogo "Excluir sua conta?" com Cancelar e Excluir (vermelho). |
| google_pressionado | press `acct.out.google` | Escala 0.96. |
| sair_pressionado | press `acct.in.signout` | Escala 0.96. |

Estado vem do `AuthState` (ViewModel) e do enum `AccountDialog { None, SignOut, Delete, UfSheet }`; o estado `logado_sem_uf` é `SignedIn` com `uf == null`. Quem controla diálogo e sheet é o ViewModel, não `remember` local.

## 7. Motion
| data-id | Gatilho | Propriedade | De -> Para | Duração | Atraso | Easing | Loop | Reduzir animação |
|---|---|---|---|---|---|---|---|---|
| `acct.out.google.spinner` | estado entrando | rotação | 0 -> 360deg | 1000ms | 0 | linear | infinito (keyframes `acct-spin`) | parado |
| `acct.out.google`, `acct.out.google.retry`, `acct.in.signout`, `acct.in.delete`, `dlg.cancel`, `dlg.confirm`, `dlg.del.cancel`, `dlg.del.confirm`, `uf.confirm` | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |
| `uf.ac` ... `uf.to` (todos os chips de UF) | toque | escala | 1 -> 0.96 | 120ms | 0 | ease-out | não | sem escala |

Regra geral: a única animação em repouso é o spinner (só no estado `entrando`). Entrada/saída do diálogo e da sheet usam os padrões do app (`EliteMotion.DialogEnter` 200ms / `DialogExit` 150ms; sheet padrão do Material 3); não estão no HTML e não entram no diff. `AisleMotion`/`EliteMotion.Tap` = 100ms existe; use os 120ms desta tabela nos botões novos.

## 8. Assets
Fontes (res/font, nomes exatos, já existem): `barlow_condensed_medium.ttf`, `barlow_condensed_semibold.ttf`, `barlow_condensed_bold.ttf`, `inter_regular.ttf`, `inter_medium.ttf`, `inter_semibold.ttf`, `inter_bold.ttf`.
Ícones Material (`data-icon`): Person, Warning, Place, ChevronRight (`KeyboardArrowRight`), Edit (já usado). O ícone **G** do botão é um traço monocromático simples (círculo aberto com barra), não a marca colorida do Google. Quando o Auth for ligado, usar o recurso oficial do Google conforme as diretrizes de marca (ver Perguntas em aberto). Spinner = `CircularProgressIndicator` de 20dp, traço 2dp, cor `Muted`, ou `Canvas` + `drawArc` (arco de 270°). A marca do avatar do header (`ic_elite_mark`) já existe; o SVG do HTML é só aproximação.

## 9. Mudanças
Fase 1 = implementar agora. Fase 2 = só registro, **não implementar**.

| Arquivo / Componente | Hoje | Alvo | Observação |
|---|---|---|---|
| ui/theme/Color.kt | Não tem `AmberSoft`, `AmberNoticeLine`, `OkSoft`, `EliteScrim`, `EliteAvatar.A1..A5` | Criar os que faltarem (Fase 1) | Conferir antes se o pacote Competição já criou. Não redefinir `RedLine` (existe com .35). |
| account/AccountConfig.kt (novo) | — | `const val USE_FAKE_ACCOUNT = true`; `const val REQUIRE_LOGIN_FOR_COMPETITION = false` | Fase 1. Duas chaves: trocar o repositório e exigir login na Competição. |
| account/AccountModels.kt (novo) | — | `data class UserAccount(uid, displayName, email, photoUrl: String?, uf: String?)`; `sealed interface AuthState { SignedOut; SigningIn; Error(message); SignedIn(account) }`; `enum class AccountDialog { None, SignOut, Delete, UfSheet }`; lista `BRAZIL_UFS` (27 siglas) | Fase 1. Sem dependência de Firebase. |
| account/AccountRepository.kt (novo) | — | `interface AccountRepository { val authState: StateFlow<AuthState>; suspend fun signIn(); suspend fun signOut(); suspend fun deleteAccount(); suspend fun updateUf(uf: String) }` | Fase 1. `signIn()` **sem** parâmetro de Context: a fase 2 recebe a Activity por outro meio (ver Fase 2). |
| account/FakeAccountRepository.kt (novo) | — | Implementação de exemplo: `signIn()` espera 1200ms e entra como "Marcos Silva / marcos.silva@gmail.com" sem UF; um toggle de teste (`failNextSignIn`) força o estado Error; `signOut`/`deleteAccount` limpam; `updateUf` valida a sigla | Fase 1. Guarda o último estado no `AccountStore` para sobreviver ao reinício do app. |
| account/AccountStore.kt (novo) | — | DataStore Preferences próprio (`account_store`): uid, displayName, email, uf, signedIn | Fase 1. **Não usar Room**: o app tem 4 bancos (AppDatabase, Plan2_5k, Plan5k, Plan10k) e uma coluna nova em `runner_profile` exigiria migração em todos. |
| ui/MainViewModel.kt | Só tem `setUserName`; sem conta | `accountState: StateFlow<AuthState>`, `accountDialog: StateFlow<AccountDialog>`, `onSignInClick()`, `onSignOutConfirm()`, `onDeleteConfirm()`, `onUfConfirm(uf)`, `openAccountDialog(d)`, `dismissAccountDialog()` | Fase 1. Escolhe o repositório por `USE_FAKE_ACCOUNT`. Se o nome local estiver vazio no primeiro login, preencher com o nome da conta; **nunca sobrescrever** um nome que o usuário já digitou. |
| ui/components/account/AccountCard.kt (novo) | — | `AccountCard(state, onSignIn, onRetry, onPickUf, onSignOut, onDelete)` com `SignedOutContent` e `SignedInContent` | Fase 1. Medidas da seção 5. |
| ui/components/account/GoogleSignInButton.kt (novo) | — | Botão h56 r28 com 3 estados (normal, carregando desabilitado, tentar de novo) | Fase 1. Mesmo tamanho nos 3 estados. |
| ui/components/account/InitialsAvatar.kt (novo) | — | Avatar 48dp, r16, borda 2dp OliveDark, iniciais Barlow 700 18sp; cor de fundo por hash do `uid` entre A1..A5 | Fase 1. Sem foto (não há Cloud Storage no plano gratuito; ver Perguntas). |
| ui/components/account/StatePickerSheet.kt (novo) | — | `ModalBottomSheet` com grade 5 colunas das 27 UFs, "Confirmar" desabilitado sem escolha | Fase 1. Estado `selected` interno; confirmar chama `onUfConfirm`. |
| ui/components/SettingsComponents.kt | `SettingsNotice` exige botão; `ValueRow` sem variante de aviso | Botão opcional no `SettingsNotice`; `ValueRow` aceita subtítulo âmbar e valor-chip âmbar | Fase 1. Não mudar o visual dos usos atuais. |
| ui/screens/ProfileScreen.kt | `ProfileHeader` -> (`NameEditor`) -> "Durante a corrida" | Inserir `SectionTitle("Conta")` + `AccountCard` entre eles; novos parâmetros com valor padrão (`accountState = AuthState.SignedOut`, callbacks vazios) | Fase 1. Diálogos (`EliteDialog` sair/excluir) e `StatePickerSheet` desenhados aqui, dirigidos por `accountDialog`. Testes antigos precisam continuar passando (padrões garantem). |
| MainActivity.kt (linha ~644, chamada de `ProfileScreen`) | Sem conta | Passar `accountState`, `accountDialog` e callbacks do ViewModel | Fase 1. Linha 424 (`isPremium = true`) **não mexer** nesta entrega. |
| MainActivity.kt (linha ~425, `onOpenCompetition`) | Abre a Competição direto | `if (!REQUIRE_LOGIN_FOR_COMPETITION \|\| signedIn) showCompetition = true else navegar ao Perfil` | Fase 1, desligado (flag = false). Deixa o gancho pronto sem mudar o comportamento atual. |
| src/test/.../account/FakeAccountRepositoryTest.kt (novo) | — | Testa: SignedOut -> SigningIn -> SignedIn; falha forçada -> Error -> SignedIn; signOut; deleteAccount; `updateUf` rejeita sigla inválida | Fase 1. Usar `runTest` e tempo virtual (sem esperar 1200ms real). |
| Limpeza | `src/main/java/com/elite.zip` e `src/main/java/com/estou aqui.txt` dentro do código-fonte | Remover os dois | Fase 1. Não afetam o build, mas não pertencem ao projeto. |
| account/FirebaseAccountRepository.kt (novo) | — | `signIn` com Credential Manager + `GoogleIdTokenCredential` -> `FirebaseAuth.signInWithCredential`; cria `users/{uid}`; `deleteAccount` apaga os dados do Firestore e depois o usuário Auth | **Fase 2, não implementar.** Exige reautenticação recente para excluir. A Activity chega por um `ActivityProvider`, não pela interface. |
| build.gradle.kts, google-services.json, AndroidManifest.xml | Sem Firebase | Plugin `google-services`, BOM, `firebase-auth`, `firebase-firestore`, `androidx.credentials`, permissão `INTERNET` | **Fase 2, não implementar.** Cadastrar SHA-1 de debug, de release e da assinatura do Google Play no console. |

## 10. Armadilhas
- A seção entra **dentro** da coluna rolável existente; não criar uma segunda `Column` com `verticalScroll`.
- Mesmo tamanho nos 3 estados do botão (56dp): o card não pode pular entre `deslogado` e `entrando`; só o estado `erro` cresce (o aviso entra no fluxo).
- Chips de benefício: `FlowRow` (quebram de linha com `fontScale` 1.3), cada um com `maxLines = 1`. Nunca largura fixa.
- Elástico = `Modifier.weight(1f)` (não `fill = false`) nos textos ao lado de avatar e badge; `Ellipsis` em nome e e-mail.
- Estado vem do ViewModel (`StateFlow<AuthState>` e `AccountDialog`), não de `remember` local; trocar de aba e voltar não pode perder o diálogo aberto nem a escolha da sheet.
- `AuthState.SigningIn`: botão desabilitado e **sem** reentrada (dois toques não disparam dois logins); rotação do dispositivo durante o login não cancela nem repete.
- O `signIn()` da interface não recebe `Context`: a fase 2 precisa de uma Activity, e passar contexto por ViewModel vaza memória. Resolver com um provedor de Activity, não com parâmetro.
- Diálogo e sheet cobrem a tela toda, inclusive a barra inferior (`Dialog`/`ModalBottomSheet`, não `Box` interno). Alvo de toque mínimo 48dp: chips de UF têm 40dp visual, usar `minimumInteractiveComponentSize()`.
- Gradiente do avatar do header copia o do `ProfileHeader` existente (`radialGradient` 0xFF262B1A -> Background); não recriar.
- Cor CSS `rgba(r,g,b,a)` é `Color(0xAARRGGBB)` no Kotlin (alpha primeiro); a tabela de tokens já está convertida. `RedLine` existente é .35, não o .50 do pacote Competição.
- `text-transform: uppercase` não existe no Compose: aplicar `.uppercase()` (cuidado com acentos) nos títulos `t2` e no eyebrow.
- `font-variant-numeric: tabular-nums` = `fontFeatureSettings = "tnum"`; `letter-spacing` em px = `letterSpacing` em sp.
- Fontes como arquivos `.ttf` em `res/font`; meça com `--fonts-dir` (o render sem as fontes engana).
- Não gravar senha, token nem ID token no `AccountStore`. Só nome, e-mail, UF e uid.
- Nome do header: o `ProfileHeader` continua mostrando o nome **local** (`userName`). O HTML mostra "Marcos" no logado só como exemplo de nome preenchido a partir da conta.

## 11. Aceitação
- diff médio <= 3.0 e pior célula <= 10.0 em cada estado de `states.json`, **excluindo a região da barra inferior** (y >= 777, `profile.nav`, que já existe e foi só desenhada aproximada).
- `compare_bounds`: todos os `data-id` dentro de ±2dp, exceto `profile.nav` (fora do escopo) e as alturas dos textos de 3 linhas, que dependem da fonte real (±3dp). Diálogos centralizados: conferir o centro vertical, não o y absoluto.
- Cada linha da tabela Motion implementada e conferida na folha de movimento (spinner, 120ms de escala).
- Teste extra obrigatório: largura 360dp e 412dp, `fontScale` 1.3: nada cortado, chips quebram de linha, nenhum botão abaixo de 48dp de toque, nenhuma sobreposição, sheet de estado rola se não couber.
- Teste de fluxo (no emulador, com o repositório de exemplo): deslogado -> entrando -> logado sem UF -> escolher UF -> logado -> sair -> deslogado; forçar falha -> erro -> tentar de novo -> logado; girar a tela no meio do login; excluir conta.
- `./gradlew testDebugUnitTest` passando, incluindo `FakeAccountRepositoryTest` e todos os testes existentes.
- Desvios aceitos: ícone G monocromático; spinner e barra inferior aproximados no HTML; `backdrop-filter` não é usado; avatar de iniciais no lugar de foto.

## 12. Perguntas em aberto
- Login só com Google na v1? Se quiser e-mail e senha também, falta desenhar a tela de e-mail, recuperação de senha e verificação.
- Exigir login para abrir a Competição Online? A flag `REQUIRE_LOGIN_FOR_COMPETITION` deixa isso pronto; hoje fica desligada. Quando ligar, o toque em "Competição online" sem conta leva ao Perfil com foco em Conta (ou a um bottom sheet de login; não desenhado).
- Excluir conta: confirmar no Play Console a regra atual de exclusão de conta (dentro do app e link na ficha da loja) antes de publicar.
- Botão do Google: a fase 2 deve seguir as diretrizes oficiais de marca (ícone colorido, texto "Fazer login com o Google" ou equivalente). O protótipo usa um G monocromático.
- Foto de perfil: o plano gratuito do Firebase não inclui Cloud Storage. Usar só iniciais (como no protótipo) ou a foto da conta Google (URL, sem upload nosso)?
- O estado (UF) é obrigatório para entrar no ranking local ou opcional (usuário sem UF só aparece no global)? O protótipo trata como opcional, com aviso.
- Fuso dos horários das missões (Brasília ou local do usuário) não afeta esta tela, mas depende da conta; decidir junto com o Hub.

## Changelog
- v1: primeira entrega.
