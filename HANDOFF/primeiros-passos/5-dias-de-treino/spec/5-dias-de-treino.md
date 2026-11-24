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
      - button.ib aria="Voltar"  @(17,23) 44x44
          display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:42px
        - svg.ic  @(28,34) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-back"></use></svg>
    - div.cl  @(17,81) 359x108.2
        display:flex; flex-direction:column; flex-shrink:0; gap:6px
      - h1.t1 "Como você prefere treinar?"  @(17,81) 359x63
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
      - p.mu "Escolha sua frequência semanal e a data para dar a largada."  @(17,150) 359x39.2
          color:rgb(143, 147, 132)
    - span.lb "Dias por semana"  @(17,203.2) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.seg  @(17,231.2) 359x44
        display:flex; flex-shrink:0; gap:6px
      - div.s "2"  @(17,231.2) 176.5x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:174.5px
      - div.s.on "3"  @(199.5,231.2) 176.5x44
          display:grid; align-items:center; flex-grow:1; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-weight:600; grid-template-columns:174.5px
    - div.rw.b  @(17,289.2) 359x18
        display:flex; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px
      - span.lb "Quais dias da semana?"  @(17,291.2) 165.8x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - span.tag "3 de 3"  @(323,289.2) 53x18
          padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
    - div.wk  @(17,321.2) 359x58
        display:grid; flex-shrink:0; gap:6px; text-align:center; grid-template-columns:46.1406px 46.1406px 46.1406px 46.1406px 46.1406px 46.1406px 46.1406px
      - div "Dom"  @(17,321.2) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
      - div.on "Seg"  @(69.1,321.2) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-size:11px; line-height:15.4px
      - div "Ter"  @(121.3,321.2) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
      - div.on "Qua"  @(173.4,321.2) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-size:11px; line-height:15.4px
      - div "Qui"  @(225.6,321.2) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
      - div.on "Sex"  @(277.7,321.2) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-size:11px; line-height:15.4px
      - div "Sáb"  @(329.8,321.2) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
    - p.mu.sm "Escolha dias com um dia de descanso entre eles."  @(17,393.2) 359x16.8
        flex-shrink:0; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - span.lb "Data de início"  @(17,424) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - button.card.rw.b  @(17,452) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,467) 215.3x36.4
        - b "segunda-feira, 23 de novembro"  @(32,468) 215.3x17
            font-weight:700
        - div.mu.sm "23/11/2026"  @(32,486.6) 215.3x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - svg.ic  @(339,474.2) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-cal"></use></svg>
    - div.card.rw.sel  @(17,532.4) 359x52
        display:flex; position:relative; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
      - div.card.rw.sel::before  size 3pxx22px (pseudo; inset 14px 355px 14px -1px)
          position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
          content:""
      - svg.ic  @(32,547.4) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-check"></use></svg>
      - span.sm "3 dias por semana: seg, qua, sex"  @(66,550) 186.3x16.8
          font-size:12px; line-height:16.8px
    - div.sp  @(17,598.4) 359x157.6
        flex-grow:1
    - button.btn.pri "Continuar para a Duração"  @(17,770) 359x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:0px 0px 24px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
