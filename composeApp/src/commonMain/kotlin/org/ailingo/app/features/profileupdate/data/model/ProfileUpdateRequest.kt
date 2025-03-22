package org.ailingo.app.features.profileupdate.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(
    val name: String,
    val email: String,
    val avatar: String?,
    val newPassword: String?,
    val oldPassword: String
)