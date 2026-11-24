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
  - div.a  @(560,120) 700x704
      position:absolute
    - svg  @(560,120) 700x700
        opacity:0.05; overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="700" height="700" style="opacity:0.05"><path fill="#A3B565" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#A3B565" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z  @(72,72) 64x64
      position:absolute; z-index:2
    - svg  @(72,72) 64x64
        overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="64" height="64" style="opacity:1"><path fill="#ECE8DA" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#D12B2B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z.wm "ELITE"  @(160,76) 187.3x56
      position:absolute; font-family:Barlow; font-size:44px; font-weight:700; line-height:56px; letter-spacing:10.56px; z-index:5
  - div.z.ctx "Ranking de Mato Grosso"  @(702.2,86) 305.8x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:26px; font-weight:600; line-height:32px; text-align:right; z-index:5
  - div.z.bar  @(72,1232) 66.1x10
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.tg "Missão dada é missão cumprida"  @(72,1262) 371.8x30
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.tg "Competição online"  @(790.8,1262) 217.2x30
      position:absolute; font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.lbl "Sua posição na temporada"  @(72,200) 410.4x40
      position:absolute; color:rgb(163, 181, 101); font-family:Inter; font-size:32px; font-weight:600; line-height:40px; z-index:3
  - div.z.d.sk "#12"  @(72,230) 777.3x360
      position:absolute; color:rgb(163, 181, 101); font-family:Barlow, sans-serif; font-size:420px; font-weight:700; line-height:360px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z  @(740,400) 375.4x182
      position:absolute; z-index:2
    - div.k "pontos Elo"  @(740,400) 375.4x32
        color:rgb(143, 147, 132); font-family:Inter; font-size:26px; line-height:32px
    - div.d "1.482"  @(740,432) 375.4x150
        font-family:Barlow, sans-serif; font-size:150px; font-weight:700; line-height:150px
  - div.z.d "Rafael Souza"  @(72,640) 288.2x56
      position:absolute; color:rgb(143, 147, 132); font-family:Barlow, sans-serif; font-size:48px; font-weight:500; line-height:56px; z-index:2
  - div.a.sk  @(72,790) 920.4x96
      position:absolute; background-color:rgb(29, 33, 22); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.z.d  @(112,790) 820x96
      display:flex; position:absolute; align-items:center; font-family:Barlow, sans-serif; font-size:44px; font-weight:700; z-index:2
    - span "11"  @(112,813) 110x50
    - span "Bruno T."  @(222,816) 147.1x44
        font-family:Inter; font-size:36px; font-weight:600
    - span "1.497"  @(811.9,810.5) 120.1x55
        margin:0px 0px 0px 442.75px; font-size:48px
  - div.a.sk  @(72,898) 927.2x128
      position:absolute; background-color:rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.z.d  @(112,898) 820x128
      display:flex; position:absolute; align-items:center; color:rgb(14, 19, 6); font-family:Barlow, sans-serif; font-size:50px; font-weight:700; z-index:2
    - span "12"  @(112,933) 110x58
    - span "Rafael S."  @(222,940) 152.7x44
        font-family:Inter; font-size:36px; font-weight:600
    - span "1.482"  @(786.8,928.5) 145.2x67
        margin:0px 0px 0px 412.172px; font-size:58px
  - div.a.sk  @(72,1038) 920.4x96
      position:absolute; background-color:rgb(29, 33, 22); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.z.d  @(112,1038) 820x96
      display:flex; position:absolute; align-items:center; font-family:Barlow, sans-serif; font-size:44px; font-weight:700; z-index:2
    - span "13"  @(112,1061) 110x50
    - span "Lívia P."  @(222,1064) 123.6x44
        font-family:Inter; font-size:36px; font-weight:600
    - span "1.470"  @(811.9,1058.5) 120.1x55
        margin:0px 0px 0px 466.312px; font-size:48px
