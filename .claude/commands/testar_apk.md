---
description: Compila o APK debug mais recente, inicia o emulador Android, instala o app e executa testes UI via Maestro MCP.
argument-hint: "[funcionalidade ou tela a testar, ex: Excluir Plano]"
---

Execute o ciclo completo de build, instalação e teste visual no emulador Android para o projeto Aisle:

1. **Compilação:** Execute `.\gradlew.bat assembleDebug` na raiz do projeto. Se houver erro de compilação, pare e me mostre o erro para corrigirmos.
2. **Emulador Android:** Verifique `adb devices`. Se nenhum emulador estiver ativo com status `device`, liste os AVDs disponíveis (`emulator -list-avds`), inicie o emulador em segundo plano com `-no-snapshot-load` e aguarde até `adb shell getprop sys.boot_completed` retornar `1`.
3. **Instalação do APK mais recente:** Localize o arquivo `.apk` modificado mais recentemente em `app\build\outputs\apk\debug\`, mostre o nome e a data/hora dele e instale no emulador com `adb install -r -t "<caminho_do_apk>"`.
4. **Viewer e Abertura do App:** Garanta que o Maestro Viewer (`http://localhost:9999`) esteja aberto no navegador (usando `start http://localhost:9999` se necessário). Leia o `applicationId` em `app/build/gradle.kts` e abra o app usando o Maestro MCP (`launchApp`).
5. **Execução do Teste:**
   - Se eu informei uma funcionalidade ou tela em `$ARGUMENTS`, inspecione a interface via Maestro MCP, crie ou execute o fluxo de teste correspondente para validar `$ARGUMENTS` (incluindo persistência após reiniciar o app, se aplicável).
   - Se `$ARGUMENTS` estiver vazio, inspecione a tela inicial e valide o fluxo básico de navegação.
   - Se algum botão falhar ou o comportamento estiver incorreto, investigue o código Kotlin/Jetpack Compose e proponha a correção.
