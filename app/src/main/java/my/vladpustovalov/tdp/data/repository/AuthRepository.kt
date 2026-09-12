package my.vladpustovalov.tdp.data.repository

import my.vladpustovalov.tdp.data.local.TokenManager
import my.vladpustovalov.tdp.data.model.JwtDTO
import my.vladpustovalov.tdp.data.model.SignInDTO
import my.vladpustovalov.tdp.data.model.SignUpDTO
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.network.api.AuthService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager
) : BaseRepository() {

    suspend fun signIn(request: SignInDTO): NetworkResult<JwtDTO> {
        val result = safeApiCall { authService.signIn(request) }
        if (result is NetworkResult.Success) {
            tokenManager.saveAuthData(result.data.accessToken, result.data.userId)
        }
        return result
    }

    suspend fun signUp(request: SignUpDTO): NetworkResult<JwtDTO> {
        val result = safeApiCall { authService.signUp(request) }
        if (result is NetworkResult.Success) {
            tokenManager.saveAuthData(result.data.accessToken, result.data.userId)
        }
        return result
    }

    fun logout() {
        tokenManager.clearToken()
    }

    fun isLoggedIn(): Boolean {
        return !tokenManager.getToken().isNullOrEmpty()
    }

    fun getUserId(): Int {
        return tokenManager.getUserId()
    }
}
