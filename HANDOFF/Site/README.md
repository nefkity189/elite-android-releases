# Site do ELITE

## Arquitetura atual

- Site estático responsivo em `site/`, publicado em Firebase Hosting no projeto `aisler-runner` (plano Spark).
- O APK não fica no Firebase. O Spark bloqueia APK no Hosting e no Cloud Storage; o arquivo oficial fica no repositório público [elite-android-releases](https://github.com/nefkity189/elite-android-releases).
- O site consulta a API pública de Releases do GitHub para exibir a versão e escolher o APK mais recente. `site/latest-release.json` é o fallback offline e deve ser atualizado junto de cada release.
- O contador Firestore `downloads/{AAAA-MM-DD}` limita a quatro cliques do botão por dia no fuso de São Paulo. É uma cota leve: links diretos do GitHub e clientes fabricados podem contorná-la.
- O site não usa Cloud Functions nem Cloud Storage. O Firebase Hosting serve somente HTML, fontes e ícone; o tráfego do APK fica no GitHub.

## Arquivos

- `site/index.html`: página pública, conteúdo e comportamento do botão.
- `site/latest-release.json`: metadados de fallback do release atual.
- `site/privacidade.html` e `site/termos.html`: páginas acessíveis no rodapé.
- `site/fonts/` e `site/assets/`: Barlow Condensed do app, Inter variável e Saira Stencil One de `google/fonts`, respectivos arquivos OFL e ícone do app. As fontes servidas devem começar com uma assinatura TrueType válida; não copie os `.ttf` de Inter/Saira do Android, pois os arquivos locais do app estão inválidos para navegador.
- `firebase.json`: raiz pública, cabeçalhos e exclusões de arquivos internos/APKs.
- `firestore.rules`: inclui as regras do contador diário e as regras existentes do app.

## Publicar uma nova versão

1. Gere o APK release com a chave de assinatura original e aumente `versionCode` e `versionName` em `app/build.gradle.kts`.
2. Crie um Release no GitHub com a tag `v<versionName>` e anexe o APK nomeado exatamente `elite.apk`.
3. Confira que o APK novo usa o mesmo certificado de assinatura da versão instalada. O Android não atualiza um pacote assinado por outra chave; nesse caso seria necessário desinstalar e instalar novamente, perdendo os dados locais.
4. Atualize `site/latest-release.json` com versão, código, tamanho, SHA-256, URL do APK e URL da página do Release. O site tenta a API do GitHub primeiro e usa esse JSON se a API falhar.
5. Publique o site com `firebase deploy --project aisler-runner --only hosting`. A versão do app será detectada pela página quando a API do GitHub estiver disponível; o JSON mantém o fallback.
6. O app consulta o Release no GitHub na abertura e no máximo uma vez a cada seis horas por instalação. Para instalações fora da Play Store, o Android sempre exige confirmação da pessoa para instalar.

## Regras e limites

- Não coloque APK dentro de `site/`: o Firebase Spark bloqueia executáveis `.apk` no Hosting. `firebase.json` também ignora `**/*.apk` como proteção adicional.
- O contador em `/downloads` é público e serve apenas para reduzir cliques pelo site. Não é uma proteção de banda ou mecanismo antifraude, porque o download é servido pelo GitHub.
- O Firestore deve continuar no Spark. Não habilite Blaze, Cloud Functions ou Cloud Storage para esta arquitetura.
- Confira as quotas do Hosting no Console Firebase. O Hosting gratuito tem cotas de armazenamento e transferência; se a transferência mensal acabar, o site pode ser desativado até o próximo ciclo. O APK não consome a cota do Hosting, pois vem do GitHub.
- A página de ranking contém dados explicitamente ilustrativos; não remova o aviso sem conectar dados reais.
- As fontes estão locais. Não adicionar Google Fonts externo, para manter a página funcional e evitar solicitações extras.

## Teste visual e publicação

Renderize a página em 390x844 e 1280x900, verifique overflow horizontal e o estado de limite do botão. Após publicar, confira `https://aisler-runner.web.app`, os links do rodapé e se o botão aponta para `elite.apk` no Release mais recente.
