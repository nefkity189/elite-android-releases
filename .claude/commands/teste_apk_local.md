---
description: Compila o APK debug e executa testes UI via Maestro MCP diretamente em um celular físico conectado via USB.
argument-hint: "[funcionalidade ou tela a testar, ex: Excluir Plano]"
---

Execute o ciclo completo de build, instalação e teste visual diretamente no **celular físico conectado via USB**, considerando o objetivo informado ($ARGUMENTS):

1. **Detecção do Celular USB (Obrigatório):**
   - Execute `adb devices`.
   - Filtre apenas dispositivos físicos conectados via USB com status `device` (ignore qualquer entrada que comece com `emulator-` ou que esteja `offline`/`unauthorized`).
   - Se nenhum celular físico estiver conectado, pare imediatamente e peça para o usuário conectar o cabo USB e ativar a **Depuração USB** nas Opções do Desenvolvedor (ou autorizar a chave RSA na tela do aparelho).
   - Guarde o `<serial_usb>` exato do celular físico para usar em todos os comandos seguintes.

2. **Compilação do Projeto:**
   - Execute `.\gradlew.bat assembleDebug` na raiz do projeto. Se houver erro de compilação, corrija antes de seguir.

3. **Instalação no Celular Físico:**
   - Localize o `.apk` modificado mais recentemente em `app\build\outputs\apk\debug\` (ou `outputs\apk`), informe o nome/horário e instale diretamente no celular físico usando `adb -s <serial_usb> install -r -t "<caminho_apk>"`.

4. **Abertura e Viewer:**
   - Leia o `applicationId` (ou `namespace`) em `app/build/gradle.kts` ou `app/build/gradle`.
   - Garanta que `http://localhost:9999` esteja aberto no navegador e abra o app no celular físico usando o Maestro MCP (`launchApp` apontando para o `<serial_usb>` e com `clearState: false` por padrão).

5. **Regras de Navegação Precisa (Maestro MCP):**
   - **Nunca adivinhe IDs:** Inspecione a hierarquia da tela atual do celular via Maestro MCP antes de cada clique ou validação.
   - **Prefira textos visíveis:** Use `tapOn: "Texto exato da tela"` sempre que o componente não tiver um `testTag` confirmado.
   - **Suporte a Jetpack Compose:** Caso um botão não tenha texto nem identificador visível na hierarquia, garanta que `testTagsAsResourceId = true` esteja ativo na raiz semântica do Compose e adicione `Modifier.testTag("...")` antes de recompilar.

6. **Execução do Teste:**
   - Valide o fluxo pedido em "$ARGUMENTS" diretamente na tela do celular (incluindo persistência após reiniciar o app, se aplicável) e corrija qualquer bug encontrado no código.
