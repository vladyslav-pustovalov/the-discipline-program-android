package my.vladpustovalov.thedisciplineprogram.data.repository

import kotlinx.coroutines.flow.Flow
import my.vladpustovalov.thedisciplineprogram.data.local.TokenManager
import my.vladpustovalov.thedisciplineprogram.data.model.JwtDTO
import my.vladpustovalov.thedisciplineprogram.data.model.SignInDTO
import my.vladpustovalov.thedisciplineprogram.data.network.NetworkResult
import my.vladpustovalov.thedisciplineprogram.data.network.api.AuthService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : BaseRepository() {

    val tokenFlow: Flow<String?> = tokenManager.tokenFlow

    suspend fun signIn(request: SignInDTO): NetworkResult<JwtDTO> {
        val result = safeApiCall { authService.signIn(request) }
        if (result is NetworkResult.Success) {
            tokenManager.saveAuthData(result.data.accessToken, result.data.userId)
        }
        return result
    }

    suspend fun logout() {
        tokenManager.clearToken()
    }

    fun isLoggedInSync(): Boolean {
        return !tokenManager.getTokenSync().isNullOrEmpty()
    }

    suspend fun getUserId(): Int {
        return tokenManager.getUserId()
    }

}
