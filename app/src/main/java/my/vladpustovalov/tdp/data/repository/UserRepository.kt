package my.vladpustovalov.tdp.data.repository

import my.vladpustovalov.tdp.data.model.ChangePasswordDTO
import my.vladpustovalov.tdp.data.model.User
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.network.api.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) : BaseRepository() {

    suspend fun getUser(id: Int): NetworkResult<User> =
        safeApiCall { userService.getUser(id) }

    suspend fun updateUser(user: User): NetworkResult<User> =
        safeApiCall { userService.updateUser(user) }

    suspend fun changePassword(request: ChangePasswordDTO): NetworkResult<Unit> =
        safeApiCall { userService.changePassword(request) }
}
