package org.ailingo.app.core.presentation.model

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.dictionary
import ailingo.composeapp.generated.resources.exit
import ailingo.composeapp.generated.resources.favourite_words
import ailingo.composeapp.generated.resources.free_mode
import ailingo.composeapp.generated.resources.profile
import ailingo.composeapp.generated.resources.topics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRightCircle
import compose.icons.feathericons.Book
import compose.icons.feathericons.MessageCircle
import org.jetbrains.compose.resources.StringResource

sealed class DrawerItems(val title: StringResource, val icon: ImageVector) {
    data object ChatMode : DrawerItems(Res.string.free_mode, FeatherIcons.MessageCircle)
    data object Topics : DrawerItems(Res.string.topics, FeatherIcons.ArrowRightCircle)
    data object Dictionary : DrawerItems(Res.string.dictionary, FeatherIcons.Book)
    data object FavouriteWords : DrawerItems(Res.string.favourite_words, Icons.Filled.Favorite)
    data object Profile : DrawerItems(Res.string.profile, Icons.Filled.Person)
    data object Exit : DrawerItems(Res.string.exit, Icons.AutoMirrored.Filled.ExitToApp)
}
