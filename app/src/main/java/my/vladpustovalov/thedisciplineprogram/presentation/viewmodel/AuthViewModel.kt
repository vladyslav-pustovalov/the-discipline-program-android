package my.vladpustovalov.thedisciplineprogram.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.vladpustovalov.thedisciplineprogram.data.model.SignInDTO
import my.vladpustovalov.thedisciplineprogram.data.network.NetworkResult
import my.vladpustovalov.thedisciplineprogram.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    var showingAlert by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    private val _isLoggedIn = MutableStateFlow(authRepository.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    val isLoginButtonDisabled: Boolean
        get() = email.isBlank() || password.isBlank() || isLoading

    fun performLogin(onSuccess: () -> Unit = {}) {
        if (isLoginButtonDisabled) return

        viewModelScope.launch {
            isLoading = true
            showingAlert = false
            when (val result = authRepository.signIn(SignInDTO(username = email, password = password))) {
                is NetworkResult.Success -> {
                    isLoading = false
                    _isLoggedIn.value = true
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    isLoading = false
                    errorMessage = result.message ?: "Authentication failed (code: ${result.code})"
                    showingAlert = true
                }
                is NetworkResult.Exception -> {
                    isLoading = false
                    errorMessage = result.e.message ?: "Authentication failed"
                    showingAlert = true
                }
            }
        }
    }

    fun signOut() {
        authRepository.logout()
        _isLoggedIn.value = false
        email = ""
        password = ""
    }
}
