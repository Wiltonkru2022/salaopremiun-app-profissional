package br.com.salaopremiun.profissional.domain.model

import br.com.salaopremiun.profissional.core.model.Money
import com.google.gson.annotations.SerializedName

data class ProfessionalProfile(
    val id: String,
    val name: String,
    val salonId: String,
    val salonName: String,
    val email: String,
    val phone: String,
    val active: Boolean,
    @SerializedName("nome_exibicao")
    val displayName: String = name,
    @SerializedName("categoria")
    val category: String = "",
    val role: String = "",
    val cpf: String = "",
    val whatsapp: String = "",
    @SerializedName("foto_url")
    val photoUrl: String = "",
    val bio: String = "",
    @SerializedName("pix_tipo")
    val pixType: String = "",
    @SerializedName("pix_chave")
    val pixKey: String = "",
    @SerializedName("notificacao_app_ativa")
    val appNotificationsEnabled: Boolean = true,
    @SerializedName("notificacao_email_ativa")
    val emailNotificationsEnabled: Boolean = true,
)

data class ClientSummary(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val lastVisit: String,
)

data class ClientDetail(
    val id: String,
    @SerializedName("nome")
    val name: String,
    @SerializedName("nome_social")
    val socialName: String = "",
    @SerializedName("telefone")
    val phone: String = "",
    val whatsapp: String = "",
    val email: String = "",
    val cpf: String = "",
    @SerializedName("data_nascimento")
    val birthDate: String = "",
    val address: String = "",
    @SerializedName("observacoes")
    val notes: String = "",
    val status: String = "ativo",
)

data class CommandSummary(
    val id: String,
    val clientName: String,
    val status: CommandStatus,
    val total: Money,
    val itemCount: Int,
    val number: String = "",
)

data class CommandDetail(
    val id: String,
    @SerializedName("numero")
    val number: String = "",
    val clientName: String = "Cliente não informado",
    val clientPhone: String = "",
    val status: CommandStatus = CommandStatus.Open,
    val subtotal: Money = Money(0),
    val discount: Money = Money(0),
    val addition: Money = Money(0),
    val total: Money = Money(0),
    val notes: String = "",
    @SerializedName("comanda_itens")
    val items: List<CommandItem> = emptyList(),
)

data class CommandItem(
    val id: String,
    @SerializedName("tipo_item")
    val type: String,
    @SerializedName("descricao")
    val description: String,
    @SerializedName("quantidade")
    val quantity: Double,
    @SerializedName("valor_unitario")
    val unitValue: Money,
    @SerializedName("valor_total")
    val totalValue: Money,
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
    val baseValue: Money = Money(0),
    val percentage: Double = 0.0,
    val paidAt: String = "",
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

data class AppointmentDetail(
    val id: String,
    @SerializedName("data")
    val date: String = "",
    @SerializedName("hora_inicio")
    val timeStart: String = "",
    @SerializedName("hora_fim")
    val timeEnd: String = "",
    val status: AppointmentStatus = AppointmentStatus.PendingConfirmation,
    @SerializedName("duracao_minutos")
    val durationMinutes: Int = 0,
    @SerializedName("observacoes")
    val notes: String = "",
    @SerializedName("id_comanda")
    val commandId: String = "",
    @SerializedName("cliente_id")
    val clientId: String = "",
    val clientName: String = "Cliente",
    val clientPhone: String = "",
    @SerializedName("servico_id")
    val serviceId: String = "",
    val serviceName: String = "Serviço",
    val servicePrice: Money = Money(0),
)

data class CatalogItem(
    val id: String,
    val name: String,
    val description: String = "",
    val price: Money = Money(0),
    val durationMinutes: Int = 0,
    val stock: Double = 0.0,
    val type: String = "servico",
)

data class CalendarDayMarker(
    val day: Int,
    val hasAppointments: Boolean,
    val selected: Boolean,
)
