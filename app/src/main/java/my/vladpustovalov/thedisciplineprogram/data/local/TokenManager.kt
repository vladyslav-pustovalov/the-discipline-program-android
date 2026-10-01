package my.vladpustovalov.thedisciplineprogram.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class TokenManager(private val context: Context) {

    companion object {
        private val KEY_JWT_TOKEN = stringPreferencesKey("jwt_token")
        private val KEY_USER_ID = intPreferencesKey("user_id")
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_JWT_TOKEN]
    }

    val userIdFlow: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[KEY_USER_ID] ?: -1
    }

    suspend fun saveAuthData(token: String, userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_JWT_TOKEN] = token
            preferences[KEY_USER_ID] = userId
        }
    }

    fun getTokenSync(): String? = runBlocking {
        context.dataStore.data.map { it[KEY_JWT_TOKEN] }.firstOrNull()
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[KEY_JWT_TOKEN] }.firstOrNull()
    }

    fun getUserIdSync(): Int = runBlocking {
        context.dataStore.data.map { it[KEY_USER_ID] ?: -1 }.firstOrNull() ?: -1
    }

    suspend fun getUserId(): Int {
        return context.dataStore.data.map { it[KEY_USER_ID] ?: -1 }.firstOrNull() ?: -1
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_JWT_TOKEN)
            preferences.remove(KEY_USER_ID)
        }
    }
}
