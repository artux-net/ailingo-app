package org.ailingo.app.features.profileupdate.presentation

import ailingo.composeapp.generated.resources.Res
import ailingo.composeapp.generated.resources.back
import ailingo.composeapp.generated.resources.change_of_personal_data
import ailingo.composeapp.generated.resources.change_user_data
import ailingo.composeapp.generated.resources.current_password
import ailingo.composeapp.generated.resources.data_changed_successfully
import ailingo.composeapp.generated.resources.email
import ailingo.composeapp.generated.resources.enter_current_password
import ailingo.composeapp.generated.resources.enter_your_email
import ailingo.composeapp.generated.resources.enter_your_name
import ailingo.composeapp.generated.resources.enter_your_new_password
import ailingo.composeapp.generated.resources.name
import ailingo.composeapp.generated.resources.new_password
import ailingo.composeapp.generated.resources.visibility
import ailingo.composeapp.generated.resources.visibility_off
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.ailingo.app.core.presentation.ErrorScreen
import org.ailingo.app.core.presentation.LoadingScreen
import org.ailingo.app.core.presentation.UiState
import org.ailingo.app.features.login.presentation.VerticalSpacer
import org.ailingo.app.features.profileupdate.data.model.ProfileUpdateRequest
import org.ailingo.app.features.profileupdate.data.model.ProfileUpdateResponse
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileUpdateScreen(
    profileUpdateUiState: UiState<ProfileUpdateResponse>,
    onProfileUpdate: (ProfileUpdateRequest) -> Unit,
    onReLoginUser: (newLogin: String, currentPassword: String, newPassword: String, passwordChanged: Boolean) -> Unit,
    onNavigateProfileScreen: () -> Unit,
    onBackToEmptyState: () -> Unit,
    name: String,
    email: String,
    avatar: String?
) {

    val newName = remember {
        mutableStateOf(name)
    }
    val newEmail = remember {
        mutableStateOf(email)
    }

    val newPassword = remember {
        mutableStateOf("")
    }

    val currentPassword = remember {
        mutableStateOf("")
    }

    LaunchedEffect(profileUpdateUiState) {
        if (profileUpdateUiState is UiState.Success) {
            delay(1000L)
            onBackToEmptyState()
            val passwordChanged = newPassword.value.isNotEmpty()
            onReLoginUser(newEmail.value, currentPassword.value, newPassword.value, passwordChanged)
            onNavigateProfileScreen()
        }
    }

    when (profileUpdateUiState) {
        is UiState.Idle -> {
            ProfileUpdateContent(
                onProfileUpdate,
                newName,
                newEmail,
                newPassword,
                currentPassword
            )
        }

        is UiState.Error -> {
            ErrorScreen(
                errorMessage = profileUpdateUiState.message,
                onButtonClick = onBackToEmptyState,
                buttonMessage = stringResource(Res.string.back),
                modifier = Modifier.fillMaxSize()
            )
        }

        is UiState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }

        is UiState.Success -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.data_changed_successfully), style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}

@Composable
fun ProfileUpdateContent(
    onProfileUpdate: (ProfileUpdateRequest) -> Unit,
    newName: MutableState<String>,
    newEmail: MutableState<String>,
    newPassword: MutableState<String>,
    currentPassword: MutableState<String>
) {

    val currentPasswordVisible = remember { mutableStateOf(false) }
    val newPasswordVisible = remember { mutableStateOf(false) }
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterEmail = remember { FocusRequester() }
    val focusRequesterNewPassword = remember { FocusRequester() }
    val focusRequesterCurrentPassword = remember { FocusRequester() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                stringResource(Res.string.change_of_personal_data),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            VerticalSpacer(16.dp)

            InputProfileUpdateTextField(
                labelResId = Res.string.name,
                placeholderResId = Res.string.enter_your_name,
                state = newName,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterEmail.requestFocus() }),
                focusRequester = focusRequesterName
            )
            VerticalSpacer(8.dp)

            InputProfileUpdateTextField(
                labelResId = Res.string.email,
                placeholderResId = Res.string.enter_your_email,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterNewPassword.requestFocus() }),
                state = newEmail,
                focusRequester = focusRequesterEmail,
            )

            VerticalSpacer(8.dp)

            InputProfileUpdateTextField(
                labelResId = Res.string.new_password,
                placeholderResId = Res.string.enter_your_new_password,
                visualTransformation = if (newPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusRequesterCurrentPassword.requestFocus() }),
                state = newPassword,
                focusRequester = focusRequesterNewPassword,
                trailingIcon = {
                    val image =
                        if (newPasswordVisible.value) Res.drawable.visibility else Res.drawable.visibility_off
                    IconButton(onClick = {
                        newPasswordVisible.value = !newPasswordVisible.value
                    }) {
                        Icon(painter = painterResource(image), contentDescription = null)
                    }
                }
            )

            VerticalSpacer(8.dp)

            InputProfileUpdateTextField(
                labelResId = Res.string.current_password,
                placeholderResId = Res.string.enter_current_password,
                visualTransformation = if (currentPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                state = currentPassword,
                focusRequester = focusRequesterCurrentPassword,
                trailingIcon = {
                    val image =
                        if (currentPasswordVisible.value) Res.drawable.visibility else Res.drawable.visibility_off
                    IconButton(onClick = {
                        currentPasswordVisible.value = !currentPasswordVisible.value
                    }) {
                        Icon(painter = painterResource(image), contentDescription = null)
                    }
                }
            )

            VerticalSpacer(8.dp)
            Button(
                onClick = {
                    onProfileUpdate(
                        ProfileUpdateRequest(
                            name = newName.value,
                            email = newEmail.value,
                            avatar = null,
                            newPassword = newPassword.value,
                            oldPassword = currentPassword.value
                        )
                    )
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .defaultMinSize(
                        minHeight = OutlinedTextFieldDefaults.MinHeight,
                        minWidth = OutlinedTextFieldDefaults.MinWidth
                    )
            ) {
                Text(
                    stringResource(Res.string.change_user_data),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            VerticalSpacer(32.dp)
        }
    }
}

@Composable
fun InputProfileUpdateTextField(
    labelResId: StringResource,
    placeholderResId: StringResource,
    state: MutableState<String>,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    focusRequester: FocusRequester
) {
    Text(stringResource(labelResId), style = MaterialTheme.typography.titleMedium)
    VerticalSpacer(4.dp)
    OutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it.trim() },
        singleLine = true,
        modifier = modifier
            .focusRequester(focusRequester).width(OutlinedTextFieldDefaults.MinWidth),
        shape = RoundedCornerShape(16.dp),
        placeholder = {
            Text(stringResource(placeholderResId), color = Color.Gray)
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}