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
      display:flex; flex-direction:column; flex-grow:1; gap:14px; padding:22px 16px 0px
    - div.top  @(17,23) 359x44
        display:flex; align-items:center; flex-shrink:0; gap:10px
      - h2 "Como foi o treino?"  @(17,33) 359x24
          flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:24px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
    - span.lb "Esforço"  @(17,81) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.g2  @(17,109) 359x109.2
        display:grid; flex-shrink:0; gap:10px; grid-template-columns:174.5px 174.5px
      - button.card "Muito fácil"  @(17,109) 174.5x49.6
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; font-weight:600
      - button.card "Fácil"  @(201.5,109) 174.5x49.6
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; font-weight:600
      - button.card.sel "Difícil"  @(17,168.6) 174.5x49.6
          position:relative; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px; font-weight:600
        - button.card.sel::before  size 3pxx19.5938px (pseudo; inset 14px 170.5px 14px -1px)
            position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
            content:""
      - button.card "No limite"  @(201.5,168.6) 174.5x49.6
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; font-weight:600
    - span.lb "Sentiu dor?"  @(17,232.2) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.seg  @(17,260.2) 359x44
        display:flex; flex-shrink:0; gap:6px
      - div.s "Não"  @(17,260.2) 176.5x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:174.5px
      - div.s.on "Sim"  @(199.5,260.2) 176.5x44
          display:grid; align-items:center; flex-grow:1; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-weight:600; grid-template-columns:174.5px
    - span.lb "Onde?"  @(17,318.2) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.rw  @(17,346.2) 359x72
        display:flex; align-items:center; flex-shrink:0; gap:12px
      - span.chip "Joelho"  @(17,346.2) 65.8x30
          display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12.5px; line-height:17.5px
      - span.chip.on "Canela"  @(94.8,346.2) 66.9x30
          display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12.5px; line-height:17.5px
      - span.chip "Tornozelo"  @(173.7,346.2) 84.7x30
          display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12.5px; line-height:17.5px
      - span.chip "Quadril"  @(270.4,346.2) 68.6x30
          display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12.5px; line-height:17.5px
      - span.chip "Panturrilha"  @(17,388.2) 90.1x30
          display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12.5px; line-height:17.5px
    - div.card.sel  @(17,432.2) 359x89.2
        position:relative; flex-shrink:0; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
      - div.card.sel::before  size 3pxx59.1875px (pseudo; inset 14px 355px 14px -1px)
          position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
          content:""
      - span.lb.ol "Próximo passo"  @(32,450.2) 108.6x14
          color:rgb(163, 181, 101); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - p.sm "Plano mantido, com uma etapa mais leve no próximo treino. Terça-feira, 24 de novembro."  @(32,472.8) 329x33.6
          margin:6px 0px 0px; font-size:12px; line-height:16.8px
    - div.card.rw  @(17,535.4) 359x63.6
        display:flex; position:relative; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(209, 43, 43, 0.45); border-radius:18px
      - svg.ic  @(32,556.2) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-shield"></use></svg>
      - span.sm "Dor forte? Pare e procure um profissional de saúde. O Elite não faz diagnóstico."  @(66,550.4) 295x33.6
          font-size:12px; line-height:16.8px
    - div.sp  @(17,613) 359x143
        flex-grow:1
    - button.btn.pri "Salvar"  @(17,770) 359x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:0px 0px 24px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
