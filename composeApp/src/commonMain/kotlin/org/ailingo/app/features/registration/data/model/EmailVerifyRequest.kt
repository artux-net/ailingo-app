package org.ailingo.app.features.registration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerifyRequest(
    val email: String,
    val verificationCode: String
)