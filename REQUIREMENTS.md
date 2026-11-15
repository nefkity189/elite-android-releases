# REQUIREMENTS — Primeiro 5K

## 1. Requisitos Funcionais (RF)

| ID | Descrição | Status |
|---|---|---|
| **RF-01** | Tela de boas-vindas com duas frases explicativas e botão "Começar". | Implementado |
| **RF-02** | Questionário de triagem passo a passo com 3 perguntas de seleção única: tempo sem correr, frequência semanal e tipo de exercício. | Implementado |
| **RF-03** | Configuração de frequência semanal (2 a 4 dias, padrão 3) e seleção de dias preferidos da semana. | Implementado |
| **RF-04** | Seleção de data de início (padrão hoje), garantindo que o primeiro treino ocorra nessa data. | Implementado |
| **RF-05** | Seleção de duração do plano de 6 a 16 semanas (padrão 9), com a diretriz "Mais curto = mais puxado. Mais longo = mais gradual." | Implementado |
| **RF-06** | Prévia do plano apresentando todas as semanas, aviso de saúde e checkbox obrigatório "Li e entendi". | Implementado |
| **RF-07** | O botão "Iniciar plano" só é habilitado após a marcação da confirmação do aviso médico. | Implementado |
| **RF-08** | Motor `PlanGenerator` puro em Kotlin: sem chamadas de rede ou IA em tempo de execução. | Implementado |
| **RF-09** | Tabela de progressão isolada (`ProgressionTable.kt`) contendo as 9 etapas provisórias de treino. | Implementado |
| **RF-10** | Garantia matemática de que nunca haverá treinos de corrida em dias seguidos (espaçamento mínimo de 1 dia de descanso). | Implementado |
| **RF-11** | Nível de condicionamento alto (exercício 3x ou mais por semana + cardio) inicia automaticamente na etapa 2. | Implementado |
| **RF-12** | Cada treino possui 5 min de aquecimento (caminhada), blocos intervalados e 5 min de desaquecimento (caminhada). | Implementado |
| **RF-13** | Tela principal "Hoje" exibe o treino agendado para o dia com lista de passos, ou "Dia de descanso" caso não haja treino. | Implementado |
| **RF-14** | Ações de "Marcar como concluído", "Pular" e "Desfazer status" na tela principal e na lista completa. | Implementado |
| **RF-15** | Visualização do cronograma completo das semanas e progresso percentual. | Implementado |
| **RF-16** | Opção de reinício / reconfiguração do plano completo. | Implementado |

## 2. Requisitos Não Funcionais (RNF)

| ID | Descrição | Status |
|---|---|---|
| **RNF-01** | Armazenamento exclusivamente local utilizando Room (SQLite). | Implementado |
| **RNF-02** | Interface 100% nativa em Jetpack Compose com Material Design 3. | Implementado |
| **RNF-03** | Suporte a tema claro e escuro. | Implementado |
| **RNF-04** | Todos os textos no padrão Português do Brasil (pt-BR). | Implementado |
| **RNF-05** | Tratamento explícito de estados de Carregando (`LoadingState`) e Vazio (`EmptyState`) em todas as telas. | Implementado |
| **RNF-06** | Acessibilidade: áreas de toque com mínimo de 48dp x 48dp e atributos de semântica. | Implementado |
| **RNF-07** | Testes automatizados cobrindo todas as regras de negócio do gerador e ciclo de vida do app. | Implementado |
