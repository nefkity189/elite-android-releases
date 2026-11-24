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
      - button.ib  @(17,23) 44x44
          display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:42px
        - svg.ic  @(28,34) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-back"></use></svg>
      - h2 "Detalhes da corrida"  @(71,33) 305x24
          flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:24px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
    - div.cl  @(17,81) 359x47.5
        display:flex; flex-direction:column; flex-shrink:0; gap:2px
      - span.lb.ol "quinta-feira, 19 de nov"  @(17,81) 359x14
          color:rgb(163, 181, 101); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - h1.t1 "Treino de Corrida"  @(17,97) 359x31.5
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
    - div.card  @(17,142.5) 359x137.6
        position:relative; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; overflow:hidden
      - svg aria="Traçado da rota"  @(18,143.5) 357x130
          overflow:hidden
          svg: <svg viewBox="0 0 361 130" width="100%" height="130" aria-label="Traçado da rota"><rect width="361" height="130" fill="#0C0E09"></rect><path d="M0 100Q80 80 160 95T361 60" fill="none" stroke="#1C2015" stroke-width="8"></path><path d="M40 100C70 66 110 98 150 62S230 30 270 56 320 30 330 22" fill="none" stroke="#A3B565" stroke-width="4" stroke-linecap="round"></path><circle cx="40" cy="100" r="6" fill="#A3B565"></circle><circle cx="330" cy="22" r="6" fill="#D12B2B"></circle></svg>
    - div.card  @(17,294.1) 359x113.6
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.lb "Métricas da corrida"  @(32,312.1) 152.8x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.rw.b  @(32,336.7) 329x28
          display:flex; justify-content:space-between; align-items:center; gap:12px; margin:8px 0px 0px
        - span "Distância"  @(32,340.9) 60.9x19.6
        - b.num "2,40 km"  @(282,336.7) 79x28
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
      - div.rw.b  @(32,364.7) 329x28
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - span "Ritmo médio"  @(32,368.9) 82.4x19.6
        - b.num "6:05 min/km"  @(239,364.7) 122x28
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
    - div.card  @(17,421.7) 359x124.4
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.lb "Esforço percebido"  @(32,439.7) 140.1x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.lv  @(32,464.3) 329x44
          display:grid; gap:6px; margin:8px 0px 0px; grid-template-columns:61px 61px 61px 61px 61px
        - div.s "1"  @(32,464.3) 61x44
            display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:59px
        - div.s "2"  @(99,464.3) 61x44
            display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:59px
        - div.s.on "3"  @(166,464.3) 61x44
            display:grid; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:59px
        - div.s "4"  @(233,464.3) 61x44
            display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:59px
        - div.s "5"  @(300,464.3) 61x44
            display:grid; align-items:center; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; font-size:12px; font-weight:600; grid-template-columns:59px
      - div.rw.b.mu.sm  @(32,514.3) 329x16.8
          display:flex; justify-content:space-between; align-items:center; gap:12px; margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - span "Muito leve"  @(32,514.3) 58.4x16.8
        - span "Máximo"  @(316.3,514.3) 44.8x16.8
    - span.lb "Anotações particulares"  @(17,560.1) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.fld "Ex: Me senti leve na primeira corrida e consegui manter o ritmo."  @(17,588.1) 359x84
        display:flex; align-items:flex-start; flex-shrink:0; padding:12px 14px 0px; color:rgb(143, 147, 132); background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.18); border-radius:14px
    - button.btn.olv.stk "Salvar anotações"  @(17,696) 359x56
        display:flex; position:sticky; justify-content:center; align-items:center; flex-shrink:0; gap:10px; margin:9.92188px 0px 0px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; box-shadow:rgb(10, 11, 9) 0px -14px 14px -6px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
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
          display:grid; position:relative; align-items:center; background-image:conic-gradient(from 39.5928deg, rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:16px; box-shadow:rgba(163, 181, 101, 0.086) 0px 0px 16px 2px; text-align:center; transition:transform 0.18s; animation:5s linear infinite spin, 3.2s ease-in-out infinite glow; grid-template-columns:48px
        - button.cb::before  size 44pxx44px (pseudo; inset 2px)
            position:absolute; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(12, 14, 8) 70%); border-radius:14px
            content:""
        - button.cb::after  size 48pxx48px (pseudo; inset 0px)
            position:absolute; border:2px solid rgb(163, 181, 101); border-radius:16px; opacity:0
            content:""
        - svg  @(183.5,795) 26x26
            position:relative; color:rgb(163, 181, 101); overflow:hidden
            svg: <svg viewBox="0 0 64 64"><use href="#blade"></use></svg>
    - button.ni.on  @(231.8,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(163, 181, 101); font-size:11px; font-weight:500; text-align:center
      - svg.ic  @(256.1,789) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-trend"></use></svg>
      - span "Progresso"  @(240.3,813) 53.6x14
    - button.ni  @(302.4,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; text-align:center
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
