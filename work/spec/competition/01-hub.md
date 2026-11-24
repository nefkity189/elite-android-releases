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
- Saira Stencil One  loaded=True
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
  - header.topbar  @(16,28) 361x48
      display:flex; align-items:center; flex-shrink:0; gap:12px
    - button.btn-back aria="Voltar"  @(16,32) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(26,42) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="hub.topbar.back.ic" data-icon="material:ArrowBack" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M15 18l-6-6 6-6"></path></svg>
    - div.logo.grow  @(68,39) 181.2x26
        display:flex; align-items:center; flex-grow:1; gap:8px
      - svg  @(68,39) 26x26
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="hub.topbar.logo.mark" data-icon="drawable:ic_elite_blade" viewBox="0 0 64 64" width="26" height="26" aria-hidden="true"><path d="M19.1 8H55.1L53.2 17H27.2L20.8 47H46.8L44.9 56H8.9Z" style="fill:var(--olive)"></path><path d="M25.1 27H45.1L43.1 36H23.1Z" style="fill:var(--red)"></path></svg>
      - span.logo-t "ELITE"  @(102,42) 62.6x20
          font-family:"Saira Stencil One", Impact, sans-serif; font-size:17px; line-height:20px; letter-spacing:3.4px
    - div.chip.chip-on  @(261.2,37) 115.8x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12px; font-weight:600; line-height:16px
      - svg  @(274.2,45) 14x14
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="hub.topbar.clock.ic" data-icon="material:Schedule" viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"></circle><path d="M12 7v5l3 2"></path></svg>
      - span "Agora 12:42"  @(294.2,44) 69.8x16
  - section.col  @(16,82) 361x50
      display:flex; flex-direction:column; gap:4px; margin:6px 0px 0px
    - p.eyebrow.olive "Foco, Missão dada, Missão cumprida."  @(16,82) 361x14
        color:rgb(163, 181, 101); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
    - h1.t1 "Competição Online"  @(16,100) 361x32
        font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
  - section  @(16,144) 361x60
      display:grid; gap:10px; margin:12px 0px 0px; grid-template-columns:169.219px 181.781px
    - button.card.sc aria="Abrir ranking"  @(16,144) 169.2x60
        display:flex; align-items:center; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(29,158) 32x32
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="hub.shortcuts.ranking.badge" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="32" height="32" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
      - div.col.grow  @(71,155.5) 75.2x37
          display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:1px
        - span.t3 "Ranking"  @(71,155.5) 62.4x20
            font-size:16px; font-weight:600; line-height:20px
        - span.cap.ell "Ouro II · 128º"  @(71,176.5) 73.2x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px; overflow:hidden
      - svg  @(156.2,166) 16x16
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="hub.shortcuts.ranking.go" data-icon="material:ChevronRight" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M9 6l6 6-6 6"></path></svg>
    - button.card.sc aria="Abrir conquistas"  @(195.2,144) 181.8x60
        display:flex; align-items:center; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - div.sc-ic  @(208.2,158) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:16px
        - svg  @(215.2,165) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="hub.shortcuts.achievements.badge.ic" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6M18 9h1.5a2.5 2.5 0 0 0 0-5H18M4 22h16M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22M18 2H6v7a6 6 0 0 0 12 0V2Z"></path></svg>
      - div.col.grow  @(250.2,155.5) 87.8x37
          display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:1px
        - span.t3 "Conquistas"  @(250.2,155.5) 87.8x20
            font-size:16px; font-weight:600; line-height:20px
        - span.cap.ell "7 de 24"  @(250.2,176.5) 42.8x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px; overflow:hidden
      - svg  @(348,166) 16x16
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="hub.shortcuts.achievements.go" data-icon="material:ChevronRight" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M9 6l6 6-6 6"></path></svg>
  - nav.seg aria="Distância"  @(16,216) 361x48
      display:flex; gap:4px; padding:4px; margin:12px 0px 0px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px
    - button "2,5K"  @(21,221) 114.3x38
        flex-grow:1; color:rgb(143, 147, 132); border-radius:20px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
    - button.on "5K"  @(139.3,221) 114.3x38
        flex-grow:1; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:20px; box-shadow:rgb(93, 107, 52) 0px 0px 0px 1px inset; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
    - button "10K"  @(257.7,221) 114.3x38
        flex-grow:1; color:rgb(143, 147, 132); border-radius:20px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
  - div.row  @(16,278) 361x23
      display:flex; justify-content:space-between; align-items:center; margin:14px 0px 8px
    - h2.t2 "Missões de hoje"  @(16,278) 133.6x23
        font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
    - span.cap "02:00 – 21:00"  @(300,281.5) 77x16
        color:rgb(143, 147, 132); font-size:12px; line-height:16px
  - section  @(16,309) 361x542
      display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:0px 0px 12px; overflow:hidden auto
    - article.card.m.m-compact  @(16,123) 361x83
        display:flex; flex-direction:column; flex-shrink:0; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,136) 331x57
          display:flex; align-items:center; gap:12px
        - div.col  @(31,136) 66x57
            display:flex; flex-direction:column; align-items:flow-start; gap:5px
          - span.m-time "11:00"  @(31,136) 66x28
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; letter-spacing:0.5px; font-variant-numeric:tabular-nums
          - span.dist "5K"  @(31,169) 34x24
              display:flex; justify-content:center; align-items:center; padding:0px 9px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:7px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:16px; letter-spacing:0.5px
        - div.col.grow  @(109,142.5) 193x44
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.badge.b-none "Não iniciada"  @(109,142.5) 102.6x24
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.18); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - span.cap "por falta de competidores"  @(109,170.5) 146.3x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - button.btn.btn-share.btn-icon aria="Compartilhar missão 11:00"  @(314,140.5) 48x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
          - svg  @(328,154.5) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="hub.m1100.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
    - article.card.m.m-compact  @(16,216) 361x83
        display:flex; flex-direction:column; flex-shrink:0; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,229) 331x57
          display:flex; align-items:center; gap:12px
        - div.col  @(31,229) 66x57
            display:flex; flex-direction:column; align-items:flow-start; gap:5px
          - span.m-time "12:00"  @(31,229) 66x28
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; letter-spacing:0.5px; font-variant-numeric:tabular-nums
          - span.dist "5K"  @(31,262) 34x24
              display:flex; justify-content:center; align-items:center; padding:0px 9px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:7px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:16px; letter-spacing:0.5px
        - div.col.grow  @(109,235.5) 193x44
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.badge.b-live "Em andamento"  @(109,235.5) 128.6x24
              display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(209, 43, 43); background-color:rgba(209, 43, 43, 0.15); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
            - i.dot.live-dot  @(119,244) 7x7
                flex-shrink:0; background-color:rgb(209, 43, 43); border-radius:4px; opacity:0.303063; animation:1.4s ease-in-out infinite live-pulse
          - span.cap "3 competidores correndo"  @(109,263.5) 143.8x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - button.btn.btn-share.btn-icon aria="Compartilhar missão 12:00"  @(314,233.5) 48x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
          - svg  @(328,247.5) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="hub.m1200.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
    - article.card.m  @(16,309) 361x162
        display:flex; flex-direction:column; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,324) 331x36
          display:flex; align-items:center; gap:10px
        - span.m-time "13:00"  @(31,324) 84.1x36
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:36px; font-weight:700; line-height:36px; letter-spacing:0.5px; font-variant-numeric:tabular-nums
        - span.dist "5K"  @(125.1,330) 34x24
            display:flex; justify-content:center; align-items:center; padding:0px 9px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:7px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:16px; letter-spacing:0.5px
        - span.grow  @(169.2,342) 26.3x0
            flex-grow:1
        - span.badge.b-one "Falta 1 competidor"  @(205.4,330) 156.6x24
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(224, 169, 58); background-color:rgba(224, 169, 58, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - i.dot  @(215.4,338.5) 7x7
              flex-shrink:0; background-color:rgb(224, 169, 58); border-radius:4px
      - div.row  @(31,368) 331x28
          display:flex; align-items:center; gap:10px; margin:8px 0px 0px
        - div.stack  @(31,368) 48x28
            display:flex
          - div.av "RN"  @(31,368) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:11px; font-weight:700; line-height:14px; letter-spacing:0.5px
          - div.av.av-empty  @(51,368) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; margin:0px 0px 0px -8px; color:rgb(94, 98, 85); border:1px dashed rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:14px; letter-spacing:0.5px
            - svg  @(59.5,376.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="hub.m1300.av2.ic" data-icon="material:Add" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 5v14M5 12h14"></path></svg>
        - span.num "1/2"  @(89,372) 28.2x20
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
        - span.cap "Mínimo 2 competidores"  @(127.2,374) 133.5x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.row  @(31,408) 331x48
          display:flex; align-items:center; gap:10px; margin:12px 0px 0px
        - button.btn.btn-red.grow "Entrar na missão"  @(31,408) 158.3x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - button.btn.btn-share.btn-icon aria="Compartilhar missão 13:00"  @(199.3,408) 48x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
          - svg  @(213.3,422) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="hub.m1300.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
    - article.card.m  @(16,481) 361x162
        display:flex; flex-direction:column; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,496) 331x36
          display:flex; align-items:center; gap:10px
        - span.m-time "14:00"  @(31,496) 84.1x36
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:36px; font-weight:700; line-height:36px; letter-spacing:0.5px; font-variant-numeric:tabular-nums
        - span.dist "5K"  @(125.1,502) 34x24
            display:flex; justify-content:center; align-items:center; padding:0px 9px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:7px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:16px; letter-spacing:0.5px
        - span.grow  @(169.2,514) 67.7x0
            flex-grow:1
        - span.badge.b-open "Aguardando"  @(246.8,502) 115.2x24
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - i.dot  @(256.8,510.5) 7x7
              flex-shrink:0; background-color:rgb(163, 181, 101); border-radius:4px
      - div.row  @(31,540) 331x28
          display:flex; align-items:center; gap:10px; margin:8px 0px 0px
        - div.stack  @(31,540) 48x28
            display:flex
          - div.av.av-empty  @(31,540) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(94, 98, 85); border:1px dashed rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:14px; letter-spacing:0.5px
            - svg  @(39.5,548.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="hub.m1400.av1.ic" data-icon="material:Add" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 5v14M5 12h14"></path></svg>
          - div.av.av-empty  @(51,540) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; margin:0px 0px 0px -8px; color:rgb(94, 98, 85); border:1px dashed rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:14px; letter-spacing:0.5px
            - svg  @(59.5,548.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="hub.m1400.av2.ic" data-icon="material:Add" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 5v14M5 12h14"></path></svg>
        - span.num "0/2"  @(89,544) 28.2x20
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
        - span.cap "Mínimo 2 competidores"  @(127.2,546) 133.5x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.row  @(31,580) 331x48
          display:flex; align-items:center; gap:10px; margin:12px 0px 0px
        - button.btn.btn-red.grow "Entrar na missão"  @(31,580) 158.3x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - button.btn.btn-share.btn-icon aria="Compartilhar missão 14:00"  @(199.3,580) 48x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
          - svg  @(213.3,594) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="hub.m1400.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
    - article.card.m  @(16,653) 361x162
        display:flex; flex-direction:column; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,668) 331x36
          display:flex; align-items:center; gap:10px
        - span.m-time "15:00"  @(31,668) 84.1x36
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:36px; font-weight:700; line-height:36px; letter-spacing:0.5px; font-variant-numeric:tabular-nums
        - span.dist "5K"  @(125.1,674) 34x24
            display:flex; justify-content:center; align-items:center; padding:0px 9px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:7px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:16px; letter-spacing:0.5px
        - span.grow  @(169.2,686) 71.4x0
            flex-grow:1
        - span.badge.b-conf "Confirmada"  @(250.5,674) 111.5x24
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(14, 19, 6); background-color:rgb(127, 176, 105); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - i.dot  @(260.5,682.5) 7x7
              flex-shrink:0; background-color:rgb(14, 19, 6); border-radius:4px
      - div.row  @(31,712) 331x28
          display:flex; align-items:center; gap:10px; margin:8px 0px 0px
        - div.stack  @(31,712) 48x28
            display:flex
          - div.av "CD"  @(31,712) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:11px; font-weight:700; line-height:14px; letter-spacing:0.5px
          - div.av "BT"  @(51,712) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; margin:0px 0px 0px -8px; background-color:rgb(46, 59, 58); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:11px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - span.num "2/2"  @(89,716) 28.2x20
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
        - span.cap "Mínimo 2 competidores"  @(127.2,718) 133.5x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.row  @(31,752) 331x48
          display:flex; align-items:center; gap:10px; margin:12px 0px 0px
        - button.btn.btn-red.grow "Entrar na missão"  @(31,752) 158.3x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - button.btn.btn-share.btn-icon aria="Compartilhar missão 15:00"  @(199.3,752) 48x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
          - svg  @(213.3,766) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="hub.m1500.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
    - article.card-dash.m.m-compact  @(16,825) 361x80
        display:flex; flex-direction:column; flex-shrink:0; padding:12px 14px; border:1px dashed rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,838) 331x54
          display:flex; align-items:center; gap:12px
        - span.m-time.muted "16:00"  @(31,851) 66x28
            color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; letter-spacing:0.5px; font-variant-numeric:tabular-nums
        - div.col.grow  @(109,838) 178.9x54
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - span.t3 "Sem missão aberta"  @(109,838) 178.9x20
              font-weight:600; line-height:20px
          - span.cap "Neste horário ainda não há sala."  @(109,860) 178.9x32
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - button.btn.btn-ghost "Abrir"  @(299.9,845) 62.1x40
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 14px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px; font-size:13px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
    - article.card.m  @(16,915) 361x162
        display:flex; flex-direction:column; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,930) 331x36
          display:flex; align-items:center; gap:10px
        - span.m-time "17:00"  @(31,930) 84.1x36
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:36px; font-weight:700; line-height:36px; letter-spacing:0.5px; font-variant-numeric:tabular-nums
        - span.dist "5K"  @(125.1,936) 34x24
            display:flex; justify-content:center; align-items:center; padding:0px 9px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:7px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:16px; letter-spacing:0.5px
        - span.grow  @(169.2,948) 67.7x0
            flex-grow:1
        - span.badge.b-open "Aguardando"  @(246.8,936) 115.2x24
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - i.dot  @(256.8,944.5) 7x7
              flex-shrink:0; background-color:rgb(163, 181, 101); border-radius:4px
      - div.row  @(31,974) 331x28
          display:flex; align-items:center; gap:10px; margin:8px 0px 0px
        - div.stack  @(31,974) 48x28
            display:flex
          - div.av.av-empty  @(31,974) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(94, 98, 85); border:1px dashed rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:14px; letter-spacing:0.5px
            - svg  @(39.5,982.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="hub.m1700.av1.ic" data-icon="material:Add" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 5v14M5 12h14"></path></svg>
          - div.av.av-empty  @(51,974) 28x28
              display:flex; justify-content:center; align-items:center; flex-shrink:0; margin:0px 0px 0px -8px; color:rgb(94, 98, 85); border:1px dashed rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:14px; letter-spacing:0.5px
            - svg  @(59.5,982.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="hub.m1700.av2.ic" data-icon="material:Add" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 5v14M5 12h14"></path></svg>
        - span.num "0/2"  @(89,978) 28.2x20
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
        - span.cap "Mínimo 2 competidores"  @(127.2,980) 133.5x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.row  @(31,1014) 331x48
          display:flex; align-items:center; gap:10px; margin:12px 0px 0px
        - button.btn.btn-red.grow "Entrar na missão"  @(31,1014) 158.3x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - button.btn.btn-share.btn-icon aria="Compartilhar missão 17:00"  @(199.3,1014) 48x48
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
          - svg  @(213.3,1028) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="hub.m1700.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
  - section.card-dash.col  [display:none]
