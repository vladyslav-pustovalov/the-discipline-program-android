package my.vladpustovalov.thedisciplineprogram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Program(
    val id: Int,
    val scheduledDate: String? = null,
    val trainingLevel: TrainingLevel? = null,
    val userId: Int? = null,
    val isRestDay: Boolean = false,
    val dailyProgram: DailyProgram? = null
)

@Serializable
data class DailyProgram(
    val dayTrainings: List<DayTraining> = emptyList()
)

@Serializable
data class DayTraining(
    val trainingNumber: Int,
    val blocks: List<Block> = emptyList()
)

@Serializable
data class Block(
    val name: String,
    val exercises: List<String> = emptyList()
)
