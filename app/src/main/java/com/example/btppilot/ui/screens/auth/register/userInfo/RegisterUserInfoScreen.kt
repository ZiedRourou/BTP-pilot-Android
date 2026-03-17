package com.example.btppilot.ui.screens.auth.register.userInfo


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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.ui.navigation.Screen
import com.example.btppilot.ui.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.ui.screens.auth.register.component.HeaderRegister
import com.example.btppilot.ui.screens.auth.register.RegisterSharedViewModel
import com.example.btppilot.ui.screens.shared.eventState.EventState


@Composable
fun RegisterUserInfoScreen(
    navController: NavController,
    registerViewModel: RegisterSharedViewModel
) {

    val userInfo by registerViewModel.registerUserInfoStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        registerViewModel.registerUserEventSharedFlow.collect { event ->
            when (event) {

                is EventState.ShowMessageSnackBar ->
                    snackBarHostState.showSnackbar(context.getString(event.message))

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                        if (event.screen is Screen.RegisterRole)
                            popUpTo(Screen.Login.route) { inclusive = true }
                    }

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
    ) { padding ->

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

