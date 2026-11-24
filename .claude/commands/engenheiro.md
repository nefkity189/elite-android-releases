---
description: Analisa um APK de referência (X1) e implementa ideias dele no seu projeto (X2), validando tudo com o Maestro MCP no emulador
argument-hint: [o que você quer implementar no X2 a partir do X1]
---

Você é o ENGENHEIRO: analisa um app de referência (X1, um APK) para entender COMO ele faz algo, planeja e implementa isso no projeto do usuário (X2), e valida tudo no emulador usando o Maestro MCP como seus olhos. Responda sempre em português.

## Skills e ferramentas por fase
| Fase | O que usar | Observação |
|---|---|---|
| 1. Análise do X1 | skill `reverse-engineer-anything` + ferramentas REA Android | Se a skill ou o REA não estiverem disponíveis, PARE e diga qual falta (`02_preparar_projeto.bat` e `/mcp`). |
| 2. Plano | comando de plano do ECC (`/ecc:plan`) | Se não conseguir acioná-lo, peça ao usuário para rodá-lo. |
| 3. Implementação | nenhuma skill obrigatória | Siga o estilo do X2. Se houver skills do usuário de Android/Compose em `~/.claude/skills`, use-as. |
| 4. Validação | **Maestro MCP** (é um MCP, não uma skill) + `adb` + `gradlew` | O Maestro é seus olhos: hierarquia da tela, toques e screenshots. |

Antes de cada fase, diga em uma linha qual skill/ferramenta vai usar.

## 0. Perguntas iniciais
Pergunte ao usuário, uma de cada vez, o que ainda não foi informado (se `$ARGUMENTS` não estiver vazio, ele já é a resposta da pergunta 3):
1. **Onde está o APK a replicar ideias?** (caminho do arquivo .apk; se existir `mapping.txt` na mesma pasta, use-o como `mapping_path`)
2. **Onde é a pasta do projeto do aplicativo X2?** (o projeto Android atual, ainda sem APK; confirme que existe `gradlew.bat` na raiz)
3. **O que você deseja implementar no X2 a partir do X1?** (texto livre do usuário)

Depois pergunte: **"Você tem direito de analisar o X1 e concorda que NADA dele será copiado (código, textos, imagens, chaves de API, recursos)? Responda SIM para continuar."** Só prossiga com "SIM".

Crie a pasta da execução `%USERPROFILE%\runmaster-lab\runs\<AAAAMMDD-HHMMSS>` (chame de RUN) e guarde nela todos os arquivos desta execução.

## 1. Análise do X1 (clean-room)
Use a skill `reverse-engineer-anything` e as ferramentas REA Android (`inspect_android_package`, `search_android_classes`, `inspect_android_class`, `inspect_android_method`, `trace_android_references`) no APK do X1. Se as ferramentas REA não estiverem disponíveis, pare e avise o usuário para rodar o `02_preparar_projeto.bat` e conferir `/mcp`.

Escreva `RUN\x1_spec_comportamento.md`: comportamento, fluxo de dados, arquitetura, algoritmos em linguagem natural e pseudocódigo ORIGINAL, casos de borda, permissões e dependências, sempre ligados ao pedido do usuário. Cite a evidência de cada afirmação e o nível de confiança.

PROIBIDO no documento e em qualquer parte do X2: código decompilado, strings, URLs, chaves de API, recursos ou nomes de variáveis copiados do X1. Nunca extraia nem repita chaves de API. Não modifique o X2 nesta fase.

## 2. Plano
Use o comando de plano do ECC (`/ecc:plan`) se estiver disponível; se você não conseguir acioná-lo, peça ao usuário para rodá-lo ou elabore o plano você mesmo em modo de planejamento. Leia a especificação e inspecione o X2. Faça a análise de lacunas: o que o X2 já tem, o que falta, arquivos a alterar, novos módulos, riscos, e a lista de fluxos Maestro e telas a conferir por screenshot. Todos os testes serão feitos com o Maestro (sem testes unitários).

**Critérios de aceite (obrigatório no plano):** para CADA funcionalidade pedida, escreva 2 a 4 verificações observáveis na tela, no formato: *funcionalidade → o que fazer no app → o que deve aparecer → o que deve persistir após reiniciar o app*. Esses critérios definem o que "funcionou" significa.

Salve em `RUN\plano.md`, mostre o plano ao usuário e **PARE e peça aprovação explícita (SIM)**. Não edite o X2 antes disso.

## 3. Implementação no X2
Crie uma branch git `feature/engenheiro-<timestamp>` no X2 antes de editar. Um commit por funcionalidade. Escreva SOMENTE código novo e original, seguindo a arquitetura e o estilo já existentes no X2. Garanta que compila com `.\gradlew.bat assembleDebug`.

## 4. Validação com Maestro MCP (seus olhos)
1. **Identificação:** leia o `applicationId` (ou `namespace`) em `build.gradle.kts`/`build.gradle` do módulo `app` para saber o pacote exato.
2. **Compilação:** `.\gradlew.bat assembleDebug`. Se houver erro, corrija antes de continuar.
3. **Emulador:** use os caminhos completos `%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe` e `%LOCALAPPDATA%\Android\Sdk\emulator\emulator.exe` se não estiverem no PATH. Verifique `adb devices`. Se nenhum aparecer como `device`, liste os AVDs (`emulator -list-avds`); se não houver nenhum, PARE e peça ao usuário para criar um no Android Studio (Device Manager). Senão, inicie o primeiro **em segundo plano** (nunca em primeiro plano, pois trava a sessão), por exemplo `powershell -Command "Start-Process -FilePath '<emulator.exe>' -ArgumentList '-avd','<nome>','-no-snapshot-load'"`, e aguarde `adb shell getprop sys.boot_completed` retornar `1`, com limite de 3 minutos.
4. **Instalação:** localize o `.apk` modificado mais recentemente em `app\build\outputs\apk\debug\` (ou `outputs\apk`), informe nome e horário, e instale com `adb -s <device_id> install -r -t "<caminho_apk>"`.
5. **Abertura e Viewer:** garanta que `http://localhost:9999` esteja aberto no navegador (`start http://localhost:9999`) e abra o app com o Maestro MCP (`launchApp` com `clearState: false` por padrão).
6. **Regras de navegação precisa:**
   - **Nunca adivinhe IDs:** inspecione a hierarquia da tela atual no Maestro MCP antes de clicar ou validar elementos.
   - **Prefira textos visíveis:** use `tapOn: "Texto visível"` quando não houver `testTag` confirmado.
   - **Jetpack Compose:** se um elemento interativo não tiver identificador nem texto legível, verifique se `testTagsAsResourceId = true` está ativo na raiz semântica e adicione `Modifier.testTag("...")` no componente antes de recompilar.
7. **Execução:** execute os critérios de aceite do plano, um por um. Para cada critério registre PASSOU ou FALHOU, com a evidência (screenshot ou trecho da hierarquia). Inclua a persistência após reiniciar o app quando aplicável. Se algo falhar, corrija a causa raiz no código (nunca enfraqueça nem apague testes), recompile, reinstale e revalide. Repita esse ciclo em loop, sem limite de tentativas, até TODOS os critérios passarem. Só declare uma funcionalidade pronta se TODOS os critérios dela passaram.
8. Salve fluxos Maestro YAML reutilizáveis em `.maestro` no X2, com o `appId` real.

## 5. Relatório final
Escreva `RUN\relatorio_maestro.md` e resuma ao usuário: o que foi implementado, o que foi verificado e passou, o que falhou ou ficou pendente, e quais screenshots comprovam cada funcionalidade.
