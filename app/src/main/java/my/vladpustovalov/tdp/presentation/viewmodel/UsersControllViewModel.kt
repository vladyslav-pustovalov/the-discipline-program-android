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
class UsersControllViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _usersState = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val usersState: StateFlow<UiState<List<User>>> = _usersState.asStateFlow()

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() {
        viewModelScope.launch {
            _usersState.value = UiState.Loading
            when (val result = userRepository.getAllUsers()) {
                is NetworkResult.Success -> {
                    _usersState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _usersState.value = UiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Exception -> {
                    _usersState.value = UiState.Error(result.e.message ?: "Unknown error")
                }
            }
        }
    }
}
