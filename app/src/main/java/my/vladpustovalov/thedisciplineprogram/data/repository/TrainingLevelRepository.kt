package my.vladpustovalov.thedisciplineprogram.data.repository

import my.vladpustovalov.thedisciplineprogram.data.model.TrainingLevel
import my.vladpustovalov.thedisciplineprogram.data.network.NetworkResult
import my.vladpustovalov.thedisciplineprogram.data.network.api.TrainingLevelService
import javax.inject.Inject

class TrainingLevelRepository @Inject constructor(
    private val trainingLevelService: TrainingLevelService
) : BaseRepository() {

    suspend fun getTrainingLevels(): NetworkResult<List<TrainingLevel>> =
        safeApiCall { trainingLevelService.getTrainingLevels() }

    suspend fun getTrainingLevelById(id: String): NetworkResult<TrainingLevel> =
        safeApiCall { trainingLevelService.getTrainingLevelById(id) }
}