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

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - svg.map aria="Mapa"  @(1,1) 391x849
      position:absolute; overflow:hidden
      svg: <svg aria-label="Mapa" class="map" data-component="MapLayer" preserveAspectRatio="xMidYMid slice" viewBox="0 0 390 844"><rect fill="#0C0E09" height="844" width="390"></rect><g fill="#13160F"><rect height="70" rx="8" width="110" x="20" y="300"></rect><rect height="90" rx="8" width="120" x="250" y="520"></rect><rect height="80" rx="8" width="100" x="30" y="560"></rect><rect height="60" rx="8" width="100" x="260" y="330"></rect></g><path d="M0 640Q60 560 150 580T390 520L390 844H0Z" fill="#102019" opacity=".8"></path><g fill="none" stroke="#1C2015" stroke-linecap="round" stroke-width="9"><path d="M-10 470C90 450 140 380 220 400S340 360 400 330"></path><path d="M120 300C140 400 110 500 160 640S190 760 180 860"></path><path d="M300 300C280 420 330 500 290 600"></path></g><path d="M110 590C130 540 170 470 200 430" fill="none" stroke="#A3B565" stroke-linecap="round" stroke-width="5"></path><circle class="pulse" cx="200" cy="430" fill="#A3B565" r="14"></circle><circle cx="200" cy="430" fill="#A3B565" r="8" stroke="#0A0B09" stroke-width="3"></circle></svg>
  - div.shade  @(1,1) 391x849
      position:absolute; background-image:linear-gradient(rgba(10, 11, 9, 0.96) 0%, rgba(10, 11, 9, 0.94) 30%, rgba(10, 11, 9, 0.9) 43%, rgba(10, 11, 9, 0) 53%, rgba(10, 11, 9, 0) 76%, rgba(10, 11, 9, 0.85) 100%)
  - div.layer  @(1,1) 391x849
      display:flex; position:relative; flex-direction:column; flex-grow:1; background-color:rgba(10, 11, 9, 0.35); z-index:2
    - div.scr  @(1,1) 391x758
        display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:20px 16px 0px
      - div.rw.b  @(17,21) 359x30
          display:flex; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px
        - span.chip.glass "GPS ativo"  @(17,21) 97.3x30
            display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(143, 147, 132); background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; backdrop-filter:blur(14px); font-size:12.5px; line-height:17.5px
          - i.dot.ok  @(30,32.5) 7x7
              background-color:rgb(127, 176, 105); border-radius:50%
        - span.chip "Treino em pausa"  @(251.9,21) 124.1x30
            display:flex; align-items:center; gap:7px; padding:0px 12px; color:rgb(224, 169, 58); background-color:rgb(22, 25, 17); border:1px solid rgba(224, 169, 58, 0.4); border-radius:15px; font-size:12.5px; line-height:17.5px
      - div.ring  @(140.5,61) 112x112
          display:grid; position:relative; align-items:center; flex-shrink:0; margin:0px 123.5px; grid-template-columns:112px
        - svg  @(140.5,61) 112x112
            position:absolute; transform:matrix(0, -1, 1, 0, 0, 0); overflow:hidden
            svg: <svg aria-hidden="true" height="112" viewBox="0 0 112 112" width="112"><circle cx="56" cy="56" fill="rgba(16,18,13,.85)" r="52" stroke="rgba(236,232,218,.1)" stroke-width="4"></circle><circle cx="56" cy="56" fill="none" r="52" stroke="#E0A93A" stroke-dasharray="326" stroke-dashoffset="230" stroke-linecap="round" stroke-width="4"></circle></svg>
        - b "03:12"  @(150.8,97.5) 91.4x39
            position:relative; color:rgb(224, 169, 58); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:32px; font-weight:600; font-variant-numeric:tabular-nums
      - div.stats  @(17,189) 359x59.6
          display:grid; align-items:center; flex-shrink:0; padding:0px 24px; margin:6px 0px 0px; text-align:center; grid-template-columns:155px 1px 155px
        - div  @(41,189) 155x59.6
          - b "0.42"  @(41,189) 155x40
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:40px; font-weight:600; line-height:40px
            - small "km"  @(153,207) 23.5x20
                margin:0px 0px 0px 3px; color:rgb(143, 147, 132); font-size:16px; line-height:16px
          - span "Distância"  @(92.4,231) 52.2x15
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
        - i  @(196,200.8) 1x36
            background-color:rgba(236, 232, 218, 0.1)
        - div  @(197,189) 155x59.6
          - b "6:10"  @(197,189) 155x40
              font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:40px; font-weight:600; line-height:40px
          - span "Ritmo (min/km)"  @(231.3,231) 86.3x15
              color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - div.sp  @(17,258.6) 359x463.6
          flex-grow:1
      - p.mu.sm "Solte p/ cancelar"  @(17,732.2) 359x16.8
          flex-shrink:0; margin:0px 0px 10px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px; text-align:center
    - nav.acts  @(1,759) 391x91
        display:grid; gap:12px; padding:16px 16px 18px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px; grid-template-columns:173.5px 173.5px
      - button.btn.olv "Retomar"  @(17,776) 173.5x56
          display:flex; justify-content:center; align-items:center; gap:10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
        - svg.ic.fl  @(55.7,795) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic fl"><use href="#i-play"></use></svg>
      - button.btn.pri.hold  @(202.5,776) 173.5x56
          display:flex; position:relative; justify-content:center; align-items:center; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; overflow:hidden; transition:transform 0.1s
        - i  @(202.5,776) 104.1x56
            position:absolute; background-color:rgba(0, 0, 0, 0.3)
        - span "Segure 1,5s"  @(228.1,794) 122.4x20
            display:flex; position:relative; align-items:center; gap:10px
          - svg.ic.fl  @(228.1,795) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg class="ic fl"><use href="#i-stop"></use></svg>
