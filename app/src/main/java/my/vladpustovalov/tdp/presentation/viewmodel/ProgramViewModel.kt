package my.vladpustovalov.tdp.presentation.viewmodel

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
import my.vladpustovalov.tdp.data.model.Program
import my.vladpustovalov.tdp.data.network.NetworkResult
import my.vladpustovalov.tdp.data.repository.AuthRepository
import my.vladpustovalov.tdp.data.repository.ProgramRepository
import my.vladpustovalov.tdp.domain.state.UiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ProgramViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var programDate by mutableStateOf(LocalDate.now())
        private set

    var isShownPicker by mutableStateOf(false)

    private val _programState = MutableStateFlow<UiState<Program>>(UiState.Loading)
    val programState: StateFlow<UiState<Program>> = _programState.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    init {
        loadProgram(programDate)
    }

    fun loadProgram(date: LocalDate = programDate) {
        programDate = date
        val dateString = date.format(dateFormatter)
        val userId = authRepository.getUserId()

        if (userId == -1) {
            _programState.value = UiState.Error("User ID not found")
            return
        }

        viewModelScope.launch {
            _programState.value = UiState.Loading
            when (val result = programRepository.getProgram(userId, dateString)) {
                is NetworkResult.Success -> {
                    _programState.value = UiState.Success(result.data)
                }
                is NetworkResult.Error -> {
                    _programState.value = UiState.Error("Error ${result.code}: ${result.message ?: "Program not found"}")
                }
                is NetworkResult.Exception -> {
                    _programState.value = UiState.Error(result.e.message ?: "Failed to load program")
                }
            }
        }
    }

    fun loadNextDay() {
        loadProgram(programDate.plusDays(1))
    }

    fun loadPreviousDay() {
        loadProgram(programDate.minusDays(1))
    }
}
