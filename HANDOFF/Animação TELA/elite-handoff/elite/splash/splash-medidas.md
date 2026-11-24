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
- Saira Stencil One  loaded=False
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`
- `@media (max-width: 430px)`

## Animations / transitions running or defined
- **out** on `div.sp-all`: 300ms, delay 2800ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in", "transform": "none", "opacity": "1"}
    - keyframe {"offset": 1, "easing": "ease-in", "transform": "scale(1.04)", "opacity": "0"}
- **glowIn** on `div.sp-glow`: 900ms, delay 0ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scale(0.6)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "none", "opacity": "0.55"}
- **breath** on `div.sp-glow`: 3200ms, delay 1200ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "opacity": "0.55"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "opacity": "0.85"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "opacity": "0.55"}
- **spineIn** on `path.sp-spine`: 450ms, delay 200ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "scaleY(0)"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "none"}
- **armIn** on `path.sp-top`: 400ms, delay 350ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateX(-18px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "none", "opacity": "1"}
- **armIn** on `path.sp-bot`: 400ms, delay 450ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "translateX(-18px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "transform": "none", "opacity": "1"}
- **redIn** on `path.sp-red`: 450ms, delay 750ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "scaleX(0)"}
    - keyframe {"offset": 0.7, "easing": "ease-out", "transform": "scaleX(1.12)"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "scaleX(1)"}
- **redFlash** on `path.sp-red`: 600ms, delay 750ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "fill": "rgb(255, 122, 110)"}
    - keyframe {"offset": 1, "easing": "ease-out", "fill": "rgb(209, 43, 43)"}
- **wmIn** on `div.sp-wm`: 700ms, delay 900ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "letterSpacing": "24px", "filter": "blur(6px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "cubic-bezier(0.2, 0.8, 0.2, 1)", "letterSpacing": "9.6px", "filter": "none", "opacity": "1"}
- **tgIn** on `div.sp-tg`: 500ms, delay 1350ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-out", "transform": "translateY(6px)", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "ease-out", "transform": "none", "opacity": "1"}
- **fadeIn** on `div.ld`: 400ms, delay 100ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "ease", "opacity": "1"}
- **load** on `div.track`: 2300ms, delay 200ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "linear"}
    - keyframe {"offset": 0.22, "easing": "linear"}
    - keyframe {"offset": 0.5, "easing": "linear"}
    - keyframe {"offset": 0.78, "easing": "linear"}
    - keyframe {"offset": 1, "easing": "linear"}
- **fadeIn** on `div.rwrap`: 300ms, delay 200ms, iterations 1, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease", "opacity": "0"}
    - keyframe {"offset": 1, "easing": "ease", "opacity": "1"}
- **bob** on `g.bob`: 300ms, delay 0ms, iterations infinite, easing `linear`, direction alternate
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "translateY(0px)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "translateY(-3px)"}
- **arA** on `g.arB`: 600ms, delay -300ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(40deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(-45deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(40deg)"}
- **frA** on `g.frB`: 600ms, delay -300ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(-60deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(-90deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(-60deg)"}
- **thA** on `g.thB`: 600ms, delay -300ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(-50deg)"}
    - keyframe {"offset": 0.25, "easing": "ease-in-out", "transform": "rotate(-10deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(35deg)"}
    - keyframe {"offset": 0.75, "easing": "ease-in-out", "transform": "rotate(-5deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(-50deg)"}
- **shA** on `g.shB`: 600ms, delay -300ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(20deg)"}
    - keyframe {"offset": 0.25, "easing": "ease-in-out", "transform": "rotate(15deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(40deg)"}
    - keyframe {"offset": 0.75, "easing": "ease-in-out", "transform": "rotate(110deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(20deg)"}
- **thA** on `g.thA`: 600ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(-50deg)"}
    - keyframe {"offset": 0.25, "easing": "ease-in-out", "transform": "rotate(-10deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(35deg)"}
    - keyframe {"offset": 0.75, "easing": "ease-in-out", "transform": "rotate(-5deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(-50deg)"}
- **shA** on `g.shA`: 600ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(20deg)"}
    - keyframe {"offset": 0.25, "easing": "ease-in-out", "transform": "rotate(15deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(40deg)"}
    - keyframe {"offset": 0.75, "easing": "ease-in-out", "transform": "rotate(110deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(20deg)"}
- **arA** on `g.arA`: 600ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(40deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(-45deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(40deg)"}
- **frA** on `g.frA`: 600ms, delay 0ms, iterations infinite, easing `linear`, direction normal
    - keyframe {"offset": 0, "easing": "ease-in-out", "transform": "rotate(-60deg)"}
    - keyframe {"offset": 0.5, "easing": "ease-in-out", "transform": "rotate(-90deg)"}
    - keyframe {"offset": 1, "easing": "ease-in-out", "transform": "rotate(-60deg)"}

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.splash.st.play  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; background-color:rgb(10, 11, 9); border:1px solid rgba(236, 232, 218, 0.1); overflow:hidden
  - div.sp-all  @(1,1) 391x849
      display:flex; position:absolute; flex-direction:column; justify-content:center; align-items:center; animation:0.3s ease-in 2.8s forwards out
    - div.sp-glow  @(-11.3,217.7) 415.6x415.6
        position:absolute; margin:-240px 0px 0px -240px; background-image:radial-gradient(circle, rgba(163, 181, 101, 0.18), rgba(163, 181, 101, 0) 65%); border-radius:50%; opacity:0.365537; transform:matrix(0.865845, 0, 0, 0.865845, 0, 0); animation:0.9s ease-out both glowIn, 3.2s ease-in-out 1.2s infinite breath
    - svg.sp-mark aria="Elite"  @(144.5,330) 104x104
        position:relative
        svg: <svg class="sp-mark" viewBox="0 0 64 64" aria-label="Elite"><g transform="translate(6.8 0) skewX(-12)"><path class="sp-spine" fill="#ECE8DA" d="M14 8H24.2V56H14Z"></path><path class="sp-top" fill="#ECE8DA" d="M23.6 8H50V17H23.6Z"></path><path class="sp-bot" fill="#ECE8DA" d="M23.6 47H50V56H23.6Z"></path><path class="sp-red" fill="#D12B2B" d="M23.6 27H44V36H23.6Z"></path></g></svg>
    - div.sp-wm "ELITE"  @(85.7,456) 231.2x40
        position:relative; margin:22px -9.6px 0px 0px; filter:blur(6px); opacity:0; font-family:"Saira Stencil One", "Barlow Condensed", Impact, sans-serif; font-size:40px; line-height:40px; letter-spacing:24px; animation:0.7s cubic-bezier(0.2, 0.8, 0.2, 1) 0.9s both wmIn
    - div.sp-tg "MISSÃO DADA É MISSÃO CUMPRIDA"  @(90.7,516) 211.6x11
        position:relative; margin:14px 0px 0px; color:rgb(143, 147, 132); opacity:0; font-size:9px; font-weight:600; letter-spacing:1.8px; transform:matrix(1, 0, 0, 1, 0, 6); animation:0.5s ease-out 1.35s both tgIn
    - div.ld  @(66.5,764) 260x2
        position:absolute; margin:0px 0px 0px -130px; opacity:0.98351; animation:0.4s 0.1s both fadeIn
      - div.track  @(66.5,764) 260x2
          position:relative; background-color:rgb(29, 33, 22); border-radius:1px; animation:2.3s linear 0.2s both load
        - i  @(66.5,764) 33.6x2
            background-color:rgb(163, 181, 101); border-radius:1px
        - div.rwrap  @(100.1,766) 0x0
            position:absolute; opacity:0.969476; animation:0.3s 0.2s both fadeIn
          - svg.rn aria="Corredor"  @(84.1,726) 30x40
              position:absolute
              svg: <svg class="rn" viewBox="40 10 100 135" aria-label="Corredor"><g class="bob">
<g class="arB"><line class="f" x1="112" y1="38" x2="112" y2="60" stroke-width="9"></line><g class="frB"><line class="f" x1="112" y1="60" x2="112" y2="80" stroke-width="7.5"></line></g></g>
<g class="thB"><line class="f" x1="94" y1="72" x2="94" y2="104" stroke-width="13"></line><g class="shB"><line class="f" x1="94" y1="104" x2="94" y2="134" stroke-width="9.5"></line><line x1="94" y1="134" x2="105" y2="136" stroke="#8E1F1F" stroke-width="7"></line></g></g>
<line class="n" x1="94" y1="72" x2="103" y2="55" stroke-width="14"></line><line class="n" x1="103" y1="55" x2="112" y2="38" stroke-width="18"></line><circle cx="121" cy="23" r="10" fill="#ECE8DA"></circle>
<g class="thA"><line class="n" x1="94" y1="72" x2="94" y2="104" stroke-width="14.5"></line><g class="shA"><line class="n" x1="94" y1="104" x2="94" y2="134" stroke-width="10.5"></line><line x1="94" y1="134" x2="105" y2="136" stroke="#D12B2B" stroke-width="8"></line></g></g>
<g class="arA"><line class="n" x1="112" y1="38" x2="112" y2="60" stroke-width="10"></line><g class="frA"><line class="n" x1="112" y1="60" x2="112" y2="80" stroke-width="8.5"></line></g></g>
</g></svg>
        - div.pct aria="Progresso"  @(66.5,780) 260x16
            position:absolute; color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", system-ui, sans-serif; font-size:13px; font-weight:600; letter-spacing:1.3px; text-align:center; font-variant-numeric:tabular-nums
          - div.pct::after  size autoxauto (pseudo; inset auto)
              content:counter(pp) "%"
