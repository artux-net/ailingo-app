package org.ailingo.app.features.jwt.data.model

import kotlinx.serialization.Serializable
import org.ailingo.app.features.login.data.model.User

@Serializable
data class AuthResponse(
    val token: String,
    val refreshToken: String,
    val user: User,
    val isStreakContinued: Boolean
)