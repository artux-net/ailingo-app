package org.ailingo.app

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.dictionary
import ailingo.composeapp.generated.resources.favourite_words
import ailingo.composeapp.generated.resources.profile
import ailingo.composeapp.generated.resources.topics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Topic
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource

data class ScreenInfo(
    val route: Any,
    val label: StringResource,
    val icon: ImageVector
)

val screens = listOf(
    ScreenInfo(TopicsPage, Res.string.topics, Icons.Filled.Topic),
    ScreenInfo(DictionaryPage(), Res.string.dictionary, Icons.Filled.Book),
    ScreenInfo(FavouriteWordsPage, Res.string.favourite_words, Icons.Filled.Favorite),
    ScreenInfo(ProfilePage, Res.string.profile, Icons.Filled.Person)
)

@Serializable
object LoginPage

@Serializable
data class ChatPage(
    val topicName: String
)

@Serializable
object RegistrationPage

@Serializable
object TopicsPage

@Serializable
data class DictionaryPage(
    val word: String = ""
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