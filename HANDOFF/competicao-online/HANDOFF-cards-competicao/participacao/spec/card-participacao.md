# Layout spec (dp = CSS px)

Screen: 1080 x 1350 dp

## Design tokens (:root)
- `--ink`: #0A0B09
- `--card`: #161911
- `--elev`: #1D2116
- `--bone`: #ECE8DA
- `--muted`: #8F9384
- `--dim`: #5E6255
- `--olive`: #A3B565
- `--on-olive`: #0E1306
- `--red`: #D12B2B
- `--brass`: #C9A24B
- `--silver`: #B8BCB0
- `--bronze`: #A8703E
- `--skew`: -12deg

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Barlow  loaded=False

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main.phone  @(0,0) 1080x1350
    position:relative; flex-shrink:0; background-color:rgb(10, 11, 9); overflow:hidden
  - div.a  @(430,300) 760x764
      position:absolute
    - svg  @(430,300) 760x760
        opacity:0.07; overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="760" height="760" style="opacity:0.07"><path fill="#A3B565" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#A3B565" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z  @(72,72) 64x64
      position:absolute; z-index:2
    - svg  @(72,72) 64x64
        overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="64" height="64" style="opacity:1"><path fill="#ECE8DA" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#D12B2B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z.wm "ELITE"  @(160,76) 187.3x56
      position:absolute; font-family:Barlow; font-size:44px; font-weight:700; line-height:56px; letter-spacing:10.56px; z-index:5
  - div.z.ctx "Corrida 5K"  @(872.7,86) 135.3x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:26px; font-weight:600; line-height:32px; text-align:right; z-index:5
  - div.z.bar  @(72,1232) 66.1x10
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.tg "Missão dada é missão cumprida"  @(72,1262) 371.8x30
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.tg "Competição online"  @(790.8,1262) 217.2x30
      position:absolute; font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.lbl "Missão cumprida"  @(72,200) 265.7x40
      position:absolute; color:rgb(163, 181, 101); font-family:Inter; font-size:32px; font-weight:600; line-height:40px; z-index:3
  - div.z.d "Rafael Souza"  @(72,244) 323.7x60
      position:absolute; font-family:Barlow, sans-serif; font-size:52px; font-weight:700; line-height:60px; z-index:2
  - div.z.d.sk "24:18"  @(72,330) 1150.7x360
      position:absolute; font-family:Barlow, sans-serif; font-size:420px; font-weight:700; line-height:360px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.bar  @(72,700) 242.6x12
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.k "tempo oficial na prova de 5 km"  @(72,728) 378.5x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:26px; line-height:32px; z-index:2
  - div.a  @(72,1008) 936x4
      position:absolute; background-color:rgb(29, 33, 22)
  - div.a  @(72,1008) 936x4
      position:absolute; background-color:rgb(163, 181, 101)
  - div.z  @(730,772) 240x284
      position:absolute; z-index:2
    - svg  @(730,772) 240x280
        overflow:hidden
        svg: <svg viewBox="30 5 120 140" width="240" height="280"><line x1="112.0" y1="38.0" x2="127.6" y2="53.6" stroke="#6F735E" stroke-width="9" stroke-linecap="round"></line><line x1="127.6" y1="53.6" x2="142.2" y2="39.9" stroke="#6F735E" stroke-width="7.5" stroke-linecap="round"></line><line x1="94.0" y1="72.0" x2="77.0" y2="99.1" stroke="#6F735E" stroke-width="13" stroke-linecap="round"></line><line x1="77.0" y1="99.1" x2="47.1" y2="100.7" stroke="#6F735E" stroke-width="9.5" stroke-linecap="round"></line><line x1="47.1" y1="100.7" x2="45.7" y2="111.8" stroke="#8E1F1F" stroke-width="7" stroke-linecap="round"></line><line x1="94.0" y1="72.0" x2="103.0" y2="55.0" stroke="#ECE8DA" stroke-width="14" stroke-linecap="round"></line><line x1="103.0" y1="55.0" x2="112.0" y2="38.0" stroke="#ECE8DA" stroke-width="18" stroke-linecap="round"></line><circle cx="121" cy="23" r="10" fill="#ECE8DA"></circle><line x1="94.0" y1="72.0" x2="117.8" y2="93.4" stroke="#ECE8DA" stroke-width="14.5" stroke-linecap="round"></line><line x1="117.8" y1="93.4" x2="130.9" y2="120.4" stroke="#ECE8DA" stroke-width="10.5" stroke-linecap="round"></line><line x1="130.9" y1="120.4" x2="141.7" y2="117.4" stroke="#D12B2B" stroke-width="8" stroke-linecap="round"></line><line x1="112.0" y1="38.0" x2="97.9" y2="54.9" stroke="#ECE8DA" stroke-width="10" stroke-linecap="round"></line><line x1="97.9" y1="54.9" x2="105.4" y2="73.4" stroke="#ECE8DA" stroke-width="8.5" stroke-linecap="round"></line></svg>
  - div.a  @(986,996) 28x28
      position:absolute; background-color:rgb(236, 232, 218); border-radius:14px
  - div.z  @(72,1060) 936x116
      display:flex; position:absolute; z-index:2
    - div  @(72,1060) 312x116
        flex-grow:1
      - div.v "4º"  @(72,1060) 312x84
          font-family:Barlow; font-size:84px; font-weight:700; line-height:84px
      - div.k "colocação"  @(72,1144) 312x32
          color:rgb(143, 147, 132); font-family:Inter; font-size:26px; line-height:32px
    - div  @(384,1060) 312x116
        flex-grow:1
      - div.v "12"  @(384,1060) 312x84
          font-family:Barlow; font-size:84px; font-weight:700; line-height:84px
      - div.k "corredores"  @(384,1144) 312x32
          color:rgb(143, 147, 132); font-family:Inter; font-size:26px; line-height:32px
    - div  @(696,1060) 312x116
        flex-grow:1
      - div.v "4:51"  @(696,1060) 312x84
          font-family:Barlow; font-size:84px; font-weight:700; line-height:84px
      - div.k "ritmo por km"  @(696,1144) 312x32
          color:rgb(143, 147, 132); font-family:Inter; font-size:26px; line-height:32px
