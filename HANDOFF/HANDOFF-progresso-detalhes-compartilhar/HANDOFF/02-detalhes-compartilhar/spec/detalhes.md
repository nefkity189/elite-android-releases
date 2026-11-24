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
- Saira Stencil One  loaded=False

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
    - div  @(20,32) 353x710
        display:flex; flex-direction:column; flex-grow:1; gap:12px; padding:12px 0px 0px
      - header  @(20,44) 353x48
          display:flex; justify-content:space-between; align-items:center; flex-shrink:0
        - button.iconbtn aria="Voltar"  @(20,44) 48x48
            display:grid; align-items:center; flex-shrink:0; border-radius:24px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:48px
          - svg  @(32,56) 24x24
              overflow:hidden
              svg: <svg data-id="det.top.back.icon" data-icon="material:AutoMirrored.Filled.ArrowBack" aria-hidden="true" viewBox="0 0 24 24"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"></path></svg>
        - h1 "Detalhes da Corrida"  @(118.2,57) 156.6x22
            font-weight:700; line-height:22px; letter-spacing:0.1px
        - button.iconbtn aria="Compartilhar corrida"  @(325,44) 48x48
            display:grid; align-items:center; flex-shrink:0; border-radius:24px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:48px
          - svg  @(337,56) 24x24
              overflow:hidden
              svg: <svg data-id="det.top.share.icon" data-icon="material:Filled.Share" aria-hidden="true" viewBox="0 0 24 24"><path d="M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92s2.92-1.31 2.92-2.92-1.31-2.92-2.92-2.92z"></path></svg>
      - section  @(20,104) 353x104
          display:flex; flex-direction:column; align-items:center; flex-shrink:0; padding:12px 0px
        - p "5,12 km"  @(138.3,116) 116.4x32
            color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:32px; letter-spacing:0.9px; font-variant-numeric:tabular-nums
        - div  @(116.5,148) 160x48
            display:flex; gap:12px
          - div.metric  @(116.5,148) 71.6x48
              display:flex; flex-direction:column; align-items:center
            - p.metric-value "28:41"  @(116.5,148) 71.6x28
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Tempo"  @(131.5,180) 41.7x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
          - div.metric  @(200.1,148) 76.3x48
              display:flex; flex-direction:column; align-items:center
            - p.metric-value "5:36"  @(210.3,148) 56x28
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Ritmo Médio"  @(200.1,180) 76.3x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
      - section.card  @(20,220) 353x274
          flex-shrink:0; padding:12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div  @(33,233) 327x16
            display:flex; justify-content:space-between; align-items:center
          - p "Trajeto percorrido"  @(33,233) 112.9x16
              color:rgb(143, 147, 132); font-size:12.5px; font-weight:700; line-height:16px; letter-spacing:0.15px
          - div  @(259.6,233) 100.4x16
              display:flex; align-items:center; gap:8px
            - span.dot  @(259.6,237) 8x8
                background-color:rgb(163, 181, 101); border-radius:4px
            - span.legend-text "Início"  @(275.6,233) 28.9x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
            - span  @(312.6,240.5) 4x1
            - span.dot  @(324.6,237) 8x8
                background-color:rgb(209, 43, 43); border-radius:4px
            - span.legend-text "Fim"  @(340.6,233) 19.4x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
        - svg  @(33,261) 329x220
            margin:12px 0px 0px; overflow:hidden
            svg: <svg data-id="det.route.canvas" data-compose="Canvas (rota)" aria-hidden="true" viewBox="0 0 329 220">
        <polyline class="route-line" points="40,150 62,118 100,96 150,78 205,66 255,82 292,112 296,150 270,180 220,192 160,186 110,194 66,182 48,166"></polyline>
        <circle class="pin-start" cx="40" cy="150" r="3.3"></circle><circle class="pin-core" cx="40" cy="150" r="1.5"></circle>
        <circle class="pin-end" cx="48" cy="166" r="3.3"></circle><circle class="pin-core" cx="48" cy="166" r="1.5"></circle>
      </svg>
      - section.card  @(20,506) 353x260
          flex-shrink:0; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div  @(37,523) 319x22
            display:flex; justify-content:space-between; align-items:center
          - p "Todos os dados"  @(37,526) 98x16
              font-size:12.5px; font-weight:700; line-height:16px; letter-spacing:0.15px
          - span "Corrida livre"  @(268.1,523) 87.9x22
              display:flex; align-items:center; padding:0px 10px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:11px; font-size:11px; font-weight:700; line-height:16px; letter-spacing:0.13px
        - p "Sábado, 22 de novembro · 15:04"  @(37,547) 319x16
            margin:2px 0px 0px; color:rgb(143, 147, 132); font-size:11px; line-height:16px; letter-spacing:0.13px
        - div  @(37,577) 319x172
            display:grid; gap:14px 16px; margin:14px 0px 0px; grid-template-columns:151.5px 151.5px
          - div.metric.start  @(37,577) 151.5x48
              display:flex; flex-direction:column; align-items:flex-start
            - p.metric-value.olive "5,12 km"  @(37,577) 102.8x28
                color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Distância"  @(37,609) 56.7x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
          - div.metric.start  @(204.5,577) 151.5x48
              display:flex; flex-direction:column; align-items:flex-start
            - p.metric-value "28:41"  @(204.5,577) 71.6x28
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Tempo total"  @(204.5,609) 72.1x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
          - div.metric.start  @(37,639) 151.5x48
              display:flex; flex-direction:column; align-items:flex-start
            - p.metric-value "5:36 /km"  @(37,639) 112.1x28
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Ritmo médio"  @(37,671) 76x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
          - div.metric.start  @(204.5,639) 151.5x48
              display:flex; flex-direction:column; align-items:flex-start
            - p.metric-value "5:08 /km"  @(204.5,639) 112.1x28
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Ritmo mais rápido"  @(204.5,671) 109.5x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
          - div.metric.start  @(37,701) 151.5x48
              display:flex; flex-direction:column; align-items:flex-start
            - p.metric-value "6/10"  @(37,701) 54.5x28
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Esforço (Moderado)"  @(37,733) 122x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
          - div.metric.start  @(204.5,701) 151.5x48
              display:flex; flex-direction:column; align-items:flex-start
            - p.metric-value "1.284"  @(204.5,701) 70.1x28
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Pontos de GPS"  @(204.5,733) 90.6x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
      - section.card  @(20,778) 353x144
          display:flex; align-items:center; flex-shrink:0; gap:16px; padding:16px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - button aria="Abrir cartão de compartilhamento"  @(37,795) 88x110
            position:relative; flex-shrink:0; background-image:radial-gradient(circle, rgb(30, 35, 20), rgb(10, 11, 9)); border:1px solid rgb(93, 107, 52); border-radius:12px; text-align:center; overflow:hidden
          - span "ELITE"  @(46,804) 23.7x8
              position:absolute; font-family:"Saira Stencil One", "Barlow Condensed", sans-serif; font-size:6px; line-height:8px; letter-spacing:1.4px
          - svg  @(46,818) 72x44
              position:absolute; overflow:hidden
              svg: <svg data-id="det.share.thumb.route" aria-hidden="true" viewBox="0 0 72 44"><polyline class="thumb-halo" points="6,32 12,22 22,16 34,12 48,14 58,22 60,32 50,38 34,40 18,38"></polyline><polyline class="thumb-line" points="6,32 12,22 22,16 34,12 48,14 58,22 60,32 50,38 34,40 18,38"></polyline></svg>
          - span  @(46,862) 55.5x20
              display:flex; position:absolute; align-items:flex-end; gap:2px
            - span "5,12"  @(46,862) 42.8x20
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:22px; font-weight:600; line-height:20px
            - span "km"  @(90.8,870) 10.7x12
                padding:0px 0px 2px; color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:8px; line-height:10px
          - span "28:41 · 5:36"  @(46,886) 43.4x9
              position:absolute; color:rgb(143, 147, 132); font-size:7px; font-weight:600; line-height:9px; letter-spacing:0.3px
        - div  @(141,796) 215x108
            display:flex; flex-direction:column; flex-grow:1
          - p "Compartilhar corrida"  @(141,796) 215x22
              font-weight:700; line-height:22px; letter-spacing:0.1px
          - p "Cartão com a rota, a distância e o tempo desta corrida."  @(141,820) 215x32
              margin:2px 0px 0px; color:rgb(143, 147, 132); font-size:12px; line-height:16px
          - button aria="Compartilhar corrida"  @(141,864) 215x40
              display:flex; justify-content:center; align-items:center; gap:8px; margin:12px 0px 0px; background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(163, 181, 101); border-radius:20px; text-align:center; transition:transform 0.1s ease-out
            - svg  @(193.7,875) 18x18
                overflow:hidden
                svg: <svg data-id="det.share.cta.icon" data-icon="material:Filled.Share" aria-hidden="true" viewBox="0 0 24 24"><path d="M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92s2.92-1.31 2.92-2.92-1.31-2.92-2.92-2.92z"></path></svg>
            - span "Compartilhar"  @(219.7,876) 83.6x16
                color:rgb(163, 181, 101); font-size:13px; font-weight:700; line-height:16px
      - section.card  @(20,934) 353x232
          flex-shrink:0; padding:12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - p "Ritmo ao longo do treino"  @(33,947) 327x16
            font-size:12.5px; font-weight:700; line-height:16px; letter-spacing:0.15px
        - p "Janela móvel de 30s filtrada"  @(33,963) 327x16
            color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:16px; letter-spacing:0.13px
        - div  @(33,993) 329x160
            position:relative; margin:14px 0px 0px
          - svg  @(33,993) 329x160
              position:absolute; overflow:hidden
              svg: <svg data-id="det.pace.chart.svg" aria-hidden="true" viewBox="0 0 329 160">
          <line class="grid-line" x1="0" y1="14" x2="329" y2="14"></line><line class="grid-line" x1="0" y1="80" x2="329" y2="80"></line><line class="grid-line" x1="0" y1="146" x2="329" y2="146"></line>
          <polyline class="pace-line" points="28,92 52,80 80,70 110,76 140,60 170,66 200,48 230,58 260,40 290,52 320,34"></polyline>
        </svg>
          - span "5:08"  @(33,993) 22x12
              position:absolute; color:rgb(143, 147, 132); font-size:10px; font-weight:500; line-height:12px
          - span "6:02"  @(33,1141) 22x12
              position:absolute; color:rgb(143, 147, 132); font-size:10px; font-weight:500; line-height:12px
      - section.card  @(20,1178) 353x110
          flex-shrink:0; padding:12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - div  @(33,1191) 327x22
            display:flex; align-items:center; gap:8px
          - svg  @(33,1191) 22x22
              overflow:hidden
              svg: <svg data-id="det.records.icon" data-icon="material:Filled.EmojiEvents" aria-hidden="true" viewBox="0 0 24 24"><path d="M19 5h-2V3H7v2H5c-1.1 0-2 .9-2 2v1c0 2.55 1.92 4.63 4.39 4.94.63 1.5 1.98 2.63 3.61 2.96V19H7v2h10v-2h-4v-3.1c1.63-.33 2.98-1.46 3.61-2.96C19.08 12.63 21 10.55 21 8V7c0-1.1-.9-2-2-2zM5 8V7h2v3.82C5.84 10.4 5 9.3 5 8zm14 0c0 1.3-.84 2.4-2 2.82V7h2v1z"></path></svg>
          - p "Seus recordes"  @(63,1194) 89.7x16
              color:rgb(201, 162, 75); font-size:12.5px; font-weight:700; line-height:16px; letter-spacing:0.15px
        - div  @(33,1227) 327x48
            display:flex; justify-content:space-around; margin:14px 0px 0px
          - div.metric  @(63,1227) 118.3x48
              display:flex; flex-direction:column; align-items:center
            - p.metric-value.olive "10,03 km"  @(63,1227) 118.3x28
                color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Maior distância"  @(75.4,1259) 93.6x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
          - div.metric  @(241.5,1227) 88.4x48
              display:flex; flex-direction:column; align-items:center
            - p.metric-value.brass "58:12"  @(249.9,1227) 71.6x28
                color:rgb(201, 162, 75); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:28px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
            - p.metric-label "Maior duração"  @(241.5,1259) 88.4x16
                margin:4px 0px 0px; color:rgb(143, 147, 132); font-size:12.5px; font-weight:500; line-height:16px; letter-spacing:0.15px
      - section.card  @(20,1300) 353x194
          display:flex; flex-direction:column; flex-shrink:0; gap:12px; padding:12px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.18); border-radius:18px
        - p "Anotações particulares"  @(33,1313) 327x16
            font-size:12.5px; font-weight:700; line-height:16px; letter-spacing:0.15px
        - p "Sensações, clima ou observações do seu treino (salvo no celular)."  @(33,1341) 327x32
            color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div "Ex: Bom ritmo no trecho intermediário..."  @(33,1385) 327x96
            padding:12px 16px; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.18); border-radius:14px; font-size:14px; line-height:19.6px
      - div  @(20,1506) 353x16
          flex-shrink:0
    - footer  @(20,742) 353x109
        flex-shrink:0; background-color:rgb(10, 11, 9)
      - div  @(20,742) 353x1
          background-color:rgba(236, 232, 218, 0.18)
      - div  @(20,743) 353x108
          padding:12px 0px 40px
        - button "Salvar alterações"  @(20,755) 353x56
            display:flex; justify-content:center; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-weight:700; line-height:22px; text-align:center
  - div.view  [display:none]
