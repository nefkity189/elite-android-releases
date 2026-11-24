# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--page`: #000
- `--bg`: #0a0b09
- `--card`: #161911
- `--card-elev`: #1d2116
- `--line`: rgba(236,232,218,.10)
- `--text`: #ece8da
- `--muted`: #8f9384
- `--olive`: #a3b565
- `--olive-dark`: #5d6b34
- `--red`: #d12b2b
- `--brass`: #c9a24b
- `--on-red`: #fff
- `--on-olive`: #0e1306
- `--display`: "Barlow Condensed","Arial Narrow",sans-serif
- `--body`: "Inter",sans-serif

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Saira Stencil One  loaded=False
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    position:relative; flex-shrink:0; background-color:rgb(10, 11, 9); overflow:hidden
  - div.view  @(0,0) 393x851
      display:flex; position:absolute; flex-direction:column; gap:8px; padding:32px 16px 8px; background-color:rgb(10, 11, 9)
    - div.v  @(16,32) 361x811
        display:flex; flex-direction:column; flex-grow:1; gap:10px
      - div.hrow  @(16,32) 361x52
          display:flex; align-items:center; flex-shrink:0; gap:8px
        - button.back aria="Voltar"  @(16,38) 40x40
            display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:38px
          - svg.ic  @(24,46) 24x24
              overflow:hidden
              svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20z"></path></svg>
        - span.brand "ELITE"  @(64,46) 70.6x24
            font-family:"Saira Stencil One", "Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; line-height:24px; letter-spacing:3px
        - span  @(142.6,58) 103.5x0
            flex-grow:1
        - span.pill.olive.on "◷ 09:42"  @(254,41) 79x34
            display:flex; justify-content:center; align-items:center; padding:0px 12px; color:rgb(163, 181, 101); background-color:color(srgb 0.639216 0.709804 0.396078 / 0.15); border:1px solid color(srgb 0.639216 0.709804 0.396078 / 0.55); border-radius:24px; font-size:13px; font-weight:700; line-height:16px
        - span  @(341,46) 36x24
            display:grid; align-items:center; color:rgb(143, 147, 132); grid-template-columns:36px
          - svg.ic  @(347,46) 24x24
              overflow:hidden
              svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"></path></svg>
      - p.eyb "FOCO, MISSÃO DADA, MISSÃO CUMPRIDA."  @(16,94) 361x14
          color:rgb(163, 181, 101); font-size:12px; font-weight:700; line-height:14px; letter-spacing:1.1px
      - p.big "COMPETIÇÃO ONLINE"  @(16,118) 361x37
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:31px; font-weight:700; line-height:37px
      - div.short  @(16,165) 361x60
          display:flex; gap:10px
        - div  @(16,165) 178.3x60
            display:flex; align-items:center; flex-grow:1; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
          - span.ci  @(29,176) 38x38
              display:grid; align-items:center; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.15); border-radius:50%; grid-template-columns:38px
            - svg.ic  @(37,184) 22x22
                overflow:hidden
                svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M19 5h-2V3H7v2H5c-1.1 0-2 .9-2 2v1c0 2.55 1.92 4.63 4.39 4.94.63 1.5 1.98 2.63 3.61 2.96V19H7v2h10v-2h-4v-3.1c1.63-.33 2.98-1.46 3.61-2.96C19.08 12.63 21 10.55 21 8V7c0-1.1-.9-2-2-2zM5 8V7h2v3.82C5.84 10.4 5 9.3 5 8zm14 0c0 1.3-.84 2.4-2 2.82V7h2v1z"></path></svg>
          - span  @(77,175.5) 104.3x39
            - b "Ranking"  @(77,175.5) 104.3x19
                font-weight:700; line-height:19px
            - small "Dados do servidor"  @(77,198.5) 104.3x15
                color:rgb(143, 147, 132); font-size:12px; line-height:14px
        - div  @(204.3,165) 172.7x60
            display:flex; align-items:center; flex-grow:1; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
          - span.ci  @(217.3,176) 38x38
              display:grid; align-items:center; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.15); border-radius:50%; grid-template-columns:38px
            - svg.ic  @(225.3,184) 22x22
                overflow:hidden
                svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"></path></svg>
          - span  @(265.3,175.5) 89.1x39
            - b "Conquistas"  @(265.3,175.5) 89.1x19
                font-weight:700; line-height:19px
            - small "Dados do perfil"  @(265.3,198.5) 86.9x15
                color:rgb(143, 147, 132); font-size:12px; line-height:14px
      - div.seg  @(16,235) 361x44
          display:flex; gap:4px; padding:4px; background-color:rgb(10, 11, 9); border:1px solid rgba(236, 232, 218, 0.1); border-radius:28px
        - span.pill "2,5K"  @(21,240) 114.3x34
            display:flex; justify-content:center; align-items:center; flex-grow:1; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px; font-size:13px; line-height:16px
        - span.pill.olive.on "5K"  @(139.3,240) 114.3x34
            display:flex; justify-content:center; align-items:center; flex-grow:1; padding:0px 12px; color:rgb(163, 181, 101); background-color:color(srgb 0.639216 0.709804 0.396078 / 0.15); border:1px solid color(srgb 0.639216 0.709804 0.396078 / 0.55); border-radius:24px; font-size:13px; font-weight:700; line-height:16px
        - span.pill "10K"  @(257.7,240) 114.3x34
            display:flex; justify-content:center; align-items:center; flex-grow:1; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px; font-size:13px; line-height:16px
      - div.sec  @(16,289) 361x24
          display:flex; justify-content:space-between; align-items:center
        - h2 "Missões de hoje"  @(16,289) 184.5x24
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:24px; text-transform:uppercase
        - span "02:00 – 21:00"  @(293.5,293) 83.5x16
            color:rgb(143, 147, 132); font-size:13px; line-height:16px
      - div.mrow  @(16,323) 361x74
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px
        - span.t "08:00"  @(29,343) 71.6x34
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:34px
        - span.pill.mute "5K"  @(110.6,343) 42.6x34
            display:flex; justify-content:center; align-items:center; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px; font-size:13px; line-height:16px
        - span.grow  @(163.3,343) 146.7x34
            flex-grow:1
          - span.pill.mute.on "ENCERRADA"  @(163.3,343) 106.7x34
              display:inline-flex; justify-content:center; align-items:center; padding:0px 12px; color:rgb(143, 147, 132); background-color:color(srgb 0.560784 0.576471 0.517647 / 0.15); border:1px solid color(srgb 0.560784 0.576471 0.517647 / 0.55); border-radius:24px; font-size:13px; font-weight:700; line-height:16px
        - span.iconc  @(320,338) 44x44
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:color(srgb 0.639216 0.709804 0.396078 / 0.12); border:1px solid rgb(93, 107, 52); border-radius:50%; transition:transform 0.1s ease-out; grid-template-columns:42px
          - svg.ic aria="Compartilhar convite"  @(331,349) 22x22
              overflow:hidden
              svg: <svg class="ic " viewBox="0 0 24 24" aria-label="Compartilhar convite" role="img"><path d="M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92s2.92-1.31 2.92-2.92-1.31-2.92-2.92-2.92z"></path></svg>
      - div.mrow  @(16,407) 361x74
          display:flex; align-items:center; flex-shrink:0; gap:10px; padding:0px 12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px
        - span.t "09:00"  @(29,427) 71.6x34
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:700; line-height:34px
        - span.pill.mute "5K"  @(110.6,427) 42.6x34
            display:flex; justify-content:center; align-items:center; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px; font-size:13px; line-height:16px
        - span.grow  @(163.3,427) 146.7x34
            flex-grow:1
          - span.pill.red.on "EM ANDAMENTO"  @(163.3,427) 135.5x34
              display:inline-flex; justify-content:center; align-items:center; padding:0px 12px; color:rgb(209, 43, 43); background-color:color(srgb 0.819608 0.168627 0.168627 / 0.15); border:1px solid color(srgb 0.819608 0.168627 0.168627 / 0.55); border-radius:24px; font-size:13px; font-weight:700; line-height:16px
        - button.iconc aria="Assistir ao vivo"  @(320,422) 44x44
            display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:color(srgb 0.639216 0.709804 0.396078 / 0.12); border:1px solid rgb(93, 107, 52); border-radius:50%; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:42px
          - svg.ic  @(331,433) 22x22
              overflow:hidden
              svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"></path></svg>
      - div.mcard  @(16,491) 361x175
          display:flex; flex-direction:column; flex-shrink:0; gap:10px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.l1  @(31,506) 331x43
            display:flex; align-items:center; gap:8px
          - span.t "10:00"  @(31,506) 92.1x43
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:36px; font-weight:700; line-height:43px
          - span.pill.mute "5K"  @(131.1,510.5) 42.6x34
              display:flex; justify-content:center; align-items:center; padding:0px 12px; background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:24px; font-size:13px; line-height:16px
          - span.sp  @(181.7,527.5) 52x0
              flex-grow:1
          - span.pill.olive.on "AGUARDANDO"  @(241.7,510.5) 120.3x34
              display:flex; justify-content:center; align-items:center; padding:0px 12px; color:rgb(163, 181, 101); background-color:color(srgb 0.639216 0.709804 0.396078 / 0.15); border:1px solid color(srgb 0.639216 0.709804 0.396078 / 0.55); border-radius:24px; font-size:13px; font-weight:700; line-height:16px
        - div.l2  @(31,559) 331x30
            display:flex; align-items:center; gap:8px
          - span.avs "+"  @(31,559) 30x30
              display:grid; align-items:center; color:rgb(143, 147, 132); border:1px solid rgb(143, 147, 132); border-radius:50%; font-size:18px; grid-template-columns:28px
          - span.disp "0/∞"  @(69,563) 29.4x22
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700
          - span "Mínimo 2 competidores"  @(106.4,566.5) 133.5x15
              color:rgb(143, 147, 132); font-size:12px
        - div.l3  @(31,599) 331x52
            display:flex; align-items:center; gap:8px
          - span.act "Entrar na missão"  @(31,599) 279x52
              display:grid; align-items:center; flex-grow:1; color:rgb(255, 255, 255); background-color:rgb(209, 43, 43); border:1px solid rgb(209, 43, 43); border-radius:28px; font-weight:700; line-height:19px; transition:transform 0.1s ease-out; grid-template-columns:277px
          - span.iconc  @(318,603) 44x44
              display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:color(srgb 0.639216 0.709804 0.396078 / 0.12); border:1px solid rgb(93, 107, 52); border-radius:50%; transition:transform 0.1s ease-out; grid-template-columns:42px
            - svg.ic  @(329,614) 22x22
                overflow:hidden
                svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92s2.92-1.31 2.92-2.92-1.31-2.92-2.92-2.92z"></path></svg>
  - div.view  [display:none]
  - div.view  [display:none]
