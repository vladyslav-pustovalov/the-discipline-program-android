package my.vladpustovalov.tdp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Program(
    val id: String,
    val title: String,
    val description: String?,
    val dailyPrograms: List<DailyProgram> = emptyList()
)

@Serializable
data class DailyProgram(
    val id: String,
    val dayNumber: Int,
    val title: String,
    val dayTrainings: List<DayTrainigs> = emptyList()
)

@Serializable
data class DayTrainigs(
    val id: String,
    val title: String,
    val blocks: List<Block> = emptyList()
)

@Serializable
data class Block(
    val id: String,
    val name: String,
    val exercises: String
)