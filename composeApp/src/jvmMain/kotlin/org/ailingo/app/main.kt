package org.ailingo.app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.ailingo.app.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            title = "ailingo",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            onCloseRequest = ::exitApplication,
        ) {
            App()
        }
    }
}
