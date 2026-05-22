package br.com.salaopremiun.profissional.core.network

import br.com.salaopremiun.profissional.core.session.SessionStore
import br.com.salaopremiun.profissional.data.remote.ProfessionalApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    fun createProfessionalApi(sessionStore: SessionStore): ProfessionalApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val token = runBlocking { sessionStore.accessToken.firstOrNull() }
                val request = chain.request().newBuilder()
                    .apply {
                        if (!token.isNullOrBlank()) {
                            addHeader("Authorization", "Bearer $token")
                        }
                        addHeader("Accept", "application/json")
                    }
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(OracleApiConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfessionalApiService::class.java)
    }
}
