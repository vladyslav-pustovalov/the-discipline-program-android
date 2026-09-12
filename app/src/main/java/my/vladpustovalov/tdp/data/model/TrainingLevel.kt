package my.vladpustovalov.tdp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TrainingLevel(
    val id: String,
    val levelName: String,
    val description: String? = null
)