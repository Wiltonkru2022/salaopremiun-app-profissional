package br.com.salaopremiun.profissional.data.repository

import br.com.salaopremiun.profissional.core.model.Money
import br.com.salaopremiun.profissional.core.network.OracleApiConfig
import br.com.salaopremiun.profissional.core.session.ProfessionalSession
import br.com.salaopremiun.profissional.core.session.SessionStore
import br.com.salaopremiun.profissional.data.local.CachedItemEntity
import br.com.salaopremiun.profissional.data.local.LocalCacheDao
import br.com.salaopremiun.profissional.data.remote.ProfessionalApiService
import br.com.salaopremiun.profissional.data.remote.dto.DeviceTokenRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.AppointmentSaveRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.ClientSaveRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.CommandItemRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.CommandSaveRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.ReservationRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.StatusRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.LoginRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.RefreshRequestDto
import br.com.salaopremiun.profissional.domain.model.AppointmentPreview
import br.com.salaopremiun.profissional.domain.model.AppointmentStatus
import br.com.salaopremiun.profissional.domain.model.ClientSummary
import br.com.salaopremiun.profissional.domain.model.CommandStatus
import br.com.salaopremiun.profissional.domain.model.CommandSummary
import br.com.salaopremiun.profissional.domain.model.CommissionStatus
import br.com.salaopremiun.profissional.domain.model.CommissionSummary
import br.com.salaopremiun.profissional.domain.model.ProfessionalDashboard
import br.com.salaopremiun.profissional.domain.model.ProfessionalNotification
import br.com.salaopremiun.profissional.domain.model.ProfessionalProfile
import br.com.salaopremiun.profissional.domain.model.QuickAction
import br.com.salaopremiun.profissional.domain.model.WorkdaySummary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

class ProfessionalAppRepository(
    private val api: ProfessionalApiService,
    private val sessionStore: SessionStore,
    private val cacheDao: LocalCacheDao,
) {
    private val gson = Gson()

    suspend fun login(login: String, password: String): ProfessionalProfile {
        if (OracleApiConfig.MOCK_MODE) {
            sessionStore.saveSession(
                ProfessionalSession(
                    accessToken = "mock-access-token",
                    refreshToken = "mock-refresh-token",
                ),
            )
            return mockProfile()
        }

        val response = api.login(LoginRequestDto(login = login, senha = password))
        sessionStore.saveSession(
            ProfessionalSession(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
            ),
        )
        return api.me().also { cache("profile", it) }
    }

    suspend fun restoreSession(): ProfessionalProfile? {
        if (OracleApiConfig.MOCK_MODE) return mockProfile()
        if (sessionStore.session.firstOrNull() == null) return null

        return runCatching { api.me().also { cache("profile", it) } }.getOrElse {
            val refreshToken = sessionStore.refreshToken.firstOrNull()
            if (refreshToken.isNullOrBlank()) {
                return cached("profile")
            }
            runCatching {
                val response = api.refresh(RefreshRequestDto(refreshToken = refreshToken))
                sessionStore.saveSession(
                    ProfessionalSession(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken,
                    ),
                )
                api.me().also { cache("profile", it) }
            }.getOrElse {
                cached("profile")
            }
        }
    }

    suspend fun logout() {
        runCatching { api.logout() }
        sessionStore.clear()
        cacheDao.clear()
    }

    suspend fun dashboard(): ProfessionalDashboard {
        if (OracleApiConfig.MOCK_MODE) return mockDashboard()
        return runCatching {
            api.dashboard().also { cache("dashboard", it) }
        }.getOrElse {
            cached("dashboard") ?: throw it
        }
    }

    suspend fun clients(search: String, page: Int): List<ClientSummary> {
        if (OracleApiConfig.MOCK_MODE) {
            return mockClients().filter {
                search.isBlank() ||
                    it.name.contains(search, ignoreCase = true) ||
                    it.phone.contains(search, ignoreCase = true) ||
                    it.email.contains(search, ignoreCase = true)
            }
        }

        return runCatching {
            api.clients(
            search = search,
            page = page,
            limit = OracleApiConfig.PAGE_LIMIT,
            ).items.also { cache("clients:$search:$page", it) }
        }.getOrElse {
            cached("clients:$search:$page") ?: emptyList()
        }
    }

    suspend fun appointmentsForToday(): List<AppointmentPreview> {
        if (OracleApiConfig.MOCK_MODE) return mockAppointments()
        val today = LocalDate.now().toString()
        return runCatching { api.agendaDay(today).also { cache("agenda:$today", it) } }
            .getOrElse { cached("agenda:$today") ?: emptyList() }
    }

    suspend fun commands(status: String? = null): List<CommandSummary> {
        if (OracleApiConfig.MOCK_MODE) return mockCommands()
        val key = "commands:${status.orEmpty()}"
        return runCatching {
            api.commands(status = status, page = 1, limit = OracleApiConfig.PAGE_LIMIT).items
                .also { cache(key, it) }
        }.getOrElse { cached(key) ?: emptyList() }
    }

    suspend fun commissions(): List<CommissionSummary> {
        if (OracleApiConfig.MOCK_MODE) return mockCommissions()
        return runCatching { api.commissions(start = null, end = null, status = null).also { cache("commissions", it) } }
            .getOrElse { cached("commissions") ?: emptyList() }
    }

    suspend fun notifications(): List<ProfessionalNotification> {
        if (OracleApiConfig.MOCK_MODE) return mockNotifications()
        return runCatching { api.notifications().also { cache("notifications", it) } }
            .getOrElse { cached("notifications") ?: emptyList() }
    }

    suspend fun saveDeviceToken(token: String) {
        runCatching { api.saveDeviceToken(DeviceTokenRequestDto(token = token)) }
    }

    suspend fun saveClient(name: String, phone: String, email: String, notes: String): ClientSummary {
        return api.createClient(
            ClientSaveRequestDto(
                nome = name,
                telefone = phone,
                whatsapp = phone,
                email = email,
                observacoes = notes,
            ),
        )
    }

    suspend fun createReservation(clienteId: String, servicoId: String, date: String, time: String): String {
        return api.createReservation(
            ReservationRequestDto(
                clienteId = clienteId,
                servicoId = servicoId,
                data = date,
                horario = time,
            ),
        ).id
    }

    suspend fun createAppointment(clienteId: String, servicoId: String, date: String, time: String, reservationId: String?) {
        api.createAppointment(
            AppointmentSaveRequestDto(
                clienteId = clienteId,
                servicoId = servicoId,
                data = date,
                horario = time,
                reservaId = reservationId,
            ),
        )
    }

    suspend fun updateAppointmentStatus(id: String, status: String) {
        api.updateAppointmentStatus(id, StatusRequestDto(status))
    }

    suspend fun cancelAppointment(id: String) {
        api.cancelAppointment(id)
    }

    suspend fun createCommand(clienteId: String?): CommandSummary {
        return api.createCommand(CommandSaveRequestDto(clienteId = clienteId))
    }

    suspend fun addCommandItem(commandId: String, description: String, value: Double) {
        api.addCommandItem(
            commandId,
            CommandItemRequestDto(
                tipo = "extra",
                descricao = description,
                quantidade = 1.0,
                valorUnitario = value,
            ),
        )
    }

    suspend fun sendCommandToCashier(commandId: String): CommandSummary {
        return api.sendCommandToCashier(commandId)
    }

    private suspend fun <T> cache(key: String, value: T) {
        cacheDao.upsert(
            CachedItemEntity(
                cacheKey = key,
                payloadJson = gson.toJson(value),
                updatedAtMillis = System.currentTimeMillis(),
            ),
        )
    }

    private suspend inline fun <reified T> cached(key: String): T? {
        val item = cacheDao.get(key) ?: return null
        return runCatching { gson.fromJson<T>(item.payloadJson, object : TypeToken<T>() {}.type) }.getOrNull()
    }

    private fun mockProfile(): ProfessionalProfile {
        return ProfessionalProfile(
            id = "prof-1",
            name = "Thainara Maia",
            salonId = "salao-1",
            salonName = "SalaoPremiun",
            email = "profissional@salaopremiun.com.br",
            phone = "67999999999",
            active = true,
        )
    }

    private fun mockDashboard(): ProfessionalDashboard {
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
            appointments = mockAppointments(),
            quickActions = listOf(
                QuickAction("Novo agendamento", "Reserve um horário com validação no servidor."),
                QuickAction("Abrir comanda", "Registre serviços, produtos e comissão prevista."),
                QuickAction("Buscar cliente", "Encontre cadastro, WhatsApp e histórico."),
            ),
        )
    }

    private fun mockAppointments(): List<AppointmentPreview> {
        return listOf(
            AppointmentPreview(
                id = "ag-1",
                timeRange = "09:30 - 13:00",
                clientName = "Isabela Macedo",
                serviceName = "Progressiva Curto",
                professionalName = "Thainara Maia",
                status = AppointmentStatus.PendingConfirmation,
            ),
            AppointmentPreview(
                id = "ag-2",
                timeRange = "13:00 - 16:30",
                clientName = "Jhennyfer Souza",
                serviceName = "Coloração e escova",
                professionalName = "Thainara Maia",
                status = AppointmentStatus.Confirmed,
            ),
            AppointmentPreview(
                id = "ag-3",
                timeRange = "16:00 - 17:10",
                clientName = "Anny Sales",
                serviceName = "Corte dia das mães",
                professionalName = "Wilton Kruszciako",
                status = AppointmentStatus.Confirmed,
            ),
        )
    }

    private fun mockClients(): List<ClientSummary> {
        return listOf(
            ClientSummary("cli-1", "Isabela Macedo", "67999990001", "isabela@email.com", "22/05/2026"),
            ClientSummary("cli-2", "Jhennyfer Souza", "67999990002", "jhennyfer@email.com", "20/05/2026"),
            ClientSummary("cli-3", "Anny Sales", "67999990003", "anny@email.com", "18/05/2026"),
        )
    }

    private fun mockCommands(): List<CommandSummary> {
        return listOf(
            CommandSummary("cmd-1", "Jhennyfer Souza", CommandStatus.Open, Money(35000), 2),
            CommandSummary("cmd-2", "Anny Sales", CommandStatus.SentToCashier, Money(9000), 1),
            CommandSummary("cmd-3", "Isabela Macedo", CommandStatus.Closed, Money(30000), 1),
        )
    }

    private fun mockCommissions(): List<CommissionSummary> {
        return listOf(
            CommissionSummary("com-1", "Progressiva Curto", "22/05/2026", Money(9000), CommissionStatus.Pending),
            CommissionSummary("com-2", "Coloração e escova", "20/05/2026", Money(7000), CommissionStatus.Paid),
        )
    }

    private fun mockNotifications(): List<ProfessionalNotification> {
        return listOf(
            ProfessionalNotification("not-1", "Novo agendamento", "Isabela reservou 09:30.", "Hoje", false),
            ProfessionalNotification("not-2", "Comissão paga", "Repasse de R$ 70,00 confirmado.", "Ontem", true),
        )
    }
}
