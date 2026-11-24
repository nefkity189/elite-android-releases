# Revisão de integridade da competição — 21/11/2026

## Evidências e alterações

- `firebase functions:list --project aisler-runner --json` retornou lista vazia. Não existe validação GPS por Cloud Functions publicada neste projeto.
- [Repositório](main/java/com/elite/sch/app/data/competition/OnlineCompetitionRepository.kt): o celular escreve distância acumulada, tempo, ritmo e chegada diretamente em `missions/{id}/players/{uid}`. Não envia coordenadas nesse fluxo.
- [Regras](../../firestore.rules): não calculam percurso; aceitam chegada sem exigir distância mínima ou início da prova. A monotonicidade dos valores não impede distâncias fabricadas.
- [Estado da competição](main/java/com/elite/sch/app/ui/competition/CompetitionDemoState.kt): envio a cada 7 segundos após concluir a escrita; uma escrita offline pode ficar pendente. Corrigido o encerramento do loop por exceção de envio, preservando cancelamento da coroutine. Removido o aumento artificial de distância na chegada; agora exige distância local finita e suficiente.
- A interface considera a missão encerrada quando qualquer participante termina. Precisa separar vencedor, chegada individual e encerramento da sala.
- O fluxo atual observa participantes de todas as salas carregadas. Para reduzir leituras, o progresso detalhado deve ser observado apenas na sala aberta, com resumo persistido para as demais.
- Alterações Android desta revisão ainda não compiladas nem testadas em aparelho. Nenhuma regra remota alterada nesta revisão.

## Contrato proposto para um servidor confiável

1. Cliente autenticado envia amostras em lotes a cada 5–10 segundos, coletadas mais frequentemente para preservar curvas. Cada lote tem corrida, sessão, sequência e identificador único; cada ponto tem coordenadas, precisão e tempo monotônico de captura. O horário informado pelo cliente é evidência não confiável.
2. Servidor verifica inscrição, largada, sessão, limites de payload, sequência e duplicatas. Reenvio do mesmo lote retorna o mesmo resultado, sem somar distância novamente. Coordenadas detalhadas ficam privadas.
3. Calcular segmentos aceitos no servidor (Haversine ou PostGIS geography em metros), considerando precisão, jitter parado, lacunas, saltos e velocidade. Um ponto ruim não deve gerar banimento automático nem virar âncora para o próximo segmento. Limites precisam de calibração com percursos reais.
4. Substituir o resumo público de progresso; manter evidência mínima privada com retenção definida. Apagar todo ponto anterior impede auditoria e reprocessamento de resultados contestados.
5. Na travessia da meta, enviar imediatamente o último lote. Gravar chegada individual de forma idempotente e transacional. Cliente nunca escreve distância oficial, posição, vencedor ou elo diretamente.
6. Uma transação impede dois vencedores oficiais, mas a primeira requisição recebida não prova quem cruzou a distância primeiro. Manter resultado provisório, tolerância de atraso publicada e política de empate/revisão compatível com a incerteza do GPS. Não prometer precisão de milissegundos.
7. Vencedor definido não deve interromper os outros corredores. Encerrar a sala quando todos terminarem/desistirem ou quando atingir prazo definido.
8. Realtime distribui apenas estado persistido e autorizado. Após reconectar, buscar snapshot oficial e sequência confirmada. Mostrar progresso pendente/offline sem anunciá-lo como resultado confirmado.
9. Ranking e histórico são atualizados pelo servidor uma única vez por resultado validado. Limpeza diária só depois de arquivar resultados; nunca excluir uma prova ainda em andamento.

## Firebase ou Supabase

Firebase atual: manter Auth e Firestore evita migração. Entretanto, publicar Cloud Functions exige Blaze; Spark não fornece esse processamento confiável de GPS. Não habilitar cobrança sem decisão do responsável.

Supabase: RPC + PostGIS é uma alternativa, mas exige projeto, integração de identidade, RLS, permissões restritas da função e migração coordenada. Não manter dois bancos decidindo independentemente o vencedor. Nunca colocar chave administrativa no APK. Avaliar limites do plano gratuito antes de escolher.

No PostgreSQL, `now()` é o início da transação, não o instante exato da chegada ou do commit. `clock_timestamp()` mede o relógio corrente, mas também não resolve atraso de rede nem falsificação do tempo do dispositivo.

GPS plausível não comprova deslocamento autêntico: Fake GPS pode simular velocidade humana e um carro pode trafegar devagar. Integridade do dispositivo e análise de trajetória ajudam, sem garantia absoluta.

## Validação necessária antes de publicar

- Dois atletas cruzando a meta simultaneamente; reenviar lote/chegada sem duplicar distância ou elo.
- Lotes fora de ordem, desconexão, retomada, processo morto e relógio do celular alterado.
- Usuário parado, GPS ruim, túnel, curva, salto de posição e deslocamento de carro.
- Chegada abaixo da meta, antes da largada, sem inscrição ou gravada por outro usuário: rejeitar.
- Primeiro colocado termina enquanto outro continua; desistência e encerramento por prazo.
- Contas reais distintas, permissões de leitura e limites de carga/quota.

## Fontes oficiais

- [Firebase: implantação de Functions e Blaze](https://firebase.google.com/docs/functions/get-started)
- [Firestore: transações, repetição e funcionamento offline](https://firebase.google.com/docs/firestore/manage-data/transactions)
- [Firestore: listeners e escritas locais pendentes](https://firebase.google.com/docs/firestore/query-data/listen)
- [Supabase: PostGIS](https://supabase.com/docs/guides/database/extensions/postgis)
- [Supabase: RLS](https://supabase.com/docs/guides/database/postgres/row-level-security)
- [Supabase: Realtime](https://supabase.com/docs/guides/realtime/subscribing-to-database-changes)
- [PostgreSQL: funções de data e hora](https://www.postgresql.org/docs/current/functions-datetime.html)
