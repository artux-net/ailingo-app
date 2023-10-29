package org.ailingo.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.compose.stringResource


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegistrationScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        var email by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        Text(
            stringResource(SharedRes.strings.create_your_account),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = stringResource(SharedRes.strings.email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Focus on password field when 'Next' is clicked
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text(text = stringResource(SharedRes.strings.password)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Perform login when 'Done' is clicked
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .width(OutlinedTextFieldDefaults.MinWidth)
                .height(OutlinedTextFieldDefaults.MinHeight),
            shape = MaterialTheme.shapes.small,
            onClick = {

            }
        ) {
            Text(stringResource(SharedRes.strings.continue_app))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                stringResource(SharedRes.strings.already_have_an_account)
            )
            Text(" ")
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = stringResource(SharedRes.strings.log_in),
                modifier = Modifier.clickable {
                    //log in
                })
        }
    }
}