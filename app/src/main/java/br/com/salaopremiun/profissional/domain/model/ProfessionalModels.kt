package br.com.salaopremiun.profissional.domain.model

import br.com.salaopremiun.profissional.core.model.Money

data class ProfessionalProfile(
    val id: String,
    val name: String,
    val salonId: String,
    val salonName: String,
    val email: String,
    val phone: String,
    val active: Boolean,
)

data class ClientSummary(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val lastVisit: String,
)

data class CommandSummary(
    val id: String,
    val clientName: String,
    val status: CommandStatus,
    val total: Money,
    val itemCount: Int,
)

enum class CommandStatus(val label: String) {
    Open("Aberta"),
    SentToCashier("Enviada ao caixa"),
    Closed("Fechada"),
    Canceled("Cancelada"),
}

data class CommissionSummary(
    val id: String,
    val description: String,
    val date: String,
    val value: Money,
    val status: CommissionStatus,
)

enum class CommissionStatus(val label: String) {
    Pending("Pendente"),
    Paid("Paga"),
}

data class ProfessionalNotification(
    val id: String,
    val title: String,
    val message: String,
    val date: String,
    val read: Boolean,
)

data class CalendarDayMarker(
    val day: Int,
    val hasAppointments: Boolean,
    val selected: Boolean,
)
