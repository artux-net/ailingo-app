package org.ailingo.app.features.registration.utils

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)(@{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}
