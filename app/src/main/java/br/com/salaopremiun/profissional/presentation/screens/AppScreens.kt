package br.com.salaopremiun.profissional.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.salaopremiun.profissional.presentation.AppUiState
import br.com.salaopremiun.profissional.presentation.AppViewModel
import br.com.salaopremiun.profissional.presentation.components.AppHeader
import br.com.salaopremiun.profissional.presentation.components.AppointmentCard
import br.com.salaopremiun.profissional.presentation.components.ActionTile
import br.com.salaopremiun.profissional.presentation.components.CalendarView
import br.com.salaopremiun.profissional.presentation.components.ClientCard
import br.com.salaopremiun.profissional.presentation.components.ComandaCard
import br.com.salaopremiun.profissional.presentation.components.EmptyState
import br.com.salaopremiun.profissional.presentation.components.HeroCard
import br.com.salaopremiun.profissional.presentation.components.PremiumCard
import br.com.salaopremiun.profissional.presentation.components.PrimaryButton
import br.com.salaopremiun.profissional.presentation.components.SearchInput
import br.com.salaopremiun.profissional.presentation.components.StatusBadge
import br.com.salaopremiun.profissional.presentation.components.StatusColor

@Composable
fun LoginScreen(
    viewModel: AppViewModel,
    onLoggedIn: () -> Unit,
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ScreenColumn {
        AppHeader(
            title = "App Profissional",
            subtitle = "Acesse sua agenda, clientes e comandas.",
        )
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("E-mail ou telefone") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha") },
                    singleLine = true,
                )
                PrimaryButton(
                    text = "Entrar",
                    onClick = {
                        viewModel.login(login, password)
                    },
                )
            }
        }
    }
}

@Composable
fun DashboardScreen(
    state: AppUiState,
    onOpenAgenda: () -> Unit,
    onOpenCommands: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenProfile: () -> Unit,
) {
    val dashboard = state.dashboard
    ScreenColumn {
        if (state.offline) {
            EmptyState("Você está offline", "Os dados salvos continuam disponíveis. Ações críticas ficam bloqueadas.")
        }
        if (dashboard == null) {
            EmptyState("Resumo indisponível", "Entre novamente quando a API da Oracle estiver disponível.")
            return@ScreenColumn
        }
        HeroCard(
            professionalName = state.profile?.name ?: dashboard.professionalName,
            nextTime = dashboard.appointments.firstOrNull()?.timeRange?.take(5) ?: "Livre",
            todayCount = dashboard.workday.appointmentCount,
            monthCount = 18,
            onProfile = onOpenProfile,
        )
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader("Ações rápidas", "Tudo o que você mais usa no dia a dia.")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionTile(
                        icon = Icons.Filled.PersonAdd,
                        title = "Cadastrar cliente",
                        subtitle = "Novo cadastro",
                        onClick = onOpenNotifications,
                        modifier = Modifier.weight(1f),
                    )
                    ActionTile(
                        icon = Icons.Filled.CalendarMonth,
                        title = "Novo horário",
                        subtitle = "Abrir agenda",
                        onClick = onOpenAgenda,
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    ActionTile(
                        icon = Icons.AutoMirrored.Filled.ReceiptLong,
                        title = "Nova comanda",
                        subtitle = "Criar no app",
                        onClick = onOpenCommands,
                        modifier = Modifier.weight(1f),
                    )
                    ActionTile(
                        icon = Icons.Filled.RequestQuote,
                        title = "Comissões",
                        subtitle = "Ver repasses",
                        onClick = onOpenNotifications,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader("Agenda de hoje", "Toque para abrir os detalhes do atendimento.")
                dashboard.appointments.forEach { appointment ->
                    AppointmentCard(appointment = appointment, onOpen = onOpenAgenda)
                }
            }
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader("Resumo", "Indicadores principais do seu dia.")
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("Comissão do mês", "R$ 539,90", Modifier.weight(1f))
                    MetricCard("Atendimentos hoje", dashboard.workday.appointmentCount.toString(), Modifier.weight(1f))
                }
            }
        }
        PremiumCard {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ActionIcon(Icons.Filled.AccessTime)
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Precisa de ajuda?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "Use o suporte para login, agenda, comandas, caixa e dúvidas operacionais do app profissional.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    PrimaryButton("Abrir suporte", onOpenNotifications)
                }
            }
        }
    }
}

@Composable
fun ClientsScreen(
    state: AppUiState,
    viewModel: AppViewModel,
    onNewClient: () -> Unit,
    onOpenClient: () -> Unit,
) {
    LaunchedEffect(Unit) { viewModel.loadClients() }
    ScreenColumn {
        SearchInput(
            value = state.searchQuery,
            onValueChange = viewModel::loadClients,
            placeholder = "Buscar por nome, WhatsApp ou e-mail",
        )
        PrimaryButton("Cadastrar cliente", onNewClient)
        if (state.clients.isEmpty()) {
            EmptyState("Nenhum cliente encontrado", "A busca retorna poucos campos para poupar a API e o banco.")
        }
        state.clients.forEach { client ->
            ClientCard(client = client, onOpen = onOpenClient)
        }
    }
}

@Composable
fun ClientFormScreen(title: String, viewModel: AppViewModel) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    ScreenColumn {
        AppHeader(title, "Cadastro leve, validado no servidor.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Nome completo") })
                OutlinedTextField(phone, { phone = it }, Modifier.fillMaxWidth(), label = { Text("WhatsApp") })
                OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("E-mail") })
                OutlinedTextField(notes, { notes = it }, Modifier.fillMaxWidth(), label = { Text("Observações") })
                PrimaryButton("Salvar cliente", { viewModel.saveClient(name, phone, email, notes) })
            }
        }
    }
}

@Composable
fun AgendaScreen(
    state: AppUiState,
    viewModel: AppViewModel,
    onNewAppointment: () -> Unit,
    onOpenAppointment: () -> Unit,
) {
    var selectedDay by remember { mutableIntStateOf(22) }
    LaunchedEffect(Unit) { viewModel.loadAgenda() }
    ScreenColumn {
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader(
                    title = "Calendário",
                    subtitle = "Toque em um dia para ver os atendimentos.",
                    trailing = {
                        PrimaryButton(
                            text = "Novo",
                            onClick = onNewAppointment,
                            modifier = Modifier.fillMaxWidth(0.28f),
                        )
                    },
                )
                CalendarView(selectedDay = selectedDay, onDaySelected = { selectedDay = it })
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Atendimentos", state.appointments.size.toString(), Modifier.weight(1f))
            MetricCard("Previsto", "R$ 539,90", Modifier.weight(1f))
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader("Dia de trabalho", "Expediente das 08:00 às 18:00.")
                state.appointments.forEach { appointment ->
                    AppointmentCard(appointment = appointment, onOpen = onOpenAppointment)
                }
            }
        }
    }
}

@Composable
fun NewAppointmentScreen(state: AppUiState, viewModel: AppViewModel) {
    var clienteId by remember { mutableStateOf("") }
    var servicoId by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026-05-22") }
    var time by remember { mutableStateOf("09:00") }
    ScreenColumn {
        AppHeader("Novo agendamento", "Reserva temporária com expiração no servidor.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(clienteId, { clienteId = it }, Modifier.fillMaxWidth(), label = { Text("ID do cliente") })
                OutlinedTextField(servicoId, { servicoId = it }, Modifier.fillMaxWidth(), label = { Text("ID do serviço") })
                OutlinedTextField(date, { date = it }, Modifier.fillMaxWidth(), label = { Text("Data") })
                OutlinedTextField(time, { time = it }, Modifier.fillMaxWidth(), label = { Text("Horário") })
                state.activeReservationId?.let {
                    StatusBadge("Reserva ativa", StatusColor.Green)
                }
                PrimaryButton("Criar reserva temporária", { viewModel.createReservation(clienteId, servicoId, date, time) })
                PrimaryButton("Confirmar agendamento", { viewModel.createAppointment(clienteId, servicoId, date, time) })
            }
        }
        EmptyState(
            title = "Regra de segurança",
            message = "O app nunca confirma conflito sozinho. A API valida expediente, pausas, permissões e reserva ativa.",
        )
    }
}

@Composable
fun AppointmentDetailScreen() {
    DetailScaffold(
        title = "Detalhe do atendimento",
        subtitle = "Status, cliente, serviço, horário e ações permitidas.",
        button = "Editar atendimento",
    )
}

@Composable
fun EditAppointmentScreen() {
    DetailScaffold(
        title = "Editar atendimento",
        subtitle = "Troca de horário cancela a reserva anterior e cria uma nova reserva.",
        button = "Salvar alterações",
    )
}

@Composable
fun CommandsScreen(
    state: AppUiState,
    viewModel: AppViewModel,
    onOpenCommand: () -> Unit,
) {
    LaunchedEffect(Unit) { viewModel.loadCommands() }
    ScreenColumn {
        AppHeader("Comandas", "Abertas, enviadas ao caixa e fechadas.")
        PrimaryButton("Criar nova comanda", { viewModel.createCommand(null) })
        state.commands.forEach { command ->
            ComandaCard(command = command, onOpen = onOpenCommand)
        }
    }
}

@Composable
fun CommandDetailScreen(
    state: AppUiState,
    viewModel: AppViewModel,
    onAddItem: () -> Unit,
) {
    val command = state.commands.firstOrNull()
    ScreenColumn {
        AppHeader("Detalhe da comanda", "Total, itens, descontos permitidos e comissão prevista.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(command?.clientName ?: "Cliente não informado")
                Text("Total: ${command?.total?.format() ?: "R$ 0,00"}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                StatusBadge(command?.status?.label ?: "Aberta", StatusColor.Gold)
            }
        }
        PrimaryButton("Adicionar serviço ou produto", onAddItem)
        PrimaryButton("Enviar para o caixa", { command?.let { viewModel.sendCommandToCashier(it.id) } })
    }
}

@Composable
fun AddCommandItemScreen(state: AppUiState, viewModel: AppViewModel) {
    var description by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    val command = state.commands.firstOrNull()
    ScreenColumn {
        AppHeader("Adicionar item", "Busca de serviço, produto ou extra.")
        SearchInput(description, { description = it }, "Digite serviço, produto ou extra")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value, { value = it }, Modifier.fillMaxWidth(), label = { Text("Valor") })
                PrimaryButton("Adicionar item", {
                    command?.let { viewModel.addCommandItem(it.id, description, value.replace(",", ".").toDoubleOrNull() ?: 0.0) }
                })
            }
        }
    }
}

@Composable
fun CommissionsScreen(
    state: AppUiState,
    viewModel: AppViewModel,
) {
    LaunchedEffect(Unit) { viewModel.loadCommissions() }
    ScreenColumn {
        AppHeader("Comissões", "Pendentes, pagas e filtradas por período.")
        state.commissions.forEach { commission ->
            PremiumCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(commission.description, fontWeight = FontWeight.Bold)
                    Text(commission.date, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(commission.value.format(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    StatusBadge(
                        text = commission.status.label,
                        color = if (commission.status.name == "Paid") StatusColor.Green else StatusColor.Gold,
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen(
    state: AppUiState,
    viewModel: AppViewModel,
) {
    LaunchedEffect(Unit) { viewModel.loadNotifications() }
    ScreenColumn {
        AppHeader("Notificações", "FCM para avisos importantes sem consultar toda hora.")
        state.notifications.forEach { notification ->
            PremiumCard {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(notification.title, fontWeight = FontWeight.Bold)
                    Text(notification.message)
                    Text(notification.date, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    state: AppUiState,
    onSettings: () -> Unit,
    onPassword: () -> Unit,
    onSupport: () -> Unit,
    onLegal: () -> Unit,
    onLogout: () -> Unit,
) {
    ScreenColumn {
        AppHeader("Perfil", state.profile?.salonName ?: "SalaoPremiun")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(state.profile?.name ?: "Profissional", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(state.profile?.email ?: "E-mail não informado")
                Text(state.profile?.phone ?: "Telefone não informado")
            }
        }
        PrimaryButton("Configurações", onSettings)
        PrimaryButton("Alterar senha", onPassword)
        PrimaryButton("Suporte e dúvidas", onSupport)
        PrimaryButton("Termos e privacidade", onLegal)
        PrimaryButton("Sair", onLogout)
    }
}

@Composable
fun SettingsScreen() {
    DetailScaffold(
        title = "Configurações",
        subtitle = "Preferências do app, cache offline, notificações e fuso horário do salão.",
        button = "Salvar configurações",
    )
}

@Composable
fun ChangePasswordScreen() {
    DetailScaffold("Alterar senha", "A API valida a senha atual e registra logs de segurança.", "Atualizar senha")
}

@Composable
fun SupportScreen() {
    DetailScaffold("Suporte e dúvidas", "Atendimento para profissionais do SalaoPremiun.", "Chamar suporte")
}

@Composable
fun LegalScreen() {
    DetailScaffold("Termos e privacidade", "Regras de uso, privacidade e segurança dos dados.", "Li e concordo")
}

@Composable
private fun DetailScaffold(
    title: String,
    subtitle: String,
    button: String,
) {
    ScreenColumn {
        AppHeader(title, subtitle)
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(subtitle)
                Text("Todas as ações críticas exigem validação da API Oracle.")
            }
        }
        PrimaryButton(button, {})
    }
}

@Composable
private fun MetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    PremiumCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ActionIcon(icon: ImageVector) {
    Surface(
        modifier = Modifier.padding(top = 2.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        color = Color(0xFFF8F3E7),
    ) {
        Box(
            modifier = Modifier.padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            androidx.compose.material3.Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFC89B3C),
            )
        }
    }
}

@Composable
private fun ScreenColumn(
    content: @Composable () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                content()
            }
        }
    }
}
