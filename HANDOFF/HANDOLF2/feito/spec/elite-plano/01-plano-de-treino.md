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

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - div.scr.sc  @(1,1) 391x775
      display:flex; flex-direction:column; flex-grow:1; gap:14px; padding:22px 16px 24px
    - div.top  @(17,23) 359x44
        display:flex; align-items:center; flex-shrink:0; gap:10px
      - h2 "Plano de treino"  @(17,33) 305x24
          flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:24px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
      - button.ib aria="Gerenciar plano"  @(332,23) 44x44
          display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:42px
        - svg.ic  @(343,34) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-gear"></use></svg>
    - div.card.sel  @(17,81) 359x101.5
        position:relative; flex-shrink:0; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
      - div.card.sel::before  size 3pxx71.5px (pseudo; inset 14px 355px 14px -1px)
          position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
          content:""
      - div.rw.b  @(32,96) 329x18
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - span.lb.ol "Etapa atual"  @(32,98) 88.9x14
            color:rgb(163, 181, 101); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - span.tag "Plano 5k"  @(286.1,96) 74.9x18
            padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
      - div.t1 "Semana 3 de 9"  @(32,118) 329x31.5
          margin:4px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
      - div.bar  @(32,161.5) 329x6
          margin:12px 0px 0px; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
        - i  @(32,161.5) 108.6x6
            background-color:rgb(163, 181, 101); border-radius:3px
    - div.card.wc  @(17,196.5) 359x70.4
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.hdr.rw.b  @(32,211.5) 329x40.4
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,211.5) 114.8x40.4
          - b.t2 "Semana 2"  @(32,210.5) 111.8x24
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - div.mu.sm "3 treinos · concluída"  @(32,235.1) 114.8x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - div.rw  @(291.2,217.9) 69.8x27.6
            display:flex; align-items:center; gap:12px
          - span.tag "3/3"  @(291.2,222.7) 35.8x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
          - span.chv  @(339,217.9) 22x27.6
              transition:transform 0.2s
            - svg.ic  @(339,217.9) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-chev"></use></svg>
    - div.card.wc.open.sel  @(17,280.9) 359x281.4
        position:relative; flex-shrink:0; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
      - div.card.wc.open.sel::before  size 3pxx251.391px (pseudo; inset 14px 355px 14px -1px)
          position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
          content:""
      - div.hdr.rw.b  @(32,295.9) 329x40.4
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,295.9) 112x40.4
          - b.t2 "Semana 3"  @(32,294.9) 112x24
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - div.ol.sm "Esta semana"  @(32,319.5) 112x16.8
              color:rgb(163, 181, 101); font-size:12px; line-height:16.8px
        - div.rw  @(293.3,302.3) 67.7x27.6
            display:flex; align-items:center; gap:12px
          - span.tag "1/3"  @(293.3,307.1) 33.7x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
          - span.chv  @(336.2,305.1) 27.6x22
              transform:matrix(0, 1, -1, 0, 0, 0); transition:transform 0.2s
            - svg.ic  @(341.8,305.1) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-chev"></use></svg>
      - div.bd  @(32,336.3) 329x211
          display:flex; flex-direction:column; gap:8px; padding:10px 0px 0px
        - div.hr  @(32,348.3) 329x1
            margin:2px 0px; background-color:rgba(236, 232, 218, 0.1)
        - div.rw.b  @(32,359.3) 329x40
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - div.rw  @(32,359.3) 220.7x40
              display:flex; align-items:center; gap:12px
            - span.ico  @(32,359.3) 40x40
                display:grid; align-items:center; flex-shrink:0; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:12px; grid-template-columns:40px
              - svg.ic  @(41,368.3) 22x22
                  flex-shrink:0; overflow:hidden
                  svg: <svg class="ic"><use href="#i-check"></use></svg>
            - div  @(84,361.1) 168.7x36.4
              - b.sm "Treino 1 · ter, 17 de novembro"  @(84,363.1) 168.7x15
                  font-size:12px; font-weight:700; line-height:16.8px
              - div.mu.sm "• 8 × 1 min correndo"  @(84,380.7) 168.7x16.8
                  color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - span.mu.sm "28 min"  @(322.4,370.9) 38.6x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - div.rw.b  @(32,407.3) 329x40
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - div.rw  @(32,407.3) 226.6x40
              display:flex; align-items:center; gap:12px
            - span.ico  @(32,407.3) 40x40
                display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
              - svg.ic  @(41,416.3) 22x22
                  flex-shrink:0; overflow:hidden
                  svg: <svg class="ic"><use href="#i-play"></use></svg>
            - div  @(84,409.1) 174.6x36.4
              - b.sm "Treino 2 · qui, 19 de novembro"  @(84,411.1) 174.6x15
                  font-size:12px; font-weight:700; line-height:16.8px
              - div.mu.sm "• 8 × 1 min correndo"  @(84,428.7) 174.6x16.8
                  color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - span.mu.sm "28 min"  @(322.4,418.9) 38.6x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - div.rw.b  @(32,455.3) 329x40
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - div.rw  @(32,455.3) 229.5x40
              display:flex; align-items:center; gap:12px
            - span.ico  @(32,455.3) 40x40
                display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
              - svg.ic  @(41,464.3) 22x22
                  flex-shrink:0; overflow:hidden
                  svg: <svg class="ic"><use href="#i-cal"></use></svg>
            - div  @(84,457.1) 177.5x36.4
              - b.sm "Treino 3 · sáb, 21 de novembro"  @(84,459.1) 177.5x15
                  font-size:12px; font-weight:700; line-height:16.8px
              - div.mu.sm "• 6 × 2 min correndo"  @(84,476.7) 177.5x16.8
                  color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - span.mu.sm "30 min"  @(322.1,466.9) 38.9x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - button.btn.gh.sm2 "Ver detalhes"  @(32,503.3) 329x44
            display:flex; justify-content:center; align-items:center; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
    - div.card.wc  @(17,576.3) 359x70.4
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.hdr.rw.b  @(32,591.3) 329x40.4
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,591.3) 112.6x40.4
          - b.t2 "Semana 4"  @(32,590.3) 112.6x24
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - div.mu.sm "3 treinos"  @(32,614.9) 112.6x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - div.rw  @(339,597.7) 22x27.6
            display:flex; align-items:center; gap:12px
          - span.chv  @(339,597.7) 22x27.6
              transition:transform 0.2s
            - svg.ic  @(339,597.7) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-chev"></use></svg>
    - div.card.wc  @(17,660.7) 359x70.4
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.hdr.rw.b  @(32,675.7) 329x40.4
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div  @(32,675.7) 111.9x40.4
          - b.t2 "Semana 5"  @(32,674.7) 111.9x24
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - div.mu.sm "3 treinos"  @(32,699.3) 111.9x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - div.rw  @(339,682.1) 22x27.6
            display:flex; align-items:center; gap:12px
          - span.chv  @(339,682.1) 22x27.6
              transition:transform 0.2s
            - svg.ic  @(339,682.1) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-chev"></use></svg>
  - nav.nav  @(13,776) 367x64
      display:flex; align-items:center; flex-shrink:0; padding:0px 6px; margin:0px 12px 10px; background-color:rgb(16, 18, 13); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px
    - button.ni  @(20,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; text-align:center
      - svg.ic  @(44.3,789) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-home"></use></svg>
      - span "Hoje"  @(43.3,813) 24.1x14
    - button.ni.on  @(90.6,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(163, 181, 101); font-size:11px; font-weight:500; text-align:center
      - svg.ic  @(114.9,789) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-cal"></use></svg>
      - span "Plano"  @(111.2,813) 29.3x14
    - div.ni.cn  @(161.2,777) 70.6x62
        display:grid; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; grid-template-columns:48px
      - button.cb aria="Menu rápido"  @(172.5,784) 48x48
          display:grid; position:relative; align-items:center; background-image:conic-gradient(from 37.1928deg, rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:16px; box-shadow:rgba(163, 181, 101, 0.075) 0px 0px 16px 2px; text-align:center; transition:transform 0.18s; animation:5s linear infinite spin, 3.2s ease-in-out infinite glow; grid-template-columns:48px
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
  - div#pz.sheet  @(1,875.6) 391x512.8
      display:flex; position:absolute; flex-direction:column; gap:12px; padding:10px 16px 24px; background-color:rgb(16, 18, 13); border-radius:28px 28px 0px 0px; transform:matrix(1, 0, 0, 1, 0, 538.437); z-index:21
    - div#pz.sheet::before  size 40pxx4px (pseudo; inset auto)
        background-color:rgba(236, 232, 218, 0.18); border-radius:2px
        content:""
    - h2.t2 "Gerenciar plano"  @(18,902.6) 357x23
        font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
    - p.mu.sm "O que você deseja fazer com o seu plano de treino?"  @(18,937.6) 357x16.8
        color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - button.card.rw  @(18,966.4) 357x70
        display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.ico  @(33,981.4) 40x40
          display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
        - svg.ic  @(42,990.4) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-cal"></use></svg>
      - b "Trocar de plano"  @(85,991.6) 106.5x19.6
          font-weight:700
    - button.card.rw  @(18,1048.4) 357x70
        display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.ico  @(33,1063.4) 40x40
          display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
        - svg.ic  @(42,1072.4) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-clock"></use></svg>
      - b "Preciso de uma pausa"  @(85,1073.6) 149.7x19.6
          font-weight:700
    - button.card.rw  @(18,1130.4) 357x70
        display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.ico  @(33,1145.4) 40x40
          display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
        - svg.ic  @(42,1154.4) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-walk"></use></svg>
      - b "Marcar férias"  @(85,1155.6) 91.2x19.6
          font-weight:700
    - button.card.rw  @(18,1212.4) 357x70
        display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.ico  @(33,1227.4) 40x40
          display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; grid-template-columns:40px
        - svg.ic  @(42,1236.4) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-edit"></use></svg>
      - b "Alterar data final"  @(85,1237.6) 114.2x19.6
          font-weight:700
    - button.card.rw  @(18,1294.4) 357x70
        display:flex; position:relative; align-items:center; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(209, 43, 43, 0.45); border-radius:18px
      - span.ico  @(33,1309.4) 40x40
          display:grid; align-items:center; flex-shrink:0; color:rgb(240, 128, 124); background-color:rgba(209, 43, 43, 0.15); border-radius:12px; grid-template-columns:40px
        - svg.ic  @(42,1318.4) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-trash"></use></svg>
      - b "Excluir plano"  @(85,1319.6) 88x19.6
          color:rgb(240, 128, 124); font-weight:700
