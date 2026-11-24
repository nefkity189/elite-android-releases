# Configuração do Mapa Offline (PMTiles) - Elite

## Visão Geral
O app agora suporta modo offline usando MapLibre + PMTiles (Protomaps). O código está implementado e compilando. Falta apenas gerar/obter os assets necessários.

## Assets Necessários

### 1. brazil.pmtiles (PMTiles do Brasil)
**Tamanho estimado:** ~200-500 MB (região Brasil, zoom máx 14)
**Localização:** `app/src/main/assets/brazil.pmtiles` OU baixado em tempo de execução

**Como gerar:**
```bash
# 1. Instale pmtiles CLI
# Windows: baixe de https://github.com/protomaps/go-pmtiles/releases
# Linux/Mac: go install github.com/protomaps/go-pmtiles/cmd/pmtiles@latest

# 2. Escolha uma build diária do Protomaps
# https://maps.protomaps.com/builds
# Exemplo: 20261117.pmtiles

# 3. Defina o bounding box do Brasil
# Oeste=-73.98, Sul=-33.75, Leste=-34.79, Norte=5.27
# Fonte: http://bboxfinder.com

# 4. Extraia a região
pmtiles extract https://build.protomaps.com/20261117.pmtiles brazil.pmtiles \
  --bbox=-73.98,-33.75,-34.79,5.27 --maxzoom=14

# 5. Copie para assets (se < 100MB) OU configure download em tempo de execução
cp brazil.pmtiles D:\AISLE\AISLE_Proj\app\src\main\assets\
```

### 2. Glyphs (fontes para rótulos)
**Fonte:** https://github.com/protomaps/basemaps-assets/releases/download/v4.0.0/glyphs.zip
**Localização:** `app/src/main/assets/glyphs/`

```bash
# Baixe e extraia
wget https://github.com/protomaps/basemaps-assets/releases/download/v4.0.0/glyphs.zip
unzip glyphs.zip -d D:\AISLE\AISLE_Proj\app\src\main\assets\glyphs
```

### 3. Sprites (ícones para POIs)
**Fonte:** https://github.com/protomaps/basemaps-assets/releases/download/v4.0.0/sprites-v4.zip
**Localização:** `app/src/main/assets/sprites/`

```bash
wget https://github.com/protomaps/basemaps-assets/releases/download/v4.0.0/sprites-v4.zip
unzip sprites-v4.zip -d D:\AISLE\AISLE_Proj\app\src\main\assets\sprites
```

## Estrutura Final dos Assets
```
app/src/main/assets/
├── brazil.pmtiles          (~200-500 MB - opcional no APK)
├── style_offline.json      (já criado)
├── glyphs/
│   ├── Noto Sans Regular/
│   │   ├── 0-255.pbf
│   │   └── ...
│   └── ...
└── sprites/
    └── v4/
        ├── light/
        │   ├── sprite.json
        │   └── sprite.png
        └── dark/
            ├── sprite.json
            └── sprite.png
```

## Como Testar

### Opção 1: Com PMTiles no APK (se < 100MB)
1. Copie `brazil.pmtiles` para `app/src/main/assets/`
2. Build e instale: `./gradlew.bat assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk`
3. No app, vá em Perfil > Preferências > ative "Usar Mapa Offline"
4. Inicie uma corrida livre e verifique se o mapa carrega

### Opção 2: Download em Tempo de Execução (Recomendado para Brasil)
Como o arquivo do Brasil é > 100MB, o ideal é baixar na primeira execução:

```kotlin
// No MapComponents.kt, prepareOfflineAssets() já tenta copiar de assets
// Se o arquivo não estiver em assets, você pode implementar download:

private suspend fun downloadPMTilesIfNeeded(context: Context) {
    val destFile = File(context.filesDir, "brazil.pmtiles")
    if (destFile.exists() && destFile.length() > 0) return
    
    // Baixe via WorkManager ou DownloadManager
    val url = "https://seu-servidor.com/brazil.pmtiles"
    // Use DownloadManager ou OkHttp para baixar
}
```

## Ativação do Modo Offline
1. Abra o app
2. Vá na aba **Perfil** (ícone de pessoa)
3. Role até "Preferências de Mapa"
4. Ative **"Usar Mapa Offline"**
5. Inicie uma "Corrida Livre" pela tela Hoje

## Notas Importantes
- O estilo offline (`style_offline.json`) já está configurado para usar o esquema Protomaps
- Se os assets offline não forem encontrados, o app faz **fallback automático** para o modo online (OpenFreeMap Liberty)
- O PMTiles do Brasil inteiro em zoom 14 é grande (~200-500MB). Considere:
  - Zoom máximo 12 para arquivo menor
  - Região específica (ex: apenas Sudeste)
  - Download sob demanda via WorkManager

## Verificação de Logs
Para debugar, filtre logcat por:
```
adb logcat | grep -E "MapLibre|MapView|Style|getMapAsync|offline|PMTiles|prepareOffline|loadOffline|copyAsset"
```