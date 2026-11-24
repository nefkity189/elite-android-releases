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
      - h1.t1 "Escolha seu objetivo"  @(17,81) 359x63
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
      - p.mu "Como você quer correr hoje? Toque em uma modalidade para ver os detalhes."  @(17,150) 359x39.2
          color:rgb(143, 147, 132)
    - div.cl  @(17,203.2) 359x474.2
        display:flex; flex-direction:column; flex-shrink:0; gap:8px
      - button.card  @(17,203.2) 359x69.8
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.rw.b  @(32,218.2) 329x23
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - b.t2 "Corrida Livre"  @(32,218.2) 161.9x23
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - span.tag "Sem meta"  @(284,220.7) 77x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
        - div.mu.sm "Sem plano, sem alvo. Apenas você e o caminho."  @(32,241.2) 329x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - button.card  @(17,281) 359x69.8
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.rw.b  @(32,296) 329x23
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - b.t2 "Primeiros 2,5K"  @(32,296) 170.5x23
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - span.tag "6 semanas"  @(276.2,298.5) 84.8x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
        - div.mu.sm "6 semanas · 3 treinos/semana · Meta: 2,5 km"  @(32,319) 329x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - button.card.sel  @(17,358.8) 359x240.8
          position:relative; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
        - button.card.sel::before  size 3pxx210.766px (pseudo; inset 14px 355px 14px -1px)
            position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
            content:""
        - div.rw.b  @(32,373.8) 329x23
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - b.t2 "Primeiros 5K"  @(32,373.8) 150.3x23
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - span.tag "9 semanas"  @(276.2,376.3) 84.8x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
        - div.mu.sm "9 semanas · 2 ou 3 treinos/semana · Meta: 5 km"  @(32,396.8) 329x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - div.hr  @(32,425.6) 329x1
            margin:12px 0px; background-color:rgba(236, 232, 218, 0.1)
        - div.cl  @(32,438.6) 329x146
            display:flex; flex-direction:column; gap:10px
          - div.rw  @(32,438.6) 329x53.2
              display:flex; align-items:center; gap:12px
            - span.ico  @(32,449.2) 32x32
                display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:32px
              - svg.ic  @(37,454.2) 22x22
                  flex-shrink:0; overflow:hidden
                  svg: <svg class="ic"><use href="#i-trend"></use></svg>
            - div  @(76,438.6) 285x53.2
              - b.sm "9 etapas"  @(76,440.6) 50.6x15
                  font-size:12px; font-weight:700; line-height:16.8px
              - div.mu.sm "Progressão clássica de caminhada para corrida contínua"  @(76,458.2) 285x33.6
                  color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - div.rw  @(32,501.8) 329x36.4
              display:flex; align-items:center; gap:12px
            - span.ico  @(32,504) 32x32
                display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:32px
              - svg.ic  @(37,509) 22x22
                  flex-shrink:0; overflow:hidden
                  svg: <svg class="ic"><use href="#i-cal"></use></svg>
            - div  @(76,501.8) 248.4x36.4
              - b.sm "Você escolhe"  @(76,503.8) 78.8x15
                  font-size:12px; font-weight:700; line-height:16.8px
              - div.mu.sm "2 ou 3 treinos por semana, dias que preferir"  @(76,521.4) 248.4x16.8
                  color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - div.rw  @(32,548.2) 329x36.4
              display:flex; align-items:center; gap:12px
            - span.ico  @(32,550.3) 32x32
                display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:32px
              - svg.ic  @(37,555.3) 22x22
                  flex-shrink:0; overflow:hidden
                  svg: <svg class="ic"><use href="#i-bolt"></use></svg>
            - div  @(76,548.2) 257.7x36.4
              - b.sm "Ajuste inteligente"  @(76,550.2) 103x15
                  font-size:12px; font-weight:700; line-height:16.8px
              - div.mu.sm "O plano se adapta se você pular ou sentir dor"  @(76,567.8) 257.7x16.8
                  color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - button.card  @(17,607.5) 359x69.8
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.rw.b  @(32,622.5) 329x23
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - b.t2 "Primeiros 10K"  @(32,622.5) 160.2x23
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - span.tag "12 semanas"  @(271.1,625) 89.9x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
        - div.mu.sm "12 semanas · 3 treinos/semana · Meta: 10 km"  @(32,645.5) 329x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - div.sp  @(17,691.3) 359x64.7
        flex-grow:1
    - button.btn.pri "Continuar com Primeiros 5K"  @(17,770) 359x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:0px 0px 24px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
