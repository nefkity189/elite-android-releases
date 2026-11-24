Pasta do site: `site-divulgacao/` (contém `index.html`, `HANDOFF.md`, `states.json`, `fonts/`, `assets/`).

Tarefa: publicar este site no Firebase Hosting do meu projeto, com o APK para download direto.

1. Leia o `HANDOFF.md` inteiro, principalmente as seções 9 (Mudanças) e 10 (Armadilhas). Não altere o design.
2. Rode `firebase init hosting` apontando a pasta pública para `site-divulgacao/` e crie o `firebase.json` com os cabeçalhos do APK.
3. Tudo tem que ser gratuito: plano Spark, sem Blaze, sem Functions, sem Cloud Storage. O APK vai em `downloads/elite.apk` no Hosting.
4. Configure o Firestore com as regras da seção 9 do HANDOFF, preencha `REL` (`preview:false`, `firebase`, `dailyMax`, versão, tamanho), remova o bloco `dev.preview` e o link do Google Fonts.
5. Renderize em 390x844 e 1280x900, abra os PNGs e confira cada estado de `states.json`, em especial `download_limite`.
6. Faça `firebase deploy --only hosting,firestore:rules`, teste o download em um Android real e me entregue o relatório com prints e o que não conseguiu verificar.
