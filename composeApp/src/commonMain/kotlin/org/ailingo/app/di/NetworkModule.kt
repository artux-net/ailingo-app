package org.ailingo.app.di

import AiLingo.composeApp.BuildConfig.BASE_URL
import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.connection_timeout
import ailingo.composeapp.generated.resources.could_not_connect
import ailingo.composeapp.generated.resources.request_timeout
import ailingo.composeapp.generated.resources.unexpected_error
import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.ailingo.app.core.presentation.navigation.NavigationController
import org.ailingo.app.core.presentation.navigation.NavigationEvent
import org.ailingo.app.core.presentation.snackbar.SnackbarController
import org.ailingo.app.core.presentation.snackbar.SnackbarEvent
import org.ailingo.app.features.jwt.data.model.RefreshTokenRequest
import org.ailingo.app.features.jwt.data.model.RefreshTokenResponse
import org.ailingo.app.features.jwt.domain.repository.TokenRepository
import org.jetbrains.compose.resources.getString
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            expectSuccess = false
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                socketTimeoutMillis = 15000
                connectTimeoutMillis = 15000
            }
            install(AuthTokenInterceptor) {
                tokenRepository = get(named("tokenRepository"))
                allowedBaseUrls = listOf(
                    "http://localhost:8080/ailingo",
                    "http://localhost:8081/ailingo",
                    "https://app.artux.net/ailingo"
                )
                excludedPaths = listOf(
                    "/ailingo/api/v1/user/login" to HttpMethod.Post,
                    "/ailingo/api/v1/user/info" to HttpMethod.Get,
                    "/ailingo/api/v1/user/register" to HttpMethod.Post,
                    "/ailingo/api/v1/user/verifyEmail" to HttpMethod.Post,
                    "/ailingo/api/v1/user/refreshToken" to HttpMethod.Post,
                    "/ailingo/api/v1/user/resendVerificationCode" to HttpMethod.Post
                )
            }
        }
    }

    single<ErrorMapper> {
        object : ErrorMapper {
            override suspend fun mapError(throwable: Throwable?, httpResponse: HttpResponse?): String {
                if (httpResponse != null && !httpResponse.status.isSuccess()) {
                    return try {
                        val errorBody = httpResponse.body<JsonObject>()
                        errorBody["message"]?.jsonPrimitive?.content ?: getString(Res.string.unexpected_error)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        getString(Res.string.unexpected_error)
                    }
                }
                return when (throwable) {
                    is HttpRequestTimeoutException -> getString(Res.string.request_timeout)
                    is ConnectTimeoutException -> getString(Res.string.could_not_connect)
                    is SocketTimeoutException -> getString(Res.string.connection_timeout)
                    else -> getString(Res.string.unexpected_error, throwable?.message.toString())
                }
            }
        }
    }
}

interface ErrorMapper {
    suspend fun mapError(throwable: Throwable? = null, httpResponse: HttpResponse? = null): String
}

class AuthTokenInterceptor(config: Config) {

    private val tokenRepository = config.tokenRepository
    private val allowedBaseUrls = config.allowedBaseUrls
    private val excludedPaths = config.excludedPaths

    class Config {
        lateinit var tokenRepository: Deferred<TokenRepository>
        var allowedBaseUrls: List<String> = listOf()
        var excludedPaths: List<Pair<String, HttpMethod>> = listOf()
    }

    companion object Plugin : HttpClientPlugin<Config, AuthTokenInterceptor> {
        override val key: AttributeKey<AuthTokenInterceptor> = AttributeKey("AuthTokenInterceptor")

        override fun prepare(block: Config.() -> Unit): AuthTokenInterceptor {
            return AuthTokenInterceptor(Config().apply(block))
        }

        override fun install(plugin: AuthTokenInterceptor, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                if (!plugin.isTokenRequired(context)) {
                    proceed()
                    return@intercept
                }
                if (plugin.isPathExcluded(context)) {
                    proceed()
                    return@intercept
                }

                val tokens = plugin.tokenRepository.await().getTokens()
                Logger.i("Tokens retrieved: $tokens")
                if (tokens != null) {
                    context.header(HttpHeaders.Authorization, "Bearer ${tokens.token}")
                } else {
                    Logger.i("Tokens are null, Authorization header not added.")
                }
                proceed()
            }
            scope.responsePipeline.intercept(HttpResponsePipeline.Receive) {
                val originalCall = context
                val originalResponse = originalCall.response
                if (originalResponse.status == HttpStatusCode.Forbidden) {
                    Logger.i("Received 403/Forbidden, attempting to refresh token")
                    val refreshedTokens = plugin.refreshToken(plugin.tokenRepository.await(), scope)
                    if (refreshedTokens != null) {
                        Logger.i("Token refreshed successfully, retrying original request")
                        val originalRequest = context.request
                        scope.request {
                            HttpRequestBuilder().apply {
                                takeFrom(originalRequest)
                                headers {
                                    remove(HttpHeaders.Authorization)
                                    set(HttpHeaders.Authorization, "Bearer ${refreshedTokens.accessToken}")
                                }
                            }
                        }
                    } else {
                        //TODO NAVIGATE TO LOGIN
                        CoroutineScope(Dispatchers.Main.immediate).launch {
                            SnackbarController.sendEvent(
                                event = SnackbarEvent(
                                    message = "Token refresh failed, need to re-login",
                                )
                            )
                        }
                        NavigationController.sendNavigationEvent(NavigationEvent.NavigateToLogin)
                        Logger.i("Token refresh failed, user needs to re-login")
                    }
                } else {
                    proceed()
                }
            }
        }
    }

    private suspend fun refreshToken(tokenRepository: TokenRepository, httpClient: HttpClient): RefreshTokenResponse? {
        val currentRefreshToken = tokenRepository.getTokens()?.refreshToken
        if (currentRefreshToken == null) {
            Logger.i("No refresh token found")
            return null
        }

        return try {
            val refreshResponse: HttpResponse = httpClient.post("$BASE_URL/api/v1/user/refreshToken") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(refreshToken = currentRefreshToken))
            }

            if (refreshResponse.status.isSuccess()) {
                val authResponse = refreshResponse.body<RefreshTokenResponse>()
                tokenRepository.saveTokens(RefreshTokenResponse(authResponse.accessToken, authResponse.refreshToken))
                Logger.i("New tokens saved successfully.")
                authResponse
            } else {
                Logger.i("Refresh token request failed with status: ${refreshResponse.status}")
                tokenRepository.deleteTokens()
                null
            }
        } catch (e: Exception) {
            Logger.i("Error during refresh token request: ${e.message}")
            tokenRepository.deleteTokens()
            null
        }
    }

    private fun isTokenRequired(request: HttpRequestBuilder): Boolean {
        val url = request.url.toString()
        return allowedBaseUrls.any { url.startsWith(it) }
    }

    private fun isPathExcluded(request: HttpRequestBuilder): Boolean {
        val path = request.url.encodedPath
        val method = request.method
        return excludedPaths.any { (excludedPath, excludedMethod) ->
            path == excludedPath && method == excludedMethod
        }
    }
}
