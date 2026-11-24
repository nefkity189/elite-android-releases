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
      display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:16px; padding:20px 16px
    - span.lb "Compartilhar no Elite"  @(112.5,156.1) 167.9x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div  @(17,186.1) 359x448.8
        display:flex; flex-direction:column; flex-shrink:0; gap:12px; padding:20px; background-image:radial-gradient(circle at 30% 0px, rgb(30, 35, 20), rgb(10, 11, 9) 70%); border:1px solid rgb(93, 107, 52); border-radius:22px
      - div.rw.b  @(38,207.1) 317x30
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - span.lg  @(38,207.1) 101.3x30
            display:flex; align-items:center; gap:8px
          - svg  @(38,207.1) 26x30
              overflow:hidden
              svg: <svg style="width:26px;height:30px" viewBox="0 0 64 64"><use href="#mark"></use></svg>
          - span.wm "Elite"  @(72,213.1) 71.6x18
              margin:0px -4.32px 0px 0px; font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:18px; line-height:18px; letter-spacing:4.32px; text-transform:uppercase
        - span.tag "Treino concluído"  @(222.3,213.1) 132.7x18
            padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
      - svg aria="Trajeto"  @(38,249.1) 317x150
          overflow:hidden
          svg: <svg aria-label="Trajeto" height="150" viewBox="0 0 320 150" width="100%"><path d="M0 110Q80 90 160 105T320 70" fill="none" stroke="#1C2015" stroke-width="8"></path><path d="M30 118C60 80 100 112 140 74S210 40 250 68 300 40 305 30" fill="none" stroke="#A3B565" stroke-linecap="round" stroke-width="4"></path><circle cx="30" cy="118" fill="#A3B565" r="6"></circle><circle cx="305" cy="30" fill="#D12B2B" r="6"></circle></svg>
      - div  @(38,411.1) 317x82.4
        - div.lb "Distância"  @(38,411.1) 317x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.num "2,40"  @(38,425.1) 317x68.4
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:72px; font-weight:600; line-height:68.4px; font-variant-numeric:tabular-nums
          - small "km"  @(197.1,461.1) 41.7x29
              color:rgb(143, 147, 132); font-size:24px; line-height:22.8px
      - div.g2  @(38,505.5) 317x56
          display:grid; gap:10px; grid-template-columns:153.5px 153.5px
        - div  @(38,505.5) 153.5x56
          - div.lb "Tempo"  @(38,505.5) 153.5x14
              color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
          - b.num "28:12"  @(38,522.5) 85.7x36
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:600; line-height:42px; font-variant-numeric:tabular-nums
        - div  @(201.5,505.5) 153.5x56
          - div.lb "Ritmo"  @(201.5,505.5) 153.5x14
              color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
          - b.num "6:05 /km"  @(201.5,522.5) 129.8x36
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:600; line-height:42px; font-variant-numeric:tabular-nums
      - div.sp  @(38,573.5) 317x17.4
          flex-grow:1
      - div.tg "Elite · Missão dada é missão cumprida"  @(38,602.9) 317x11
          color:rgb(143, 147, 132); font-size:9px; font-weight:600; letter-spacing:1.8px; text-align:center; text-transform:uppercase
    - button.btn.pri.sm2 "Compartilhar"  @(17,650.9) 359x44
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
      - svg.ic  @(135.8,663.9) 18x18
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-share"></use></svg>
