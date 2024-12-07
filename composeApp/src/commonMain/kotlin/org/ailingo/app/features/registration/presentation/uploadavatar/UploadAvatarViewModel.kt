package org.ailingo.app.features.registration.presentation.uploadavatar

import AiLingo.composeApp.BuildConfig.API_ENDPOINT_USER
import AiLingo.composeApp.BuildConfig.BASE_URL
import AiLingo.composeApp.BuildConfig.BASE_URL_UPLOAD_IMAGE
import AiLingo.composeApp.BuildConfig.UPLOAD_IMAGE_KEY
import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.connection_timeout
import ailingo.composeapp.generated.resources.could_not_connect
import ailingo.composeapp.generated.resources.request_timeout
import ailingo.composeapp.generated.resources.unexpected_error
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.ailingo.app.features.registration.data.image.UploadImageResponse
import org.ailingo.app.features.registration.data.model.SuccessRegister
import org.ailingo.app.features.registration.data.model.UserRegistrationData
import org.jetbrains.compose.resources.getString

class UploadAvatarViewModel : ViewModel() {
    fun onEvent(event: UploadAvatarEvent) {
        when (event) {
            UploadAvatarEvent.OnBackToEmptyRegisterState -> {
                _registerState.update { RegisterApiState.Empty }
            }

            UploadAvatarEvent.OnBackToEmptyUploadAvatar -> {
                _imageState.update { UploadImageUiState.EmptyImage }
            }

            is UploadAvatarEvent.OnUploadImage -> {
                uploadImage(event.image)
            }

            is UploadAvatarEvent.RegisterUser -> {
                registerUser(event.user)
            }
        }
    }

    private val _registerState = MutableStateFlow<RegisterApiState>(RegisterApiState.Empty)
    val registerState: StateFlow<RegisterApiState> = _registerState.asStateFlow()

    fun registerUser(user: UserRegistrationData) {
        viewModelScope.launch {
            _registerState.value = RegisterApiState.Loading
            val httpClient = HttpClient {
                install(ContentNegotiation) {
                    json()
                }
                install(Logging)
                install(HttpTimeout) {
                    requestTimeoutMillis = 15000
                    socketTimeoutMillis = 15000
                    connectTimeoutMillis = 15000
                }
            }
            try {
                val response = httpClient.post("$BASE_URL$API_ENDPOINT_USER/register") {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    setBody(user)
                }
                _registerState.value = when {
                    response.status.isSuccess() -> {
                        val body = response.body<SuccessRegister>()
                        if (body.success) {
                            RegisterApiState.Success(
                                body.success,
                                body.code,
                                body.description,
                                body.failure
                            )
                        } else {
                            RegisterApiState.Error(body.description)
                        }
                    }
                    else -> RegisterApiState.Error("Request failed with $response")
                }
            } catch (e: HttpRequestTimeoutException) {
                _registerState.update { RegisterApiState.Error(getString(Res.string.request_timeout)) }
            } catch (e: ConnectTimeoutException) {
                _registerState.update { RegisterApiState.Error(getString(Res.string.could_not_connect)) }
            } catch (e: SocketTimeoutException) {
                _registerState.update { RegisterApiState.Error(getString(Res.string.connection_timeout)) }
            } catch (e: Exception) {
                _registerState.update {
                    RegisterApiState.Error(
                        getString(
                            Res.string.unexpected_error,
                            e.message ?: ""
                        )
                    )
                }
            } finally {
                httpClient.close()
            }
        }
    }

    private val _imageState = MutableStateFlow<UploadImageUiState>(UploadImageUiState.EmptyImage)
    val imageState = _imageState.asStateFlow()

    @OptIn(InternalAPI::class, InternalAPI::class)
    fun uploadImage(base64Image: String) {
        _imageState.value = UploadImageUiState.LoadingImage
        val httpClient = HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
            }
        }
        viewModelScope.launch {
            try {
                val response = httpClient.post("$BASE_URL_UPLOAD_IMAGE?key=$UPLOAD_IMAGE_KEY") {
                    body = MultiPartFormDataContent(
                        formData {
                            append("image", base64Image)
                        }
                    )
                }
                if (response.status.isSuccess()) {
                    val body = response.body<UploadImageResponse>()
                    _imageState.value = UploadImageUiState.Success(uploadImageResponse = body)
                } else {
                    _imageState.value = UploadImageUiState.Error("error: ${response.status}")
                }
            } catch (e: Exception) {
                _imageState.value = UploadImageUiState.Error("error: ${e.message}")
            } finally {
                httpClient.close()
            }
        }
    }
}