# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--page`: #000000
- `--bg`: #0a0b09
- `--surface`: #10120d
- `--card`: #161911
- `--card-elev`: #1d2116
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
- `--on-red`: #fff4f0
- `--brass`: #c9a24b
- `--share-glow`: #1e2314
- `--route-halo`: #1c2015
- `--white`: #ffffff
- `--display`: "Barlow Condensed","Arial Narrow",sans-serif
- `--body`: "Inter",sans-serif
- `--logo`: "Saira Stencil One","Barlow Condensed",sans-serif

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    position:relative; flex-shrink:0; background-color:rgb(10, 11, 9); overflow:hidden
  - div.view  @(0,0) 393x851
      display:flex; position:absolute; flex-direction:column; padding:0px 20px; background-color:rgb(10, 11, 9)
    - div.statusbar  @(20,0) 353x32
        flex-shrink:0
    - header  @(20,32) 353x56
        display:flex; align-items:center; flex-shrink:0
      - h1 "Progresso"  @(20,48.5) 106.1x23
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px
    - section  @(20,88) 353x689
        display:flex; flex-direction:column; flex-grow:1; gap:16px; padding:4px 0px 0px; overflow:hidden
      - div  @(20,92) 353x24
          display:flex; justify-content:space-between; align-items:center; flex-shrink:0
        - h2 "Histórico de Corridas"  @(20,93) 167.8x22
            font-weight:700; line-height:22px; letter-spacing:0.1px
        - span "3 corridas"  @(293.2,92) 79.8x24
            display:flex; align-items:center; padding:0px 12px; background-color:rgb(29, 33, 22); border-radius:12px; font-size:11px; font-weight:700; line-height:16px; letter-spacing:0.13px
      - article.card.run  @(20,132) 353x134.6
          flex-shrink:0; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px
        - div.run-head  @(37,149) 319x40
            display:flex; align-items:center; gap:8px
          - div.run-info  @(37,149) 211x40
              flex-grow:1
            - p.run-date "Sábado, 22 de nov"  @(37,149) 211x16
                font-size:12.5px; font-weight:600; line-height:16px; letter-spacing:0.15px
            - p.run-title "Corrida Livre"  @(37,167) 211x22
                margin:2px 0px 0px; font-weight:700; line-height:22px; letter-spacing:0.1px
          - div.run-mini  @(256,151) 52x36
              display:grid; align-items:center; flex-shrink:0; background-color:rgba(163, 181, 101, 0.14); border-radius:8px; grid-template-columns:52px
            - svg  @(260,155) 44x28
                overflow:hidden
                svg: <svg data-id="prog.run1.mini.svg" aria-hidden="true" viewBox="0 0 44 28"><polyline class="mini-line" points="4,20 10,12 18,8 28,6 38,11 40,19 32,24 20,25 10,24"></polyline></svg>
          - button.run-arrow aria="Ver detalhes da corrida"  @(316,149) 40x40
              display:grid; align-items:center; flex-shrink:0; border-radius:20px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:40px
            - span.run-arrow-bg  @(320,153) 32x32
                display:grid; align-items:center; background-color:rgba(163, 181, 101, 0.14); border-radius:16px; grid-template-columns:32px
              - svg  @(327,160) 18x18
                  overflow:hidden
                  svg: <svg data-id="prog.run1.arrow.icon" data-icon="material:AutoMirrored.Filled.ArrowForward" aria-hidden="true" viewBox="0 0 24 24"><path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"></path></svg>
        - div.run-divider  @(37,201) 319x1
            margin:12px 0px 10px; background-color:rgba(236, 232, 218, 0.1)
        - div.run-metrics  @(37,212) 319x37.6
            display:flex; justify-content:space-between
          - div  @(37,212) 37.6x37.6
            - p.run-metric-label "Tempo"  @(37,212) 37.6x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "28:41"  @(37,230) 37.6x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(117.2,212) 51.1x37.6
            - p.run-metric-label "Distância"  @(117.2,212) 51.1x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "5,12 km"  @(117.2,230) 51.1x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(210.9,212) 61.4x37.6
            - p.run-metric-label "Ritmo"  @(210.9,212) 61.4x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "5:36 /km"  @(210.9,230) 61.4x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(315,212) 41x37.6
            - p.run-metric-label "Esforço"  @(315,212) 41x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "6/10"  @(315,230) 41x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
      - article.card.run  @(20,282.6) 353x134.6
          flex-shrink:0; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px
        - div.run-head  @(37,299.6) 319x40
            display:flex; align-items:center; gap:8px
          - div.run-info  @(37,299.6) 211x40
              flex-grow:1
            - p.run-date "Quinta, 20 de nov"  @(37,299.6) 211x16
                font-size:12.5px; font-weight:600; line-height:16px; letter-spacing:0.15px
            - p.run-title "Treino 7"  @(37,317.6) 211x22
                margin:2px 0px 0px; font-weight:700; line-height:22px; letter-spacing:0.1px
          - div.run-mini  @(256,301.6) 52x36
              display:grid; align-items:center; flex-shrink:0; background-color:rgba(163, 181, 101, 0.14); border-radius:8px; grid-template-columns:52px
            - svg  @(260,305.6) 44x28
                overflow:hidden
                svg: <svg data-id="prog.run2.mini.svg" aria-hidden="true" viewBox="0 0 44 28"><polyline class="mini-line" points="4,22 12,16 20,18 28,10 36,12 40,6"></polyline></svg>
          - button.run-arrow aria="Ver detalhes da corrida"  @(316,299.6) 40x40
              display:grid; align-items:center; flex-shrink:0; border-radius:20px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:40px
            - span.run-arrow-bg  @(320,303.6) 32x32
                display:grid; align-items:center; background-color:rgba(163, 181, 101, 0.14); border-radius:16px; grid-template-columns:32px
              - svg  @(327,310.6) 18x18
                  overflow:hidden
                  svg: <svg data-id="prog.run2.arrow.icon" data-icon="material:AutoMirrored.Filled.ArrowForward" aria-hidden="true" viewBox="0 0 24 24"><path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"></path></svg>
        - div.run-divider  @(37,351.6) 319x1
            margin:12px 0px 10px; background-color:rgba(236, 232, 218, 0.1)
        - div.run-metrics  @(37,362.6) 319x37.6
            display:flex; justify-content:space-between
          - div  @(37,362.6) 37.8x37.6
            - p.run-metric-label "Tempo"  @(37,362.6) 37.8x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "22:10"  @(37,380.6) 37.8x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(116.5,362.6) 56.5x37.6
            - p.run-metric-label "Distância"  @(116.5,362.6) 56.5x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "3,40 km"  @(116.5,380.6) 56.5x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(214.8,362.6) 58.5x37.6
            - p.run-metric-label "Ritmo"  @(214.8,362.6) 58.5x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "6:31 /km"  @(214.8,380.6) 58.5x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(315,362.6) 41x37.6
            - p.run-metric-label "Esforço"  @(315,362.6) 41x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "5/10"  @(315,380.6) 41x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
      - article.card.run  @(20,433.2) 353x134.6
          flex-shrink:0; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:16px
        - div.run-head  @(37,450.2) 319x40
            display:flex; align-items:center; gap:8px
          - div.run-info  @(37,450.2) 211x40
              flex-grow:1
            - p.run-date "Terça, 18 de nov"  @(37,450.2) 211x16
                font-size:12.5px; font-weight:600; line-height:16px; letter-spacing:0.15px
            - p.run-title "Treino 6"  @(37,468.2) 211x22
                margin:2px 0px 0px; font-weight:700; line-height:22px; letter-spacing:0.1px
          - div.run-mini  @(256,452.2) 52x36
              display:grid; align-items:center; flex-shrink:0; background-color:rgba(163, 181, 101, 0.14); border-radius:8px; grid-template-columns:52px
            - svg  @(260,456.2) 44x28
                overflow:hidden
                svg: <svg data-id="prog.run3.mini.svg" aria-hidden="true" viewBox="0 0 44 28"><polyline class="mini-line" points="4,14 12,8 22,10 30,18 38,16 40,22"></polyline></svg>
          - button.run-arrow aria="Ver detalhes da corrida"  @(316,450.2) 40x40
              display:grid; align-items:center; flex-shrink:0; border-radius:20px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:40px
            - span.run-arrow-bg  @(320,454.2) 32x32
                display:grid; align-items:center; background-color:rgba(163, 181, 101, 0.14); border-radius:16px; grid-template-columns:32px
              - svg  @(327,461.2) 18x18
                  overflow:hidden
                  svg: <svg data-id="prog.run3.arrow.icon" data-icon="material:AutoMirrored.Filled.ArrowForward" aria-hidden="true" viewBox="0 0 24 24"><path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"></path></svg>
        - div.run-divider  @(37,502.2) 319x1
            margin:12px 0px 10px; background-color:rgba(236, 232, 218, 0.1)
        - div.run-metrics  @(37,513.2) 319x37.6
            display:flex; justify-content:space-between
          - div  @(37,513.2) 38.2x37.6
            - p.run-metric-label "Tempo"  @(37,513.2) 38.2x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "19:05"  @(37,531.2) 38.2x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(116,513.2) 56.1x37.6
            - p.run-metric-label "Distância"  @(116,513.2) 56.1x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "2,90 km"  @(116,531.2) 56.1x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(212.8,513.2) 61.4x37.6
            - p.run-metric-label "Ritmo"  @(212.8,513.2) 61.4x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "6:35 /km"  @(212.8,531.2) 61.4x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
          - div  @(315,513.2) 41x37.6
            - p.run-metric-label "Esforço"  @(315,513.2) 41x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - p.run-metric-value "5/10"  @(315,531.2) 41x19.6
                margin:2px 0px 0px; font-size:14px; font-weight:700; line-height:19.6px
    - nav  @(12,777) 369x74
        display:flex; align-items:flex-end; flex-shrink:0; padding:0px 0px 10px; margin:0px -8px
      - div  @(12,777) 369x64
          display:flex; align-items:center; flex-grow:1; padding:0px 6px; background-color:rgb(16, 18, 13); border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px
        - div.nav-item "Hoje"  @(19,778) 71x62
            display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px
          - svg  @(43.5,790) 22x22
              overflow:hidden
              svg: <svg viewBox="0 0 24 24"><path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"></path></svg>
        - div.nav-item "Plano"  @(90,778) 71x62
            display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px
          - svg  @(114.5,790) 22x22
              overflow:hidden
              svg: <svg viewBox="0 0 24 24"><path d="M19 4h-1V2h-2v2H8V2H6v2H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2zm0 16H5V10h14zM7 12h4v4H7z"></path></svg>
        - div.nav-coach  @(161,785) 71x48
            display:flex; justify-content:center; align-items:center; flex-grow:1
          - div  @(172.5,785) 48x48
              padding:2px; background-image:conic-gradient(rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:16px
            - div  @(174.5,787) 44x44
                display:grid; align-items:center; background-image:radial-gradient(circle at 30% 20%, rgb(38, 43, 26), rgb(12, 14, 8) 70%); border-radius:14px; grid-template-columns:44px
              - svg  @(183.5,796) 26x26
                  overflow:hidden
                  svg: <svg viewBox="0 0 26 26"><path d="M9 4h12l-1.6 4H12.5l-.8 3H18l-1.5 3.8h-5.1l-.9 3.2H19L17.4 22H5l3.2-12z" fill="#d12b2b"></path></svg>
        - div.nav-item "Progresso"  @(232,778) 71x62
            display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; color:rgb(163, 181, 101); font-size:11px; font-weight:500; line-height:16px
          - svg  @(256.5,790) 22x22
              overflow:hidden
              svg: <svg viewBox="0 0 24 24"><path d="M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z"></path></svg>
        - div.nav-item "Perfil"  @(303,778) 71x62
            display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px
          - svg  @(327.5,790) 22x22
              overflow:hidden
              svg: <svg viewBox="0 0 24 24"><path d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"></path></svg>
  - div.view  [display:none]
