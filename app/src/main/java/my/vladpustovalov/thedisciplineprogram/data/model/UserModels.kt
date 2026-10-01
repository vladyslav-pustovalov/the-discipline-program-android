package my.vladpustovalov.thedisciplineprogram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRole(
    val id: Int,
    val name: String
) {
    companion object
}

@Serializable
data class UserPlan(
    val id: Int,
    val name: String
) {
    companion object
}

@Serializable
data class Team(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class User(
    val id: Int,
    val isEnabled: Boolean = true,
    val username: String,
    val userRole: UserRole? = null,
    val trainingLevel: TrainingLevel? = null,
    val userPlan: UserPlan? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val dateOfBirth: String? = null,
    val team: Team? = null,
    val phoneNumber: String? = null
)
