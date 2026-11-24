# Competição no Firebase Spark — implementação de 21/11/2026

Decisão: permanecer no Firebase; nenhum Supabase, Blaze ou serviço pago ativado.

## Implementado

- [CompetitionSync](main/java/com/elite/sch/app/data/competition/CompetitionSync.kt) produz atualizações a partir do serviço, a cada 7 segundos, sem aguardar rede. A fila persistente padrão do Firestore Android guarda escritas offline e retoma quando conectado. Erros definitivos de permissão/quota são apresentados; não são confirmações de chegada.
- A inscrição em uma sessão local fica em preferências privadas com UID, missão, treino e bases de tempo/distância. Distância anterior do treino não conta na nova competição. É necessário iniciar rastreamento GPS antes de entrar na prova.
- [RunTrackingService](main/java/com/elite/sch/app/service/RunTrackingService.kt) já usava foreground service do tipo location e notificação. Passou a solicitar reinício START_STICKY, promover imediatamente a notificação na recuperação e preservar distância do filtro restaurado. Registros antigos e bases monotônicas incompatíveis são rejeitados. Corrigido cálculo do tempo ao restaurar uma pausa.
- [GpsSession](main/java/com/elite/sch/app/tracker/GpsSession.kt) rejeita precisão >20m, valores inválidos e marcação Android de localização simulada. Deslocamento mínimo usa a precisão dos dois pontos (mínimo 3m), velocidade máxima 7m/s. Após intervalo >30s entre pontos aceitos, reancora sem inventar distância no trecho desconhecido. O filtro pode subcontar trajetos; calibrar em campo.
- Pontos aceitos continuam persistidos em Room. Uma janela de até 10 pontos recentes da competição é enviada em `missions/{id}/gpsPrivate/{uid}`, junto ao resumo numa escrita atômica; só o dono pode ler essa janela. O documento é sobrescrito. Não é arquivo completo da rota no servidor; a rota completa permanece local.
- Chegada é enfileirada imediatamente ao alcançar a meta, mesmo sem a tela aberta; nenhuma distância é arredondada para criar uma conclusão. A interface separa chegada aguardando sincronização de chegada registrada.
- Regras exigem início da prova, participantes mínimos, tempo/distância não negativos e crescentes, limite global e incremental de 7m/s com tolerância de 20m, tempo compatível com o relógio do servidor e chegada com distância suficiente. Entrada/saída exigem atualizar o contador na mesma operação atômica.
- Uma chegada individual não encerra a sala enquanto houver participantes sem chegada. Ainda falta um estado persistido de desistência e um prazo de encerramento; salas com desistentes podem continuar em andamento.
- Removidos segundo lugar e conquistas fixas da tela de resultado; tempo mostrado passa a ser do próprio atleta. Classificação continua provisória pela ordem de recebimento.

## Limites importantes

As regras NÃO recalculam Haversine nem comprovam GPS autêntico. Um cliente adulterado ainda pode produzir valores plausíveis. Fake GPS sofisticado não é impedido só pela marcação Android. App Check de produção ainda precisa de integração/configuração própria.

Foreground service não impede encerramento forçado pelo usuário, reboot ou restrições do fabricante. Não afirmar que Android/HyperOS nunca matará o processo. Recuperação depende das permissões e de estado local válido; testes físicos com tela bloqueada continuam necessários.

Escritas pendentes sobrevivem a reconexão/reinício normal pelo SDK; limpar dados ou desinstalar apaga a fila. Não há promessa de persistir uma escrita que ainda não chegou ao armazenamento local antes de uma morte abrupta do processo. Pontos coletados durante um período em que o serviço não executou não podem ser recuperados.

O resumo e a janela GPS geram até duas escritas a cada 7 segundos (aproximadamente 1.029 escritas/atleta/hora), mais leituras dos listeners. Não há capacidade ilimitada no Spark. A janela evita crescimento de armazenamento, mas não elimina consumo de operações. Os listeners de participantes de várias salas ainda precisam de otimização de escala.

Tempo oficial de travessia, empate por precisão, análise confiável do percurso, elo automático, rotação anual e limpeza diária automática continuam sem backend confiável publicado. Lista de Functions remotas estava vazia na revisão. Não anunciar essas automações como ativas.

## Verificação e entrega

Compilação Kotlin debug executada; não equivale a teste físico. As regras foram compiladas pelo Firebase e publicadas em `aisler-runner`. APK não foi instalado nem publicado nesta etapa. Fazer teste de tela bloqueada, modo avião/reconexão, duas contas, desistência e chegada antes de distribuir uma nova versão.

Fontes: [Firestore offline](https://firebase.google.com/docs/firestore/manage-data/enable-offline), [serviços Android](https://developer.android.com/develop/background-work/services/fgs/service-types).
