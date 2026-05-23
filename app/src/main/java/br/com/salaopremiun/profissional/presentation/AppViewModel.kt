package br.com.salaopremiun.profissional.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.salaopremiun.profissional.data.repository.ProfessionalAppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AppViewModel(
    private val repository: ProfessionalAppRepository,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = mutableUiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        restoreSession()
    }

    fun login(cpf: String, password: String) {
        viewModelScope.launch {
            mutableUiState.update { it.copy(loading = true, errorMessage = null) }
            runCatching {
                val profile = repository.login(cpf, password)
                mutableUiState.update {
                    it.copy(
                        authenticated = profile.active,
                        profile = profile,
                        loading = false,
                    )
                }
                loadHome()
            }.getOrElse { error ->
                mutableUiState.update {
                    it.copy(
                        authenticated = false,
                        loading = false,
                        errorMessage = error.toUserMessage("Não foi possível entrar. Confira seu CPF e sua senha."),
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            mutableUiState.value = AppUiState(loading = false)
        }
    }

    fun loadHome() {
        viewModelScope.launch {
            runCatching { repository.dashboard() }
                .onSuccess { dashboard ->
                    mutableUiState.update {
                        it.copy(
                            loading = false,
                            authenticated = true,
                            dashboard = dashboard,
                            appointments = dashboard.appointments,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { error ->
                    mutableUiState.update {
                        it.copy(
                            loading = false,
                            errorMessage = error.toUserMessage("Não foi possível carregar o início."),
                        )
                    }
                }
        }
    }

    private fun restoreSession() {
        viewModelScope.launch {
            val profile = repository.restoreSession()
            if (profile == null) {
                mutableUiState.update { it.copy(loading = false, authenticated = false) }
                return@launch
            }
            mutableUiState.update {
                it.copy(
                    loading = false,
                    authenticated = true,
                    profile = profile,
                )
            }
            loadHome()
        }
    }

    fun loadClients(search: String = mutableUiState.value.searchQuery) {
        mutableUiState.update { it.copy(searchQuery = search) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(350)
            val clients = repository.clients(search = search, page = 1)
            mutableUiState.update { it.copy(clients = clients) }
        }
    }

    fun loadAgenda() {
        viewModelScope.launch {
            val appointments = repository.appointmentsForToday()
            mutableUiState.update { it.copy(appointments = appointments) }
        }
    }

    fun loadAppointmentDetail(id: String) {
        viewModelScope.launch {
            runCatching { repository.appointment(id) }
                .onSuccess { detail -> mutableUiState.update { it.copy(selectedAppointment = detail, errorMessage = null) } }
                .onFailure { error -> mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível carregar o atendimento.")) } }
        }
    }

    fun loadCommands() {
        viewModelScope.launch {
            val commands = repository.commands()
            mutableUiState.update { it.copy(commands = commands) }
        }
    }

    fun loadCommandDetail(id: String) {
        viewModelScope.launch {
            runCatching { repository.command(id) }
                .onSuccess { command -> mutableUiState.update { it.copy(selectedCommand = command, errorMessage = null) } }
                .onFailure { error -> mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível carregar a comanda.")) } }
        }
    }

    fun loadClientDetail(id: String) {
        viewModelScope.launch {
            runCatching {
                repository.client(id) to repository.clientHistory(id)
            }.onSuccess { (client, history) ->
                mutableUiState.update { it.copy(selectedClient = client, clientHistory = history, errorMessage = null) }
            }.onFailure { error ->
                mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível carregar o cliente.")) }
            }
        }
    }

    fun loadCatalog(search: String = "") {
        viewModelScope.launch {
            runCatching { repository.services(search) to repository.products(search) }
                .onSuccess { (services, products) -> mutableUiState.update { it.copy(services = services, products = products) } }
        }
    }

    fun loadCommissions() {
        viewModelScope.launch {
            val commissions = repository.commissions()
            mutableUiState.update { it.copy(commissions = commissions) }
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            val notifications = repository.notifications()
            mutableUiState.update { it.copy(notifications = notifications) }
        }
    }

    fun saveDeviceToken(token: String) {
        viewModelScope.launch {
            repository.saveDeviceToken(token)
        }
    }

    fun updateProfile(
        name: String?,
        displayName: String?,
        phone: String?,
        whatsapp: String?,
        email: String?,
        bio: String?,
        notificationsEnabled: Boolean?,
    ) {
        viewModelScope.launch {
            runCatching {
                repository.updateProfile(name, displayName, phone, whatsapp, email, bio, notificationsEnabled)
            }.onSuccess { profile ->
                mutableUiState.update { it.copy(profile = profile, errorMessage = "Perfil atualizado com sucesso.") }
            }.onFailure { error ->
                mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível atualizar o perfil.")) }
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmation: String) {
        viewModelScope.launch {
            if (newPassword != confirmation) {
                mutableUiState.update { it.copy(errorMessage = "A confirmação da senha não confere.") }
                return@launch
            }
            runCatching { repository.changePassword(currentPassword, newPassword) }
                .onSuccess { mutableUiState.update { it.copy(errorMessage = "Senha atualizada com sucesso.") } }
                .onFailure { error -> mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível alterar a senha.")) } }
        }
    }

    fun saveClient(name: String, phone: String, email: String, notes: String) {
        viewModelScope.launch {
            mutableUiState.update { it.copy(loading = true, errorMessage = null) }
            runCatching { repository.saveClient(name, phone, email, notes) }
                .onSuccess {
                    mutableUiState.update { state ->
                        state.copy(
                            loading = false,
                            clients = listOf(it) + state.clients,
                            errorMessage = "Cliente salvo com sucesso.",
                        )
                    }
                }
                .onFailure { error ->
                    mutableUiState.update {
                        it.copy(loading = false, errorMessage = error.toUserMessage("Não foi possível salvar o cliente."))
                    }
                }
        }
    }

    fun createReservation(clienteId: String, servicoId: String, date: String, time: String) {
        viewModelScope.launch {
            runCatching { repository.createReservation(clienteId, servicoId, date, time) }
                .onSuccess { reservationId ->
                    mutableUiState.update { it.copy(activeReservationId = reservationId, errorMessage = "Horário reservado por 10 minutos.") }
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível reservar o horário.")) }
                }
        }
    }

    fun createAppointment(clienteId: String, servicoId: String, date: String, time: String) {
        viewModelScope.launch {
            runCatching { repository.createAppointment(clienteId, servicoId, date, time, mutableUiState.value.activeReservationId) }
                .onSuccess {
                    mutableUiState.update { it.copy(activeReservationId = null, errorMessage = "Agendamento confirmado.") }
                    loadAgenda()
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível confirmar o agendamento.")) }
                }
        }
    }

    fun confirmAppointment(id: String) = updateAppointmentStatus(id, "confirmado")

    fun markNoShow(id: String) = updateAppointmentStatus(id, "faltou")

    fun updateAppointment(id: String, serviceId: String, date: String, time: String, notes: String) {
        viewModelScope.launch {
            runCatching { repository.updateAppointment(id, serviceId, date, time, notes) }
                .onSuccess {
                    mutableUiState.update { state -> state.copy(errorMessage = "Atendimento atualizado.") }
                    loadAppointmentDetail(id)
                    loadAgenda()
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível salvar as alterações.")) }
                }
        }
    }

    fun cancelAppointment(id: String) {
        viewModelScope.launch {
            runCatching { repository.cancelAppointment(id) }
                .onSuccess {
                    mutableUiState.update { it.copy(errorMessage = "Agendamento cancelado.") }
                    loadAgenda()
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível cancelar o agendamento.")) }
                }
        }
    }

    private fun updateAppointmentStatus(id: String, status: String) {
        viewModelScope.launch {
            runCatching { repository.updateAppointmentStatus(id, status) }
                .onSuccess {
                    mutableUiState.update { it.copy(errorMessage = "Status atualizado.") }
                    loadAgenda()
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível atualizar o status.")) }
                }
        }
    }

    fun createCommand(clienteId: String?) {
        viewModelScope.launch {
            runCatching { repository.createCommand(clienteId) }
                .onSuccess {
                    mutableUiState.update { state -> state.copy(commands = listOf(it) + state.commands, errorMessage = "Comanda criada.") }
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível criar a comanda.")) }
                }
        }
    }

    fun addCommandItem(commandId: String, description: String, value: Double) {
        viewModelScope.launch {
            runCatching { repository.addCommandItem(commandId, description, value) }
                .onSuccess {
                    mutableUiState.update { it.copy(errorMessage = "Item adicionado.") }
                    loadCommands()
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível adicionar o item.")) }
                }
        }
    }

    fun removeCommandItem(commandId: String, itemId: String) {
        viewModelScope.launch {
            runCatching { repository.removeCommandItem(commandId, itemId) }
                .onSuccess {
                    mutableUiState.update { it.copy(errorMessage = "Item removido.") }
                    loadCommandDetail(commandId)
                    loadCommands()
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível remover o item.")) }
                }
        }
    }

    fun sendCommandToCashier(commandId: String) {
        viewModelScope.launch {
            runCatching { repository.sendCommandToCashier(commandId) }
                .onSuccess {
                    mutableUiState.update { it.copy(errorMessage = "Comanda enviada ao caixa.") }
                    loadCommands()
                }
                .onFailure { error ->
                    mutableUiState.update { it.copy(errorMessage = error.toUserMessage("Não foi possível enviar a comanda.")) }
                }
        }
    }

    private fun Throwable.toUserMessage(fallback: String): String {
        return when (this) {
            is HttpException -> if (code() == 401) "Login ou senha inválidos." else fallback
            else -> message?.takeIf { it.isNotBlank() } ?: fallback
        }
    }
}
