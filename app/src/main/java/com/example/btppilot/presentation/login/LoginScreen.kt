package com.example.btppilot.presentation.login

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
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController, loginViewModel: LoginViewModel
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by loginViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        loginViewModel.event.collect { event ->
            when(event){
                is LoginViewModel.LoginEvent.ShowError ->
                    snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            LoginContent(
                modifier = Modifier.padding(padding),
                onLoginClick = { email, password ->
                    loginViewModel.login(email, password)
                }            )

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
