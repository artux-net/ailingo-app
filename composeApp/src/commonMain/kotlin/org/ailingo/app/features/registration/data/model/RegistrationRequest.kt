package org.ailingo.app.features.registration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val login: String,
    val password: String,
    val email: String,
    val name: String
)