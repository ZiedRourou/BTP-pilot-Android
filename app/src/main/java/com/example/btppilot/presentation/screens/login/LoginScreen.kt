package com.example.btppilot.presentation.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.navigation.Screen


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val userLoginInfo by loginViewModel.loginUserInfo.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.event.collect { event ->
            when (event) {
                is LoginViewModel.LoginEvent.ShowError ->
                    snackbarHostState.showSnackbar(event.message)

                is LoginViewModel.LoginEvent.LoginSuccess ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(Modifier.fillMaxSize()) {

            LoginContent(
                modifier = Modifier.padding(20.dp),

                email = userLoginInfo.email,
                password = userLoginInfo.password,

                emailError = userLoginInfo.emailError,
                passwordError = userLoginInfo.passwordError,

                onEmailChange = loginViewModel::onEmailChange,
                onPasswordChange = loginViewModel::onPasswordChange,
                onLoginClick = { loginViewModel.login() },
                onRegisterClick = {
                    navController.navigate(Screen.RegisterGraph.route)
                }
            )

            if (userLoginInfo.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}