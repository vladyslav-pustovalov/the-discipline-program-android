package my.vladpustovalov.tdp.data.repository

import my.vladpustovalov.tdp.data.model.UserPlan
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.network.api.UserPlansService
import javax.inject.Inject

class UserPlansRepository @Inject constructor(
    private val userPlansService: UserPlansService
) : BaseRepository() {

    suspend fun getUserPlans(userId: String): NetworkResult<List<UserPlan>> =
        safeApiCall { userPlansService.getUserPlans(userId) }

    suspend fun getUserPlanById(id: String): NetworkResult<UserPlan> =
        safeApiCall { userPlansService.getUserPlanById(id) }
}