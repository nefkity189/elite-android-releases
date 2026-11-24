# SPEC - card ranking (RankingCard, kind 3)
Medidas exatas em `spec/card-ranking.md`; referência `screens/card-ranking.png`; marca, tokens e fontes em `../THEME.md`.

## Intenção
Posição na temporada. Hero: "#12" (Barlow 420 olive) com Elo ao lado e a lista com anterior, você e seguinte em barras inclinadas.

## Dados
NOVO rankPosition (hoje placement é da missão, não do ranking), elo, runnerName, state, neighbors (anterior e seguinte de state.onlineRanking).

## Estados
Sem rankPosition: não gerar o card. Estado visto: o padrão, com dados de exemplo (nomes, tempos e Elo são ilustrativos).

## Componentes e medidas
Linhas 900 px de largura inclinadas: 96 px (elev), a sua 128 px (olive, texto on-olive), x=72, y=790/898/1038. Posição Barlow 44/50, nome Inter 36, elo Barlow 48/58.

## Animações
Nenhuma (imagem estática).

## Aproximações
- Palavra ELITE: no protótipo foi Barlow espaçado (a Saira Stencil não estava instalada); no app deve sair em Saira Stencil One.
- Inclinação: canvas.skew(-0.21256f, 0f) com pivô na base-esquerda; conferir que a borda esquerda dos blocos coincide com o PNG.

## Não fazer
- Não voltar ao layout único atual nem ao painel com borda arredondada.
- Não tingir a barra vermelha do símbolo; não inventar dados; sem emoji, sombra, blur ou gradiente.
- Não mudar a lógica de publishCard nem as rotas, só passar os parâmetros novos.
