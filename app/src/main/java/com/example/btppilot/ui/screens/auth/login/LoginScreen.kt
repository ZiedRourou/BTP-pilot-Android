package com.example.btppilot.ui.screens.auth.login


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.btppilot.ui.navigation.NavGraph
import com.example.btppilot.ui.screens.shared.uiState.EventState


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val userLoginInfoState by loginViewModel.loginUserInfoStateFlo.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.loginUserEventStateFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)

                is EventState.RedirectGraph ->
                    navController.navigate(event.graph.route) {
                        if (event.graph is NavGraph.MainGraph)
                            popUpTo(NavGraph.AuthGraph.route){inclusive= true}
                    }

                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LoginContent(
            modifier = Modifier.padding(padding),
            loginInfoState = userLoginInfoState,
            onEmailChange = loginViewModel::onEmailChange,
            onPasswordChange = loginViewModel::onPasswordChange,
            onLoginClick = loginViewModel::login,
            onRegisterClick = loginViewModel::goToRegister
        )
    }
}
