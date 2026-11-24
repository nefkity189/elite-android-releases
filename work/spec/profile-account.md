# Layout spec (dp = CSS px)

Screen: 393 x 851 dp

## Design tokens (:root)
- `--page`: #050604
- `--bg`: #0A0B09
- `--surface`: #10120D
- `--card`: #161911
- `--card-el`: #1D2116
- `--field`: #0D0F0A
- `--line`: rgba(236,232,218,.10)
- `--line-strong`: rgba(236,232,218,.18)
- `--text`: #ECE8DA
- `--muted`: #8F9384
- `--dim`: #5E6255
- `--olive`: #A3B565
- `--olive-dark`: #5D6B34
- `--olive-soft`: rgba(163,181,101,.14)
- `--on-olive`: #0E1306
- `--red`: #D12B2B
- `--red-soft`: rgba(209,43,43,.15)
- `--red-line`: rgba(209,43,43,.35)
- `--on-red`: #FFF4F0
- `--brass`: #C9A24B
- `--amber`: #E0A93A
- `--amber-soft`: rgba(224,169,58,.14)
- `--amber-notice`: rgba(224,169,58,.33)
- `--ok`: #7FB069
- `--ok-soft`: rgba(127,176,105,.15)
- `--scrim`: rgba(0,0,0,.62)
- `--av1`: #3B4426
- `--hdr-core`: #262B1A
- `--num`: "Barlow Condensed","Arial Narrow",sans-serif
- `--ui`: "Inter",sans-serif

## Fonts used  (loaded=false means the browser fell back — install the real font files in the native app!)
- Inter  loaded=True
- Barlow Condensed  loaded=False

## Media queries (responsive rules to port)
- `@media (prefers-reduced-motion: reduce)`

## Animations / transitions running or defined
- none active in this state

## Element tree (indent = nesting; x,y relative to screen)
- main  @(0,0) 393x851
    display:flex; position:relative; flex-direction:column; flex-shrink:0; padding:28px 0px 0px; background-color:rgb(10, 11, 9); overflow:hidden
  - div  @(0,28) 393x749
      display:flex; flex-direction:column; flex-grow:1; gap:12px; padding:0px 16px 16px; overflow:hidden
    - header  @(16,28) 361x44
        display:flex; align-items:center; flex-shrink:0
      - h1 "Perfil"  @(16,38) 64.6x24
          font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:24px; font-weight:700; line-height:24px; letter-spacing:0.96px; text-transform:uppercase
    - section.card  @(16,84) 361x94
        display:flex; align-items:center; flex-shrink:0; gap:12px; padding:14px; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div  @(31,99) 64x64
          display:grid; align-items:center; flex-shrink:0; background-image:radial-gradient(circle, rgb(38, 43, 26), rgb(10, 11, 9)); border:2px solid rgb(93, 107, 52); border-radius:20px; grid-template-columns:60px
        - svg  @(45,113) 36x36
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="profile.header.mark" aria-hidden="true" width="36" height="36" viewBox="0 0 64 64"><polygon points="14,50 30,10 44,10 28,50" fill="var(--olive)"></polygon><polygon points="34,50 40,34 50,34 44,50" fill="var(--red)"></polygon></svg>
      - div  @(107,113.5) 199x35
          flex-grow:1
        - p "Recruta"  @(107,113.5) 199x23
            font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; overflow:hidden
        - p "MISSÃO DADA É MISSÃO CUMPRIDA"  @(107,138.5) 199x10
            margin:2px 0px 0px; color:rgb(143, 147, 132); font-size:8px; font-weight:600; line-height:10px; letter-spacing:1.04px; overflow:hidden
      - button aria="Editar nome"  @(318,109) 44x44
          display:grid; align-items:center; flex-shrink:0; color:rgb(143, 147, 132); border:1px solid rgba(236, 232, 218, 0.1); border-radius:22px; font-size:13.3333px; text-align:center; grid-template-columns:42px
        - svg.ic  @(330,121) 20x20
            flex-shrink:0; overflow:hidden
            svg: <svg data-id="profile.header.edit.icon" aria-hidden="true" class="ic" viewBox="0 0 24 24"><path d="M4 20h4L19 9l-4-4L4 16z"></path></svg>
    - p.eyebrow "Conta"  @(16,190) 361x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
    - section.card  @(16,216) 361x310.8
        flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px; overflow:hidden
      - div.pad  @(17,217) 359x308.8
          display:flex; flex-direction:column; gap:14px; padding:16px
        - div.row  @(33,233) 327x44
            display:flex; align-items:center; gap:12px
          - div.tile  @(33,233) 44x44
              display:grid; align-items:center; flex-shrink:0; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border-radius:14px; grid-template-columns:44px
            - svg.ic  @(45,245) 20x20
                flex-shrink:0; overflow:hidden
                svg: <svg data-id="acct.out.tile.icon" class="ic" viewBox="0 0 24 24" data-icon="material:Person"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
          - h2.t2 "Entre para competir"  @(89,243.5) 271x23
              flex-grow:1; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:20px; font-weight:600; line-height:23px; letter-spacing:0.8px; text-transform:uppercase
        - p.body "Com a conta Google você entra nas missões online, aparece no ranking e guarda seu progresso na nuvem."  @(33,291) 327x58.8
            color:rgb(143, 147, 132); font-size:14px; line-height:19.6px
        - div.chips  @(33,363.8) 327x30
            display:flex; gap:8px
          - span.chip "Missões online"  @(33,363.8) 111.6x30
              padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12px; font-weight:500; line-height:30px
          - span.chip "Ranking"  @(152.6,363.8) 72.1x30
              padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12px; font-weight:500; line-height:30px
          - span.chip "Conquistas"  @(232.8,363.8) 90.8x30
              padding:0px 12px; color:rgb(163, 181, 101); background-color:rgba(163, 181, 101, 0.14); border:1px solid rgb(93, 107, 52); border-radius:15px; font-size:12px; font-weight:500; line-height:30px
        - div.notice  [display:none]
        - button.btn.btn-olive "Entrar com Google"  @(33,407.8) 327x56
            display:flex; justify-content:center; align-items:center; flex-shrink:0; gap:10px; color:rgb(14, 19, 6); background-color:rgb(163, 181, 101); border-radius:28px; font-weight:600; line-height:20px; text-align:center; transition:transform 0.12s ease-out
          - svg.ic  @(109.7,425.8) 20x20
              flex-shrink:0; overflow:hidden
              svg: <svg data-id="acct.out.google.icon" aria-hidden="true" class="ic" viewBox="0 0 24 24" style="stroke-width:2.5"><path d="M20 12h-8"></path><path d="M19 7a8 8 0 1 0 1 5"></path></svg>
        - button.btn.btn-off  [display:none]
        - button.btn.btn-olive  [display:none]
        - p.cap "Você pode usar o Elite sem conta. Seus treinos continuam neste aparelho."  @(33,477.8) 327x32
            color:rgb(143, 147, 132); font-size:12px; line-height:16px; text-align:center
      - div  [display:none]
    - p.eyebrow "Durante a corrida"  @(16,538.8) 361x14
        flex-shrink:0; color:rgb(143, 147, 132); font-size:11px; font-weight:600; line-height:14px; letter-spacing:1.3px; text-transform:uppercase
    - section.card  @(16,564.8) 361x87
        flex-shrink:0; background-color:rgb(22, 25, 17); border:1px solid rgba(236, 232, 218, 0.1); border-radius:18px
      - div.set  @(17,565.8) 359x85
          display:flex; align-items:center; gap:12px; padding:14px
        - svg.ic  @(31,598.3) 20x20
            flex-shrink:0; color:rgb(143, 147, 132); overflow:hidden
            svg: <svg data-id="run.voice.icon" aria-hidden="true" class="ic" viewBox="0 0 24 24" style="color:var(--muted)"><path d="M4 10v4M8 7v10M12 4v16M16 8v8M20 11v2"></path></svg>
        - div  @(63,579.8) 241x57
            flex-grow:1
          - b "Voz do treinador"  @(63,579.8) 241x17
              font-size:14px; font-weight:700; line-height:17px
          - span.cap "Instruções sonoras para início, trocas de passo e ritmo"  @(63,600.8) 230.5x35
              color:rgb(143, 147, 132); font-size:12px; line-height:16px
        - div.sw  @(316,594.3) 46x28
            position:relative; flex-shrink:0; background-color:rgb(93, 107, 52); border-radius:14px
          - i  @(337,597.3) 22x22
              position:absolute; background-color:rgb(236, 232, 218); border-radius:11px
  - nav  @(12,777) 369x64
      display:grid; align-items:center; flex-shrink:0; padding:0px 6px; margin:0px 12px 10px; background-color:rgb(16, 18, 13); border:1px solid rgba(236, 232, 218, 0.18); border-radius:24px; grid-template-columns:71px 71px 71px 71px 71px
    - div.nv "Hoje"  @(19,778) 71x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:14px
      - svg.ic  @(43.5,790) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="profile.nav.today.icon" aria-hidden="true" class="ic" viewBox="0 0 24 24"><path d="M4 11l8-7 8 7v9H4z"></path></svg>
    - div.nv "Plano"  @(90,778) 71x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:14px
      - svg.ic  @(114.5,790) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="profile.nav.plan.icon" aria-hidden="true" class="ic" viewBox="0 0 24 24"><rect x="4" y="5" width="16" height="15" rx="2"></rect><path d="M4 10h16M9 3v4M15 3v4"></path></svg>
    - div aria="Assistente"  @(172.5,785) 48x48
        display:grid; padding:2px; background-image:conic-gradient(rgb(163, 181, 101), rgb(209, 43, 43), rgb(201, 162, 75), rgb(163, 181, 101)); border-radius:16px; grid-template-columns:44px
      - div "E"  @(174.5,787) 44x44
          display:grid; align-items:center; color:rgb(163, 181, 101); background-color:rgb(10, 11, 9); border-radius:14px; font-family:"Barlow Condensed", "Arial Narrow", sans-serif; font-size:25px; font-weight:700; line-height:25px; grid-template-columns:44px
    - div.nv "Progresso"  @(232,778) 71x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(143, 147, 132); font-size:11px; font-weight:500; line-height:14px
      - svg.ic  @(256.5,790) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="profile.nav.progress.icon" aria-hidden="true" class="ic" viewBox="0 0 24 24"><path d="M3 17l6-6 4 4 8-8"></path></svg>
    - div.nv.on "Perfil"  @(303,778) 71x62
        display:flex; flex-direction:column; justify-content:center; align-items:center; gap:2px; color:rgb(163, 181, 101); font-size:11px; font-weight:500; line-height:14px
      - svg.ic  @(327.5,790) 22x22
          flex-shrink:0; overflow:hidden
          svg: <svg data-id="profile.nav.profile.icon" aria-hidden="true" class="ic" viewBox="0 0 24 24"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg>
  - div  [display:none]
  - div  [display:none]
  - div  [display:none]
