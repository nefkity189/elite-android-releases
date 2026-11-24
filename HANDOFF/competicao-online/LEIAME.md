# Elite — Modo Competição Online (pacote de layout)

11 telas para Android (Poco X7, 393x851dp), no tema Elite que o app já usa (oliva, vermelho, latão, Barlow Condensed, Inter, Saira Stencil One). Cada pasta tem `screen.html`, `HANDOFF.md`, `states.json` e `CLAUDE-CODE-PROMPT.md`.

| # | Pasta | Tela |
|---|---|---|
| 1 | `01-hub` | Hub: seletor 2,5K/5K/10K, atalhos Ranking e Conquistas, missões 02h–21h, compartilhar sempre visível |
| 2 | `02-sala` | Sala da missão: countdown, competidores, convite, avisos de 10 e 5 min, confirmada |
| 3 | `03-nao-iniciada` | Missão não iniciada por falta de competidores + convocação |
| 4 | `04-prova` | Prova ao vivo: tempo, progresso, pace, placar, Finalizar |
| 5 | `05-resultado` | Missão cumprida: colocação, elo, ranking global/estado, classificação |
| 6 | `06-ranking` | Ranking estilo games: temporada, liga, card fixo, filtros, pódio, zonas, perfil público |
| 7 | `07-temporada` | Temporada, ligas/divisões, recompensas, melhores posições |
| 8 | `08-conquistas` | Conquistas globais e locais, destaque de nova conquista |
| 9 | `09-compartilhar` | Cards de redes sociais: participação, pódio, conquista, ranking, convite, convite sem app |
| 10 | `10-avisos` | Banners, notificações push e bottom sheets |
| 11 | `11-menu-competicao` | Menu "Adicionar corrida" com o novo card Premium da Competição Online |

## Ordem de implementação (Claude Code)
1. **Base compartilhada** (sai da tabela Mudanças do `01-hub`): tokens novos em `Color.kt`, `EliteLeague.kt` + `LeagueBadge`, avatares, `ShareIconButton`, `MissionStatusBadge`.
2. `01-hub` → `02-sala` (traz `WarningBanner`, `InviteCard`) → `03-nao-iniciada` → `04-prova` → `05-resultado`.
3. `06-ranking` (traz `RankingRow`) → `07-temporada` → `08-conquistas`.
4. `11-menu-competicao` (porta de entrada do modo; precisa de `isPremium`) → `09-compartilhar` (estende `ShareCardGenerator.kt`) → `10-avisos`.
Cada tela é um pacote independente: copie a pasta para o projeto e use o prompt dela. A coluna **Novo?** do Mapa de componentes diz o que vem de outra tela.

## Decisões que tomei (confirme)
- Mantive a identidade Elite do app (paleta do zip) em vez de criar outra; as 6 cores de liga são novas.
- Lema do modo: "Foco, Missão dada, Missão cumprida." (como no briefing).
- *Pontuação geral* = pontos de elo; pontos na classificação da missão = variação de elo (+34, +18, −12). Faixas das ligas e textos de recompensa são propostas.
- Exemplos com estado MT; nomes de competidores fictícios.

## O que não foi verificado aqui
Não há emulador: o lado Compose não foi testado por mim. As fontes (Barlow Condensed, Inter, Saira) não carregaram no render do meu ambiente, então os PNGs usam fonte de reserva, mais larga; a medição final deve ser feita pelo Claude Code com `--fonts-dir`. O linter da skill passou com 0 erros nas 10 telas.
