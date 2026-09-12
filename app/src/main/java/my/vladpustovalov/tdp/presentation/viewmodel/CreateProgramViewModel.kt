package my.vladpustovalov.tdp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import my.vladpustovalov.tdp.data.model.Block
import my.vladpustovalov.tdp.data.model.DailyProgram
import my.vladpustovalov.tdp.data.model.DayTrainigs
import my.vladpustovalov.tdp.data.model.Program
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateProgramViewModel @Inject constructor() : ViewModel() {

    private val _programState = MutableStateFlow(
        Program(
            id = UUID.randomUUID().toString(),
            title = "",
            description = null,
            dailyPrograms = emptyList()
        )
    )
    val programState: StateFlow<Program> = _programState.asStateFlow()

    fun updateProgramInfo(title: String, description: String?) {
        _programState.update { it.copy(title = title, description = description) }
    }

    fun addDailyProgram(title: String, dayNumber: Int) {
        val newDaily = DailyProgram(
            id = UUID.randomUUID().toString(),
            dayNumber = dayNumber,
            title = title,
            dayTrainings = emptyList()
        )
        _programState.update { program ->
            program.copy(dailyPrograms = program.dailyPrograms + newDaily)
        }
    }

    fun addDayTraining(dailyProgramId: String, title: String) {
        val newTraining = DayTrainigs(
            id = UUID.randomUUID().toString(),
            title = title,
            blocks = emptyList()
        )
        _programState.update { program ->
            program.copy(
                dailyPrograms = program.dailyPrograms.map { daily ->
                    if (daily.id == dailyProgramId) {
                        daily.copy(dayTrainings = daily.dayTrainings + newTraining)
                    } else {
                        daily
                    }
                }
            )
        }
    }

    fun addBlock(dailyProgramId: String, trainingId: String, name: String, exercises: String) {
        val newBlock = Block(
            id = UUID.randomUUID().toString(),
            name = name,
            exercises = exercises
        )
        _programState.update { program ->
            program.copy(
                dailyPrograms = program.dailyPrograms.map { daily ->
                    if (daily.id == dailyProgramId) {
                        daily.copy(
                            dayTrainings = daily.dayTrainings.map { training ->
                                if (training.id == trainingId) {
                                    training.copy(blocks = training.blocks + newBlock)
                                } else {
                                    training
                                }
                            }
                        )
                    } else {
                        daily
                    }
                }
            )
        }
    }
}
