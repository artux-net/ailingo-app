@file:OptIn(ExperimentalResourceApi::class)

package org.ailingo.app.core.presentation.utils

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.dictionary
import ailingo.composeapp.generated.resources.exit
import ailingo.composeapp.generated.resources.free_mode
import ailingo.composeapp.generated.resources.topics
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowRightCircle
import compose.icons.feathericons.Book
import compose.icons.feathericons.MessageCircle
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

sealed class DrawerItems(val title: StringResource, val icon: ImageVector) {
    data object FreeMode: DrawerItems(Res.string.free_mode, FeatherIcons.ArrowRightCircle)
    data object Topics: DrawerItems(Res.string.topics, FeatherIcons.MessageCircle)
    data object Dictionary: DrawerItems(Res.string.dictionary, FeatherIcons.Book)
    data object Exit: DrawerItems(Res.string.exit, Icons.AutoMirrored.Filled.ExitToApp)
}