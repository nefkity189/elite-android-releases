# SPEC - card podio (PodiumCard, kind 1)
Medidas exatas em `spec/card-podio.md`; referência `screens/card-podio.png`; marca, tokens e fontes em `../THEME.md`.

## Intenção
Posição 1 a 3. Hero: pódio de paralelogramos inclinados, base comum em y=1150; 2º 300 px, 1º 450 px, 3º 220 px de altura, largura 272; x = 72, 386, 700. O corredor leva a barra vermelha acima do nome.

## Dados
placement, runnerName, e NOVO podium: List<PodiumEntry(name, time)> dos 3 primeiros (de mission.results ordenado por finishedAt, como em publishCard). Se placement > 3, não oferecer este card.

## Estados
Menos de 3 finalistas: desenhar só os blocos existentes. Estado visto: o padrão, com dados de exemplo (nomes, tempos e Elo são ilustrativos).

## Componentes e medidas
Cores: ouro brass, prata silver, bronze bronze; numeral Barlow 190 on-olive desinclinado (contra-skew). Nome Barlow 36, tempo Barlow 54 (bone no corredor, muted nos outros). O texto de cada bloco desloca para a direita altura*0,2126 para alinhar com o topo inclinado.

## Animações
Nenhuma (imagem estática).

## Aproximações
- Palavra ELITE: no protótipo foi Barlow espaçado (a Saira Stencil não estava instalada); no app deve sair em Saira Stencil One.
- Inclinação: canvas.skew(-0.21256f, 0f) com pivô na base-esquerda; conferir que a borda esquerda dos blocos coincide com o PNG.

## Não fazer
- Não voltar ao layout único atual nem ao painel com borda arredondada.
- Não tingir a barra vermelha do símbolo; não inventar dados; sem emoji, sombra, blur ou gradiente.
- Não mudar a lógica de publishCard nem as rotas, só passar os parâmetros novos.
