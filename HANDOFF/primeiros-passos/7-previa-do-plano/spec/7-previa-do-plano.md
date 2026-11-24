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
  - div.scr.sc  @(1,1) 391x849
      display:flex; flex-direction:column; flex-grow:1; gap:14px; padding:22px 16px 24px
    - div.top  @(17,23) 359x44
        display:flex; align-items:center; flex-shrink:0; gap:10px
      - button.ib aria="Voltar"  @(17,23) 44x44
          display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:42px
        - svg.ic  @(28,34) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-back"></use></svg>
    - div.cl  @(17,81) 359x55.5
        display:flex; flex-direction:column; flex-shrink:0; gap:6px
      - h1.t1 "Prévia do seu plano"  @(17,81) 359x31.5
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
      - div.rw  @(17,118.5) 359x18
          display:flex; align-items:center; gap:12px
        - span.tag "Plano 5k"  @(17,118.5) 74.9x18
            padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
        - span.mu.sm "Início: 23 de novembro · Meta 5K: 24 de janeiro"  @(103.9,119.1) 268.2x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - div.card  @(17,150.5) 359x135.6
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(224, 169, 58, 0.4); border-radius:18px
      - div.rw  @(32,165.5) 329x22
          display:flex; align-items:center; gap:12px; color:rgb(224, 169, 58)
        - svg.ic  @(32,165.5) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-shield"></use></svg>
        - b "Aviso de Saúde"  @(66,166.7) 105.1x19.6
            font-weight:700
      - p.mu.sm "Se estiver doente ou com dor, não treine e procure um profissional de saúde. O Elite não faz diagnóstico."  @(32,195.5) 329x33.6
          margin:8px 0px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - div.rw  @(32,241.1) 329x30
          display:flex; align-items:center; gap:12px
        - button.sw aria="Li e entendi"  @(32,241.1) 50x30
            position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
          - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
              position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
              content:""
        - span "Li e entendi o aviso de saúde"  @(94,246.3) 192.5x19.6
    - span.lb "Cronograma semana a semana"  @(17,300.1) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.card.rw.b  @(17,328.1) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,343.1) 68.9x36.4
        - b "Semana 1"  @(32,344.1) 64.6x17
            font-weight:700
        - div.mu.sm "Etapa 1 de 9"  @(32,362.7) 68.9x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - span.mu.sm "3 treinos"  @(311.4,352.9) 49.6x16.8
          color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - div.card.rw.b  @(17,408.5) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,423.5) 71.4x36.4
        - b "Semana 2"  @(32,424.5) 67.4x17
            font-weight:700
        - div.mu.sm "Etapa 2 de 9"  @(32,443.1) 71.4x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - span.mu.sm "3 treinos"  @(311.4,433.3) 49.6x16.8
          color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - div.card.rw.b  @(17,488.9) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,503.9) 71.5x36.4
        - b "Semana 3"  @(32,504.9) 67.6x17
            font-weight:700
        - div.mu.sm "Etapa 3 de 9"  @(32,523.5) 71.5x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - span.mu.sm "3 treinos"  @(311.4,513.7) 49.6x16.8
          color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - div.card.rw.b  @(17,569.3) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,584.3) 71.8x36.4
        - b "Semana 4"  @(32,585.3) 68x17
            font-weight:700
        - div.mu.sm "Etapa 4 de 9"  @(32,603.9) 71.8x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - span.mu.sm "3 treinos"  @(311.4,594.1) 49.6x16.8
          color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - div.card.rw.b  @(17,649.7) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,664.7) 71.3x36.4
        - b "Semana 5"  @(32,665.7) 67.5x17
            font-weight:700
        - div.mu.sm "Etapa 5 de 9"  @(32,684.3) 71.3x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - span.mu.sm "3 treinos"  @(311.4,674.5) 49.6x16.8
          color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - button.btn.pri.stk "Começar meu plano"  @(17,770) 359x56
        display:flex; position:sticky; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:39.9531px 0px 0px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; box-shadow:rgb(10, 11, 9) 0px -14px 14px -6px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
