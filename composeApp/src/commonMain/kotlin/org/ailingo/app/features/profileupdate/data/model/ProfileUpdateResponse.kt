package org.ailingo.app.features.profileupdate.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateResponse(
    val avatar: String?,
    val coins: Int,
    val email: String,
    val id: String,
    val isEmailVerified: Boolean,
    val lastLoginAt: String,
    val login: String,
    val name: String,
    val registration: String,
    val role: String,
    val streak: Int,
    val xp: Int
)