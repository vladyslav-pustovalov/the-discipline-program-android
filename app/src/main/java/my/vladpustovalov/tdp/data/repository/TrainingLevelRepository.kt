package my.vladpustovalov.tdp.data.repository

import my.vladpustovalov.tdp.data.model.TrainingLevel
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.network.api.TrainingLevelService
import javax.inject.Inject

class TrainingLevelRepository @Inject constructor(
    private val trainingLevelService: TrainingLevelService
) : BaseRepository() {

    suspend fun getTrainingLevels(): NetworkResult<List<TrainingLevel>> =
        safeApiCall { trainingLevelService.getTrainingLevels() }

    suspend fun getTrainingLevelById(id: String): NetworkResult<TrainingLevel> =
        safeApiCall { trainingLevelService.getTrainingLevelById(id) }
}