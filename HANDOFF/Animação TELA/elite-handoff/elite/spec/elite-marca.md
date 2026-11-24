# Layout spec (dp = CSS px)

Screen: 1100 x 2505.3 dp

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
- Saira Stencil One  loaded=False
- monospace  loaded=True

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
- body  @(0,0) 1100x2505.3
    padding:24px 16px 48px; color:rgb(236, 232, 218); background-color:rgb(5, 6, 4); font-family:Inter, system-ui, sans-serif; font-size:14px; line-height:19.6px
  - svg  @(16,24) 0x0
      position:absolute; overflow:hidden
      svg: <svg width="0" height="0" style="position:absolute" aria-hidden="true"><symbol id="i-home" viewBox="0 0 24 24"><path d="M3 11l9-8 9 8v9a1 1 0 0 1-1 1h-5v-6H9v6H4a1 1 0 0 1-1-1z"></path></symbol><symbol id="i-cal" viewBox="0 0 24 24"><rect x="3" y="5" width="18" height="16" rx="2"></rect><path d="M3 10h18M8 3v4M16 3v4"></path></symbol><symbol id="i-trend" viewBox="0 0 24 24"><path d="M3 17l6-6 4 4 8-9"></path><path d="M15 6h6v6"></path></symbol><symbol id="i-user" viewBox="0 0 24 24"><circle cx="12" cy="8" r="4"></circle><path d="M4 21c1-4 4-6 8-6s7 2 8 6"></path></symbol><symbol id="i-gear" viewBox="0 0 24 24"><circle cx="12" cy="12" r="3"></circle><path d="M19.4 15a1.7 1.7 0 0 0 .3 1.8l.1.1a2 2 0 1 1-2.8 2.8l-.1-.1a1.7 1.7 0 0 0-1.8-.3 1.7 1.7 0 0 0-1 1.5V21a2 2 0 1 1-4 0v-.1a1.7 1.7 0 0 0-1.1-1.5 1.7 1.7 0 0 0-1.8.3l-.1.1a2 2 0 1 1-2.8-2.8l.1-.1a1.7 1.7 0 0 0 .3-1.8 1.7 1.7 0 0 0-1.5-1H3a2 2 0 1 1 0-4h.1a1.7 1.7 0 0 0 1.5-1.1 1.7 1.7 0 0 0-.3-1.8l-.1-.1a2 2 0 1 1 2.8-2.8l.1.1a1.7 1.7 0 0 0 1.8.3H9a1.7 1.7 0 0 0 1-1.5V3a2 2 0 1 1 4 0v.1a1.7 1.7 0 0 0 1 1.5 1.7 1.7 0 0 0 1.8-.3l.1-.1a2 2 0 1 1 2.8 2.8l-.1.1a1.7 1.7 0 0 0-.3 1.8V9a1.7 1.7 0 0 0 1.5 1H21a2 2 0 1 1 0 4h-.1a1.7 1.7 0 0 0-1.5 1z"></path></symbol><symbol id="i-sound" viewBox="0 0 24 24"><path d="M11 5 6 9H3v6h3l5 4z"></path><path d="M15.5 8.5a5 5 0 0 1 0 7"></path></symbol><symbol id="i-vib" viewBox="0 0 24 24"><rect x="8" y="4" width="8" height="16" rx="2"></rect><path d="M4 8v8M20 8v8"></path></symbol><symbol id=
  - header.hd1  @(16,24) 1068x84.2
      display:flex; align-items:center; gap:14px; margin:0px 0px 20px
    - h1 "Elite · Marca e tema"  @(16,24) 326.7x31
        font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:26px; font-weight:700; letter-spacing:1.56px; text-transform:uppercase
    - p "Identidade, paleta, tipografia, botão central animado e componentes. Tema escuro profissional inspirado em operações táticas (preto, oliva, osso e vermelho de sinalização)."  @(16,69) 1068x39.2
        color:rgb(143, 147, 132)
  - div.sec  @(16,128.2) 1068x449.6
      margin:0px 0px 36px
    - h3 "1 · Marca"  @(16,128.2) 1068x30
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - div.grid  @(16,172.2) 1068x360
        display:grid; gap:14px; grid-template-columns:256.5px 256.5px 256.5px 256.5px
      - div.tile  @(16,172.2) 256.5x176
          display:flex; justify-content:center; align-items:center; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.lg  @(29.3,237.2) 229.8x46
            display:flex; align-items:center; gap:12px
          - svg  @(29.3,237.2) 46x46
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(87.3,239.2) 171.8x42
            - div.wm "Elite"  @(87.3,239.2) 178x26
                margin:0px -6.24px 0px 0px; font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:26px; line-height:26px; letter-spacing:6.24px; text-transform:uppercase
            - div.tg "Missão dada é missão cumprida"  @(87.3,271.2) 171.8x10
                margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:8px; font-weight:600; letter-spacing:1.04px; text-transform:uppercase
      - div.tile  @(286.5,172.2) 256.5x176
          display:flex; justify-content:center; align-items:center; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.lg.v  @(328.8,189.2) 171.8x142
            display:flex; flex-direction:column; align-items:center; gap:10px; text-align:center
          - svg  @(375.8,189.2) 78x78
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(328.8,277.2) 171.8x54
            - div.wm "Elite"  @(328.8,277.2) 180.9x38
                margin:0px -9.12px 0px 0px; font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:38px; line-height:38px; letter-spacing:9.12px; text-transform:uppercase
            - div.tg "Missão dada é missão cumprida"  @(328.8,321.2) 171.8x10
                margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:8px; font-weight:600; letter-spacing:1.04px; text-transform:uppercase
      - div.tile.ol  @(557,172.2) 256.5x176
          display:flex; justify-content:center; align-items:center; padding:16px; color:rgb(163, 181, 101); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:18px
        - div.lg  @(607.6,237.2) 155.2x46
            display:flex; align-items:center; gap:12px
          - svg  @(607.6,237.2) 46x46
              color:rgb(10, 11, 9); overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(665.6,247.2) 97.2x26
            - div.wm "Elite"  @(665.6,247.2) 103.5x26
                margin:0px -6.24px 0px 0px; color:rgb(10, 11, 9); font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:26px; line-height:26px; letter-spacing:6.24px; text-transform:uppercase
      - div.tile.bn  @(827.5,172.2) 256.5x176
          display:flex; justify-content:center; align-items:center; padding:16px; background-color:rgb(236, 232, 218); border:1px solid rgba(0, 0, 0, 0); border-radius:18px
        - div.lg  @(878.1,237.2) 155.2x46
            display:flex; align-items:center; gap:12px
          - svg  @(878.1,237.2) 46x46
              color:rgb(10, 11, 9); overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
          - div  @(936.1,247.2) 97.2x26
            - div.wm "Elite"  @(936.1,247.2) 103.5x26
                margin:0px -6.24px 0px 0px; color:rgb(10, 11, 9); font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:26px; line-height:26px; letter-spacing:6.24px; text-transform:uppercase
      - div.tile  @(16,362.2) 256.5x170
          display:flex; justify-content:center; align-items:center; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.ai  @(78.3,381.2) 132x132
            display:grid; align-items:center; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(10, 11, 9) 72%); border:2px solid rgb(93, 107, 52); border-radius:30px; grid-template-columns:128px
          - svg  @(102.3,405.2) 84x84
              overflow:hidden
              svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#mark"></use></svg>
      - div.tile  @(286.5,362.2) 256.5x170
          display:flex; justify-content:center; align-items:center; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.demo  @(303.5,385.4) 222.5x123.6
            display:flex; align-items:center; gap:18px
          - button.cb aria="Botão central animado"  @(303.5,385.4) 72x72
              display:grid; position:relative; align-items:center; background-image:conic-gradient(from 37.2024deg, rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:22px; box-shadow:rgba(163, 181, 101, 0.075) 0px 0px 16px 2px; text-align:center; transition:transform 0.18s; animation:5s linear infinite spin, 3.2s ease-in-out infinite glow; grid-template-columns:72px
            - button.cb::before  size 68pxx68px (pseudo; inset 2px)
                position:absolute; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(12, 14, 8) 70%); border-radius:20px
                content:""
            - button.cb::after  size 72pxx72px (pseudo; inset 0px)
                position:absolute; border:2px solid rgb(163, 181, 101); border-radius:16px; opacity:0
                content:""
            - svg  @(320.5,402.4) 38x38
                position:relative; color:rgb(163, 181, 101); overflow:hidden
                svg: <svg viewBox="0 0 64 64" aria-hidden="true"><use href="#blade"></use></svg>
          - div.mu.sm "Toque no botão: pulo, giro do símbolo e onda."  @(303.5,475.4) 150x33.6
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - p.mu.sm "Símbolo: um “E” inclinado para frente, com três barras de velocidade; a barra do meio é vermelha. Sem escudo, para não lembrar empresa de segurança. Logotipo: Saira Stencil One, caixa alta, espaçamento de 0,24 em. O botão central do menu : é o mesmo “E” inclinado, em oliva."  @(16,544.2) 1068x33.6
        margin:12px 0px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - b "não é mais o “A”"  @(338.4,561) 95x15
          font-weight:700
  - div.sec  @(16,613.8) 1068x459
      margin:0px 0px 36px
    - h3 "2 · Paleta"  @(16,613.8) 1068x30
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - div.grid  @(16,657.8) 1068x415
        display:grid; gap:14px; grid-template-columns:256.5px 256.5px 256.5px 256.5px
      - div.sw2  @(16,657.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(17,658.8) 254.5x72
            background-color:rgb(10, 11, 9)
        - div "Fundo das telas"  @(17,730.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Preto tático #0A0B09"  @(29,740.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(286.5,657.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(287.5,658.8) 254.5x72
            background-color:rgb(16, 18, 13)
        - div "Barra inferior, sheets"  @(287.5,730.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Superfície #10120D"  @(299.5,740.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(557,657.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(558,658.8) 254.5x72
            background-color:rgb(22, 25, 17)
        - div "Cards, trilhas"  @(558,730.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Card #161911 / #1D2116"  @(570,740.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(827.5,657.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(828.5,658.8) 254.5x72
            background-color:rgb(236, 232, 218)
        - div "Texto principal"  @(828.5,730.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Osso #ECE8DA"  @(840.5,740.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(16,800.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(17,801.8) 254.5x72
            background-color:rgb(143, 147, 132)
        - div "Texto secundário"  @(17,873.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Cinza oliva #8F9384"  @(29,883.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(286.5,800.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(287.5,801.8) 254.5x72
            background-color:rgb(163, 181, 101)
        - div "Seleção, Pausar, abas ativas"  @(287.5,873.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Oliva #A3B565"  @(299.5,883.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(557,800.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(558,801.8) 254.5x72
            background-color:rgb(93, 107, 52)
        - div "Switch ligado, bordas"  @(558,873.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Oliva escuro #5D6B34"  @(570,883.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(827.5,800.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(828.5,801.8) 254.5x72
            background-color:rgb(209, 43, 43)
        - div "Ação principal, Parar"  @(828.5,873.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Vermelho operacional #D12B2B"  @(840.5,883.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(16,943.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(17,944.8) 254.5x72
            background-color:rgb(201, 162, 75)
        - div "Conquistas e patentes"  @(17,1016.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Latão #C9A24B"  @(29,1026.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
      - div.sw2  @(286.5,943.8) 256.5x129
          background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; overflow:hidden
        - i  @(287.5,944.8) 254.5x72
            background-color:rgb(224, 169, 58)
        - div "Avisos"  @(287.5,1016.8) 254.5x55
            padding:10px 12px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
          - b "Âmbar #E0A93A"  @(299.5,1026.8) 230.5x18.2
              color:rgb(236, 232, 218); font-size:13px; font-weight:700; line-height:18.2px
  - div.sec  @(16,1108.7) 1068x448.9
      margin:0px 0px 36px
    - h3 "3 · Mapa de tokens (app atual → Elite)"  @(16,1108.7) 1068x30
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - table  @(16,1152.7) 1068x404.9
        display:table; font-size:13px; line-height:18.2px
      - tbody  @(16,1152.7) 1068x404.4
          display:table-row-group
        - tr  @(16,1152.7) 1068x32.5
            display:table-row
          - th "Token atual"  @(16,1152.7) 413.3x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Novo valor"  @(429.3,1152.7) 317.7x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Uso"  @(747,1152.7) 337x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
        - tr  @(16,1185.2) 1068x37.2
            display:table-row
          - td "/"  @(16,1185.2) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaBackground"  @(26,1196.7) 108.4x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "AisleBackground"  @(146.4,1196.7) 108.4x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#0A0B09"  @(429.3,1185.2) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "fundo"  @(747,1185.2) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1222.4) 1068x37.2
            display:table-row
          - td "(82%)"  @(16,1222.4) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaGlass"  @(26,1233.9) 72.3x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#10120D a 82%"  @(429.3,1222.4) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "chips, banner, barra de ações"  @(747,1222.4) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1259.6) 1068x37.2
            display:table-row
          - td  @(16,1259.6) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaGlassElevated"  @(26,1271.1) 130x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#1D2116 a 92%"  @(429.3,1259.6) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "arco do menu radial"  @(747,1259.6) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1296.8) 1068x37.2
            display:table-row
          - td "/"  @(16,1296.8) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaTeal"  @(26,1308.3) 65x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "AisleTurquoise"  @(103,1308.3) 101.2x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#A3B565 (oliva)"  @(429.3,1296.8) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "seleção, Pausar, progresso"  @(747,1296.8) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1334) 1068x37.2
            display:table-row
          - td  @(16,1334) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaTealOn"  @(26,1345.5) 79.5x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#0E1306"  @(429.3,1334) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "texto sobre oliva"  @(747,1334) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1371.2) 1068x37.2
            display:table-row
          - td "/"  @(16,1371.2) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaOrange"  @(26,1382.7) 79.5x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "AisleActionOrange"  @(117.5,1382.7) 122.8x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#D12B2B (vermelho)"  @(429.3,1371.2) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "Começar treino, Parar"  @(747,1371.2) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1408.4) 1068x37.2
            display:table-row
          - td  @(16,1408.4) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaOrangeOn"  @(26,1419.9) 93.9x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#FFF4F0"  @(429.3,1408.4) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "texto sobre vermelho"  @(747,1408.4) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1445.5) 1068x37.2
            display:table-row
          - td ","  @(16,1445.5) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "AislePurple"  @(26,1457) 79.5x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
            - code "BrandGradient"  @(112.9,1457) 93.9x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "removidos; usar oliva sólido"  @(429.3,1445.5) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "sem gradiente teal→roxo"  @(747,1445.5) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1482.7) 1068x37.2
            display:table-row
          - td "(lilás)"  @(16,1482.7) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "AisleTextAccent2"  @(26,1494.2) 115.6x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#C9A24B (latão)"  @(429.3,1482.7) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "destaques secundários"  @(747,1482.7) 337x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1519.9) 1068x37.2
            display:table-row
          - td  @(16,1519.9) 413.3x37.2
              display:table-cell; padding:9px 10px
            - code "RunnaTextPrimary / Secondary"  @(26,1531.4) 202.3x14
                color:rgb(163, 181, 101); font-family:monospace; font-size:12px; line-height:16.8px
          - td "#ECE8DA / #8F9384"  @(429.3,1519.9) 317.7x37.2
              display:table-cell; padding:9px 10px
          - td "textos"  @(747,1519.9) 337x37.2
              display:table-cell; padding:9px 10px
  - div.sec.ty  @(16,1593.6) 1068x138.4
      margin:0px 0px 36px
    - h3 "4 · Tipografia"  @(16,1593.6) 1068x30
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - p "Missão de hoje · 05:42"  @(16,1637.6) 1068x38
        margin:0px 0px 10px; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:38px; font-weight:700; line-height:38px; letter-spacing:1.52px; text-transform:uppercase
    - p.mu.sm "Títulos e números: 600/700, caixa alta nos títulos, números tabulares. Logotipo: (só na marca)."  @(16,1685.6) 1068x16.8
        margin:0px 0px 10px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - b "Barlow Condensed"  @(124.2,1685.6) 110.8x15
          color:rgb(236, 232, 218); font-weight:700
      - b "Saira Stencil One"  @(584.9,1685.6) 99.6x15
          color:rgb(236, 232, 218); font-weight:700
    - p "Texto e interface: 400/500/600/700. Corpo 14/20, rótulos 11 com 0,12 em de espaçamento em caixa alta."  @(16,1712.4) 1068x19.6
        margin:0px 0px 10px
      - b "Inter"  @(135.3,1713.4) 31.7x17
          font-weight:700
  - div.sec  @(16,1768) 1068x337.3
      margin:0px 0px 36px
    - h3 "5 · Animações"  @(16,1768) 1068x30
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - table  @(16,1812) 1068x293.3
        display:table; font-size:13px; line-height:18.2px
      - tbody  @(16,1812) 1068x292.8
          display:table-row-group
        - tr  @(16,1812) 1068x32.5
            display:table-row
          - th "Elemento"  @(16,1812) 242.2x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Gatilho"  @(258.2,1812) 193x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
          - th "Detalhe"  @(451.1,1812) 632.9x32.5
              display:table-cell; padding:9px 10px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.1px; text-transform:uppercase
        - tr  @(16,1844.5) 1068x37.2
            display:table-row
          - td "Borda do botão central"  @(16,1844.5) 242.2x37.2
              display:table-cell; padding:9px 10px
          - td "repouso"  @(258.2,1844.5) 193x37.2
              display:table-cell; padding:9px 10px
          - td "gradiente cônico (oliva, vermelho, latão) girando, 5 s por volta, linear"  @(451.1,1844.5) 632.9x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1881.7) 1068x37.2
            display:table-row
          - td "Brilho do botão central"  @(16,1881.7) 242.2x37.2
              display:table-cell; padding:9px 10px
          - td "repouso"  @(258.2,1881.7) 193x37.2
              display:table-cell; padding:9px 10px
          - td "respira até 35% de oliva, 3,2 s ease-in-out"  @(451.1,1881.7) 632.9x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1918.9) 1068x37.2
            display:table-row
          - td "Toque no botão central"  @(16,1918.9) 242.2x37.2
              display:table-cell; padding:9px 10px
          - td "tap"  @(258.2,1918.9) 193x37.2
              display:table-cell; padding:9px 10px
          - td "pulo elástico 0,5 s, giro do símbolo 0,6 s, onda 0,6 s (escala 1 → 1,7)"  @(451.1,1918.9) 632.9x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1956.1) 1068x37.2
            display:table-row
          - td "Pulso do ponto no mapa"  @(16,1956.1) 242.2x37.2
              display:table-cell; padding:9px 10px
          - td "repouso"  @(258.2,1956.1) 193x37.2
              display:table-cell; padding:9px 10px
          - td "escala 0,6 → 2,2 e opacidade 0,5 → 0, 2 s ease-out, infinito"  @(451.1,1956.1) 632.9x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,1993.3) 1068x37.2
            display:table-row
          - td "Menu radial"  @(16,1993.3) 242.2x37.2
              display:table-cell; padding:9px 10px
          - td "abrir"  @(258.2,1993.3) 193x37.2
              display:table-cell; padding:9px 10px
          - td "arco 0,25 s e itens 0,28 s, cubic-bezier(.2,.8,.2,1); botão gira 90°"  @(451.1,1993.3) 632.9x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,2030.4) 1068x37.2
            display:table-row
          - td "Bottom sheets"  @(16,2030.4) 242.2x37.2
              display:table-cell; padding:9px 10px
          - td "abrir/fechar"  @(258.2,2030.4) 193x37.2
              display:table-cell; padding:9px 10px
          - td "sobe 0,3 s; fundo escurece 0,2 s"  @(451.1,2030.4) 632.9x37.2
              display:table-cell; padding:9px 10px
        - tr  @(16,2067.6) 1068x37.2
            display:table-row
          - td "Todas"  @(16,2067.6) 242.2x37.2
              display:table-cell; padding:9px 10px
          - td "reduzir movimento"  @(258.2,2067.6) 193x37.2
              display:table-cell; padding:9px 10px
          - td "sem animação nem transição"  @(451.1,2067.6) 632.9x37.2
              display:table-cell; padding:9px 10px
  - div.sec  @(16,2141.3) 1068x280
      margin:0px 0px 36px
    - h3 "6 · Componentes"  @(16,2141.3) 1068x30
        padding:0px 0px 8px; margin:0px 0px 14px; color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:18px; font-weight:700; letter-spacing:2.52px; text-transform:uppercase
    - div.grid  @(16,2185.3) 1068x236
        display:grid; gap:14px; grid-template-columns:256.5px 256.5px 256.5px 256.5px
      - div.card.cl  @(16,2185.3) 256.5x236
          display:flex; position:relative; flex-direction:column; gap:8px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.lb "Botões"  @(31,2200.3) 226.5x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - button.btn.pri "Começar treino"  @(31,2222.3) 226.5x56
            display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
          - svg.ic.fl  @(67.7,2241.3) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic fl"><use href="#i-play"></use></svg>
        - button.btn.olv "Pausar"  @(31,2286.3) 226.5x56
            display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
          - svg.ic.fl  @(102.2,2305.3) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic fl"><use href="#i-pause"></use></svg>
        - button.btn.gh "Agora não"  @(31,2350.3) 226.5x56
            display:flex; justify-content:center; align-items:center; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
      - div.card.cl  @(286.5,2185.3) 256.5x236
          display:flex; position:relative; flex-direction:column; gap:8px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.lb "Chips e tags"  @(301.5,2200.3) 226.5x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.rw  @(301.5,2222.3) 226.5x72
            display:flex; align-items:center; gap:12px
          - span.chip "Sem sinal"  @(301.5,2222.3) 96.8x30
              display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12.5px; line-height:17.5px
            - i.dot  @(314.5,2233.8) 7x7
                background-color:rgb(209, 43, 43); border-radius:50%
          - span.chip.on "Seguindo você"  @(410.3,2222.3) 114.2x30
              display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12.5px; line-height:17.5px
          - span.chip.rd "Dor"  @(301.5,2264.3) 47.2x30
              display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(240, 128, 124); background-color:rgba(209, 43, 43, 0.15); border:1px solid rgba(209, 43, 43, 0.4); border-radius:15px; font-size:12.5px; line-height:17.5px
          - span.tag "Etapa 3"  @(360.7,2270.3) 64.3x18
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
          - span.tag.br "Recruta"  @(437,2270.3) 70.2x18
              padding:3px 8px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.15); border-radius:6px; font-size:10px; font-weight:700; letter-spacing:1px; text-transform:uppercase
        - span.lb "Seleção"  @(301.5,2308.3) 226.5x14
            margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.seg  @(301.5,2330.3) 226.5x44
            display:flex; gap:6px
          - div.s "2"  @(301.5,2330.3) 71.5x44
              display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:69.5px
          - div.s.on "3"  @(379,2330.3) 71.5x44
              display:grid; align-items:center; flex-grow:1; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-weight:600; grid-template-columns:69.5px
          - div.s "4"  @(456.5,2330.3) 71.5x44
              display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:69.5px
      - div.card.cl  @(557,2185.3) 256.5x236
          display:flex; position:relative; flex-direction:column; gap:8px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - span.lb "Switch, progresso, campo"  @(572,2200.3) 226.5x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.rw.b  @(572,2222.3) 226.5x30
            display:flex; justify-content:space-between; align-items:center; gap:12px
          - span "Voz do treinador"  @(572,2227.5) 109.3x19.6
          - button.sw aria="Voz"  @(748.5,2222.3) 50x30
              position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border:1px solid rgba(0, 0, 0, 0); border-radius:15px; text-align:center; transition:background 0.2s
            - button.sw::after  size 22pxx22px (pseudo; inset 3px 23px 3px 3px)
                position:absolute; background-color:rgb(236, 232, 218); border-radius:50%; transform:matrix(1, 0, 0, 1, 20, 0); transition:transform 0.2s, background 0.2s
                content:""
        - div.bar  @(572,2260.3) 226.5x6
            background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
          - i  @(572,2260.3) 99.7x6
              background-color:rgb(163, 181, 101); border-radius:3px
        - div.fld.f "Seu nome ou apelido"  @(572,2274.3) 226.5x52
            display:flex; align-items:center; padding:0px 14px; background-color:rgb(13, 15, 10); border:1px solid rgb(163, 181, 101); border-radius:14px
      - div.card.sel  @(827.5,2185.3) 256.5x236
          position:relative; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
        - div.card.sel::before  size 3pxx206px (pseudo; inset 14px 252.5px 14px -1px)
            position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
            content:""
        - span.lb "Card selecionado"  @(842.5,2203.3) 133.9x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
        - div.t2 "Primeiros 5K"  @(842.5,2223.9) 226.5x23
            margin:4px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - div.mu.sm "9 semanas · 2 ou 3 treinos por semana"  @(842.5,2246.9) 226.5x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
