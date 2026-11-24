# HANDOFF - Progresso -> Detalhes da Corrida -> Compartilhar (v2)

Substitui o pacote v1 (uma pasta só, com lacunas). Agora são **duas pastas**, uma por tela, mais o tema compartilhado.

```
HANDOFF/
  LEIA-ME.md                      <- este arquivo (ordem e como usar)
  THEME.md                        <- tokens, fontes, ícones, dispositivo (vale para as duas)
  01-progresso-seta/              <- ABA PROGRESSO: a seta/card de cada corrida abre os Detalhes
      SPEC.md  PROMPT-CLAUDE-CODE.md  screen.html  states.json  screens/  spec/  .claude/skills/
  02-detalhes-compartilhar/       <- DETALHES DA CORRIDA: botão compartilhar no topo + cards novos
      SPEC.md  PROMPT-CLAUDE-CODE.md  screen.html  states.json  screens/  spec/  .claude/skills/
```

## O que foi pedido
1. Em **Detalhes da Corrida**: botão de **compartilhar no topo, à direita do título**, nas 4 modalidades (livre, 2,5K, 5K, 10K).
2. Na aba **Progresso**: a **setinha** de cada corrida **abre os Detalhes daquela corrida**; lá aparecem **um card com todos os dados** e **um card de compartilhamento**.

## O que mudou em relação ao v1 (lacunas fechadas)
- **Progresso virou pasta própria** e a seção 0 do SPEC explica que é a **4ª aba da barra inferior** (a contornada de vermelho no print), não o "Hoje".
- O v1 desenhava a barra inferior como um retângulo vazio; agora está desenhada **como a real** (flutuante 64 dp, "Progresso" selecionado em oliva, botão E no centro).
- O SPEC 01 mostra **por que a seta não funcionava** (ícone sem clique; o card abria o diálogo antigo `RunDetailDialog`, nunca a tela de Detalhes) e traz o **código Kotlin exato** das 6 mudanças, com os arquivos e linhas lidos do seu `src.zip`.
- Fluxo, voltar, rolagem preservada e critérios de teste nas 4 modalidades estão escritos passo a passo.
- Um prompt por pasta, sem placeholders.

## Ordem de trabalho (importa)
1. **02-detalhes-compartilhar** primeiro: é o destino. Sem ele a seta não teria o que abrir.
2. **01-progresso-seta** depois: liga a aba Progresso nesse destino.
3. Teste final (as duas juntas) nas 4 modalidades: lista de verificação na seção "Aceitação" do SPEC 01.

## Como usar (3 passos)
1. Descompacte `HANDOFF/` na raiz do projeto (ao lado de `app/`).
2. Abra o Claude Code na raiz do projeto.
3. Cole o conteúdo de `02-detalhes-compartilhar/PROMPT-CLAUDE-CODE.md`; quando terminar e aprovar, cole `01-progresso-seta/PROMPT-CLAUDE-CODE.md`.

As fontes **já estão no projeto** (`app/src/main/res/font`: Barlow Condensed, Inter, Saira Stencil One). Nada para baixar.
Obs.: o `src/` do seu zip corresponde a `app/src/` do projeto.
