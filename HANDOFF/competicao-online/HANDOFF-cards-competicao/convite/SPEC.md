# SPEC - card convite (InviteCard, kind 4)
Medidas exatas em `spec/card-convite.md`; referência `screens/card-convite.png`; marca, tokens e fontes em `../THEME.md`.

## Intenção
Convite para a próxima prova. Hero: a distância ("5K", Barlow 800, olive) que sangra pela borda direita (cortada pelo Bitmap de propósito).

## Dados
distance, missionTime, participants (já existem). Faixas: 10 paralelogramos 64x26, preenchidos = min(participants,10).

## Estados
participants nulo ou 0: faixas vazias e texto "Primeira vaga disponível"; sem missionTime: ocultar a linha de largada. Estado visto: o padrão, com dados de exemplo (nomes, tempos e Elo são ilustrativos).

## Componentes e medidas
Barra vermelha 300 px em y=846. "LARGADA 13:00" Barlow 116 inclinado. Botão visual 900x96 olive inclinado, texto Inter SemiBold 38 on-olive, em y=1100.

## Animações
Nenhuma (imagem estática).

## Aproximações
- Palavra ELITE: no protótipo foi Barlow espaçado (a Saira Stencil não estava instalada); no app deve sair em Saira Stencil One.
- Inclinação: canvas.skew(-0.21256f, 0f) com pivô na base-esquerda; conferir que a borda esquerda dos blocos coincide com o PNG.

## Não fazer
- Não voltar ao layout único atual nem ao painel com borda arredondada.
- Não tingir a barra vermelha do símbolo; não inventar dados; sem emoji, sombra, blur ou gradiente.
- Não mudar a lógica de publishCard nem as rotas, só passar os parâmetros novos.
