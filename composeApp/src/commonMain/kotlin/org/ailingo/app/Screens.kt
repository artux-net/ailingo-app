package org.ailingo.app

import kotlinx.serialization.Serializable

@Serializable
object LoginPage

@Serializable
object ChatPage

@Serializable
object RegistrationPage

@Serializable
object GetStartedPage

@Serializable
object TopicsPage

@Serializable
data class DictionaryPage(
    val word: String?
)

@Serializable
object ProfilePage

@Serializable
data class ProfileUpdatePage(
    val name: String,
    val email: String,
    var avatar: String?
)

@Serializable
object FavouriteWordsPage

@Serializable
data class VerifyEmailPage(
    val email: String,
    val password: String,
)