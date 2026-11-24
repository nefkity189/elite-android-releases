Use a skill **html-to-compose**.

Pasta do layout: `login/` (contém `screen.html`, `HANDOFF.md`, `states.json`, `ic_login_topo.xml`, `ic_google_g.xml`).
Projeto Android: o app `com.elite.sch.app` aberto nesta pasta. Tela(s): `LoginScreen` (tela de login depois do splash, com folha de e-mail).

1. Rode o preflight da skill e me diga se faltar algo (Chromium, emulador, fontes). Não escreva código antes de resolver.
2. Renderize o HTML com `--fonts-dir` apontando para `res/font` do app (`app/src/main/res/font` ou `src/main/res/font`) e **abra os PNGs**; meça com `inspect_dom.py`.
3. Siga a tabela **Mudanças** do HANDOFF.md na ordem. Animações: tabela **Motion**, linha por linha.
4. Compile, rode no emulador (393dp), capture, compare com `diff_images.py` e `compare_bounds.py`; repita até a seção **Aceitação**.
5. Entregue o relatório com prints antes/depois, números do diff, checklist de animações e **desvios conhecidos**.

Três pontos que não podem falhar (detalhes em **Armadilhas**): a flag "login dispensado" fica em `LoginPrefs` próprio, nunca no `AccountStore` (o `save(null)` faz `prefs.clear()`); o splash só termina depois de `restoreFinished` (teto de 4 s); a `LoginScreen` fica entre o `AppNavigation` e o `EliteSplash`, consumindo toques.

Não altere o design nem adicione elementos que não estejam no HTML. Se algo estiver ambíguo ou faltar arquivo (fonte, ícone), pergunte antes de codar.
