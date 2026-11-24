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
    - div.cl  @(17,81) 359x120.1
        display:flex; flex-direction:column; flex-shrink:0; gap:6px
      - h1.t1 "Em quantas semanas deseja alcançar os 5K?"  @(17,81) 359x94.5
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
      - p.mu "Mais curto = mais puxado. Mais longo = mais gradual."  @(17,181.5) 359x19.6
          color:rgb(143, 147, 132)
    - div.card  @(17,215.1) 359x192.8
        position:relative; flex-shrink:0; padding:22px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; text-align:center
      - div.num "9"  @(32,238.1) 329x84
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:84px; font-weight:600; line-height:84px; font-variant-numeric:tabular-nums
      - div.lb "semanas"  @(32,322.1) 329x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.bar  @(38,354.1) 317x6
          margin:18px 6px 8px; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
        - i  @(38,354.1) 114.1x6
            background-color:rgb(163, 181, 101); border-radius:3px
      - div.rw.b.mu.sm  @(32,368.1) 329x16.8
          display:flex; justify-content:space-between; align-items:center; gap:12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - span "6 sem (Mín)"  @(32,368.1) 67.6x16.8
        - span.ol "9 sem (Padrão)"  @(149,368.1) 86.9x16.8
            color:rgb(163, 181, 101)
        - span "16 sem (Máx)"  @(285.2,368.1) 75.8x16.8
    - span.lb "Escolha rápida"  @(17,421.9) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.seg  @(17,449.9) 359x44
        display:flex; flex-shrink:0; gap:6px
      - div.s "6 sem"  @(17,449.9) 85.3x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:83.25px
      - div.s.on "9 sem"  @(108.3,449.9) 85.3x44
          display:grid; align-items:center; flex-grow:1; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-weight:600; grid-template-columns:83.25px
      - div.s "12 sem"  @(199.5,449.9) 85.3x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:83.25px
      - div.s "16 sem"  @(290.8,449.9) 85.3x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:83.25px
    - div.card.sel.rw  @(17,507.9) 359x63.6
        display:flex; position:relative; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
      - div.card.sel.rw::before  size 3pxx33.5938px (pseudo; inset 14px 355px 14px -1px)
          position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
          content:""
      - svg.ic  @(32,528.7) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-info"></use></svg>
      - span.sm "Ritmo equilibrado e clássico (Padrão). Permite avanço consistente sem sobrecarga."  @(66,522.9) 295x33.6
          font-size:12px; line-height:16.8px
    - div.rw.mu.sm  @(17,585.5) 359x33.6
        display:flex; align-items:flex-start; flex-shrink:0; gap:8px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - svg.ic  @(17,585.5) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-shield"></use></svg>
      - span "Estes valores e etapas são provisórios e requerem validação médica."  @(47,585.5) 329x33.6
    - div.sp  @(17,633.1) 359x122.9
        flex-grow:1
    - button.btn.pri "Ver prévia do plano"  @(17,770) 359x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:0px 0px 24px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
