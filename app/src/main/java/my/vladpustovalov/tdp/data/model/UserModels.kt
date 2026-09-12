package my.vladpustovalov.tdp.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    USER, ADMIN, COACH
}

@Serializable
data class Team(
    val id: String,
    val name: String
)

@Serializable
data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val teamId: String? = null
)

@Serializable
data class UserPlan(
    val id: String,
    val userId: String,
    val programId: String,
    val startDate: String,
    val active: Boolean
)