package br.com.salaopremiun.profissional.data.remote.dto

data class LoginRequestDto(
    val cpf: String,
    val senha: String,
)

data class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
)

data class RefreshRequestDto(
    val refreshToken: String,
)

data class DeviceTokenRequestDto(
    val token: String,
    val platform: String = "android",
)

data class ClientSaveRequestDto(
    val nome: String,
    val telefone: String,
    val whatsapp: String,
    val email: String,
    val observacoes: String,
)

data class AppointmentSaveRequestDto(
    val clienteId: String,
    val servicoId: String,
    val data: String,
    val horario: String,
    val reservaId: String? = null,
    val observacoes: String = "",
)

data class CommandSaveRequestDto(
    val clienteId: String? = null,
    val observacoes: String = "",
)

data class CommandItemRequestDto(
    val tipo: String,
    val servicoId: String? = null,
    val produtoId: String? = null,
    val descricao: String = "",
    val quantidade: Double = 1.0,
    val valorUnitario: Double = 0.0,
)

data class StatusRequestDto(
    val status: String,
)

data class ChangePasswordRequestDto(
    val senhaAtual: String,
    val novaSenha: String,
)

data class PaginatedResponseDto<T>(
    val items: List<T>,
    val page: Int,
    val limit: Int,
    val hasNextPage: Boolean,
)

data class ReservationRequestDto(
    val clienteId: String,
    val servicoId: String,
    val data: String,
    val horario: String,
)

data class ReservationResponseDto(
    val id: String,
    val expiresAt: String,
)
