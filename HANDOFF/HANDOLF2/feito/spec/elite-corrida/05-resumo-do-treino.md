# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--bg`: #0A0B09
- `--s1`: #10120D
- `--card`: #161911
- `--card2`: #1D2116
- `--field`: #0D0F0A
- `--page`: #050604
- `--line`: rgba(236,232,218,.10)
- `--line2`: rgba(236,232,218,.18)
- `--text`: #ECE8DA
- `--mut`: #8F9384
- `--dim`: #5E6255
- `--olive`: #A3B565
- `--olive-d`: #5D6B34
- `--olive-s`: rgba(163,181,101,.14)
- `--on-olive`: #0E1306
- `--red`: #D12B2B
- `--red-s`: rgba(209,43,43,.15)
- `--on-red`: #FFF4F0
- `--brass`: #C9A24B
- `--amber`: #E0A93A
- `--ok`: #7FB069
- `--num`: "Barlow Condensed","Arial Narrow",system-ui,sans-serif
- `--ui`: "Inter",system-ui,sans-serif
- `--sten`: "Saira Stencil One","Barlow Condensed",Impact,sans-serif

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`
- `@media (max-width: 430px)`

## Animations / transitions running or defined
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}
- **pl** on `circle.pulse`: 2000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0.5"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scale(2.2)", "opacity": "0"}

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - div.scr.sc  @(1,1) 391x849
      display:flex; flex-direction:column; flex-grow:1; gap:14px; padding:22px 16px 24px
    - div.top  @(17,23) 359x44
        display:flex; align-items:center; flex-shrink:0; gap:10px
      - button.ib  @(17,23) 44x44
          display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:42px
        - svg.ic  @(28,34) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-back"></use></svg>
      - h2 "Detalhes da corrida"  @(71,33) 305x24
          flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:24px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
    - div.cl  @(17,81) 359x49.5
        display:flex; flex-direction:column; flex-shrink:0; gap:4px
      - span.lb.ol "Treino 1 · 19 de novembro"  @(17,81) 359x14
          color:rgb(163, 181, 101); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - h1.t1 "Treino concluído!"  @(17,99) 359x31.5
          font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:30px; font-weight:700; line-height:31.5px; letter-spacing:0.9px; text-transform:uppercase
    - div.card  @(17,144.5) 359x150
        position:relative; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; overflow:hidden
      - svg aria="Trajeto percorrido"  @(18,145.5) 357x150
          overflow:hidden
          svg: <svg viewBox="0 0 361 150" width="100%" height="150" aria-label="Trajeto percorrido"><rect width="361" height="150" fill="#0C0E09"></rect><path d="M0 110Q80 90 160 105T361 70M120 0C130 60 110 100 150 150" fill="none" stroke="#1C2015" stroke-width="8"></path><path d="M40 118C70 80 110 112 150 74S230 40 270 68 320 40 330 30" fill="none" stroke="#A3B565" stroke-width="4" stroke-linecap="round"></path><circle cx="40" cy="118" r="6" fill="#A3B565" stroke="#0A0B09" stroke-width="2"></circle><circle cx="330" cy="30" r="6" fill="#D12B2B" stroke="#0A0B09" stroke-width="2"></circle></svg>
    - div.card.stat3  @(17,308.5) 359x77.6
        display:grid; position:relative; flex-shrink:0; gap:8px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; text-align:center; grid-template-columns:104.328px 104.328px 104.344px
      - div  @(32,323.5) 104.3x47.6
        - b "2,40"  @(32,323.5) 104.3x28
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:28px; font-weight:600; line-height:28px
        - span "km"  @(76.3,354.5) 15.7x14
            color:rgb(143, 147, 132); font-size:11px; line-height:15.4px
      - div  @(144.3,323.5) 104.3x47.6
        - b "28:12"  @(144.3,323.5) 104.3x28
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:28px; font-weight:600; line-height:28px
        - span "Tempo"  @(178.7,354.5) 35.6x14
            color:rgb(143, 147, 132); font-size:11px; line-height:15.4px
      - div  @(256.7,323.5) 104.3x47.6
        - b "6:05"  @(256.7,323.5) 104.3x28
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:28px; font-weight:600; line-height:28px
        - span "Ritmo médio"  @(276.5,354.5) 64.7x14
            color:rgb(143, 147, 132); font-size:11px; line-height:15.4px
    - div.card  @(17,400.1) 359x142
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.lb "Ritmo ao longo do treino"  @(32,418.1) 191.6x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.mu.sm "Janela móvel de 30 s filtrada"  @(32,434.7) 329x16.8
          color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - svg aria="Gráfico de ritmo"  @(32,451.5) 329x70
          overflow:hidden
          svg: <svg viewBox="0 0 320 70" width="100%" height="70" aria-label="Gráfico de ritmo"><defs><linearGradient id="pg" x1="0" y1="0" x2="0" y2="1"><stop offset="0" stop-color="#A3B565" stop-opacity=".35"></stop><stop offset="1" stop-color="#A3B565" stop-opacity="0"></stop></linearGradient></defs><path d="M0 44C20 40 30 22 55 26S95 52 120 40 160 14 190 24 240 50 265 34 305 20 320 26V70H0Z" fill="url(#pg)"></path><path d="M0 44C20 40 30 22 55 26S95 52 120 40 160 14 190 24 240 50 265 34 305 20 320 26" fill="none" stroke="#A3B565" stroke-width="2.5" stroke-linecap="round"></path></svg>
    - div.card  @(17,556.1) 359x119.6
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.lb.br "Seus recordes"  @(32,574.1) 108.3x14
          color:rgb(201, 162, 75); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.rw.b  @(32,598.7) 329x28
          display:flex; justify-content:space-between; align-items:center; gap:12px; margin:8px 0px 0px
        - span "Maior distância"  @(32,602.9) 100.9x19.6
        - b.num "2,40 km"  @(282,598.7) 79x28
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
      - div.rw.b  @(32,632.7) 329x28
          display:flex; justify-content:space-between; align-items:center; gap:12px; margin:6px 0px 0px
        - span "Maior duração"  @(32,636.9) 95.9x19.6
        - b.num "28:12"  @(303.9,632.7) 57.1x28
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:20px; font-weight:600; line-height:28px; font-variant-numeric:tabular-nums
    - div.card  @(17,689.7) 359x139.2
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.lb "Parciais por quilômetro"  @(32,707.7) 183.1x14
          color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.rw.b  @(32,732.3) 329x19.6
          display:flex; justify-content:space-between; align-items:center; gap:12px; margin:8px 0px 0px
        - span "Km 1"  @(32,732.3) 31.3x19.6
        - b.num "6:10 /km"  @(300.4,732.3) 60.6x19.6
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-weight:600; font-variant-numeric:tabular-nums
      - div.rw.b  @(32,751.9) 329x19.6
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - span "Km 2"  @(32,751.9) 34.2x19.6
        - b.num.ol "5:58 /km"  @(300.4,751.9) 60.6x19.6
            color:rgb(163, 181, 101); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-weight:600; font-variant-numeric:tabular-nums
      - div.rw.b  @(32,771.5) 329x19.6
          display:flex; justify-content:space-between; align-items:center; gap:12px
        - span "Km 3 (parcial)"  @(32,771.5) 92.7x19.6
        - b.num "6:02 /km"  @(300.4,771.5) 60.6x19.6
            font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-weight:600; font-variant-numeric:tabular-nums
      - div.mu.sm "Mais rápido: 5:58 /km · Mais lento: 6:10 /km"  @(32,797) 329x16.8
          margin:6px 0px 0px; color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - span.lb "Anotações particulares"  @(17,842.8) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.fld "Ex: Bom ritmo no trecho intermediário…"  @(17,870.8) 359x72
        display:flex; align-items:flex-start; flex-shrink:0; padding:12px 14px 0px; color:rgb(143, 147, 132); background-color:rgb(13, 15, 10); border:1px solid rgba(236, 232, 218, 0.18); border-radius:14px
    - button.btn.gh.sm2 "Compartilhar no Elite"  @(17,956.8) 359x44
        display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; border:1px solid rgba(236, 232, 218, 0.18); border-radius:22px; font-weight:700; letter-spacing:0.28px; text-align:center; transition:transform 0.1s
      - svg.ic  @(107.9,969.8) 18x18
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-share"></use></svg>
    - button.btn.pri.stk "Salvar e concluir treino"  @(17,770) 359x56
        display:flex; position:sticky; justify-content:center; align-items:center; flex-shrink:0; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; box-shadow:rgb(10, 11, 9) 0px -14px 14px -6px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
