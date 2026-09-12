package my.vladpustovalov.tdp.data.network.api

import my.vladpustovalov.tdp.data.model.Program
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProgramService {
    @GET("program")
    suspend fun getProgram(
        @Query("userId") userId: Int,
        @Query("scheduledDate") scheduledDate: String
    ): Response<Program>
}
