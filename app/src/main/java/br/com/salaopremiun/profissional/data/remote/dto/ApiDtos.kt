package br.com.salaopremiun.profissional.data.remote.dto

data class LoginRequestDto(
    val login: String,
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
