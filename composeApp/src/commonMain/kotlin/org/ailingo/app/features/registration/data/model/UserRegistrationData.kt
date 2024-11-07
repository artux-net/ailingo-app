package org.ailingo.app.features.registration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationData(
    val login: String,
    val password: String,
    val email: String,
    val name: String,
    val avatar: String? = null
)
