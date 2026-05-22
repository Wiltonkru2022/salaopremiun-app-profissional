# API Oracle VPS para o App Profissional

Esta documentação define o contrato esperado pelo app Android nativo.

## Regras gerais

- Todas as rotas privadas exigem JWT.
- O backend extrai `profissional_id` e `salao_id` do token.
- O app nunca envia `salao_id` como fonte de verdade.
- O backend valida permissões por salão e profissional.
- Respostas devem ser pequenas e paginadas.
- Evitar consultas pesadas no Supabase.
- Usar logs estruturados, rate limit e sanitização de entrada.

## Reserva temporária de horário

Fluxo:

1. App chama `POST /api/profissional/agenda/reservas`.
2. API valida expediente, pausa e conflito.
3. API cria reserva temporária com expiração.
4. Outro usuário não consegue reservar o mesmo horário.
5. Se confirmar, a reserva vira agendamento.
6. Se trocar horário, o app cancela a reserva antiga.
7. Se abandonar, a API expira automaticamente.

Payload sugerido:

```json
{
  "clienteId": "cli_123",
  "servicoId": "srv_123",
  "data": "2026-05-22",
  "horario": "14:00"
}
```

Resposta:

```json
{
  "id": "res_123",
  "expiresAt": "2026-05-22T17:10:00Z"
}
```

## Índices recomendados no PostgreSQL

```sql
create index if not exists idx_agendamentos_salao_data on agendamentos (salao_id, data);
create index if not exists idx_agendamentos_profissional_data on agendamentos (profissional_id, data);
create index if not exists idx_agendamentos_status on agendamentos (status);
create index if not exists idx_clientes_salao_nome on clientes (salao_id, nome);
create index if not exists idx_clientes_salao_whatsapp on clientes (salao_id, whatsapp);
create index if not exists idx_comandas_salao_status on comandas (salao_id, status);
create index if not exists idx_comissoes_profissional_status on comissoes (profissional_id, status);
create index if not exists idx_notificacoes_profissional_created_at on notificacoes (profissional_id, created_at desc);
```

## Estrutura sugerida da API Node.js

```text
src/
  auth/
  controllers/
  middlewares/
  repositories/
  routes/
  services/
  validations/
  logs/
  config/
```

## Variáveis de ambiente da API

```env
PORT=3001
JWT_SECRET=
JWT_REFRESH_SECRET=
DATABASE_URL=
SUPABASE_URL=
SUPABASE_SERVICE_ROLE_KEY=
FCM_SERVER_KEY=
RATE_LIMIT_WINDOW_MS=60000
RATE_LIMIT_MAX=120
```

Nunca enviar essas variáveis para o app Android.
