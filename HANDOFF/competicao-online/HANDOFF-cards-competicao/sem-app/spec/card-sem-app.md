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
  - div.a  @(600,120) 520x524
      position:absolute
    - svg  @(600,120) 520x520
        opacity:0.04; overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="520" height="520" style="opacity:0.04"><path fill="#ECE8DA" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#ECE8DA" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z  @(72,72) 64x64
      position:absolute; z-index:2
    - svg  @(72,72) 64x64
        overflow:hidden
        svg: <svg viewBox="0 0 64 64" width="64" height="64" style="opacity:1"><path fill="#ECE8DA" d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z"></path><path fill="#D12B2B" d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z"></path></svg>
  - div.z.wm "ELITE"  @(160,76) 187.3x56
      position:absolute; font-family:Barlow; font-size:44px; font-weight:700; line-height:56px; letter-spacing:10.56px; z-index:5
  - div.z.ctx "Prova 5K"  @(895,86) 113x32
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:26px; font-weight:600; line-height:32px; text-align:right; z-index:5
  - div.z.bar  @(72,1232) 66.1x10
      position:absolute; background-color:rgb(209, 43, 43); transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
  - div.z.tg "Missão dada é missão cumprida"  @(72,1262) 371.8x30
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.tg "Competição online"  @(790.8,1262) 217.2x30
      position:absolute; font-family:Inter; font-size:24px; font-weight:600; line-height:30px; z-index:2
  - div.z.lbl "Você foi convidado"  @(72,190) 296.3x40
      position:absolute; color:rgb(163, 181, 101); font-family:Inter; font-size:32px; font-weight:600; line-height:40px; z-index:3
  - div.z.d.sk "VEM PARA"  @(72,240) 1115.6x352
      position:absolute; font-family:Barlow, sans-serif; font-size:200px; font-weight:700; line-height:176px; transform:matrix(1, 0, -0.212557, 1, 0, 0); z-index:2
    - br  @(1145.3,216) 47.4x223
    - span "O ELITE"  @(67.1,392) 825.2x223
        color:rgb(163, 181, 101)
  - div.z.k "Corra a prova 5K das 13:00 com a gente"  @(72,620) 607.8x40
      position:absolute; color:rgb(143, 147, 132); font-family:Inter; font-size:32px; line-height:40px; z-index:2
  - div.a  @(72,740) 560x2
      position:absolute; background-color:rgb(29, 33, 22)
  - div.z.d "1"  @(72,754) 42.3x80
      position:absolute; color:rgb(163, 181, 101); font-family:Barlow, sans-serif; font-size:76px; font-weight:700; line-height:80px; z-index:2
  - div.z "Baixe o app Elite"  @(150,770) 283.9x44
      position:absolute; font-family:Inter; font-size:36px; font-weight:600; line-height:44px; z-index:2
  - div.a  @(72,850) 560x2
      position:absolute; background-color:rgb(29, 33, 22)
  - div.z.d "2"  @(72,864) 42.3x80
      position:absolute; color:rgb(163, 181, 101); font-family:Barlow, sans-serif; font-size:76px; font-weight:700; line-height:80px; z-index:2
  - div.z "Crie seu perfil de corredor"  @(150,880) 457.8x44
      position:absolute; font-family:Inter; font-size:36px; font-weight:600; line-height:44px; z-index:2
  - div.a  @(72,960) 560x2
      position:absolute; background-color:rgb(29, 33, 22)
  - div.z.d "3"  @(72,974) 42.3x80
      position:absolute; color:rgb(163, 181, 101); font-family:Barlow, sans-serif; font-size:76px; font-weight:700; line-height:80px; z-index:2
  - div.z "Entre na sala da prova 5K"  @(150,990) 439.7x44
      position:absolute; font-family:Inter; font-size:36px; font-weight:600; line-height:44px; z-index:2
  - div.z  @(650,660) 343x404
      position:absolute; z-index:2
    - svg  @(650,660) 343x400
        overflow:hidden
        svg: <svg viewBox="30 5 120 140" width="343" height="400"><line x1="112.0" y1="38.0" x2="127.6" y2="53.6" stroke="#6F735E" stroke-width="9" stroke-linecap="round"></line><line x1="127.6" y1="53.6" x2="142.2" y2="39.9" stroke="#6F735E" stroke-width="7.5" stroke-linecap="round"></line><line x1="94.0" y1="72.0" x2="77.0" y2="99.1" stroke="#6F735E" stroke-width="13" stroke-linecap="round"></line><line x1="77.0" y1="99.1" x2="47.1" y2="100.7" stroke="#6F735E" stroke-width="9.5" stroke-linecap="round"></line><line x1="47.1" y1="100.7" x2="45.7" y2="111.8" stroke="#8E1F1F" stroke-width="7" stroke-linecap="round"></line><line x1="94.0" y1="72.0" x2="103.0" y2="55.0" stroke="#ECE8DA" stroke-width="14" stroke-linecap="round"></line><line x1="103.0" y1="55.0" x2="112.0" y2="38.0" stroke="#ECE8DA" stroke-width="18" stroke-linecap="round"></line><circle cx="121" cy="23" r="10" fill="#ECE8DA"></circle><line x1="94.0" y1="72.0" x2="117.8" y2="93.4" stroke="#ECE8DA" stroke-width="14.5" stroke-linecap="round"></line><line x1="117.8" y1="93.4" x2="130.9" y2="120.4" stroke="#ECE8DA" stroke-width="10.5" stroke-linecap="round"></line><line x1="130.9" y1="120.4" x2="141.7" y2="117.4" stroke="#D12B2B" stroke-width="8" stroke-linecap="round"></line><line x1="112.0" y1="38.0" x2="97.9" y2="54.9" stroke="#ECE8DA" stroke-width="10" stroke-linecap="round"></line><line x1="97.9" y1="54.9" x2="105.4" y2="73.4" stroke="#ECE8DA" stroke-width="8.5" stroke-linecap="round"></line></svg>
  - div.a.sk  @(72,1090) 920.4x96
      position:absolute; border:3px solid rgb(236, 232, 218); transform:matrix(1, 0, -0.212557, 1, 0, 0)
  - div.z "Baixe o Elite"  @(72,1090) 900x96
      display:flex; position:absolute; justify-content:center; align-items:center; font-family:Inter; font-size:38px; font-weight:600; z-index:2
