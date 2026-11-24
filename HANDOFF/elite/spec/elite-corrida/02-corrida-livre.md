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
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - svg.map aria="Mapa"  @(1,1) 391x849
      position:absolute; overflow:hidden
      svg: <svg class="map" data-component="MapLayer" viewBox="0 0 390 844" preserveAspectRatio="xMidYMid slice" aria-label="Mapa"><rect width="390" height="844" fill="#0C0E09"></rect><g fill="#13160F"><rect x="20" y="300" width="110" height="70" rx="8"></rect><rect x="250" y="520" width="120" height="90" rx="8"></rect><rect x="30" y="560" width="100" height="80" rx="8"></rect><rect x="260" y="330" width="100" height="60" rx="8"></rect></g><path d="M0 640Q60 560 150 580T390 520L390 844H0Z" fill="#102019" opacity=".8"></path><g fill="none" stroke="#1C2015" stroke-width="9" stroke-linecap="round"><path d="M-10 470C90 450 140 380 220 400S340 360 400 330"></path><path d="M120 300C140 400 110 500 160 640S190 760 180 860"></path><path d="M300 300C280 420 330 500 290 600"></path></g><path d="M110 590C130 540 170 470 200 430" fill="none" stroke="#A3B565" stroke-width="5" stroke-linecap="round"></path><circle class="pulse" cx="200" cy="430" r="14" fill="#A3B565"></circle><circle cx="200" cy="430" r="8" fill="#A3B565" stroke="#0A0B09" stroke-width="3"></circle></svg>
  - div.shade  @(1,1) 391x849
      position:absolute; background-image:linear-gradient(rgba(10, 11, 9, 0.96) 0%, rgba(10, 11, 9, 0.94) 30%, rgba(10, 11, 9, 0.9) 43%, rgba(10, 11, 9, 0) 53%, rgba(10, 11, 9, 0) 76%, rgba(10, 11, 9, 0.85) 100%)
  - div.layer  @(1,1) 391x849
      display:flex; position:relative; flex-direction:column; flex-grow:1; z-index:2
    - div.scr  @(1,1) 391x758
        display:flex; flex-direction:column; flex-grow:1; gap:8px; padding:20px 16px 0px
      - div.rw.b  @(17,21) 359x48
          display:flex; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px
        - span.chip.glass "Sem sinal"  @(17,30) 96.8x30
            display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; backdrop-filter:blur(14px); font-size:12.5px; line-height:17.5px
          - i.dot  @(30,41.5) 7x7
              background-color:rgb(209, 43, 43); border-radius:50%
        - div.rad  @(328,21) 48x48
            position:relative; margin:0px 0px 0px 202.156px; z-index:10
          - div.arc  @(325.6,45) 26.4x26.4
              position:absolute; background-color:rgb(29, 33, 22); border-radius:0px 0px 0px 100%; opacity:0; transform:matrix(0.2, 0, 0, 0.2, 0, 0); transition:transform 0.25s cubic-bezier(0.2, 0.8, 0.2, 1), opacity 0.2s
          - button.it.i1 aria="Som"  @(341,34) 22x22
              display:grid; position:absolute; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:50%; opacity:0; text-align:center; transform:matrix(0.5, 0, 0, 0.5, 0, 0); transition:transform 0.28s cubic-bezier(0.2, 0.8, 0.2, 1), opacity 0.2s, background 0.15s; grid-template-columns:42px
            - svg.ic  @(346.5,39.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-sound"></use></svg>
          - button.it.i2 aria="Vibração"  @(341,34) 22x22
              display:grid; position:absolute; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:50%; opacity:0; text-align:center; transform:matrix(0.5, 0, 0, 0.5, 0, 0); transition:transform 0.28s cubic-bezier(0.2, 0.8, 0.2, 1), opacity 0.2s, background 0.15s; grid-template-columns:42px
            - svg.ic  @(346.5,39.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-vib"></use></svg>
          - button.it.i3 aria="Manter GPS ativo"  @(341,34) 22x22
              display:grid; position:absolute; align-items:center; color:rgb(143, 147, 132); background-color:rgb(31, 36, 24); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; opacity:0; text-align:center; transform:matrix(0.5, 0, 0, 0.5, 0, 0); transition:transform 0.28s cubic-bezier(0.2, 0.8, 0.2, 1), opacity 0.2s, background 0.15s; grid-template-columns:42px
            - svg.ic  @(346.5,39.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-pin"></use></svg>
          - button.fab aria="Configurações da corrida"  @(328,21) 48x48
              display:grid; position:absolute; align-items:center; background-color:rgba(16, 18, 13, 0.85); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; transition:transform 0.3s, background 0.2s; grid-template-columns:46px
            - svg.ic  @(341,34) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg class="ic"><use href="#i-gear"></use></svg>
      - div#bn.card.glass.rw  @(17,77) 359x78.4
          display:flex; position:relative; align-items:flex-start; flex-shrink:0; gap:8px; padding:10px 12px; background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; backdrop-filter:blur(14px)
        - p.sm "O celular pode pausar o GPS com a tela apagada. Toque para ajustar."  @(30,88) 303.8x56.4
            flex-grow:1; color:rgb(201, 205, 189); font-size:12px; line-height:16.8px
          - br  @(138.7,104.8) 0x15
          - a.ol "Não mostrar de novo"  @(30,127.6) 119.9x16.8
              display:inline-block; margin:6px 0px 0px; color:rgb(163, 181, 101); font-weight:500
        - button.mu "×" aria="Fechar aviso"  @(341.8,88) 21.2x24
            padding:2px 4px; color:rgb(143, 147, 132); font-size:20px; line-height:20px; text-align:center
      - div.ring  @(140.5,163.4) 112x112
          display:grid; position:relative; align-items:center; flex-shrink:0; margin:0px 123.5px; grid-template-columns:112px
        - svg  @(140.5,163.4) 112x112
            position:absolute; transform:matrix(0, -1, 1, 0, 0, 0); overflow:hidden
            svg: <svg width="112" height="112" viewBox="0 0 112 112" aria-hidden="true"><circle cx="56" cy="56" r="52" fill="rgba(16,18,13,.85)" stroke="rgba(236,232,218,.1)" stroke-width="4"></circle><circle cx="56" cy="56" r="52" fill="none" stroke="#A3B565" stroke-width="4" stroke-linecap="round" stroke-dasharray="326" stroke-dashoffset="300"></circle></svg>
        - b "00:03"  @(150.8,199.9) 91.4x39
            position:relative; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:32px; font-weight:600; font-variant-numeric:tabular-nums
      - div.ol.sm "Corrida livre"  @(17,283.4) 359x16.8
          flex-shrink:0; color:rgb(163, 181, 101); font-size:12px; line-height:16.8px; text-align:center
      - div.stats  @(17,308.2) 359x59.6
          display:grid; align-items:center; flex-shrink:0; padding:0px 24px; text-align:center; grid-template-columns:155px 1px 155px
        - div  @(41,308.2) 155x59.6
          - b "0.00"  @(41,308.2) 155x40
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:40px; font-weight:600; line-height:40px
            - small "km"  @(153.1,326.2) 23.5x20
                margin:0px 0px 0px 3px; color:rgb(143, 147, 132); font-size:16px; line-height:16px
          - span "Distância"  @(92.4,350.2) 52.2x15
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - i  @(196,320) 1x36
            background-color:rgba(236, 232, 218, 0.1)
        - div  @(197,308.2) 155x59.6
          - b "--:--"  @(197,308.2) 155x40
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:40px; font-weight:600; line-height:40px
          - span "Ritmo (min/km)"  @(231.3,350.2) 86.3x15
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - div.sp  @(17,375.8) 359x383.2
          position:relative; flex-grow:1
        - span.chip.glass "Seguindo você"  @(17,715) 114.2x30
            display:flex; position:absolute; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; backdrop-filter:blur(14px); font-size:12.5px; line-height:17.5px
        - button.ib.glass aria="Centralizar no mapa"  @(330,703) 46x46
            display:grid; position:absolute; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; backdrop-filter:blur(14px); text-align:center; grid-template-columns:44px
          - svg.ic  @(342,715) 22x22
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic"><use href="#i-loc"></use></svg>
    - nav.acts  @(1,759) 391x91
        display:grid; gap:12px; padding:16px 16px 18px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px; grid-template-columns:173.5px 173.5px
      - button.btn.olv "Pausar"  @(17,776) 173.5x56
          display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
        - svg.ic.fl  @(61.7,795) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic fl"><use href="#i-pause"></use></svg>
      - button.btn.pri "Parar"  @(202.5,776) 173.5x56
          display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
        - svg.ic.fl  @(253.7,795) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic fl"><use href="#i-stop"></use></svg>
