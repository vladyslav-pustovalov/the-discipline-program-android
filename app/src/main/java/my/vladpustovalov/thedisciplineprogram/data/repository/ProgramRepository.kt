package my.vladpustovalov.thedisciplineprogram.data.repository

import my.vladpustovalov.thedisciplineprogram.data.model.Program
import my.vladpustovalov.thedisciplineprogram.data.network.NetworkResult
import my.vladpustovalov.thedisciplineprogram.data.network.api.ProgramService
import javax.inject.Inject

class ProgramRepository @Inject constructor(
    private val programService: ProgramService
) : BaseRepository() {

    suspend fun getProgram(userId: Int, scheduledDate: String): NetworkResult<Program> =
        safeApiCall { programService.getProgram(userId, scheduledDate) }
}
