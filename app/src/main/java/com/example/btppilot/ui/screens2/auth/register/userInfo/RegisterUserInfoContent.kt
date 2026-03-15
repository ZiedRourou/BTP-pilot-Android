package com.example.btppilot.ui.screens2.auth.register.userInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.ui.screens2.auth.register.RegisterSharedViewModel
import com.example.btppilot.ui.screens2.auth.register.sharedComponent.BottomBarRegister
import com.example.btppilot.ui.screens2.auth.register.sharedComponent.HeaderRegister
import com.example.btppilot.presentation.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.presentation.screens.shared.component.AppTextField
import com.example.btppilot.presentation.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun RegisterUserInfoPreview() {
    BtpPilotTheme {
        Scaffold(

            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    HeaderRegister(1)
                }
            },
            bottomBar = {
                BottomBarRegister(
                    onClick = {},
                )
            }) { padding ->

            RegisterUserInfoContent(
                paddingValues = padding,
                userInfo = RegisterSharedViewModel.RegisterState(),
                onFirstNameChange = {},
                onEmailChange = {},
                onPasswordChange = {},
                onConfirmPasswordChange = {},
            )
        }
    }
}

@Composable
fun RegisterUserInfoContent(
    paddingValues: PaddingValues,
    userInfo: RegisterSharedViewModel.RegisterState,
    onFirstNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
) {
    LoadingOverlay(isVisible = userInfo.isLoading)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        AppSecondaryTitle(text = "Vos informations")

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