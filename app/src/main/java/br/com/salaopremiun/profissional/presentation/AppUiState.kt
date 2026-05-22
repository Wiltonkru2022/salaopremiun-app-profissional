package br.com.salaopremiun.profissional.presentation

import br.com.salaopremiun.profissional.domain.model.AppointmentPreview
import br.com.salaopremiun.profissional.domain.model.ClientSummary
import br.com.salaopremiun.profissional.domain.model.CommandSummary
import br.com.salaopremiun.profissional.domain.model.CommissionSummary
import br.com.salaopremiun.profissional.domain.model.ProfessionalDashboard
import br.com.salaopremiun.profissional.domain.model.ProfessionalNotification
import br.com.salaopremiun.profissional.domain.model.ProfessionalProfile

data class AppUiState(
    val loading: Boolean = true,
    val offline: Boolean = false,
    val authenticated: Boolean = false,
    val profile: ProfessionalProfile? = null,
    val dashboard: ProfessionalDashboard? = null,
    val appointments: List<AppointmentPreview> = emptyList(),
    val clients: List<ClientSummary> = emptyList(),
    val commands: List<CommandSummary> = emptyList(),
    val commissions: List<CommissionSummary> = emptyList(),
    val notifications: List<ProfessionalNotification> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null,
)
