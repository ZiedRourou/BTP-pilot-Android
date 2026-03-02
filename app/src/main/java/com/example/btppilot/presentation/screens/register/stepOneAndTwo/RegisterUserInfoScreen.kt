package com.example.btppilot.presentation.screens.register.stepOneAndTwo


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.navigation.Screen
import com.example.btppilot.presentation.screens.register.stepOneAndTwo.RegisterViewModel
import com.example.btppilot.presentation.screens.register.component.RegisterSecondStepContent


@Composable
fun RegisterSecondStepScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel
) {

    val userInfo by registerViewModel.registerInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    val userRole = userInfo.selectedRole.toString()

    LaunchedEffect(Unit) {
        registerViewModel.registerEvent.collect { event ->
            when (event) {

                is RegisterViewModel.RegisterEvent.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                RegisterViewModel.RegisterEvent.NavigateToOwnerCompany ->
                    navController.navigate("${Screen.RegisterOwnerCompany.route}/$userRole")

                RegisterViewModel.RegisterEvent.NavigateToInviteCompany ->
                    navController.navigate("${Screen.RegisterInviteCompany.route}/$userRole")

                else -> {}
            }
        }
    }

    Box(Modifier.fillMaxSize()) {

        RegisterSecondStepContent(
            userInfo = userInfo,
            onFirstNameChange = registerViewModel::onFirstNameChange,
            onEmailChange = registerViewModel::onEmailChange,
            onPasswordChange = registerViewModel::onPasswordChange,
            onConfirmPasswordChange = registerViewModel::onConfirmPasswordChange,
            onNextClick = registerViewModel::registerUser,
            snackBarHostState = snackBarHostState
        )


        if (userInfo.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

