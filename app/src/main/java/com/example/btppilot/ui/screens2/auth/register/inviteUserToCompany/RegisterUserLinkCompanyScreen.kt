package com.example.btppilot.ui.screens2.auth.register.inviteUserToCompany


import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.btppilot.presentation.navigation.NavGraph
import com.example.btppilot.ui.screens2.auth.register.sharedComponent.BottomBarRegister
import com.example.btppilot.ui.screens2.auth.register.sharedComponent.HeaderRegister
import com.example.btppilot.presentation.screens.shared.uiState.EventState

@Composable
fun RegisterLinkUserToCompanyScreen(
    navController: NavController,
    registerLinkUserToCompany: RegisterLinkUserToCompanyViewModel,
) {

    val companyInfo by registerLinkUserToCompany.companyInfoInviteStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        registerLinkUserToCompany.companyInfoInviteEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackBarHostState.showSnackbar(event.message)

                is EventState.RedirectGraph ->
                    navController.navigate(event.graph.route) {
                        popUpTo(NavGraph.AuthGraph.route){inclusive= true}
                    }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = { HeaderRegister(3) },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            BottomBarRegister(
                text = "Valider",
                onClick = registerLinkUserToCompany::inviteUserToCompany
            )
        },
    ) { padding ->

        RegisterLinkUserToCompanyContent(
            paddingValues = padding,
            companyInfo = companyInfo,
            onEmailChange = registerLinkUserToCompany::onEmailChange
        )
    }
}

