package br.com.salaopremiun.profissional.domain.model

import br.com.salaopremiun.profissional.core.model.Money

data class ProfessionalDashboard(
    val professionalName: String,
    val salonName: String,
    val workday: WorkdaySummary,
    val appointments: List<AppointmentPreview>,
    val quickActions: List<QuickAction>,
)

data class WorkdaySummary(
    val dateLabel: String,
    val scheduleLabel: String,
    val appointmentCount: Int,
    val confirmedCount: Int,
    val pendingCount: Int,
    val expectedRevenue: Money,
)

data class AppointmentPreview(
    val id: String,
    val timeRange: String,
    val clientName: String,
    val serviceName: String,
    val professionalName: String,
    val status: AppointmentStatus,
)

enum class AppointmentStatus(
    val label: String,
) {
    WaitingPayment("Aguardando sinal"),
    PendingConfirmation("Pendente de confirmação"),
    Confirmed("Confirmado"),
    InProgress("Em atendimento"),
    Completed("Atendido"),
    NoShow("Não compareceu"),
    Canceled("Cancelado"),
}

data class QuickAction(
    val title: String,
    val description: String,
)
