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
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
- **spin** on `button.cb`: 5000ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **glow** on `button.cb`: 3200ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0.35) 0px 0px 16px 2px"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "boxShadow": "rgba(163, 181, 101, 0) 0px 0px 16px 2px"}

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.st  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); background-image:repeating-linear-gradient(135deg, rgba(236, 232, 218, 0.016) 0px, rgba(236, 232, 218, 0.016) 1px, rgba(0, 0, 0, 0) 1px, rgba(0, 0, 0, 0) 8px); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - div.scr.sc  @(1,1) 391x849
      display:flex; flex-direction:column; flex-grow:1; gap:14px; padding:22px 16px 24px
    - div.top  @(17,23) 359x48
        display:flex; align-items:center; flex-shrink:0; gap:10px
      - button.ib  @(17,25) 44x44
          display:grid; align-items:center; flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; grid-template-columns:42px
        - svg.ic  @(28,36) 22x22
            flex-shrink:0; overflow:hidden
            svg: <svg class="ic"><use href="#i-back"></use></svg>
      - h2 "Preciso de uma pausa"  @(71,23) 305x48
          flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:24px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
    - div.card.rw  @(17,85) 359x63.6
        display:flex; position:relative; align-items:center; flex-shrink:0; gap:12px; padding:14px; color:rgb(224, 169, 58); background-color:rgb(22, 25, 17); border:1px solid rgba(224, 169, 58, 0.4); border-radius:18px
      - svg.ic  @(32,105.8) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-shield"></use></svg>
      - span.sm "Se estiver doente ou com dor, não treine e procure um profissional de saúde."  @(66,100) 295x33.6
          color:rgb(236, 232, 218); font-size:12px; line-height:16.8px
    - span.lb "Período da pausa (1 a 14 dias)"  @(17,162.6) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - button.card.rw.b  @(17,190.6) 359x66.4
        display:flex; position:relative; justify-content:space-between; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(32,205.6) 108x36.4
        - b "23 nov a 29 nov"  @(32,206.6) 108x17
            font-weight:700
        - div.mu.sm "7 dias"  @(32,225.2) 108x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - svg.ic  @(339,212.8) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg class="ic"><use href="#i-cal"></use></svg>
    - span.lb "Nível durante a pausa"  @(17,271) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.cl  @(17,299) 359x215.2
        display:flex; flex-direction:column; flex-shrink:0; gap:8px
      - button.card  @(17,299) 359x66.4
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - b "Mantém os treinos"  @(32,315) 126.8x17
            font-weight:700
        - div.mu.sm "Usa a etapa anterior (mínimo etapa 1)"  @(32,333.6) 329x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - button.card.sel  @(17,373.4) 359x66.4
          position:relative; padding:14px; background-image:linear-gradient(rgba(163, 181, 101, 0.14), rgb(22, 25, 17)); border:1px solid rgb(163, 181, 101); border-radius:18px
        - button.card.sel::before  size 3pxx36.3906px (pseudo; inset 14px 355px 14px -1px)
            position:absolute; background-color:rgb(163, 181, 101); border-radius:0px 3px 3px 0px
            content:""
        - b "Só caminhada"  @(32,389.4) 96.7x17
            font-weight:700
        - div.mu.sm "Caminhada leve de 20 minutos com um passo só"  @(32,408) 329x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
      - button.card  @(17,447.8) 359x66.4
          position:relative; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
        - b "Sem treino"  @(32,463.8) 73.5x17
            font-weight:700
        - div.mu.sm "Treinos planejados viram pausa"  @(32,482.4) 329x16.8
            color:rgb(143, 147, 132); font-size:12px; line-height:16.8px
    - span.lb "Ritmo de retorno"  @(17,528.2) 359x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
    - div.seg  @(17,556.2) 359x44
        display:flex; flex-shrink:0; gap:6px
      - div.s "Cauteloso"  @(17,556.2) 115.7x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:113.656px
      - div.s.on "Proporcional"  @(138.7,556.2) 115.7x44
          display:grid; align-items:center; flex-grow:1; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(0, 0, 0, 0); border-radius:14px; font-weight:600; grid-template-columns:113.672px
      - div.s "Rápido"  @(260.3,556.2) 115.7x44
          display:grid; align-items:center; flex-grow:1; color:rgb(143, 147, 132); background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:14px; font-weight:600; grid-template-columns:113.672px
    - div.card  @(17,614.2) 359x106
        position:relative; flex-shrink:0; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - span.lb.ol "Prévia da retomada"  @(32,632.2) 147.7x14
          color:rgb(163, 181, 101); font-size:11px; font-weight:600; letter-spacing:1.32px; text-transform:uppercase
      - div.sm "• Recuo: 1 etapa • Etapa de retomada: Etapa 2 • Treinos afetados: 2"  @(32,654.8) 329x50.4
          margin:6px 0px 0px; font-size:12px; line-height:16.8px
        - br  @(124.8,654.8) 0x15
        - br  @(198.7,671.5) 0x15
    - div.sp  @(17,734.1) 359x21.9
        flex-grow:1
    - button.btn.pri.stk "Confirmar pausa"  @(17,770) 359x56
        display:flex; position:sticky; justify-content:center; align-items:center; flex-shrink:0; gap:10px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; box-shadow:rgb(10, 11, 9) 0px -14px 14px -6px; font-size:16px; font-weight:700; letter-spacing:0.32px; text-align:center; transition:transform 0.1s
