package my.vladpustovalov.tdp.data.repository

import my.vladpustovalov.tdp.data.model.User
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.network.api.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) : BaseRepository() {

    suspend fun getCurrentUser(): NetworkResult<User> =
        safeApiCall { userService.getCurrentUser() }

    suspend fun getUserById(id: String): NetworkResult<User> =
        safeApiCall { userService.getUserById(id) }

    suspend fun getAllUsers(): NetworkResult<List<User>> =
        safeApiCall { userService.getAllUsers() }

    suspend fun updateUser(id: String, user: User): NetworkResult<User> =
        safeApiCall { userService.updateUser(id, user) }
}