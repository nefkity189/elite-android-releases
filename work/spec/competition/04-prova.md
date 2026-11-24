# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--page`: #050604
- `--bg`: #0a0b09
- `--surface`: #10120d
- `--card`: #161911
- `--card-el`: #1d2116
- `--field`: #0d0f0a
- `--line`: rgba(236,232,218,.10)
- `--line-strong`: rgba(236,232,218,.18)
- `--text`: #ece8da
- `--muted`: #8f9384
- `--dim`: #5e6255
- `--olive`: #a3b565
- `--olive-dark`: #5d6b34
- `--olive-soft`: rgba(163,181,101,.14)
- `--on-olive`: #0e1306
- `--red`: #d12b2b
- `--red-soft`: rgba(209,43,43,.15)
- `--on-red`: #fff4f0
- `--brass`: #c9a24b
- `--brass-soft`: rgba(201,162,75,.16)
- `--amber`: #e0a93a
- `--amber-soft`: rgba(224,169,58,.14)
- `--ok`: #7fb069
- `--ok-soft`: rgba(127,176,105,.15)
- `--amber-line`: rgba(224,169,58,.45)
- `--red-line`: rgba(209,43,43,.5)
- `--ok-line`: rgba(127,176,105,.45)
- `--brass-line`: rgba(201,162,75,.5)
- `--glass`: rgba(16,18,13,.82)
- `--glass-bar`: rgba(16,18,13,.90)
- `--scrim`: rgba(0,0,0,.62)
- `--lg-bronze`: #b07a4a
- `--lg-prata`: #b4bab8
- `--lg-ouro`: #c9a24b
- `--lg-platina`: #86bdb3
- `--lg-diamante`: #7fb4de
- `--lg-elite`: #e7e2d0
- `--av1`: #3b4426
- `--av2`: #4a3f1e
- `--av3`: #2e3b3a
- `--av4`: #46301f
- `--av5`: #3a2e3f
- `--num`: "Barlow Condensed","Arial Narrow",sans-serif
- `--ui`: "Inter",sans-serif
- `--logo`: "Saira Stencil One","Impact",sans-serif

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- **live-pulse** on `i.dot.live-dot`: 1400ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "opacity": "1"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "opacity": "0.3"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "opacity": "1"}

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 16px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - header.row  @(16,28) 361x48
      display:flex; align-items:center; flex-shrink:0; gap:8px
    - span.chip  @(16,37) 84.2x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12px; font-weight:600; line-height:16px
      - span.num "5K · 13:00"  @(29,44.5) 58.2x15
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:15px; font-weight:700; line-height:15px; font-variant-numeric:tabular-nums
    - span.badge.b-live "Ao vivo"  @(108.2,40) 81.5x24
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(209, 43, 43); background-color:rgba(209, 43, 43, 0.15); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
      - i.dot.live-dot  @(118.2,48.5) 7x7
          flex-shrink:0; background-color:rgb(209, 43, 43); border-radius:4px; opacity:0.3; animation:1.4s ease-in-out infinite live-pulse
    - span.grow  @(197.7,52) 16x0
        flex-grow:1
    - span.chip.chip-on  @(221.8,37) 155.3x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12px; font-weight:600; line-height:16px
      - svg  @(234.8,45) 14x14
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="run.top.rule.ic" data-icon="material:Flag" viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1zM4 22v-7"></path></svg>
      - span "1º a concluir vence"  @(254.8,44) 109.3x16
  - section.col  @(16,86) 361x124
      display:flex; flex-direction:column; align-items:center; flex-shrink:0; gap:6px; margin:10px 0px 0px
    - span.eyebrow "Tempo decorrido"  @(133.1,86) 126.9x14
        color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
    - span.num "24:18"  @(76.1,106) 240.8x104
        font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:104px; font-weight:700; line-height:104px; letter-spacing:1px; font-variant-numeric:tabular-nums
  - section.card  @(16,220) 361x84
      flex-shrink:0; padding:14px 16px; margin:10px 0px 0px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
    - div.row  @(33,235) 327x34
        display:flex; justify-content:space-between
      - span.num "2,3"  @(33,235) 117.1x34
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:34px; font-weight:700; line-height:34px; font-variant-numeric:tabular-nums
        - span.muted "/ 5,0 km"  @(80.8,243) 69.2x26
            color:rgb(143, 147, 132); font-size:22px; line-height:22px
      - span.num.olive "46%"  @(324.8,247) 35.3x20
          color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
    - div.bar  @(33,279) 327x10
        margin:10px 0px 0px; background-color:rgb(29, 33, 22); border-radius:5px; overflow:hidden
      - i  @(33,279) 150.4x10
          background-color:rgb(163, 181, 101); border-radius:5px; transition:width 0.6s linear
  - section.row  @(16,314) 361x70
      display:flex; align-items:center; flex-shrink:0; gap:10px; margin:10px 0px 0px
    - div.mt  @(16,314) 175.5x70
        display:flex; flex-direction:column; flex-grow:1; gap:4px; padding:10px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px
      - span.eyebrow "Pace atual"  @(31,325) 145.5x14
          color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
      - span.num "5:12"  @(31,343) 145.5x30
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:30px; font-variant-numeric:tabular-nums
        - span.muted "/km"  @(90.1,354) 24.6x19
            color:rgb(143, 147, 132); font-size:16px; line-height:16px
    - div.mt  @(201.5,314) 175.5x70
        display:flex; flex-direction:column; flex-grow:1; gap:4px; padding:10px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px
      - span.eyebrow "Sua posição"  @(216.5,325) 145.5x14
          color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
      - span.num.olive "2º"  @(216.5,343) 145.5x30
          color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:30px; font-variant-numeric:tabular-nums
        - span.muted "de 3"  @(246.3,354) 25.3x19
            color:rgb(143, 147, 132); font-size:16px; line-height:16px
  - section.card.col  @(16,394) 361x334
      display:flex; flex-direction:column; flex-grow:1; padding:12px 12px 8px; margin:10px 0px 0px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px; overflow:hidden
    - div.row  @(31,407) 331x23
        display:flex; justify-content:space-between; align-items:center; margin:0px 2px 6px
      - h2.t2 "Placar ao vivo"  @(31,407) 123.8x23
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
      - span.cap "3 competidores"  @(273.3,410.5) 88.7x16
          color:rgb(143, 147, 132); font-size:12px; line-height:16px
    - div.sb  @(29,436) 335x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 10px; border-radius:14px
      - span.rk-pos.olive "1"  @(39,454) 22x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av "CD"  @(71,448) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(113,449.5) 213x29
          display:flex; flex-direction:column; flex-grow:1; gap:5px
        - div.row  @(113,449.5) 213x20
            display:flex; justify-content:space-between; align-items:center
          - span.t3 "Camila Duarte"  @(113,449.5) 95.5x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "2,6 km"  @(287.6,451.5) 38.4x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.bar  @(113,474.5) 213x4
            background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
          - i  @(113,474.5) 110.8x4
              background-color:rgb(163, 181, 101); border-radius:3px; transition:width 0.6s linear
      - span.brass  @(336,455) 18x18
          color:rgb(201, 162, 75)
        - svg  @(336,455) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="run.board.p1.medal.ic" data-icon="material:WorkspacePremium" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="9" r="6"></circle><path d="M8.5 14.5L7 22l5-3 5 3-1.5-7.5"></path></svg>
    - div.sb.sb-me  @(29,492) 335x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 10px; background-color:rgba(163, 181, 101, 0.14); border-radius:14px; box-shadow:rgb(93, 107, 52) 0px 0px 0px 1px inset
      - span.rk-pos.olive "2"  @(39,510) 22x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av.av-me "RN"  @(71,504) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgb(163, 181, 101); border-radius:50%; box-shadow:rgba(163, 181, 101, 0.14) 0px 0px 0px 2px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(113,505.5) 213x29
          display:flex; flex-direction:column; flex-grow:1; gap:5px
        - div.row  @(113,505.5) 213x20
            display:flex; justify-content:space-between; align-items:center
          - span.t3 "Você"  @(113,505.5) 34.3x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "2,3 km"  @(287.7,507.5) 38.3x16
              font-size:12px; line-height:16px
        - div.bar  @(113,530.5) 213x4
            background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
          - i  @(113,530.5) 98x4
              background-color:rgb(163, 181, 101); border-radius:3px; transition:width 0.6s linear
      - span  @(336,520) 18x0
    - div.sb  @(29,548) 335x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 10px; border-radius:14px
      - span.rk-pos "3"  @(39,566) 22x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av "BT"  @(71,560) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(46, 59, 58); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(113,561.5) 213x29
          display:flex; flex-direction:column; flex-grow:1; gap:5px
        - div.row  @(113,561.5) 213x20
            display:flex; justify-content:space-between; align-items:center
          - span.t3 "Bruno Tavares"  @(113,561.5) 97.4x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "1,9 km"  @(289.8,563.5) 36.2x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.bar  @(113,586.5) 213x4
            background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
          - i  @(113,586.5) 80.9x4
              background-color:rgb(163, 181, 101); border-radius:3px; transition:width 0.6s linear
      - span  @(336,576) 18x0
  - footer.dock  @(0,738) 393x113
      flex-shrink:0; padding:12px 16px 18px; margin:10px -16px 0px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px
    - div.row  @(16,751) 361x16
        display:flex; justify-content:center; align-items:center; gap:8px; margin:0px 0px 10px; color:rgb(143, 147, 132)
      - svg  @(54.8,752) 14x14
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="run.dock.info.ic" data-icon="material:Info" viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"></circle><path d="M12 16v-4M12 8h.01"></path></svg>
      - p.cap "Conclua a distância para registrar o resultado."  @(76.8,751) 261.4x16
          font-size:12px; line-height:16px
    - button.btn.btn-lg.btn-off.s-run "Finalizar · faltam 2,7 km"  @(16,777) 361x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(94, 98, 85); background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:28px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
    - button.btn.btn-lg.btn-red.s-fin  [display:none]
