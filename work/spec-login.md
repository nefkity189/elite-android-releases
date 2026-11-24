# Layout spec (dp = CSS px)

Screen: 207 x 404 dp

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

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- div.phone  @(0,0) 207x404
    position:absolute; padding:5px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.18); border-radius:28px; box-shadow:rgba(0, 0, 0, 0.45) 0px 18px 40px 0px
  - div.phone-screen  @(6,6) 195x392
      display:flex; position:relative; flex-direction:column; padding:24px 14px 14px; background-color:rgb(10, 11, 9); border-radius:23px; overflow:hidden
    - div.phone-notch  @(83,14) 40x4
        position:absolute; background-color:rgba(236, 232, 218, 0.18); border-radius:2px
    - div.m-head  @(20,30) 167x12
        display:flex; justify-content:space-between; align-items:center; margin:0px 0px 12px
      - span.m-label "Competição online"  @(20,30) 125.9x12
          display:flex; align-items:center; gap:6px; color:rgb(163, 181, 101); font-size:9px; font-weight:600; line-height:12px; letter-spacing:1px; text-transform:uppercase
        - i.m-dot  @(20,33) 6x6
            background-color:rgb(163, 181, 101); border-radius:3px
      - span.m-aside "Premium"  @(149,30) 38x12
          color:rgb(143, 147, 132); font-size:9px; font-weight:500; line-height:12px
    - div.m-card.mission  @(20,54) 167x46
        display:flex; align-items:center; gap:10px; padding:0px 10px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:12px
      - span.badge "5K"  @(31,66) 30x22
          flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:8px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:22px; text-align:center
      - div.m-col  @(71,62) 101x30
          display:flex; flex-direction:column
        - span.m-t "Missão das 13:00"  @(71,62) 101x16
            font-size:12px; font-weight:600; line-height:16px
        - span.m-s "3 na sala"  @(71,78) 101x14
            color:rgb(143, 147, 132); font-size:10px; line-height:14px
    - div.countdown  @(20,116) 167x88
        display:flex; flex-direction:column; align-items:center; margin:16px 0px 0px
      - span.m-s "Começa em"  @(75.1,116) 56.7x14
          color:rgb(143, 147, 132); font-size:10px; line-height:14px
      - span.cd-time "18:42"  @(37.5,134) 132x56
          margin:4px 0px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:56px; font-weight:700; line-height:56px; letter-spacing:1px; font-variant-numeric:tabular-nums
      - span.m-s "Vence quem concluir primeiro"  @(32.7,194) 141.6x14
          color:rgb(143, 147, 132); font-size:10px; line-height:14px
    - div.players  @(20,220) 167x102
        display:flex; flex-direction:column; margin:16px 0px 0px
      - div.player  @(20,220) 167x34
          display:flex; align-items:center; gap:10px
        - span.avatar.me "VC"  @(20,224) 26x26
            flex-shrink:0; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:13px; font-size:10px; font-weight:700; line-height:26px; text-align:center
        - span.p-name "Você"  @(56,229) 77.1x16
            flex-grow:1; font-size:12px; font-weight:500; line-height:16px
        - span.p-status "● Na sala"  @(143.1,230) 43.9x14
            color:rgb(127, 176, 105); font-size:10px; font-weight:500; line-height:14px
      - div.player  @(20,254) 167x34
          display:flex; align-items:center; gap:10px
        - span.avatar "RM"  @(20,258) 26x26
            flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:13px; font-size:10px; font-weight:700; line-height:26px; text-align:center
        - span.p-name "Rafael M."  @(56,263) 77.1x16
            flex-grow:1; font-size:12px; font-weight:500; line-height:16px
        - span.p-status "● Na sala"  @(143.1,264) 43.9x14
            color:rgb(127, 176, 105); font-size:10px; font-weight:500; line-height:14px
      - div.player  @(20,288) 167x34
          display:flex; align-items:center; gap:10px
        - span.avatar "CS"  @(20,292) 26x26
            flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:13px; font-size:10px; font-weight:700; line-height:26px; text-align:center
        - span.p-name "Camila S."  @(56,297) 77.1x16
            flex-grow:1; font-size:12px; font-weight:500; line-height:16px
        - span.p-status "● Na sala"  @(143.1,298) 43.9x14
            color:rgb(127, 176, 105); font-size:10px; font-weight:500; line-height:14px
    - div.m-btn "Entrar na prova"  @(20,348) 167x36
        margin:26px 0px 0px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:18px; font-size:12px; font-weight:600; line-height:36px; text-align:center
