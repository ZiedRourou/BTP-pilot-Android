package com.example.btppilot.ui.screens.auth.register.userRoleInCompany


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
import androidx.navigation.NavController
import com.example.btppilot.ui.navigation.Screen
import com.example.btppilot.ui.screens.auth.register.component.HeaderRegister
import com.example.btppilot.ui.screens.auth.register.RegisterSharedViewModel
import com.example.btppilot.ui.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.ui.screens.shared.eventState.EventState


@Composable
fun RegisterStepOneScreen(
    navController: NavController,
    registerViewModel: RegisterSharedViewModel
) {

    val userInfo by registerViewModel.registerUserInfoStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val userRole = userInfo.selectedRole.toString()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        registerViewModel.registerUserEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(context.getString(event.message))

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route)

                is EventState.RedirectScreenWithId ->
                    navController.navigate(event.route)

                else -> {}

            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HeaderRegister(step = 2) },
        bottomBar = {
            BottomBarRegister(onClick = registerViewModel::goToCompanyInfo)
        },
    ) { padding ->

        RegisterContent(
            modifier = Modifier.padding(padding),
            selectedRole = userInfo.selectedRole,
            onRoleSelected = registerViewModel::selectRole,
        )
    }
}

