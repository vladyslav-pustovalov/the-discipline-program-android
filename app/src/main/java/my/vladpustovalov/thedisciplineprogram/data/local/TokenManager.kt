package my.vladpustovalov.thedisciplineprogram.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenManager(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val KEY_JWT_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "user_id"
    }

    fun saveAuthData(token: String, userId: Int) {
        sharedPreferences.edit()
            .putString(KEY_JWT_TOKEN, token)
            .putInt(KEY_USER_ID, userId)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null)
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(KEY_USER_ID, -1)
    }

    fun clearToken() {
        sharedPreferences.edit()
            .remove(KEY_JWT_TOKEN)
            .remove(KEY_USER_ID)
            .apply()
    }
}
