package my.vladpustovalov.tdp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SignInDTO(
    val email: String,
    val passwordHash: String
)

@Serializable
data class SignUpDTO(
    val email: String,
    val passwordHash: String,
    val firstName: String,
    val lastName: String
)

@Serializable
data class JwtDTO(
    val token: String
)

@Serializable
data class ChangePasswordDTO(
    val oldPasswordHash: String,
    val newPasswordHash: String
)