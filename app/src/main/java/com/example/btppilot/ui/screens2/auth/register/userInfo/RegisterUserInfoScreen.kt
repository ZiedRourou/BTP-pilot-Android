package com.example.btppilot.ui.screens2.auth.register.userInfo


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.ui.screens2.auth.register.sharedComponent.BottomBarRegister
import com.example.btppilot.ui.screens2.auth.register.sharedComponent.HeaderRegister
import com.example.btppilot.ui.screens2.auth.register.RegisterSharedViewModel
import com.example.btppilot.presentation.screens.shared.uiState.EventState


@Composable
fun RegisterUserInfoScreen(
    navController: NavController,
    registerViewModel: RegisterSharedViewModel
) {

    val userInfo by registerViewModel.registerUserInfoStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        registerViewModel.registerUserEventSharedFlow.collect { event ->
            when (event) {

                is EventState.ShowMessageSnackBar ->
                    snackBarHostState.showSnackbar(event.message)

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route)
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },

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
                onClick = registerViewModel::registerUser,
            )
        }
    ){ padding->

        RegisterUserInfoContent(
            paddingValues = padding,
            userInfo = userInfo,
            onFirstNameChange = registerViewModel::onFirstNameChange,
            onEmailChange = registerViewModel::onEmailChange,
            onPasswordChange = registerViewModel::onPasswordChange,
            onConfirmPasswordChange = registerViewModel::onConfirmPasswordChange,
        )
    }
}

