package my.vladpustovalov.tdp.data.network.api

import my.vladpustovalov.tdp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("users/me")
    suspend fun getCurrentUser(): Response<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<User>

    @GET("users")
    suspend fun getAllUsers(): Response<List<User>>

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<User>
}