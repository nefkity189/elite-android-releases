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
  - div.a  @(500,420) 820x824
      position:absolute
    - svg  @(500,420) 820x820
        opacity:0.06; overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="820" height="820" style="opacity:0.06"><path fill="#C9A24B" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#C9A24B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z  @(72,72) 64x64
      position:absolute; z-index:2
    - svg  @(72,72) 64x64
        overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="64" height="64" style="opacity:1"><path fill="#ECE8DA" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#D12B2B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z.wm "ELITE"  @(160,76) 187.3x56
      position:absolute; font-family:Barlow; font-size:44px; font-weight:700; line-height:56px; letter-spacing:10.56px; z-index:5
  - div.z.ctx "Prova 5K das 13:00"  @(766.3,86) 241.7x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:26px; font-weight:600; line-height:32px; text-align:right; z-index:5
  - div.z.bar  @(72,1232) 66.1x10
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.tg "Missão dada é missão cumprida"  @(72,1262) 371.8x30
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.tg "Competição online"  @(790.8,1262) 217.2x30
      position:absolute; font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.d.sk "NO PÓDIO"  @(72,200) 986.2x170
      position:absolute; font-family:Barlow, sans-serif; font-size:190px; font-weight:700; line-height:170px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.k "Os três primeiros da missão, tempo oficial"  @(72,400) 634.5x40
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:32px; line-height:40px; z-index:2
  - div.z  @(72,720) 300x102
      position:absolute; z-index:2
    - div  @(136,720) 236x102
        margin:0px 0px 0px 64px
      - div.d "Marina L."  @(136,720) 236x42
          font-family:Barlow, sans-serif; font-size:36px; font-weight:700; line-height:42px
      - div.d "24:02"  @(136,762) 236x60
          color:rgb(143, 147, 132); font-family:Barlow, sans-serif; font-size:54px; font-weight:700; line-height:60px
  - div.a.sk  @(72,850) 335.8x300
      position:absolute; background-color:rgb(184, 188, 176); transform:matrix(1, 0, -0.212557, 1, 0, 0)
    - div.d "2"  @(112.6,864) 272x190
        position:absolute; color:rgb(14, 19, 6); font-family:Barlow, sans-serif; font-size:190px; font-weight:700; line-height:190px; text-align:center; transform:matrix(1, 0, 0.212557, 1, 0, 0)
  - div.z  @(386,570) 300x128
      position:absolute; z-index:2
    - div  @(482,570) 204x128
        margin:0px 0px 0px 96px
      - div.bar  @(482,570) 86.6x12
          margin:0px 0px 14px; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0)
      - div.d "Rafael S."  @(482,596) 204x42
          font-family:Barlow, sans-serif; font-size:36px; font-weight:700; line-height:42px
      - div.d "23:47"  @(482,638) 204x60
          font-family:Barlow, sans-serif; font-size:54px; font-weight:700; line-height:60px
  - div.a.sk  @(386,700) 367.7x450
      position:absolute; background-color:rgb(201, 162, 75); transform:matrix(1, 0, -0.212557, 1, 0, 0)
    - div.d "1"  @(458.5,714) 272x190
        position:absolute; color:rgb(14, 19, 6); font-family:Barlow, sans-serif; font-size:190px; font-weight:700; line-height:190px; text-align:center; transform:matrix(1, 0, 0.212557, 1, 0, 0)
  - div.z  @(700,800) 300x102
      position:absolute; z-index:2
    - div  @(747,800) 253x102
        margin:0px 0px 0px 47px
      - div.d "Caio M."  @(747,800) 253x42
          font-family:Barlow, sans-serif; font-size:36px; font-weight:700; line-height:42px
      - div.d "24:55"  @(747,842) 253x60
          color:rgb(143, 147, 132); font-family:Barlow, sans-serif; font-size:54px; font-weight:700; line-height:60px
  - div.a.sk  @(700,930) 318.8x220
      position:absolute; background-color:rgb(168, 112, 62); transform:matrix(1, 0, -0.212557, 1, 0, 0)
    - div.d "3"  @(723.6,944) 272x190
        position:absolute; color:rgb(14, 19, 6); font-family:Barlow, sans-serif; font-size:190px; font-weight:700; line-height:190px; text-align:center; transform:matrix(1, 0, 0.212557, 1, 0, 0)
  - div.a  @(72,1150) 936x4
      position:absolute; background-color:rgb(29, 33, 22)
