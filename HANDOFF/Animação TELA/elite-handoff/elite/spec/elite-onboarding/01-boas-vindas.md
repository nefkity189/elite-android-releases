# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--bg`: #0A0B09
- `--s1`: #10120D
- `--card`: #161911
- `--card2`: #1D2116
- `--field`: #0D0F0A
- `--page`: #050604
- `--line`: rgba(236,232,218,.10)
- `--line2`: rgba(236,232,218,.18)
- `--text`: #ECE8DA
- `--mut`: #8F9384
- `--dim`: #5E6255
- `--olive`: #A3B565
- `--olive-d`: #5D6B34
- `--olive-s`: rgba(163,181,101,.14)
- `--on-olive`: #0E1306
- `--red`: #D12B2B
- `--red-s`: rgba(209,43,43,.15)
- `--on-red`: #FFF4F0
- `--brass`: #C9A24B
- `--amber`: #E0A93A
- `--ok`: #7FB069
- `--num`: "Barlow Condensed","Arial Narrow",system-ui,sans-serif
- `--ui`: "Inter",system-ui,sans-serif
- `--sten`: "Saira Stencil One","Barlow Condensed",Impact,sans-serif

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Saira Stencil One  loaded=False
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`
- `@media (max-width: 430px)`

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - div.scr  @(1,1) 391x849
      display:flex; flex-direction:column; flex-grow:1; gap:18px; padding:56px 16px 0px
    - div  @(110.6,57) 171.8x142
        flex-shrink:0
      - div.lg.v  @(110.6,57) 171.8x142
          display:inline-flex; flex-direction:column; align-items:center; gap:10px; text-align:center
        - svg  @(157.5,57) 78x78
            overflow:hidden
            svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
        - div  @(110.6,145) 171.8x54
          - div.wm "Elite"  @(110.6,145) 180.9x38
              margin:0px -9.12px 0px 0px; font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:38px; line-height:38px; letter-spacing:9.12px; text-transform:uppercase
          - div.tg "Missão dada é missão cumprida"  @(110.6,189) 171.8x10
              margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:8px; font-weight:600; letter-spacing:1.04px; text-transform:uppercase
    - div.cl  @(17,225) 359x76.7
        display:flex; flex-direction:column; flex-shrink:0; gap:6px; margin:8px 0px 0px; text-align:center
      - h1.t1 "Bem-vindo ao Elite"  @(17,225) 359x31.5
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
      - p.mu "Um plano de caminhada e corrida que respeita o seu fôlego, do primeiro passo até os 5 km."  @(17,262.5) 359x39.2
          color:rgb(143, 147, 132)
    - div.cl  @(17,319.7) 359x226
        display:flex; flex-direction:column; flex-shrink:0; gap:8px
      - div.card.rw  @(17,319.7) 359x70
          display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.ico  @(32,334.7) 40x40
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
          - svg.ic  @(41,343.7) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-shield"></use></svg>
        - div  @(84,336.5) 138.7x36.4
          - b "Tudo no seu celular"  @(84,337.5) 133.3x17
              font-weight:700
          - div.mu.sm "Sem login e sem nuvem."  @(84,356.1) 138.7x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - div.card.rw  @(17,397.7) 359x70
          display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.ico  @(32,412.7) 40x40
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
          - svg.ic  @(41,421.7) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-trend"></use></svg>
        - div  @(84,414.5) 177.1x36.4
          - b "Progressivo"  @(84,415.5) 81.8x17
              font-weight:700
          - div.mu.sm "A corrida aumenta aos poucos."  @(84,434.1) 177.1x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - div.card.rw  @(17,475.7) 359x70
          display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.ico  @(32,490.7) 40x40
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
          - svg.ic  @(41,499.7) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-clock"></use></svg>
        - div  @(84,492.5) 194x36.4
          - b "Sem pressa"  @(84,493.5) 79.9x17
              font-weight:700
          - div.mu.sm "Você escolhe os dias e a duração."  @(84,512.1) 194x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - div.sp  @(17,563.7) 359x96.3
        flex-grow:1
    - div.cl  @(17,678) 359x74
        display:flex; flex-direction:column; flex-shrink:0; gap:8px
      - span.lb "Como devemos te chamar?"  @(17,678) 359x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.fld.f "Seu nome ou apelido"  @(17,700) 359x52
          display:flex; align-items:center; padding:0px 14px; background-color:rgb(13, 15, 10); border:1px solid rgb(163, 181, 101); border-radius:14px
    - button.btn.pri "Começar"  @(17,770) 359x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:0px 0px 24px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
