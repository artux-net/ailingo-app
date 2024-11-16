package org.ailingo.app.core.utils.windowinfo.info

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.ailingo.app.core.utils.windowinfo.util.PlatformName
import org.ailingo.app.getConfiguration
import org.ailingo.app.getPlatformName

@Composable
fun rememberWindowInfo(): WindowInfo {
    val (width, height) = getConfiguration()
    return WindowInfo(
        screenWidthInfo = when {
            width.dp < 840.dp || getPlatformName() == PlatformName.Android -> WindowInfo.WindowType.MobileWindowInfo
            else -> WindowInfo.WindowType.DesktopWindowInfo
        },
        screenHeightInfo = when {
            height.dp < 900.dp || getPlatformName() == PlatformName.Android -> WindowInfo.WindowType.MobileWindowInfo
            else -> WindowInfo.WindowType.DesktopWindowInfo
        },
        screenHeight = height.dp,
        screenWidth = width.dp
    )
}

data class WindowInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class WindowType {
        object MobileWindowInfo : WindowType()
        object DesktopWindowInfo : WindowType()
    }
}
