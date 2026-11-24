# Lições para um app Android de corrida (GPS + tela apagada)

Extraídas do CLAUDE.md do SPRAWL//RUN (app de corrida em Flutter, sem Play Services).
Ficaram só as lições que valem para **Android nativo (Kotlin/Compose)**. Cada uma
traz o sintoma, a causa e o que fazer. Nada aqui foi testado no seu app: use como
checklist de riscos, não como verdade absoluta.

---

## 1. A corrida precisa sobreviver

**Foreground service é dono da corrida, não a Activity nem o ViewModel.**
- O estado da corrida (tempo, distância, trace) vive no Service. A UI só observa.
- Não chame `stopSelf()` em `onTaskRemoved`. Um corredor perdeu a corrida inteira
  ao deslizar o app para fora da tela.
- Inicie o service **antes** de qualquer espera (GPS pronto, permissões, música),
  senão o processo pode ser morto antes de virar foreground.
- Use `FOREGROUND_SERVICE_TYPE_LOCATION` (e a permissão `FOREGROUND_SERVICE_LOCATION`
  no Android 14+). Confira na documentação do seu `targetSdk` quando o tipo `location`
  exige que a permissão de localização já esteja concedida ao iniciar.
- No Android 12+, a notificação do foreground service pode demorar até 10 s para
  aparecer. Use `setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)`.
- Segure um **partial wake lock**. Estar em foreground não mantém a CPU acordada, e
  um processo congelado para de disparar o ticker (avisos de parcial, falas, etc.).

**Salve a corrida em andamento no disco.**
- Grave um snapshot a cada ~15 s de corrida e no instante em que a meta é atingida.
- Apague o snapshot só depois que o resultado final foi gravado.
- Se, ao abrir o app, houver um snapshot, o processo morreu no meio da corrida:
  ofereça **retomar** ou **descartar**. Ao retomar, o intervalo morto não conta
  como tempo de corrida.
- Ao apagar snapshot, passe o **id da corrida**: uma decisão atrasada sobre uma
  corrida antiga não pode apagar o snapshot de uma corrida nova.

**Fabricantes matam o service mesmo em foreground.**
- Um gerenciador de bateria de fabricante deu SIGKILL num service com importância 125.
- Peça uma vez, antes da corrida, para o app ficar fora da otimização de bateria
  (`ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`) e mostre o estado nas configurações.
- Para investigar: `adb shell dumpsys activity exit-info <pacote>`.

---

## 2. GPS

- **"Localização ligada" não significa "GPS funcionando".** O modo de economia de
  bateria deixa a localização por rede ativa e desliga o provedor GPS: permissão
  concedida, serviço ligado e nenhum fix. Verifique o provedor GPS especificamente.
- **Trate `onProviderDisabled` e `onProviderEnabled`** durante a corrida, não só na
  assinatura inicial.
- **O aviso tem que sumir também.** Registre o listener **antes** de reportar que o
  provedor está desligado e continue ouvindo mesmo assim. Senão, quando o GPS volta,
  ninguém avisa e o alerta fica na tela a corrida toda. Limpe o aviso no primeiro fix
  de qualquer precisão.
- **Nunca engula erro de stream de localização.** Uma corrida de duas horas foi
  gravada só com tempo, sem distância, e a tela não mostrava nada de errado.
  Transforme o erro em estado visível na UI.
- **Sem Play Services (F-Droid, ROM sem Google):** o provedor "fused" do fabricante
  pode aceitar pedidos e nunca entregar um fix. Nesse caso peça o provedor GPS pelo
  nome via `LocationManager`. Se seu app usa o Fused do Google, teste também em um
  aparelho sem GMS.
- Para saber se o pedido chegou ao chip GNSS: `adb shell appops get <pacote>`.
  `MONITOR_HIGH_POWER_LOCATION` indica GNSS; só `MONITOR_LOCATION` indica fused.
- **Velocidade entre dois fixes oscila ±1 m/s só por ruído.** Para pace e para
  qualquer decisão em tempo real use **média em janela** (por exemplo, últimos
  12 s), com cada amostra limitada, e não mediana nem valor instantâneo. Evite
  julgar nada no **primeiro minuto** de movimento, enquanto os fixes se acomodam.
- **Cadência (contador de passos):** com a tela apagada o sensor pode segurar passos
  e entregar muitos de uma vez, o que parece um sprint. Só confie em cadência sobre
  janela de pelo menos 5 s e descarte valores acima de ~240 passos/min.
- **O "pace de corrida" não é a média da corrida.** Pausas para caminhar puxam a
  média para baixo e fazem corrida normal parecer sprint depois. Se você compara com
  o ritmo habitual, use o ritmo *correndo* (por exemplo, o tempo em movimento).
- A pausa **nunca** entra na duração, no pace ou na velocidade média salvos.

---

## 3. Notificações

- **A importância do canal é congelada na criação.** Você não consegue baixá-la nem
  subi-la depois com `createNotificationChannel`; só o usuário muda, nas configurações
  do Android. Para mudar, crie um **novo id de canal**. Escolha com cuidado:
  - canal da corrida: `IMPORTANCE_LOW` (silencioso);
  - lembrete/aviso que deve chamar atenção: `IMPORTANCE_DEFAULT`.
- **Se uma biblioteca cria o canal primeiro, ela decide.** O geolocator criou o canal
  com `IMPORTANCE_NONE`, que o Android trata como bloqueado: a notificação do
  service nunca chegava à bandeja. Crie o seu canal antes, na sua `Application`/Activity.
  Um aparelho que já tem o canal mantém as configurações antigas, então **teste em
  instalação limpa**.
- **Peça `POST_NOTIFICATIONS` (Android 13+).** Sem a permissão o service roda com a
  notificação suprimida.
- **O ícone pequeno precisa ser uma silhueta transparente.** Só o canal alfa conta.
- **O shrinker (R8) pode remover um ícone referenciado só por nome.** O ícone vira 0
  e o Android substitui sua notificação pelo aviso genérico "app em execução". Proteja
  com `res/raw/keep.xml` (`tools:keep`).
- **Se você atualiza a notificação a cada segundo, use `setSilent(true)`.** Senão um
  corredor que deu som ao canal ouve um "plim" a cada segundo.
- **Para só mudar o texto, use `notify()`, não `startForeground()` de novo.**
- **Texto longo:** o corpo colapsa em uma linha. Use `BigTextStyle` se a mensagem passa
  de uma linha.
- Ações Pause/Retomar/Parar funcionam sem ícone (o Android não os desenha desde o 7.0).
  Parar antes da meta deve abrir o app para confirmar; depois da meta, finalize direto.
- **`cancel(id)` também remove uma notificação já exibida.** Ao reagendar, cancele só
  os ids ainda pendentes.
- **Notificação agendada é fixada no momento do agendamento.** Depois de atualizar o
  app, a primeira que dispara ainda é a da versão antiga. Para testar uma mudança, abra
  o app uma vez antes.
- **Toque que abre o app a frio** (processo morto) chega pela Intent de lançamento, não
  pelo callback de "app em execução". Leia as duas rotas e deduplique.

---

## 4. Áudio (música, voz do narrador, avisos de parcial)

- **Um único dono do foco de áudio.** Peça o foco **uma vez por corrida** e devolva
  no fim. Um player que ativa a sessão a cada `play()` e nunca a desativa faz um efeito
  sonoro curto pausar a música do corredor para sempre.
- **Fala só abaixa a música (ducking)**; não devolva o foco depois de cada fala, ou o
  player do corredor recomeça por baixo.
- **Registre o player do corredor antes de tomar o foco**, para saber quem retomar.
- **Nunca dê `await` num `play()` de loop.** O futuro só completa quando a reprodução
  termina, e um loop nunca termina. Isso travou o início da corrida.
- **Sem permissão INTERNET:** bibliotecas que servem áudio por servidor HTTP em
  loopback exigem INTERNET. Para áudio gerado, use `AudioTrack`. Mantenha ~4 s de
  buffer e faça pausa, parada e abaixar volume **no player**, não na geração.
- Ao tocar arquivos do usuário por SAF (`content://`), use um player que aceite URI.

---

## 5. Dados e persistência

- **Escrita atômica.** `writeText` trunca o arquivo antes de gravar; se o processo
  morre no meio, sobra um arquivo corrompido. Grave num arquivo irmão e **renomeie**.
  Um arquivo que não parseia vai para **quarentena**, nunca é lido como vazio e
  sobrescrito (isso transforma um crash em perda total).
- **O que é derivável fica derivado.** Estatísticas, streaks e progresso de conquistas
  são funções do histórico de corridas, não contadores que podem divergir.
- **Meta semanal tem histórico.** Julgue cada semana contra a meta que valia *naquela
  semana*. Julgar tudo pela meta de hoje fez um streak real de 4 semanas aparecer
  como 0 depois que a meta subiu.
- **Conquista nova precisa ser datada pelo histórico.** Se a próxima corrida "varre"
  tudo que o histórico já satisfazia, aparece tudo como se aquela corrida tivesse
  ganhado, com a data de hoje. Reexecute o histórico e date cada conquista na corrida
  que cruzou o limite (e nunca mova uma data para depois).
- **Todo campo novo precisa decodificar de dados que não o têm.** Adicionar campo
  obrigatório sem valor padrão apaga o histórico de quem atualiza.
  No Room: nunca use `fallbackToDestructiveMigration`; escreva `Migration` numerada e
  teste com banco antigo congelado. Migrações devem poder rodar duas vezes sem dano
  (um crash entre migrar e salvar repete o passo).
- **Backup = o aparelho inteiro** (perfil + todas as corridas com o trace). Se você
  criar um arquivo ou tabela persistida nova, **estenda o backup/restauração junto**,
  senão ele não vai no backup.
- **Pasta de backup via SAF:** a permissão persistida vive em
  `persistedUriPermissions`, não nas configurações do app (senão um restore em outro
  aparelho traz uma pasta onde ele não pode escrever). Ao liberar permissões, libere só
  as de escrita; pasta de música com permissão de leitura pode ser a mesma árvore.
  Apague só os backups automáticos do app, nunca exportações manuais na mesma pasta.
- **Dados lentos fora do caminho de abertura.** Arquivo de histórico dividido por ano:
  a abertura lê só o ano corrente. Ler tudo só quando a tela de histórico abrir, e fora
  da thread de UI.
- **Referencie textos por id, não por conteúdo,** em arquivos que sobrevivem a updates;
  reescrever um texto não deve deixar registros antigos órfãos.

---

## 6. Testabilidade e fuso/horário

- **O motor da corrida não conhece persistência nem UI.** Receba `LocationSource`,
  narrador/áudio e relógio por injeção. Assim missões inteiras rodam em milissegundos
  em teste.
- **Relógio injetável.** Tudo que depende de hora (esta semana, streak, "há 3 dias",
  saudação) lê o relógio injetado, nunca `System.currentTimeMillis()` solto. Teste
  streaks atravessando uma mudança real de horário (fuso com horário de verão).
- **O que a suíte não alcança** (GPS, foco de áudio, swipe do app, pasta de backup,
  canais de notificação): teste em aparelho real e trate como regressão se algo disso
  quebrar.

---

## 7. Instalar sem apagar os dados

- **Nunca desinstale para atualizar.** Desinstalar apaga o diretório de dados e,
  com ele, o histórico. Use `adb install -r` ou `./gradlew installDebug`.
- Confirme que atualizou no lugar: `firstInstallTime` em
  `adb shell dumpsys package <pacote>` **não pode ter mudado**. Verifique antes se a
  instalação realmente funcionou (uma falha também deixa o timestamp igual).
- **APK assinado com outra chave bloqueia todos os builds locais**
  (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`). O único caminho é desinstalar, o que apaga
  o histórico, então **exporte um backup antes**.
- Se publicar com `versionCode` por ABI, instale o split da ABI certa; o APK universal
  com versionCode menor é recusado como downgrade.
- Builds de release demoram: rode em segundo plano.
- Adicionou arquivo em `assets` e ele não entrou no APK? Limpe (`./gradlew clean`) e
  confira com `unzip -l`.

---

## 8. Checklist antes de cada release

Uma **corrida real ao ar livre**, com tela apagada:
- [ ] fixes chegam e a distância acumula;
- [ ] a notificação aparece na bandeja (instalação limpa);
- [ ] pausa e retomada pela notificação funcionam;
- [ ] a música abaixa para uma fala e volta depois;
- [ ] deslizar o app da lista de recentes **não** mata a corrida;
- [ ] desligar o GPS (ou ligar economia de bateria) mostra o aviso, e religar o apaga;
- [ ] atualizar sobre uma versão anterior mantém o histórico e o `firstInstallTime`;
- [ ] backup exportado e restaurado em outro aparelho.
