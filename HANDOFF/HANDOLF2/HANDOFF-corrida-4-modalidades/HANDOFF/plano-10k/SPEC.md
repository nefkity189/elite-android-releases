# SPEC — Primeiros 10K (modalidade `plano_10k`) — treino estruturado com mapa   (escrito pelo Claude do chat; medidas do protótipo)
Tokens, componentes compartilhados, animações e divergências: **leia `../THEME.md` primeiro**. Aqui só o que é desta tela.

## 1. Propósito
Seguir o plano etapa a etapa: o usuário precisa saber **o que fazer agora** (Corra/Caminhe), **onde está no treino** (Etapa X de Y, anel de progresso), **como está indo** (Distância, Ritmo) e **o que vem depois** (card "A seguir"). Menu do topo, Pausar e Parar **ficam iguais**. Novo: leque de opções do mapa (inferior direito) e pill da câmera conforme o Perfil.
As 3 telas de plano (2,5K, 5K, 10K) têm a mesma estrutura; esta usa os dados do plano Primeiros 10K.

## 2. Moldura e medidas (393×851)
| bloco | posição e tamanho |
|---|---|
| topo (chip GPS à esquerda) | (0,0) 393×64 |
| hero do plano (padding-top 14) | (0,64) 393×257.6 |
| ↳ linha ícone 28 + rótulo (espaço 12) | (147.8,78) 97.4×28 |
| ↳ "Etapa X de Y" (4 abaixo da linha) | (151.7,110) 89.6×19.6 |
| ↳ TimerRing 112 (8 abaixo) | (140.5,137.6) 112×112 |
| ↳ Distância/Ritmo (8 abaixo; coluna com 16 de padding) | (16,257.6) 361×64 |
| spacer flexível (região do mapa) | (0,321.6) 393×376.4 |
| ↳ pill da câmera | (16,654) 117.7×30 |
| bloco inferior (card + ActionBar) | (0,698) 393×153 |
| ↳ card "A seguir" (margem 12/16, padding 12/14, raio 18) | (16,710) 361×39 |
| ↳ ActionBar | (0,761) 393×90 |
| MapStyleFan (contêiner) | (217,528) 160×160 |
| RadialMenu (contêiner) | (217,16) 160×160 |
Altura do hero = 14 + 28 + 4 + 19,6 + 8 + 112 + 8 + 64 = 257,6. Elemento flexível: o spacer (cai de 475 na corrida livre para ≈376 aqui). Quando não há próxima etapa (última), o card some e o bloco inferior volta a 90: o spacer cresce 63 e a âncora do leque desce junto (efeito pulo, aceito).

## 3. Dados do plano usados nos exemplos (origem: Template10K, "Intervalado 4x(3min Forte / 2min Leve)" (aquecimento 5 min + 4×(3 min forte / 2 min leve) + desaquecimento 5 min))
Etapas (duração em segundos, tipo → cor da barra não existe mais; o hero usa verde-oliva p/ correr e latão p/ o resto):
1. AQUECIMENTO 300s — "Aquecimento: caminhada em ritmo confortável (5 min)"
2. CORRER 180s — "3 min forte"
3. CAMINHAR 120s — "2 min leve"
4. CORRER 180s — "3 min forte"
5. CAMINHAR 120s — "2 min leve"
6. CORRER 180s — "3 min forte"
7. CAMINHAR 120s — "2 min leve"
8. CORRER 180s — "3 min forte"
9. CAMINHAR 120s — "2 min leve"
10. DESAQUECIMENTO 300s — "Desaquecimento: caminhada leve e respiração profunda (5 min)"
Total = 1800 s (30 min).

## 4. Hero (regras)
- Ícone 28 (DirectionsRun / DirectionsWalk) + rótulo `titleLarge`: **"Corra"** em `--teal` quando a etapa é CORRER; **"Caminhe"** em `--brass` para CAMINHAR, AQUECIMENTO e DESAQUECIMENTO (o código só distingue correr × resto).
- "Etapa {índice+1} de {total de etapas}" em `--muted`, centralizado.
- Anel: tempo decorrido total (mm:ss), progresso = decorrido ÷ 1800 s; pausado = âmbar.
- Distância e Ritmo atuais.
- Card "A seguir: <instrução> <duração>" só se existe próxima etapa. testTags: `current_step_card`, `timer_ring`, `stats_row_hero`.
Exemplos (valores ilustrativos):
| estado | rótulo | etapa | anel | progresso | dist. · ritmo | card |
|---|---|---|---|---|---|---|
| padrão (correndo) | Corra | Etapa 4 de 10 | 11:03 | 37% | 1,98 km · 5:35 | "A seguir: 2 min leve 2 min" |
| caminhe | Caminhe | Etapa 5 de 10 | 13:36 | 45% | 2,41 km · 5:39 | "A seguir: 3 min forte 3 min" |
| última etapa | Caminhe | Etapa 10 de 10 | 26:40 | 89% | 3,95 km · 6:45 | (sem card) |

## 5. Estados (PNGs em `screens/`)
- `plano-10k.png` — padrão: câmera = Seguir você, leques fechados, em andamento
- `plano-10k--caminhe.png` — etapa de caminhada (rótulo e ícone em latão)
- `plano-10k--ultima-etapa.png` — desaquecimento: sem card "A seguir"; leque desce 63
- `plano-10k--camera-direcao.png` — Perfil = "Com direção": mapa girado, pill "Seguindo com direção"
- `plano-10k--camera-rota.png` — Perfil = "Rota completa": rota inteira enquadrada, pill "Rota completa"
- `plano-10k--radial-aberto.png` — menu do topo aberto (existente)
- `plano-10k--fan-open.png` / `plano-10k--fan-dark.png` — leque do mapa aberto / modo escuro ligado
- `plano-10k--pausado.png` — botão vira "Retomar" e o anel/tempo ficam âmbar
- `plano-10k--parar-segurando.png` — Parar segurado a 50% (anel de retenção; texto continua "Parar")
- `plano-10k--reduced-motion.png` — sem animações
