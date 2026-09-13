package my.vladpustovalov.tdp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import my.vladpustovalov.tdp.data.model.User
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phoneNumber by mutableStateOf("")
    var dateOfBirth by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    var showingAlert by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    fun initUser(user: User) {
        firstName = user.firstName ?: ""
        lastName = user.lastName ?: ""
        phoneNumber = user.phoneNumber ?: ""
        dateOfBirth = user.dateOfBirth ?: ""
    }

    private fun updatedUser(currentUser: User): User {
        return User(
            id = currentUser.id,
            isEnabled = currentUser.isEnabled,
            username = currentUser.username,
            userRole = currentUser.userRole,
            trainingLevel = currentUser.trainingLevel,
            userPlan = currentUser.userPlan,
            team = currentUser.team,
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            dateOfBirth = dateOfBirth
        )
    }

    fun saveUpdatedUser(currentUser: User, onSuccess: (User) -> Unit) {
        val userToSave = updatedUser(currentUser)

        viewModelScope.launch {
            isLoading = true
            showingAlert = false
            when (val result = userRepository.updateUser(userToSave)) {
                is NetworkResult.Success -> {
                    isLoading = false
                    onSuccess(result.data)
                }
                is NetworkResult.Error -> {
                    isLoading = false
                    errorMessage = result.message ?: "Failed to update user (code: ${result.code})"
                    showingAlert = true
                }
                is NetworkResult.Exception -> {
                    isLoading = false
                    errorMessage = result.e.message ?: "Failed to update user"
                    showingAlert = true
                }
            }
        }
    }
}
