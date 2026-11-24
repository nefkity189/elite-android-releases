Você é o Claude Code no projeto Elite (Kotlin, Android, Compose; os cards são Bitmaps desenhados em Canvas).

Tarefa: refazer CompetitionShareCardGenerator.render(...) (ui/components/CompetitionShareCardGenerator.kt) para que cada kind (0 Participação, 1 Pódio, 2 Conquista, 3 Ranking, 4 Convite, 5 Sem app) tenha o layout próprio desta pasta (HANDOFF/<card>/screens/card-<card>.png), 1080x1350, usando a identidade real do app: símbolo E vermelho, corredor da splash, inclinação de -12 graus, cores e fontes do tema.

Ordem: 1) leia THEME.md e conserte as fontes corrompidas em res/font (Inter pelos .otf da pasta fonts/, Saira Stencil One do Google Fonts) 2) crie helpers: drawEliteMark(canvas, x, y, size, color?), drawRunner (reuse a lógica de SmallRunner da EliteSplash, pose parada), withSkew(canvas, pivotX, pivotY) { }, drawFrame (cabeçalho e rodapé comuns) 3) uma função por card (drawParticipation, drawPodium, drawAchievement, drawRanking, drawInvite, drawNoApp) seguindo cada SPEC.md e spec/card-*.md 4) estenda os parâmetros de render() e os pontos de chamada (publishCard em CompetitionFlowScreen.kt e ShareCardPreview em CompetitionSecondaryScreens.kt) com os dados novos pedidos nos SPEC.md.

Não mudar: lógica de competição, rotas, ShareCardGenerator.saveShareCard, textos de compartilhamento, tamanho 1080x1350, estados vazios (nunca inventar dados).

Verificação obrigatória: gere cada card em PNG (teste Robolectric, atualizando ShareCardGeneratorTest se mudar assinatura), compare com screens/card-<card>.png usando a skill html-to-native-replica em .claude/skills da pasta do card, corrija posição, tamanho e cor, e repita até ficar igual.
