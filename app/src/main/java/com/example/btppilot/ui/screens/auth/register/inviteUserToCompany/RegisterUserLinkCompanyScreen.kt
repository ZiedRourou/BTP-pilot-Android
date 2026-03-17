package com.example.btppilot.ui.screens.auth.register.inviteUserToCompany


import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.btppilot.R
import com.example.btppilot.ui.navigation.NavGraph
import com.example.btppilot.ui.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.ui.screens.auth.register.component.HeaderRegister
import com.example.btppilot.ui.screens.shared.eventState.EventState

@Composable
fun RegisterLinkUserToCompanyScreen(
    navController: NavController,
    registerLinkUserToCompany: RegisterLinkUserToCompanyViewModel,
    roleEnum : String,
) {

    registerLinkUserToCompany.setRole(roleEnum)
    val context = LocalContext.current
    val companyInfo by registerLinkUserToCompany.companyInfoInviteStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        registerLinkUserToCompany.companyInfoInviteEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackBarHostState.showSnackbar(context.getString(event.message))

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
                text = stringResource(id = R.string.btn_txt_validate),
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

