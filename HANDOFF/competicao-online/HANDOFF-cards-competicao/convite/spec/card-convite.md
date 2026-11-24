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
  - div.a  @(560,300) 700x704
      position:absolute
    - svg  @(560,300) 700x700
        opacity:0.05; overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="700" height="700" style="opacity:0.05"><path fill="#A3B565" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#A3B565" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z  @(72,72) 64x64
      position:absolute; z-index:2
    - svg  @(72,72) 64x64
        overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="64" height="64" style="opacity:1"><path fill="#ECE8DA" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#D12B2B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z.wm "ELITE"  @(160,76) 187.3x56
      position:absolute; font-family:Barlow; font-size:44px; font-weight:700; line-height:56px; letter-spacing:10.56px; z-index:5
  - div.z.ctx "Convite"  @(911.2,86) 96.8x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:26px; font-weight:600; line-height:32px; text-align:right; z-index:5
  - div.z.bar  @(72,1232) 66.1x10
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.tg "Missão dada é missão cumprida"  @(72,1262) 371.8x30
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.tg "Competição online"  @(790.8,1262) 217.2x30
      position:absolute; font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.lbl "Corra a próxima prova com a gente"  @(72,190) 539.6x40
      position:absolute; color:rgb(163, 181, 101); font-family:Inter; font-size:32px; font-weight:600; line-height:40px; z-index:3
  - div.z.d.sk "5K"  @(40,230) 1145.9x580
      position:absolute; color:rgb(163, 181, 101); font-family:Barlow, sans-serif; font-size:800px; font-weight:700; line-height:580px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.bar  @(72,846) 302.6x12
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.d.sk "LARGADA 13:00"  @(72,872) 931.8x128
      position:absolute; font-family:Barlow, sans-serif; font-size:116px; font-weight:700; line-height:128px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.a.sk  @(72,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(164,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(256,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(348,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(440,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(532,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(624,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(716,1010) 69.5x26
      position:absolute; background-color:rgb(163, 181, 101); border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(808,1010) 69.5x26
      position:absolute; border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.a.sk  @(900,1010) 69.5x26
      position:absolute; border:3px solid rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.z.k "8 corredores inscritos, sua vaga está aberta"  @(72,1056) 624.5x32
      position:absolute; font-family:Inter; font-size:30px; line-height:32px; z-index:2
  - div.a.sk  @(72,1100) 920.4x96
      position:absolute; background-color:rgb(163, 181, 101); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.z "Entre na sala da prova"  @(72,1100) 900x96
      display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(14, 19, 6); font-family:Inter; font-size:38px; font-weight:600; z-index:2
