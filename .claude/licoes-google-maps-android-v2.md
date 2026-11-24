# Lições 2.0: Google Maps SDK em um app Android de corrida (Compose)

Versão 2.0, revisada em 17/11/2026. Mudanças em relação à 1.0 estão na seção 14.

## O que foi (e o que não foi) verificado

Fontes, por nível de confiança:

- **Lido na página inteira da documentação oficial** (developers.google.com/maps/documentation/android-sdk):
  *Camera and view* (`/views`), *Add a Styled Map* (`/styling`) e *Shapes* (`/shapes`, onde
  estão as polilinhas). Mostravam "última atualização 2026-11-09".
- **Visto em trechos de resultado de busca de páginas oficiais, nesta revisão:**
  *Maps Compose library*, *Location data*, *Lite mode* e *Configure a map*. Os trechos
  bastam para as afirmações marcadas "(trecho oficial)", mas não li as páginas inteiras.
- **Visto em trechos, na revisão anterior:** *Use API keys* (chave, restrição, SHA-1),
  *Quickstart* (Secrets Gradle Plugin) e *Usage and billing*.
- **De fontes que não são do Google** (blogs, espelhos do GitHub, resumo de Termos de
  Serviço de terceiros): só como pista, marcadas no texto.
- **Marcado "(confira)":** conhecimento geral, **não** lido numa página. Valide antes de
  depender. Isso vale para todo o código Kotlin e para a parte de Android (serviço em
  primeiro plano, permissões).

Nenhuma lição aqui foi compilada ou testada no seu app.

Contexto: você está portando o Larping (iOS, MapKit) e escolheu o **Maps SDK for Android**.
O atrito vem de duas coisas: o Maps SDK depende de **Google Play Services** e de **rede**, e
o Larping prometia "zero networking".

## Resumo (o essencial do app de corrida)

1. O traço da corrida é uma **`Polyline`**: uma lista de `LatLng` ligados **em linha reta, na
   ordem dada**. Ela **não segue ruas**. Ligar só A a B dá uma reta; a rota que você correu
   é a lista de pontos que o GPS gravou.
2. **O Maps SDK não fornece os pontos.** Eles vêm da **Location API do Google Play
   Services** (seção 4). A camada "My Location" do mapa só mostra o ponto azul.
3. Grave cada ponto no **Room** assim que chega, com um **`segmentId`** que sobe a cada
   pausa ou perda de GPS (uma `Polyline` por trecho).
4. A tela **só observa** o banco (Flow) e desenha. A rota fica como **estado**, não como
   objeto guardado; por isso sobrevive a girar a tela, apagar e reabrir (seção 5).
5. Ordem de trabalho: chave e mapa → polilinha fixa → GPX de teste → pausa e A/B → service e
   Room → detalhe → extras (seção 11).

---

## 0. Decisões a tomar antes de escrever código

1. **O app deixa de ser "zero rede".** O mapa baixa tiles. Você precisará de `INTERNET` e
   `ACCESS_NETWORK_STATE` no manifesto. Ajuste README, política de privacidade e (se
   publicar na Play Store) o formulário de Segurança dos dados. Os **dados** (atividades,
   pontos, estatísticas) continuam locais; só a imagem do mapa usa rede.
2. **Sem Play Services, não há mapa.** ROMs sem Google, alguns aparelhos e o F-Droid ficam
   sem o mapa, **e também sem a Location API** (seção 4). Decida o que o app mostra nesse
   caso (seção 9).
3. **Não existe mapa offline com o Google Maps.** Se isso for requisito, o caminho é outra
   biblioteca (MapLibre/OSM), não um "cache" do Google (seção 9).
4. **Map ID ou não.** Usar Map ID (estilo na nuvem, marcadores avançados) muda a cobrança
   (seção 1). Para começar, não use.
5. **Rota "seguindo ruas" é outro produto.** Se um dia quiser traçar um trajeto *planejado*
   até um destino, o Maps SDK não faz isso; precisa de outra API paga (Routes API). Para
   mostrar a rota que **você correu**, não precisa.

---

## 1. Chave de API, segurança e custo

- **Restrinja a chave antes de produção.** No Console do Google Cloud, em *Credenciais*:
  - Restrição de aplicativo: **Apps Android**, com **nome do pacote + SHA-1** do
    certificado que assina o app;
  - Restrição de API: **Maps SDK for Android** (e só as APIs que você realmente usa).
- **Uma chave por app e por plataforma.** Se amanhã fizer um app iOS, use outra chave.
- **Cada tipo de build tem uma SHA-1 diferente.** Registre a do **debug** e a do
  **release**. No Windows:
  ```powershell
  .\gradlew signingReport
  ```
  Com *Play App Signing*, a SHA-1 que vale em produção é a da chave de assinatura que a
  Play usa, mostrada no Play Console **(confira)**. SHA-1 errada = mapa em branco só na
  versão publicada.
- **A chave vai dentro do APK** (manifesto). Por isso a restrição é obrigatória; esconder a
  chave do git só evita vazar o código-fonte. Use o **Secrets Gradle Plugin**
  (`com.google.android.libraries.mapsplatform.secrets-gradle-plugin`): a chave fica em
  `secrets.properties`/`local.properties` (no `.gitignore`) e entra no manifesto como
  `com.google.android.geo.API_KEY`, o nome de metadado recomendado.
- **Não cole a chave em chats, issues ou prints.** Se vazou, **crie outra e apague a
  antiga**.
- **Custo.** O uso móvel do Maps SDK for Android é ilimitado, mas **o projeto precisa de
  conta de faturamento** ativa. Configure **alertas de orçamento e cotas** de qualquer forma.
- **Map ID muda o SKU.** Mapa criado com Map ID passa a ser cobrado como *Dynamic Maps*
  (franquia mensal gratuita limitada), não mais como mapa móvel nativo.
- **Street View é cobrado à parte.** Se não usar, não ative.

---

## 2. Dependências e ambiente

- **Template do projeto:** com Compose, crie como **"Empty Compose Activity"**. *(trecho
  oficial)* A página do Maps Compose usa esse template; não use "Google Maps Activity".
- **`minSdk` 23 ou superior** e linguagem Kotlin. *(trecho oficial)*
- Use **`maps-compose`** (`com.google.maps.android:maps-compose`). Opcionais:
  `maps-compose-utils` (clustering, metadados de Street View) e `maps-compose-widgets`
  (ScaleBar).
- **Versão:** uma cópia em inglês da página mostra **8.4.0**. Outras cópias da mesma página
  (outros idiomas e domínios) mostram 6.12.0, 6.4.1 e 4.4.1, ou seja, **cópias
  desatualizadas circulam**. Confirme a versão atual no GitHub
  (`googlemaps/android-maps-compose`, aba Releases) e fixe no `libs.versions.toml`.
- **Idioma:** a página inicial em português tem cópia de 2024; as em inglês são mais
  recentes. Prefira o inglês.
- **Location API é uma dependência separada:** `com.google.android.gms:play-services-location`.
  **(confira)** se o `maps-compose` já a traz; declare explicitamente, não faz mal.
- **O emulador precisa de imagem com Google APIs/Play** (a página de estilo pede isso).
  Sem ela o mapa não carrega e **os testes com Maestro falham sem explicação**.
- Para simular movimento, use *Extended controls → Location → Routes* e carregue um
  **GPX/KML** **(confira o nome do menu na sua versão)**. Um ponto fixo não testa nada de
  movimento.

---

## 3. Mapa em Compose: o básico certo

- Estado de câmera elevado: `rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(...) }`.
  Passe o mesmo `cameraPositionState` a quem precisa controlá-lo (botão de recentralizar).
- Dentro de `GoogleMap { ... }` use `Polyline`, `Marker`, `Circle`. O ciclo de vida é da
  biblioteca, sem `MapView` manual. *(trecho oficial)*
- Propriedades vão em `MapProperties` e `MapUiSettings`; modifique **cópias**
  (`uiSettings.copy(...)`) para disparar recomposição. *(trecho oficial)*
- **Marcador que se move:** o exemplo oficial usa `rememberUpdatedMarkerState(position = ...)`.
  Prefira isso a `remember(pos) { MarkerState(pos) }`, que recria o estado a cada ponto.
- **`MapEffect` dá acesso ao `GoogleMap` cru**, necessário para coisas como `snapshot()`. A
  própria documentação avisa que é perigoso: o objeto é gerenciado pela biblioteca. Use só
  para o que o Compose não expõe.
- O Larping esconde o controle padrão de localização e usa um botão próprio. No Android,
  deixe `myLocationButtonEnabled = false` e faça o seu, ou terá dois botões.

---

## 4. De onde vêm os pontos (nova)

**A página *Location data* diz** *(trecho oficial)*: a camada **My Location** só *exibe* a
posição no mapa e **não fornece dados**; para pedir localização por código, a recomendação é
a **Location API do Google Play Services**. Há também a interface **`LocationSource`**, que
deixa você trocar o provedor do ponto azul por um seu.

O que decorre disso para o app (esboço meu, **não compilado**):

**Permissões.**
- Declare `ACCESS_FINE_LOCATION` e `ACCESS_COARSE_LOCATION` e peça **as duas na mesma
  solicitação**. O exemplo oficial da página *My Location* aceita qualquer uma das duas como
  concedida. No Android 12+, o usuário pode conceder só a localização **aproximada**, e um
  app de corrida precisa tratar isso (avisar que a precisão não serve para gravar).
- Explique o motivo **antes** de pedir, e trate a negação sem quebrar o app. *(trecho
  oficial)*
- Android 13+: permissão de notificações (para a notificação fixa do service). Android 14+:
  permissão específica de service de localização em primeiro plano. **(confira os nomes
  exatos na documentação do Android: `POST_NOTIFICATIONS`, `FOREGROUND_SERVICE_LOCATION`.)**

**Service em primeiro plano.** A corrida mora num *foreground service* com
`android:foregroundServiceType="location"` e notificação fixa. **(confira)** Ele é quem pede
os pontos e grava; a Activity só observa.

**Pedido de atualizações (Kotlin, de memória, não testado):**

```kotlin
val client = LocationServices.getFusedLocationProviderClient(context)

val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2_000L)
    .setMinUpdateDistanceMeters(3f)
    .build()

val callback = object : LocationCallback() {
    override fun onLocationResult(result: LocationResult) {
        result.locations.forEach { loc ->
            if (loc.hasAccuracy() && loc.accuracy <= 25f) {   // filtro: ideia minha
                // grave no Room AGORA, com o segmentId atual
            }
        }
    }
}

@SuppressLint("MissingPermission")   // só chame depois de checar a permissão
fun start() = client.requestLocationUpdates(request, callback, Looper.getMainLooper())
fun stop() = client.removeLocationUpdates(callback)
```

**Regras de gravação (minhas, valide com corridas reais):**
- Descarte fixes com **precisão ruim** (o limite de 25 m acima é um chute; ajuste olhando
  traços reais) e saltos impossíveis de velocidade.
- Grave **assim que chega**, não ao terminar, para sobreviver a morte do app.
- Incremente `segmentId` ao **pausar/retomar** e quando o GPS ficar sem fix por um tempo.
- Ao parar, chame `removeLocationUpdates`; esquecer isso drena a bateria.

**Sem Play Services:** a Location API também some. O fallback da seção 9 precisa de outra
fonte (`LocationManager`, do próprio Android). **(confira)**

**Ponto azul e pontos gravados da mesma fonte:** o `LocationSource` personalizado pode
servir para isso. Não testei; só confirmei que a interface existe.

---

## 5. Desenhar o trajeto A→B e manter a rota salva

Duas necessidades que são a mesma coisa: **(a)** o traço enquanto você corre, de A até onde
está agora, e **(b)** a rota **guardada**, que continua desenhada quando você reabre a
corrida.

### O que a documentação diz (página *Shapes*, lida)

- Uma **`Polyline`** é "uma série de segmentos de reta conectados", usada para marcar
  trajetos e rotas. Os pontos são `LatLng`, ligados **na ordem em que você os adiciona**.
- A página Maps Compose lista **`Polyline` como composable** *(trecho oficial)*. A rota não
  precisa de `MapEffect` nem de Views.
- API clássica (Views): `PolylineOptions` (`add`/`addAll`) + `GoogleMap.addPolyline(...)`, que
  devolve um `Polyline` **mutável**; para crescer com a corrida, `Polyline.setPoints(lista)`.
  Em Compose você não faz isso: passa uma lista nova e a recomposição cuida.
- Aparência: `color` (padrão preto), **largura em pixels** (padrão 10 px, **não escala com o
  zoom**), `geodesic` (padrão `false`), `zIndex` (padrão 0), `visible`, padrão tracejado,
  `jointType` (`ROUND` evita pontas) e capas (`RoundCap`, ou `CustomCap` com imagem sua).
- **Marcadores são sempre desenhados acima das outras formas**, qualquer que seja o `zIndex`.
- Trechos coloridos (`StyleSpan`), **gradiente** e **carimbo** exigem Maps SDK **18.1.0+** e o
  **renderer mais recente**.
- Polilinhas **não são clicáveis** por padrão; dá para guardar um dado com `setTag`.

### Como isso vira um app (esboço meu, **não compilado nem testado**)

**1. Modelo de dados.** Guarde cada ponto no Room **assim que chega**, pelo service:

| coluna | para quê |
|---|---|
| `runId` | a qual corrida pertence |
| `segmentId` | sobe 1 a cada **pausa/retomada** ou perda de GPS |
| `idx` | ordem |
| `lat`, `lng` | posição |
| `timeMs`, `accuracyM` | tempo e precisão (para filtrar e calcular pace) |

Por que `segmentId`: a polilinha liga pontos consecutivos. Se você pausou e retomou em outro
lugar, ou o GPS sumiu num túnel, **uma única polilinha desenharia uma reta atravessando o
intervalo**. Uma `Polyline` por trecho deixa o buraco vazio, que é o certo.

**2. Do banco para o desenho.**

```kotlin
fun List<TrackPointEntity>.toSegments(): List<List<LatLng>> =
    groupBy { it.segmentId }
        .toSortedMap()
        .values
        .map { seg -> seg.sortedBy { it.idx }.map { LatLng(it.lat, it.lng) } }

@Composable
fun RouteMap(
    segments: List<List<LatLng>>,
    modifier: Modifier = Modifier,
    fitToRoute: Boolean,            // true no detalhe; false ao gravar
    following: Boolean = false,     // true ao gravar, enquanto o usuário não arrastar
    onUserDraggedMap: () -> Unit = {},
) {
    val camera = rememberCameraPositionState()
    var mapLoaded by remember { mutableStateOf(false) }
    val strokePx = with(LocalDensity.current) { 5.dp.toPx() }   // largura é em px, não dp

    val all = remember(segments) { segments.flatten() }
    val start = all.firstOrNull()
    val end = all.lastOrNull()

    // Enquadrar só depois do mapa ter tamanho (a documentação exige o layout pronto)
    LaunchedEffect(mapLoaded, all.size, fitToRoute) {
        if (fitToRoute && mapLoaded && all.size >= 2) {
            val b = LatLngBounds.builder().apply { all.forEach(::include) }.build()
            camera.animate(CameraUpdateFactory.newLatLngBounds(b, 96))
        }
    }

    // Seguir o corredor, mas largar quando o usuário arrasta o mapa
    LaunchedEffect(end, following, mapLoaded) {
        if (following && mapLoaded && end != null) {
            camera.animate(CameraUpdateFactory.newLatLng(end))
        }
    }
    LaunchedEffect(camera) {
        snapshotFlow { camera.isMoving to camera.cameraMoveStartedReason }
            .collect { (moving, reason) ->
                if (moving && reason == CameraMoveStartedReason.GESTURE) onUserDraggedMap()
            }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camera,
        onMapLoaded = { mapLoaded = true },
    ) {
        segments.forEach { pts ->
            if (pts.size >= 2) {
                // contorno escuro por baixo, para a linha se destacar em qualquer mapa
                Polyline(points = pts, color = Color.Black.copy(alpha = 0.45f),
                    width = strokePx * 1.7f, jointType = JointType.ROUND,
                    startCap = RoundCap(), endCap = RoundCap(), zIndex = 0f)
                Polyline(points = pts, color = Color(0xFFCEFE06),
                    width = strokePx, jointType = JointType.ROUND,
                    startCap = RoundCap(), endCap = RoundCap(), zIndex = 1f)
            }
        }
        if (start != null) {
            Marker(state = rememberUpdatedMarkerState(position = start), title = "A")
        }
        if (end != null && end != start) {
            Marker(state = rememberUpdatedMarkerState(position = end), title = "B")
        }
    }
}
```

Os nomes de parâmetros e tipos (`points`, `color`, `width`, `jointType`, `startCap`,
`CameraMoveStartedReason`, `isMoving`...) são de memória: **confira na referência da versão
que usar** (googlemaps.github.io/android-maps-compose). A ideia (uma lista de `LatLng`
entra, a biblioteca desenha) é a mesma.

**3. Ao vivo.** O service grava no banco e expõe um `StateFlow` com a rota; a tela só coleta
e passa `segments` ao `RouteMap`. Assim:
- a rota **continua desenhada** quando você apaga a tela e volta, gira o aparelho ou o
  Android recria a Activity: o desenho vem do **estado**, não de um `Polyline` guardado;
- se o app for morto e reaberto no meio da corrida, o traço reaparece do banco.

**4. Rota guardada.** No detalhe, carregue os pontos do `runId`, converta com `toSegments()` e
passe `fitToRoute = true`. É o mesmo composable.

### Acréscimos

- **Largura em px:** converta de dp (`dp.toPx()`) ou o traço fica fino em tela de alta
  densidade. Olhe em aparelho de verdade.
- **Contorno:** duas polilinhas sobrepostas (larga e escura embaixo, fina e clara em cima)
  dão legibilidade sobre ruas, parques e satélite. Técnica minha, não da documentação.
- **Marcadores A e B** ficam sempre acima da linha, por regra da biblioteca.
- **`geodesic` em `false`** (padrão): em trechos curtos não faz diferença.
- **Cor por pace (fase 2):** `StyleSpan`/gradiente exigem SDK 18.1.0+ e o renderer mais
  recente. **(confira)** se o Maps Compose expõe `spans`; senão, `MapEffect`.
- **Muitos pontos:** mantenha todos no banco para as contas e **desenhe uma versão
  simplificada** (`PolyUtil.simplify`, da `android-maps-utils` **(confira)**). Calcule
  distância e pace **sobre os dados**, nunca sobre o que está desenhado.
- **Distância:** `Location.distanceBetween(...)` do Android serve entre dois pontos. Some só
  pontos de **boa precisão** e **dentro do mesmo trecho** (não some o salto da pausa).
- **Mapa de calor de corridas antigas:** a documentação lista **Heatmaps** na biblioteca de
  utilitários, mais barato que centenas de polilinhas. Não li a página: **(confira)**.
- **Privacidade ao compartilhar:** a rota mostra onde você mora. Antes de exportar imagem ou
  GPX, ofereça **cortar começo e fim** (como as zonas de privacidade do Strava). Ideia minha.
- **Teste sem mapa:** `toSegments()`, o enquadramento (0, 1 e 2 pontos) e o corte por pausa
  são funções puras; teste em unitário. O Maestro **não enxerga** a linha dentro do mapa.
- **Armadilhas:** `LatLngBounds` sem pontos lança exceção (por isso `all.size >= 2`), e
  `newLatLngBounds(bounds, padding)` só vale depois do layout.
- **Roads API (Snap to Roads)** poderia alisar traços ruins de GPS, mas é outra API (paga,
  com chave própria). Não li a página; não é necessária para o básico.

---

## 6. Tela de gravação (a mais delicada)

- **O mapa só mostra; a corrida mora no service.** O `GoogleMap` observa os pontos que o
  service grava (Flow/StateFlow). Se a Activity morrer, a corrida continua.
- **Não redesenhe milhares de pontos a cada fix.** Mantenha **todos** no banco e desenhe uma
  versão **simplificada** (Douglas-Peucker) ou atualizada a cada alguns segundos.
- **Ponto azul do sistema vs marcador do esporte.** O Larping troca o ponto azul por um
  marcador com o ícone do esporte. No Android, desligue `isMyLocationEnabled` durante a
  corrida e use um `Marker` na última coordenada. Ligue de volta antes de iniciar.
- **Criar ícone de marcador cedo demais derruba o app.** `BitmapDescriptorFactory` só
  funciona depois que o mapa foi inicializado **(confira)**; erro típico:
  "IBitmapDescriptorFactory is not initialized". Crie ícones depois de `onMapLoaded`.
- **Seguir o corredor sem brigar com ele:** anime a câmera para o último ponto, mas **pare de
  seguir quando o usuário arrasta o mapa** e mostre "recentralizar" (código na seção 5).
- **Tela apagada:** nada precisa ser desenhado; ao voltar, redesenhe do que foi gravado.
- **Corrida sem GPS** (meta por tempo): sem rota. Mostre estado vazio, não um mapa de oceano
  em (0, 0).

---

## 7. Tela de detalhe e histórico

- **Enquadrar a rota:** `LatLngBounds.Builder` + `CameraUpdateFactory.newLatLngBounds(bounds, padding)`.
  - **Verificado na documentação:** `newLatLngBounds(bounds, padding)` só pode ser usado
    **depois que o layout do mapa terminou**; antes disso use
    `newLatLngBounds(bounds, width, height, padding)`. Em Compose, espere `onMapLoaded` ou
    use a variante com dimensões.
  - `LatLngBounds` **sem nenhum ponto lança exceção** (`IllegalStateException`). Trate
    atividade vazia ou com 1 ponto (por exemplo, a importada do HealthKit sem rota).
- **Animação de desenho da rota.** O Larping descobriu que animar a polilinha a 60 fps não
  acompanhava (o MapKit não aplicava tantas atualizações) e foi para ~12 fps, com o marcador
  na frente. **Teste o mesmo no Maps Compose**, em aparelho de entrada, não só no emulador.
- **Heatmap de rotas antigas** (Larping: 500 mais recentes, polilinhas translúcidas
  empilhadas): custa muito com rotas longas. Simplifique, limite o número de rotas.
- **Miniaturas em lista: não coloque um `GoogleMap` por linha.** Opções:
  - **Modo Lite** (`GoogleMapOptions().liteMode(true)`). *(trecho oficial)* Polilinhas,
    polígonos, círculos, My Location e *padding* funcionam como no mapa completo. Limites:
    não dá para dar zoom nem arrastar; zoom arredondado para inteiro; `moveCamera` gera
    outra imagem; sem Street View; marcadores só em parte; estilo só por **JSON** (não na
    nuvem) e **sem** modo escuro nativo (`mapColorScheme`). Também **consome rede e conta
    como mapa**, então não é "de graça" numa lista longa.
  - **Ainda em aberto:** se o `GoogleMap` do Maps Compose aceita `liteMode` (talvez via um
    parâmetro de `GoogleMapOptions`, como `googleMapOptionsFactory`). **(confira na
    referência da sua versão.)**
  - **Alternativa sem rede e sem cota:** desenhar a rota num `Canvas` (seção 9).
- **Marcadores de início e fim:** poucos; muitos deixam o mapa lento.

---

## 8. Compartilhar (cartões e vídeo)

- O Larping usa `MKMapSnapshotter`. No Android, o equivalente é `GoogleMap.snapshot(callback)`
  **(confira)**, que devolve um `Bitmap`; chame via `MapEffect` **depois** de `onMapLoaded`,
  ou sai um bitmap cinza.
- **O snapshot não inclui os composables por cima.** Capture só o mapa e componha o cartão
  (rota, título, estatísticas, logo) em `Canvas`/`Bitmap`. Converta coordenadas com
  `map.projection.toScreenLocation`.
- **Vídeo animado:** busque o snapshot **uma vez** e renderize cada quadro em `Canvas`
  revelando a rota. Não use mapa vivo por quadro. Codifique com `MediaCodec`/`MediaMuxer`; é a
  peça mais trabalhosa, deixe para o fim.
- **Sem rede, o snapshot sai sem tiles.** Faça fallback em `Canvas` (fundo escuro com a rota).
- **Termos para imagens exportadas:** mantenha a atribuição/logo do Google visíveis. Antes de
  gerar cartões para redes sociais, **leia "License Restrictions" nos Termos do Google Maps
  Platform**; não recorte nem esconda o logo ou o aviso de direitos.

---

## 9. Offline, Termos de uso e quando não há Google

- **Sem rede:** a polilinha aparece sobre fundo cinza vazio. Os dados estão intactos. Diga
  isso na UI para não parecer bug.
- **Não faça cache de mapa.** Os Termos do Google proíbem pré-carregar, guardar ou indexar o
  conteúdo do mapa para uso fora do serviço (exceção: cache temporário de coordenadas por até
  30 dias). Inclui salvar tiles e guardar snapshots do mapa em banco. **Fonte:** li a cláusula
  num resumo de terceiros; o texto oficial é o dos Termos, confirme lá. Seus **pontos de
  GPS** são seus e podem ser guardados normalmente.
- **Sem Play Services:** verifique com `GoogleApiAvailability`, **mostre a rota em `Canvas`**
  sobre fundo liso (o SPRAWL//RUN faz isso com o "filamento neon" sobre uma grade) e use
  `LocationManager` para os pontos (seção 4). Serve também para miniaturas e cartões sem
  rede.
- **Privacidade:** ao pedir tiles, o Google recebe requisições do aparelho (IP, versão do
  app). Não use Places/Geocoding com dados que o usuário não escolheu. Atualize o texto que
  diz "nunca toca a rede".

---

## 10. Tema e aparência

- **Duas formas de escurecer o mapa, escolha uma:**
  1. **Estilo JSON local** (`MapStyleOptions`; em Compose, provavelmente
     `MapProperties(mapStyleOptions = ...)` **(confira)**), escolhido por `isSystemInDarkTheme()`.
     Não precisa de Map ID.
  2. **Esquema de cores nativo (`mapColorScheme`)**, novo nesta revisão. *(trecho oficial da
     página *Configure a map*)*: valores `light` (padrão), `dark` e `follow_system`, válido
     para mapa **normal e terreno**. Não li a página inteira nem sei como o Maps Compose
     expõe isso. **(confira)** Não funciona no modo Lite.
- **Verificado na documentação:** o estilo por JSON **só funciona no tipo `normal`** (não no
  satélite nem no híbrido). Com satélite, o tema escuro não vale.
- **Verificado na documentação:** **não combine** estilização na nuvem (Map ID) com estilo fixo
  no código. O JSON da nuvem **não é compatível** com o JSON embutido.
- Gere o JSON no *Styling Wizard* (mapstyle.withgoogle.com) e guarde em `res/raw`.
- A paleta do Larping (`#463CFF` no claro, `#CEFE06` no escuro) passa direto para
  `Polyline(color = ...)`. Verifique o contraste nos dois temas e no satélite.
- O botão de destaque tem rótulo escolhido por contraste (branco em `#463CFF`, preto em
  lima): no Compose, `contentColor` por tema.

---

## 11. Ordem sugerida para implementar

1. tela com `GoogleMap` e a chave funcionando (emulador com Google APIs);
2. polilinha fixa de teste, com 10 pontos escritos à mão;
3. rota vinda de um GPX de teste, para ver o desenho **sem sair de casa**;
4. trecho por pausa (`segmentId`) e marcadores A e B;
5. permissões + Location API + service em primeiro plano gravando no Room (traço ao vivo);
6. tela de detalhe com enquadramento;
7. só depois: cor por pace, simplificação, mapa de calor, miniaturas e compartilhar.

---

## 12. Testes com Maestro e Claude Code

- **O conteúdo do mapa não é acessível ao Maestro** (desenhado pelo SDK). Coloque
  `testTag`/`contentDescription` nos **controles em volta** (iniciar, pausar, recentralizar,
  finalizar) e valide o resultado pela UI ao redor (distância, tempo).
- **Marcadores e polilinhas não são nós da árvore de UI.** Teste pontos, simplificação,
  filtro de precisão, `segmentId` e enquadramento em testes unitários.
- Teste num **emulador com Play** e uma rota GPX, depois num aparelho real. Permissão de
  localização aproximada e negada também precisam de teste.
- **O Claude não vê o mapa.** Ele não julga se a rota ficou bonita nem se a câmera enquadrou
  bem; use screenshots seus ou capturas do Maestro.

---

## 13. Checklist de verificação

- [ ] chave restrita (pacote + SHA-1 de debug e de release, só Maps SDK for Android);
- [ ] chave fora do git e nenhuma cópia em chat, issue ou print;
- [ ] alerta de orçamento configurado no Cloud Console;
- [ ] emulador com Google Play; mapa carrega em debug e em release;
- [ ] a versão assinada pela Play mostra o mapa, não tela em branco;
- [ ] `INTERNET`/`ACCESS_NETWORK_STATE` declaradas e README/privacidade atualizados;
- [ ] permissão de localização: concedida, só aproximada e negada, sem crash;
- [ ] service em primeiro plano com notificação; atualizações removidas ao parar;
- [ ] sem rede: rota sobre fundo vazio, sem crash e com aviso;
- [ ] sem Play Services: fallback em `Canvas` e fonte de localização alternativa;
- [ ] pausa/retomada gera trecho novo (sem reta atravessando o buraco);
- [ ] atividade sem pontos ou com 1 ponto não derruba o detalhe;
- [ ] rota continua desenhada ao girar a tela e ao reabrir o app no meio da corrida;
- [ ] corrida longa: mapa fluido e bateria sob controle;
- [ ] cartão de compartilhar mostra a atribuição do Google;
- [ ] nenhum tile ou snapshot do mapa guardado em banco ou disco.

---

## 14. Páginas oficiais, pendências e o que mudou

Todas em `developers.google.com/maps/documentation/android-sdk/`.

**Lidas inteiras:** `views`, `styling`, `shapes`.

**Vistas só em trechos:** `maps-compose`, `location`, `lite`, `configure-map`, `get-api-key`,
`usage-and-billing`, quickstart (Secrets Gradle Plugin).

**Não lidas (as partes ligadas a elas continuam "(confira)"):** `polygon-tutorial` (app
oficial completo de polilinhas e polígonos), `events` (eventos de câmera, `onMapLoaded`),
Heatmaps (biblioteca de utilitários), `play-data-disclosure` (Segurança dos dados da Play),
Termos de Serviço (cloud.google.com/maps-platform/terms, "License Restrictions"), Roads API e
Routes API, referência do `android-maps-compose` (googlemaps.github.io/android-maps-compose).

A documentação também lista *Resources for AI → Agent skills* e *Code Assist toolkit*
(developers.google.com/maps/ai). Não abri; podem ajudar no fluxo com o Claude Code.

### Mudanças da 1.0 para a 2.0

- **Nova seção 4** ("De onde vêm os pontos"): a 1.0 dizia "o service grava os pontos" sem
  dizer de onde vinham.
- **A seção 12 da 1.0 virou a seção 5** e foi movida para perto do começo, com um resumo no
  topo; é o centro do app.
- **`RouteMap` corrigido:** marcadores com `rememberUpdatedMarkerState`; agora inclui o
  seguir o corredor com liberação ao arrastar.
- **Maps Compose:** template "Empty Compose Activity", `minSdk` 23, e aviso de que cópias
  antigas da documentação mostram versões 4.x, 6.x e 8.4.0.
- **Lite mode:** lista de suportado/não suportado confirmada em trecho oficial; o que
  continua em aberto é o suporte no Compose.
- **Tema:** acrescentado o `mapColorScheme` nativo e o fato de ele não valer no Lite.
- **Roteiro, permissões e testes:** permissões aproximada/precisa, foreground service e
  Location API entram no roteiro, no checklist e nos testes.
- **Rota "seguindo ruas":** registrado como fora do Maps SDK (decisão 5).
- **Corrigido:** a 1.0 dizia "as duas primeiras eu li inteiras" e listava três páginas.
