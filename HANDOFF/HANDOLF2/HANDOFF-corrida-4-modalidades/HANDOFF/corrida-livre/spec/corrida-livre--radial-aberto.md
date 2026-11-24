# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--background`: #0A0B09
- `--glass`: rgba(16,18,13,.82)
- `--glass-elevated`: rgba(29,33,22,.92)
- `--line`: rgba(236,232,218,.10)
- `--text`: #ECE8DA
- `--muted`: #8F9384
- `--teal`: #A3B565
- `--teal-on`: #0E1306
- `--brass`: #C9A24B
- `--red`: #D12B2B
- `--red-on`: #FFF4F0
- `--item-off-bg`: #1D2116
- `--track`: #1D2116
- `--card-elevated`: #1D2116
- `--amber`: #E0A93A
- `--bottom-h`: 90px
- `--top-height`: 64px
- `--top-padding`: 16px
- `--chip-height`: 32px
- `--fab-size`: 48px
- `--locate-size`: 46px
- `--item-size`: 44px
- `--arc-size`: 132px
- `--fan-box`: 160px
- `--fan-radius`: 96px
- `--radial-box`: 160px
- `--radial-radius`: 80px
- `--timer-size`: 112px
- `--stats-row-height`: 64px
- `--action-pad`: 16px
- `--action-bottom-pad`: 18px
- `--button-height`: 56px
- `--button-radius`: 28px
- `--action-radius`: 26px
- `--font-display`: "Barlow Condensed","Arial Narrow",sans-serif
- `--font-body`: "Inter",system-ui,sans-serif
- `--ease`: cubic-bezier(.4,0,.2,1)
- `--map-bg`: #f2efe9
- `--map-road`: #ffffff
- `--map-road-edge`: #d9d3c4
- `--map-park`: #d4e6c3
- `--map-water`: #aad3df
- `--map-label`: #6b6a63

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (max-width: 480px)`
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main.phone.radial-open  @(0,0) 393x851
    position:relative; background-color:rgb(10, 11, 9); overflow:hidden
  - div.map  @(0,0) 393x851
      position:absolute; background-color:rgb(242, 239, 233); overflow:hidden
    - svg  @(0,0) 393x851
        position:absolute; overflow:hidden; transition:transform 0.5s cubic-bezier(0.4, 0, 0.2, 1)
        svg: <svg viewBox="0 0 393 851" preserveAspectRatio="none">
      <rect class="map-fill-park" x="-10" y="470" width="170" height="130" rx="18"></rect>
      <rect class="map-fill-park" x="260" y="610" width="150" height="110" rx="18"></rect>
      <path class="map-fill-water" d="M300 380 Q340 420 410 410 L410 330 Q350 340 300 380Z"></path>
      <path class="map-road-edge" stroke-width="16" d="M-10 520 L140 500 L260 530 L410 500"></path><path class="map-road" stroke-width="13" d="M-10 520 L140 500 L260 530 L410 500"></path>
      <path class="map-road-edge" stroke-width="14" d="M120 380 L150 520 L190 680 L210 860"></path><path class="map-road" stroke-width="11" d="M120 380 L150 520 L190 680 L210 860"></path>
      <path class="map-road-edge" stroke-width="12" d="M-10 640 L200 610 L410 650"></path><path class="map-road" stroke-width="9" d="M-10 640 L200 610 L410 650"></path>
      <path class="map-road-edge" stroke-width="12" d="M300 420 L290 560 L310 700 L330 860"></path><path class="map-road" stroke-width="9" d="M300 420 L290 560 L310 700 L330 860"></path>
      <path class="map-road" stroke-width="6" d="M-10 570 L260 585 L410 570"></path><path class="map-road" stroke-width="6" d="M60 460 L80 700 L100 860"></path>
      <text class="map-label" x="22" y="512">Rua das Acácias</text><text class="map-label" x="214" y="604">Av. Central</text><text class="map-label" x="30" y="632">Rua Ipê</text>
      <path class="route-casing" d="M196 690 L160 610 L150 560 L152 520 L190 508 L245 524">
  - div.shade  @(0,0) 393x851
      position:absolute; background-image:linear-gradient(rgba(10, 11, 9, 0.96) 0%, rgba(10, 11, 9, 0.94) 30%, rgba(10, 11, 9, 0.9) 43%, rgba(10, 11, 9, 0) 53%, rgba(10, 11, 9, 0) 76%, rgba(10, 11, 9, 0.85) 100%)
  - div.layer  @(0,0) 393x851
      display:flex; position:absolute; flex-direction:column; align-items:center; z-index:1
    - div.top  @(0,0) 393x64
        display:flex; justify-content:space-between; align-items:flex-start; flex-shrink:0; padding:16px 16px 0px
      - div.gps-chip "GPS OK"  @(16,24) 88.7x32
          display:flex; align-items:center; gap:7px; padding:0px 12px; margin:8px 0px 0px; color:rgb(143, 147, 132); background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:16px; font-size:13px; line-height:16px
        - i  @(29,36.5) 7x7
            background-color:rgb(163, 181, 101); border-radius:50%
    - section.hero  @(0,64) 393x222
        display:flex; flex-direction:column; align-items:center; flex-shrink:0; padding:14px 0px 0px
      - div.free  @(0,78) 393x208
          display:flex; flex-direction:column; align-items:center; gap:8px
        - div.timer-ring  @(140.5,78) 112x112
            position:relative; flex-shrink:0
          - svg  @(140.5,78) 112x112
              position:absolute; overflow:hidden
              svg: <svg width="112" height="112" viewBox="0 0 112 112" aria-hidden="true"><circle cx="56" cy="56" r="56" fill="rgba(16,18,13,.82)"></circle><circle cx="56" cy="56" r="52" fill="none" stroke="rgba(236,232,218,.10)" stroke-width="4"></circle><circle class="ring-progress" id="" cx="56" cy="56" r="52" fill="none" stroke-width="4" stroke-linecap="round" stroke-dasharray="326.73" stroke-dashoffset="326.73" transform="rotate(-90 56 56)" style="display:none"></circle></svg>
          - span#-text.ring-text "24:18"  @(140.5,78) 112x112
              display:flex; position:absolute; justify-content:center; align-items:center; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:32px; font-weight:600; line-height:32px; font-variant-numeric:tabular-nums
        - div.mode-label "Corrida livre"  @(155.2,198) 82.6x16
            color:rgb(163, 181, 101); font-size:11px; font-weight:600; line-height:16px; letter-spacing:1.32px
        - div.stats-row  @(0,222) 393x64
            display:flex; align-items:center
          - div.stat  @(0,225) 196x58
              display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; padding:0px 28px
            - div.value  @(42.6,225) 110.8x40
                display:flex; align-items:flex-end; gap:4px
              - span.num "3,42"  @(42.6,225) 77.9x40
                  font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:40px; font-weight:600; line-height:40px; font-variant-numeric:tabular-nums
              - span.unit "km"  @(124.5,241) 28.9x24
                  color:rgb(143, 147, 132); font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:24px
            - div.lb "Distância"  @(67.3,267) 61.3x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:16px; letter-spacing:1.32px
          - div.divider  @(196,237) 1x34
              background-color:rgba(236, 232, 218, 0.1)
          - div.stat  @(197,225) 196x58
              display:flex; flex-direction:column; justify-content:center; align-items:center; flex-grow:1; gap:2px; padding:0px 28px
            - span.num "5:48"  @(255,225) 80.1x40
                font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:40px; font-weight:600; line-height:40px; font-variant-numeric:tabular-nums
            - div.lb "Ritmo (min/km)"  @(245,267) 100x16
                color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:16px; letter-spacing:1.32px
    - div.spacer  @(0,286) 393x475
        position:relative; flex-grow:1
      - div#pill.follow-pill "Seguindo você"  @(16,717) 117.7x30
          position:absolute; padding:7px 13px; background-color:rgba(16, 18, 13, 0.82); border-radius:15px; box-shadow:rgba(236, 232, 218, 0.1) 0px 0px 0px 1px inset; font-size:13px; line-height:16px
    - div#bottom-block.bottom-block  @(0,761) 393x90
        flex-shrink:0
      - nav.action-bar  @(0,761) 393x90
          display:flex; flex-shrink:0; gap:12px; padding:16px 16px 18px; background-color:rgba(16, 18, 13, 0.82); border-radius:26px 26px 0px 0px; box-shadow:rgba(236, 232, 218, 0.1) 0px 0px 0px 1px inset
        - button#btn-pause.btn-pause aria="Pausar"  @(16,777) 174.5x56
            display:flex; position:relative; justify-content:center; align-items:center; flex-grow:1; gap:8px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-weight:700; line-height:22px; text-align:center; transition:transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)
          - svg.ico-pause  @(63.2,796) 18x18
              overflow:hidden
              svg: <svg class="ico-pause" width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><path d="M6 4h4v16H6z M14 4h4v16h-4z"></path></svg>
          - svg.ico-play  [display:none]
          - span#pause-label "Pausar"  @(89.2,794) 54.2x22
        - button#btn-stop.btn-stop aria="Parar"  @(202.5,777) 174.5x56
            display:flex; position:relative; justify-content:center; align-items:center; flex-grow:1; gap:8px; color:rgb(255, 244, 240); background-color:rgb(209, 43, 43); border-radius:28px; box-shadow:rgba(209, 43, 43, 0.3) 0px 0px 0px 1px inset; font-weight:700; line-height:22px; text-align:center
          - svg.hold-ring  [display:none]
          - span.inner  @(256,794) 67.6x22
              display:flex; position:relative; align-items:center; gap:8px
            - svg  @(256,796) 18x18
                overflow:hidden
                svg: <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor"><rect x="5" y="5" width="14" height="14" rx="1"></rect></svg>
            - span#stop-label "Parar"  @(282,794) 41.6x22
  - div#scrim.scrim  @(0,0) 393x851
      position:absolute; z-index:2
  - div.radial-menu  @(217,16) 160x160
      position:absolute; z-index:3
    - div.radial-arc  @(221,40) 132x132
        position:absolute; background-color:rgba(29, 33, 22, 0.92); border:1px solid rgba(236, 232, 218, 0.1); border-radius:0px 0px 0px 132px; transform:matrix(1, 0, 0, 1, 0, 0); transition:transform 0.25s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.25s cubic-bezier(0.4, 0, 0.2, 1)
    - button#radial-sound.radial-item.on aria="Som"  @(251,18) 44x44
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; transform:matrix(1, 0, 0, 1, -80, 0); transition:transform 0.28s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.28s cubic-bezier(0.4, 0, 0.2, 1), visibility
      - svg  @(262.5,29.5) 21x21
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 5 6 9H2v6h4l5 4V5z"></path><path d="M15.5 8.5a5 5 0 0 1 0 7M19 5a10 10 0 0 1 0 14"></path></svg>
    - button#radial-vibration.radial-item.on aria="Vibração"  @(274.4,74.6) 44x44
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; transform:matrix(1, 0, 0, 1, -56.57, 56.57); transition:transform 0.28s cubic-bezier(0.4, 0, 0.2, 1) 0.05s, opacity 0.28s cubic-bezier(0.4, 0, 0.2, 1) 0.05s, visibility
      - svg  @(285.9,86.1) 21x21
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="8" y="3" width="8" height="18" rx="2"></rect><path d="M4 8v8M20 8v8"></path></svg>
    - button#radial-gps.radial-item.on aria="Manter GPS ativo"  @(331,98) 44x44
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; transform:matrix(1, 0, 0, 1, 0, 80); transition:transform 0.28s cubic-bezier(0.4, 0, 0.2, 1) 0.1s, opacity 0.28s cubic-bezier(0.4, 0, 0.2, 1) 0.1s, visibility
      - svg  @(342.5,109.5) 21x21
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><circle cx="12" cy="12" r="3"></circle><circle cx="12" cy="12" r="8"></circle><path d="M12 2v3M12 19v3M2 12h3M19 12h3"></path></svg>
    - button#radial-anchor.radial-anchor aria="Configurações da corrida"  @(329,16) 48x48
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center; transform:matrix(0, 1, -1, 0, 0, 0); transition:transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)
      - svg  @(342.5,29.5) 21x21
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.39a2 2 0 0 0-.73-2.73l-.15-.08a2 2 0 0 1-1-1.74v-.5a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"></path><circle cx="12" cy="12" r="3"></circle></svg>
  - div.map-style-fan  @(217,591) 160x160
      position:absolute; z-index:3
    - div.fan-arc  @(327.6,701.6) 26.4x26.4
        position:absolute; background-color:rgba(29, 33, 22, 0.92); border:1px solid rgba(236, 232, 218, 0.1); border-radius:132px 0px 0px; opacity:0; transform:matrix(0.2, 0, 0, 0.2, 0, 0); transition:transform 0.25s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.25s cubic-bezier(0.4, 0, 0.2, 1)
    - button#fan-recenter.fan-item.action aria="Centralizar no mapa"  @(343,717) 22x22
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(163, 181, 101); background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; opacity:0; text-align:center; transform:matrix(0.5, 0, 0, 0.5, 0, 0); transition:transform 0.28s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.28s cubic-bezier(0.4, 0, 0.2, 1), visibility 0s linear 0.28s
      - svg  @(348.8,722.8) 10.5x10.5
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"><circle cx="12" cy="12" r="3"></circle><circle cx="12" cy="12" r="8"></circle><path d="M12 2v3M12 19v3M2 12h3M19 12h3"></path></svg>
    - button#fan-dark.fan-item aria="Modo escuro"  @(343,717) 22x22
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(143, 147, 132); background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; opacity:0; text-align:center; transform:matrix(0.5, 0, 0, 0.5, 0, 0); transition:transform 0.28s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.28s cubic-bezier(0.4, 0, 0.2, 1), visibility 0s linear 0.28s
      - svg  @(348.8,722.8) 10.5x10.5
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path></svg>
    - button#fan-type.fan-item.action aria="Tipo de mapa"  @(343,717) 22x22
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(163, 181, 101); background-color:rgb(29, 33, 22); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; opacity:0; text-align:center; transform:matrix(0.5, 0, 0, 0.5, 0, 0); transition:transform 0.28s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.28s cubic-bezier(0.4, 0, 0.2, 1), visibility 0s linear 0.28s
      - svg  @(348.8,722.8) 10.5x10.5
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 6v16l7-4 8 4 7-4V2l-7 4-8-4-7 4z"></path><path d="M8 2v16M16 6v16"></path></svg>
    - button#fan-labels.fan-item.on aria="Nomes de ruas"  @(343,717) 22x22
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; opacity:0; text-align:center; transform:matrix(0.5, 0, 0, 0.5, 0, 0); transition:transform 0.28s cubic-bezier(0.4, 0, 0.2, 1), opacity 0.28s cubic-bezier(0.4, 0, 0.2, 1), visibility 0s linear 0.28s
      - svg  @(348.8,722.8) 10.5x10.5
          overflow:hidden
          svg: <svg width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 7V4h16v3M9 20h6M12 4v16"></path></svg>
    - button#fab.fan-anchor aria="Opções do mapa"  @(331,705) 46x46
        display:flex; position:absolute; justify-content:center; align-items:center; color:rgb(163, 181, 101); background-color:rgba(16, 18, 13, 0.82); border:1px solid rgba(236, 232, 218, 0.1); border-radius:50%; text-align:center
      - svg.ic-layers  @(343.5,717.5) 21x21
          overflow:hidden
          svg: <svg class="ic-layers" width="21" height="21" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2 2 7l10 5 10-5-10-5z"></path><path d="M2 17l10 5 10-5"></path><path d="M2 12l10 5 10-5"></path></svg>
      - svg.ic-close  [display:none]
