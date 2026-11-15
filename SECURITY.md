# SECURITY — Primeiro 5K

## 1. Modelo de Ameaças e Segurança

### 1.1 Vetor de Dados Pessoais e Privacidade (LGPD)
- **Zero-Cloud & Zero-Server:** O aplicativo não envia dados do usuário para a nuvem nesta fase.
- Não há coleta de identificadores de publicidade, e-mail, telefone, biometria ou geolocalização.
- Todos os registros (perfil e treinos) ficam restritos ao sandbox protegido do aplicativo Android em SQLite via Room.

### 1.2 Responsabilidade Médica e Saúde do Usuário
- Exercícios de corrida impõem estresse cardiovascular e musculoesquelético.
- O aplicativo implementa trava de segurança no fluxo de onboarding: o botão "Iniciar plano" permanece desabilitado até que o usuário marque obrigatoriamente a caixa:
  > *"Li e entendi o aviso de saúde: Este app não substitui orientação médica. Se tiver dúvida sobre sua saúde, consulte um médico antes de começar."*

### 1.3 Integridade do Código
- Sem Dynamic Code Loading (DCL).
- Sem dependência de serviços externos não auditados.
- Chaves sensíveis ou credenciais não são embutidas em código-fonte.
