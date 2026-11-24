Use a skill **html-to-compose**.

Pasta do layout: `perfil-conta/` (contém `screen.html`, `HANDOFF.md`, `states.json`).
Projeto Android: `<CAMINHO_DO_PROJETO>`. Tela: **Perfil** (`ProfileScreen.kt`), nova seção **Conta** com login.

1. Rode o preflight da skill e me diga se faltar algo (Chromium, emulador, fontes). Não escreva código antes de resolver.
2. Leia o `HANDOFF.md` inteiro. Esta entrega **não liga o Firebase**: ela cria a base da conta com um repositório de exemplo (`FakeAccountRepository`, flag `USE_FAKE_ACCOUNT = true`). Não adicione o plugin `google-services`, o SDK do Firebase, a permissão `INTERNET` nem nenhum código de rede.
3. Renderize o HTML com `--fonts-dir app/src/main/res/font` e **abra os PNGs**; meça com `inspect_dom.py`.
4. Siga a tabela **Mudanças** na ordem (linhas "Fase 1"). As linhas "Fase 2" são só registro: não implemente.
5. Compile, rode no emulador (393dp), capture cada estado de `states.json`, compare com `diff_images.py` e `compare_bounds.py` e repita até a seção **Aceitação**. Teste também 360dp e 412dp com `fontScale` 1.3. Exclua do diff a barra inferior (`profile.nav`, já existente).
6. Rode os testes unitários novos e os existentes (`./gradlew testDebugUnitTest`). Nenhum teste antigo pode quebrar.
7. Entregue o relatório com prints antes/depois, números do diff, checklist de animações e **desvios conhecidos**.

Não altere o design nem adicione elementos que não estejam no HTML. Se algo estiver ambíguo (veja **Perguntas em aberto**), pergunte antes de codar.
