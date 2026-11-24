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
  - div.a  @(380,500) 900x904
      position:absolute
    - svg  @(380,500) 900x900
        opacity:0.05; overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="900" height="900" style="opacity:0.05"><path fill="#C9A24B" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#C9A24B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z  @(72,72) 64x64
      position:absolute; z-index:2
    - svg  @(72,72) 64x64
        overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="64" height="64" style="opacity:1"><path fill="#ECE8DA" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#D12B2B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z.wm "ELITE"  @(160,76) 187.3x56
      position:absolute; font-family:Barlow; font-size:44px; font-weight:700; line-height:56px; letter-spacing:10.56px; z-index:5
  - div.z.ctx "Conquista"  @(879.6,86) 128.4x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:26px; font-weight:600; line-height:32px; text-align:right; z-index:5
  - div.z.bar  @(72,1232) 66.1x10
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.tg "Missão dada é missão cumprida"  @(72,1262) 371.8x30
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.tg "Competição online"  @(790.8,1262) 217.2x30
      position:absolute; font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z  @(72,190) 470x493
      position:absolute; z-index:2
    - svg  @(72,190) 470x489
        overflow:hidden
        svg: <svg viewBox="0 0 100 104" width="470" height="489"><path d="M50 8 L90 22 L88 55 C84 77 65 90 50 96 C34 90 15 77 12 55 L10 22 Z" fill="var(--brass)"></path><g transform="translate(25 22) scale(.78)"><path fill="#0A0B09" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#D12B2B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></g><path d="M30 78 L50 88 L70 78" fill="none" stroke="#0A0B09" stroke-width="5" stroke-linecap="round" stroke-linejoin="round" transform="translate(0 -6)"></path></svg>
  - div.z.d.sk "MT"  @(580,220) 540.3x300
      position:absolute; color:rgb(201, 162, 75); font-family:Barlow, sans-serif; font-size:330px; font-weight:700; line-height:300px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.k "ranking do estado"  @(586,560) 255.5x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:30px; line-height:32px; z-index:2
  - div.z.lbl "Conquista desbloqueada"  @(72,720) 385.9x40
      position:absolute; color:rgb(201, 162, 75); font-family:Inter; font-size:32px; font-weight:600; line-height:40px; z-index:3
  - div.z.d.sk "TOP 10 DO ESTADO"  @(72,770) 1100.4x316
      position:absolute; font-family:Barlow, sans-serif; font-size:176px; font-weight:700; line-height:158px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
    - br  @(701.7,751) 41.7x196
  - div.z.k "Entre os dez melhores corredores de Mato Grosso"  @(72,1112) 761x40
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:32px; line-height:40px; z-index:2
