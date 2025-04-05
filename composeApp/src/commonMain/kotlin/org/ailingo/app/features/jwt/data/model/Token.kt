package org.ailingo.app.features.jwt.data.model

data class Token(
    val id: Long,
    val token: String,
    val refreshToken: String
)