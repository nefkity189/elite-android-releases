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
      - span.lb "Pergunta 3 de 3"  @(71,38) 261x14
          flex-grow:1; margin:0px 44px 0px 0px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-align:center; text-transform:uppercase
    - div.bar  @(17,81) 359x6
        flex-shrink:0; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
      - i  @(17,81) 359x6
          background-color:rgb(163, 181, 101); border-radius:3px
    - div.cl  @(17,111) 359x141.7
        display:flex; flex-direction:column; flex-shrink:0; gap:8px; margin:10px 0px 0px
      - h1.t1 "Que tipo de exercício você pratica?"  @(17,111) 359x94.5
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
      - p.mu "Isso nos ajuda a calibrar a intensidade inicial do seu plano."  @(17,213.5) 359x39.2
          color:rgb(143, 147, 132)
    - div.cl  @(17,274.7) 359x173.2
        display:flex; flex-direction:column; flex-shrink:0; gap:8px; margin:8px 0px 0px
      - button.card "Nenhum"  @(17,274.7) 359x52.4
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; font-size:16px; font-weight:600; line-height:22.4px
      - button.card.sel "Baixo impacto, como caminhada"  @(17,335.1) 359x52.4
          position:relative; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px; font-size:16px; font-weight:600; line-height:22.4px
        - button.card.sel::before  size 3pxx22.3906px (pseudo; inset 14px 355px 14px -1px)
            position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
            content:""
      - button.card "Cardio, como bicicleta ou natação"  @(17,395.5) 359x52.4
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; font-size:16px; font-weight:600; line-height:22.4px
    - div.sp  @(17,461.9) 359x294.1
        flex-grow:1
    - button.btn.pri "Continuar para os Dias"  @(17,770) 359x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:0px 0px 24px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
