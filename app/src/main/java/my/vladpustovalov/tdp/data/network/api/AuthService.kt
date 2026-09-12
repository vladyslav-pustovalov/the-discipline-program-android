package my.vladpustovalov.tdp.data.network.api

import my.vladpustovalov.tdp.data.model.JwtDTO
import my.vladpustovalov.tdp.data.model.SignInDTO
import my.vladpustovalov.tdp.data.model.SignUpDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/signin")
    suspend fun signIn(@Body request: SignInDTO): Response<JwtDTO>

    @POST("auth/signup")
    suspend fun signUp(@Body request: SignUpDTO): Response<JwtDTO>
}
