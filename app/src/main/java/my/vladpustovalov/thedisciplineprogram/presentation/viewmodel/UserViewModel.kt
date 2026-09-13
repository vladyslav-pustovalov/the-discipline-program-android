package my.vladpustovalov.thedisciplineprogram.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.vladpustovalov.thedisciplineprogram.data.model.User
import my.vladpustovalov.thedisciplineprogram.data.network.NetworkResult
import my.vladpustovalov.thedisciplineprogram.data.repository.AuthRepository
import my.vladpustovalov.thedisciplineprogram.data.repository.UserRepository
import my.vladpustovalov.thedisciplineprogram.domain.state.UiState
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UiState<User>>(UiState.Loading)
    val userState: StateFlow<UiState<User>> = _userState.asStateFlow()

    init {
        loadUser()
    }

    fun loadUser() {
        val userId = authRepository.getUserId()
        if (userId == -1) {
            _userState.value = UiState.Error("User ID not found")
            return
        }

        viewModelScope.launch {
            _userState.value = UiState.Loading
            when (val result = userRepository.getUser(userId)) {
                is NetworkResult.Success -> {
                    _userState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _userState.value = UiState.Error(result.message ?: "Error ${result.code}")
                }
                is NetworkResult.Exception -> {
                    _userState.value = UiState.Error(result.e.message ?: "Failed to load user")
                }
            }
        }
    }

    fun updateUser(user: User) {
        _userState.value = UiState.Success(user)
    }

    fun reloadUser() {
        loadUser()
    }
}
