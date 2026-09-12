package my.vladpustovalov.tdp.data.repository

import my.vladpustovalov.tdp.data.model.Program
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.network.api.ProgramService
import javax.inject.Inject

class ProgramRepository @Inject constructor(
    private val programService: ProgramService
) : BaseRepository() {

    suspend fun getPrograms(): NetworkResult<List<Program>> =
        safeApiCall { programService.getPrograms() }

    suspend fun getProgramById(id: String): NetworkResult<Program> =
        safeApiCall { programService.getProgramById(id) }
}