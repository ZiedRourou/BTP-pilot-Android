package com.example.btppilot.presentation.screens.register.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.presentation.component.AppButtonRegisterScaffold
import com.example.btppilot.presentation.component.AppPrimaryTitleBlue
import com.example.btppilot.presentation.component.AppTextField
import com.example.btppilot.presentation.screens.register.RegisterViewModel


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun RegisterSecondStepPreview() {


}


@Composable
fun RegisterSecondStepContent(
    userInfo: RegisterViewModel.RegisterState,
    onFirstNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onNextClick: () -> Unit,
    snackBarHostState: SnackbarHostState
) {

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },

        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                HeaderRegister(2)
            }
        },

        bottomBar = {
            AppButtonRegisterScaffold(
                onClick = onNextClick,
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){

            AppPrimaryTitleBlue(text = "Vos informations")

            AppTextField(
                value = userInfo.firstName,
                onValueChange = onFirstNameChange,
                label = "Prénom",
                isError = userInfo.firstNameError != null,
                supportingText = userInfo.firstNameError,
                leadingIcon = Icons.Filled.Person
                )

            AppTextField(
                value = userInfo.email,
                onValueChange = onEmailChange,
                label = "Email",
                leadingIcon = Icons.Filled.Email,
                isError = userInfo.emailError != null,
                supportingText = userInfo.emailError,
            )

            AppTextField(
                value = userInfo.password,
                onValueChange = onPasswordChange,
                isPassword = true,
                label = "Mot de passe",
                leadingIcon = Icons.Filled.Lock,
                isError = userInfo.passwordError != null,
                supportingText = userInfo.passwordError,
                )

            AppTextField(
                value = userInfo.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                isPassword = true,
                label = "Confirmation",
                leadingIcon = Icons.Filled.Lock,
                isError = userInfo.confirmPasswordError != null,
                supportingText = userInfo.confirmPasswordError,
            )
        }
    }
}



