# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--page`: #000000
- `--bg`: #0A0B09
- `--bg-0`: rgba(10,11,9,0)
- `--surface`: #10120D
- `--card`: #161911
- `--card2`: #1D2116
- `--field`: #0D0F0A
- `--line`: rgba(236,232,218,.10)
- `--line2`: rgba(236,232,218,.18)
- `--text`: #ECE8DA
- `--muted`: #8F9384
- `--dim`: #5E6255
- `--olive`: #A3B565
- `--olive-dark`: #5D6B34
- `--olive-soft`: rgba(163,181,101,.14)
- `--on-olive`: #0E1306
- `--olive-glow`: rgba(163,181,101,.20)
- `--olive-glow0`: rgba(163,181,101,0)
- `--red`: #D12B2B
- `--on-red`: #FFF4F0
- `--brass`: #C9A24B
- `--brass-soft`: rgba(201,162,75,.16)
- `--brass-50`: rgba(201,162,75,.5)
- `--amber`: #E0A93A
- `--amber-soft`: rgba(224,169,58,.13)
- `--amber-line`: rgba(224,169,58,.35)
- `--ok`: #7FB069
- `--step-rest`: #4A4E40
- `--scrim`: rgba(0,0,0,.6)
- `--shadow`: rgba(0,0,0,.45)
- `--disp`: "Barlow Condensed","Arial Narrow",sans-serif
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
- body  @(0,0) 393x851
    display:flex; justify-content:center; color:rgb(236, 232, 218); background-color:rgb(0, 0, 0); font-family:Inter, sans-serif
  - main  @(0,0) 393x851
      position:relative; flex-shrink:0; background-color:rgb(10, 11, 9); overflow:hidden
    - div  @(0,0) 393x468
        position:absolute; background-color:rgb(10, 11, 9); overflow:hidden
      - svg  @(0,0) 477x468
          position:absolute; overflow:hidden; transition:transform 0.35s cubic-bezier(0.2, 0.8, 0.2, 1)
          svg: <svg data-id="login.hero.art" aria-hidden="true" viewBox="0 0 477 468" width="477" height="468" fill="none"><path d="M374.3,150.0 C376.1,154.5 376.8,161.1 374.6,165.4 C372.3,169.6 365.6,173.0 360.7,175.6 C355.8,178.1 350.4,179.6 345.3,180.7 C340.2,181.9 334.9,182.8 330.0,182.4 C325.1,182.0 320.5,179.9 315.9,178.3 C311.4,176.7 306.8,175.2 302.7,172.8 C298.6,170.3 293.3,167.2 291.2,163.4 C289.0,159.6 289.4,154.4 289.5,150.0 C289.7,145.6 290.8,141.4 292.2,137.0 C293.6,132.5 294.2,126.6 297.7,123.1 C301.1,119.5 307.5,116.6 312.9,115.6 C318.3,114.6 324.8,116.1 330.0,117.1 C335.2,118.0 340.1,119.3 344.3,121.3 C348.4,123.3 351.8,126.3 355.1,129.1 C358.3,132.0 360.5,134.9 363.7,138.4 C366.9,141.8 372.5,145.5 374.3,150.0Z" style="stroke:var(--olive);stroke-opacity:0.22;stroke-width:1.2"></path><path d="M411.1,150.0 C414.1,158.2 414.3,170.0 409.8,177.5 C405.3,185.1 392.9,190.6 384.1,195.1 C375.3,199.6 366.1,202.3 357.0,204.4 C348.0,206.6 338.7,208.5 330.0,208.0 C321.3,207.4 312.9,203.9 304.6,201.1 C296.4,198.3 288.0,195.7 280.6,191.2 C273.2,186.7 264.1,180.9 260.3,174.1 C256.4,167.2 257.3,158.0 257.5,150.0 C257.7,142.0 259.1,134.5 261.5,126.3 C263.8,118.2 265.3,107.6 271.6,101.3 C277.9,95.1 289.8,90.3 299.6,88.7 C309.3,87.2 320.7,90.3 330.0,92.0 C339.3,93.8 347.7,95.8 355.2,99.2 C362.8,102.6 369.0,107.5 375.1,112.4 C381.2,117.3 386.0,122.3 392.0,128.6 C398.0,134.9 408.2,141.8 411.1,150.0Z" style="stroke:var(--olive);stroke-opacity:0.202;stroke-width:1.2"></path><path d="M449.1,150.0 C4
      - div  @(0,0) 393x468
          position:absolute; background-image:radial-gradient(circle at 196px 300px, rgba(163, 181, 101, 0.2), rgba(163, 181, 101, 0) 230px)
      - div  @(0,328) 393x140
          position:absolute; background-image:linear-gradient(rgba(10, 11, 9, 0), rgb(10, 11, 9))
    - div  @(0,0) 393x600
        position:absolute; overflow:hidden
      - div  @(0,0) 1572x600
          display:flex; position:absolute; transition:transform 0.35s cubic-bezier(0.2, 0.8, 0.2, 1)
        - section.slide  @(0,0) 393x600
            position:relative; flex-shrink:0
          - div.phone  @(93,108) 207x404
              position:absolute; padding:5px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; box-shadow:rgba(0, 0, 0, 0.45) 0px 18px 40px 0px
            - div.phone-screen  @(99,114) 195x392
                display:flex; position:relative; flex-direction:column; padding:24px 14px 14px; background-color:rgb(10, 11, 9); border-radius:23px; overflow:hidden
              - div.phone-notch  @(176,122) 40x4
                  position:absolute; background-color:rgba(236, 232, 218, 0.18); border-radius:2px
              - div.m-head  @(113,138) 167x12
                  display:flex; justify-content:space-between; align-items:center; margin:0px 0px 12px
                - span.m-label "Competição online"  @(113,138) 125.9x12
                    display:flex; align-items:center; gap:6px; color:rgb(163, 181, 101); font-size:9px; font-weight:600; line-height:12px; letter-spacing:1px; text-transform:uppercase
                  - i.m-dot  @(113,141) 6x6
                      background-color:rgb(163, 181, 101); border-radius:3px
                - span.m-aside "Premium"  @(242,138) 38x12
                    color:rgb(143, 147, 132); font-size:9px; font-weight:500; line-height:12px
              - div.m-card.mission  @(113,162) 167x46
                  display:flex; align-items:center; gap:10px; padding:0px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px
                - span.badge "5K"  @(124,174) 30x22
                    flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:8px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:22px; text-align:center
                - div.m-col  @(164,170) 101x30
                    display:flex; flex-direction:column
                  - span.m-t "Missão das 13:00"  @(164,170) 101x16
                      font-size:12px; font-weight:600; line-height:16px
                  - span.m-s "3 na sala"  @(164,186) 101x14
                      color:rgb(143, 147, 132); font-size:10px; line-height:14px
              - div.countdown  @(113,224) 167x88
                  display:flex; flex-direction:column; align-items:center; margin:16px 0px 0px
                - span.m-s "Começa em"  @(168.1,224) 56.7x14
                    color:rgb(143, 147, 132); font-size:10px; line-height:14px
                - span.cd-time "18:42"  @(130.5,242) 132x56
                    margin:4px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:56px; font-weight:700; line-height:56px; letter-spacing:1px; font-variant-numeric:tabular-nums
                - span.m-s "Vence quem concluir primeiro"  @(125.7,302) 141.6x14
                    color:rgb(143, 147, 132); font-size:10px; line-height:14px
              - div.players  @(113,328) 167x102
                  display:flex; flex-direction:column; margin:16px 0px 0px
                - div.player  @(113,328) 167x34
                    display:flex; align-items:center; gap:10px
                  - span.avatar.me "VC"  @(113,332) 26x26
                      flex-shrink:0; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:13px; font-size:10px; font-weight:700; line-height:26px; text-align:center
                  - span.p-name "Você"  @(149,337) 77.1x16
                      flex-grow:1; font-size:12px; font-weight:500; line-height:16px
                  - span.p-status "● Na sala"  @(236.1,338) 43.9x14
                      color:rgb(127, 176, 105); font-size:10px; font-weight:500; line-height:14px
                - div.player  @(113,362) 167x34
                    display:flex; align-items:center; gap:10px
                  - span.avatar "RM"  @(113,366) 26x26
                      flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:13px; font-size:10px; font-weight:700; line-height:26px; text-align:center
                  - span.p-name "Rafael M."  @(149,371) 77.1x16
                      flex-grow:1; font-size:12px; font-weight:500; line-height:16px
                  - span.p-status "● Na sala"  @(236.1,372) 43.9x14
                      color:rgb(127, 176, 105); font-size:10px; font-weight:500; line-height:14px
                - div.player  @(113,396) 167x34
                    display:flex; align-items:center; gap:10px
                  - span.avatar "CS"  @(113,400) 26x26
                      flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:13px; font-size:10px; font-weight:700; line-height:26px; text-align:center
                  - span.p-name "Camila S."  @(149,405) 77.1x16
                      flex-grow:1; font-size:12px; font-weight:500; line-height:16px
                  - span.p-status "● Na sala"  @(236.1,406) 43.9x14
                      color:rgb(127, 176, 105); font-size:10px; font-weight:500; line-height:14px
              - div.m-btn "Entrar na prova"  @(113,456) 167x36
                  margin:26px 0px 0px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:18px; font-size:12px; font-weight:600; line-height:36px; text-align:center
          - h2.slide-title "Corra contra gente de verdade."  @(0,528) 393x64
              position:absolute; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-align:center; text-transform:uppercase
            - br  @(283.5,526) 0x36
        - section.slide  @(393,0) 393x600
            position:relative; flex-shrink:0
          - div.phone  @(486,108) 207x404
              position:absolute; padding:5px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; box-shadow:rgba(0, 0, 0, 0.45) 0px 18px 40px 0px
            - div.phone-screen  @(492,114) 195x392
                display:flex; position:relative; flex-direction:column; padding:24px 14px 14px; background-color:rgb(10, 11, 9); border-radius:23px; overflow:hidden
              - div.phone-notch  @(569,122) 40x4
                  position:absolute; background-color:rgba(236, 232, 218, 0.18); border-radius:2px
              - div.m-head  @(506,138) 167x12
                  display:flex; justify-content:space-between; align-items:center; margin:0px 0px 12px
                - span.m-label "Ranking e ligas"  @(506,138) 105x12
                    display:flex; align-items:center; gap:6px; color:rgb(163, 181, 101); font-size:9px; font-weight:600; line-height:12px; letter-spacing:1px; text-transform:uppercase
                  - i.m-dot  @(506,141) 6x6
                      background-color:rgb(163, 181, 101); border-radius:3px
                - span.m-aside "Temporada"  @(624.3,138) 48.7x12
                    color:rgb(143, 147, 132); font-size:9px; font-weight:500; line-height:12px
              - div.m-card.league  @(506,162) 167x76
                  display:flex; flex-direction:column; gap:4px; padding:10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px
                - div.league-top  @(517,173) 145x22
                    display:flex; align-items:center; gap:8px
                  - svg  @(517,173) 22x22
                      overflow:hidden
                      svg: <svg data-id="login.slide.2.league.icon" data-icon="material:Shield" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="var(--brass)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 3l7.5 3v5.5c0 4.6-3.1 8.2-7.5 9.5C7.6 19.700 4.500 16.100 4.500 11.500V6L12 3z"></path></svg>
                  - span.league-name "Liga Ouro II"  @(547,173) 96.6x22
                      font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:22px; letter-spacing:0.8px; text-transform:uppercase
                - span.m-s "Faltam 58 pontos para subir"  @(517,199) 145x14
                    color:rgb(143, 147, 132); font-size:10px; line-height:14px
                - div.bar  @(517,223) 145x4
                    position:relative; margin:6px 0px 0px; background-color:rgb(29, 33, 22); border-radius:2px
                  - div.bar-fill  @(517,223) 120x4
                      position:absolute; background-color:rgb(163, 181, 101); border-radius:2px
              - div.chips  @(506,250) 167x24
                  display:flex; gap:6px; margin:12px 0px 0px
                - span.chip.on "Global"  @(506,250) 52.5x24
                    padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:12px; font-size:10px; font-weight:500; line-height:22px
                - span.chip "Meu estado"  @(564.5,250) 78.3x24
                    padding:0px 10px; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.18); border-radius:12px; font-size:10px; font-weight:500; line-height:22px
              - div.rank  @(506,284) 167x136
                  display:flex; flex-direction:column; margin:10px 0px 0px
                - div.rank-row  @(506,284) 167x34
                    display:flex; align-items:center; gap:10px; padding:0px 8px; border-radius:8px
                  - span.rank-pos.gold "1"  @(514,292) 14x18
                      color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:15px; font-weight:700; line-height:18px; text-align:center
                  - span.rank-name "Bruno A."  @(538,293) 68.9x16
                      flex-grow:1; font-size:12px; font-weight:500; line-height:16px
                  - span.rank-pts "3.120 pts"  @(616.9,292) 48.1x18
                      font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:600; line-height:18px; font-variant-numeric:tabular-nums
                - div.rank-row  @(506,318) 167x34
                    display:flex; align-items:center; gap:10px; padding:0px 8px; border-radius:8px
                  - span.rank-pos "2"  @(514,326) 14x18
                      color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:15px; font-weight:700; line-height:18px; text-align:center
                  - span.rank-name "Camila S."  @(538,327) 68.9x16
                      flex-grow:1; font-size:12px; font-weight:500; line-height:16px
                  - span.rank-pts "2.940 pts"  @(616.9,326) 48.1x18
                      font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:600; line-height:18px; font-variant-numeric:tabular-nums
                - div.rank-row  @(506,352) 167x34
                    display:flex; align-items:center; gap:10px; padding:0px 8px; border-radius:8px
                  - span.rank-pos "3"  @(514,360) 14x18
                      color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:15px; font-weight:700; line-height:18px; text-align:center
                  - span.rank-name "Rafael M."  @(538,361) 68.9x16
                      flex-grow:1; font-size:12px; font-weight:500; line-height:16px
                  - span.rank-pts "2.815 pts"  @(616.9,360) 48.1x18
                      font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:600; line-height:18px; font-variant-numeric:tabular-nums
                - div.rank-row.me  @(506,386) 167x34
                    display:flex; align-items:center; gap:10px; padding:0px 8px; background-color:rgba(163, 181, 101, 0.14); border-radius:8px
                  - span.rank-pos "4"  @(514,394) 14x18
                      color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:15px; font-weight:700; line-height:18px; text-align:center
                  - span.rank-name "Você"  @(538,395) 68.9x16
                      flex-grow:1; font-size:12px; font-weight:500; line-height:16px
                  - span.rank-pts "2.402 pts"  @(616.9,394) 48.1x18
                      color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:600; line-height:18px; font-variant-numeric:tabular-nums
              - span.note "Exemplo ilustrativo"  @(506,480) 167x12
                  margin:60px 0px 0px; color:rgb(94, 98, 85); font-size:9px; line-height:12px; text-align:center
          - h2.slide-title "Suba no pódio e no ranking."  @(393,528) 393x64
              position:absolute; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-align:center; text-transform:uppercase
            - br  @(675.6,526) 0x36
        - section.slide  @(786,0) 393x600
            position:relative; flex-shrink:0
          - div.phone  @(879,108) 207x404
              position:absolute; padding:5px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; box-shadow:rgba(0, 0, 0, 0.45) 0px 18px 40px 0px
            - div.phone-screen  @(885,114) 195x392
                display:flex; position:relative; flex-direction:column; padding:24px 14px 14px; background-color:rgb(10, 11, 9); border-radius:23px; overflow:hidden
              - div.phone-notch  @(962,122) 40x4
                  position:absolute; background-color:rgba(236, 232, 218, 0.18); border-radius:2px
              - div.m-head  @(899,138) 167x12
                  display:flex; justify-content:space-between; align-items:center; margin:0px 0px 12px
                - span.m-label "Seu plano"  @(899,138) 72.4x12
                    display:flex; align-items:center; gap:6px; color:rgb(163, 181, 101); font-size:9px; font-weight:600; line-height:12px; letter-spacing:1px; text-transform:uppercase
                  - i.m-dot  @(899,141) 6x6
                      background-color:rgb(163, 181, 101); border-radius:3px
                - span.m-aside "Semana 3 de 12"  @(998.3,138) 67.7x12
                    color:rgb(143, 147, 132); font-size:9px; font-weight:500; line-height:12px
              - div.weeks  @(899,168) 167x4
                  display:flex; gap:3px; margin:6px 0px 0px
                - i.week.done  @(899,168) 11.2x4
                    flex-grow:1; background-color:rgb(163, 181, 101); border-radius:2px
                - i.week.done  @(913.2,168) 11.2x4
                    flex-grow:1; background-color:rgb(163, 181, 101); border-radius:2px
                - i.week  @(927.3,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(941.5,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(955.7,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(969.8,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(984,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(998.1,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(1012.3,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(1026.5,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(1040.7,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
                - i.week  @(1054.8,168) 11.2x4
                    flex-grow:1; background-color:rgb(29, 33, 22); border-radius:2px
              - span.plan-title "Corrida leve"  @(899,184) 167x30
                  margin:12px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:30px; letter-spacing:0.9px; text-transform:uppercase
              - span.m-s "Treino de hoje · 42 min"  @(899,214) 167x14
                  color:rgb(143, 147, 132); font-size:10px; line-height:14px
              - div.plan-chart  @(899,242) 167x96
                  display:flex; align-items:flow-end; gap:2px; margin:14px 0px 0px
                - div.seg  @(899,297) 18.6x40
                    flex-grow:3; background-color:rgb(201, 162, 75); border-radius:3px 3px 0px 0px
                - div.seg  @(919.6,237) 31x100
                    flex-grow:5; background-color:rgb(163, 181, 101); border-radius:3px 3px 0px 0px
                - div.seg  @(952.6,292) 12.4x45
                    flex-grow:2; background-color:rgb(74, 78, 64); border-radius:3px 3px 0px 0px
                - div.seg  @(967,237) 31x100
                    flex-grow:5; background-color:rgb(163, 181, 101); border-radius:3px 3px 0px 0px
                - div.seg  @(1000,292) 12.4x45
                    flex-grow:2; background-color:rgb(74, 78, 64); border-radius:3px 3px 0px 0px
                - div.seg  @(1014.4,237) 31x100
                    flex-grow:5; background-color:rgb(163, 181, 101); border-radius:3px 3px 0px 0px
                - div.seg  @(1047.4,297) 18.6x40
                    flex-grow:3; background-color:rgba(201, 162, 75, 0.5); border-radius:3px 3px 0px 0px
              - div.axis  @(899,342) 167x12
                  display:flex; justify-content:space-between; margin:4px 0px 0px; color:rgb(94, 98, 85); font-size:9px; line-height:12px
                - span "0"  @(899,342) 5.7x12
                - span "42 min"  @(1036.8,342) 29.2x12
              - div.sum  @(899,368) 167x34
                  display:flex; margin:14px 0px 12px
                - div.sum-col  @(899,368) 55x34
                    display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:2px
                  - span.sum-v "5,2 km"  @(905.7,368) 41.6x20
                      font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:17px; font-weight:600; line-height:20px; font-variant-numeric:tabular-nums
                  - span.sum-l "Distância"  @(906.9,390) 39.2x12
                      color:rgb(143, 147, 132); font-size:9px; line-height:12px
                - div.sum-col  @(954,368) 56x34
                    display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:2px
                  - span.sum-v "6:10"  @(968.2,368) 28.5x20
                      font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:17px; font-weight:600; line-height:20px; font-variant-numeric:tabular-nums
                  - span.sum-l "Ritmo /km"  @(961.1,390) 42.8x12
                      color:rgb(143, 147, 132); font-size:9px; line-height:12px
                - div.sum-col  @(1010,368) 56x34
                    display:flex; flex-direction:column; align-items:center; flex-grow:1; gap:2px
                  - span.sum-v "3 blocos"  @(1013,368) 50.9x20
                      font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:17px; font-weight:600; line-height:20px; font-variant-numeric:tabular-nums
                  - span.sum-l "De corrida"  @(1016.2,390) 44.6x12
                      color:rgb(143, 147, 132); font-size:9px; line-height:12px
              - div.m-btn "Começar treino"  @(899,456) 167x36
                  margin:42px 0px 0px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:18px; font-size:12px; font-weight:600; line-height:36px; text-align:center
          - h2.slide-title "Seu plano de treino, semana a semana."  @(786,528) 393x64
              position:absolute; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-align:center; text-transform:uppercase
            - br  @(1112.2,526) 0x36
        - section.slide  @(1179,0) 393x600
            position:relative; flex-shrink:0
          - div.phone  @(1272,108) 207x404
              position:absolute; padding:5px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; box-shadow:rgba(0, 0, 0, 0.45) 0px 18px 40px 0px
            - div.phone-screen  @(1278,114) 195x392
                display:flex; position:relative; flex-direction:column; padding:24px 14px 14px; background-color:rgb(10, 11, 9); border-radius:23px; overflow:hidden
              - div.phone-notch  @(1355,122) 40x4
                  position:absolute; background-color:rgba(236, 232, 218, 0.18); border-radius:2px
              - div.m-head  @(1292,138) 167x12
                  display:flex; justify-content:space-between; align-items:center; margin:0px 0px 12px
                - span.m-label "Conquistas"  @(1292,138) 81.7x12
                    display:flex; align-items:center; gap:6px; color:rgb(163, 181, 101); font-size:9px; font-weight:600; line-height:12px; letter-spacing:1px; text-transform:uppercase
                  - i.m-dot  @(1292,141) 6x6
                      background-color:rgb(163, 181, 101); border-radius:3px
                - span.m-aside "2 de 6"  @(1432.1,138) 26.9x12
                    color:rgb(143, 147, 132); font-size:9px; font-weight:500; line-height:12px
              - div.ach-list  @(1292,162) 167x282
                  display:flex; flex-direction:column; gap:8px
                - div.m-card.ach  @(1292,162) 167x50
                    display:flex; align-items:center; gap:10px; padding:0px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px
                  - span.medal  @(1303,171) 32x32
                      display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgba(201, 162, 75, 0.16); border-radius:16px
                    - svg  @(1311,179) 16x16
                        overflow:hidden
                        svg: <svg data-id="login.slide.4.list.a1.icon" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="var(--brass)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="9" r="5.5"></circle><path d="M8.800 13.700L7 21l5-2.600L17 21l-1.800-7.300"></path></svg>
                  - div.m-col  @(1345,174) 90.9x26
                      display:flex; flex-direction:column
                    - span.ach-t "Primeira missão"  @(1345,174) 90.9x14
                        font-size:11px; font-weight:600; line-height:14px
                    - span.ach-d "Conclua uma missão."  @(1345,188) 90.9x12
                        color:rgb(143, 147, 132); font-size:9px; line-height:12px
                - div.m-card.ach  @(1292,220) 167x50
                    display:flex; align-items:center; gap:10px; padding:0px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px
                  - span.medal  @(1303,229) 32x32
                      display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgba(201, 162, 75, 0.16); border-radius:16px
                    - svg  @(1311,237) 16x16
                        overflow:hidden
                        svg: <svg data-id="login.slide.4.list.a2.icon" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="var(--brass)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="9" r="5.5"></circle><path d="M8.800 13.700L7 21l5-2.600L17 21l-1.800-7.300"></path></svg>
                  - div.m-col  @(1345,232) 80.5x26
                      display:flex; flex-direction:column
                    - span.ach-t "Primeira vitória"  @(1345,232) 80.5x14
                        font-size:11px; font-weight:600; line-height:14px
                    - span.ach-d "Seja o primeiro."  @(1345,246) 80.5x12
                        color:rgb(143, 147, 132); font-size:9px; line-height:12px
                - div.m-card.ach  @(1292,278) 167x50
                    display:flex; align-items:center; gap:10px; padding:0px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px
                  - span.medal  @(1303,287) 32x32
                      display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgba(201, 162, 75, 0.16); border-radius:16px
                    - svg  @(1311,295) 16x16
                        overflow:hidden
                        svg: <svg data-id="login.slide.4.list.a3.icon" data-icon="material:EmojiEvents" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="var(--brass)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="9" r="5.5"></circle><path d="M8.800 13.700L7 21l5-2.600L17 21l-1.800-7.300"></path></svg>
                  - div.m-col  @(1345,285.5) 100x35
                      display:flex; flex-direction:column
                    - span.ach-t "Sequência"  @(1345,285.5) 100x14
                        font-size:11px; font-weight:600; line-height:14px
                    - span.ach-d "5 missões seguidas."  @(1345,299.5) 100x12
                        color:rgb(143, 147, 132); font-size:9px; line-height:12px
                    - div.bar.ach-bar  @(1345,316.5) 100x4
                        position:relative; margin:5px 0px 0px; background-color:rgb(29, 33, 22); border-radius:2px
                      - div.bar-fill  @(1345,316.5) 60x4
                          position:absolute; background-color:rgb(163, 181, 101); border-radius:2px
                - div.m-card.ach.locked  @(1292,336) 167x50
                    display:flex; align-items:center; gap:10px; padding:0px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; opacity:0.55
                  - span.medal.off  @(1303,345) 32x32
                      display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(29, 33, 22); border-radius:16px
                    - svg  @(1311,353) 16x16
                        overflow:hidden
                        svg: <svg data-id="login.slide.4.list.a4.icon" data-icon="material:Lock" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="var(--muted)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="5" y="10.500" width="14" height="10" rx="2.500"></rect><path d="M8 10.500V8a4 4 0 0 1 8 0v2.500"></path></svg>
                  - div.m-col  @(1345,348) 119x26
                      display:flex; flex-direction:column
                    - span.ach-t "Participação completa"  @(1345,348) 119x14
                        font-size:11px; font-weight:600; line-height:14px
                    - span.ach-d "2,5K, 5K e 10K."  @(1345,362) 119x12
                        color:rgb(143, 147, 132); font-size:9px; line-height:12px
                - div.m-card.ach.locked  @(1292,394) 167x50
                    display:flex; align-items:center; gap:10px; padding:0px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px; opacity:0.55
                  - span.medal.off  @(1303,403) 32x32
                      display:flex; justify-content:center; align-items:center; flex-shrink:0; background-color:rgb(29, 33, 22); border-radius:16px
                    - svg  @(1311,411) 16x16
                        overflow:hidden
                        svg: <svg data-id="login.slide.4.list.a5.icon" data-icon="material:Lock" viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="var(--muted)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="5" y="10.500" width="14" height="10" rx="2.500"></rect><path d="M8 10.500V8a4 4 0 0 1 8 0v2.500"></path></svg>
                  - div.m-col  @(1345,406) 78.5x26
                      display:flex; flex-direction:column
                    - span.ach-t "Pódio estadual"  @(1345,406) 78.5x14
                        font-size:11px; font-weight:600; line-height:14px
                    - span.ach-d "Chegue ao pódio."  @(1345,420) 78.5x12
                        color:rgb(143, 147, 132); font-size:9px; line-height:12px
          - h2.slide-title "Missão dada é missão cumprida."  @(1179,528) 393x64
              position:absolute; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; text-align:center; text-transform:uppercase
            - br  @(1461.1,526) 0x36
    - header  @(0,44) 393x34
        display:flex; position:absolute; justify-content:center; align-items:center; gap:10px
      - svg  @(117.2,44) 34x34
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="login.logo.mark" data-icon="asset:ic_elite_mark" viewBox="0 0 64 64" width="34" height="34"><path d="M19.1,8.0 L55.1,8.0 L53.2,17.0 L27.2,17.0 L20.8,47.0 L46.8,47.0 L44.9,56.0 L8.9,56.0 Z" style="fill:var(--text)"></path><path d="M25.1,27.0 L45.1,27.0 L43.1,36.0 L23.1,36.0 Z" style="fill:var(--red)"></path></svg>
      - span "ELITE"  @(161.2,44) 114.7x34
          padding:0px 0px 0px 6px; font-family:"Saira Stencil One", Impact, sans-serif; font-size:28px; line-height:34px; letter-spacing:6.7px
    - div  @(0,612) 393x8
        display:flex; position:absolute; justify-content:center; gap:8px
      - i.dot  @(160.5,612) 24x8
          background-color:rgb(163, 181, 101); border-radius:4px; transition:width 0.2s ease-out, background-color 0.2s ease-out
      - i.dot  @(192.5,612) 8x8
          background-color:rgb(94, 98, 85); border-radius:4px; transition:width 0.2s ease-out, background-color 0.2s ease-out
      - i.dot  @(208.5,612) 8x8
          background-color:rgb(94, 98, 85); border-radius:4px; transition:width 0.2s ease-out, background-color 0.2s ease-out
      - i.dot  @(224.5,612) 8x8
          background-color:rgb(94, 98, 85); border-radius:4px; transition:width 0.2s ease-out, background-color 0.2s ease-out
    - nav  @(24,634) 345x160
        display:flex; position:absolute; flex-direction:column
      - button.btn aria="Entrar com Google"  @(24,634) 345x52
          display:flex; justify-content:center; align-items:center; gap:8px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:26px; font-weight:600; line-height:22px; text-align:center; transition:transform 0.1s ease-out
        - svg  @(110.7,650) 20x20
            overflow:hidden
            svg: <svg data-id="login.actions.google.icon" viewBox="0 0 24 24" width="20" height="20" fill="currentColor" stroke="none" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M21.35 11.1H12v2.98h5.35c-.5 2.3-2.4 3.5-5.35 3.5a5.9 5.9 0 1 1 0-11.8c1.5 0 2.8.55 3.85 1.45l2.1-2.1A8.9 8.9 0 0 0 12 3.1a8.9 8.9 0 1 0 0 17.8c5.1 0 8.5-3.6 8.5-8.7 0-.4 0-.75-.15-1.1z" fill="currentColor" stroke="none"></path></svg>
        - span  [display:none]
        - span "Entrar com Google"  @(138.7,649) 143.5x22
      - button.btn aria="Entrar com e-mail"  @(24,696) 345x48
          display:flex; justify-content:center; align-items:center; gap:8px; margin:10px 0px 0px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:26px; font-size:15px; font-weight:600; line-height:20px; text-align:center; transition:transform 0.1s ease-out
        - svg  @(118.1,710) 20x20
            overflow:hidden
            svg: <svg data-id="login.actions.email.icon" data-icon="material:Mail" viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><rect x="3" y="5" width="18" height="14" rx="2.5"></rect><path d="M3.5 7.5l8.5 6 8.5-6"></path></svg>
        - span "Entrar com e-mail"  @(146.1,710) 128.7x20
      - button.btn aria="Continuar sem conta"  @(24,750) 345x44
          display:flex; justify-content:center; align-items:center; gap:8px; margin:6px 0px 0px; color:rgb(163, 181, 101); border-radius:22px; font-size:15px; font-weight:600; line-height:20px; text-align:center; transition:opacity 0.1s ease-out
        - span "Continuar sem conta"  @(121.1,762) 150.7x20
    - p "Ao continuar, você aceita os e a ."  @(24,798) 345x28
        position:absolute; color:rgb(143, 147, 132); font-size:11px; line-height:14px; text-align:center
      - a "Termos"  @(180.4,798) 39x14
          color:rgb(236, 232, 218)
      - a "Política de Privacidade"  @(241.3,798) 118.3x14
          color:rgb(236, 232, 218)
    - div  @(0,0) 393x851
        position:absolute; background-color:rgba(0, 0, 0, 0.6); opacity:0; transition:opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1)
    - section  @(0,1002) 393x369
        display:flex; position:absolute; flex-direction:column; padding:10px 24px 28px; background-color:rgb(16, 18, 13); border-radius:26px 26px 0px 0px; transform:matrix(1, 0, 0, 1, 0, 520); transition:transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)
      - div  @(178.5,1013) 36x4
          background-color:rgba(236, 232, 218, 0.18); border-radius:2px
      - h3 "Entrar com e-mail"  @(24,1035) 345x28
          margin:18px 0px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:28px; letter-spacing:0.96px; text-transform:uppercase
      - p "Use o e-mail e a senha da sua conta Elite."  @(24,1067) 345x18
          margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:13px; line-height:18px
      - span.f-label "E-mail"  @(24,1105) 345x14
          margin:20px 0px 0px; color:rgb(143, 147, 132); font-size:12px; font-weight:500; line-height:14px
      - div.field  @(24,1125) 345x56
          display:flex; align-items:center; gap:8px; padding:0px 16px; margin:6px 0px 0px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.18); border-radius:14px
        - span.f-text "voce@email.com"  @(41,1142) 311x22
            flex-grow:1; color:rgb(94, 98, 85); line-height:22px
      - span.f-label.second "Senha"  @(24,1195) 345x14
          margin:14px 0px 0px; color:rgb(143, 147, 132); font-size:12px; font-weight:500; line-height:14px
      - div.field  @(24,1215) 345x56
          display:flex; align-items:center; gap:8px; padding:0px 16px; margin:6px 0px 0px; background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.18); border-radius:14px
        - span.f-text "Digite sua senha"  @(41,1232) 271x22
            flex-grow:1; color:rgb(94, 98, 85); line-height:22px
        - button aria="Mostrar senha"  @(320,1221) 44x44
            display:flex; justify-content:center; align-items:center; margin:0px -12px 0px 0px; color:rgb(255, 255, 255); font-size:13.3333px; text-align:center
          - svg  @(331,1232) 22x22
              overflow:hidden
              svg: <svg data-id="login.sheet.eye.icon" data-icon="material:Visibility" viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="var(--muted)" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M2 12s3.5-6.5 10-6.5S22 12 22 12s-3.5 6.5-10 6.5S2 12 2 12z"></path><circle cx="12" cy="12" r="2.8"></circle></svg>
      - p  [display:none]
      - button.btn aria="Entrar"  @(24,1291) 345x52
          display:flex; justify-content:center; align-items:center; gap:8px; margin:20px 0px 0px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:26px; font-weight:600; line-height:22px; text-align:center; transition:transform 0.1s ease-out
        - span  [display:none]
        - span "Entrar"  @(173.1,1306) 46.9x22
