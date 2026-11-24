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
- **hl-glow** on `div.hl-glow`: 2400ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "opacity": "0"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "opacity": "0.7"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "opacity": "0"}

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 16px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - header.topbar  @(16,28) 361x48
      display:flex; align-items:center; flex-shrink:0; gap:12px
    - button.btn-back aria="Voltar"  @(16,32) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(26,42) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="ach.topbar.back.ic" data-icon="material:ArrowBack" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M15 18l-6-6 6-6"></path></svg>
    - h1.t1.grow "Conquistas"  @(68,36) 202.2x32
        flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
    - button.chip "Ranking" aria="Abrir ranking"  @(282.2,34) 94.8x36
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px 0px 8px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border:1px solid rgba(201, 162, 75, 0.5); border-radius:15px; font-size:12px; font-weight:600; line-height:16px; text-align:center
      - svg  @(291.2,42) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="ach.topbar.ranking.badge" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="20" height="20" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
  - section.card.row  @(16,84) 361x76
      display:flex; align-items:center; flex-shrink:0; gap:14px; padding:12px 14px; margin:8px 0px 0px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
    - div.col  @(31,97) 105.3x50
        display:flex; flex-direction:column; gap:2px
      - span.num.brass "7"  @(31,97) 105.3x34
          color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:34px; font-weight:700; line-height:34px; font-variant-numeric:tabular-nums
        - span.muted "/ 24"  @(47.9,107) 36.2x24
            color:rgb(143, 147, 132); font-size:20px; line-height:20px
      - span.eyebrow "Desbloqueadas"  @(31,133) 105.3x14
          color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
    - div.col.grow  @(150.3,99) 211.7x46
        display:flex; flex-direction:column; flex-grow:1; gap:6px
      - div.bar.bar-brass  @(150.3,99) 211.7x8
          background-color:rgb(29, 33, 22); border-radius:4px; overflow:hidden
        - i  @(150.3,99) 61.4x8
            background-color:rgb(201, 162, 75); border-radius:3px
      - span.cap "Conquistas globais e do seu estado (MT)."  @(150.3,113) 211.7x32
          color:rgb(143, 147, 132); font-size:12px; line-height:16px
  - nav.seg.seg-sm aria="Escopo"  @(16,170) 361x40
      display:flex; flex-shrink:0; gap:4px; padding:4px; margin:10px 0px 0px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px
    - button.on "Globais"  @(21,175) 173.5x30
        flex-grow:1; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:20px; box-shadow:rgb(93, 107, 52) 0px 0px 0px 1px inset; font-size:13px; font-weight:600; line-height:16px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
    - button "Locais / Estado"  @(198.5,175) 173.5x30
        flex-grow:1; color:rgb(143, 147, 132); border-radius:20px; font-size:13px; font-weight:600; line-height:16px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
  - div  @(16,220) 361x631
      display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:0px 0px 12px; margin:10px 0px 0px; overflow:hidden auto
    - section.hl.g-only  @(16,220) 361x151
        display:flex; position:relative; flex-direction:column; flex-shrink:0; gap:12px; padding:14px; background-image:linear-gradient(135deg, rgba(201, 162, 75, 0.16), rgb(22, 25, 17) 75%); border:1px solid rgb(201, 162, 75); border-radius:18px
      - div.hl-glow  @(17,221) 359x149
          position:absolute; border:1px solid rgb(201, 162, 75); border-radius:18px; opacity:0.479979; animation:2.4s ease-in-out infinite hl-glow
      - div.row  @(31,235) 331x65
          display:flex; align-items:center; gap:14px
        - div.ac-ic  @(31,239.5) 56x56
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(14, 19, 6); background-color:rgb(201, 162, 75); border-radius:28px
          - svg  @(45,253.5) 28x28
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ach.new.icon.ic" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6M18 9h1.5a2.5 2.5 0 0 0 0-5H18M4 22h16M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22M18 2H6v7a6 6 0 0 0 12 0V2Z"></path></svg>
        - div.col.grow  @(101,235) 261x65
            display:flex; flex-direction:column; flex-grow:1; gap:3px
          - span.badge.b-brass "Nova conquista"  @(101,235) 124.8x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - h2.t2 "Pódio"  @(101,258) 261x23
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - span.cap "Você terminou em 2º na Missão 5K das 13:00."  @(101,284) 261x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - button.btn.btn-share "Compartilhar conquista"  @(31,312) 331x44
          display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - svg  @(98,326) 16x16
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ach.new.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
    - div.col.g-only  @(16,381) 361x980
        display:flex; flex-direction:column; flex-shrink:0; gap:10px
      - article.ac.st-un  @(16,381) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(201, 162, 75, 0.5); border-radius:18px
        - div.row  @(31,394) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,394) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:22px
            - svg  @(42,405) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g1.ic" data-icon="material:WorkspacePremium" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="9" r="6"></circle><path d="M8.5 14.5L7 22l5-3 5 3-1.5-7.5"></path></svg>
          - div.col.grow  @(87,394) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,394) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Primeira missão cumprida"  @(87,394) 188.3x20
                  font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,395) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Conclua sua primeira missão."  @(87,417) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,448) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-brass "Desbloqueada"  @(31,448) 113.8x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(154.8,458) 207.2x0
              flex-grow:1
      - article.ac.st-lk  @(16,491) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div.row  @(31,504) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,504) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(94, 98, 85); background-color:rgb(29, 33, 22); border-radius:22px
            - svg  @(42,515) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g2.ic" data-icon="material:Lock" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="11" width="18" height="11" rx="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
          - div.col.grow  @(87,504) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,504) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Primeira vitória"  @(87,504) 109.7x20
                  color:rgb(143, 147, 132); font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,505) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Seja o primeiro a concluir uma missão."  @(87,527) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,558) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-none "Bloqueada"  @(31,558) 93.1x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.18); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(134.1,560) 227.9x16
              flex-grow:1
            - div.row  @(134.1,560) 227.9x16
                display:flex; align-items:center; gap:6px
              - span.cap "Progresso"  @(134.1,560) 57.7x16
                  color:rgb(143, 147, 132); font-size:12px; line-height:16px
              - span.num "0/1"  @(197.8,560) 22.6x16
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; font-variant-numeric:tabular-nums
      - article.ac.st-pr  @(16,601) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div.row  @(31,614) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,614) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:22px
            - svg  @(42,625) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g3.ic" data-icon="material:LocalFireDepartment" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 2c1 4 6 6 6 12a6 6 0 0 1-12 0c0-3 2-4 3-7 1 1 2 2 2 3 1-2 1-5 1-8z"></path></svg>
          - div.col.grow  @(87,614) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,614) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Sequência de missões"  @(87,614) 162.4x20
                  font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,615) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Cumpra 5 missões seguidas."  @(87,637) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,668) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-open "Em progresso"  @(31,668) 110.6x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(151.6,670) 210.4x16
              flex-grow:1
            - div.row  @(151.6,670) 210.4x16
                display:flex; align-items:center; gap:10px
              - div.bar.grow  @(151.6,675) 177.8x6
                  flex-grow:1; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
                - i  @(151.6,675) 106.7x6
                    background-color:rgb(163, 181, 101); border-radius:3px
              - span.num.olive "3/5"  @(339.4,670) 22.6x16
                  color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; font-variant-numeric:tabular-nums
      - article.ac.st-pr  @(16,711) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div.row  @(31,724) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,724) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:22px
            - svg  @(42,735) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g4.ic" data-icon="material:DirectionsRun" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="14" cy="4.5" r="2"></circle><path d="M4 21l4-6 3 2 3-5-3-3-4 1M14 9.5l3 3 4 1M11 17l2 4"></path></svg>
          - div.col.grow  @(87,724) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,724) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Participação completa"  @(87,724) 162.3x20
                  font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,725) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Participe de missões 2,5K, 5K e 10K."  @(87,747) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,778) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-open "Em progresso"  @(31,778) 110.6x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(151.6,780) 210.4x16
              flex-grow:1
            - div.row  @(151.6,780) 210.4x16
                display:flex; align-items:center; gap:10px
              - div.bar.grow  @(151.6,785) 177.8x6
                  flex-grow:1; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
                - i  @(151.6,785) 117.3x6
                    background-color:rgb(163, 181, 101); border-radius:3px
              - span.num.olive "2/3"  @(339.4,780) 22.6x16
                  color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; font-variant-numeric:tabular-nums
      - article.ac.st-pr  @(16,821) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div.row  @(31,834) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,834) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:22px
            - svg  @(42,845) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g5.ic" data-icon="material:Bolt" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z"></path></svg>
          - div.col.grow  @(87,834) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,834) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Melhor pace"  @(87,834) 90.4x20
                  font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,835) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Supere seu melhor pace em uma missão."  @(87,857) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,888) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-open "Em progresso"  @(31,888) 110.6x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(151.6,898) 210.4x0
              flex-grow:1
      - article.ac.st-pr  @(16,931) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div.row  @(31,944) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,944) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:22px
            - svg  @(42,955) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g6.ic" data-icon="material:Link" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"></path></svg>
          - div.col.grow  @(87,944) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,944) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Convites aceitos"  @(87,944) 120.5x20
                  font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,945) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Traga 3 competidores pelo seu convite."  @(87,967) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,998) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-open "Em progresso"  @(31,998) 110.6x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(151.6,1000) 210.4x16
              flex-grow:1
            - div.row  @(151.6,1000) 210.4x16
                display:flex; align-items:center; gap:10px
              - div.bar.grow  @(151.6,1005) 177.8x6
                  flex-grow:1; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
                - i  @(151.6,1005) 58.7x6
                    background-color:rgb(163, 181, 101); border-radius:3px
              - span.num.olive "1/3"  @(339.4,1000) 22.6x16
                  color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; font-variant-numeric:tabular-nums
      - article.ac.st-lk  @(16,1041) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div.row  @(31,1054) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,1054) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(94, 98, 85); background-color:rgb(29, 33, 22); border-radius:22px
            - svg  @(42,1065) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g7.ic" data-icon="material:Lock" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="11" width="18" height="11" rx="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
          - div.col.grow  @(87,1054) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,1054) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Top 10 global"  @(87,1054) 95.5x20
                  color:rgb(143, 147, 132); font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,1055) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Entre no top 10 do ranking global."  @(87,1077) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,1108) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-none "Bloqueada"  @(31,1108) 93.1x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.18); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(134.1,1110) 227.9x16
              flex-grow:1
            - div.row  @(134.1,1110) 227.9x16
                display:flex; align-items:center; gap:6px
              - span.cap "Posição atual"  @(134.1,1110) 75.7x16
                  color:rgb(143, 147, 132); font-size:12px; line-height:16px
              - span.num "#128"  @(215.9,1110) 33.8x16
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; font-variant-numeric:tabular-nums
      - article.ac.st-un  @(16,1151) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(201, 162, 75, 0.5); border-radius:18px
        - div.row  @(31,1164) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,1164) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:22px
            - svg  @(42,1175) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g8.ic" data-icon="material:BarChart" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 20V10M12 20V4M6 20v-6"></path></svg>
          - div.col.grow  @(87,1164) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,1164) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Subir de liga"  @(87,1164) 89.8x20
                  font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,1165) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Suba de liga na temporada."  @(87,1187) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,1218) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-brass "Desbloqueada"  @(31,1218) 113.8x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(154.8,1228) 207.2x0
              flex-grow:1
      - article.ac.st-lk  @(16,1261) 361x100
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div.row  @(31,1274) 331x44
            display:flex; align-items:flow-start; gap:12px
          - div.ac-ic  @(31,1274) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(94, 98, 85); background-color:rgb(29, 33, 22); border-radius:22px
            - svg  @(42,1285) 22x22
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ach.g9.ic" data-icon="material:Lock" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="11" width="18" height="11" rx="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
          - div.col.grow  @(87,1274) 275x39
              display:flex; flex-direction:column; flex-grow:1; gap:3px
            - div.row  @(87,1274) 275x20
                display:flex; justify-content:space-between; align-items:center; gap:8px
              - span.t3 "Elite da temporada"  @(87,1274) 134.9x20
                  color:rgb(143, 147, 132); font-size:15px; font-weight:600; line-height:20px
              - span.scope "Global"  @(306.1,1275) 55.9x18
                  display:flex; align-items:center; padding:0px 7px; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:9px; font-size:9.5px; font-weight:700; line-height:10px; letter-spacing:0.6px; text-transform:uppercase
            - span.cap "Termine a temporada em divisão de elite."  @(87,1297) 275x16
                color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.row  @(31,1328) 331x20
            display:flex; justify-content:space-between; align-items:center; gap:10px
          - span.badge.b-none "Bloqueada"  @(31,1328) 93.1x20
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.18); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.grow  @(134.1,1330) 227.9x16
              flex-grow:1
            - div.row  @(134.1,1330) 227.9x16
                display:flex; align-items:center; gap:6px
              - span.cap "Liga atual"  @(134.1,1330) 54.6x16
                  color:rgb(143, 147, 132); font-size:12px; line-height:16px
              - span.num "Ouro II"  @(194.7,1330) 37.4x16
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; font-variant-numeric:tabular-nums
    - div.col.l-only  [display:none]
