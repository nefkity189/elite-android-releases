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
- Saira Stencil One  loaded=True

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
    - button.btn-back aria="Fechar"  @(16,32) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(26,42) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="shr.topbar.close.ic" data-icon="material:Close" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M18 6L6 18M6 6l12 12"></path></svg>
    - h1.t1.grow "Compartilhar"  @(68,36) 309x32
        flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
  - nav aria="Modelo do card"  @(0,82) 393x30
      display:flex; flex-shrink:0; gap:8px; padding:0px 16px; margin:6px -16px 0px
    - button.chip.chip-on "Participação"  @(16,82) 98.8x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12px; font-weight:600; line-height:16px; text-align:center
    - button.chip "Pódio"  @(122.8,82) 58.9x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12px; font-weight:600; line-height:16px; text-align:center
    - button.chip "Conquista"  @(189.7,82) 85.3x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12px; font-weight:600; line-height:16px; text-align:center
    - button.chip "Ranking"  @(283,82) 72.8x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12px; font-weight:600; line-height:16px; text-align:center
    - button.chip "Convite"  @(363.8,82) 70.7x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12px; font-weight:600; line-height:16px; text-align:center
    - button.chip "Convite sem app"  @(442.5,82) 123.1x30
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:15px; font-size:12px; font-weight:600; line-height:16px; text-align:center
  - section.col  @(16,124) 361x362
      display:flex; flex-direction:column; flex-shrink:0; margin:12px 0px 0px
    - div.sc-card.card-v.v-participacao  @(51.5,124) 290x362
        display:flex; position:relative; flex-shrink:0; background-image:linear-gradient(160deg, rgb(29, 33, 22), rgb(10, 11, 9) 62%); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px; overflow:hidden
      - div.sc-deco1  @(182.3,115) 256.5x420
          position:absolute; background-color:rgba(163, 181, 101, 0.14); transform:matrix(1, 0, -0.32492, 1, 0, 0)
      - div.sc-deco2  @(208.3,115) 150.5x420
          position:absolute; background-color:rgba(209, 43, 43, 0.15); transform:matrix(1, 0, -0.32492, 1, 0, 0)
      - div.sc-in  @(52.5,125) 288x360
          display:flex; position:absolute; flex-direction:column; justify-content:space-between; padding:14px 16px 12px
        - div.sc-top  @(68.5,139) 256x20
            display:flex; justify-content:space-between; align-items:center
          - div.sc-logo  @(68.5,139) 73.9x20
              display:flex; align-items:center; gap:6px; font-family:"Saira Stencil One", Impact, sans-serif; font-size:13px; line-height:14px; letter-spacing:2.6px
            - svg  @(68.5,139) 20x20
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="shr.c1.mark" data-icon="drawable:ic_elite_blade" viewBox="0 0 64 64" width="20" height="20" aria-hidden="true"><path d="M19.1 8H55.1L53.2 17H27.2L20.8 47H46.8L44.9 56H8.9Z" style="fill:var(--olive)"></path><path d="M25.1 27H45.1L43.1 36H23.1Z" style="fill:var(--red)"></path></svg>
            - span "ELITE"  @(94.5,142) 47.9x14
          - span.sc-mt "Missão 5K · 13:00"  @(225.4,144) 99.1x10
              color:rgb(143, 147, 132); font-size:9px; font-weight:700; line-height:10px; letter-spacing:1px; text-transform:uppercase
        - div.col  @(68.5,175) 256x141
            display:flex; flex-direction:column; align-items:flow-start
          - span.eyebrow.olive "Missão cumprida"  @(68.5,175) 115.3x14
              color:rgb(163, 181, 101); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - div.row  @(68.5,189) 136.4x104
              display:flex; align-items:flow-end; gap:8px
            - span.num "2º"  @(68.5,189) 84.5x104
                color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:104px; font-weight:700; line-height:104px; letter-spacing:1px; font-variant-numeric:tabular-nums
            - span.eyebrow "lugar"  @(161,265) 43.9x14
                margin:0px 0px 14px; color:rgb(201, 162, 75); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.t2 "Rafael Nunes"  @(68.5,293) 107.7x23
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - div.row  @(68.5,332) 256x29
            display:flex; align-items:center; gap:8px
          - div.sc-stat  @(68.5,332) 80x29
              display:flex; flex-direction:column; flex-grow:1; gap:2px
            - span "Tempo"  @(68.5,332) 80x9
                color:rgb(143, 147, 132); font-size:8px; font-weight:600; line-height:9px; letter-spacing:0.8px; text-transform:uppercase
            - span.num "24:18"  @(68.5,343) 80x18
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; font-variant-numeric:tabular-nums
          - div.sc-stat  @(156.5,332) 80x29
              display:flex; flex-direction:column; flex-grow:1; gap:2px
            - span "Pace médio"  @(156.5,332) 80x9
                color:rgb(143, 147, 132); font-size:8px; font-weight:600; line-height:9px; letter-spacing:0.8px; text-transform:uppercase
            - span.num "4:52 /km"  @(156.5,343) 80x18
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; font-variant-numeric:tabular-nums
          - div.sc-stat  @(244.5,332) 80x29
              display:flex; flex-direction:column; flex-grow:1; gap:2px
            - span "Distância"  @(244.5,332) 80x9
                color:rgb(143, 147, 132); font-size:8px; font-weight:600; line-height:9px; letter-spacing:0.8px; text-transform:uppercase
            - span.num "5K"  @(244.5,343) 80x18
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:18px; font-weight:700; line-height:18px; font-variant-numeric:tabular-nums
        - div.row  @(68.5,377) 256x46
            display:flex; align-items:center; gap:6px
          - span.sc-chip "Global #128"  @(68.5,377) 72.4x20
              display:flex; align-items:center; gap:4px; padding:0px 8px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:10px; font-size:9.5px; font-weight:700; line-height:10px
          - span.sc-chip "MT #7"  @(146.9,377) 47.1x20
              display:flex; align-items:center; gap:4px; padding:0px 8px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:10px; font-size:9.5px; font-weight:700; line-height:10px
          - span.sc-chip "Ouro II"  @(200,377) 64.3x20
              display:flex; align-items:center; gap:4px; padding:0px 8px; color:rgb(201, 162, 75); background-color:rgb(29, 33, 22); border:1px solid rgba(201, 162, 75, 0.5); border-radius:10px; font-size:9.5px; font-weight:700; line-height:10px
            - svg  @(209,381) 12x12
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="shr.c1.lg.badge" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="12" height="12" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
          - span.sc-chip "Pódio"  @(68.5,403) 59.3x20
              display:flex; align-items:center; gap:4px; padding:0px 8px; color:rgb(201, 162, 75); background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:10px; font-size:9.5px; font-weight:700; line-height:10px
            - svg  @(77.5,407.5) 11x11
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="shr.c1.ac.ic" data-icon="material:WorkspacePremium" viewBox="0 0 24 24" width="11" height="11" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="9" r="6"></circle><path d="M8.5 14.5L7 22l5-3 5 3-1.5-7.5"></path></svg>
        - div.sc-foot  @(68.5,439) 256x34
            display:flex; flex-direction:column; gap:5px
          - div.stripe  @(68,439) 61.1x3
              background-color:rgb(209, 43, 43); border-radius:2px; transform:matrix(1, 0, -0.36397, 1, 0, 0)
          - span.sc-motto "Foco, Missão dada, Missão cumprida."  @(68.5,447) 256x9
              color:rgb(143, 147, 132); font-size:7.5px; font-weight:700; line-height:9px; letter-spacing:1.1px; text-transform:uppercase
          - span.sc-cta "Baixe o Elite e entre na missão."  @(68.5,461) 256x12
              font-size:10px; font-weight:600; line-height:12px
    - div.sc-card.card-v.v-podio  [display:none]
    - div.sc-card.card-v.v-conquista  [display:none]
    - div.sc-card.card-v.v-ranking  [display:none]
    - div.sc-card.card-v.v-convite  [display:none]
  - section.row  @(16,498) 361x72
      display:flex; align-items:center; flex-shrink:0; gap:10px; margin:12px 0px 0px
    - button.tg "Instagram"  @(16,498) 113.7x72
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:6px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px; font-size:12px; font-weight:600; line-height:14px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(61.8,513) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="shr.targets.stories.ic" data-icon="material:PhotoCamera" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"></path><circle cx="12" cy="13" r="4"></circle></svg>
    - button.tg "WhatsApp"  @(139.7,498) 113.7x72
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:6px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px; font-size:12px; font-weight:600; line-height:14px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(185.5,513) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="shr.targets.msg.ic" data-icon="material:Link" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71"></path></svg>
    - button.tg "Mais"  @(263.3,498) 113.7x72
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:6px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px; font-size:12px; font-weight:600; line-height:14px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(309.2,513) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="shr.targets.more.ic" data-icon="material:MoreHoriz" viewBox="0 0 24 24" width="22" height="22" fill="currentColor" stroke="currentColor" stroke-width="1.2" aria-hidden="true"><circle cx="5" cy="12" r="1.2"></circle><circle cx="12" cy="12" r="1.2"></circle><circle cx="19" cy="12" r="1.2"></circle></svg>
  - div.grow  @(16,570) 361x198
      flex-grow:1
  - footer.dock  @(0,768) 393x83
      flex-shrink:0; padding:12px 16px 18px; margin:0px -16px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px
    - div.row  @(16,781) 361x52
        display:flex; align-items:center; gap:10px
      - button.btn.btn-ghost.btn-icon aria="Salvar imagem"  @(16,781) 48x52
          display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - svg  @(30,797) 20x20
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="shr.dock.save.ic" data-icon="material:Download" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4M7 10l5 5 5-5M12 15V3"></path></svg>
      - button.btn.btn-olive.grow "Compartilhar card"  @(74,781) 194.9x52
          display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:24px; font-size:15px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
        - svg  @(92,798) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="shr.dock.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
