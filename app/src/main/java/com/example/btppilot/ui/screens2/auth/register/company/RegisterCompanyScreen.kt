package com.example.btppilot.ui.screens2.auth.register.company

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
fun RegisterCompanyScreen(
    navController: NavController,
    registerViewModel: RegisterCompanyViewModel,
) {

    val companyInfo by registerViewModel.companyRegisterStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        registerViewModel.companyRegisterEventSharedFlow.collect { event ->
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
                onClick = registerViewModel::createCompany
            )
        },
    ) { padding ->

        RegisterCompanyContent(
            paddingValues = padding,
            companyInfo = companyInfo,
            onSiretChange = registerViewModel::onSiretChange,
            onActivityChange = registerViewModel::onActivityChange,
            onNameChange = registerViewModel::onNameChange,
        )
    }
}

