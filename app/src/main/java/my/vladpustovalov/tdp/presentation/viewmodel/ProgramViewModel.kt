package my.vladpustovalov.tdp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import my.vladpustovalov.tdp.data.model.Program
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.repository.ProgramRepository
import my.vladpustovalov.tdp.domain.state.UiState
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel @Inject constructor(
    private val programRepository: ProgramRepository
) : ViewModel() {

    private val _programsState = MutableStateFlow<UiState<List<Program>>>(UiState.Loading)
    val programsState: StateFlow<UiState<List<Program>>> = _programsState.asStateFlow()

    private val _programDetailState = MutableStateFlow<UiState<Program>?>(null)
    val programDetailState: StateFlow<UiState<Program>?> = _programDetailState.asStateFlow()

    init {
        fetchPrograms()
    }

    fun fetchPrograms() {
        viewModelScope.launch {
            _programsState.value = UiState.Loading
            when (val result = programRepository.getPrograms()) {
                is NetworkResult.Success -> {
                    _programsState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _programsState.value = UiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Exception -> {
                    _programsState.value = UiState.Error(result.e.message ?: "Unknown error")
                }
            }
        }
    }

    fun fetchProgramById(id: String) {
        viewModelScope.launch {
            _programDetailState.value = UiState.Loading
            when (val result = programRepository.getProgramById(id)) {
                is NetworkResult.Success -> {
                    _programDetailState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _programDetailState.value = UiState.Error(result.message ?: "Unknown error")
                }
                is NetworkResult.Exception -> {
                    _programDetailState.value = UiState.Error(result.e.message ?: "Unknown error")
                }
            }
        }
    }
}
