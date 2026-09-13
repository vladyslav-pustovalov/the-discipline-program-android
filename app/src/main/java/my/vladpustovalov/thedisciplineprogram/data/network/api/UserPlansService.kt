package my.vladpustovalov.thedisciplineprogram.data.network.api

import my.vladpustovalov.thedisciplineprogram.data.model.UserPlan
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserPlansService {
    @GET("user-plans/user/{userId}")
    suspend fun getUserPlans(@Path("userId") userId: String): Response<List<UserPlan>>

    @GET("user-plans/{id}")
    suspend fun getUserPlanById(@Path("id") id: String): Response<UserPlan>
}