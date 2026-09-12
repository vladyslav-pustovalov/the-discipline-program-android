package my.vladpustovalov.tdp.data.network.api

import my.vladpustovalov.tdp.data.model.Program
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProgramService {
    @GET("programs")
    suspend fun getPrograms(): Response<List<Program>>

    @GET("programs/{id}")
    suspend fun getProgramById(@Path("id") id: String): Response<Program>
}