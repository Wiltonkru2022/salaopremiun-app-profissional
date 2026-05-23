package br.com.salaopremiun.profissional.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
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
    state: AppUiState,
    viewModel: AppViewModel,
) {
    var cpf by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ScreenColumn {
        Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
            color = Color(0xFF09090B),
            tonalElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(52.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                        color = Color.White.copy(alpha = 0.10f),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Filled.WorkspacePremium,
                                contentDescription = null,
                                tint = Color(0xFFF5D27A),
                            )
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(999.dp),
                            color = Color.White.copy(alpha = 0.10f),
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Filled.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Color(0xFFF5D27A),
                                    modifier = Modifier.size(14.dp),
                                )
                                Text(
                                    "App profissional",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFF3C4),
                                )
                            }
                        }
                        Text(
                            "Entrar na rotina do salão",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
                Text(
                    "Agenda, comandas, clientes e comissões em um acesso leve e seguro.",
                    color = Color(0xFFD4D4D8),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                    color = Color.White.copy(alpha = 0.10f),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            tint = Color(0xFFF5D27A),
                            modifier = Modifier.size(16.dp),
                        )
                        Text(
                            "Login por CPF e senha cadastrados pelo salão.",
                            color = Color.White.copy(alpha = 0.82f),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(999.dp),
                    color = Color(0xFFF4F4F5),
                ) {
                    Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(999.dp),
                            color = Color(0xFF09090B),
                        ) {
                            Text(
                                "CPF e senha",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Text(
                            "App profissional",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            color = Color(0xFF52525B),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Text(
                    "Acesse sua conta",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF09090B),
                )
                Text(
                    "Login rápido para abrir agenda, comandas e clientes direto no celular.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { cpf = formatCpf(it) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("CPF") },
                    placeholder = { Text("000.000.000-00") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha") },
                    placeholder = { Text("Digite sua senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                )
                state.errorMessage?.let {
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                        color = Color(0xFFFFF1F2),
                    ) {
                        Text(
                            it,
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFFBE123C),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                PrimaryButton(
                    text = "Entrar",
                    onClick = {
                        viewModel.login(cpf, password)
                    },
                )
                Text(
                    "Esqueceu sua senha?",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF52525B),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    "Versão 1.0.0",
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFA1A1AA),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

private fun formatCpf(value: String): String {
    val digits = value.filter(Char::isDigit).take(11)
    return buildString {
        digits.forEachIndexed { index, char ->
            if (index == 3 || index == 6) append('.')
            if (index == 9) append('-')
            append(char)
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
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Atendimentos", state.appointments.size.toString(), Modifier.weight(1f))
            MetricCard("Previsto", "R$ 539,90", Modifier.weight(1f))
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader(
                    title = "Agenda e horários",
                    subtitle = "Calendário do mês com os atendimentos do dia.",
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
        FilterRow(listOf("Todos", "Confirmados", "Pendentes"))
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader("Dia de trabalho", "Expediente das 08:00 às 18:00.")
                if (state.appointments.isEmpty()) {
                    EmptyInline("Nenhum atendimento para o dia selecionado.")
                } else {
                    state.appointments.forEach { appointment ->
                        AppointmentCard(appointment = appointment, onOpen = onOpenAppointment)
                    }
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
        PremiumCard {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                ActionIcon(Icons.Filled.CalendarMonth)
                Column(modifier = Modifier.weight(1f)) {
                    Text("Novo agendamento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Text("Escolha cliente, serviço, data e horário. A reserva é validada na API Oracle.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                AppHeader("Dados do atendimento", "Preencha as informações principais.")
                OutlinedTextField(clienteId, { clienteId = it }, Modifier.fillMaxWidth(), label = { Text("Cliente") }, placeholder = { Text("Buscar ou informar ID do cliente") })
                OutlinedTextField(servicoId, { servicoId = it }, Modifier.fillMaxWidth(), label = { Text("Serviço") }, placeholder = { Text("Selecionar serviço") })
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(date, { date = it }, Modifier.weight(1f), label = { Text("Data") })
                    OutlinedTextField(time, { time = it }, Modifier.weight(1f), label = { Text("Horário") })
                }
                state.activeReservationId?.let {
                    StatusBadge("Reserva ativa", StatusColor.Green)
                }
                PrimaryButton("Reservar horário por 10 minutos", { viewModel.createReservation(clienteId, servicoId, date, time) })
                PrimaryButton("Confirmar agendamento", { viewModel.createAppointment(clienteId, servicoId, date, time) })
            }
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Segurança do horário", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("A confirmação sempre passa pelo servidor. Se trocar horário, a reserva anterior deve ser liberada e a nova fica bloqueada por tempo limitado.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun AppointmentDetailScreen() {
    ScreenColumn {
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AppHeader("Detalhe do atendimento", "Status, cliente, serviço, horário e ações.")
                StatusBadge("Confirmado", StatusColor.Green)
                InfoRow("Cliente", "Cliente selecionado")
                InfoRow("Serviço", "Serviço do atendimento")
                InfoRow("Horário", "09:00 às 10:00")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SmallActionButton("Confirmar", Modifier.weight(1f))
            SmallActionButton("Cancelar", Modifier.weight(1f), danger = true)
        }
        PrimaryButton("Editar atendimento", {})
    }
}

@Composable
fun EditAppointmentScreen() {
    ScreenColumn {
        AppHeader("Editar atendimento", "A alteração de horário cria uma nova validação no servidor.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField("", {}, Modifier.fillMaxWidth(), label = { Text("Cliente") })
                OutlinedTextField("", {}, Modifier.fillMaxWidth(), label = { Text("Serviço") })
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField("", {}, Modifier.weight(1f), label = { Text("Data") })
                    OutlinedTextField("", {}, Modifier.weight(1f), label = { Text("Horário") })
                }
                PrimaryButton("Salvar alterações", {})
            }
        }
    }
}

@Composable
fun CommandsScreen(
    state: AppUiState,
    viewModel: AppViewModel,
    onOpenCommand: () -> Unit,
) {
    LaunchedEffect(Unit) { viewModel.loadCommands() }
    ScreenColumn {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Abertas", state.commands.count { it.status.name == "Open" }.toString(), Modifier.weight(1f))
            MetricCard("Total", state.commands.sumOf { it.total.cents }.let { "R$ ${"%.2f".format(it / 100.0).replace(".", ",")}" }, Modifier.weight(1f))
        }
        FilterRow(listOf("Abertas", "Enviadas", "Fechadas"))
        PrimaryButton("Criar nova comanda", { viewModel.createCommand(null) })
        if (state.commands.isEmpty()) {
            EmptyState("Nenhuma comanda encontrada", "As comandas abertas e enviadas ao caixa aparecem aqui.")
        } else {
            state.commands.forEach { command ->
                ComandaCard(command = command, onOpen = onOpenCommand)
            }
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
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AppHeader("Detalhe da comanda", "Itens, total e envio para o caixa.")
                Text(command?.clientName ?: "Cliente não informado", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                Text("Total: ${command?.total?.format() ?: "R$ 0,00"}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                StatusBadge(command?.status?.label ?: "Aberta", StatusColor.Gold)
                InfoRow("Itens", "${command?.itemCount ?: 0}")
                InfoRow("Comissão prevista", "Calculada após os itens")
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
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AppHeader("Adicionar item", "Busque serviço, produto ou extra.")
                SearchInput(description, { description = it }, "Digite para procurar")
                OutlinedTextField(value, { value = it }, Modifier.fillMaxWidth(), label = { Text("Valor") })
                FilterRow(listOf("Serviço", "Produto", "Extra"))
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
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard("Pendente", state.commissions.filter { it.status.name != "Paid" }.sumOf { it.value.cents }.let { "R$ ${"%.2f".format(it / 100.0).replace(".", ",")}" }, Modifier.weight(1f))
            MetricCard("Pago", state.commissions.filter { it.status.name == "Paid" }.sumOf { it.value.cents }.let { "R$ ${"%.2f".format(it / 100.0).replace(".", ",")}" }, Modifier.weight(1f))
        }
        FilterRow(listOf("Mês", "Pendentes", "Pagas"))
        if (state.commissions.isEmpty()) {
            EmptyState("Nenhuma comissão encontrada", "Quando houver repasse ou atendimento com comissão, ele aparece aqui.")
        } else {
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
}

@Composable
fun NotificationsScreen(
    state: AppUiState,
    viewModel: AppViewModel,
) {
    LaunchedEffect(Unit) { viewModel.loadNotifications() }
    ScreenColumn {
        AppHeader("Notificações", "Avisos importantes do salão e dos agendamentos.")
        if (state.notifications.isEmpty()) {
            EmptyState("Tudo em dia", "Nenhuma notificação nova para mostrar.")
        } else {
            state.notifications.forEach { notification ->
                PremiumCard {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.Top) {
                        ActionIcon(Icons.Filled.AccessTime)
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(notification.title, fontWeight = FontWeight.Bold)
                            Text(notification.message)
                            Text(notification.date, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
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
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                StatusBadge("Acesso ativo", StatusColor.Green)
                Text(state.profile?.name ?: "Profissional", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black)
                Text(state.profile?.salonName ?: "SalaoPremiun", color = MaterialTheme.colorScheme.onSurfaceVariant)
                InfoRow("E-mail", state.profile?.email ?: "Não informado")
                InfoRow("Telefone", state.profile?.phone ?: "Não informado")
            }
        }
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Ações do perfil", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                ProfileAction("Configurações", "Fuso horário, notificações e preferências.", onSettings)
                ProfileAction("Alterar senha", "Atualize sua senha de acesso.", onPassword)
                ProfileAction("Suporte e dúvidas", "Fale com o suporte do SalaoPremiun.", onSupport)
                ProfileAction("Termos e privacidade", "Regras de uso e proteção de dados.", onLegal)
            }
        }
        PrimaryButton("Sair", onLogout)
    }
}

@Composable
fun SettingsScreen() {
    ScreenColumn {
        AppHeader("Configurações", "Preferências do App Profissional.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SettingsLine("Fuso horário do salão", "America/Campo_Grande")
                SettingsLine("Notificações", "Ativas para agenda e comanda")
                SettingsLine("Cache offline", "Agenda do dia, clientes e comandas recentes")
                SettingsLine("API", "Oracle VPS")
                PrimaryButton("Salvar configurações", {})
            }
        }
    }
}

@Composable
fun ChangePasswordScreen() {
    var current by remember { mutableStateOf("") }
    var next by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    ScreenColumn {
        AppHeader("Alterar senha", "Use uma senha segura para proteger a agenda e as comandas.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(current, { current = it }, Modifier.fillMaxWidth(), label = { Text("Senha atual") }, visualTransformation = PasswordVisualTransformation())
                OutlinedTextField(next, { next = it }, Modifier.fillMaxWidth(), label = { Text("Nova senha") }, visualTransformation = PasswordVisualTransformation())
                OutlinedTextField(confirm, { confirm = it }, Modifier.fillMaxWidth(), label = { Text("Confirmar nova senha") }, visualTransformation = PasswordVisualTransformation())
                PrimaryButton("Atualizar senha", {})
            }
        }
    }
}

@Composable
fun SupportScreen() {
    ScreenColumn {
        AppHeader("Suporte e dúvidas", "Atendimento para profissionais do SalaoPremiun.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SettingsLine("Agenda", "Problemas com horários, reservas e confirmações.")
                SettingsLine("Comandas", "Itens, envio para caixa e comissão prevista.")
                SettingsLine("Acesso", "CPF, senha e sessão do profissional.")
                PrimaryButton("Chamar suporte", {})
            }
        }
    }
}

@Composable
fun LegalScreen() {
    ScreenColumn {
        AppHeader("Termos e privacidade", "Regras de uso, privacidade e segurança dos dados.")
        PremiumCard {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Uso profissional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("O acesso é individual e deve ser usado apenas pelo profissional autorizado pelo salão.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Dados e segurança", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("As ações críticas são validadas pela API Oracle. O app não guarda chave secreta do banco.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                PrimaryButton("Li e concordo", {})
            }
        }
    }
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
private fun FilterRow(items: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEachIndexed { index, item ->
            Surface(
                modifier = Modifier.weight(1f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(999.dp),
                color = if (index == 0) Color(0xFF09090B) else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (index == 0) Color(0xFF09090B) else Color(0xFFE4E7EC),
                ),
            ) {
                Box(modifier = Modifier.padding(vertical = 9.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (index == 0) Color.White else Color(0xFF52525B),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyInline(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        color = Color(0xFFF6F7F9),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E7EC)),
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(14.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF101828))
    }
}

@Composable
private fun SmallActionButton(
    text: String,
    modifier: Modifier = Modifier,
    danger: Boolean = false,
) {
    Surface(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        color = if (danger) Color(0xFFFFF1F2) else Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (danger) Color(0xFFFDA4AF) else Color(0xFFE4E7EC)),
    ) {
        Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = if (danger) Color(0xFFBE123C) else Color(0xFF101828),
            )
        }
    }
}

@Composable
private fun ProfileAction(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        color = Color(0xFFF6F7F9),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E7EC)),
    ) {
        Row(
            modifier = Modifier.padding(13.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ActionIcon(Icons.Filled.WorkspacePremium)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF101828))
                Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun SettingsLine(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        color = Color(0xFFF6F7F9),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE4E7EC)),
    ) {
        Column(modifier = Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(label, fontWeight = FontWeight.Bold, color = Color(0xFF101828))
            Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
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
