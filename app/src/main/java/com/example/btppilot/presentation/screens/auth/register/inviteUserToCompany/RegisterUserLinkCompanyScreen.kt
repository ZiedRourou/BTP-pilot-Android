package com.example.btppilot.presentation.screens.auth.register.inviteUserToCompany

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.presentation.navigation.NavGraph
import com.example.btppilot.presentation.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.presentation.screens.auth.register.component.HeaderRegister
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryTitleBlue
import com.example.btppilot.presentation.screens.shared.component.AppTitleDescription
import com.example.btppilot.presentation.screens.shared.component.AppTextField
import com.example.btppilot.presentation.screens.component.LoadingOverlay
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun RegisterLinkUserToCompanyPreview() {
    BtpPilotTheme {

    }
}

@Composable
fun RegisterLinkUserToCompanyScreen(
    navController: NavController,
    registerLinkUserToCompany: RegisterLinkUserToCompanyViewModel,
//    userRole: String
) {

    val companyInfo by registerLinkUserToCompany.companyInfoInviteStateFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

//    registerLinkUserToCompany.setUserRoleCompanyLink(userRole)

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


@Composable
fun RegisterLinkUserToCompanyContent(
    paddingValues: PaddingValues,
    companyInfo: RegisterLinkUserToCompanyViewModel.CompanyInfo,
    onEmailChange: (String) -> Unit,
) {

    LoadingOverlay(isVisible = companyInfo.isLoading)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Surface(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Color.Gray),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Icon(
                        imageVector = Icons.Filled.BusinessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    AppPrimaryTitleBlue(text = "Rejoignner votre entreprise")

                }

                Spacer(modifier = Modifier.height(10.dp))

                AppTitleDescription(
                    text = "Entrez l'email de votre entreprise afin de faire une demande d'invitation"
                )

                Spacer(modifier = Modifier.height(10.dp))

                AppTextField(
                    value = companyInfo.email,
                    onValueChange = onEmailChange,
                    isError = companyInfo.emailError != null,
                    supportingText = companyInfo.emailError,
                    label = "Email de votre entreprise",
                    leadingIcon = Icons.Filled.Email
                )
            }
        }
    }
}
