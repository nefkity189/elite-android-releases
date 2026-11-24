# Layout spec (dp = CSS px)

Screen: 393 x 5296.2 dp

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
- Saira Stencil One  loaded=True
- monospace  loaded=False

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
- body  @(0,0) 393x5296.2
    padding:0px 0px 40px; color:rgb(236, 232, 218); background-color:rgb(5, 6, 4); font-family:Inter, system-ui, sans-serif; font-size:14px; line-height:19.6px
  - svg  @(0,0) 0x0
      position:absolute; overflow:hidden
      svg: <svg width="0" height="0" style="position:absolute" aria-hidden="true"><symbol id="i-home" viewBox="0 0 24 24"><path d="M3 11l9-8 9 8v9a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1z"></path></symbol><symbol id="i-cal" viewBox="0 0 24 24"><rect x="3" y="5" width="18" height="16" rx="2"></rect><path d="M3 10h18M8 3v4M16 3v4"></path></symbol><symbol id="i-trend" viewBox="0 0 24 24"><path d="M3 17l6-6 4 4 8-9"></path><path d="M15 6h6v6"></path></symbol><symbol id="i-user" viewBox="0 0 24 24"><circle cx="12" cy="8" r="4"></circle><path d="M4 21c1-4 4-6 8-6s7 2 8 6"></path></symbol><symbol id="i-gear" viewBox="0 0 24 24"><circle cx="12" cy="12" r="3"></circle><path d="M19.4 15a1.7 1.7 0 0 0 .3 1.8l.1.1a2 2 0 1 1-2.8 2.8l-.1-.1a1.7 1.7 0 0 0-1.8-.3 1.7 1.7 0 0 0-1 1.5V21a2 2 0 1 1-4 0v-.1a1.7 1.7 0 0 0-1.1-1.5 1.7 1.7 0 0 0-1.8.3l-.1.1a2 2 0 1 1-2.8-2.8l.1-.1a1.7 1.7 0 0 0 .3-1.8 1.7 1.7 0 0 0-1.5-1H3a2 2 0 1 1 0-4h.1a1.7 1.7 0 0 0 1.5-1.1 1.7 1.7 0 0 0-.3-1.8l-.1-.1a2 2 0 1 1 2.8-2.8l.1.1a1.7 1.7 0 0 0 1.8.3H9a1.7 1.7 0 0 0 1-1.5V3a2 2 0 1 1 4 0v.1a1.7 1.7 0 0 0 1 1.5 1.7 1.7 0 0 0 1.8-.3l.1-.1a2 2 0 1 1 2.8 2.8l-.1.1a1.7 1.7 0 0 0-.3 1.8V9a1.7 1.7 0 0 0 1.5 1H21a2 2 0 1 1 0 4h-.1a1.7 1.7 0 0 0-1.5 1z"></path></symbol><symbol id="i-sound" viewBox="0 0 24 24"><path d="M11 5 6 9H3v6h3l5 4z"></path><path d="M15.5 8.5a5 5 0 0 1 0 7"></path></symbol><symbol id="i-vib" viewBox="0 0 24 24"><rect x="8" y="4" width="8" height="16" rx="2"></rect><path d="M4 8v8M20 8v8"></path></symbol><symbol id=
  - header.hd1  @(0,0) 393x155.4
      display:flex; align-items:center; gap:14px; padding:16px; margin:0px 0px 20px
    - h1 "Elite · Marca e tema"  @(16,16) 234.9x31
        font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:26px; font-weight:700; letter-spacing:1.56px; text-transform:uppercase
    - p "Identidade, paleta, tipografia, botão central animado e componentes. Tema escuro profissional inspirado em operações táticas (preto, oliva, osso e vermelho de sinalização)."  @(16,61) 361x78.4
        color:rgb(143, 147, 132)
  - div.sec  @(0,175.4) 393x1253.2
      margin:0px 0px 36px
    - h3 "1 · Marca"  @(0,175.4) 393x31
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - div.grid  @(0,220.4) 393x1129
        display:grid; gap:14px; grid-template-columns:393px
      - div.tile  @(0,220.4) 393x170
          display:flex; justify-content:center; align-items:center; padding:24px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.lg  @(62.7,280.4) 267.6x50
            display:flex; align-items:center; gap:12px
          - svg  @(62.7,280.4) 44x50
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(118.7,283.9) 211.6x43
            - div.wm "Elite"  @(118.7,283.9) 217.8x26
                margin:0px -6.24px 0px 0px; font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:26px; line-height:26px; letter-spacing:6.24px; text-transform:uppercase
            - div.tg "Missão dada é missão cumprida"  @(118.7,315.9) 211.6x11
                margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:9px; font-weight:600; letter-spacing:1.8px; text-transform:uppercase
      - div.tile  @(0,404.4) 393x197
          display:flex; justify-content:center; align-items:center; padding:24px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.lg.v  @(90.7,429.4) 211.6x147
            display:flex; flex-direction:column; align-items:center; gap:10px; text-align:center
          - svg  @(160.5,429.4) 72x82
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(90.7,521.4) 211.6x55
            - div.wm "Elite"  @(90.7,521.4) 220.7x38
                margin:0px -9.12px 0px 0px; font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:38px; line-height:38px; letter-spacing:9.12px; text-transform:uppercase
            - div.tg "Missão dada é missão cumprida"  @(90.7,565.4) 211.6x11
                margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:9px; font-weight:600; letter-spacing:1.8px; text-transform:uppercase
      - div.tile.ol  @(0,615.4) 393x170
          display:flex; justify-content:center; align-items:center; padding:24px; color:rgb(163, 181, 101); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:18px
        - div.lg  @(121.1,675.4) 150.8x50
            display:flex; align-items:center; gap:12px
          - svg  @(121.1,675.4) 44x50
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(177.1,687.4) 94.8x26
            - div.wm "Elite"  @(177.1,687.4) 101x26
                margin:0px -6.24px 0px 0px; color:rgb(10, 11, 9); font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:26px; line-height:26px; letter-spacing:6.24px; text-transform:uppercase
      - div.tile.bn  @(0,799.4) 393x170
          display:flex; justify-content:center; align-items:center; padding:24px; background-color:rgb(236, 232, 218); border:1px solid rgba(0, 0, 0, 0); border-radius:18px
        - div.lg  @(121.1,859.4) 150.8x50
            display:flex; align-items:center; gap:12px
          - svg  @(121.1,859.4) 44x50
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(177.1,871.4) 94.8x26
            - div.wm "Elite"  @(177.1,871.4) 101x26
                margin:0px -6.24px 0px 0px; color:rgb(10, 11, 9); font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:26px; line-height:26px; letter-spacing:6.24px; text-transform:uppercase
      - div.tile  @(0,983.4) 393x182
          display:flex; justify-content:center; align-items:center; padding:24px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.ai  @(130.5,1008.4) 132x132
            display:grid; align-items:center; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(10, 11, 9) 72%); border:2px solid rgb(93, 107, 52); border-radius:30px; grid-template-columns:128px
          - svg  @(157.5,1030.4) 78x88
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
      - div.tile  @(0,1179.4) 393x170
          display:flex; justify-content:center; align-items:center; padding:24px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.demo  @(76.5,1228.4) 240x72
            display:flex; align-items:center; gap:18px
          - button.cb aria="Botão central animado"  @(76.5,1228.4) 72x72
              display:grid; position:relative; align-items:center; background-image:conic-gradient(from 46.7928deg, rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:22px; box-shadow:rgba(163, 181, 101, 0.118) 0px 0px 16px 2px; text-align:center; transition:transform 0.18s; animation:5s linear infinite spin, 3.2s ease-in-out infinite glow; grid-template-columns:72px
            - button.cb::before  size 68pxx68px (pseudo; inset 2px)
                position:absolute; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(12, 14, 8) 70%); border-radius:20px
                content:""
            - button.cb::after  size 72pxx72px (pseudo; inset 0px)
                position:absolute; border:2px solid rgb(163, 181, 101); border-radius:16px; opacity:0
                content:""
            - svg  @(93.5,1245.4) 38x38
                position:relative; color:rgb(163, 181, 101); overflow:hidden
                svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#blade"></use></svg>
          - div.mu.sm "Toque no botão: pulo, giro do símbolo e onda."  @(166.5,1247.6) 150x33.6
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - p.mu.sm "Símbolo: escudo com uma lâmina em forma de “E”; a ponta da lâmina é vermelha. Logotipo: Saira Stencil One, caixa alta, espaçamento de 0,24 em. O botão central do menu : é a lâmina “E” (símbolo sem o escudo)."  @(0,1361.4) 393x67.2
        margin:12px 0px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - b "não é mais o “A”"  @(0,1395) 367.8x31.8
          font-weight:700
  - div.sec  @(0,1464.6) 393x1460.8
      margin:0px 0px 36px
    - h3 "2 · Paleta"  @(0,1464.6) 393x31
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - div.grid  @(0,1509.6) 393x1415.8
        display:grid; gap:14px; grid-template-columns:393px
      - div.sw2  @(0,1509.6) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,1510.6) 391x72
            background-color:rgb(10, 11, 9)
        - div "Fundo das telas"  @(1,1582.6) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Preto tático #0A0B09"  @(13,1592.6) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,1652.5) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,1653.5) 391x72
            background-color:rgb(16, 18, 13)
        - div "Barra inferior, sheets"  @(1,1725.5) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Superfície #10120D"  @(13,1735.5) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,1795.5) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,1796.5) 391x72
            background-color:rgb(22, 25, 17)
        - div "Cards, trilhas"  @(1,1868.5) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Card #161911 / #1D2116"  @(13,1878.5) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,1938.5) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,1939.5) 391x72
            background-color:rgb(236, 232, 218)
        - div "Texto principal"  @(1,2011.5) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Osso #ECE8DA"  @(13,2021.5) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,2081.5) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,2082.5) 391x72
            background-color:rgb(143, 147, 132)
        - div "Texto secundário"  @(1,2154.5) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Cinza oliva #8F9384"  @(13,2164.5) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,2224.5) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,2225.5) 391x72
            background-color:rgb(163, 181, 101)
        - div "Seleção, Pausar, abas ativas"  @(1,2297.5) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Oliva #A3B565"  @(13,2307.5) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,2367.5) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,2368.5) 391x72
            background-color:rgb(93, 107, 52)
        - div "Switch ligado, bordas"  @(1,2440.5) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Oliva escuro #5D6B34"  @(13,2450.5) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,2510.5) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,2511.5) 391x72
            background-color:rgb(209, 43, 43)
        - div "Ação principal, Parar"  @(1,2583.5) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Vermelho operacional #D12B2B"  @(13,2593.5) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,2653.4) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,2654.4) 391x72
            background-color:rgb(201, 162, 75)
        - div "Conquistas e patentes"  @(1,2726.4) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Latão #C9A24B"  @(13,2736.4) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(0,2796.4) 393x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(1,2797.4) 391x72
            background-color:rgb(224, 169, 58)
        - div "Avisos"  @(1,2869.4) 391x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Âmbar #E0A93A"  @(13,2879.4) 367x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
  - div.sec  @(0,2961.4) 393x655.4
      margin:0px 0px 36px
    - h3 "3 · Mapa de tokens (app atual → Elite)"  @(0,2961.4) 393x31
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - table  @(0,3006.4) 393x610.4
        display:table; font-size:13px; line-height:18.2px
      - tbody  @(0,3006.4) 393x609.9
          display:table-row-group
        - tr  @(0,3006.4) 393x32.5
            display:table-row
          - th "Token atual"  @(0,3006.4) 160.1x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Novo valor"  @(160.1,3006.4) 112.3x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Uso"  @(272.5,3006.4) 120.5x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
        - tr  @(0,3038.9) 393x56.6
            display:table-row
          - td "/"  @(0,3038.9) 160.1x56.6
              display:table-cell; padding:9px 10px
            - code "RunnaBackground"  @(10,3051.4) 99x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "AisleBackground"  @(10,3070.2) 99x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#0A0B09"  @(160.1,3038.9) 112.3x56.6
              display:table-cell; padding:9px 10px
          - td "fundo"  @(272.5,3038.9) 120.5x56.6
              display:table-cell; padding:9px 10px
        - tr  @(0,3095.5) 393x55.4
            display:table-row
          - td "(82%)"  @(0,3095.5) 160.1x55.4
              display:table-cell; padding:9px 10px
            - code "RunnaGlass"  @(10,3108) 66x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#10120D a 82%"  @(160.1,3095.5) 112.3x55.4
              display:table-cell; padding:9px 10px
          - td "chips, banner, barra de ações"  @(272.5,3095.5) 120.5x55.4
              display:table-cell; padding:9px 10px
        - tr  @(0,3150.9) 393x55.4
            display:table-row
          - td  @(0,3150.9) 160.1x55.4
              display:table-cell; padding:9px 10px
            - code "RunnaGlassElevated"  @(10,3163.4) 118.8x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#1D2116 a 92%"  @(160.1,3150.9) 112.3x55.4
              display:table-cell; padding:9px 10px
          - td "arco do menu radial"  @(272.5,3150.9) 120.5x55.4
              display:table-cell; padding:9px 10px
        - tr  @(0,3206.3) 393x56.6
            display:table-row
          - td "/"  @(0,3206.3) 160.1x56.6
              display:table-cell; padding:9px 10px
            - code "RunnaTeal"  @(10,3218.8) 59.4x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "AisleTurquoise"  @(10,3237.5) 92.4x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#A3B565 (oliva)"  @(160.1,3206.3) 112.3x56.6
              display:table-cell; padding:9px 10px
          - td "seleção, Pausar, progresso"  @(272.5,3206.3) 120.5x56.6
              display:table-cell; padding:9px 10px
        - tr  @(0,3262.8) 393x55.4
            display:table-row
          - td  @(0,3262.8) 160.1x55.4
              display:table-cell; padding:9px 10px
            - code "RunnaTealOn"  @(10,3275.3) 72.6x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#0E1306"  @(160.1,3262.8) 112.3x55.4
              display:table-cell; padding:9px 10px
          - td "texto sobre oliva"  @(272.5,3262.8) 120.5x55.4
              display:table-cell; padding:9px 10px
        - tr  @(0,3318.2) 393x56.6
            display:table-row
          - td "/"  @(0,3318.2) 160.1x56.6
              display:table-cell; padding:9px 10px
            - code "RunnaOrange"  @(10,3330.7) 72.6x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "AisleActionOrange"  @(10,3349.5) 112.2x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#D12B2B (vermelho)"  @(160.1,3318.2) 112.3x56.6
              display:table-cell; padding:9px 10px
          - td "Começar treino, Parar"  @(272.5,3318.2) 120.5x56.6
              display:table-cell; padding:9px 10px
        - tr  @(0,3374.8) 393x55.4
            display:table-row
          - td  @(0,3374.8) 160.1x55.4
              display:table-cell; padding:9px 10px
            - code "RunnaOrangeOn"  @(10,3387.3) 85.8x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#FFF4F0"  @(160.1,3374.8) 112.3x55.4
              display:table-cell; padding:9px 10px
          - td "texto sobre vermelho"  @(272.5,3374.8) 120.5x55.4
              display:table-cell; padding:9px 10px
        - tr  @(0,3430.2) 393x73.6
            display:table-row
          - td ","  @(0,3430.2) 160.1x73.6
              display:table-cell; padding:9px 10px
            - code "AislePurple"  @(10,3442.7) 72.6x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "BrandGradient"  @(10,3461.5) 85.8x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "removidos; usar oliva sólido"  @(160.1,3430.2) 112.3x73.6
              display:table-cell; padding:9px 10px
          - td "sem gradiente teal→roxo"  @(272.5,3430.2) 120.5x73.6
              display:table-cell; padding:9px 10px
        - tr  @(0,3503.8) 393x56
            display:table-row
          - td "(lilás)"  @(0,3503.8) 160.1x56
              display:table-cell; padding:9px 10px
            - code "AisleTextAccent2"  @(10,3516.3) 105.6x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#C9A24B (latão)"  @(160.1,3503.8) 112.3x56
              display:table-cell; padding:9px 10px
          - td "destaques secundários"  @(272.5,3503.8) 120.5x56
              display:table-cell; padding:9px 10px
        - tr  @(0,3559.7) 393x56.6
            display:table-row
          - td  @(0,3559.7) 160.1x56.6
              display:table-cell; padding:9px 10px
            - code "RunnaTextPrimary / Secondary"  @(10,3572.2) 118.8x32.8
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#ECE8DA / #8F9384"  @(160.1,3559.7) 112.3x56.6
              display:table-cell; padding:9px 10px
          - td "textos"  @(272.5,3559.7) 120.5x56.6
              display:table-cell; padding:9px 10px
  - div.sec.ty  @(0,3652.8) 393x192.6
      margin:0px 0px 36px
    - h3 "4 · Tipografia"  @(0,3652.8) 393x31
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - p "Missão de hoje · 05:42"  @(0,3697.8) 393x38
        margin:0px 0px 10px; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:38px; font-weight:700; line-height:38px; letter-spacing:1.52px; text-transform:uppercase
    - p.mu.sm "Títulos e números: 600/700, caixa alta nos títulos, números tabulares. Logotipo: (só na marca)."  @(0,3745.8) 393x50.4
        margin:0px 0px 10px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - b "Barlow Condensed"  @(108.2,3745.8) 110.8x15
          color:rgb(236, 232, 218); font-weight:700
      - b "Saira Stencil One"  @(210.1,3762.6) 99.6x15
          color:rgb(236, 232, 218); font-weight:700
    - p "Texto e interface: 400/500/600/700. Corpo 14/20, rótulos 11 com 0,12 em de espaçamento em caixa alta."  @(0,3806.2) 393x39.2
        margin:0px 0px 10px
      - b "Inter"  @(119.3,3807.2) 31.7x17
          font-weight:700
  - div.sec  @(0,3881.4) 393x538.4
      margin:0px 0px 36px
    - h3 "5 · Animações"  @(0,3881.4) 393x31
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - table  @(0,3926.4) 393x493.4
        display:table; font-size:13px; line-height:18.2px
      - tbody  @(0,3926.4) 393x492.9
          display:table-row-group
        - tr  @(0,3926.4) 393x32.5
            display:table-row
          - th "Elemento"  @(0,3926.4) 105.3x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Gatilho"  @(105.3,3926.4) 100.7x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Detalhe"  @(205.9,3926.4) 187.1x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
        - tr  @(0,3958.9) 393x73.6
            display:table-row
          - td "Borda do botão central"  @(0,3958.9) 105.3x73.6
              display:table-cell; padding:9px 10px
          - td "repouso"  @(105.3,3958.9) 100.7x73.6
              display:table-cell; padding:9px 10px
          - td "gradiente cônico (oliva, vermelho, latão) girando, 5 s por volta, linear"  @(205.9,3958.9) 187.1x73.6
              display:table-cell; padding:9px 10px
        - tr  @(0,4032.5) 393x55.4
            display:table-row
          - td "Brilho do botão central"  @(0,4032.5) 105.3x55.4
              display:table-cell; padding:9px 10px
          - td "repouso"  @(105.3,4032.5) 100.7x55.4
              display:table-cell; padding:9px 10px
          - td "respira até 35% de oliva, 3,2 s ease-in-out"  @(205.9,4032.5) 187.1x55.4
              display:table-cell; padding:9px 10px
        - tr  @(0,4087.8) 393x73.6
            display:table-row
          - td "Toque no botão central"  @(0,4087.8) 105.3x73.6
              display:table-cell; padding:9px 10px
          - td "tap"  @(105.3,4087.8) 100.7x73.6
              display:table-cell; padding:9px 10px
          - td "pulo elástico 0,5 s, giro do símbolo 0,6 s, onda 0,6 s (escala 1 → 1,7)"  @(205.9,4087.8) 187.1x73.6
              display:table-cell; padding:9px 10px
        - tr  @(0,4161.4) 393x73.6
            display:table-row
          - td "Pulso do ponto no mapa"  @(0,4161.4) 105.3x73.6
              display:table-cell; padding:9px 10px
          - td "repouso"  @(105.3,4161.4) 100.7x73.6
              display:table-cell; padding:9px 10px
          - td "escala 0,6 → 2,2 e opacidade 0,5 → 0, 2 s ease-out, infinito"  @(205.9,4161.4) 187.1x73.6
              display:table-cell; padding:9px 10px
        - tr  @(0,4235) 393x73.6
            display:table-row
          - td "Menu radial"  @(0,4235) 105.3x73.6
              display:table-cell; padding:9px 10px
          - td "abrir"  @(105.3,4235) 100.7x73.6
              display:table-cell; padding:9px 10px
          - td "arco 0,25 s e itens 0,28 s, cubic-bezier(.2,.8,.2,1); botão gira 90°"  @(205.9,4235) 187.1x73.6
              display:table-cell; padding:9px 10px
        - tr  @(0,4308.5) 393x55.4
            display:table-row
          - td "Bottom sheets"  @(0,4308.5) 105.3x55.4
              display:table-cell; padding:9px 10px
          - td "abrir/fechar"  @(105.3,4308.5) 100.7x55.4
              display:table-cell; padding:9px 10px
          - td "sobe 0,3 s; fundo escurece 0,2 s"  @(205.9,4308.5) 187.1x55.4
              display:table-cell; padding:9px 10px
        - tr  @(0,4363.9) 393x55.4
            display:table-row
          - td "Todas"  @(0,4363.9) 105.3x55.4
              display:table-cell; padding:9px 10px
          - td "reduzir movimento"  @(105.3,4363.9) 100.7x55.4
              display:table-cell; padding:9px 10px
          - td "sem animação nem transição"  @(205.9,4363.9) 187.1x55.4
              display:table-cell; padding:9px 10px
  - div.sec  @(0,4455.8) 393x764.4
      margin:0px 0px 36px
    - h3 "6 · Componentes"  @(0,4455.8) 393x31
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - div.grid  @(0,4500.8) 393x719.4
        display:grid; gap:14px; grid-template-columns:393px
      - div.card.cl  @(0,4500.8) 393x236
          display:flex; position:relative; flex-direction:column; gap:8px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.lb "Botões"  @(15,4515.8) 363x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - button.btn.pri "Começar treino"  @(15,4537.8) 363x56
            display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
          - svg.ic.fl  @(120,4556.8) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic fl"><use href="#i-play"></use></svg>
        - button.btn.olv "Pausar"  @(15,4601.8) 363x56
            display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
          - svg.ic.fl  @(154.4,4620.8) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic fl"><use href="#i-pause"></use></svg>
        - button.btn.gh "Agora não"  @(15,4665.8) 363x56
            display:flex; justify-content:center; align-items:center; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
      - div.card.cl  @(0,4750.8) 393x192
          display:flex; position:relative; flex-direction:column; gap:8px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.lb "Chips e tags"  @(15,4765.8) 363x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.rw  @(15,4787.8) 363x60
            display:flex; align-items:center; gap:12px
          - span.chip "Sem sinal"  @(15,4787.8) 96.8x30
              display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12.5px; line-height:17.5px
            - i.dot  @(28,4799.3) 7x7
                background-color:rgb(209, 43, 43); border-radius:50%
          - span.chip.on "Seguindo você"  @(123.8,4787.8) 114.2x30
              display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12.5px; line-height:17.5px
          - span.chip.rd "Dor"  @(250,4787.8) 47.2x30
              display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(240, 128, 124); background-color:rgba(209, 43, 43, 0.15); border:1px solid rgba(209, 43, 43, 0.4); border-radius:15px; font-size:12.5px; line-height:17.5px
          - span.tag "Etapa 3"  @(309.2,4793.8) 64.3x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
          - span.tag.br "Recruta"  @(15,4829.8) 70.2x18
              padding:3px 8px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.15); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
        - span.lb "Seleção"  @(15,4861.8) 363x14
            margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.seg  @(15,4883.8) 363x44
            display:flex; gap:6px
          - div.s "2"  @(15,4883.8) 117x44
              display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:115px
          - div.s.on "3"  @(138,4883.8) 117x44
              display:grid; align-items:center; flex-grow:1; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-weight:600; grid-template-columns:115px
          - div.s "4"  @(261,4883.8) 117x44
              display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:115px
      - div.card.cl  @(0,4956.8) 393x156
          display:flex; position:relative; flex-direction:column; gap:8px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.lb "Switch, progresso, campo"  @(15,4971.8) 363x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.rw.b  @(15,4993.8) 363x30
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - span "Voz do treinador"  @(15,4999) 109.3x19.6
          - button.sw aria="Voz"  @(328,4993.8) 50x30
              position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
            - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
                position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
                content:""
        - div.bar  @(15,5031.8) 363x6
            background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
          - i  @(15,5031.8) 159.7x6
              background-color:rgb(163, 181, 101); border-radius:3px
        - div.fld.f "Seu nome ou apelido"  @(15,5045.8) 363x52
            display:flex; align-items:center; padding:0px 14px; background-color:rgb(13, 15, 10); border:1px solid rgb(163, 181, 101); border-radius:14px
      - div.card.sel  @(0,5126.8) 393x93.4
          position:relative; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
        - div.card.sel::before  size 3pxx63.3906px (pseudo; inset 14px 389px 14px -1px)
            position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
            content:""
        - span.lb "Card selecionado"  @(15,5145.8) 133.9x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.t2 "Primeiros 5K"  @(15,5165.4) 363x23
            margin:4px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - div.mu.sm "9 semanas · 2 ou 3 treinos por semana"  @(15,5188.4) 363x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
