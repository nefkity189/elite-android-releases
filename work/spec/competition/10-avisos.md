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
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 16px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - header.topbar  @(16,28) 361x48
      display:flex; align-items:center; flex-shrink:0; gap:12px
    - button.btn-back aria="Voltar"  @(16,32) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(26,42) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="avi.topbar.back.ic" data-icon="material:ArrowBack" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M15 18l-6-6 6-6"></path></svg>
    - h1.t1.grow "Avisos"  @(68,36) 309x32
        flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
  - nav.tabs  @(16,82) 361x32
      display:flex; flex-shrink:0; gap:20px; margin:6px 0px 0px
    - button.on "Banners"  @(16,82) 55.9x31
        padding:0px 2px 10px; color:rgb(163, 181, 101); font-size:13px; font-weight:600; line-height:16px; text-align:center
    - button "Push"  @(91.9,82) 35.5x31
        padding:0px 2px 10px; color:rgb(143, 147, 132); font-size:13px; font-weight:600; line-height:16px; text-align:center
  - div  @(16,126) 361x725
      display:flex; flex-direction:column; flex-grow:1; gap:8px; padding:0px 0px 12px; margin:12px 0px 0px; overflow:hidden auto
    - div.col.pg-b  @(16,126) 361x876
        display:flex; flex-direction:column; flex-shrink:0; gap:8px
      - span.sec "Banners dentro do app"  @(18,126) 357x12
          margin:0px 2px; color:rgb(94, 98, 85); font-size:10px; font-weight:700; line-height:12px; letter-spacing:1.2px; text-transform:uppercase
      - aside.banner.bn-amber  @(16,146) 361x80
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(224, 169, 58, 0.14), rgb(22, 25, 17)); border:1px solid rgba(224, 169, 58, 0.45); border-radius:16px
        - div.banner-ico  @(31,168) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(224, 169, 58); background-color:rgba(224, 169, 58, 0.14); border-radius:18px
          - svg  @(40,177) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.b10.ic" data-icon="material:Notifications" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
        - div.col.grow  @(79,159) 191.6x54
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Faltam 10 minutos"  @(79,159) 191.6x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Complete o mínimo de 2 competidores. Convide alguém."  @(79,181) 191.6x32
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - button.btn.btn-share "Convidar"  @(282.6,168) 79.4x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:12px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
      - aside.banner.bn-red  @(16,234) 361x80
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(209, 43, 43, 0.15), rgb(22, 25, 17)); border:1px solid rgba(209, 43, 43, 0.5); border-radius:16px
        - div.banner-ico  @(31,256) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(209, 43, 43); background-color:rgba(209, 43, 43, 0.15); border-radius:18px
          - svg  @(40,265) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.b5.ic" data-icon="material:Warning" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M10.3 3.9L1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.9a2 2 0 0 0-3.4 0zM12 9v4M12 17h.01"></path></svg>
        - div.col.grow  @(79,247) 191.6x54
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Faltam 5 minutos"  @(79,247) 191.6x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Sem 2 competidores, a missão não inicia."  @(79,269) 191.6x32
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - button.btn.btn-share "Convidar"  @(282.6,256) 79.4x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:12px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
      - aside.banner.bn-olive  @(16,322) 361x64
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(93, 107, 52); border-radius:16px
        - div.banner-ico  @(31,336) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:18px
          - svg  @(40,345) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bin.ic" data-icon="material:Check" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M20 6L9 17l-5-5"></path></svg>
        - div.col.grow  @(79,335) 283x38
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Entrada confirmada"  @(79,335) 283x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Você está na Missão 5K das 13:00."  @(79,357) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - aside.banner.bn-amber  @(16,394) 361x84
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(224, 169, 58, 0.14), rgb(22, 25, 17)); border:1px solid rgba(224, 169, 58, 0.45); border-radius:16px
        - div.banner-ico  @(31,418) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(224, 169, 58); background-color:rgba(224, 169, 58, 0.14); border-radius:18px
          - svg  @(40,427) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bone.ic" data-icon="material:Groups" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
        - div.col.grow  @(79,407) 191.6x58
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Falta 1 competidor para a missão iniciar"  @(79,407) 191.6x40
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Compartilhe o convite."  @(79,449) 191.6x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - button.btn.btn-share "Convidar"  @(282.6,418) 79.4x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:12px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
      - aside.banner.bn-ok  @(16,486) 361x64
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(127, 176, 105, 0.15), rgb(22, 25, 17)); border:1px solid rgba(127, 176, 105, 0.45); border-radius:16px
        - div.banner-ico  @(31,500) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(127, 176, 105); background-color:rgba(127, 176, 105, 0.15); border-radius:18px
          - svg  @(40,509) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bconf.ic" data-icon="material:Flag" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1zM4 22v-7"></path></svg>
        - div.col.grow  @(79,499) 283x38
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Missão confirmada. Prepare-se."  @(79,499) 283x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Início às 13:00."  @(79,521) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - aside.banner.bn-mute  @(16,558) 361x84
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px
        - div.banner-ico  @(31,582) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:18px
          - svg  @(40,591) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bnone.ic" data-icon="material:Schedule" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"></circle><path d="M12 7v5l3 2"></path></svg>
        - div.col.grow  @(79,571) 188.1x58
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Missão não iniciada por falta de competidores"  @(79,571) 188.1x40
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Missão 5K das 13:00."  @(79,613) 188.1x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - button.btn.btn-share "Convocar"  @(279.1,582) 82.9x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:24px; font-size:12px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
      - aside.banner.bn-brass  @(16,650) 361x64
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(201, 162, 75, 0.16), rgb(22, 25, 17)); border:1px solid rgba(201, 162, 75, 0.5); border-radius:16px
        - div.banner-ico  @(31,664) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:18px
          - svg  @(40,673) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bach.ic" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6M18 9h1.5a2.5 2.5 0 0 0 0-5H18M4 22h16M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22M18 2H6v7a6 6 0 0 0 12 0V2Z"></path></svg>
        - div.col.grow  @(79,663) 283x38
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Conquista desbloqueada"  @(79,663) 283x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Pódio · global"  @(79,685) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - aside.banner.bn-brass  @(16,722) 361x64
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(201, 162, 75, 0.16), rgb(22, 25, 17)); border:1px solid rgba(201, 162, 75, 0.5); border-radius:16px
        - div.banner-ico  @(31,736) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:18px
          - svg  @(40,745) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bachl.ic" data-icon="material:Place" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
        - div.col.grow  @(79,735) 283x38
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Conquista local desbloqueada"  @(79,735) 283x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Top 10 do estado · MT"  @(79,757) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - aside.banner.bn-ok  @(16,794) 361x64
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(127, 176, 105, 0.15), rgb(22, 25, 17)); border:1px solid rgba(127, 176, 105, 0.45); border-radius:16px
        - div.banner-ico  @(31,808) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(127, 176, 105); background-color:rgba(127, 176, 105, 0.15); border-radius:18px
          - svg  @(40,817) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bup.ic" data-icon="material:BarChart" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 20V10M12 20V4M6 20v-6"></path></svg>
        - div.col.grow  @(79,807) 283x38
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Você subiu para Ouro I"  @(79,807) 283x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Promoção de liga na temporada 1."  @(79,829) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - aside.banner.bn-red  @(16,866) 361x64
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(209, 43, 43, 0.15), rgb(22, 25, 17)); border:1px solid rgba(209, 43, 43, 0.5); border-radius:16px
        - div.banner-ico  @(31,880) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(209, 43, 43); background-color:rgba(209, 43, 43, 0.15); border-radius:18px
          - svg  @(40,889) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bdown.ic" data-icon="material:BarChart" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 20V10M12 20V4M6 20v-6"></path></svg>
        - div.col.grow  @(79,879) 283x38
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Você caiu para Ouro III"  @(79,879) 283x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Rebaixamento de divisão."  @(79,901) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - aside.banner.bn-amber  @(16,938) 361x64
          display:flex; align-items:center; flex-shrink:0; gap:12px; padding:12px 14px; background-image:linear-gradient(90deg, rgba(224, 169, 58, 0.14), rgb(22, 25, 17)); border:1px solid rgba(224, 169, 58, 0.45); border-radius:16px
        - div.banner-ico  @(31,952) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(224, 169, 58); background-color:rgba(224, 169, 58, 0.14); border-radius:18px
          - svg  @(40,961) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="avi.bend.ic" data-icon="material:CalendarMonth" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="4" width="18" height="18" rx="2"></rect><path d="M16 2v4M8 2v4M3 10h18"></path></svg>
        - div.col.grow  @(79,951) 283x38
            display:flex; flex-direction:column; flex-grow:1; gap:2px
          - p.t3 "Fim da Temporada 1"  @(79,951) 283x20
              font-size:14px; font-weight:600; line-height:20px
          - p.cap "Reset parcial de pontos em 12d 04h."  @(79,973) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
    - div.col.pg-p  [display:none]
  - div.sheet-wrap  [display:none]
