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
          svg: <svg data-id="ssn.topbar.back.ic" data-icon="material:ArrowBack" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M15 18l-6-6 6-6"></path></svg>
    - h1.t1.grow "Temporada 1"  @(68,36) 257x32
        flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
    - button.btn-back aria="Compartilhar liga"  @(337,32) 40x40
        display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:20px; font-size:13.3333px; text-align:center; transition:transform 0.12s ease-out
      - svg  @(348,43) 18x18
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="ssn.topbar.share.ic" data-icon="material:Share" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
  - div  @(16,84) 361x684
      display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:0px 0px 12px; margin:8px 0px 0px; overflow:hidden auto
    - section.card  @(16,84) 361x108
        flex-shrink:0; padding:14px 16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(33,99) 327x22
          display:flex; justify-content:space-between; align-items:center
        - span.eyebrow "Fim de temporada"  @(33,103) 130.9x14
            color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
        - span.num.amber "12d 04h"  @(292.1,99) 67.9x22
            color:rgb(224, 169, 58); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
      - div.bar  @(33,131) 327x6
          margin:10px 0px 0px; background-color:rgb(29, 33, 22); border-radius:3px; overflow:hidden
        - i  @(33,131) 196.2x6
            background-color:rgb(224, 169, 58); border-radius:3px
      - p.cap "Ao final, os pontos sofrem reset parcial e a divisão alcançada define as recompensas."  @(33,145) 327x32
          margin:8px 0px 0px; color:rgb(143, 147, 132); font-size:12px; line-height:16px
    - section.card.card-brass  @(16,202) 361x148
        flex-shrink:0; padding:14px 16px; background-image:linear-gradient(rgba(201, 162, 75, 0.16), rgb(22, 25, 17)); border:1px solid rgb(201, 162, 75); border-radius:18px
      - div.row  @(33,217) 327x72
          display:flex; align-items:center; gap:14px
        - svg  @(33,217) 72x72
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.me.badge" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="72" height="72" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
        - div.col.grow  @(119,227.5) 241x51
            display:flex; flex-direction:column; flex-grow:1; gap:3px
          - span.t1 "Liga Ouro II"  @(119,227.5) 241x32
              color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-transform:uppercase
          - span.cap "pts · faixa 1.400–1.599"  @(119,262.5) 241x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
            - b "1.542"  @(119,262.5) 32.3x15
                color:rgb(236, 232, 218); font-weight:700
      - div.bar.bar-brass  @(33,303) 327x8
          margin:14px 0px 0px; background-color:rgb(29, 33, 22); border-radius:4px; overflow:hidden
        - i  @(33,303) 232.2x8
            background-color:rgb(201, 162, 75); border-radius:3px
      - div.row  @(33,319) 327x16
          display:flex; justify-content:space-between; align-items:center; margin:8px 0px 0px
        - span.cap "Faltam para subir de divisão"  @(33,319) 221.8x16
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
          - b "58 pontos"  @(74.2,319) 58.9x15
              color:rgb(236, 232, 218); font-weight:700
        - span.row.cap "Ouro I"  @(304.6,319) 55.4x16
            display:flex; align-items:center; gap:5px; color:rgb(143, 147, 132); font-size:12px; line-height:16px
          - svg  @(304.6,319) 16x16
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.me.target.badge" data-icon="custom:ic_league_ouro_1" viewBox="0 0 24 24" width="16" height="16" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
    - section.card  @(16,360) 361x312
        flex-shrink:0; padding:12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - h2.t2 "Ligas e divisões"  @(31,373) 331x23
          margin:0px 2px 10px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
      - div.ladder  @(29,406) 335x71
          display:flex; gap:8px
        - button.lgi aria="Liga Bronze"  @(29,406) 49x71
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:5px; padding:10px 0px 8px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:13.3333px; text-align:center; transition:background 0.18s ease-out, border-color 0.18s ease-out
          - svg  @(36.5,417) 34x34
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.ladder.bronze.badge" data-icon="custom:ic_league_bronze_1" viewBox="0 0 24 24" width="34" height="34" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-bronze)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
          - span "Bronze"  @(35.7,456) 35.6x12
              color:rgb(143, 147, 132); font-size:10.5px; font-weight:600; line-height:12px
        - button.lgi aria="Liga Prata"  @(86,406) 49x71
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:5px; padding:10px 0px 8px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:13.3333px; text-align:center; transition:background 0.18s ease-out, border-color 0.18s ease-out
          - svg  @(93.5,417) 34x34
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.ladder.prata.badge" data-icon="custom:ic_league_prata_1" viewBox="0 0 24 24" width="34" height="34" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-prata)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
          - span "Prata"  @(97.1,456) 26.7x12
              color:rgb(143, 147, 132); font-size:10.5px; font-weight:600; line-height:12px
        - button.lgi.on.here aria="Liga Ouro"  @(143,406) 49x71
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:5px; padding:10px 0px 8px; background-color:rgba(201, 162, 75, 0.16); border:1px solid rgb(201, 162, 75); border-radius:14px; box-shadow:rgba(163, 181, 101, 0.14) 0px 0px 0px 2px; font-size:13.3333px; text-align:center; transition:background 0.18s ease-out, border-color 0.18s ease-out
          - svg  @(150.4,417) 34x34
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.ladder.ouro.badge" data-icon="custom:ic_league_ouro_1" viewBox="0 0 24 24" width="34" height="34" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
          - span "Ouro"  @(155,456) 24.8x12
              font-size:10.5px; font-weight:600; line-height:12px
        - button.lgi aria="Liga Platina"  @(199.9,406) 49x71
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:5px; padding:10px 0px 8px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:13.3333px; text-align:center; transition:background 0.18s ease-out, border-color 0.18s ease-out
          - svg  @(207.4,417) 34x34
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.ladder.platina.badge" data-icon="custom:ic_league_platina_1" viewBox="0 0 24 24" width="34" height="34" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-platina)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
          - span "Platina"  @(207.2,456) 34.5x12
              color:rgb(143, 147, 132); font-size:10.5px; font-weight:600; line-height:12px
        - button.lgi aria="Liga Diamante"  @(256.9,406) 50.1x71
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:5px; padding:10px 0px 8px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:13.3333px; text-align:center; transition:background 0.18s ease-out, border-color 0.18s ease-out
          - svg  @(265,417) 34x34
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.ladder.diamante.badge" data-icon="custom:ic_league_diamante_1" viewBox="0 0 24 24" width="34" height="34" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-diamante)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
          - span "Diamante"  @(257.9,456) 48.1x12
              color:rgb(143, 147, 132); font-size:10.5px; font-weight:600; line-height:12px
        - button.lgi aria="Liga Elite"  @(315,406) 49x71
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:5px; padding:10px 0px 8px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-size:13.3333px; text-align:center; transition:background 0.18s ease-out, border-color 0.18s ease-out
          - svg  @(322.5,417) 34x34
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.ladder.elite.badge" data-icon="custom:ic_league_elite_0" viewBox="0 0 24 24" width="34" height="34" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-elite)"></path><path d="M10.2 7.2H17L16.6 8.9H11.7L10.5 14.6H15.4L15 16.3H8.3Z" style="fill:var(--bg)"></path><path d="M11.3 10.8H15.1L14.8 12.3H11Z" style="fill:var(--red)"></path></svg>
          - span "Elite"  @(328.7,456) 21.7x12
              color:rgb(143, 147, 132); font-size:10.5px; font-weight:600; line-height:12px
      - div  @(29,487) 335x172
          margin:10px 0px 0px
        - div.dl.dl-bronze  [display:none]
        - div.dl.dl-prata  [display:none]
        - div.dl.dl-ouro  @(29,487) 335x172
            display:flex; flex-direction:column; gap:8px
          - div.dvr  @(29,487) 335x52
              display:flex; align-items:center; gap:12px; padding:0px 12px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px
            - svg  @(42,500) 26x26
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ssn.dv.ouro.i.insig" data-icon="custom:ic_league_ouro_1" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
            - div.col.grow  @(80,494.5) 185.2x37
                display:flex; flex-direction:column; flex-grow:1; gap:1px
              - span.t3 "Ouro I"  @(80,494.5) 185.2x20
                  font-size:14px; font-weight:600; line-height:20px
              - span.cap "1.600–1.799 pts"  @(80,515.5) 185.2x16
                  color:rgb(143, 147, 132); font-size:12px; line-height:16px
            - span.badge.b-brass "Próxima"  @(277.2,503) 73.8x20
                display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.dvr.dvr-me  @(29,547) 335x52
              display:flex; align-items:center; gap:12px; padding:0px 12px; background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:14px
            - svg  @(42,560) 26x26
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ssn.dv.ouro.ii.insig" data-icon="custom:ic_league_ouro_2" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 10L12 13L16.2 10" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 14.5L12 17.5L16.2 14.5" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
            - div.col.grow  @(80,554.5) 206.9x37
                display:flex; flex-direction:column; flex-grow:1; gap:1px
              - span.t3 "Ouro II"  @(80,554.5) 206.9x20
                  font-size:14px; font-weight:600; line-height:20px
              - span.cap "1.400–1.599 pts"  @(80,575.5) 206.9x16
                  color:rgb(143, 147, 132); font-size:12px; line-height:16px
            - span.badge.b-open "Você"  @(298.9,563) 52.1x20
                display:flex; align-items:center; flex-shrink:0; gap:6px; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:12px; font-size:10.5px; font-weight:700; line-height:12px; letter-spacing:0.6px; text-transform:uppercase
          - div.dvr  @(29,607) 335x52
              display:flex; align-items:center; gap:12px; padding:0px 12px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px
            - svg  @(42,620) 26x26
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ssn.dv.ouro.iii.insig" data-icon="custom:ic_league_ouro_3" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
            - div.col.grow  @(80,614.5) 271x37
                display:flex; flex-direction:column; flex-grow:1; gap:1px
              - span.t3 "Ouro III"  @(80,614.5) 271x20
                  font-size:14px; font-weight:600; line-height:20px
              - span.cap "1.200–1.399 pts"  @(80,635.5) 271x16
                  color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.dl.dl-platina  [display:none]
        - div.dl.dl-diamante  [display:none]
        - div.dl.dl-elite  [display:none]
    - section.card  @(16,682) 361x342
        flex-shrink:0; padding:12px 14px 6px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,695) 331x23
          display:flex; align-items:center; gap:8px; margin:0px 0px 6px
        - span.brass  @(31,697.5) 18x18
            color:rgb(201, 162, 75)
          - svg  @(31,697.5) 18x18
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.rewards.ic" data-icon="material:CardGiftcard" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="8" width="18" height="4" rx="1"></rect><path d="M12 8v14M5 12v9h14v-9M7.5 8a2.5 2.5 0 0 1 0-5C11 3 12 8 12 8s1-5 4.5-5a2.5 2.5 0 0 1 0 5"></path></svg>
        - h2.t2 "Recompensas da temporada"  @(57,695) 230.2x23
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
      - div.rw  @(31,724) 331x48
          display:flex; align-items:center; gap:12px
        - svg  @(31,735) 26x26
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.rw.bronze.badge" data-icon="custom:ic_league_bronze_1" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-bronze)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
        - div.col.grow  @(69,729.5) 293x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Bronze"  @(69,729.5) 293x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "Insígnia de bronze no perfil"  @(69,750.5) 293x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.hr  @(31,772) 331x1
          flex-shrink:0; background-color:rgba(236, 232, 218, 0.1)
      - div.rw  @(31,773) 331x48
          display:flex; align-items:center; gap:12px
        - svg  @(31,784) 26x26
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.rw.prata.badge" data-icon="custom:ic_league_prata_1" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-prata)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
        - div.col.grow  @(69,778.5) 293x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Prata"  @(69,778.5) 293x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "Insígnia de prata no perfil"  @(69,799.5) 293x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.hr  @(31,821) 331x1
          flex-shrink:0; background-color:rgba(236, 232, 218, 0.1)
      - div.rw  @(31,822) 331x48
          display:flex; align-items:center; gap:12px
        - svg  @(31,833) 26x26
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.rw.ouro.badge" data-icon="custom:ic_league_ouro_1" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-ouro)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
        - div.col.grow  @(69,827.5) 293x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Ouro"  @(69,827.5) 293x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "Moldura dourada no avatar"  @(69,848.5) 293x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.hr  @(31,870) 331x1
          flex-shrink:0; background-color:rgba(236, 232, 218, 0.1)
      - div.rw  @(31,871) 331x48
          display:flex; align-items:center; gap:12px
        - svg  @(31,882) 26x26
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.rw.platina.badge" data-icon="custom:ic_league_platina_1" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-platina)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
        - div.col.grow  @(69,876.5) 293x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Platina"  @(69,876.5) 293x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "Moldura platina + título da liga"  @(69,897.5) 293x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.hr  @(31,919) 331x1
          flex-shrink:0; background-color:rgba(236, 232, 218, 0.1)
      - div.rw  @(31,920) 331x48
          display:flex; align-items:center; gap:12px
        - svg  @(31,931) 26x26
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.rw.diamante.badge" data-icon="custom:ic_league_diamante_1" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-diamante)"></path><path d="M7.8 8L12 11L16.2 8" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 12L12 15L16.2 12" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path><path d="M7.8 16L12 19L16.2 16" fill="none" style="stroke:var(--bg)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
        - div.col.grow  @(69,925.5) 293x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Diamante"  @(69,925.5) 293x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "Moldura diamante + card exclusivo"  @(69,946.5) 293x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
      - div.hr  @(31,968) 331x1
          flex-shrink:0; background-color:rgba(236, 232, 218, 0.1)
      - div.rw  @(31,969) 331x48
          display:flex; align-items:center; gap:12px
        - svg  @(31,980) 26x26
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.rw.elite.badge" data-icon="custom:ic_league_elite_0" viewBox="0 0 24 24" width="26" height="26" aria-hidden="true"><path d="M12 2L21 5V11C21 16.5 17 20.5 12 22C7 20.5 3 16.5 3 11V5Z" style="fill:var(--lg-elite)"></path><path d="M10.2 7.2H17L16.6 8.9H11.7L10.5 14.6H15.4L15 16.3H8.3Z" style="fill:var(--bg)"></path><path d="M11.3 10.8H15.1L14.8 12.3H11Z" style="fill:var(--red)"></path></svg>
        - div.col.grow  @(69,974.5) 293x37
            display:flex; flex-direction:column; flex-grow:1; gap:1px
          - span.t3 "Elite"  @(69,974.5) 293x20
              font-size:14px; font-weight:600; line-height:20px
          - span.cap "Título “Elite da temporada” + card exclusivo"  @(69,995.5) 293x16
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
    - section.card  @(16,1034) 361x105
        flex-shrink:0; padding:12px 14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - h2.t2 "Suas melhores posições"  @(31,1047) 331x23
          margin:0px 0px 10px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
      - div.row  @(31,1080) 331x46
          display:flex; align-items:center; gap:10px
        - div.kv  @(31,1080) 103.7x46
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.eyebrow "Global"  @(31,1080) 47.9x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num "#94"  @(31,1098) 45.1x28
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
        - div.kv  @(144.7,1080) 103.7x46
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.eyebrow "Estado MT"  @(144.7,1080) 70.5x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num "#3"  @(144.7,1098) 31.2x28
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:28px; font-variant-numeric:tabular-nums
        - div.kv  @(258.3,1083) 103.7x40
            display:flex; flex-direction:column; align-items:flow-start; flex-grow:1; gap:4px
          - span.eyebrow "Melhor liga"  @(258.3,1083) 82.3x14
              color:rgb(143, 147, 132); font-size:10px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
          - span.num.brass "Ouro I"  @(258.3,1101) 46.4x22
              color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:700; line-height:22px; font-variant-numeric:tabular-nums
    - section.card  @(16,1149) 361x177
        flex-shrink:0; padding:12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
      - div.row  @(31,1162) 331x23
          display:flex; justify-content:space-between; align-items:center; margin:0px 2px 10px
        - h2.t2 "Conquistas do ranking"  @(31,1162) 191.3x23
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - button.row.olive "Ver todas"  @(292.7,1166.5) 69.3x14
            display:flex; align-items:center; gap:2px; color:rgb(163, 181, 101); font-size:12px; font-weight:600; line-height:14px; text-align:center
          - svg  @(350,1167.5) 12x12
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="ssn.achv.all.ic" data-icon="material:ChevronRight" viewBox="0 0 24 24" width="12" height="12" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M9 6l6 6-6 6"></path></svg>
      - div.row  @(29,1195) 335x118
          display:flex; gap:8px
        - div.mini-a  @(29,1195) 106.3x118
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:6px; padding:12px 6px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; text-align:center
          - div.ic  @(64.2,1208) 36x36
              display:flex; justify-content:center; align-items:center; color:rgb(94, 98, 85); background-color:rgb(29, 33, 22); border-radius:18px
            - svg  @(73.2,1217) 18x18
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ssn.achv.a1.icon" data-icon="material:Lock" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="11" width="18" height="11" rx="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg>
          - span.n "Top 10 global"  @(44,1250) 76.4x14
              font-size:12px; font-weight:600; line-height:14px
          - span.cap "#128"  @(69.7,1270) 25x16
              color:rgb(143, 147, 132); font-size:11px; line-height:16px
        - div.mini-a.ok  @(143.3,1195) 106.3x118
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:6px; padding:12px 6px; color:rgb(127, 176, 105); background-color:rgba(201, 162, 75, 0.16); border:1px solid rgba(201, 162, 75, 0.5); border-radius:14px; text-align:center
          - div.ic  @(178.5,1208) 36x36
              display:flex; justify-content:center; align-items:center; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.16); border-radius:18px
            - svg  @(187.5,1217) 18x18
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ssn.achv.a2.icon" data-icon="material:Place" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
          - span.n "Top 10 local"  @(162.3,1250) 68.4x14
              font-size:12px; font-weight:600; line-height:14px
          - span.cap.brass "Desbloqueada"  @(159,1270) 75x16
              color:rgb(201, 162, 75); font-size:11px; line-height:16px
        - div.mini-a  @(257.7,1195) 106.3x118
            display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:6px; padding:12px 6px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; text-align:center
          - div.ic  @(292.8,1208) 36x36
              display:flex; justify-content:center; align-items:center; color:rgb(94, 98, 85); background-color:rgb(29, 33, 22); border-radius:18px
            - svg  @(301.8,1217) 18x18
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="ssn.achv.a3.icon" data-icon="material:Stars" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 8l4 4 5-7 5 7 4-4-2 12H5z"></path></svg>
          - span.n "Elite da temporada"  @(264.7,1250) 92.3x28
              font-size:12px; font-weight:600; line-height:14px
          - span.cap "Ouro II"  @(293.6,1284) 34.5x16
              color:rgb(143, 147, 132); font-size:11px; line-height:16px
  - footer.dock  @(0,768) 393x83
      flex-shrink:0; padding:12px 16px 18px; margin:0px -16px; background-color:rgba(16, 18, 13, 0.9); border-radius:26px 26px 0px 0px
    - div.row  @(16,781) 361x52
        display:flex; align-items:center; gap:10px
      - button.btn.btn-olive "Compartilhar posição"  @(16,781) 174.5x52
          display:flex; justify-content:center; align-items:center; flex-grow:1; gap:8px; padding:0px 10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:24px; font-size:14px; font-weight:700; line-height:16px; text-align:center; transition:transform 0.12s ease-out
        - svg  @(26,798) 18x18
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="ssn.dock.pos.ic" data-icon="material:Share" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="18" cy="5" r="3"></circle><circle cx="6" cy="12" r="3"></circle><circle cx="18" cy="19" r="3"></circle><path d="M8.6 10.5l6.8-4M8.6 13.5l6.8 4"></path></svg>
      - button.btn.btn-ghost "Compartilhar liga"  @(200.5,781) 176.5x52
          display:flex; justify-content:center; align-items:center; flex-grow:1; gap:8px; padding:0px 10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px; font-size:14px; font-weight:700; line-height:16px; text-align:center; transition:transform 0.12s ease-out
