package br.com.salaopremiun.profissional.core.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "professional_session")

class SessionStore(
    private val context: Context,
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")

    val accessToken: Flow<String?> = context.sessionDataStore.data.map { preferences ->
        preferences[accessTokenKey]
    }

    val refreshToken: Flow<String?> = context.sessionDataStore.data.map { preferences ->
        preferences[refreshTokenKey]
    }

    val session: Flow<ProfessionalSession?> = context.sessionDataStore.data.map { preferences ->
        val accessToken = preferences[accessTokenKey]
        val refreshToken = preferences[refreshTokenKey]
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            null
        } else {
            ProfessionalSession(accessToken = accessToken, refreshToken = refreshToken)
        }
    }

    suspend fun saveSession(session: ProfessionalSession) {
        context.sessionDataStore.edit { preferences ->
            preferences[accessTokenKey] = session.accessToken
            preferences[refreshTokenKey] = session.refreshToken
        }
    }

    suspend fun clear() {
        context.sessionDataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
        }
    }
}

data class ProfessionalSession(
    val accessToken: String,
    val refreshToken: String,
)
