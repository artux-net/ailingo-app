package org.ailingo.app.features.registration.presentation.email_verification

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.back
import ailingo.composeapp.generated.resources.verify_email_subtitle
import ailingo.composeapp.generated.resources.verify_email_title
import ailingo.composeapp.generated.resources.verifying_code
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.jwt.data.model.AuthResponse
import org.jetbrains.compose.resources.stringResource

@Composable
fun VerifyEmailScreen(
    email: String,
    registrationState: UiState<AuthResponse>,
    otpState: OtpState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
    onNavigateChatScreen: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack:()->Unit
) {
    LaunchedEffect(key1 = registrationState) {
        if (registrationState is UiState.Success) {
            onNavigateChatScreen()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(Res.string.verify_email_title, email),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(Res.string.verify_email_subtitle),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
        ) {
            otpState.code.forEachIndexed { index, number ->
                OtpInputField(
                    number = number,
                    focusRequester = focusRequesters[index],
                    onFocusChanged = { isFocused ->
                        if (isFocused) {
                            onAction(OtpAction.OnChangeFieldFocused(index))
                        }
                    },
                    onNumberChanged = { newNumber ->
                        onAction(OtpAction.OnEnterNumber(newNumber, index))
                    },
                    onKeyboardBack = {
                        onAction(OtpAction.OnKeyboardBack)
                    },
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (registrationState) {
            is UiState.Loading -> {
                LoadingScreen(loadingText = stringResource(Res.string.verifying_code))
            }

            is UiState.Error -> {
                ErrorScreen(errorMessage = registrationState.message)
            }

            else -> {}
        }
        Spacer(modifier = Modifier.weight(1f))
        ElevatedButton(onClick = {
            onNavigateBack()
        }) {
            Text(stringResource(Res.string.back))
        }
    }
}