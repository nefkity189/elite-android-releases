# SPEC — Corrida livre (mapa visível)   (escrito pelo Claude do chat; medidas do protótipo)
Tokens, componentes compartilhados, animações e divergências: **leia `../THEME.md` primeiro**. Aqui só o que é desta tela.

## 1. Propósito
Corrida sem plano: o herói é o cronômetro; Distância e Ritmo ao lado. O mapa é atmosfera. Tudo que já existe (menu do topo, Pausar, Parar) **fica igual**; o que muda é o botão inferior direito (agora um leque de opções do mapa) e o texto do pill da câmera (conforme o Perfil).

## 2. Moldura e medidas (393×851)
| bloco | posição e tamanho |
|---|---|
| topo (chip GPS à esquerda) | (0,0) 393×64 |
| hero `FreeRunLayout` (padding-top 14, espaço 8 entre itens) | (0,64) 393×222 |
| ↳ TimerRing 112 | (140.5,78) 112×112 |
| ↳ Distância/Ritmo | (0,222) 393×64 |
| spacer flexível (região do mapa) | (0,286) 393×475 |
| ↳ pill da câmera | (16,717) 117.7×30 |
| bloco inferior = ActionBar (sem card "A seguir") | (0,761) 393×90 |
| MapStyleFan (contêiner) | (217,591) 160×160 |
| RadialMenu (contêiner) | (217,16) 160×160 |
Elemento flexível: o spacer. Sem rolagem. Em telas baixas o spacer encolhe (os leques podem cobrir o hero).

## 3. Hero
- Anel `--teal`, `progress = 0` (não desenha progresso), texto "24:18" (exemplo) em `--text`.
- Rótulo "Corrida livre": Inter 600 11/16, espaçamento 1,32, `--teal`.
- Distância "3,42" km e Ritmo "5:48" min/km (exemplos). testTags: `free_run_layout`, `timer_ring`, `stats_row`.

## 4. Estados (PNGs em `screens/`)
- `corrida-livre.png` — padrão: câmera = Seguir você, leques fechados, em andamento
- `corrida-livre--camera-direcao.png` — Perfil = "Com direção": mapa girado, pill "Seguindo com direção"
- `corrida-livre--camera-rota.png` — Perfil = "Rota completa": rota inteira enquadrada, pill "Rota completa"
- `corrida-livre--radial-aberto.png` — menu do topo aberto (existente)
- `corrida-livre--fan-open.png` / `corrida-livre--fan-dark.png` — leque do mapa aberto / modo escuro ligado
- `corrida-livre--pausado.png` — botão vira "Retomar"
- `corrida-livre--parar-segurando.png` — Parar segurado a 50% (anel de retenção; texto continua "Parar")
- `corrida-livre--reduced-motion.png` — sem animações

## 5. Específico desta tela
- Pausado: o texto do anel **não** vira âmbar na corrida livre (só no plano).
- Sem card "A seguir": a âncora do leque fica 10 acima da ActionBar (bottom = 100).
