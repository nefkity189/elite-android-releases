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

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - div.scr  @(1,1) 391x775
      display:flex; flex-direction:column; flex-grow:1; gap:12px; padding:22px 16px 0px
    - div.rw.b  @(17,23) 359x44.3
        display:flex; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px
      - div  @(17,23) 242.5x44.3
        - div.lb "sábado, 21 de novembro"  @(17,23) 242.5x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - h1.t1.gr "Boa tarde, Alex!"  @(17,41) 242.5x26.3
            margin:4px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:25px; font-weight:700; line-height:26.25px; letter-spacing:0.75px; text-transform:uppercase
      - span.chip.on "4 dias"  @(285.1,30.1) 90.9x30
          display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12.5px; line-height:17.5px
        - svg.ic  @(298.1,34.1) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-flame"></use></svg>
    - div.wk  @(17,79.3) 359x58
        display:grid; flex-shrink:0; gap:6px; text-align:center; grid-template-columns:46.1406px 46.1406px 46.1406px 46.1406px 46.1406px 46.1406px 46.1406px
      - div "Seg"  @(17,79.3) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
        - b "16"  @(30.5,106.4) 19.1x21
            color:rgb(236, 232, 218); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:600
      - div.dn "Ter"  @(69.1,79.3) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
        - b "17"  @(83.2,94.5) 18x21
            color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:600
        - svg.ic  @(81.2,117.5) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-check"></use></svg>
      - div "Qua"  @(121.3,79.3) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
        - b "18"  @(134.8,106.4) 19.1x21
            color:rgb(236, 232, 218); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:600
      - div.dn "Qui"  @(173.4,79.3) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
        - b "19"  @(186.9,94.5) 19.1x21
            color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:600
        - svg.ic  @(185.5,117.5) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-check"></use></svg>
      - div "Sex"  @(225.6,79.3) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
        - b "20"  @(237.1,106.4) 23.1x21
            color:rgb(236, 232, 218); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:600
      - div.td "Sáb"  @(277.7,79.3) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-size:11px; line-height:15.4px
        - b "21"  @(291.4,106.4) 18.8x21
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:600
      - div "Dom"  @(329.8,79.3) 46.1x58
          display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:11px; line-height:15.4px
        - b "22"  @(341.7,106.4) 22.4x21
            color:rgb(236, 232, 218); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:600
    - div#aj.card  @(17,149.3) 359x151.2
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(201, 162, 75, 0.45); border-radius:18px
      - div.rw.b  @(32,164.3) 329x27.6
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - div.rw.br  @(32,167) 175.2x22
            display:flex; align-items:center; gap:12px; color:rgb(201, 162, 75)
          - svg.ic  @(32,167) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-bolt"></use></svg>
          - b "Seu plano se ajustou"  @(66,168.3) 141.2x19.6
              font-weight:700
        - button.mu aria="Ocultar"  @(339,164.3) 22x27.6
            color:rgb(143, 147, 132); text-align:center
          - svg.ic  @(339,164.3) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-x"></use></svg>
      - p.mu.sm "Você pulou dois treinos: o plano volta algumas etapas para o retorno ser gradual."  @(32,197.8) 329x33.6
          margin:6px 0px 10px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - div.rw  @(32,241.4) 329x44
          display:flex; align-items:center; gap:12px
        - button.btn.gh.sm2 "Por quê?"  @(32,241.4) 96.5x44
            display:flex; justify-content:center; align-items:center; gap:10px; padding:0px 16px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
        - button.btn.gh.sm2 "Desfazer"  @(140.5,241.4) 97.8x44
            display:flex; justify-content:center; align-items:center; gap:10px; padding:0px 16px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
    - div.card  @(17,312.4) 359x351.6
        display:flex; position:relative; flex-direction:column; flex-grow:1; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.rw.b  @(32,327.4) 329x18
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - span.lb "Semana 3 de 9"  @(32,329.4) 102.4x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - span.tag "Concluído"  @(275.2,327.4) 85.8x18
            padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
      - div  @(32,357.4) 329x59.4
        - span.lb.ol "Treino de hoje"  @(32,360.4) 109.3x14
            color:rgb(163, 181, 101); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - h2.t1 "Treino 2"  @(32,379) 329x37.8
            margin:2px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:36px; font-weight:700; line-height:37.8px; letter-spacing:1.08px; text-transform:uppercase
      - div.rw  @(32,428.8) 329x56
          display:flex; align-items:center; gap:10px
        - span.ico  @(32,428.8) 56x56
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:50%; grid-template-columns:56px
          - svg.ic  @(49,445.8) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-check"></use></svg>
        - div  @(98,436.6) 248.1x40.4
          - b.t2 "Treino concluído"  @(98,435.6) 213.7x24
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - div.mu.sm "Próximo treino: terça-feira, 24 de novembro"  @(98,460.2) 248.1x16.8
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - div.g2  @(32,496.8) 329x86
          display:grid; gap:10px; grid-template-columns:159.5px 159.5px
        - div.card  @(32,496.8) 159.5x86
            position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
          - div.lb "Distância"  @(47,511.8) 129.5x14
              color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
          - b.num "2,40 km"  @(47,528.8) 118.4x36
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:600; line-height:42px; font-variant-numeric:tabular-nums
        - div.card  @(201.5,496.8) 159.5x86
            position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
          - div.lb "Ritmo médio"  @(216.5,511.8) 129.5x14
              color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
          - b.num "6:05"  @(216.5,528.8) 66.3x36
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:600; line-height:42px; font-variant-numeric:tabular-nums
      - div.sp  @(32,594.8) 329x0
          flex-grow:1
      - button.btn.gh "Ver detalhes"  @(32,606.8) 329x42.2
          display:flex; justify-content:center; align-items:center; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
    - div.g2  @(17,676) 359x100
        display:grid; flex-shrink:0; gap:10px; grid-template-columns:174.5px 174.5px
      - div.card  @(17,676) 174.5x100
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.lb "Treinos esta semana"  @(32,691) 144.5x28
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - b.num "2/3"  @(32,722) 50.2x36
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:600; line-height:42px; font-variant-numeric:tabular-nums
      - div.card  @(201.5,676) 174.5x100
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.lb "Em movimento"  @(216.5,691) 144.5x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - b.num "71 min"  @(216.5,708) 100.1x36
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:600; line-height:42px; font-variant-numeric:tabular-nums
  - nav.nav  @(13,776) 367x64
      display:flex; align-items:center; flex-shrink:0; padding:0px 6px; margin:0px 12px 10px; background-color:rgb(16, 18, 13); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px
    - button.ni.on  @(20,777) 70.6x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; color:rgb(163, 181, 101); font-size:11px; font-weight:500; text-align:center
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
          display:grid; position:relative; align-items:center; background-image:conic-gradient(from 33.5952deg, rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:16px; box-shadow:rgba(163, 181, 101, 0.063) 0px 0px 16px 2px; text-align:center; transition:transform 0.18s; animation:5s linear infinite spin, 3.2s ease-in-out infinite glow; grid-template-columns:48px
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
