# Lições para um app Android de corrida: parte 2

Complemento de `licoes-android-corrida.md`. Vem do README, SECURITY, CONTRIBUTING e
.gitignore do SPRAWL//RUN (Flutter), somados ao CLAUDE.md. Só entra o que **não estava
na parte 1** e vale para Android nativo (Kotlin/Compose). Nada aqui foi testado no seu
app; use como checklist de riscos.

---

## 1. Medir distância e tempo direito

- **Distância é filtrada, não aceita como veio.**
  - Descarte fixes com precisão pior que ~35 m.
  - Passos abaixo do ruído do GPS **seguram o ponto de referência** em vez de avançá-lo.
    Assim o movimento lento se acumula, em vez de ser jogado fora a cada fix.
  - Rejeite saltos fisicamente impossíveis, **até que vários seguidos** indiquem que o
    aparelho realmente se moveu (senão um teleporte de GPS vira distância, e um
    deslocamento real fica bloqueado para sempre).
- **Tempo decorrido vem do relógio de parede**, não de contar ticks de timer.
  Colocar o app em segundo plano não pode perder tempo.
- **Auto-pause só funciona com GPS vivo.** Sem fixes chegando, não dá para distinguir
  corredor parado de sinal perdido, e congelar o relógio estragaria em silêncio uma meta
  por tempo. Mostre um aviso discreto depois de ~30 s sem sinal.
- **Distância por passos (esteira, túnel) é um plano B e se declara como tal:**
  - só entra depois de ~5 s sem fix;
  - quando o primeiro fix volta, o GPS **recomeça de onde está**, sem somar a linha reta
    pelo intervalo sem sinal;
  - o comprimento da passada vem das próprias corridas do usuário: cada corrida guarda
    passos e distância GPS **dos mesmos segundos**, separados em caminhada e corrida pela
    cadência; as últimas ~30 corridas dão a passada, e a altura é só o primeiro chute;
  - mostre `≈` quando uma fração relevante (um décimo ou mais) veio de passos, e **não
    conte como recorde de pace** (nem desenhe trace, se não há pontos).
- **Calorias:** use equações publicadas (ACSM) e a massa corporal. Sem multiplicadores
  inventados. **Não invente métrica de esforço** (frequência cardíaca, "carga") a partir
  de GPS: é pior que não ter.
- **Caminhada não é corrida.** Mesmos dados, regras separadas: conta nos totais e no
  streak, tem conquistas próprias, e **não gera recorde de pace**.
- **Streak:** uma semana em andamento nunca quebra a sequência; só uma semana
  *terminada* abaixo da meta quebra. Deixa o usuário escolher a meta (minutos, km ou
  missões/treinos).
- **Desafio calibrado pelo próprio usuário.** Se o app propõe "acelere por 90 s", o alvo
  deve ser o ritmo dos últimos ~3 minutos *dele*, não um número fixo: é esforço real
  tanto a 4:00/km quanto a 7:00/km, e nunca impossível. Deixe desligável.
- **Exporte cada corrida como GPX 1.1.** OsmAnd, Strava e Komoot abrem; custo baixo,
  grande ganho de confiança.

---

## 2. Histerese: não deixe o app "tremer"

Se qualquer coisa muda conforme o ritmo (música, aviso, cor da UI, "você está acelerando"):

- Use **média** dos últimos ~12 s com cada segundo limitado (por exemplo, 1,6× o ritmo
  habitual), não mediana nem velocidade instantânea. Uma mediana cruzou um limiar 10%
  acima do habitual a cada meia hora de corrida: **130 mudanças em 42 minutos** num ritmo
  constante.
- **Limiar de subida diferente do de volta** (por exemplo, sobe a 120% e só desce a
  110%). Para cair, exija também que uma janela maior (~20 s) concorde.
- **Nenhum julgamento no primeiro minuto de movimento**, enquanto os fixes se acomodam.
- Alimente a lógica com a velocidade já tratada pelo seu filtro de movimento, não com o
  valor suavizado por decaimento: com sinal ruim (um fix a cada poucos segundos), o
  decaimento parece "diminuiu o ritmo" e derrubou o nível no meio da corrida.
- **Degrade de forma visível.** Se o aparelho não dá conta (CPU atrasada), baixe um nível
  pelo resto da corrida *antes* de cortar o recurso, e mostre um aviso na tela. Sem
  isso, "travou num nível" e "está limitado" ficam indistinguíveis.
- **Grave o motivo de cada mudança no registro da corrida** (o que a causou e a velocidade
  naquele momento). O `logcat` some em poucas horas e o trace só tem pontos GPS. Quando
  um bug só aparece ao ar livre, **leia o diagnóstico guardado no backup antes de
  adivinhar**. Isso vale para qualquer comportamento adaptativo.

---

## 3. Permissões: peça o mínimo, no momento certo

- **`ACCESS_BACKGROUND_LOCATION` não é necessária** para rastrear de tela apagada. Um
  foreground service com tipo `location` é a permissão mais estreita, e a revisão da
  Play Store não a trata como caso especial.
- **Corrida sem GPS** (meta por tempo, sem localização) deve rodar o service sob
  `FOREGROUND_SERVICE_SPECIAL_USE`, em vez de declarar uma localização que não tem. Se o
  service for só `location` e a localização não vem, o sistema pode congelar o app no
  meio da história. Promova para `location` quando a resposta de "GPS pronto" chegar.
- **`POST_NOTIFICATIONS`:** recusar não pode afetar o rastreamento; só esconde o aviso.
- **`ACTIVITY_RECOGNITION` (contagem de passos):** recurso **desligado por padrão**, e a
  permissão só é pedida quando o usuário o liga.
- **Pergunte sobre otimização de bateria uma vez, antes da corrida**, e nunca como
  requisito. Mostre nas configurações onde isso está.
- **Importar/exportar sem permissão de armazenamento:** exporte pela folha de
  compartilhamento do sistema e importe pelo seletor de documentos (SAF). O app nunca
  vê um arquivo que o usuário não escolheu.
- **Importar pode ser "substituir" ou "mesclar".** Mesclar só **adiciona**: trazer um
  backup velho nunca pode re-bloquear progresso nem sobrescrever configurações do
  aparelho.
- **Lembrete para exportar** quando o histórico contém corridas que só existem no
  aparelho (dispensável).
- **Backup automático:** o usuário escolhe uma pasta uma vez (que um app como Syncthing
  copie para outro aparelho); o app grava depois de cada corrida e mantém só os N mais
  recentes. O histórico sobrevive à perda do celular sem o app nunca ter acesso à rede.

---

## 4. Privacidade e superfície de ataque

- **Um trace de GPS mostra onde a pessoa mora e corre.** Trate o histórico como dado
  privado: nada que deixe outro app lê-lo, e nada que o app grave em lugar que o
  usuário não escolheu.
- **A superfície de ataque é tudo que o app lê de fora de si:** arquivos importados
  (pacotes, backups), a pasta de backup automático, notificações (inclusive **o que um
  toque devolve ao app**) e as permissões que ele tem. Valide e trate como entrada
  hostil; um parser de payload deve **retornar nulo**, não lançar exceção, para
  qualquer coisa que não reconheça (um toque não pode derrubar o app).
- **`android:allowBackup` padrão é `true`:** o backup do sistema pode copiar o log para a
  conta Google do usuário, mesmo que o app nunca faça isso. Decida de propósito: ou
  desligue (e perca a transferência entre aparelhos), ou configure
  `dataExtractionRules`/`fullBackupContent`, **e diga isso no README** em vez de
  prometer mais isolamento do que existe.
- **Confira o manifesto mesclado depois de adicionar qualquer biblioteca.** O ExoPlayer
  puxa `ACCESS_NETWORK_STATE` (só lê o estado, não dá acesso à rede), e outras bibliotecas
  declaram permissões próprias. Sem `INTERNET`, nada alcança a rede, mas o manifesto
  mesclado deve ser inspecionado, não presumido.
- **Dependência proprietária pode entrar sem você pedir.** O CI do projeto faz um build de
  release sem assinar e roda `dexdump` procurando classes em `com/google`; **falha** se
  achar. Rode essa verificação depois de **qualquer** plugin novo.
- **Poucos plugins.** O projeto reimplementou abrir links com uma linha de Kotlin
  (`ACTION_VIEW`) para não adicionar mais uma dependência a ser auditada.
- **`.gitignore`:** backups exportados (`*-backup-*.json`), chaves de assinatura e
  `key.properties` nunca vão para o repositório. Um backup é o histórico inteiro com
  trace.
- **Assinatura:** sem `key.properties` o release sai **sem assinatura** (o que o
  F-Droid precisa); assine você mesmo antes de instalar.
- **Um canal de segurança privado e um aviso de versões suportadas** (SECURITY.md): quem
  acha um problema sabe a quem avisar sem abrir issue pública.

---

## 5. Build e ambiente Android

- **JDK 17** para o AGP em certos setups; JDKs mais novos quebram a transformação
  `jdkImage`. Fixe o JDK no projeto (toolchain do Gradle).
- **NDK é obrigatório** se alguma dependência compila código nativo; sem ele o build
  quebra. Não remova `ndkVersion` por "limpeza".
- **Módulos com `compileSdk` antigo:** subir o `compileSdk` de todos os módulos evita
  ter de instalar todas as plataformas históricas do SDK. `minSdk` e `targetSdk`, que
  mudam comportamento em runtime, ficam intocados.
- **Primeiro fix mais lento e bateria um pouco pior** são o preço de usar o
  `LocationManager` direto em vez do provedor fused. Declare essa troca; o ganho é rodar
  igual em ROM sem Google.
- **Fixe e centralize a versão das ferramentas** (Flutter no `.fvmrc`; no seu caso,
  Gradle wrapper, AGP e Kotlin no `libs.versions.toml`), para o CI e a sua máquina usarem
  exatamente a mesma.
- **Rode os testes em um fuso com horário de verão** (o projeto usa Europe/Vienna) para
  que os testes de streak cruzem uma mudança de relógio real. Se o seu fuso não
  tem horário de verão, force um fuso que tenha dentro do teste.
- **Notas de versão por release:** cada versão com recurso novo adiciona suas notas, da
  mais nova para a mais antiga; quem pulou versões vê **todas** as que perdeu; mostre
  uma vez por versão e **nunca por cima de outro popup**. Um teste falha quando a versão
  sobe sem notas.

---

## 6. Interface e textos

- **Nenhum texto que o usuário lê é literal no código.** No Android nativo: tudo em
  `strings.xml` (inclusive o texto das notificações feitas em Kotlin). Plurais com
  `<plurals>`, nunca `"s"` concatenado.
- **Teste de estouro com texto 40% maior.** O projeto renderiza todas as telas em um
  pseudo-locale (inglês acentuado, 40% mais longo, entre colchetes) a 360 dp. No Android
  você tem isso pronto: `pseudoLocalesEnabled = true` no build de debug, e `en-XA` /
  `ar-XB` nas configurações de idioma. Corrija um rótulo que cresce deixando-o quebrar
  linha, reticenciar ou diminuir, **nunca encurtando o texto original**.
- **Cores como funções do tema, não constantes.** Se o tema pode mudar em runtime,
  nunca guarde uma cor em campo estático ou `const`: ela fica com a paleta antiga. Em
  Compose, leia de `MaterialTheme`/`CompositionLocal` no momento de compor.
- **Legibilidade correndo:** fonte condensada de alta densidade, números em fonte
  monoespaçada, contraste verificado por teste (o projeto tem um teste que reprova um
  tema ilegível).
- **Rolagem com fade na borda que ainda esconde conteúdo**, definido uma vez no tema do
  app, não em cada tela.
- **Voz (TTS):** use uma voz offline do idioma, se o aparelho tiver; senão, leia o texto
  original no idioma do motor e **diga na tela por quê**. O motor de TTS guarda o último
  idioma que recebeu, então volte-o ao padrão antes da próxima fala.
- **Lembrete de corrida esquecida em pausa:** avise aos 15, 30 e 60 minutos e depois
  deixe quieto. Em um canal próprio com som (o canal da corrida é silencioso de
  propósito).

---

## 7. Como trabalhar com o Claude no repositório (CLAUDE.md)

O CLAUDE.md do SPRAWL//RUN é um bom modelo. O que copiar:

- **Curto e só com o que desperdiça tempo.** O produto e a arquitetura ficam no README;
  não duplique.
- **Cada regra conta o bug real que a motivou** ("isso já foi enviado quebrado na
  0.2.1"). Uma regra com história é seguida; uma regra solta é "otimizada" por alguém.
- **Uma seção "parece errado mas não é"** para Gradle e manifesto (NDK, `compileSdk`, a
  exclusão do Play Services). Também para código: "o `RunEngine` **de propósito** não dá
  `await` nesses cancelamentos; não 'conserte'".
- **Gerado nunca é editado à mão.** Liste o que é gerado e o comando que regenera.
- **Reproduzível:** a ferramenta de screenshots fixa o relógio e usa dados semeados, então
  duas execuções produzem imagens idênticas. Tudo na tela derivado do tempo deve ler o
  relógio injetado, não `now()`.
- **Marque o que o Claude não consegue verificar** ("como uma música soa, só ouvindo").
- **Builds longos (3 a 6 min) rodam em segundo plano** com monitoramento, em vez de
  bloquear o terminal.
- **Um comando de verificação único** (analyze + testes + build) que deve continuar limpo.
- **Formulário de bug que pede o que importa para GPS:** versão do app, aparelho e sabor
  do Android, e se a tela estava apagada. É isso que separa um bug de GPS real de ruído.
  Um botão "reportar problema" dentro do app, que preenche a versão sozinho, ajuda quem
  testa para você.

---

## 8. Acréscimos ao checklist de release (parte 1, seção 8)

- [ ] uma corrida sem GPS (meta por tempo) não é congelada com a tela apagada;
- [ ] recusar `POST_NOTIFICATIONS` não interrompe o rastreamento;
- [ ] o app **não** aparece pedindo `ACTIVITY_RECOGNITION` antes de o usuário ligar passos;
- [ ] manifesto mesclado revisado: nenhuma permissão que você não pediu;
- [ ] build de release sem assinar passa na verificação de `com/google` (se for requisito);
- [ ] importar um backup antigo em modo "mesclar" não apaga nem re-bloqueia nada;
- [ ] notas da versão aparecem uma vez, e não sobre outro diálogo.
