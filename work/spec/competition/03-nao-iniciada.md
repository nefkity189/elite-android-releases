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

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 16px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - header.topbar  @(16,28) 361x56
      display:flex; align-items:center; flex-shrink:0; gap:12px
    - button.btn-back aria="Voltar"  @(16,36) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(26,46) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="ni.topbar.back.ic" data-icon="material:ArrowBack" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M15 18l-6-6 6-6"></path></svg>
    - div.col.grow  @(68,35) 309x42
        display:flex; flex-direction:column; flex-grow:1; gap:2px
      - p.eyebrow "Competição online"  @(68,35) 309x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
      - h1.t1s "Missão 5K — 13:00"  @(68,51) 309x26
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:26px; letter-spacing:0.96px; text-transform:uppercase
  - section.card.col  @(16,90) 361x309.2
      display:flex; flex-direction:column; align-items:center; flex-shrink:0; gap:10px; padding:20px 16px 16px; margin:6px 0px 0px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
    - div.sc-ic  @(164.5,111) 64x64
        display:flex; justify-content:center; align-items:center; flex-shrink:0; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border-radius:32px
      - svg  @(181.5,128) 30x30
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="ni.status.icon.ic" data-icon="material:Schedule" viewBox="0 0 24 24" width="30" height="30" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="9"></circle><path d="M12 7v5l3 2"></path></svg>
    - span.badge.b-none "Não iniciada por falta de competidores"  @(56.3,185) 280.5x24
        display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.18); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
    - h2.t1 "Missão não iniciada"  @(74.8,219) 243.3x32
        font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-align:center; text-transform:uppercase
    - p.body.muted "Não houve competidores suficientes. O mínimo é 2."  @(33,261) 327x39.2
        color:rgb(143, 147, 132); font-size:14px; line-height:19.6px; text-align:center
    - div.row  @(33,314.2) 327x68
        display:flex; align-items:center; gap:10px; margin:4px 0px 0px
      - div.tile  @(33,314.2) 102.3x68
          display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:4px; padding:10px 0px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px
        - span.eyebrow "Horário"  @(56.6,325.2) 55x14
            color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.num "13:00"  @(52.4,343.2) 63.5x28
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
      - div.tile  @(145.3,314.2) 102.3x68
          display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:4px; padding:10px 0px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px
        - span.eyebrow "Distância"  @(163.4,325.2) 66.1x14
            color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.num "5K"  @(182.6,343.2) 27.7x28
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
      - div.tile  @(257.7,314.2) 102.3x68
          display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:4px; padding:10px 0px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px
        - span.eyebrow "Mínimo"  @(285.3,325.2) 47.1x14
            color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.num "2"  @(301.9,343.2) 14x28
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
  - section.invite  @(16,411.2) 361x242
      display:flex; flex-direction:column; flex-shrink:0; gap:12px; padding:14px; margin:12px 0px 0px; background-image:linear-gradient(135deg, rgba(163, 181, 101, 0.14), rgb(22, 25, 17) 70%); border:1px solid rgb(93, 107, 52); border-radius:18px
    - div.row  @(31,426.2) 331x148
        display:flex; gap:14px
      - div.mini  @(31,426.2) 118x148
          display:flex; flex-direction:column; justify-content:space-between; flex-shrink:0; padding:12px; background-image:linear-gradient(160deg, rgb(29, 33, 22), rgb(10, 11, 9)); border:1px solid rgb(93, 107, 52); border-radius:14px
        - div.row  @(44,439.2) 92x14
            display:flex; align-items:center; gap:5px
          - svg  @(44,439.2) 14x14
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ni.invite.mini.mark" data-icon="drawable:ic_elite_blade" viewBox="0 0 64 64" width="14" height="14" aria-hidden="true"><path d="M19.1 8H55.1L53.2 17H27.2L20.8 47H46.8L44.9 56H8.9Z" style="fill:var(--olive)"></path><path d="M25.1 27H45.1L43.1 36H23.1Z" style="fill:var(--red)"></path></svg>
          - span "ELITE"  @(63,441.2) 33.2x10
              font-family:"Saira Stencil One", Impact, sans-serif; font-size:9px; line-height:10px; letter-spacing:1.8px
        - div.col  @(44,459.2) 92x72
            display:flex; flex-direction:column; gap:3px
          - span.num "Missão precisa de você"  @(44,459.2) 92x54
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:17px; font-weight:700; line-height:18px; font-variant-numeric:tabular-nums; text-transform:uppercase
          - span.num.olive "5K · 13:00"  @(44,516.2) 92x15
              color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:15px; font-weight:700; line-height:15px; font-variant-numeric:tabular-nums
        - div.col  @(44,537.2) 92x24
            display:flex; flex-direction:column; gap:4px
          - div.stripe  @(43.3,537.2) 93.5x4
              background-color:rgb(209, 43, 43); border-radius:2px; transform:matrix(1, 0, -0.36397, 1, 0, 0)
          - span "Foco, Missão dada, Missão cumprida."  @(44,545.2) 92x16
              color:rgb(143, 147, 132); font-size:6.5px; font-weight:600; line-height:8px; letter-spacing:0.6px; text-transform:uppercase
      - div.col.grow  @(163,426.2) 199x148
          display:flex; flex-direction:column; justify-content:center; flex-grow:1; gap:6px
        - h2.t1s "Convoque competidores"  @(163,434.2) 199x46
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:21px; font-weight:700; line-height:23px; letter-spacing:0.96px; text-transform:uppercase
        - p.cap "Compartilhe o card de convite e atraia gente para as próximas missões. Quem tem o Elite entra direto; quem não tem vai para o download."  @(163,486.2) 199x80
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
    - button.btn.btn-olive.btn-lg "Compartilhar convite"  @(31,586.2) 331x52
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(100.1,602.2) 20x20
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="ni.invite.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
  - div.grow  @(16,653.2) 361x108.8
      flex-grow:1
  - footer.dock  @(0,762) 393x89
      flex-shrink:0; padding:14px 16px 18px; margin:0px -16px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px
    - button.btn.btn-ghost.btn-lg "Ver próximas missões"  @(16,777) 361x56
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:8px; padding:0px 18px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.12s ease-out
