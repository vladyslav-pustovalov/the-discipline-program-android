package my.vladpustovalov.thedisciplineprogram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TrainingLevel(
    val id: Int,
    val name: String
)
