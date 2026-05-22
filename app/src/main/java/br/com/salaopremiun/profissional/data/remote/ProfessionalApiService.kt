package br.com.salaopremiun.profissional.data.remote

import br.com.salaopremiun.profissional.data.remote.dto.DeviceTokenRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.LoginRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.LoginResponseDto
import br.com.salaopremiun.profissional.data.remote.dto.PaginatedResponseDto
import br.com.salaopremiun.profissional.data.remote.dto.RefreshRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.ReservationRequestDto
import br.com.salaopremiun.profissional.data.remote.dto.ReservationResponseDto
import br.com.salaopremiun.profissional.domain.model.AppointmentPreview
import br.com.salaopremiun.profissional.domain.model.ClientSummary
import br.com.salaopremiun.profissional.domain.model.CommandSummary
import br.com.salaopremiun.profissional.domain.model.CommissionSummary
import br.com.salaopremiun.profissional.domain.model.ProfessionalDashboard
import br.com.salaopremiun.profissional.domain.model.ProfessionalNotification
import br.com.salaopremiun.profissional.domain.model.ProfessionalProfile
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfessionalApiService {
    @POST("api/profissional/auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("api/profissional/auth/logout")
    suspend fun logout()

    @POST("api/profissional/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequestDto): LoginResponseDto

    @GET("api/profissional/me")
    suspend fun me(): ProfessionalProfile

    @GET("api/profissional/dashboard")
    suspend fun dashboard(): ProfessionalDashboard

    @GET("api/profissional/agenda")
    suspend fun agendaDay(@Query("data") date: String): List<AppointmentPreview>

    @GET("api/profissional/agenda/mes")
    suspend fun agendaMonth(@Query("mes") month: String): List<Int>

    @POST("api/profissional/agenda/reservas")
    suspend fun createReservation(@Body request: ReservationRequestDto): ReservationResponseDto

    @DELETE("api/profissional/agenda/reservas/{id}")
    suspend fun cancelReservation(@Path("id") id: String)

    @GET("api/profissional/clientes")
    suspend fun clients(
        @Query("busca") search: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): PaginatedResponseDto<ClientSummary>

    @GET("api/profissional/comandas")
    suspend fun commands(
        @Query("status") status: String?,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): PaginatedResponseDto<CommandSummary>

    @GET("api/profissional/comissoes")
    suspend fun commissions(
        @Query("inicio") start: String?,
        @Query("fim") end: String?,
        @Query("status") status: String?,
    ): List<CommissionSummary>

    @GET("api/profissional/notificacoes")
    suspend fun notifications(): List<ProfessionalNotification>

    @PATCH("api/profissional/notificacoes/{id}/lida")
    suspend fun markNotificationRead(@Path("id") id: String)

    @POST("api/profissional/device-token")
    suspend fun saveDeviceToken(@Body request: DeviceTokenRequestDto)
}
