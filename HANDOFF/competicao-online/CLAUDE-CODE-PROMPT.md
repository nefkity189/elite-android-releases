Use a skill **html-to-compose**.

Pasta: `competicao-online/` (10 subpastas, uma por tela; leia `LEIAME.md` antes). Projeto Android: `<CAMINHO_DO_PROJETO>`.

Implemente uma tela por vez, na ordem do LEIAME (começando pela base compartilhada descrita na tabela Mudanças de `01-hub/HANDOFF.md`). Para cada tela:
1. Rode o preflight da skill e me avise se faltar algo (Chromium, emulador, fontes).
2. Renderize `<pasta>/screen.html` com `--fonts-dir app/src/main/res/font`, abra os PNGs e meça com `inspect_dom.py`.
3. Siga a tabela **Mudanças** e a tabela **Motion** do `HANDOFF.md` da pasta.
4. Compile, rode no emulador (393dp), capture cada estado de `states.json`, compare com `diff_images.py` e `compare_bounds.py` e repita até a seção **Aceitação**. Teste também 360dp e 412dp com fontScale 1.3.
5. Entregue o relatório da tela (prints antes/depois, números, animações, desvios) e só então passe para a próxima.

Não altere o design nem adicione elementos que não estejam no HTML. Se algo estiver ambíguo (ver **Perguntas em aberto** de cada HANDOFF), pergunte antes de codar.
