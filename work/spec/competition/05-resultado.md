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
- **enter-up** on `section.card.card-brass.enter.e1`: 320ms, delay 0ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(16px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(0px)", "opacity": "1"}
- **enter-up** on `section.card.enter.e2`: 320ms, delay 60ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(16px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(0px)", "opacity": "1"}
- **enter-up** on `section.card.enter.e3`: 320ms, delay 120ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(16px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(0px)", "opacity": "1"}

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 16px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - header.topbar  @(16,28) 361x56
      display:flex; align-items:center; flex-shrink:0; gap:12px
    - button.btn-back aria="Fechar resultado"  @(16,36) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(26,46) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="res.topbar.close.ic" data-icon="material:Close" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 6L6 18M6 6l12 12"></path></svg>
    - div.col.grow  @(68,32) 309x48
        display:flex; flex-direction:column; flex-grow:1; gap:2px
      - p.eyebrow "Missão 5K · 13:00"  @(68,32) 309x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
      - h1.t1.olive "Missão cumprida"  @(68,48) 309x32
          color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
  - div  @(16,84) 361x684
      display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:0px 0px 12px; overflow:hidden auto
    - section.card.card-brass.enter.e1  @(16,84) 361x353
        flex-shrink:0; padding:14px 16px; background-image:linear-gradient(rgba(201, 162, 75, 0.16), rgb(22, 25, 17)); border:1px solid rgb(201, 162, 75); border-radius:18px; transform:matrix(1, 0, 0, 1, 0, 0); animation:0.32s cubic-bezier(0.2, 0.8, 0.2, 1) both enter-up
      - div.row  @(33,99) 327x94
          display:flex; align-items:center; gap:14px
        - div.col  @(33,102) 57.1x88
            display:flex; flex-direction:column; align-items:flow-start; gap:2px
          - span.num "2º"  @(33,102) 57.1x72
              color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:72px; font-weight:700; line-height:72px; font-variant-numeric:tabular-nums
          - span.eyebrow "lugar"  @(33,176) 43.9x14
              color:rgb(201, 162, 75); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - div.col.grow  @(104.1,99) 255.9x94
            display:flex; flex-direction:column; align-items:flow-end; flex-grow:1; gap:6px; text-align:right
          - div.av.av-me "RN"  @(316,99) 44x44
              display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgb(163, 181, 101); border-radius:50%; box-shadow:rgba(163, 181, 101, 0.14) 0px 0px 0px 2px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:14px; letter-spacing:0.5px
          - span.t3 "Rafael Nunes"  @(257.7,149) 102.3x20
              font-weight:600; line-height:20px
          - span.row.cap "Liga Ouro II"  @(271.2,175) 88.8x18
              display:flex; align-items:center; gap:6px; color:rgb(143, 147, 132); font-size:12px; line-height:16px
            - svg  @(271.2,175) 18x18
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="res.me.league.badge" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="18" height="18" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
      - div.hr  @(33,205) 327x1
          flex-shrink:0; margin:12px 0px; background-color:rgba(236, 232, 218, 0.1)
      - div.row  @(33,218) 327x46
          display:flex; align-items:center; gap:8px
        - div.kv  @(33,218) 103.7x46
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.eyebrow "Tempo total"  @(33,218) 85.2x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num "24:18"  @(33,236) 63.5x28
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
        - div.kv  @(144.7,218) 103.7x46
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.eyebrow "Pace médio"  @(144.7,218) 74.4x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num "4:52"  @(144.7,236) 73.9x28
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
            - span.muted "/km"  @(194.2,247) 24.4x17
                color:rgb(143, 147, 132); font-size:14px; line-height:14px
        - div.kv  @(256.3,218) 103.7x46
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.eyebrow "Elo"  @(256.3,218) 23x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num.olive "+18"  @(256.3,236) 40.2x28
              color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
      - div.row  @(33,276) 327x24
          display:flex; justify-content:space-between; align-items:center; gap:10px; margin:12px 0px 0px
        - span.cap "Pontuação geral"  @(33,280) 128.1x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
          - b "1.542"  @(128.8,280) 32.3x15
              color:rgb(236, 232, 218); font-weight:700
        - span.badge.b-conf "+18 pts"  @(279.2,276) 80.8x24
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(127, 176, 105); background-color:rgba(127, 176, 105, 0.15); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - svg  @(289.2,283.5) 9x9
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="res.me.elo.delta.ic" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="9" height="9" fill="currentColor" aria-hidden="true"><path d="M12 5l8 14H4z"></path></svg>
      - div.row  @(33,312) 327x46
          display:flex; align-items:center; gap:10px; margin:12px 0px 0px
        - div.rkchip  @(33,312) 158.5x46
            display:flex; align-items:center; flex-grow:1; gap:8px; padding:0px 12px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px
          - svg  @(46,327) 16x16
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="res.me.rank-g.ic" data-icon="material:Star" viewBox="0 0 24 24" width="16" height="16" fill="currentColor" stroke="currentColor" stroke-width="1.2" aria-hidden="true"><path d="M12 2l3.1 6.3 6.9 1-5 4.9 1.2 6.9L12 17.8 5.8 21.1 7 14.2 2 9.3l6.9-1z"></path></svg>
          - div.col  @(70,317.5) 45.9x35
              display:flex; flex-direction:column; gap:1px
            - span.eyebrow "Global"  @(70,317.5) 45.9x14
                color:rgb(143, 147, 132); font-size:9.5px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
            - span.num "#128"  @(70,332.5) 45.9x20
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
          - span.trend.t-up "9"  @(160.5,329) 18x12
              display:flex; justify-content:flow-end; align-items:center; gap:2px; margin:0px 0px 0px 36.5781px; color:rgb(127, 176, 105); font-size:11px; font-weight:600; line-height:12px
            - svg  @(160.5,330.5) 9x9
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="res.me.rank-g.tr" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="9" height="9" fill="currentColor" aria-hidden="true"><path d="M12 5l8 14H4z"></path></svg>
        - div.rkchip  @(201.5,312) 158.5x46
            display:flex; align-items:center; flex-grow:1; gap:8px; padding:0px 12px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px
          - svg  @(214.5,327) 16x16
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="res.me.rank-e.ic" data-icon="material:Place" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
          - div.col  @(238.5,317.5) 75.5x35
              display:flex; flex-direction:column; gap:1px
            - span.eyebrow "Estado · MT"  @(238.5,317.5) 75.5x14
                color:rgb(143, 147, 132); font-size:9.5px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
            - span.num "#7"  @(238.5,332.5) 75.5x20
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
          - span.trend.t-up "1"  @(331.3,329) 15.7x12
              display:flex; justify-content:flow-end; align-items:center; gap:2px; margin:0px 0px 0px 9.3125px; color:rgb(127, 176, 105); font-size:11px; font-weight:600; line-height:12px
            - svg  @(331.3,330.5) 9x9
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="res.me.rank-e.tr" data-icon="material:ArrowDropUp" viewBox="0 0 24 24" width="9" height="9" fill="currentColor" aria-hidden="true"><path d="M12 5l8 14H4z"></path></svg>
      - div.row  @(33,370) 327x52
          display:flex; align-items:center; gap:8px; margin:12px 0px 0px
        - span.eyebrow "Conquistas da missão"  @(33,370) 327x14
            color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.chip "Primeira missão cumprida"  @(33,392) 196.7x30
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border:1px solid rgba(201, 162, 75, 0.5); border-radius:15px; font-size:12px; font-weight:600; line-height:16px
          - svg  @(46,400) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="res.me.achv.a1.ic" data-icon="material:WorkspacePremium" viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="9" r="6"></circle><path d="M8.5 14.5L7 22l5-3 5 3-1.5-7.5"></path></svg>
        - span.chip "Pódio"  @(237.7,392) 78.9x30
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border:1px solid rgba(201, 162, 75, 0.5); border-radius:15px; font-size:12px; font-weight:600; line-height:16px
          - svg  @(250.7,400) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="res.me.achv.a2.ic" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M6 9H4.5a2.5 2.5 0 0 1 0-5H6M18 9h1.5a2.5 2.5 0 0 0 0-5H18M4 22h16M10 14.66V17c0 .55-.47.98-.97 1.21C7.85 18.75 7 20.24 7 22M14 14.66V17c0 .55.47.98.97 1.21C16.15 18.75 17 20.24 17 22M18 2H6v7a6 6 0 0 0 12 0V2Z"></path></svg>
    - section.card.enter.e2  @(16,447) 361x207
        flex-shrink:0; padding:12px 10px 8px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px; transform:matrix(1, 0, 0, 1, 0, 0); animation:0.32s cubic-bezier(0.2, 0.8, 0.2, 1) 0.06s both enter-up
      - div.row  @(31,460) 331x23
          display:flex; justify-content:space-between; align-items:center; margin:0px 4px 6px
        - h2.t2 "Classificação da missão"  @(31,460) 201.5x23
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - span.cap "Elo da missão"  @(283.8,463.5) 78.2x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.st  @(27,489) 339x52
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 10px; border-radius:14px
        - span.rk-pos.brass  @(37,505) 26x20
            color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
          - svg  @(37,505) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="res.standings.p1.medal" data-icon="material:WorkspacePremium" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="9" r="6"></circle><path d="M8.5 14.5L7 22l5-3 5 3-1.5-7.5"></path></svg>
        - div.av "CD"  @(73,499) 32x32
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - div.col.grow  @(115,496.5) 194.7x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Camila Duarte"  @(115,496.5) 194.7x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "4:47 /km"  @(115,517.5) 194.7x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.col  @(319.7,498.5) 36.3x33
            display:flex; flex-direction:column; align-items:flow-end; gap:1px
          - span.tm "23:55"  @(319.7,498.5) 36.3x16
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; text-align:right; font-variant-numeric:tabular-nums
          - span.cap.olive "+34"  @(333,515.5) 23x16
              color:rgb(163, 181, 101); font-size:12px; line-height:16px
      - div.st.st-me  @(27,541) 339x52
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 10px; background-color:rgba(163, 181, 101, 0.14); border-radius:14px; box-shadow:rgb(93, 107, 52) 0px 0px 0px 1px inset
        - span.rk-pos.olive "2"  @(37,557) 26x20
            color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
        - div.av.av-me "RN"  @(73,551) 32x32
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgb(163, 181, 101); border-radius:50%; box-shadow:rgba(163, 181, 101, 0.14) 0px 0px 0px 2px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - div.col.grow  @(115,548.5) 194.7x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Você"  @(115,548.5) 194.7x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "4:52 /km"  @(115,569.5) 194.7x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.col  @(319.7,550.5) 36.3x33
            display:flex; flex-direction:column; align-items:flow-end; gap:1px
          - span.tm "24:18"  @(319.7,550.5) 36.3x16
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; text-align:right; font-variant-numeric:tabular-nums
          - span.cap.olive "+18"  @(335.8,567.5) 20.3x16
              color:rgb(163, 181, 101); font-size:12px; line-height:16px
      - div.st  @(27,593) 339x52
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 10px; border-radius:14px
        - span.rk-pos "3"  @(37,609) 26x20
            color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
        - div.av "BT"  @(73,603) 32x32
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(46, 59, 58); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - div.col.grow  @(115,600.5) 194.7x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Bruno Tavares"  @(115,600.5) 194.7x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "5:12 /km"  @(115,621.5) 194.7x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.col  @(319.7,602.5) 36.3x33
            display:flex; flex-direction:column; align-items:flow-end; gap:1px
          - span.tm "26:02"  @(319.7,602.5) 36.3x16
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-weight:700; line-height:16px; text-align:right; font-variant-numeric:tabular-nums
          - span.cap.red "−12"  @(335.9,619.5) 20.1x16
              color:rgb(209, 43, 43); font-size:12px; line-height:16px
    - section.card.enter.e3  @(16,664) 361x238
        flex-shrink:0; padding:12px 10px 8px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px; transform:matrix(1, 0, 0, 1, 0, 0); animation:0.32s cubic-bezier(0.2, 0.8, 0.2, 1) 0.12s both enter-up
      - nav.seg.seg-sm aria="Escopo do ranking"  @(31,677) 331x40
          display:flex; gap:4px; padding:4px; margin:0px 4px 8px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px
        - button.on "Global"  @(36,682) 158.5x30
            flex-grow:1; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:20px; box-shadow:rgb(93, 107, 52) 0px 0px 0px 1px inset; font-size:13px; font-weight:600; line-height:16px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
        - button "Meu estado"  @(198.5,682) 158.5x30
            flex-grow:1; color:rgb(143, 147, 132); border-radius:20px; font-size:13px; font-weight:600; line-height:16px; letter-spacing:0.5px; text-align:center; transition:background 0.18s ease-out, color 0.18s ease-out
      - div.rk.p-g  @(27,725) 339x56
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; border-radius:14px
        - span.rk-pos "127"  @(39,743) 30x20
            color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
        - div.av "LP"  @(79,737) 32x32
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(70, 48, 31); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - span.t3.grow "Larissa Prado"  @(121,743) 183.1x20
            flex-grow:1; font-size:14px; font-weight:600; line-height:20px
        - span.rk-elo "1.546"  @(314.1,744) 39.9x18
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - div.rk.rk-me.p-g  @(27,781) 339x56
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgba(163, 181, 101, 0.14); border-radius:14px; box-shadow:rgb(93, 107, 52) 0px 0px 0px 1px inset
        - span.rk-pos.olive "128"  @(39,799) 30x20
            color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
        - div.av.av-me "RN"  @(79,793) 32x32
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgb(163, 181, 101); border-radius:50%; box-shadow:rgba(163, 181, 101, 0.14) 0px 0px 0px 2px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - span.t3.grow "Você"  @(121,799) 183.1x20
            flex-grow:1; font-size:14px; font-weight:600; line-height:20px
        - span.rk-elo.olive "1.542"  @(314.1,800) 39.9x18
            color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - div.rk.p-g  @(27,837) 339x56
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; border-radius:14px
        - span.rk-pos "129"  @(39,855) 30x20
            color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; text-align:center; font-variant-numeric:tabular-nums
        - div.av "TM"  @(79,849) 32x32
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(58, 46, 63); border:1px solid rgba(236, 232, 218, 0.18); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:12px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - span.t3.grow "Thiago Menezes"  @(121,855) 183.1x20
            flex-grow:1; font-size:14px; font-weight:600; line-height:20px
        - span.rk-elo "1.538"  @(314.1,856) 39.9x18
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; text-align:right; font-variant-numeric:tabular-nums
      - div.rk.p-e  [display:none]
      - div.rk.rk-me.p-e  [display:none]
      - div.rk.p-e  [display:none]
  - footer.dock  @(0,768) 393x83
      flex-shrink:0; padding:12px 16px 18px; margin:0px -16px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px
    - div.row  @(16,781) 361x52
        display:flex; align-items:center; gap:10px
      - button.btn.btn-olive "Compartilhar resultado"  @(16,781) 188.5x52
          display:flex; justify-content:center; align-items:center; flex-grow:1.2; gap:8px; padding:0px 10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:24px; font-size:14px; font-weight:700; line-height:16px; text-align:center; transition:transform 0.12s ease-out
        - svg  @(26,798) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="res.dock.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
      - button.btn.btn-ghost "Ver ranking completo"  @(214.5,781) 162.5x52
          display:flex; justify-content:center; align-items:center; flex-grow:1; gap:8px; padding:0px 10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px; font-size:14px; font-weight:700; line-height:16px; text-align:center; transition:transform 0.12s ease-out
