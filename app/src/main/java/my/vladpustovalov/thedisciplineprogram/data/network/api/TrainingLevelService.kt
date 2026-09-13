package my.vladpustovalov.thedisciplineprogram.data.network.api

import my.vladpustovalov.thedisciplineprogram.data.model.TrainingLevel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrainingLevelService {
    @GET("training-levels")
    suspend fun getTrainingLevels(): Response<List<TrainingLevel>>

    @GET("training-levels/{id}")
    suspend fun getTrainingLevelById(@Path("id") id: String): Response<TrainingLevel>
}