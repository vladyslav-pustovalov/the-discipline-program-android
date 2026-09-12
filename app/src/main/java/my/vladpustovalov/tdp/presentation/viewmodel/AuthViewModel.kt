package my.vladpustovalov.tdp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.vladpustovalov.tdp.data.model.SignInDTO
import my.vladpustovalov.tdp.data.model.SignUpDTO
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.repository.AuthRepository
import my.vladpustovalov.tdp.domain.state.UiState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<UiState<Unit>?>(null)
    val authState: StateFlow<UiState<Unit>?> = _authState.asStateFlow()

    fun signIn(signInDTO: SignInDTO) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            when (val result = authRepository.signIn(signInDTO)) {
                is NetworkResult.Success -> {
                    _authState.value = UiState.Success(Unit)
                }
                is NetworkResult.Error -> {
                    _authState.value = UiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Exception -> {
                    _authState.value = UiState.Error(result.e.message ?: "Unknown error")
                }
            }
        }
    }

    fun signUp(signUpDTO: SignUpDTO) {
        viewModelScope.launch {
            _authState.value = UiState.Loading
            when (val result = authRepository.signUp(signUpDTO)) {
                is NetworkResult.Success -> {
                    _authState.value = UiState.Success(Unit)
                }
                is NetworkResult.Error -> {
                    _authState.value = UiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Exception -> {
                    _authState.value = UiState.Error(result.e.message ?: "Unknown error")
                }
            }
        }
    }
    
    fun logout() {
        authRepository.logout()
        _authState.value = null
    }
}
