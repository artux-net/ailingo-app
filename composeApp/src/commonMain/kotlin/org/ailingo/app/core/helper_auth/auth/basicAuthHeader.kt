package org.ailingo.app.core.helper_auth.auth

import io.ktor.util.encodeBase64
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray

fun basicAuthHeader(name: String, password: String): String {
    val authString = "$name:$password"
    val authBuf = authString.toByteArray(Charsets.UTF_8).encodeBase64()
    return "Basic $authBuf"
}