# Correções aplicadas (P1 a P7)

Base: o projeto enviado (AISLE_Proj_1.zip). Para as telas que perderam funções na reescrita,
o código completo da versão anterior foi restaurado e ajustado ao visual dos mockups.

## Causa do problema
A reescrita das telas Hoje e Corrida ativa reduziu os arquivos de ~57 KB e ~32 KB para ~12 KB e ~9 KB
e apagou funções (28 testTags sumiram): recuperar corrida, cartão de sugestão e de ajuste do plano,
permissões de GPS, aviso de bateria, gráfico de ritmo, corridas recentes e botão de finalizar por segurar.

## O que mudou
1. ui/screens/TodayScreen.kt (restaurada e ajustada ao mockup)
   - Volta tudo: diálogos de GPS, recuperação de corrida, sugestão do motor, 'Seu plano se ajustou',
     check-in pré-treino, pausa ativa, corridas recentes, meta semanal.
   - Visual: chip 'Semana X de Y' em pílula; cartão do treino com cantos de 28 dp e borda turquesa suave;
     rótulos sem caixa alta ('Treino de hoje', 'Concluído', 'Pulado'); CTA 'Começar treino' com 56 dp;
     círculos da faixa da semana com 36 dp.
2. ui/screens/ActiveRunScreen.kt (restaurada e redesenhada)
   - Mantém: bateria, GPS, TTS, tela sempre ligada, diálogo de encerrar, botão de finalizar por segurar (1,5 s).
   - Novo layout: etapa atual centralizada com o cronômetro grande (sem moldura), barra segmentada com
     todos os blocos (atual em destaque), métricas entre dois divisores, 'A seguir', botões fixos no rodapé
     com 72 dp. Parâmetro novo opcional: steps.
3. MainActivity.kt: passa steps (blocos do treino em andamento) para ActiveRunScreen.
4. ui/components/AisleControls.kt: AisleTopBar ganhou titleStyle (opcional).
5. ui/screens/ProgressScreen.kt: título como headline, sem seta de voltar (é aba), testTags
   screen_progress e dialog_run_detail de volta.
6. ui/screens/FullPlanScreen.kt: título como headline, seta de voltar só quando aberta como sub-tela,
   testTag screen_plan de volta.
7. ui/screens/WelcomeScreen.kt: título em display ('Do zero / aos 5 km') e benefícios em ícones com moldura.

## Não alterado (já estava de acordo com os mockups)
Quiz, Dias, Duração, Prévia do plano, Resumo do treino, Plano (arquivos plan/), banco de dados, motor de ajuste.

## Limites
- Não consegui compilar aqui (sem Gradle/Android SDK). Conferi chaves e parênteses, e que todos os imports
  existem no projeto. Rode: ./gradlew assembleDebug testDebugUnitTest
- Sem fonte de marca: Type.kt ainda usa a fonte padrão do sistema.
- Os números das métricas na Corrida ativa usam o estilo do MetricBlock (22 sp); o mockup mostra maiores.
