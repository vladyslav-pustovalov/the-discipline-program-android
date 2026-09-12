package my.vladpustovalov.tdp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import my.vladpustovalov.tdp.data.model.ChangePasswordDTO
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.repository.AuthRepository
import my.vladpustovalov.tdp.data.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var oldPassword by mutableStateOf("")
    var newPassword by mutableStateOf("")
    var confirmNewPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    var showingAlert by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    val isOldAndNewPasswordsTheSame: Boolean
        get() = newPassword.isNotEmpty() && oldPassword == newPassword

    val isNewPasswordConfirmed: Boolean
        get() = newPassword == confirmNewPassword

    val isValidPassword: Boolean
        get() = newPassword.length >= 6

    val isSaveButtonDisabled: Boolean
        get() = oldPassword.isBlank() ||
                newPassword.isBlank() ||
                confirmNewPassword.isBlank() ||
                isOldAndNewPasswordsTheSame ||
                !isNewPasswordConfirmed ||
                !isValidPassword ||
                isLoading

    fun saveNewPassword(onSuccess: () -> Unit) {
        val userId = authRepository.getUserId()
        if (userId == -1 || isSaveButtonDisabled) return

        viewModelScope.launch {
            isLoading = true
            showingAlert = false
            val dto = ChangePasswordDTO(
                userId = userId,
                oldPassword = oldPassword,
                newPassword = newPassword
            )
            when (val result = userRepository.changePassword(dto)) {
                is NetworkResult.Success -> {
                    isLoading = false
                    authRepository.logout()
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    isLoading = false
                    errorMessage = result.message ?: "Failed to change password (code: ${result.code})"
                    showingAlert = true
                }
                is NetworkResult.Exception -> {
                    isLoading = false
                    errorMessage = result.e.message ?: "Failed to change password"
                    showingAlert = true
                }
            }
        }
    }
}
