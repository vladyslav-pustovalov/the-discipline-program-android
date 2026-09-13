package my.vladpustovalov.thedisciplineprogram.data.network.api

import my.vladpustovalov.thedisciplineprogram.data.model.ChangePasswordDTO
import my.vladpustovalov.thedisciplineprogram.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @GET("user/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<User>

    @PUT("user")
    suspend fun updateUser(@Body user: User): Response<User>

    @PATCH("user/changePassword")
    suspend fun changePassword(@Body request: ChangePasswordDTO): Response<Unit>
}
