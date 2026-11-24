# Lições: mapa aberto no Android (MapLibre + OpenStreetMap), sem chave de API e com offline

Escrito em 17/11/2026, como par das *Lições 2.0 do Google Maps*. Serve para o mesmo app de
corrida: desenhar a rota que você correu, mostrar ruas e funcionar sem rede.

## Como ler as marcas de confiança

- **(lido)**: li a página inteira.
- **(trecho)**: vi só o trecho que a busca devolveu; a afirmação vale, mas não li o resto da página.
- **(confira)**: conhecimento geral meu, **não** li numa página. Valide antes de depender.
- **(minha ideia)**: decisão de projeto minha, não é da documentação.

Todo o código Kotlin abaixo é esboço **não compilado e não testado**. Cada seção termina com
as fontes. A tabela completa de links está na seção 15.

---

## 1. O modelo mental: por que "não vejo as ruas"

Um mapa aberto tem **peças separadas**; o Google Maps entrega tudo junto atrás de uma chave.

| Peça | O que faz | Quem fornece (opções) |
|---|---|---|
| **Renderizador** | Desenha o mapa na tela | MapLibre Native |
| **Estilo** (`style.json`) | Diz quais dados mostrar e com que cores | OpenFreeMap, Protomaps, ou o seu |
| **Tiles** | Os dados das ruas, em pedaços | Servidor público, ou arquivo local (PMTiles/MBTiles) |
| **Fontes e ícones** (*glyphs* e *sprites*) | Texto e símbolos do mapa | Mesmo provedor do estilo, ou cópia local |
| **Posição (GPS)** | De onde você está | Android (`LocationManager`) ou Play Services |
| **Sua rota** | A linha da corrida | Seu banco (Room) → GeoJSON → camada de linha |

O MapLibre **só desenha**. Sem um estilo apontando para tiles, aparece fundo liso com a sua
linha por cima. Foi o que você viu. *(trecho: a página do Compose diz que todo mapa exige
um estilo, "um JSON que descreve os dados que o mapa mostra e como desenha".)*

Fontes: [Style the map (MapLibre Compose)](https://maplibre.org/maplibre-compose/styling/) (lido).

---

## 2. Decisão central: de onde vêm os tiles

### 2.1 Os quatro caminhos

| # | Caminho | Chave? | Offline? | Quando usar |
|---|---|---|---|---|
| A | **OpenFreeMap** (servidor público) | Não | Pacotes pequenos, com limite | Começar rápido, app com rede |
| B | **Arquivo PMTiles no aparelho** (extraído do Protomaps) | Não | Sim, total | Offline de verdade, região grande |
| C | Provedor comercial (MapTiler, Stadia etc.) | Geralmente sim, do provedor | Depende dos termos | Se quiser SLA e suporte |
| D | Servidor seu (self-host) | Não | Você decide | Quando o app crescer |

Recomendação para o seu app: **comece no A** (ver ruas hoje) e **migre para o B** quando
quiser offline. O estilo e o código da rota não mudam; muda só a fonte dos tiles.

### 2.2 OpenFreeMap (caminho A) — regras verificadas

- **Gratuito, sem limite de visualizações ou requisições, sem cadastro, sem chave de API,
  sem cookies.** Uso comercial permitido. *(lido, página inicial)*
- **Atribuição obrigatória.** Com MapLibre ela é adicionada automaticamente. Fora do MapLibre
  (vídeo, imagem exportada), use: `OpenFreeMap © OpenMapTiles Data from OpenStreetMap`
  (a parte "OpenFreeMap" é opcional). *(lido)*
- **Sem SLA nem suporte** no momento. *(lido)* Não dependa dele para algo crítico sem plano B.
- **Offline permitido com limites:** cada download de **no máximo 3.000 tiles ou 50 MB**,
  **no máximo 4 requisições paralelas** e **User-Agent único** identificando o seu app. Para
  mais, contato por e-mail. *(lido, FAQ da página inicial; não li os Termos de Serviço
  inteiros, só o resumo)*
- **Estilo de exemplo:** `https://tiles.openfreemap.org/styles/liberty` *(lido; aparece na
  documentação do MapLibre Compose)*. Outros estilos citados na página: Positron, Bright,
  Dark, Fiord, 3D.
- **Downloads do planeta inteiro** semanais em MBTiles, Btrfs e PMTiles. *(lido)* O planeta
  tem dezenas de GB, então não serve para o aparelho; serve para você hospedar.

### 2.3 Servidor oficial do OpenStreetMap (`tile.openstreetmap.org`) — **não use no app**

Corrige o que eu disse antes no chat (sugeri o OSM público "para testar"). A política oficial
(trecho) diz:

- **Offline não é permitido** ("Download city/country for offline use" e "Save area for later"
  dependem de *prefetch*, que é proibido).
- A política manda, para offline, usar **tiles auto-hospedados** ou **provedor que permita
  explicitamente**.
- Os tiles vetoriais (`vector.openstreetmap.org`) têm política própria, e ela também **proíbe
  download em massa**.
- Uma versão antiga da política, citada num fórum, tratava distribuir um app que usa os tiles
  do OSM como "uso pesado" a pedir permissão. **Não confirmei o texto atual**; leia a política
  inteira antes de qualquer uso em app distribuído.

Em resumo: o **dado** do OSM é livre; o **servidor** deles não é para o seu app. Por isso
OpenFreeMap (A) ou arquivo próprio (B).

Fontes: [Tile Usage Policy](https://operations.osmfoundation.org/policies/tiles) (trecho),
[Vector Tile Usage Policy](https://operations.osmfoundation.org/policies/vector) (trecho),
[OpenFreeMap](https://openfreemap.org) (lido).

---

## 3. Dependências e ambiente

Há dois jeitos de usar o MapLibre no Android. Escolha um.

### 3.1 Opção 1: MapLibre Native (Views), embutido no Compose com `AndroidView`

```kotlin
dependencies {
    implementation("org.maplibre.gl:android-sdk:<versão>")
}
```

- O guia oficial usa `org.maplibre.gl:android-sdk` e manda ver a versão mais recente no
  Maven Central. O README do repositório mostrava **11.11.0** quando li. *(lido / trecho)*
- Os pacotes de import são `org.maplibre.android.*` a partir da 11.0; antes eram
  `com.mapbox.mapboxsdk.*`. Tutoriais antigos usam o nome velho. *(lido)*
- Precisa de `INTERNET` no manifesto para buscar tiles online. *(trecho, tutorial de terceiro;
  é a regra geral do Android)*
- É a opção **estável e documentada pela própria MapLibre** (guia oficial em Views). Para
  Compose, o repositório cita bibliotecas externas (Ramani Maps, MapLibre Compose
  Playground). *(trecho)* Para usar o `MapView` dentro do Compose, você mesmo liga o ciclo de
  vida (`onStart`, `onResume`, `onPause`, `onStop`, `onDestroy`) ao `MapView`. *(minha ideia
  de como embutir; o ciclo de vida em si é do guia oficial)*

### 3.2 Opção 2: MapLibre Compose (declarativo)

```kotlin
// libs.versions.toml
// maplibre-compose = { module = "org.maplibre.compose:maplibre-compose", version = "0.20.0" }

dependencies {
    implementation(libs.maplibre.compose)
    // um backend de renderização é obrigatório; escolha UM
    runtimeOnly("org.maplibre.compose:maplibre-compose-runtime-opengl-android:0.20.0")
    // opcional: localização fusionada com Play Services
    implementation("org.maplibre.compose:location-runtime-gms:0.20.0")
}
```

- **Versão 0.20.0** era a última quando li; **use a mesma versão** na biblioteca e em todo
  artefato de runtime. *(lido)*
- Runtimes Android: `...-runtime-opengl-android` e `...-runtime-vulkan-android`; ambos
  suportam `armeabi-v7a`, `arm64-v8a` e `x86_64`. *(lido)*
- **A documentação assume um projeto Compose Multiplatform** e mostra as dependências no
  *source set* `commonMain`/`androidMain`. Num projeto só Android, o bloco `dependencies {}`
  comum deve servir. **(confira)**
- **É versão 0.x, API ainda muda.** Prova: na revisão anterior eu vi coordenadas
  `dev.sargunv.maplibre-compose` na versão 0.6.0 em cópias antigas da documentação; hoje o
  grupo é `org.maplibre.compose`. Fixe a versão e leia o *changelog* a cada atualização.
  *(observação minha, a partir do que vi nas duas cópias)*
- Mapa mínimo: `MaplibreMap()` mostra o estilo *demotiles* por padrão (só países, sem ruas).
  *(lido)*

### 3.3 Qual escolher

- **Quer estabilidade e a documentação oficial da MapLibre:** Opção 1, com `AndroidView`.
- **Quer Compose "de verdade", com camadas, localização e offline como composables:** Opção 2,
  sabendo que é 0.x.
- O resto destas lições mostra **os dois** onde a documentação permite.

Fontes: [Getting started (MapLibre Native Android)](https://maplibre.org/maplibre-native/docs/book/android/getting-started-guide.html)
(lido), [repositório maplibre-native](https://github.com/maplibre/maplibre-native) (trecho),
[Getting started (MapLibre Compose)](https://maplibre.org/maplibre-compose/getting-started)
(lido).

---

## 4. Primeiro mapa com ruas (caminho A, online)

### Opção 1 (Views), do guia oficial

Os passos do guia: dependência, `MapView` no layout, `MapLibre.getInstance(this)` **antes** de
inflar o layout, e repasse do ciclo de vida ao `MapView`. O trecho que importa:

```kotlin
MapLibre.getInstance(this)
// ...
mapView.getMapAsync { map ->
    map.setStyle("https://tiles.openfreemap.org/styles/liberty")   // o guia usa demotiles
    map.cameraPosition = CameraPosition.Builder()
        .target(LatLng(-22.23, -53.34))   // lat, lng (exemplo: perto de Nova Andradina)
        .zoom(13.0)
        .build()
}
```

O guia oficial usa `https://demotiles.maplibre.org/style.json` (mundo, sem ruas); trocar pela
URL do OpenFreeMap é só mudar a *string*. *(lido)* Os métodos do ciclo de vida a repassar:
`onStart`, `onResume`, `onPause`, `onStop`, `onLowMemory`, `onDestroy`, `onSaveInstanceState`.

### Opção 2 (Compose)

```kotlin
@Composable
fun BaseMap() {
    val state = rememberMapState(
        baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty")
    )
    MaplibreMap(state = state)
}
```

*(lido; é o exemplo "Use a style" da documentação)* O estilo também pode trocar com o tema
do sistema: a página mostra `isSystemInDarkTheme()` escolhendo entre variantes `dark` e
`light` (no exemplo, de um provedor que usa chave).

### Se as ruas não aparecem (checklist)

1. A URL do estilo está certa e abre no navegador?
2. O aparelho tem rede e o manifesto tem `INTERNET`?
3. O zoom está perto o bastante? Ruas pequenas só aparecem em zoom alto. *(regra geral de
   mapas)*
4. A região que você olha tem dados? *(óbvio, mas acontece com extrato local)*
5. Se usa estilo próprio: `glyphs` e `sprite` apontam para algo que carrega? Sem isso somem
   rótulos e ícones, e às vezes o mapa inteiro falha. **(confira)**

Fontes: [Getting started (Native)](https://maplibre.org/maplibre-native/docs/book/android/getting-started-guide.html)
(lido), [Style the map (Compose)](https://maplibre.org/maplibre-compose/styling/) (lido),
[Getting started (Compose)](https://maplibre.org/maplibre-compose/getting-started) (lido).

---

## 5. Desenhar a rota (o essencial do app)

No MapLibre a linha **não é um objeto `Polyline`**, como no Google. Você cria uma **fonte de
dados GeoJSON** e uma **camada de linha** que desenha essa fonte. Para atualizar a rota, você
troca os dados da fonte. A documentação confirma esse modelo nos dois SDKs.

### 5.1 Armadilha clássica: ordem das coordenadas

- No **GeoJSON** e no `Point.fromLngLat(...)`, a ordem é **longitude, latitude**.
- No `LatLng(...)` do MapLibre e em quase todo código de GPS, é **latitude, longitude**.
- Inverter isso joga a rota no outro lado do planeta. *(o exemplo `Point.fromLngLat(17.3, 51.0)`
  do guia confirma a ordem do GeoJSON; o resto é regra geral)*

### 5.2 Um trecho por pausa: `MultiLineString`

Do Room você já tem trechos (`segmentId`, veja as Lições 2.0). Em GeoJSON, **um
`MultiLineString` representa todos os trechos numa só fonte e numa só camada**, e o buraco
entre trechos fica vazio (sem reta atravessando a pausa). *(minha ideia; o `MultiLineString`
é GeoJSON padrão)*

```kotlin
// Função pura: fácil de testar sem mapa
fun List<List<LatLng>>.toRouteGeoJson(): String {
    val lines = filter { it.size >= 2 }.joinToString(",") { seg ->
        seg.joinToString(",", "[", "]") { "[${it.longitude},${it.latitude}]" }   // lng, lat
    }
    return """{"type":"Feature","geometry":{"type":"MultiLineString","coordinates":[$lines]},"properties":{}}"""
}
```

### 5.3 Opção 1 (Views): fonte + camada de linha

Do guia de GeoJSON da MapLibre: criar `GeoJsonSource`, criar `LineLayer` apontando para o id
da fonte com `withProperties(...)`, adicionar ao `style`, e **atualizar depois com
`setGeoJson(...)`**.

```kotlin
map.getStyle { style ->
    style.addSource(GeoJsonSource("route-src"))

    // contorno escuro por baixo (minha ideia), linha clara por cima
    style.addLayer(
        LineLayer("route-casing", "route-src").withProperties(
            PropertyFactory.lineColor(Color.argb(115, 0, 0, 0)),
            PropertyFactory.lineWidth(8f),
            PropertyFactory.lineJoin("round"),
            PropertyFactory.lineCap("round"),
        )
    )
    style.addLayer(
        LineLayer("route-line", "route-src").withProperties(
            PropertyFactory.lineColor(Color.parseColor("#CEFE06")),
            PropertyFactory.lineWidth(5f),
            PropertyFactory.lineJoin("round"),
            PropertyFactory.lineCap("round"),
        )
    )
}

// a cada novo ponto (ou a cada alguns segundos):
fun updateRoute(style: Style, segments: List<List<LatLng>>) {
    style.getSourceAs<GeoJsonSource>("route-src")?.setGeoJson(segments.toRouteGeoJson())
}
```

- `GeoJsonSource`, `LineLayer`, `PropertyFactory.lineColor/lineWidth/lineJoin/lineCap` e
  `setGeoJson` aparecem nos exemplos oficiais. *(trecho)*
- `setGeoJson(String)` aceitar texto JSON, e `getSourceAs<...>()`: **(confira)** na API da
  sua versão. O guia mostra `setGeoJson(featureCollection)` e `style.addSource/addLayer`.
- A unidade de `lineWidth` e como ela escala com a densidade da tela: **(confira)** olhando em
  aparelho de verdade.
- **Trocar o estilo apaga as suas camadas.** Quando `setStyle` roda de novo (por exemplo,
  tema claro/escuro), você precisa adicionar fonte e camadas outra vez. **(confira)** No
  Compose isso é cuidado por você (5.4).

### 5.4 Opção 2 (Compose): camadas como composables

A documentação mostra exatamente o padrão com contorno + linha (casing), no bloco final de
`rememberMapState`:

```kotlin
val state = rememberMapState(
    baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty")
) {
    val route = rememberGeoJsonSource(GeoJsonData.Uri(/* uri do geojson */))   // ver nota

    LineLayer(
        id = "route-casing",
        source = route,
        color = const(Color.Black.copy(alpha = 0.45f)),
        width = const(8.dp),
        cap = const(LineCap.Round),
        join = const(LineJoin.Round),
    )
    LineLayer(
        id = "route-line",
        source = route,
        color = const(Color(0xFFCEFE06)),
        width = const(5.dp),
        cap = const(LineCap.Round),
        join = const(LineJoin.Round),
    )
}
MaplibreMap(state = state)
```

- **Nota importante:** a documentação diz que `rememberGeoJsonSource` "cria uma fonte a
  partir de **um URI ou de uma string GeoJSON**" *(lido)*, mas o exemplo mostra só a variante
  `GeoJsonData.Uri(...)`. Para a rota **ao vivo** você precisa da variante com *string*;
  **(confira) o nome exato** (algo como `GeoJsonData.JsonString`) na referência da API:
  https://maplibre.org/maplibre-compose/api/
- Propriedades de camada aceitam **expressões**; um valor fixo vai em `const(...)`. A largura
  pode variar com o zoom via `interpolate(... zoom() ...)`. *(lido)*
- **Posição na pilha de camadas:** por padrão suas camadas ficam **acima** do estilo-base.
  `Anchor.Below({ it.type == "symbol" }) { ... }` coloca a rota **abaixo dos rótulos**, sem
  precisar saber o nome das camadas do estilo. *(lido)* Para rota de corrida, deixar a linha
  abaixo dos nomes de rua costuma ficar mais limpo. *(minha opinião)*
- Cor, tema escuro: a documentação usa `isSystemInDarkTheme()` direto na cor de uma camada
  (`CircleLayer`), com `colorTransition` para animar. *(lido)*

### 5.5 Marcadores A e B e a posição atual

- Na documentação do Compose, pontos são desenhados com `CircleLayer` sobre uma fonte GeoJSON
  de pontos. *(lido)* Para A (início) e B (fim), duas feições de ponto numa fonte, com
  `CircleLayer` (e, se quiser ícone, o guia de imagens: https://maplibre.org/maplibre-compose/images/
  **(não li)**).
- A posição atual do corredor: use a camada de localização (seção 6.3) ou uma
  `CircleLayer` movida por você. *(minha ideia)*

### 5.6 Seguir o corredor e enquadrar a rota

- **Compose:** controle de câmera tem página própria
  (https://maplibre.org/maplibre-compose/camera/), **não li**. O exemplo de localização usa
  `mapState.animateCamera(CameraUpdate(center = ..., zoom = 15.0))`. *(lido)*
- **Native:** `LatLngBounds` + `CameraUpdateFactory.newLatLngBounds(...)`, API herdada do
  Mapbox. **(confira)** Mesmo cuidado das Lições 2.0: `LatLngBounds` sem pontos lança
  exceção; trate 0 e 1 ponto.

### 5.7 Desempenho

- Uma fonte com a rota inteira regravada a cada fix pesa em corrida longa. Mantenha **todos**
  os pontos no banco, mas gere o GeoJSON de uma versão **simplificada** (Douglas-Peucker), ou
  atualize a cada alguns segundos. *(minha ideia; mesma lição do Google)*
- Teste em **aparelho de entrada**, não só no emulador.

Fontes: [Add data to the map (Compose)](https://maplibre.org/maplibre-compose/layers/) (lido),
[GeoJSON guide (Native)](https://maplibre.org/maplibre-native/android/examples/geojson-guide/)
(trecho), [Show the user's location](https://maplibre.org/maplibre-compose/location/) (lido).

---

## 6. Localização (GPS) sem Google

### 6.1 O mapa não te dá os pontos

Igual ao Google: o mapa só **mostra**. Quem entrega a posição é a API de localização do
Android. Você precisa de duas coisas separadas: **(a)** o ponto azul na tela (opcional) e
**(b)** os pontos que o *service* grava na corrida (obrigatório).

### 6.2 Escolhendo o provedor de localização

| Provedor | Precisa de Play Services? | Observação |
|---|---|---|
| `LocationManager` + `GPS_PROVIDER` | **Não** | GPS direto do aparelho; permissão `ACCESS_FINE_LOCATION` |
| `LocationManager` + `FUSED_PROVIDER` | Não (Android 12+) | *(trecho)* "Na API 31+, o `FUSED_PROVIDER`, antes só pelos Play Services, está disponível como provedor do `LocationManager`" |
| `FusedLocationProviderClient` (Play Services) | **Sim** | Mais simples; a página do Android diz que muitos apps o preferem porque ele escolhe sozinho o melhor provedor por potência e precisão |

- A fonte desses fatos é uma página do Android sobre **Android Automotive**, mas as frases
  sobre `FUSED_PROVIDER` e `FusedLocationProviderClient` são gerais. Se o `FUSED_PROVIDER`
  existe em todos os aparelhos sem Google na API 31+: **(confira)**.
- **Para corrida sem Google:** `LocationManager` com `GPS_PROVIDER` é o caminho garantido.

```kotlin
// esboço meu, não testado
val lm = getSystemService(LocationManager::class.java)

@SuppressLint("MissingPermission")   // só chame com a permissão concedida
fun startTracking(listener: LocationListener) {
    lm.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        2_000L,      // intervalo mínimo em ms
        3f,          // distância mínima em metros
        listener,
        Looper.getMainLooper(),
    )
}
fun stopTracking(listener: LocationListener) = lm.removeUpdates(listener)
```

`requestLocationUpdates(provider, minTime, minDistance, listener, looper)` e `removeUpdates`:
**(confira)** assinatura e comportamento na documentação do `LocationManager`; não li essa
página.

### 6.3 Ponto azul pelo MapLibre Compose (opcional)

O módulo de localização do MapLibre Compose faz o ponto e a câmera seguirem a posição:

```kotlin
val locationProvider = rememberDefaultLocationProvider()
val locationState = rememberLocationState(provider = locationProvider)

val mapState = rememberMapState {
    val mapState = checkNotNull(LocalMapState.current)
    LocationIndicatorLayer(id = "user", locationState = locationState)
    LocationTrackingEffect(locationState = locationState) {
        mapState.animateCamera(CameraUpdate(center = currentLocation.position, zoom = 15.0))
    }
}
MaplibreMap(state = mapState)
```

*(lido)* Fatos da documentação:
- Todas as plataformas têm provedor padrão. Com o módulo opcional `location-runtime-gms`, o
  provedor padrão usa localização fusionada **onde há Play Services** e os provedores do
  *framework* Android **onde não há**. *(lido)* Ou seja, o fallback sem Google já vem pronto.
- Declare `ACCESS_COARSE_LOCATION` e `ACCESS_FINE_LOCATION` no manifesto; declare
  `ACCESS_BACKGROUND_LOCATION` **só** se o app rastrear em segundo plano. *(lido)*
- **A biblioteca nunca pede a permissão sozinha.** Chame `locationState.requestPermission()`
  quando o app estiver pronto para mostrar o pedido. Se a permissão estiver negada, o estado
  informa o próximo passo: explicar antes (`shouldShowRationale`, só Android), pedir de novo
  (`canRequest`) ou mandar o usuário às configurações (`rememberSystemSettingsLauncher`).
  *(lido)*
- `locationState.lastLocation` é nulo até a primeira medição; o ponto não desenha antes disso.
  *(lido)*
- **Atenção:** esse módulo alimenta o **ponto na tela**. Os pontos da corrida continuam sendo
  gravados pelo *service* (6.4), com a sua própria assinatura de localização. Dois assinantes
  ao mesmo tempo consomem mais bateria. *(minha observação)*

### 6.4 Service em primeiro plano (para gravar com a tela apagada)

Verificado na documentação do Android *(trecho)*:

- A partir do **Android 14 (API 34)** você **deve declarar o tipo** de cada serviço em
  primeiro plano **no manifesto** e pedir a permissão correspondente, além de
  `FOREGROUND_SERVICE`. Sem tipo, `startForeground()` lança
  `MissingForegroundServiceTypeException`.
- Para localização: `android:foregroundServiceType="location"`, permissão
  `FOREGROUND_SERVICE_LOCATION`, e `FOREGROUND_SERVICE_TYPE_LOCATION` em `startForeground()`.
- Pré-requisito de execução: você precisa **já ter** `ACCESS_FINE_LOCATION` ou
  `ACCESS_COARSE_LOCATION` **concedida antes** de iniciar o serviço. A ordem importa: peça a
  permissão, depois inicie o service.
- O exemplo da própria documentação de tipos descreve um app de corrida que usa o tipo
  `location`.
- `ServiceCompat.startForeground(...)` com tipos exige androidx-core 1.12 ou superior.
- No Android 13+, a permissão de notificações (`POST_NOTIFICATIONS`) é pedida em tempo de
  execução para a notificação fixa. **(confira)**

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
<uses-permission android:name="android.permission.INTERNET"/>

<service
    android:name=".RunService"
    android:foregroundServiceType="location"
    android:exported="false"/>
```

### 6.5 Regras de gravação (valem para qualquer mapa)

Mantive as da Lição 2.0: descartar fixes com `accuracy` ruim, gravar no Room na hora,
incrementar `segmentId` a cada pausa ou perda de sinal, e parar as atualizações ao terminar.
O limite de precisão (por exemplo 25 m) é um chute: ajuste olhando traços reais.

Fontes: [Foreground service types (Android)](https://developer.android.com/develop/background-work/services/fg-service-types)
(trecho), [Foreground service types are required (Android 14)](https://developer.android.com/about/versions/14/changes/fgs-types-required)
(trecho), [Coarse location / FUSED_PROVIDER](https://developer.android.com/training/cars/apps/coarse-location)
(trecho), [Show the user's location (MapLibre Compose)](https://maplibre.org/maplibre-compose/location/)
(lido).

---

## 7. Offline, caminho A: pacotes baixados do servidor (OpenFreeMap)

**O que é:** o MapLibre baixa **os tiles, o estilo e os outros recursos** de uma região e
guarda no aparelho. Depois, o mapa os usa **sozinho quando não há rede**, sem configurar nada.
*(lido, MapLibre Compose)*

### 7.1 No Compose

```kotlin
val offlineStorage = DefaultMapRuntime.instance.offlineStorage
val offlineState by offlineStorage.state.collectAsState()

Button(
    enabled = offlineState is OfflineStorageState.Ready,
    onClick = {
        scope.launch {
            val pack = offlineStorage.create(
                definition = OfflinePackDefinition.TilePyramid(
                    styleUrl = "https://tiles.openfreemap.org/styles/liberty",
                    bounds = BoundingBox(west = -53.5, south = -22.4, east = -53.2, north = -22.1), // exemplo
                    pixelRatio = pixelRatio,
                    minZoom = 10.0,
                    maxZoom = 14.0,
                ),
                metadata = "Minha cidade".encodeToByteArray(),
            )
            offlineStorage.resume(pack)      // o pacote nasce pausado
        }
    },
) { Text("Baixar região") }
```

*(lido; é o exemplo da documentação com Seattle trocado por coordenadas de exemplo.)* O que
a página diz:

- Um pacote é definido por **URL de estilo, caixa (bounding box) e faixa de zoom**.
  `OfflinePackDefinition.Shape` baixa a região que cobre uma geometria GeoJSON em vez de uma
  caixa.
- `create` registra o pacote **pausado**; `resume` inicia; `pause` pausa.
- O progresso vem em `StateFlow`: `OfflineStorage.state` e `OfflinePack.downloadProgress`
  (estados `Healthy`, `Error`, `TileLimitExceeded`, `NotReported`). Um *worker* em segundo
  plano pode coletar o progresso até `DownloadStatus.Complete`.
- `invalidate` reconfere os tiles no servidor e atualiza os que mudaram; `delete` remove e
  libera o que nenhum outro pacote usa.
- Os `metadata` são bytes livres que a biblioteca não interpreta; use para guardar o nome
  da região.
- **Não existe API offline no navegador.** *(irrelevante para Android)*

### 7.2 Limites que você precisa respeitar

1. **OpenFreeMap:** **≤ 3.000 tiles ou ≤ 50 MB por download, ≤ 4 requisições paralelas,
   User-Agent único do seu app.** *(lido)* Como mudar o User-Agent e limitar a concorrência
   no MapLibre: a página de requisições do Compose mostra um `MapRequestInterceptor` que
   adiciona **cabeçalhos HTTP** (https://maplibre.org/maplibre-compose/requests/, lido), então
   o User-Agent pode ser definido ali. **(confira)** se o interceptor consegue sobrescrever o
   `User-Agent` e se há controle de requisições paralelas.
2. **MapLibre Native:** o limite padrão de tiles baixados e guardados no aparelho é **6.000**
   (`OfflineManager.setOfflineMapboxTileCountLimit`). *(trecho)* O nome do método é herdado do
   Mapbox, mas ele consta na API da MapLibre. Estourar o limite dispara
   `mapboxTileCountLimitExceeded` até você apagar pacotes.
3. **Estimativa minha, a conferir:** cada tile de zoom 14 cobre cerca de 2,4 km de lado perto
   do equador. Uma cidade em zoom 10 a 14 costuma caber **folgada** nos 3.000 tiles; um
   **estado inteiro, não**. Para regiões grandes, o caminho B.

### 7.3 No Native (Views)

A API é `OfflineManager`, com `createOfflineRegion`, `OfflineTilePyramidRegionDefinition` e
`OfflineGeometryRegionDefinition`. **Não li o guia de offline do MapLibre Native**; os nomes
das classes aparecem na lista de classes da API (`org.maplibre.android.offline`). *(trecho)*
Consulte a API: https://maplibre.org/maplibre-native/android/api/

### 7.4 Quando NÃO usar o caminho A

- Quando precisar de **muita área** (estado, país).
- Quando não quiser depender do servidor de um terceiro sem SLA.
- **PMTiles não suporta pacotes offline nem cache**. *(trecho)* Se você usar o caminho B, ele
  já é offline por ser arquivo local; não misture com pacotes.

Fontes: [Download maps for offline use (Compose)](https://maplibre.org/maplibre-compose/offline/)
(lido), [OpenFreeMap, FAQ](https://openfreemap.org) (lido),
[setOfflineMapboxTileCountLimit](https://maplibre.org/maplibre-native/android/api/-map-libre%20-native%20-android/org.maplibre.android.offline/-offline-manager/set-offline-mapbox-tile-count-limit.html)
(trecho), [Rewrite or serve map requests](https://maplibre.org/maplibre-compose/requests/) (lido).

---

## 8. Offline, caminho B: arquivo PMTiles no aparelho (sem servidor)

É o que você descreveu como "GPS de carro": o mapa **já está dentro do aparelho**. Fica assim:

```
Protomaps (planeta) --pmtiles extract--> regiao.pmtiles --> aparelho --> MapLibre desenha
```

### 8.1 Conseguir o arquivo da sua região

1. Baixe a ferramenta de linha de comando `pmtiles` (binário único; releases em
   https://github.com/protomaps/go-pmtiles/releases).
2. Escolha uma **build diária** em https://maps.protomaps.com/builds.
3. Descubra a caixa da sua região (por exemplo em http://bboxfinder.com, que a própria
   documentação do Protomaps cita) no formato `oeste,sul,leste,norte`.
4. Extraia:

```bash
pmtiles extract https://build.protomaps.com/<DATA>.pmtiles regiao.pmtiles \
  --bbox=OESTE,SUL,LESTE,NORTE --maxzoom=14
```

Fatos *(lidos/trecho)*:
- O comando `extract` baixa **só os tiles da caixa**, e aceita também `--region` com um
  GeoJSON e `--maxzoom` para limitar o zoom.
- O planeta inteiro tem **cerca de 120 GB** *(trecho de blog de terceiro)*: **não** baixe o
  arquivo todo, use `extract`.
- Exemplo de ordem de grandeza: a caixa de **Berlim** deu um arquivo de **84 MB**
  *(trecho, tutorial de terceiro)*. Um **zoom máximo menor** reduz bastante o tamanho: o
  exemplo do Protomaps mostra o planeta até zoom 6 com cerca de 60 MB. O tamanho da **sua**
  região eu **não sei**; o comando imprime o tamanho no final. Meça.
- As *builds* diárias ficam guardadas: todas da última semana, as de segunda-feira do último
  mês, e a última de cada versão depois disso. Use uma data recente. *(trecho)*
- A documentação do Protomaps traz hashes BLAKE3 para verificar a integridade do arquivo.
  *(trecho)*

### 8.2 Colocar o arquivo no aparelho

- **O arquivo precisa estar em disco, não nos `assets` do app.** A documentação do MapLibre
  Native diz que `pmtiles://asset://` **não é suportado** (o leitor de assets não faz leitura
  por intervalo de bytes, de que o PMTiles precisa). Use `pmtiles://file://` com caminho
  absoluto, por exemplo em `filesDir` ou `getExternalFilesDir()`. *(trecho)*
- Se o arquivo for pequeno, você pode empacotá-lo nos assets e **copiar para `filesDir` na
  primeira execução**. **(confira)** o limite de tamanho do APK/AAB. Para regiões maiores,
  **baixe depois da instalação** (com `WorkManager`, por exemplo). *(minha ideia)*
- O suporte a PMTiles no Android exige **MapLibre Android 11.7.0 ou superior**. *(trecho)*

### 8.3 Apontar o MapLibre para o arquivo

Do exemplo oficial (fonte raster; para o Protomaps use o vetor, mesma ideia):

```kotlin
val path = filesDir.absolutePath + "/regiao.pmtiles"
val source = VectorSource("protomaps", "pmtiles://file://$path")
style.addSource(source)
```

*(o exemplo oficial usa `RasterSource` com `pmtiles://file://$path`; a versão `VectorSource` é
adaptação minha, **(confira)**)* Regras do exemplo:

- O prefixo `pmtiles://` funciona com **qualquer tipo de fonte** (vetorial, raster, etc.), e
  numa linha de estilo ou criado em código.
- `pmtiles://https://...` lê um arquivo **remoto** por requisições de intervalo; `pmtiles://file://...`
  lê do disco.
- A URL dentro de `pmtiles://` tem de estar **completa e absoluta**. Relativa não funciona
  no Native.

### 8.4 O estilo local (a parte trabalhosa)

Sem rede, **o estilo também tem de ser local** e referenciar os tiles do arquivo. Peças:

1. **Estilo do Protomaps.** O repositório `protomaps/basemaps` tem um pacote TypeScript que
   gera estilos MapLibre em vários temas e pode **exportá-los como JSON**. *(trecho)* O
   estilo usa o **esquema próprio** do Protomaps; um estilo de outro esquema (como o do
   OpenFreeMap, que usa OpenMapTiles) **não** funciona com esse arquivo. *(trecho: "as
   source-layers dependem do esquema do PMTiles; o Protomaps usa o seu")*
2. **Fontes de texto (*glyphs*) e ícones (*sprites*).** O estilo referencia essas duas
   coisas, e elas **também precisam estar locais**. A documentação do Protomaps diz que os
   arquivos podem ser baixados como ZIP no repositório `basemaps-assets`, "se você precisar
   hospedá-los você mesmo, ou offline". As fontes são para os rótulos de texto; os *sprites*
   são para nomes de cidade, escudos de rodovia e ícones de pontos de interesse. *(trecho)*
3. **Caminhos absolutos em tempo de execução.** O caminho de `filesDir` só é conhecido
   quando o app roda. Padrão: guarde o estilo em `assets/style.json` com *placeholders*,
   troque pelo caminho real e carregue o JSON. *(minha ideia)*

```json
{
  "version": 8,
  "glyphs": "file://__FILES_DIR__/glyphs/{fontstack}/{range}.pbf",
  "sprite": "file://__FILES_DIR__/sprites/v4/light",
  "sources": {
    "protomaps": {
      "type": "vector",
      "url": "pmtiles://file://__FILES_DIR__/regiao.pmtiles",
      "attribution": "<a href=\"https://github.com/protomaps/basemaps\">Protomaps</a> © <a href=\"https://openstreetmap.org\">OpenStreetMap</a>"
    }
  },
  "layers": [ /* gerado pelo pacote de estilos do Protomaps */ ]
}
```

```kotlin
val json = assets.open("style.json").bufferedReader().readText()
    .replace("__FILES_DIR__", filesDir.absolutePath)
map.setStyle(Style.Builder().fromJson(json))
```

- O formato do `glyphs` (`.../fonts/{fontstack}/{range}.pbf`) e o do `sprite`
  (`.../sprites/v4/light`) são os da documentação do Protomaps. *(trecho)* O estilo pede
  fontes por nome (exemplo da doc: *Noto Sans Regular*); **a pasta local precisa ter as
  mesmas fontes que o estilo usa**.
- **`file://` para glyphs/sprites:** não achei documentação oficial do Native Android sobre
  isso. Um *issue* do wrapper Flutter relata que `asset://` falhava e `file://` funcionava,
  e um pacote Flutter descreve a mesma regra ("copie para o disco e use `file://`").
  *(fontes de terceiros, de outra plataforma)* **(confira)** no Android nativo antes de
  contar com isso. Um exemplo de iOS usa `asset://` para tiles, sprite e glyphs, o que
  mostra que o esquema existe no motor, mas pode variar por plataforma.
- `Style.Builder().fromJson(...)`: **(confira)** na API da sua versão. A documentação do
  wrapper Flutter menciona que no Android dá para passar o JSON como string, mas não
  recomenda para produção.
- Existe também o esquema **`mbtiles://`** no motor nativo (Android e iOS) para arquivos
  MBTiles. *(trecho, página do wrapper Flutter que diz que ele é implementado no MapLibre
  Native)* **(confira)** na documentação do Native Android.

### 8.5 Licença e atribuição do arquivo Protomaps

- Os tilesets do Protomaps são "obras produzidas" do OpenStreetMap sob a **ODbL**.
  **Apps nativos que os usam devem mostrar visivelmente "© OpenStreetMap"**, por exemplo no
  canto do mapa. *(trecho do README do repositório `protomaps/basemaps`, que li numa cópia
  indexada e num fork)*
- A atribuição sugerida: `Protomaps © OpenStreetMap` (a parte "Protomaps" é um pedido, não
  obrigação). *(trecho)*
- O código dos estilos é BSD-3 e o desenho do mapa é CC0. *(trecho)*
- Se publicar uma versão modificada dos estilos ou dos tiles como produto, o nome tem de ser
  diferente de "Protomaps". *(trecho)*
- Como o `attribution` fica na fonte do estilo (exemplo acima), o MapLibre deve exibi-lo
  sozinho. **(confira)** se aparece no seu app; se não aparecer, desenhe você mesmo.

### 8.6 Atualização dos dados

- O arquivo é uma **foto** do OSM no dia da build. Ruas novas só aparecem quando você baixar
  outro. Planeje: botão "atualizar mapa", ou checagem periódica com `WorkManager`. *(minha
  ideia)*

Fontes: [PMTiles no MapLibre Native Android](https://maplibre.org/maplibre-native/android/examples/data/PMTiles)
(trecho), [Protomaps: Getting started](https://docs.protomaps.com/guide/getting-started)
(trecho), [Protomaps: Basemap downloads](https://docs.protomaps.com/basemaps/downloads)
(trecho), [Protomaps: Basemaps for MapLibre](https://docs.protomaps.com/basemaps/maplibre)
(trecho), [protomaps/basemaps](https://github.com/protomaps/basemaps) (trecho, cópia indexada).

---

## 9. Caminho avançado: servir os tiles pelo seu código

A documentação do MapLibre Compose tem um gancho para isso, útil se quiser ler tiles de um
formato próprio (por exemplo MBTiles) e entregá-los ao mapa:

- `MapRequestInterceptor` **reescreve URLs** de tiles, sprites e fontes ou adiciona cabeçalhos
  HTTP.
- `MapResourceProvider(scheme = "app") { request -> ... }` **fornece os bytes** para URLs que
  ele aceita. Retorna `MapResourceLoad.Bytes`, `NoContent` (tile vazio) ou `Failed`.
- Configure uma vez, no início do app, com `DefaultMapRuntime.configure { ... }`, **antes** do
  primeiro mapa, *snapshotter* ou armazenamento offline. Fica fixo pela vida do *runtime*.
- Anexe credenciais **só** a hosts seus.

*(lido)* Isso é uma saída se `file://` para glyphs/sprites não funcionar no seu aparelho: dá
para servir fontes e ícones por um esquema próprio (`app://...`). **(minha ideia, não
testada)** Fonte: https://maplibre.org/maplibre-compose/requests/

---

## 10. Rotas "seguindo ruas" (fora do escopo do mapa)

Igual ao Google: o MapLibre **só desenha**; não calcula rotas. Para planejar caminho até um
destino é preciso um **motor de rotas** (por exemplo OSRM, GraphHopper, Valhalla).
**Não li a documentação de nenhum deles nesta revisão**; trate o que eu disse no chat (que o
GraphHopper roda no aparelho) como **(confira)**. Para mostrar a rota que **você correu**,
não precisa de nada disso.

---

## 11. Qualidade do traço e bugs prováveis

Tudo desta seção é regra geral ou ideia minha, não de uma página que li.

- **Posição:** vem do GPS, não do mapa. Trocar de Google para MapLibre **não** piora a
  precisão. O que muda é o provedor de localização (seção 6.2). Compare em aparelho real.
- **Linha fora da rua:** o GPS erra alguns metros e o mapa tem sua própria imprecisão. Ocorre
  também no Google.
- **Traço serrilhado:** filtre por `accuracy` e simplifique.
- **Bugs de integração mais prováveis nesta pilha:**
  - lat/lng invertidos (seção 5.1);
  - estilo carregado sem `glyphs`/`sprite` acessíveis (seção 8.4);
  - tiles que não existem no zoom ou na região pedida;
  - camadas somem ao trocar o estilo (seção 5.3);
  - `pmtiles://asset://` (não suportado), `pmtiles://` relativo (não suportado).
- **Map matching ("grudar na rua")** exige um serviço de *map matching* (por exemplo
  Valhalla ou OSRM). **(confira)** Em corrida por parque ou trilha, grudar na rua pode
  atrapalhar.
- **Teste sem mapa:** `toRouteGeoJson()`, `toSegments()`, o filtro de precisão e o corte por
  pausa são funções puras. Teste em unitário. O Maestro não enxerga o conteúdo do mapa.

---

## 12. Compartilhar (cartão e vídeo)

- O MapLibre Compose tem um **snapshotter** ("Capture a map image",
  https://maplibre.org/maplibre-compose/snapshotter/). **Não li a página.** Os módulos de
  *requests* e *offline* mencionam que a configuração se aplica também aos *snapshotters*.
  *(lido)*
- **Sem tiles, a imagem sai sem ruas.** Mantenha o fallback em `Canvas`: fundo escuro com a
  rota desenhada.
- **Atribuição em imagem exportada:** o OpenFreeMap exige o texto de atribuição **fora do
  MapLibre** (imagem, vídeo, impressão). Em arquivo Protomaps, `© OpenStreetMap` visível.
  *(lido/trecho)* Inclua na imagem do cartão.
- Zonas de privacidade (cortar começo e fim antes de exportar): ideia minha, igual à lição do
  Google.

---

## 13. Checklist de verificação

- [ ] mapa mostra ruas online com o estilo escolhido (caminho A) em debug e release;
- [ ] `INTERNET` no manifesto; atribuição visível (automática no MapLibre com OpenFreeMap);
- [ ] rota desenhada com lng/lat na ordem certa (teste com uma rota de GPX conhecida);
- [ ] pausa/retomada gera trecho novo, sem reta atravessando o buraco;
- [ ] rota continua desenhada ao girar a tela e ao reabrir o app no meio da corrida;
- [ ] trocar tema/estilo não perde as camadas da rota;
- [ ] permissão de localização: concedida, só aproximada e negada, sem crash;
- [ ] service em primeiro plano com tipo `location`, `FOREGROUND_SERVICE_LOCATION` e a
      permissão concedida **antes** de iniciar (Android 14+);
- [ ] sem Play Services: localização continua funcionando (`LocationManager`);
- [ ] offline A: pacote respeita 3.000 tiles/50 MB e User-Agent único (OpenFreeMap);
- [ ] offline B: PMTiles em `filesDir`, **não** em `assets`; mapa abre em modo avião;
- [ ] glyphs e sprites locais carregam em modo avião (texto e ícones aparecem);
- [ ] atribuição `© OpenStreetMap` visível no mapa **e** na imagem exportada;
- [ ] não há uso de `tile.openstreetmap.org` no app;
- [ ] corrida longa: mapa fluido e bateria sob controle.

---

## 14. Ordem sugerida para implementar

1. mapa online com ruas (OpenFreeMap + MapLibre), caminho A;
2. rota fixa de teste (10 pontos) com `LineLayer`;
3. rota de um GPX de teste, sem sair de casa;
4. trechos por pausa (`MultiLineString`) e marcadores A e B;
5. permissões + `LocationManager` + service em primeiro plano + Room (traço ao vivo);
6. tela de detalhe com enquadramento;
7. offline: primeiro pacotes pequenos do caminho A **ou** direto o caminho B (PMTiles);
8. só depois: cor por pace, simplificação, compartilhar.

---

## 15. Tabela de fontes (e o quanto eu li)

| Assunto | Link | Nível |
|---|---|---|
| MapLibre Native, início (Android) | https://maplibre.org/maplibre-native/docs/book/android/getting-started-guide.html | lido |
| MapLibre Native, repositório e versão | https://github.com/maplibre/maplibre-native | trecho |
| MapLibre Native, GeoJSON (fonte + camada + `setGeoJson`) | https://maplibre.org/maplibre-native/android/examples/geojson-guide/ | trecho |
| MapLibre Native, PMTiles | https://maplibre.org/maplibre-native/android/examples/data/PMTiles | trecho |
| MapLibre Native, limite de tiles offline | https://maplibre.org/maplibre-native/android/api/-map-libre%20-native%20-android/org.maplibre.android.offline/-offline-manager/set-offline-mapbox-tile-count-limit.html | trecho |
| MapLibre Native, referência da API | https://maplibre.org/maplibre-native/android/api/ | não li |
| MapLibre Compose, início e dependências | https://maplibre.org/maplibre-compose/getting-started | lido |
| MapLibre Compose, estilos | https://maplibre.org/maplibre-compose/styling/ | lido |
| MapLibre Compose, fontes e camadas | https://maplibre.org/maplibre-compose/layers/ | lido |
| MapLibre Compose, localização | https://maplibre.org/maplibre-compose/location/ | lido |
| MapLibre Compose, offline | https://maplibre.org/maplibre-compose/offline/ | lido |
| MapLibre Compose, requisições e provedor de bytes | https://maplibre.org/maplibre-compose/requests/ | lido |
| MapLibre Compose, câmera / imagens / snapshotter / API | https://maplibre.org/maplibre-compose/camera/ · /images/ · /snapshotter/ · /api/ | não li |
| MBTiles no motor nativo (via wrapper Flutter) | https://maplibre.org/flutter-maplibre-gl/advanced/mbtiles/ | trecho, de outra plataforma |
| `file://` para glyphs/sprites (relato) | https://github.com/maplibre/flutter-maplibre-gl/issues/338 | trecho, de outra plataforma |
| OpenFreeMap (FAQ, atribuição, offline) | https://openfreemap.org | lido |
| OpenFreeMap, termos | https://openfreemap.org/tos/ | trecho |
| OpenFreeMap, repositório | https://github.com/hyperknot/openfreemap | trecho |
| OSM, política de tiles raster | https://operations.osmfoundation.org/policies/tiles | trecho |
| OSM, política de tiles vetoriais | https://operations.osmfoundation.org/policies/vector | trecho |
| Protomaps, primeiros passos (`extract`) | https://docs.protomaps.com/guide/getting-started | trecho |
| Protomaps, downloads das builds | https://docs.protomaps.com/basemaps/downloads | trecho |
| Protomaps, estilos, glyphs e sprites | https://docs.protomaps.com/basemaps/maplibre | trecho |
| Protomaps, licença e atribuição | https://github.com/protomaps/basemaps | trecho (cópia indexada e fork) |
| Protomaps, builds | https://maps.protomaps.com/builds | não li |
| Android, tipos de serviço em primeiro plano | https://developer.android.com/develop/background-work/services/fg-service-types | trecho |
| Android 14, tipos obrigatórios | https://developer.android.com/about/versions/14/changes/fgs-types-required | trecho |
| Android, `FUSED_PROVIDER` e `LocationManager` | https://developer.android.com/training/cars/apps/coarse-location | trecho |
| Android, `LocationManager` (referência) | https://developer.android.com/reference/android/location/LocationManager | não li |

---

## 16. Correções ao que eu disse antes no chat

- **OSM público para testar:** eu sugeri `osmdroid` com os tiles do OpenStreetMap "para testes
  pequenos". A política oficial proíbe offline e download em massa nesse servidor, e não
  confirmei o que ela diz hoje sobre distribuir um app. Use OpenFreeMap. Também **não
  verifiquei o estado atual do `osmdroid`**; não o recomendo nem descarto.
- **"MapLibre não precisa de chave":** vale para o MapLibre e para o OpenFreeMap. Provedores
  como o MapTiler e o Protomaps hospedado exigem chave **do provedor** (o Protomaps mostra
  `?key=MY_KEY` num exemplo).
- **GraphHopper offline no aparelho:** foi de memória; não li a documentação. Está como
  **(confira)** na seção 10.
- **Tamanho dos arquivos:** eu disse "dezenas a centenas de MB". Só tenho um exemplo real
  (Berlim, 84 MB) e a estimativa de tiles da seção 7. O tamanho da sua região eu não sei;
  o próprio `pmtiles extract` mostra.
