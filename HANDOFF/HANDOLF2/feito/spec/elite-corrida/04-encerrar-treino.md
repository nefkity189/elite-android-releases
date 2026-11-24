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
- main.phone.o.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - svg.map aria="Mapa"  @(1,1) 391x849
      position:absolute; overflow:hidden
      svg: <svg class="map" data-component="MapLayer" viewBox="0 0 390 844" preserveAspectRatio="xMidYMid slice" aria-label="Mapa"><rect width="390" height="844" fill="#0C0E09"></rect><g fill="#13160F"><rect x="20" y="300" width="110" height="70" rx="8"></rect><rect x="250" y="520" width="120" height="90" rx="8"></rect><rect x="30" y="560" width="100" height="80" rx="8"></rect><rect x="260" y="330" width="100" height="60" rx="8"></rect></g><path d="M0 640Q60 560 150 580T390 520L390 844H0Z" fill="#102019" opacity=".8"></path><g fill="none" stroke="#1C2015" stroke-width="9" stroke-linecap="round"><path d="M-10 470C90 450 140 380 220 400S340 360 400 330"></path><path d="M120 300C140 400 110 500 160 640S190 760 180 860"></path><path d="M300 300C280 420 330 500 290 600"></path></g><path d="M110 590C130 540 170 470 200 430" fill="none" stroke="#A3B565" stroke-width="5" stroke-linecap="round"></path><circle class="pulse" cx="200" cy="430" r="14" fill="#A3B565"></circle><circle cx="200" cy="430" r="8" fill="#A3B565" stroke="#0A0B09" stroke-width="3"></circle></svg>
  - div.shade  @(1,1) 391x849
      position:absolute; background-image:linear-gradient(rgba(10, 11, 9, 0.96) 0%, rgba(10, 11, 9, 0.94) 30%, rgba(10, 11, 9, 0.9) 43%, rgba(10, 11, 9, 0) 53%, rgba(10, 11, 9, 0) 76%, rgba(10, 11, 9, 0.85) 100%)
  - div.layer  @(1,1) 391x849
      display:flex; position:relative; flex-direction:column; flex-grow:1; z-index:2
    - div.scr  @(1,1) 391x758
        display:flex; flex-direction:column; flex-grow:1; gap:14px; padding:20px 16px 0px
      - span.chip.glass "GPS ativo"  @(17,21) 97.3x30
          display:flex; align-items:center; flex-shrink:0; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; backdrop-filter:blur(14px); font-size:12.5px; line-height:17.5px
        - i.dot.ok  @(30,32.5) 7x7
            background-color:rgb(127, 176, 105); border-radius:50%
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
  - div.scrim  @(1,1) 391x849
      position:absolute; background-color:rgba(0, 0, 0, 0.6); z-index:20
  - div#dx.dlg  @(25,273.9) 343x303.2
      display:flex; position:absolute; flex-direction:column; gap:12px; padding:20px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px; transform:matrix(1, 0, 0, 1, 0, -151.594); z-index:22; transition:opacity 0.2s, transform 0.2s
    - span.ico  @(46,294.9) 40x40
        display:grid; align-items:center; flex-shrink:0; color:rgb(240, 128, 124); background-color:rgba(209, 43, 43, 0.15); border-radius:12px; grid-template-columns:40px
      - svg.ic  @(55,303.9) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-stop"></use></svg>
    - h2.t2 "Encerrar o treino agora?"  @(46,346.9) 301x46
        font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
    - p.mu "Você poderá ver o resumo com o tempo e a distância percorrida até aqui."  @(46,404.9) 301x39.2
        color:rgb(143, 147, 132)
    - button.btn.pri.sm2 "Encerrar e Salvar"  @(46,456.1) 301x44
        display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
    - button.btn.gh.sm2 "Continuar Treinando"  @(46,512.1) 301x44
        display:flex; justify-content:center; align-items:center; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
