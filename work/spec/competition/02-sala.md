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
- **slot-pulse** on `div.av.av-empty.slot-pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "borderTopColor": "rgba(236, 232, 218, 0.18)", "borderBottomColor": "rgba(236, 232, 218, 0.18)", "borderLeftColor": "rgba(236, 232, 218, 0.18)", "borderRightColor": "rgba(236, 232, 218, 0.18)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "borderTopColor": "rgb(163, 181, 101)", "borderBottomColor": "rgb(163, 181, 101)", "borderLeftColor": "rgb(163, 181, 101)", "borderRightColor": "rgb(163, 181, 101)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "borderTopColor": "rgba(236, 232, 218, 0.18)", "borderBottomColor": "rgba(236, 232, 218, 0.18)", "borderLeftColor": "rgba(236, 232, 218, 0.18)", "borderRightColor": "rgba(236, 232, 218, 0.18)"}

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 16px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - header.topbar  @(16,28) 361x56
      display:flex; align-items:center; flex-shrink:0; gap:12px
    - button.btn-back aria="Voltar"  @(16,36) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(26,46) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="lobby.topbar.back.ic" data-icon="material:ArrowBack" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M15 18l-6-6 6-6"></path></svg>
    - div.col.grow  @(68,35) 309x42
        display:flex; flex-direction:column; flex-grow:1; gap:2px
      - p.eyebrow "Sala da missão"  @(68,35) 309x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
      - h1.t1s "Missão 5K — 13:00"  @(68,51) 309x26
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:26px; letter-spacing:0.96px; text-transform:uppercase
  - div.col  @(16,84) 361x0
      display:flex; flex-direction:column; flex-shrink:0; gap:8px; margin:0px 0px 10px
    - aside.banner.bn-amber.bn.bn-10  [display:none]
    - aside.banner.bn-red.bn.bn-5  [display:none]
    - aside.banner.bn-ok.bn.bn-ok-c  [display:none]
  - div  @(16,94) 361x668
      display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:0px 0px 12px; overflow:hidden auto
    - section.card  @(16,94) 361x200
        flex-shrink:0; padding:12px 16px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(33,107) 327x24
          display:flex; justify-content:space-between; align-items:center
        - span.eyebrow "Começa em"  @(33,112) 80.5x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.badge.b-one.s-open "Falta 1 competidor"  @(203.4,107) 156.6x24
            display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(224, 169, 58); background-color:rgba(224, 169, 58, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - i.dot  @(213.4,115.5) 7x7
              flex-shrink:0; background-color:rgb(224, 169, 58); border-radius:4px
        - span.badge.b-conf.s-conf  [display:none]
      - p.num "18:42"  @(33,137) 327x52
          margin:6px 0px 8px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:52px; font-weight:700; line-height:52px; letter-spacing:1px; text-align:center; font-variant-numeric:tabular-nums
      - div.row  @(33,197) 327x56
          display:flex; align-items:center; padding:10px 0px 0px
        - div.fact  @(33,208) 108.3x45
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:3px; padding:2px 0px
          - span.eyebrow "Distância"  @(54.1,210) 66.1x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num "5 km"  @(65.3,227) 43.7x24
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:24px; font-variant-numeric:tabular-nums
        - div.fact  @(141.3,208) 109.3x45
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:3px; padding:2px 0px
          - span.eyebrow "Horário"  @(169,210) 55x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num "13:00"  @(169.3,227) 54.4x24
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:24px; font-variant-numeric:tabular-nums
        - div.fact  @(250.7,208) 109.3x45
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:3px; padding:2px 0px
          - span.eyebrow "Mínimo"  @(282.3,210) 47.1x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num "2"  @(299.9,227) 12x24
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:24px; font-variant-numeric:tabular-nums
      - div.row  @(33,263) 327x20
          display:flex; justify-content:center; align-items:center; gap:8px; margin:10px 0px 0px; color:rgb(163, 181, 101)
        - svg  @(90.4,265) 16x16
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="lobby.rule.ic" data-icon="material:Flag" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M4 15s1-1 4-1 5 2 8 2 4-1 4-1V3s-1 1-4 1-5-2-8-2-4 1-4 1zM4 22v-7"></path></svg>
        - span.t3 "Vence quem concluir primeiro"  @(114.4,263) 188.2x20
            font-size:13px; font-weight:600; line-height:20px
    - section.card  @(16,304) 361x158
        flex-shrink:0; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,317) 331x23
          display:flex; justify-content:space-between; align-items:center; margin:0px 0px 4px
        - h2.t2 "Competidores"  @(31,317) 115.6x23
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - span.num  @(333.8,318.5) 28.2x20
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:20px; font-variant-numeric:tabular-nums
          - span.s-open "1/2"  @(333.8,318.5) 28.2x20
              display:flex
          - span.s-conf  [display:none]
      - div.pr  @(31,344) 331x52
          display:flex; align-items:center; gap:12px
        - div.av.av-me "RN"  @(31,352) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgb(163, 181, 101); border-radius:50%; box-shadow:rgba(163, 181, 101, 0.14) 0px 0px 0px 2px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:13px; font-weight:700; line-height:14px; letter-spacing:0.5px
        - div.col.grow  @(79,359.5) 216.1x21
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Rafael Nunes"  @(79,359.5) 216.1x21
              font-size:14px; font-weight:600; line-height:20px
            - span.olive "VOCÊ"  @(172.1,363.5) 31.1x14
                color:rgb(163, 181, 101); font-size:11px; font-weight:700
        - span.row.cap.ok "Na sala"  @(307.1,362) 54.9x16
            display:flex; align-items:center; gap:6px; color:rgb(127, 176, 105); font-size:12px; line-height:16px
          - i.dot  @(307.1,366.5) 7x7
              flex-shrink:0; background-color:rgb(127, 176, 105); border-radius:4px
      - div.hr  @(31,396) 331x1
          flex-shrink:0; background-color:rgba(236, 232, 218, 0.1)
      - div.pr.s-conf  [display:none]
      - div.pr.s-open  @(31,397) 331x52
          display:flex; align-items:center; gap:12px
        - div.av.av-empty.slot-pulse  @(31,405) 36x36
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(94, 98, 85); border:1px dashed rgba(166, 183, 106, 0.847); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:14px; letter-spacing:0.5px; animation:2s ease-in-out infinite slot-pulse
          - svg  @(41,415) 16x16
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="lobby.players.slot.ic" data-icon="material:Add" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M12 5v14M5 12h14"></path></svg>
        - div.col.grow  @(79,404.5) 283x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3.muted "Aguardando competidor"  @(79,404.5) 283x20
              color:rgb(143, 147, 132); font-size:14px; font-weight:600; line-height:20px
          - span.cap "Vaga em aberto"  @(79,425.5) 283x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
    - section.invite  @(16,472) 361x165
        display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:14px; background-image:linear-gradient(135deg, rgba(163, 181, 101, 0.14), rgb(22, 25, 17) 70%); border:1px solid rgb(93, 107, 52); border-radius:18px
      - div.row  @(31,487) 331x75
          display:flex; align-items:flow-start; gap:12px
        - div.sc-ic  @(31,487) 40x40
            display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:20px
          - svg  @(41,497) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="lobby.invite.icon.ic" data-icon="material:Groups" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"></path><circle cx="9" cy="7" r="4"></circle><path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"></path></svg>
        - div.col.grow  @(83,487) 279x75
            display:flex; flex-direction:column; flex-grow:1; gap:4px
          - h2.t2 "Chame reforços para a missão"  @(83,487) 279x23
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
          - p.cap "O link leva direto para esta missão. Quem já tem o Elite cai na sala; quem não tem vai para o download."  @(83,514) 279x48
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - button.btn.btn-olive.btn-lg "Compartilhar convite"  @(31,572) 331x50
          display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - svg  @(100.1,587) 20x20
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="lobby.invite.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
    - section.card  @(16,647) 361x147
        flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - h2.t2 "Regras da missão"  @(31,662) 331x23
          margin:0px 0px 10px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
      - div.col  @(31,695) 331x84
          display:flex; flex-direction:column; gap:8px
        - div.rule  @(31,695) 331x18
            display:flex; align-items:flow-start; gap:10px
          - svg  @(31,697) 16x16
              flex-shrink:0; margin:2px 0px 0px; color:rgb(163, 181, 101); overflow:hidden
              svg: <svg data-id="lobby.rules.i1" data-icon="material:Check" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M20 6L9 17l-5-5"></path></svg>
          - p.cap "Vence quem concluir os 5 km primeiro."  @(57,695) 221.3x16
              font-size:12px; line-height:16px
        - div.rule  @(31,721) 331x18
            display:flex; align-items:flow-start; gap:10px
          - svg  @(31,723) 16x16
              flex-shrink:0; margin:2px 0px 0px; color:rgb(163, 181, 101); overflow:hidden
              svg: <svg data-id="lobby.rules.i2" data-icon="material:Check" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M20 6L9 17l-5-5"></path></svg>
          - p.cap "A missão só começa com no mínimo 2 competidores."  @(57,721) 302.8x16
              font-size:12px; line-height:16px
        - div.rule  @(31,747) 331x32
            display:flex; align-items:flow-start; gap:10px
          - svg  @(31,749) 16x16
              flex-shrink:0; margin:2px 0px 0px; color:rgb(163, 181, 101); overflow:hidden
              svg: <svg data-id="lobby.rules.i3" data-icon="material:Check" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M20 6L9 17l-5-5"></path></svg>
          - p.cap "É preciso completar a distância para registrar o resultado."  @(57,747) 305x32
              font-size:12px; line-height:16px
  - footer.dock  @(0,762) 393x89
      flex-shrink:0; padding:14px 16px 18px; margin:0px -16px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px
    - button.btn.btn-lg.btn-off.s-open "Aguardando o mínimo de competidores"  @(16,777) 344.7x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(94, 98, 85); background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:28px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
    - button.btn.btn-lg.btn-red.s-conf  [display:none]
