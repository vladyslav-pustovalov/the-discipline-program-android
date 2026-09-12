package my.vladpustovalov.tdp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SignInDTO(
    val username: String,
    val password: String
)

@Serializable
data class SignUpDTO(
    val username: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null
)

@Serializable
data class JwtDTO(
    val userId: Int,
    val accessToken: String,
    val userRole: UserRole? = null,
    val userPlan: UserPlan? = null
)

@Serializable
data class ChangePasswordDTO(
    val userId: Int,
    val oldPassword: String,
    val newPassword: String
)
