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
          svg: <svg data-id="rank.topbar.back.ic" data-icon="material:ArrowBack" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M15 18l-6-6 6-6"></path></svg>
    - h1.t1.grow "Ranking"  @(68,36) 257x32
        flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
    - button.btn-back aria="Compartilhar minha posição"  @(337,32) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(348,43) 18x18
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="rank.topbar.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
  - button.card.row aria="Abrir temporada"  @(16,84) 361x56
      display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 14px; margin:8px 0px 0px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px; font-size:13.3333px
    - div.col.grow  @(31,93) 179.6x38
        display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:2px
      - span.t3 "Temporada 1"  @(31,93) 98.3x20
          font-size:16px; font-weight:600; line-height:20px
      - span.cap "termina em 12d 04h"  @(31,115) 112.4x16
          color:rgb(143, 147, 132); font-size:12px; line-height:16px
    - span.chip "Liga Ouro II"  @(220.6,94) 115.4x36
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px 0px 8px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border:1px solid rgba(201, 162, 75, 0.5); border-radius:15px; font-size:12px; font-weight:600; line-height:16px
      - svg  @(229.6,101) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="rank.season.league.badge" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="22" height="22" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
    - svg  @(346,104) 16x16
        flex-shrink:0; overflow:hidden
        svg: <svg data-id="rank.season.go" data-icon="material:ChevronRight" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M9 6l6 6-6 6"></path></svg>
  - section.card.card-brass  @(16,148) 361x112
      flex-shrink:0; padding:12px 14px; margin:8px 0px 0px; background-image:linear-gradient(rgba(201, 162, 75, 0.16), rgb(22, 25, 17)); border:1px solid rgb(201, 162, 75); border-radius:18px
    - div.row  @(31,161) 331x40
        display:flex; align-items:center; gap:10px
      - div.av.av-me "RN"  @(31,161) 40x40
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgb(163, 181, 101); border-radius:50%; box-shadow:rgba(163, 181, 101, 0.14) 0px 0px 0px 2px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:15px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(81,162) 125.8x38
          display:flex; flex-direction:column; flex-grow:1; gap:2px
        - span.t3 "Rafael Nunes"  @(81,162) 125.8x20
            font-size:14px; font-weight:600; line-height:20px
        - span.row.cap "Ouro II · pts"  @(81,184) 125.8x16
            display:flex; align-items:center; gap:5px; color:rgb(143, 147, 132); font-size:12px; line-height:16px
          - svg  @(81,184) 16x16
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="rank.me.league.badge" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="16" height="16" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
          - b "1.542"  @(151.4,184) 32.3x16
              color:rgb(236, 232, 218); font-weight:700
      - div.col  @(216.8,161.5) 58.6x39
          display:flex; flex-direction:column; align-items:center; gap:1px; padding:0px 4px
        - span.eyebrow "Global"  @(223.2,161.5) 45.9x14
            color:rgb(143, 147, 132); font-size:9.5px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.num "#128"  @(220.8,176.5) 50.6x24
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:24px; font-variant-numeric:tabular-nums
      - div.col  @(285.5,161.5) 76.5x39
          display:flex; flex-direction:column; align-items:center; gap:1px; padding:0px 0px 0px 8px
        - span.eyebrow "Estado MT"  @(294.5,161.5) 67.5x14
            color:rgb(143, 147, 132); font-size:9.5px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.num "#7"  @(314.9,176.5) 26.8x24
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:24px; font-variant-numeric:tabular-nums
    - div.row  @(31,211) 331x14
        display:flex; align-items:center; gap:10px; margin:10px 0px 0px
      - div.bar.grow  @(31,215) 267.4x6
          flex-grow:1; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
        - i  @(31,215) 189.9x6
            background-color:rgb(163, 181, 101); border-radius:3px
      - span.row.t-up "+18 pts"  @(308.4,211) 53.6x14
          display:flex; align-items:center; gap:2px; color:rgb(127, 176, 105); font-size:12px; font-weight:700; line-height:14px
        - svg  @(308.4,213.5) 9x9
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="rank.me.delta.ic" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="9" height="9" fill="currentColor" aria-hidden="true"><path d="M12 5l8 14H4z"></path></svg>
    - div.row  @(31,231) 331x16
        display:flex; justify-content:space-between; align-items:center; margin:6px 0px 0px
      - span.cap "Faltam para Ouro I"  @(31,231) 144.5x16
          color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - b "58 pts"  @(72.2,231) 36.7x15
            color:rgb(236, 232, 218); font-weight:700
      - button.row.olive "Ver meu perfil no ranking"  @(201.8,232) 160.2x14
          display:flex; align-items:center; gap:2px; color:rgb(163, 181, 101); font-size:12px; font-weight:600; line-height:14px; text-align:center
        - svg  @(350,233) 12x12
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="rank.me.profile.ic" data-icon="material:ChevronRight" viewBox="0 0 24 24" width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M9 6l6 6-6 6"></path></svg>
  - section.col  @(16,270) 361x122
      display:flex; flex-direction:column; flex-shrink:0; gap:8px; margin:10px 0px 0px
    - nav.seg.seg-sm aria="Escopo"  @(16,270) 361x40
        display:flex; gap:4px; padding:4px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px
      - button.on "Global"  @(21,275) 173.5x30
          flex-grow:1; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:20px; box-shadow:rgb(93, 107, 52) 0px 0px 0px 1px inset; font-size:13px; font-weight:600; line-height:16px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
      - button "Meu estado"  @(198.5,275) 173.5x30
          flex-grow:1; color:rgb(143, 147, 132); border-radius:20px; font-size:13px; font-weight:600; line-height:16px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
    - nav.fchips aria="Distância"  @(16,318) 361x34
        display:flex; gap:6px
      - button.chip.chip-on "Geral"  @(16,318) 85.8x34
          display:flex; justify-content:center; align-items:center; flex-grow:1; gap:6px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:17px; font-size:13px; font-weight:600; line-height:16px; text-align:center
      - button.chip "2,5K"  @(107.8,318) 85.8x34
          display:flex; justify-content:center; align-items:center; flex-grow:1; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:17px; font-size:13px; font-weight:600; line-height:16px; text-align:center
      - button.chip "5K"  @(199.5,318) 85.8x34
          display:flex; justify-content:center; align-items:center; flex-grow:1; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:17px; font-size:13px; font-weight:600; line-height:16px; text-align:center
      - button.chip "10K"  @(291.3,318) 85.8x34
          display:flex; justify-content:center; align-items:center; flex-grow:1; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:17px; font-size:13px; font-weight:600; line-height:16px; text-align:center
    - nav.tabs aria="Período"  @(16,360) 361x32
        display:flex; gap:20px
      - button.on "Temporada atual"  @(16,360) 109.2x31
          padding:0px 2px 10px; color:rgb(163, 181, 101); font-size:13px; font-weight:600; line-height:16px; text-align:center
      - button "Geral histórico"  @(145.2,360) 95.6x31
          padding:0px 2px 10px; color:rgb(143, 147, 132); font-size:13px; font-weight:600; line-height:16px; text-align:center
  - div  @(16,400) 361x451
      display:flex; flex-direction:column; flex-grow:1; gap:6px; padding:0px 0px 12px; margin:8px 0px 0px; overflow:hidden auto
    - div.pod.v-top  @(16,400) 361x180
        display:flex; align-items:flow-end; flex-shrink:0; gap:8px; padding:6px 0px 0px
      - div.pod-c  @(16,455) 115x125
          display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:3px
        - div.av "FA"  @(51.5,455) 44x44
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(46, 59, 58); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:14px; letter-spacing:0.5px
        - span.nm "Felipe Arruda"  @(34.6,502) 77.8x14
            font-size:12px; font-weight:600; line-height:14px; overflow:hidden
        - span.num "3.241"  @(53.6,519) 39.9x18
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; font-variant-numeric:tabular-nums
        - div.pod-b "2"  @(16,540) 115x40
            display:flex; justify-content:center; align-items:center; color:rgb(180, 186, 184); background-color:rgb(29, 33, 22); border-radius:12px 12px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:26px; font-weight:700; line-height:26px
      - div.pod-c  @(139,406) 115x174
          display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:3px
        - span.brass  @(186.5,406) 20x20
            color:rgb(201, 162, 75)
          - svg  @(186.5,406) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="rank.podium.p1.crown" data-icon="material:Stars" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 8l4 4 5-7 5 7 4-4-2 12H5z"></path></svg>
        - div.av "MS"  @(170.5,429) 52x52
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - span.nm "Marina Siqueira"  @(151.1,484) 90.8x14
            font-size:12px; font-weight:600; line-height:14px; overflow:hidden
        - span.num.brass "3.286"  @(174.3,501) 44.3x20
            color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
        - div.pod-b "1"  @(139,524) 115x56
            display:flex; justify-content:center; align-items:center; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:12px 12px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:26px; font-weight:700; line-height:26px
      - div.pod-c  @(262,465) 115x115
          display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:3px
        - div.av "PL"  @(297.5,465) 44x44
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(58, 46, 63); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:14px; letter-spacing:0.5px
        - span.nm "Patrícia Lemos"  @(276.8,512) 85.4x14
            font-size:12px; font-weight:600; line-height:14px; overflow:hidden
        - span.num "3.198"  @(299.6,529) 39.9x18
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; font-variant-numeric:tabular-nums
        - div.pod-b "3"  @(262,550) 115x30
            display:flex; justify-content:center; align-items:center; color:rgb(176, 122, 74); background-color:rgb(29, 33, 22); border-radius:12px 12px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:26px; font-weight:700; line-height:26px
    - div.row.v-top  @(16,592) 361x16
        display:flex; justify-content:center; align-items:center; gap:14px; margin:6px 0px 2px
      - span.cap.row "Zona de promoção"  @(60.4,592) 119.1x16
          display:flex; align-items:center; gap:5px; color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - i.dot.olive  @(60.4,596.5) 7x7
            flex-shrink:0; color:rgb(163, 181, 101); background-color:rgb(163, 181, 101); border-radius:4px
      - span.cap.row "Zona de rebaixamento"  @(193.6,592) 139x16
          display:flex; align-items:center; gap:5px; color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - i.dot.red  @(193.6,596.5) 7x7
            flex-shrink:0; color:rgb(209, 43, 43); background-color:rgb(209, 43, 43); border-radius:4px
    - div.rk.v-top.z-up  @(16,616) 361x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border-radius:14px; box-shadow:rgb(163, 181, 101) 3px 0px 0px 0px inset
      - span.rk-pos "4"  @(28,634) 30x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av "GR"  @(68,628) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(70, 48, 31); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(110,626) 159.1x36
          display:flex; flex-direction:column; flex-grow:1; gap:2px
        - span.t3.ell "Gustavo Ribeiro"  @(110,626) 159.1x20
            font-size:14px; font-weight:600; line-height:20px; overflow:hidden
        - span.lg "Elite"  @(110,648) 159.1x14
            display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:12px
          - svg  @(110,648) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="rank.r4.badge" data-icon="custom:ic_league_elite_0" viewBox="0 0 24 24" width="14" height="14" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-elite)"></path><path d="M10.2 7.2H17L16.6 8.9H11.7L10.5 14.6H15.4L15 16.3H8.3Z" style="fill:var(--bg)"></path><path d="M11.3 10.8H15.1L14.8 12.3H11Z" style="fill:var(--red)"></path></svg>
      - span.rk-elo "3.120"  @(279.1,635) 39.9x18
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - span.trend.t-eq "—"  @(329,638) 36x12
          display:flex; justify-content:flow-end; align-items:center; gap:2px; color:rgb(94, 98, 85); font-size:11px; font-weight:600; line-height:12px
    - div.rk.v-top.z-down  @(16,678) 361x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border-radius:14px; box-shadow:rgb(209, 43, 43) 3px 0px 0px 0px inset
      - span.rk-pos "5"  @(28,696) 30x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av "AM"  @(68,690) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(110,688) 159.1x36
          display:flex; flex-direction:column; flex-grow:1; gap:2px
        - span.t3.ell "Ana Beatriz Moura"  @(110,688) 159.1x20
            font-size:14px; font-weight:600; line-height:20px; overflow:hidden
        - span.lg "Elite"  @(110,710) 159.1x14
            display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:12px
          - svg  @(110,710) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="rank.r5.badge" data-icon="custom:ic_league_elite_0" viewBox="0 0 24 24" width="14" height="14" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-elite)"></path><path d="M10.2 7.2H17L16.6 8.9H11.7L10.5 14.6H15.4L15 16.3H8.3Z" style="fill:var(--bg)"></path><path d="M11.3 10.8H15.1L14.8 12.3H11Z" style="fill:var(--red)"></path></svg>
      - span.rk-elo "3.047"  @(279.1,697) 39.9x18
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - span.trend.t-down "2"  @(329,700) 36x12
          display:flex; justify-content:flow-end; align-items:center; gap:2px; color:rgb(209, 43, 43); font-size:11px; font-weight:600; line-height:12px
        - svg  @(348.1,702) 8x8
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="rank.r5.tr" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="8" height="8" fill="currentColor" aria-hidden="true"><path d="M12 19L4 5h16z"></path></svg>
    - div.dv.v-top  @(16,744) 361x26
        display:flex; align-items:center; flex-shrink:0; gap:8px; margin:4px 0px 0px
      - i  @(16,756.5) 84.9x1
          flex-grow:1; background-color:rgba(236, 232, 218, 0.18)
      - span.row "Diamante I · 2.800–2.999"  @(108.9,750) 175.3x14
          display:flex; align-items:center; gap:6px; color:rgb(143, 147, 132); font-size:10px; font-weight:700; line-height:12px; letter-spacing:1px; text-transform:uppercase
        - svg  @(108.9,750) 14x14
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="rank.dv1.badge" data-icon="custom:ic_league_diamante_1" viewBox="0 0 24 24" width="14" height="14" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-diamante)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
      - i  @(292.1,756.5) 84.9x1
          flex-grow:1; background-color:rgba(236, 232, 218, 0.18)
    - div.rk.v-top.z-up  @(16,776) 361x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border-radius:14px; box-shadow:rgb(163, 181, 101) 3px 0px 0px 0px inset
      - span.rk-pos "6"  @(28,794) 30x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av "HC"  @(68,788) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(110,786) 159.1x36
          display:flex; flex-direction:column; flex-grow:1; gap:2px
        - span.t3.ell "Henrique Castro"  @(110,786) 159.1x20
            font-size:14px; font-weight:600; line-height:20px; overflow:hidden
        - span.lg "Diamante I"  @(110,808) 159.1x14
            display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:12px
          - svg  @(110,808) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="rank.r6.badge" data-icon="custom:ic_league_diamante_1" viewBox="0 0 24 24" width="14" height="14" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-diamante)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
      - span.rk-elo "2.986"  @(279.1,795) 39.9x18
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - span.trend.t-up "1"  @(329,798) 36x12
          display:flex; justify-content:flow-end; align-items:center; gap:2px; color:rgb(127, 176, 105); font-size:11px; font-weight:600; line-height:12px
        - svg  @(350.3,800) 8x8
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="rank.r6.tr" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="8" height="8" fill="currentColor" aria-hidden="true"><path d="M12 5l8 14H4z"></path></svg>
    - div.rk.v-top.z-up  @(16,838) 361x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border-radius:14px; box-shadow:rgb(163, 181, 101) 3px 0px 0px 0px inset
      - span.rk-pos "7"  @(28,856) 30x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av "BV"  @(68,850) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(46, 59, 58); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(110,848) 159.1x36
          display:flex; flex-direction:column; flex-grow:1; gap:2px
        - span.t3.ell "Bianca Vasques"  @(110,848) 159.1x20
            font-size:14px; font-weight:600; line-height:20px; overflow:hidden
        - span.lg "Diamante I"  @(110,870) 159.1x14
            display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:12px
          - svg  @(110,870) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="rank.r7.badge" data-icon="custom:ic_league_diamante_1" viewBox="0 0 24 24" width="14" height="14" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-diamante)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
      - span.rk-elo "2.951"  @(279.1,857) 39.9x18
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - span.trend.t-up "4"  @(329,860) 36x12
          display:flex; justify-content:flow-end; align-items:center; gap:2px; color:rgb(127, 176, 105); font-size:11px; font-weight:600; line-height:12px
        - svg  @(347.7,862) 8x8
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="rank.r7.tr" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="8" height="8" fill="currentColor" aria-hidden="true"><path d="M12 5l8 14H4z"></path></svg>
    - div.rk.v-top  @(16,900) 361x56
        display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border-radius:14px
      - span.rk-pos "8"  @(28,918) 30x20
          color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
      - div.av "CM"  @(68,912) 32x32
          display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(58, 46, 63); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
      - div.col.grow  @(110,910) 159.1x36
          display:flex; flex-direction:column; flex-grow:1; gap:2px
        - span.t3.ell "Caio Menezes"  @(110,910) 159.1x20
            font-size:14px; font-weight:600; line-height:20px; overflow:hidden
        - span.lg "Diamante I"  @(110,932) 159.1x14
            display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:12px
          - svg  @(110,932) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="rank.r8.badge" data-icon="custom:ic_league_diamante_1" viewBox="0 0 24 24" width="14" height="14" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-diamante)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
      - span.rk-elo "2.902"  @(279.1,919) 39.9x18
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - span.trend.t-down "1"  @(329,922) 36x12
          display:flex; justify-content:flow-end; align-items:center; gap:2px; color:rgb(209, 43, 43); font-size:11px; font-weight:600; line-height:12px
        - svg  @(350.3,924) 8x8
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="rank.r8.tr" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="8" height="8" fill="currentColor" aria-hidden="true"><path d="M12 19L4 5h16z"></path></svg>
    - div.cap.v-near  [display:none]
    - div.dv.v-near  [display:none]
    - div.rk.v-near.z-up  [display:none]
    - div.rk.v-near  [display:none]
    - div.rk.rk-me.v-near  [display:none]
    - div.rk.v-near  [display:none]
    - div.rk.v-near.z-down  [display:none]
    - div.ld.v-near aria="Carregando mais"  @(16,962) 361x30
        display:flex; justify-content:center; flex-shrink:0; gap:6px; padding:12px 0px
      - i  @(181.5,974) 6x6
          background-color:rgb(94, 98, 85); border-radius:3px
      - i  @(193.5,974) 6x6
          background-color:rgb(94, 98, 85); border-radius:3px
      - i  @(205.5,974) 6x6
          background-color:rgb(94, 98, 85); border-radius:3px
    - div.card-dash.col.v-vazio  [display:none]
  - div.sheet-wrap  [display:none]
