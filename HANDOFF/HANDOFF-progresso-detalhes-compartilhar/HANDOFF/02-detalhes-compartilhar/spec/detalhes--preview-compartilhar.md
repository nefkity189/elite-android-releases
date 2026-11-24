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
- Saira Stencil One  loaded=False
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- **share-enter** on `div.view`: 220ms, delay 0ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(16px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateY(0px)", "opacity": "1"}

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    position:relative; flex-shrink:0; background-color:rgb(10, 11, 9); overflow:hidden
  - div.view  [display:none]
  - div.view  @(0,0) 393x851
      display:flex; position:absolute; flex-direction:column; background-color:rgb(10, 11, 9); transform:matrix(1, 0, 0, 1, 0, 0); animation:0.22s cubic-bezier(0.2, 0.8, 0.2, 1) both share-enter
    - div.statusbar  @(0,0) 393x32
        flex-shrink:0
    - button.iconbtn aria="Voltar"  @(8,32) 48x48
        display:grid; position:absolute; align-items:center; flex-shrink:0; border-radius:24px; text-align:center; z-index:2; transition:transform 0.1s ease-out; grid-template-columns:48px
      - svg  @(20,44) 24x24
          overflow:hidden
          svg: <svg data-id="shr.back.icon" data-icon="material:AutoMirrored.Filled.ArrowBack" aria-hidden="true" viewBox="0 0 24 24"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"></path></svg>
    - div  @(0,32) 393x819
        display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; padding:20px 16px
      - p "COMPARTILHAR NO ELITE"  @(112.5,170) 167.9x16
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:16px; letter-spacing:1.32px
      - section  @(16,202) 361x451
          display:flex; position:relative; flex-direction:column; gap:12px; padding:20px; margin:16px 0px 0px; background-image:radial-gradient(circle, rgb(30, 35, 20), rgb(10, 11, 9)); border:1px solid rgb(93, 107, 52); border-radius:22px
        - div  @(37,223) 319x30
            display:flex; justify-content:space-between; align-items:center; flex-shrink:0
          - div  @(37,223) 105.6x30
              display:flex; align-items:center; gap:8px
            - svg  @(37,223) 26x30
                overflow:hidden
                svg: <svg data-id="shr.card.mark" data-icon="drawable:ic_elite_mark" aria-hidden="true" viewBox="0 0 26 30"><path d="M13 1 L25 8 V22 L13 29 L1 22 V8 Z M13 8 L19 11.5 V18.5 L13 22 L7 18.5 V11.5 Z" fill-rule="evenodd"></path></svg>
            - span "ELITE"  @(71,227) 71.6x22
                font-family:"Saira Stencil One", "Barlow Condensed", sans-serif; font-size:18px; line-height:22px; letter-spacing:4.32px
          - span "TREINO CONCLUÍDO"  @(223.3,228) 132.7x20
              padding:3px 8px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:6px; font-size:10px; font-weight:700; line-height:14px; letter-spacing:1px
        - div  @(37,265) 319x174
            position:relative; flex-grow:1
          - svg  @(37,265) 319x174
              position:absolute; overflow:hidden
              svg: <svg data-id="shr.card.route.svg" aria-hidden="true" viewBox="0 0 319 164" preserveAspectRatio="none">
          <polyline class="share-halo" points="30,120 60,86 110,62 170,42 230,50 280,80 292,120 260,148 200,156 140,150 80,156 44,138"></polyline>
          <polyline class="share-line" points="30,120 60,86 110,62 170,42 230,50 280,80 292,120 260,148 200,156 140,150 80,156 44,138"></polyline>
          <circle class="pin-start" cx="30" cy="120" r="6"></circle><circle class="pin-end" cx="44" cy="138" r="6"></circle>
        </svg>
        - div  @(37,451) 319x84
            display:flex; flex-direction:column; flex-shrink:0; gap:2px
          - p "DISTÂNCIA"  @(37,451) 319x14
              color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.32px
          - div  @(37,467) 319x68
              display:flex; align-items:flex-end
            - span "5,12"  @(37,467) 140.1x68
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:72px; font-weight:600; line-height:68px
            - span "km"  @(177.1,501) 36x34
                padding:0px 0px 8px 4px; color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:500; line-height:26px
        - div  @(37,547) 319x56
            display:flex; flex-shrink:0; gap:10px
          - div.share-metric  @(37,547) 154.5x56
              flex-grow:1
            - p.share-metric-label "TEMPO"  @(37,547) 154.5x14
                color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.32px
            - p.share-metric-value "28:41"  @(37,561) 154.5x42
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:600; line-height:42px
          - div.share-metric  @(201.5,547) 154.5x56
              flex-grow:1
            - p.share-metric-label "RITMO"  @(201.5,547) 154.5x14
                color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.32px
            - p.share-metric-value "5:36"  @(201.5,561) 154.5x42
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:30px; font-weight:600; line-height:42px
        - p "ELITE · MISSÃO DADA É MISSÃO CUMPRIDA"  @(37,620) 319x12
            flex-shrink:0; margin:5px 0px 0px; color:rgb(143, 147, 132); font-size:9px; font-weight:600; line-height:12px; letter-spacing:1.8px; text-align:center
      - button aria="Compartilhar"  @(16,669) 361x44
          display:flex; justify-content:center; align-items:center; gap:10px; margin:16px 0px 0px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:22px; font-size:14px; font-weight:700; line-height:20px; text-align:center; transition:transform 0.1s ease-out
        - svg  @(137.5,682) 18x18
            overflow:hidden
            svg: <svg data-id="shr.cta.icon" data-icon="material:Filled.Share" aria-hidden="true" viewBox="0 0 24 24"><path d="M18 16.08c-.76 0-1.44.3-1.96.77L8.91 12.7c.05-.23.09-.46.09-.7s-.04-.47-.09-.7l7.05-4.11c.54.5 1.25.81 2.04.81 1.66 0 3-1.34 3-3s-1.34-3-3-3-3 1.34-3 3c0 .24.04.47.09.7L8.04 9.81C7.5 9.31 6.79 9 6 9c-1.66 0-3 1.34-3 3s1.34 3 3 3c.79 0 1.5-.31 2.04-.81l7.12 4.16c-.05.21-.08.43-.08.65 0 1.61 1.31 2.92 2.92 2.92s2.92-1.31 2.92-2.92-1.31-2.92-2.92-2.92z"></path></svg>
        - span "Compartilhar"  @(165.5,681) 90x20
