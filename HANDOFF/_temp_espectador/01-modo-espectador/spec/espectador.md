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
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    position:relative; flex-shrink:0; background-color:rgb(10, 11, 9); overflow:hidden
  - div.view  [display:none]
  - div.view  @(0,0) 393x851
      display:flex; position:absolute; flex-direction:column; gap:8px; padding:32px 16px 8px; background-color:rgb(10, 11, 9)
    - div.v  @(16,32) 361x811
        display:flex; flex-direction:column; flex-grow:1; gap:10px
      - header.topbar  @(16,32) 361x56
          display:flex; align-items:center; flex-shrink:0; gap:12px
        - button.back aria="Voltar"  @(16,40) 40x40
            display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:38px
          - svg.ic  @(24,48) 24x24
              overflow:hidden
              svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20z"></path></svg>
        - div.tt  @(68,36) 198.1x48
            flex-grow:1
          - p.eyebrow "Modo espectador"  @(68,36) 198.1x13
              color:rgb(143, 147, 132); font-size:11px; font-weight:700; line-height:13px; letter-spacing:1.2px; text-transform:uppercase
          - h1 "5K · 09:00"  @(68,49) 198.1x35
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:29px; font-weight:700; line-height:35px; text-transform:uppercase
        - span.pill.red.on "●  AO VIVO"  @(278.1,43) 98.9x34
            display:flex; justify-content:center; align-items:center; padding:0px 12px; color:rgb(209, 43, 43); background-color:color(srgb 0.819608 0.168627 0.168627 / 0.15); border:1px solid color(srgb 0.819608 0.168627 0.168627 / 0.55); border-radius:24px; font-size:13px; font-weight:700; line-height:16px
      - section.tm  @(16,98) 361x101
          display:flex; flex-direction:column; align-items:center; padding:4px 0px
        - p "TEMPO DE PROVA"  @(131.1,102) 130.8x16
            color:rgb(143, 147, 132); font-size:13px; font-weight:700; line-height:16px; letter-spacing:1.1px
        - p.n "22:12"  @(114.7,118) 163.7x77
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:64px; font-weight:700; line-height:77px
      - section.card.lead  @(16,209) 361x111
          display:flex; flex-direction:column; gap:10px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - p.lab "LÍDER · MARINA SOUZA"  @(31,224) 331x13
            color:rgb(143, 147, 132); font-size:11px; font-weight:700; line-height:13px; letter-spacing:1px
        - div.l1  @(31,247) 331x38
            display:flex; justify-content:space-between; align-items:flex-end
          - p.km "4,31"  @(31,247) 135.1x38
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:32px; font-weight:700; line-height:38px
            - span "/ 5,0 km"  @(93.3,261) 72.9x21
                color:rgb(143, 147, 132); font-size:19px; font-weight:500; line-height:23px
          - p.pct "86%"  @(316,257) 46x28
              color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:23px; font-weight:700; line-height:28px
        - div.bar  @(31,295) 331x10
            background-color:rgb(29, 33, 22); border-radius:5px; overflow:hidden
          - i  @(31,295) 284.7x10
              background-color:rgb(163, 181, 101); border-radius:5px
      - div.mets  @(16,330) 361x89
          display:flex; gap:10px
        - div.card  @(16,330) 175.5x89
            display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
          - p.lab "CORRENDO"  @(31,345) 145.5x13
              color:rgb(143, 147, 132); font-size:11px; font-weight:700; line-height:13px; letter-spacing:1px
          - p.v2 "6"  @(31,368) 145.5x36
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:36px
        - div.card  @(201.5,330) 175.5x89
            display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
          - p.lab "CONCLUÍRAM"  @(216.5,345) 145.5x13
              color:rgb(143, 147, 132); font-size:11px; font-weight:700; line-height:13px; letter-spacing:1px
          - p.v2 "0"  @(216.5,368) 145.5x36
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:700; line-height:36px
      - section.card.score  @(16,429) 361x326
          display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.sec  @(31,444) 331x24
            display:flex; justify-content:space-between; align-items:center
          - h2 "Placar ao vivo"  @(31,444) 171.5x24
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:24px; text-transform:uppercase
          - span "Dados do servidor"  @(249,448) 113x16
              color:rgb(143, 147, 132); font-size:13px; line-height:16px
        - div.scroll  @(31,478) 331x262
            flex-grow:1; overflow:hidden
          - div.prow  @(31,478) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "1"  @(41,496.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a1 "MA"  @(74,490) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,491.5) 154.3x33
                flex-grow:1
              - p.pname "Marina Souza"  @(119,491.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,508.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,508.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Tempo: 22:10"  @(139,510) 69.9x13
            - div.pend  @(282.3,490) 69.7x36
                text-align:right
              - p.pkm "4,31 km"  @(282.3,490) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "5:08 /km"  @(282.3,513) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,538) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "2"  @(41,556.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a2 "RA"  @(74,550) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(46, 59, 58); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,551.5) 154.3x33
                flex-grow:1
              - p.pname "Rafael Lima"  @(119,551.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,568.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,568.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Tempo: 22:08"  @(139,570) 72.2x13
            - div.pend  @(282.3,550) 69.7x36
                text-align:right
              - p.pkm "4,26 km"  @(282.3,550) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "5:12 /km"  @(282.3,573) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,598) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "3"  @(41,616.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a3 "BR"  @(74,610) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(70, 48, 31); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,611.5) 154.3x33
                flex-grow:1
              - p.pname "Bruno Alves"  @(119,611.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,628.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,628.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Tempo: 22:11"  @(139,630) 67.4x13
            - div.pend  @(282.3,610) 69.7x36
                text-align:right
              - p.pkm "3,98 km"  @(282.3,610) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "5:34 /km"  @(282.3,633) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,658) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "4"  @(41,676.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a4 "CA"  @(74,670) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(58, 46, 63); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,671.5) 154.3x33
                flex-grow:1
              - p.pname "Camila Reis"  @(119,671.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,688.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,688.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Sinal atrasado"  @(139,690) 73.6x13
            - div.pend  @(282.3,670) 69.7x36
                text-align:right
              - p.pkm "3,74 km"  @(282.3,670) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "5:55 /km"  @(282.3,693) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,718) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "5"  @(41,736.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a0 "DI"  @(74,730) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,731.5) 154.3x33
                flex-grow:1
              - p.pname "Diego Prado"  @(119,731.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,748.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,748.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Tempo: 22:10"  @(139,750) 69.9x13
            - div.pend  @(282.3,730) 69.7x36
                text-align:right
              - p.pkm "3,12 km"  @(282.3,730) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "7:06 /km"  @(282.3,753) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,778) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "6"  @(41,796.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a1 "EL"  @(74,790) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,791.5) 154.3x33
                flex-grow:1
              - p.pname "Elisa Costa"  @(119,791.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,808.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,808.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Tempo: 22:12"  @(139,810) 69.7x13
            - div.pend  @(282.3,790) 69.7x36
                text-align:right
              - p.pkm "2,45 km"  @(282.3,790) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "9:01 /km"  @(282.3,813) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
      - p.note  @(16,765) 361x16
          display:flex; justify-content:center; align-items:center; gap:6px; color:rgb(143, 147, 132); font-size:12px; line-height:14px
        - svg.ic  @(34.3,765) 16x16
            overflow:hidden
            svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"></path></svg>
        - span "Você está assistindo. Nada é registrado no seu perfil."  @(56.3,766) 302.3x14
      - button.act.sec2 "Sair do modo espectador"  @(16,791) 361x52
          display:grid; align-items:center; flex-shrink:0; color:rgb(143, 147, 132); background-color:rgb(10, 11, 9); border:1px solid rgba(236, 232, 218, 0.1); border-radius:28px; font-weight:700; line-height:19px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:359px
  - div.view  [display:none]
