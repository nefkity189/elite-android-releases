# SPEC - card participacao (ParticipationCard, kind 0)
Medidas exatas em `spec/card-participacao.md`; referência `screens/card-participacao.png`; marca, tokens e fontes em `../THEME.md`.

## Intenção
O corredor mostra que cruzou a linha de chegada. Hero: tempo oficial (Barlow 420, inclinado). O corredor da splash está parado sobre a barra de progresso cheia, igual ao carregamento da splash chegando a 100%.

## Dados
runnerName, resultTime, distance, placement, participants (já existem). Ritmo por km = timeSec / (distance.meters/1000) em m:ss.

## Estados
Sem resultTime: sem hero, mostrar "Resultado ainda não confirmado" e esconder estatísticas. Estado visto: o padrão, com dados de exemplo (nomes, tempos e Elo são ilustrativos).

## Componentes e medidas
Barra 936x4 em y=1008 (elev) com preenchimento olive total; marcador de chegada bone r=14 na ponta. Estatísticas em 3 colunas (Barlow 84 + Inter 26 muted) em y=1060. Marca d'água do E em (430,300), 760 px.

## Animações
Nenhuma (imagem estática).

## Aproximações
- Palavra ELITE: no protótipo foi Barlow espaçado (a Saira Stencil não estava instalada); no app deve sair em Saira Stencil One.
- Inclinação: canvas.skew(-0.21256f, 0f) com pivô na base-esquerda; conferir que a borda esquerda dos blocos coincide com o PNG.

## Não fazer
- Não voltar ao layout único atual nem ao painel com borda arredondada.
- Não tingir a barra vermelha do símbolo; não inventar dados; sem emoji, sombra, blur ou gradiente.
- Não mudar a lógica de publishCard nem as rotas, só passar os parâmetros novos.
