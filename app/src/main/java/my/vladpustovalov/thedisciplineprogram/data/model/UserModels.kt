package my.vladpustovalov.thedisciplineprogram.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRole(
    val id: Int,
    val name: String
) {
    companion object {
        val ROLE_USER = UserRole(1, "USER")
        val ROLE_ADMIN = UserRole(2, "ADMIN")
    }
}

@Serializable
data class UserPlan(
    val id: Int,
    val name: String
) {
    companion object {
        val GENERAL = UserPlan(1, "General")
        val INDIVIDUAL = UserPlan(2, "Individual")
    }
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
) {
    val visibleName: String
        get() = if (!firstName.isNullOrBlank() && !lastName.isNullOrBlank()) {
            "$firstName $lastName"
        } else {
            username
        }
}
