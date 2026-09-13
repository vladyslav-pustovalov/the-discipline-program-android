package my.vladpustovalov.thedisciplineprogram.data.repository

import my.vladpustovalov.thedisciplineprogram.data.model.UserPlan
import my.vladpustovalov.thedisciplineprogram.data.network.NetworkResult
import my.vladpustovalov.thedisciplineprogram.data.network.api.UserPlansService
import javax.inject.Inject

class UserPlansRepository @Inject constructor(
    private val userPlansService: UserPlansService
) : BaseRepository() {

    suspend fun getUserPlans(userId: String): NetworkResult<List<UserPlan>> =
        safeApiCall { userPlansService.getUserPlans(userId) }

    suspend fun getUserPlanById(id: String): NetworkResult<UserPlan> =
        safeApiCall { userPlansService.getUserPlanById(id) }
}