package my.vladpustovalov.tdp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.vladpustovalov.tdp.data.model.User
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.repository.UserRepository
import my.vladpustovalov.tdp.domain.state.UiState
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<UiState<User>>(UiState.Loading)
    val userState: StateFlow<UiState<User>> = _userState.asStateFlow()

    private val _updateState = MutableStateFlow<UiState<User>?>(null)
    val updateState: StateFlow<UiState<User>?> = _updateState.asStateFlow()

    init {
        fetchCurrentUser()
    }

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _userState.value = UiState.Loading
            when (val result = userRepository.getCurrentUser()) {
                is NetworkResult.Success -> {
                    _userState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _userState.value = UiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Exception -> {
                    _userState.value = UiState.Error(result.e.message ?: "Unknown error")
                }
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _updateState.value = UiState.Loading
            when (val result = userRepository.updateUser(user.id, user)) {
                is NetworkResult.Success -> {
                    _updateState.value = UiState.Success(result.data)
                    _userState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _updateState.value = UiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Exception -> {
                    _updateState.value = UiState.Error(result.e.message ?: "Unknown error")
                }
            }
        }
    }
}
