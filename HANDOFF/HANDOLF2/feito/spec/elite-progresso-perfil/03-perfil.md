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
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - div.scr.sc  @(1,1) 391x775
      display:flex; flex-direction:column; flex-grow:1; gap:14px; padding:22px 16px 24px
    - div.top  @(17,23) 359x44
        display:flex; align-items:center; flex-shrink:0; gap:10px
      - h2 "Perfil"  @(17,33) 359x24
          flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:24px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
    - div.card.rw  @(17,81) 359x94
        display:flex; position:relative; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.ai  @(32,96) 64x64
          display:grid; align-items:center; flex-shrink:0; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(10, 11, 9) 72%); border:2px solid rgb(93, 107, 52); border-radius:20px; grid-template-columns:60px
        - svg  @(46,108) 36x40
            overflow:hidden
            svg: <svg viewBox="0 0 64 64" style="width:36px;height:40px"><use href="#mark"></use></svg>
      - div  @(108,107.8) 197x40.4
          flex-grow:1
        - b.t2 "Recruta"  @(108,106.8) 99.3x24
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - div.mu.sm "Rumo aos seus 5K no Elite"  @(108,131.4) 197x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - button.ib aria="Editar nome"  @(317,106) 44x44
          display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:42px
        - svg.ic  @(328,117) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-edit"></use></svg>
    - span.lb "Durante a corrida"  @(17,189) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.card.cl  @(17,217) 359x284.4
        display:flex; position:relative; flex-direction:column; flex-shrink:0; gap:14px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.rw.b  @(32,232) 329x53.2
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,232) 267x53.2
          - b "Voz do treinador"  @(32,233) 112.5x17
              font-weight:700
          - div.mu.sm "Instruções sonoras para início, trocas de passo e ritmo"  @(32,251.6) 267x33.6
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - button.sw aria="Voz"  @(311,243.6) 50x30
            position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
          - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
              position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
              content:""
      - div.rw.b  @(32,299.2) 329x36.4
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,299.2) 159.9x36.4
          - b "Vibração"  @(32,300.2) 61.8x17
              font-weight:700
          - div.mu.sm "Alertas táteis nas transições"  @(32,318.8) 159.9x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - button.sw aria="Vibração"  @(311,302.4) 50x30
            position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
          - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
              position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
              content:""
      - div.rw.b  @(32,349.6) 329x36.4
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,349.6) 236.5x36.4
          - b "Avisos por distância"  @(32,350.6) 138.4x17
              font-weight:700
          - div.mu.sm "Anúncios de voz a cada marca percorrida"  @(32,369.2) 236.5x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - button.sw aria="Avisos"  @(311,352.8) 50x30
            position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
          - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
              position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
              content:""
      - div.rw.b  @(32,400) 329x36
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - span "Avisar a cada"  @(32,408.2) 88.7x19.6
        - div.rw  @(217.3,400) 143.7x36
            display:flex; align-items:center; gap:12px
          - button.ib "−"  @(217.3,400) 36x36
              display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:34px
          - b.num "1 km"  @(265.3,404) 47.7x28
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
          - button.ib  @(325,400) 36x36
              display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:34px
            - svg.ic  @(332,407) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-plus"></use></svg>
      - div.rw.b  @(32,450) 329x36.4
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,450) 204.3x36.4
          - b "Mostrar mapa durante corrida"  @(32,451) 204.3x17
              font-weight:700
          - div.mu.sm "Exibe sua posição e rota no mapa"  @(32,469.6) 204.3x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - button.sw aria="Mapa"  @(311,453.2) 50x30
            position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
          - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
              position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
              content:""
    - span.lb "Modo de câmera padrão"  @(17,515.4) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.seg  @(17,543.4) 359x44
        display:flex; flex-shrink:0; gap:6px
      - div.s.on "Seguir você"  @(17,543.4) 115.7x44
          display:grid; align-items:center; flex-grow:1; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-size:12px; font-weight:600; grid-template-columns:113.656px
      - div.s "Com direção"  @(138.7,543.4) 115.7x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:12px; font-weight:600; grid-template-columns:113.672px
      - div.s "Rota completa"  @(260.3,543.4) 115.7x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:12px; font-weight:600; grid-template-columns:113.672px
    - span.lb "Plano e lembretes"  @(17,601.4) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - button.card.rw.b  @(17,629.4) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,644.4) 158.6x36.4
        - b "Ajustes do plano"  @(32,645.4) 114.1x17
            font-weight:700
        - div.mu.sm "Ajustes automáticos ligados"  @(32,664) 158.6x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - svg.ic  @(339,651.5) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-chev"></use></svg>
    - div.card.rw.b  @(17,709.8) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,724.8) 255.2x36.4
        - b "Lembrete diário"  @(32,725.8) 107.1x17
            font-weight:700
        - div.mu.sm "Apenas nos dias com treino pendente · 18:30"  @(32,744.3) 255.2x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - button.sw aria="Lembrete"  @(311,727.9) 50x30
          position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
        - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
            position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
            content:""
  - nav.nav  @(13,776) 367x64
      display:flex; align-items:center; flex-shrink:0; padding:0px 6px; margin:0px 12px 10px; background-color:rgb(16, 18, 13); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px
    - button.ni  @(20,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; text-align:center
      - svg.ic  @(44.3,789) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-home"></use></svg>
      - span "Hoje"  @(43.3,813) 24.1x14
    - button.ni  @(90.6,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; text-align:center
      - svg.ic  @(114.9,789) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-cal"></use></svg>
      - span "Plano"  @(111.2,813) 29.3x14
    - div.ni.cn  @(161.2,777) 70.6x62
        display:grid; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; grid-template-columns:48px
      - button.cb aria="Menu rápido"  @(172.5,784) 48x48
          display:grid; position:relative; align-items:center; background-image:conic-gradient(from 46.8deg, rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:16px; box-shadow:rgba(163, 181, 101, 0.118) 0px 0px 16px 2px; text-align:center; transition:transform 0.18s; animation:5s linear infinite spin, 3.2s ease-in-out infinite glow; grid-template-columns:48px
        - button.cb::before  size 44pxx44px (pseudo; inset 2px)
            position:absolute; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(12, 14, 8) 70%); border-radius:14px
            content:""
        - button.cb::after  size 48pxx48px (pseudo; inset 0px)
            position:absolute; border:2px solid rgb(163, 181, 101); border-radius:16px; opacity:0
            content:""
        - svg  @(183.5,795) 26x26
            position:relative; color:rgb(163, 181, 101); overflow:hidden
            svg: <svg viewBox="0 0 64 64"><use href="#blade"></use></svg>
    - button.ni  @(231.8,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; text-align:center
      - svg.ic  @(256.1,789) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-trend"></use></svg>
      - span "Progresso"  @(240.3,813) 53.6x14
    - button.ni.on  @(302.4,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(163, 181, 101); font-size:11px; font-weight:500; text-align:center
      - svg.ic  @(326.7,789) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-user"></use></svg>
      - span "Perfil"  @(323.9,813) 27.6x14
  - div.scrim  @(1,1) 391x849
      position:absolute; background-color:rgba(0, 0, 0, 0.6); opacity:0; z-index:20
  - div#ql.sheet  @(1,870.1) 391x402
      display:flex; position:absolute; flex-direction:column; gap:12px; padding:10px 16px 24px; background-color:rgb(16, 18, 13); border-radius:28px 28px 0px 0px; transform:matrix(1, 0, 0, 1, 0, 422.1); z-index:21
    - div#ql.sheet::before  size 40pxx4px (pseudo; inset auto)
        background-color:rgba(236, 232, 218, 0.18); border-radius:2px
        content:""
    - h2.t2 "Adicionar corrida"  @(18,897.1) 357x23
        font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
    - button.card.rw.b  @(18,932.1) 357x70
        display:flex; position:relative; justify-content:space-between; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.rw  @(33,947.1) 229.5x40
          display:flex; align-items:center; gap:12px
        - span.ico  @(33,947.1) 40x40
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
          - svg.ic  @(42,956.1) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-walk"></use></svg>
        - div  @(85,948.9) 177.5x36.4
          - b "Corrida livre"  @(85,949.9) 84.3x17
              font-weight:700
          - div.mu.sm "Sem meta, ritmo em tempo real"  @(85,968.5) 177.5x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - svg.ic  @(338,956.1) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-chev"></use></svg>
    - button.card.rw.b  @(18,1014.1) 357x70
        display:flex; position:relative; justify-content:space-between; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.rw  @(33,1029.1) 129.1x40
          display:flex; align-items:center; gap:12px
        - span.ico  @(33,1029.1) 40x40
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
          - svg.ic  @(42,1038.1) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-flame"></use></svg>
        - b "Treino 2.5k"  @(85,1039.3) 77.1x19.6
            font-weight:700
      - svg.ic  @(338,1038.1) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-chev"></use></svg>
    - button.card.rw.b  @(18,1096.1) 357x70
        display:flex; position:relative; justify-content:space-between; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.rw  @(33,1111.1) 115.7x40
          display:flex; align-items:center; gap:12px
        - span.ico  @(33,1111.1) 40x40
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
          - svg.ic  @(42,1120.1) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-flame"></use></svg>
        - b "Treino 5k"  @(85,1121.3) 63.7x19.6
            font-weight:700
      - svg.ic  @(338,1120.1) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-chev"></use></svg>
    - button.card.rw.b  @(18,1178.1) 357x70
        display:flex; position:relative; justify-content:space-between; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.rw  @(33,1193.1) 122.3x40
          display:flex; align-items:center; gap:12px
        - span.ico  @(33,1193.1) 40x40
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
          - svg.ic  @(42,1202.1) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-flame"></use></svg>
        - b "Treino 10k"  @(85,1203.3) 70.3x19.6
            font-weight:700
      - svg.ic  @(338,1202.1) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-chev"></use></svg>
  - div#ck.sheet  @(1,875.8) 391x516.4
      display:flex; position:absolute; flex-direction:column; gap:12px; padding:10px 16px 28px; background-color:rgb(16, 18, 13); border-radius:28px 28px 0px 0px; transform:matrix(1, 0, 0, 1, 0, 542.21); z-index:21
    - div#ck.sheet::before  size 40pxx4px (pseudo; inset auto)
        background-color:rgba(236, 232, 218, 0.18); border-radius:2px
        content:""
    - h2.t2 "Como você está hoje?"  @(18,902.8) 357x23
        font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
    - p.mu.sm "Isso ajuda o Elite a ajustar seu treino de hoje."  @(18,937.8) 357x16.8
        color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - span.lb "Como você acordou hoje"  @(18,966.6) 357x14
        color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.lv  @(18,992.6) 357x44
        display:grid; gap:6px; grid-template-columns:66.5938px 66.5938px 66.6094px 66.5938px 66.5938px
      - div.s "1"  @(18,992.6) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
      - div.s "2"  @(90.6,992.6) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
      - div.s "3"  @(163.2,992.6) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.6094px
      - div.s.on "4"  @(235.8,992.6) 66.6x44
          display:grid; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
      - div.s "5"  @(308.4,992.6) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
    - div.rw.b.mu.sm  @(18,1048.6) 357x16.8
        display:flex; justify-content:space-between; align-items:center; gap:12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - span "Muito baixa"  @(18,1048.6) 65.6x16.8
      - span "Ótima"  @(341.7,1048.6) 33.3x16.8
    - span.lb "Como você dormiu ontem à noite"  @(18,1077.4) 357x14
        color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.lv  @(18,1103.4) 357x44
        display:grid; gap:6px; grid-template-columns:66.5938px 66.5938px 66.6094px 66.5938px 66.5938px
      - div.s "1"  @(18,1103.4) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
      - div.s "2"  @(90.6,1103.4) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
      - div.s "3"  @(163.2,1103.4) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.6094px
      - div.s "4"  @(235.8,1103.4) 66.6x44
          display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
      - div.s.on "5"  @(308.4,1103.4) 66.6x44
          display:grid; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:64.5938px
    - div.rw.b.mu.sm  @(18,1159.4) 357x16.8
        display:flex; justify-content:space-between; align-items:center; gap:12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - span "Muito ruim"  @(18,1159.4) 60.3x16.8
      - span "Ótimo"  @(341.3,1159.4) 33.7x16.8
    - div.card.sel.rw  @(18,1188.2) 357x52
        display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
      - div.card.sel.rw::before  size 3pxx22px (pseudo; inset 14px 353px 14px -1px)
          position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
          content:""
      - svg.ic  @(33,1203.2) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-check"></use></svg>
      - span.sm "Seus sinais estão bons. O plano segue como está."  @(67,1205.8) 284.6x16.8
          font-size:12px; line-height:16.8px
    - button.btn.pri "Confirmar e iniciar treino"  @(18,1252.2) 357x56
        display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
    - button.btn.gh.sm2 "Pular check-in"  @(18,1320.2) 357x44
        display:flex; justify-content:center; align-items:center; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
  - div#dl.dlg  @(35.3,273.8) 322.4x303.4
      display:flex; position:absolute; flex-direction:column; gap:12px; padding:20px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px; opacity:0; transform:matrix(0.94, 0, 0, 0.94, 0, -161.391); z-index:22; transition:opacity 0.2s, transform 0.2s
    - span.ico  @(55,293.5) 37.6x37.6
        display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
      - svg.ic  @(63.5,302) 20.7x20.7
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-pin"></use></svg>
    - h2.t2 "Permissão de Localização"  @(55,342.4) 282.9x43.2
        font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
    - p.mu "Usamos sua localização só para medir distância e ritmo. Os dados ficam no seu celular."  @(55,396.9) 282.9x55.3
        color:rgb(143, 147, 132)
    - button.btn.olv.sm2 "Continuar"  @(55,463.5) 282.9x41.4
        display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
    - button.btn.gh.sm2 "Cancelar"  @(55,516.1) 282.9x41.4
        display:flex; justify-content:center; align-items:center; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
