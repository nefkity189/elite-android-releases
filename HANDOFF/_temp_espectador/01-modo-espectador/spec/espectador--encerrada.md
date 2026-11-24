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
        - div.tt  @(68,36) 190.3x48
            flex-grow:1
          - p.eyebrow "Modo espectador"  @(68,36) 190.3x13
              color:rgb(143, 147, 132); font-size:11px; font-weight:700; line-height:13px; letter-spacing:1.2px; text-transform:uppercase
          - h1 "5K · 09:00"  @(68,49) 190.3x35
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:29px; font-weight:700; line-height:35px; text-transform:uppercase
        - span.pill.mute.on "ENCERRADA"  @(270.3,43) 106.7x34
            display:flex; justify-content:center; align-items:center; padding:0px 12px; color:rgb(143, 147, 132); background-color:color(srgb 0.560784 0.576471 0.517647 / 0.15); border:1px solid color(srgb 0.560784 0.576471 0.517647 / 0.55); border-radius:24px; font-size:13px; font-weight:700; line-height:16px
      - section.card.win  @(16,98) 361x74
          display:flex; flex-direction:column; gap:10px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(201, 162, 75, 0.55); border-radius:18px
        - div.w1  @(31,113) 331x44
            display:flex; align-items:center; gap:12px
          - span.ci  @(31,113) 44x44
              display:grid; align-items:center; flex-shrink:0; color:rgb(201, 162, 75); background-color:rgba(201, 162, 75, 0.15); border-radius:50%; grid-template-columns:44px
            - svg.ic  @(41,123) 24x24
                overflow:hidden
                svg: <svg class="ic " viewBox="0 0 24 24" aria-hidden="true"><path d="M19 5h-2V3H7v2H5c-1.1 0-2 .9-2 2v1c0 2.55 1.92 4.63 4.39 4.94.63 1.5 1.98 2.63 3.61 2.96V19H7v2h10v-2h-4v-3.1c1.63-.33 2.98-1.46 3.61-2.96C19.08 12.63 21 10.55 21 8V7c0-1.1-.9-2-2-2zM5 8V7h2v3.82C5.84 10.4 5 9.3 5 8zm14 0c0 1.3-.84 2.4-2 2.82V7h2v1z"></path></svg>
          - div  @(87,113) 243x44
            - b "Marina Souza venceu"  @(87,113) 243x24
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:24px; text-transform:uppercase
            - small "5K em 21:58 · 5 de 6 concluíram"  @(87,141) 197.3x16
                color:rgb(143, 147, 132); font-size:13px; line-height:16px
      - section.card.score  @(16,182) 361x539
          display:flex; flex-direction:column; flex-grow:1; gap:10px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - div.sec  @(31,197) 331x24
            display:flex; justify-content:space-between; align-items:center
          - h2 "Resultado final"  @(31,197) 185.9x24
              font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:700; line-height:24px; text-transform:uppercase
          - span "Dados do servidor"  @(249,201) 113x16
              color:rgb(143, 147, 132); font-size:13px; line-height:16px
        - div.scroll  @(31,231) 331x475
            flex-grow:1; overflow:hidden
          - div.prow  @(31,231) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "1"  @(41,249.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a1 "MA"  @(74,243) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,244.5) 154.3x33
                flex-grow:1
              - p.pname "Marina Souza"  @(119,244.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,261.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,261.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Concluiu em 21:58"  @(139,263) 95.8x13
            - div.pend  @(282.3,243) 69.7x36
                text-align:right
              - p.pkm "5,00 km"  @(282.3,243) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "Concluiu"  @(282.3,266) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,291) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "2"  @(41,309.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a2 "RA"  @(74,303) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(46, 59, 58); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,304.5) 154.3x33
                flex-grow:1
              - p.pname "Rafael Lima"  @(119,304.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,321.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,321.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Concluiu em 22:41"  @(139,323) 96x13
            - div.pend  @(282.3,303) 69.7x36
                text-align:right
              - p.pkm "5,00 km"  @(282.3,303) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "Concluiu"  @(282.3,326) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,351) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "3"  @(41,369.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a3 "BR"  @(74,363) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(70, 48, 31); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,364.5) 154.3x33
                flex-grow:1
              - p.pname "Bruno Alves"  @(119,364.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,381.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,381.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Concluiu em 24:03"  @(139,383) 98.5x13
            - div.pend  @(282.3,363) 69.7x36
                text-align:right
              - p.pkm "5,00 km"  @(282.3,363) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "Concluiu"  @(282.3,386) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,411) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "4"  @(41,429.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a4 "CA"  @(74,423) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(58, 46, 63); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,424.5) 154.3x33
                flex-grow:1
              - p.pname "Camila Reis"  @(119,424.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,441.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,441.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Concluiu em 26:20"  @(139,443) 98.3x13
            - div.pend  @(282.3,423) 69.7x36
                text-align:right
              - p.pkm "5,00 km"  @(282.3,423) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "Concluiu"  @(282.3,446) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,471) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "5"  @(41,489.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a0 "DI"  @(74,483) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(59, 68, 38); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,484.5) 154.3x33
                flex-grow:1
              - p.pname "Diego Prado"  @(119,484.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,501.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,501.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Concluiu em 29:47"  @(139,503) 98x13
            - div.pend  @(282.3,483) 69.7x36
                text-align:right
              - p.pkm "5,00 km"  @(282.3,483) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "Concluiu"  @(282.3,506) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
          - div.prow  @(31,531) 331x60
              display:flex; align-items:center; gap:9px; padding:0px 10px; background-color:rgb(22, 25, 17); border-radius:14px
            - span.ppos "6"  @(41,549.5) 24x23
                color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
            - span.av.a1 "EL"  @(74,543) 36x36
                display:grid; align-items:center; flex-shrink:0; background-color:rgb(74, 63, 30); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:14px; font-weight:700; line-height:17px; grid-template-columns:34px
            - div.pmid  @(119,544.5) 154.3x33
                flex-grow:1
              - p.pname "Elisa Costa"  @(119,544.5) 154.3x17
                  font-size:14px; font-weight:700; line-height:17px; overflow:hidden
              - p.pdet  @(119,561.5) 154.3x16
                  display:flex; align-items:center; gap:4px; color:rgb(143, 147, 132); font-size:11px; line-height:13px; overflow:hidden
                - svg.shield  @(119,561.5) 16x16
                    flex-shrink:0; overflow:hidden
                    svg: <svg class="shield" viewBox="0 0 16 16" aria-hidden="true"><path d="M8 1.3L14.4 3.5 14.1 8.8C13.5 12.3 10.4 14.4 8 15.4 5.6 14.4 2.5 12.3 1.9 8.8L1.6 3.5Z"></path><path class="chev" d="M4.6 6.2L8 8.1 11.4 6.2M4.6 8.9L8 10.8 11.4 8.9"></path></svg>
                - span "Desistiu"  @(139,563) 41.4x13
            - div.pend  @(282.3,543) 69.7x36
                text-align:right
              - p.pkm "3,10 km"  @(282.3,543) 69.7x23
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:19px; font-weight:700; line-height:23px
              - p.pmv "—"  @(282.3,566) 69.7x13
                  color:rgb(163, 181, 101); font-size:11px; line-height:13px
      - div.btns  @(16,731) 361x112
          display:flex; flex-direction:column; gap:8px
        - button.act "Ver resultado completo"  @(16,731) 361x52
            display:grid; align-items:center; flex-shrink:0; color:rgb(255, 255, 255); background-color:rgb(209, 43, 43); border:1px solid rgb(209, 43, 43); border-radius:28px; font-weight:700; line-height:19px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:359px
        - button.act.sec2 "Voltar às missões"  @(16,791) 361x52
            display:grid; align-items:center; flex-shrink:0; color:rgb(143, 147, 132); background-color:rgb(10, 11, 9); border:1px solid rgba(236, 232, 218, 0.1); border-radius:28px; font-weight:700; line-height:19px; text-align:center; transition:transform 0.1s ease-out; grid-template-columns:359px
