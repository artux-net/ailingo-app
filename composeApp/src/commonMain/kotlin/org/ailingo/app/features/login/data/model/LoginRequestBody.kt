package org.ailingo.app.features.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestBody(val login: String, val password: String)