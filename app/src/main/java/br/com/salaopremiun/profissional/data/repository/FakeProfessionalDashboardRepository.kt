package br.com.salaopremiun.profissional.data.repository

import br.com.salaopremiun.profissional.core.model.Money
import br.com.salaopremiun.profissional.domain.model.AppointmentPreview
import br.com.salaopremiun.profissional.domain.model.AppointmentStatus
import br.com.salaopremiun.profissional.domain.model.ProfessionalDashboard
import br.com.salaopremiun.profissional.domain.model.QuickAction
import br.com.salaopremiun.profissional.domain.model.WorkdaySummary
import br.com.salaopremiun.profissional.domain.repository.ProfessionalDashboardRepository

class FakeProfessionalDashboardRepository : ProfessionalDashboardRepository {
    override fun getDashboard(): ProfessionalDashboard {
        return ProfessionalDashboard(
            professionalName = "Thainara Maia",
            salonName = "SalaoPremiun",
            workday = WorkdaySummary(
                dateLabel = "Hoje",
                scheduleLabel = "Expediente das 08:00 às 18:00.",
                appointmentCount = 5,
                confirmedCount = 3,
                pendingCount = 2,
                expectedRevenue = Money(53990),
            ),
            appointments = listOf(
                AppointmentPreview(
                    id = "1",
                    timeRange = "09:30 - 13:00",
                    clientName = "Isabela Macedo",
                    serviceName = "Progressiva Curto",
                    professionalName = "Thainara Maia",
                    status = AppointmentStatus.PendingConfirmation,
                ),
                AppointmentPreview(
                    id = "2",
                    timeRange = "13:00 - 16:30",
                    clientName = "Jhennyfer Souza",
                    serviceName = "Coloração e escova",
                    professionalName = "Thainara Maia",
                    status = AppointmentStatus.Confirmed,
                ),
                AppointmentPreview(
                    id = "3",
                    timeRange = "16:00 - 17:10",
                    clientName = "Anny Sales",
                    serviceName = "Corte dia das mães",
                    professionalName = "Wilton Kruszciako",
                    status = AppointmentStatus.Confirmed,
                ),
            ),
            quickActions = listOf(
                QuickAction(
                    title = "Novo atendimento",
                    description = "Abra um horário ou registre uma chegada.",
                ),
                QuickAction(
                    title = "Comandas",
                    description = "Acompanhe recebimentos e pagamentos.",
                ),
                QuickAction(
                    title = "Clientes",
                    description = "Consulte histórico, contatos e preferências.",
                ),
            ),
        )
    }
}
