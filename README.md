# App Profissional SalaoPremiun

Aplicativo Android nativo para profissionais de salão, feito em Kotlin + Jetpack Compose.

## Arquitetura

Fluxo esperado:

```text
Android nativo
-> API REST na Oracle VPS
-> JWT e permissões no backend
-> Supabase PostgreSQL acessado somente pela API
```

O app não usa WebView, não abre site dentro do Android e não deve consultar Supabase diretamente.

## Camadas

- `core`: configuração de rede, sessão com DataStore e modelos utilitários.
- `domain`: modelos do negócio e contratos.
- `data`: Retrofit, repositórios, cache local preparado e FCM.
- `presentation`: ViewModel, navegação, componentes reutilizáveis e telas Compose.

## Dependências Android

- Jetpack Compose + Material 3.
- Navigation Compose.
- ViewModel + StateFlow.
- Retrofit + OkHttp.
- DataStore para sessão.
- Room runtime para cache offline.
- Firebase Messaging para push.
- Coil para imagens.

Observação: este projeto usa o Kotlin integrado do AGP 9.2. Por isso, o processador do Room ainda não foi ativado com `kapt`. A estrutura de cache está preparada e o próximo passo técnico é migrar para KSP compatível ou Kotlin plugin tradicional antes de gerar DAOs reais.

## Configuração da API Oracle

Arquivo:

```text
app/src/main/java/br/com/salaopremiun/profissional/core/network/OracleApiConfig.kt
```

Campos:

```kotlin
const val BASE_URL = "https://api.salaopremiun.com.br/"
const val MOCK_MODE = true
```

Para usar a API real:

1. Configure `BASE_URL` com o domínio da Oracle VPS.
2. Troque `MOCK_MODE` para `false`.
3. Garanta HTTPS válido.
4. Mantenha JWT obrigatório em todos os endpoints privados.

## Segurança

- Nunca colocar `service_role` ou chave secreta do Supabase no app.
- O app envia apenas JWT para a API Oracle.
- A API valida `profissional_id` e `salao_id` pelo token.
- O servidor valida conflitos de agenda, permissões, comandas e clientes.
- O app bloqueia ações críticas quando estiver offline.

## Endpoints necessários na API Oracle

### Auth

- `POST /api/profissional/auth/login`
- `POST /api/profissional/auth/logout`
- `POST /api/profissional/auth/refresh`
- `GET /api/profissional/me`

### Dashboard

- `GET /api/profissional/dashboard`

### Agenda

- `GET /api/profissional/agenda?data=YYYY-MM-DD`
- `GET /api/profissional/agenda/mes?mes=YYYY-MM`
- `POST /api/profissional/agenda/reservas`
- `DELETE /api/profissional/agenda/reservas/{id}`
- `POST /api/profissional/agendamentos`
- `GET /api/profissional/agendamentos/{id}`
- `PATCH /api/profissional/agendamentos/{id}`
- `POST /api/profissional/agendamentos/{id}/cancelar`
- `POST /api/profissional/agendamentos/{id}/status`

### Clientes

- `GET /api/profissional/clientes?busca=&page=&limit=`
- `POST /api/profissional/clientes`
- `GET /api/profissional/clientes/{id}`
- `PATCH /api/profissional/clientes/{id}`
- `GET /api/profissional/clientes/{id}/historico`

### Comandas

- `GET /api/profissional/comandas?status=&page=&limit=`
- `POST /api/profissional/comandas`
- `GET /api/profissional/comandas/{id}`
- `POST /api/profissional/comandas/{id}/itens`
- `DELETE /api/profissional/comandas/{id}/itens/{itemId}`
- `POST /api/profissional/comandas/{id}/enviar-caixa`
- `POST /api/profissional/comandas/{id}/cancelar`

### Comissões

- `GET /api/profissional/comissoes?inicio=&fim=&status=`
- `GET /api/profissional/comissoes/{id}`

### Notificações

- `GET /api/profissional/notificacoes`
- `PATCH /api/profissional/notificacoes/{id}/lida`
- `POST /api/profissional/device-token`

### Perfil

- `PATCH /api/profissional/perfil`
- `POST /api/profissional/alterar-senha`

## Supabase Free

Recomendações para a API Oracle:

- Não usar `select *`.
- Paginar listas.
- Limitar busca de clientes.
- Criar índices em `salao_id`, `profissional_id`, `cliente_id`, `data`, `status` e `created_at`.
- Evitar realtime global.
- Usar FCM para avisos.
- Usar cache na API e cache offline no app.

## Rodar o projeto

No Windows:

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :app:assembleDebug
```

## Gerar APK

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :app:assembleDebug
```

Arquivo gerado:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Gerar AAB

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :app:bundleRelease
```

Arquivo gerado:

```text
app/build/outputs/bundle/release/app-release.aab
```

Para publicar na Play Store, configure assinatura release antes do envio.
