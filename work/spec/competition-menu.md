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
- `--bg-clear`: rgba(26,22,8,0)
- `--sheen`: rgba(255,244,214,.22)
- `--prem-a`: #3a2e12
- `--prem-b`: #1a1608
- `--prem-glow`: rgba(201,162,75,.35)
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
- **sheet-up** on `aside.sheet-demo`: 320ms, delay 0ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(100%)"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(0px)"}
- **prem-sheen** on `div.prem-sheen`: 3600ms, delay 600ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "translateX(-120px) skewX(-20deg)"}
    - keyframe {"offset": 0.45, "easing": "ease-in-out", "transform": "translateX(420px) skewX(-20deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "translateX(420px) skewX(-20deg)"}

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 16px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - section.col  @(16,28) 361x370
      display:flex; flex-direction:column; flex-shrink:0; gap:6px; padding:20px 0px 0px
    - p.cap "Sábado, 21 de novembro"  @(16,48) 361x16
        color:rgb(143, 147, 132); font-size:14px; line-height:16px
    - h1.t1 "Olá, Rodrigo"  @(16,70) 361x32
        font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
    - div.row  @(16,122) 361x22
        display:flex; justify-content:space-between; align-items:center; margin:14px 0px 0px
      - span.num.muted "16"  @(16,122) 21.9x22
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
      - span.num.muted "17"  @(72.5,122) 21.9x22
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
      - span.num.muted "18"  @(129,122) 21.9x22
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
      - span.num.muted "19"  @(185.5,122) 21.9x22
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
      - span.num.muted "20"  @(242,122) 21.9x22
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
      - span.num.olive "21"  @(298.6,122) 21.9x22
          color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
      - span.num.muted "22"  @(355.1,122) 21.9x22
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
    - div.card  @(16,168) 361x230
        padding:16px; margin:18px 0px 0px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - p.t1 "Treino 1"  @(33,185) 327x32
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
  - div.ctx-dim  @(0,0) 393x851
      position:absolute; background-color:rgba(0, 0, 0, 0.62)
  - aside.sheet-demo  @(0,368) 393x483
      display:flex; position:absolute; flex-direction:column; gap:14px; padding:12px 20px 52px; background-color:rgb(22, 25, 17); border-radius:28px 28px 0px 0px; transform:matrix(1, 0, 0, 1, 0, 0); animation:0.32s cubic-bezier(0.2, 0.8, 0.2, 1) both sheet-up
    - div.grab  @(20,380) 353x0
    - h2.t2 "Adicionar corrida"  @(20,400) 353x23
        margin:6px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:21px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-align:center; text-transform:uppercase
    - button.cap "RECOLHER"  @(305.3,437) 67.8x16
        color:rgb(143, 147, 132); font-size:12px; font-weight:500; line-height:16px; letter-spacing:0.5px; text-align:center
    - div.row  @(20,467) 353x96
        display:flex; align-items:center; gap:12px
      - button.tile.t-olive aria="Corrida livre"  @(20,467) 170.5x96
          display:flex; position:relative; align-items:center; flex-grow:1; padding:0px 0px 0px 16px; background-color:rgb(163, 181, 101); border-radius:16px; font-size:13.3333px; overflow:hidden; transition:transform 0.12s ease-out
        - div.tt  @(36,487) 96.3x56
            display:flex; flex-direction:column; align-items:flow-start; gap:8px; color:rgb(255, 244, 240)
          - svg  @(36,487) 28x28
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.free.ic" data-icon="material:DirectionsRun" viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="14" cy="4.5" r="2"></circle><path d="M4 21l4-6 3 2 3-5-3-3-4 1M14 9.5l3 3 4 1M11 17l2 4"></path></svg>
          - b "Corrida livre"  @(36,523) 96.3x20
              font-size:16px; font-weight:700; line-height:20px
        - span.deco  @(120.5,487) 56x56
            position:absolute; margin:-28px 0px 0px; color:rgb(255, 244, 240); opacity:0.15
          - svg  @(120.5,487) 56x56
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.free.decoic" data-icon="material:DirectionsRun" viewBox="0 0 24 24" width="56" height="56" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="14" cy="4.5" r="2"></circle><path d="M4 21l4-6 3 2 3-5-3-3-4 1M14 9.5l3 3 4 1M11 17l2 4"></path></svg>
      - button.tile.t-brass aria="Treino 2.5k"  @(202.5,467) 170.5x96
          display:flex; position:relative; align-items:center; flex-grow:1; padding:0px 0px 0px 16px; background-color:rgb(201, 162, 75); border-radius:16px; font-size:13.3333px; overflow:hidden; transition:transform 0.12s ease-out
        - div.tt  @(218.5,487) 87.9x56
            display:flex; flex-direction:column; align-items:flow-start; gap:8px; color:rgb(255, 244, 240)
          - svg  @(218.5,487) 28x28
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.t25.ic" data-icon="material:Flag" viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1zM4 22v-7"></path></svg>
          - b "Treino 2.5k"  @(218.5,523) 87.9x20
              font-size:16px; font-weight:700; line-height:20px
        - span.deco  @(303,487) 56x56
            position:absolute; margin:-28px 0px 0px; color:rgb(255, 244, 240); opacity:0.15
          - svg  @(303,487) 56x56
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.t25.decoic" data-icon="material:Flag" viewBox="0 0 24 24" width="56" height="56" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1zM4 22v-7"></path></svg>
    - div.row  @(20,577) 353x96
        display:flex; align-items:center; gap:12px
      - button.tile.t-red aria="Treino 5k"  @(20,577) 170.5x96
          display:flex; position:relative; align-items:center; flex-grow:1; padding:0px 0px 0px 16px; background-color:rgb(209, 43, 43); border-radius:16px; font-size:13.3333px; overflow:hidden; transition:transform 0.12s ease-out
        - div.tt  @(36,597) 72.6x56
            display:flex; flex-direction:column; align-items:flow-start; gap:8px; color:rgb(255, 244, 240)
          - svg  @(36,597) 28x28
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.t5.ic" data-icon="material:Schedule" viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"></circle><path d="M12 7v5l3 2"></path></svg>
          - b "Treino 5k"  @(36,633) 72.6x20
              font-size:16px; font-weight:700; line-height:20px
        - span.deco  @(120.5,597) 56x56
            position:absolute; margin:-28px 0px 0px; color:rgb(255, 244, 240); opacity:0.15
          - svg  @(120.5,597) 56x56
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.t5.decoic" data-icon="material:Schedule" viewBox="0 0 24 24" width="56" height="56" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"></circle><path d="M12 7v5l3 2"></path></svg>
      - button.tile.t-brass aria="Treino 10k"  @(202.5,577) 170.5x96
          display:flex; position:relative; align-items:center; flex-grow:1; padding:0px 0px 0px 16px; background-color:rgb(201, 162, 75); border-radius:16px; font-size:13.3333px; overflow:hidden; transition:transform 0.12s ease-out
        - div.tt  @(218.5,597) 80.3x56
            display:flex; flex-direction:column; align-items:flow-start; gap:8px; color:rgb(255, 244, 240)
          - svg  @(218.5,597) 28x28
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.t10.ic" data-icon="material:Bolt" viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"></path></svg>
          - b "Treino 10k"  @(218.5,633) 80.3x20
              font-size:16px; font-weight:700; line-height:20px
        - span.deco  @(303,597) 56x56
            position:absolute; margin:-28px 0px 0px; color:rgb(255, 244, 240); opacity:0.15
          - svg  @(303,597) 56x56
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="menu.t10.decoic" data-icon="material:Bolt" viewBox="0 0 24 24" width="56" height="56" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"></path></svg>
    - button.prem aria="Competição online, recurso premium"  @(20,687) 353x112
        position:relative; flex-shrink:0; font-size:13.3333px; transition:transform 0.12s ease-out
      - span.prem-new "NOVO"  @(36,678) 48.2x18
          position:absolute; padding:0px 8px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:9px; font-size:9.5px; font-weight:800; line-height:18px; letter-spacing:1px; z-index:2
      - div.prem-in  @(20,687) 353x112
          position:absolute; background-image:linear-gradient(135deg, rgb(58, 46, 18), rgb(26, 22, 8) 70%); border:1px solid rgb(201, 162, 75); border-radius:16px; overflow:hidden
        - div.prem-glow  @(202,648) 200x200
            position:absolute; background-image:radial-gradient(circle, rgba(201, 162, 75, 0.35), rgba(26, 22, 8, 0) 70%); border-radius:100px
        - div.prem-sheen  @(-117.2,678) 117.3x130
            position:absolute; background-image:linear-gradient(90deg, rgba(26, 22, 8, 0), rgba(255, 244, 214, 0.22), rgba(26, 22, 8, 0)); transform:matrix(1, 0, -0.36397, 1, -114.539, 0); animation:3.6s ease-in-out 0.6s infinite prem-sheen
      - div.prem-body  @(20,687) 353x112
          display:flex; position:absolute; flex-direction:column; justify-content:space-between; padding:14px
        - div.row  @(34,701) 325x52
            display:flex; align-items:center; gap:12px
          - div.prem-ico  @(34,701) 52x52
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(14, 19, 6); background-color:rgb(201, 162, 75); border-radius:26px
            - svg  @(46,713) 28x28
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="menu.prem.icon.ic" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6M18 9h1.5a2.5 2.5 0 0 0 0-5H18M4 22h16M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22M18 2H6v7a6 6 0 0 0 12 0V2Z"></path></svg>
          - div.col.grow  @(98,703) 215x48
              display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
            - span.prem-badge "Premium"  @(98,703) 86.2x20
                display:flex; align-items:center; gap:4px; padding:0px 8px; color:rgb(14, 19, 6); background-color:rgb(201, 162, 75); border-radius:10px; font-size:10px; font-weight:800; line-height:12px; letter-spacing:0.9px; text-transform:uppercase
              - svg  @(106,707) 12x12
                  flex-shrink:0; overflow:hidden
                  svg: <svg data-id="menu.prem.badge.ic" data-icon="material:Stars" viewBox="0 0 24 24" width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 8l4 4 5-7 5 7 4-4-2 12H5z"></path></svg>
            - span.t1s "Competição online"  @(98,727) 176.3x24
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
          - span.prem-cta.off-only  @(325,710) 34x34
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border:1px solid rgb(201, 162, 75); border-radius:17px; font-size:12px; font-weight:700; line-height:14px
            - svg  @(334,719) 16x16
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="menu.prem.cta-off.ic" data-icon="material:Lock" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="11" width="18" height="11" rx="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
          - span.prem-cta.on-only  [display:none]
        - div.row  @(34,763) 325x22
            display:flex; align-items:center; gap:6px
          - span.prem-chip "Toda hora, 02h–21h"  @(34,763) 132x22
              display:flex; align-items:center; gap:4px; padding:0px 9px; background-color:rgba(201, 162, 75, 0.16); border-radius:11px; font-size:10.5px; font-weight:600; line-height:12px
            - svg  @(43,768.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="menu.prem.chip1.ic" data-icon="material:Schedule" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"></circle><path d="M12 7v5l3 2"></path></svg>
          - span.prem-chip "Ranking e ligas"  @(172,763) 109.3x22
              display:flex; align-items:center; gap:4px; padding:0px 9px; background-color:rgba(201, 162, 75, 0.16); border-radius:11px; font-size:10.5px; font-weight:600; line-height:12px
            - svg  @(181,768.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="menu.prem.chip2.ic" data-icon="material:BarChart" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 20V10M12 20V4M6 20v-6"></path></svg>
