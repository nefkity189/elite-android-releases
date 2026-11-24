# SPEC - card sem-app (NoAppInviteCard, kind 5)
Medidas exatas em `spec/card-sem-app.md`; referência `screens/card-sem-app.png`; marca, tokens e fontes em `../THEME.md`.

## Intenção
Convite para quem não tem o app. Hero: "VEM PARA O ELITE" (Barlow 200 inclinado, segunda linha olive) e o corredor da splash grande à direita.

## Dados
distance e missionTime (já existem). Link da loja vai no texto do compartilhamento (EXTRA_TEXT), não na imagem.

## Estados
Sem missionTime: frase sem horário. Estado visto: o padrão, com dados de exemplo (nomes, tempos e Elo são ilustrativos).

## Componentes e medidas
3 passos (sequência real) com número Barlow 76 olive e texto Inter 36, linhas de 2 px de 560 px, y=740/850/960. Botão contornado 900x96, borda bone 3 inclinada, y=1090. Corredor com 400 px de altura em (650,660).

## Animações
Nenhuma (imagem estática).

## Aproximações
- Palavra ELITE: no protótipo foi Barlow espaçado (a Saira Stencil não estava instalada); no app deve sair em Saira Stencil One.
- Inclinação: canvas.skew(-0.21256f, 0f) com pivô na base-esquerda; conferir que a borda esquerda dos blocos coincide com o PNG.

## Não fazer
- Não voltar ao layout único atual nem ao painel com borda arredondada.
- Não tingir a barra vermelha do símbolo; não inventar dados; sem emoji, sombra, blur ou gradiente.
- Não mudar a lógica de publishCard nem as rotas, só passar os parâmetros novos.
