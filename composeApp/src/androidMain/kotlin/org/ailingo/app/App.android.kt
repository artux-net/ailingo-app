package org.ailingo.app

import android.Manifest
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.ailingo.app.core.utils.voice.VoiceToTextParser
import org.ailingo.app.features.dictionary.history.di.AppModule

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()

        setContent {
            var canRecord by remember {
                mutableStateOf(false)
            }

            val voiceToTextParser by lazy {
                VoiceToTextParser()
            }

            val recordAudioLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { isGranted ->
                    canRecord = isGranted
                }
            )

            LaunchedEffect(key1 = recordAudioLauncher) {
                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }

            App(
                voiceToTextParser,
                AppModule(this).dictionaryRepository
            )
        }
    }
}
