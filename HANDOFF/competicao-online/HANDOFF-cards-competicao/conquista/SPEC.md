# SPEC - card conquista (AchievementCard, kind 2)
Medidas exatas em `spec/card-conquista.md`; referência `screens/card-conquista.png`; marca, tokens e fontes em `../THEME.md`.

## Intenção
Conquista desbloqueada. Hero: escudo brass (mesmo desenho de LeagueBadge) com o símbolo E em ink e chevron, e o código do estado em Barlow 330 brass.

## Dados
NOVOS: achievementTitle, achievementDescription, achievementLocal, stateCode. Hoje a lista de conquistas está vazia, então o card só abre com conquista real.

## Estados
Sem conquista: manter o texto "Nenhuma conquista confirmada". Estado visto: o padrão, com dados de exemplo (nomes, tempos e Elo são ilustrativos).

## Componentes e medidas
Escudo 470 px em (72,190), path do LeagueBadge normalizado 100x100: M50 8 L90 22 L88 55 C84 77 65 90 50 96 C34 90 15 77 12 55 L10 22 Z. Título Barlow 176 em 2 linhas inclinadas, y=770. Descrição Inter 32.

## Animações
Nenhuma (imagem estática).

## Aproximações
- Palavra ELITE: no protótipo foi Barlow espaçado (a Saira Stencil não estava instalada); no app deve sair em Saira Stencil One.
- Inclinação: canvas.skew(-0.21256f, 0f) com pivô na base-esquerda; conferir que a borda esquerda dos blocos coincide com o PNG.

## Não fazer
- Não voltar ao layout único atual nem ao painel com borda arredondada.
- Não tingir a barra vermelha do símbolo; não inventar dados; sem emoji, sombra, blur ou gradiente.
- Não mudar a lógica de publishCard nem as rotas, só passar os parâmetros novos.
