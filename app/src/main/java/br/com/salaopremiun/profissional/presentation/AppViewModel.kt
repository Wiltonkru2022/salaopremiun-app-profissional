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

    fun login(login: String, password: String) {
        viewModelScope.launch {
            mutableUiState.update { it.copy(loading = true, errorMessage = null) }
            runCatching {
                val profile = repository.login(login, password)
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
                        errorMessage = error.toUserMessage("Não foi possível entrar. Confira seus dados e tente novamente."),
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

    fun loadCommands() {
        viewModelScope.launch {
            val commands = repository.commands()
            mutableUiState.update { it.copy(commands = commands) }
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

    private fun Throwable.toUserMessage(fallback: String): String {
        return when (this) {
            is HttpException -> if (code() == 401) "Login ou senha inválidos." else fallback
            else -> message?.takeIf { it.isNotBlank() } ?: fallback
        }
    }
}
