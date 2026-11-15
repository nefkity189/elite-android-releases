# Plano de Implementação: Verificação e Solicitação de Permissões de Localização e Notificações

## Objetivo
Solicitar permissão de Localização e Notificações em nova instalação e verificar silenciosamente na inicialização do app. Caso estejam desativadas, notificar e solicitar a concessão sem quebrar o app e sem alterar funcionalidades existentes.

## Proposta de Mudanças

### [MODIFY] [MainActivity.kt](file:///D:/AISLE/AISLE_Proj/app/src/main/java/com/example/MainActivity.kt)
- Adicionar verificação e launcher de permissões (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, e `POST_NOTIFICATIONS` para Android 13+) logo na inicialização do app (`AppNavigation` ou `MainActivity`).
- Caso as permissões não estejam concedidas, exibir um aviso/diálogo educado permitindo concedê-las com um toque, sem bloquear a navegação caso o usuário decida não conceder no momento (garantindo estabilidade e funcionamento contínuo).

## Plano de Verificação
- Compilação do projeto com sucesso.
- Execução de testes unitários existentes.
- Validação visual do fluxo de solicitação de permissões.
