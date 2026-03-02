package com.example.btppilot.presentation.screens.register.stepThree.linkUserToCompany

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.btppilot.presentation.component.AppButtonRegisterScaffold
import com.example.btppilot.presentation.component.AppPrimaryTitleBlue
import com.example.btppilot.presentation.component.AppSecondaryTitle
import com.example.btppilot.presentation.component.AppTextField
import com.example.btppilot.presentation.screens.navigation.Screen
import com.example.btppilot.presentation.screens.register.component.HeaderRegister
import com.example.btppilot.presentation.screens.register.component.RegisterLinkUserToCompanyContent
import com.example.btppilot.presentation.screens.register.component.RegisterSecondStepContent
import com.example.btppilot.presentation.screens.register.stepOneAndTwo.RegisterViewModel
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.UserRole


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
    userRole: String
) {

    val companyInfo by registerLinkUserToCompany.companyInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    registerLinkUserToCompany.setUserRoleCompanyLink(userRole)

    LaunchedEffect(Unit) {
        registerLinkUserToCompany.inviteEvent.collect { event ->
            when (event) {
                is  RegisterLinkUserToCompanyViewModel.InviteEvent.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                RegisterLinkUserToCompanyViewModel.InviteEvent.NavigateToHome -> {
                    navController.navigate(Screen.Home.route)
                }

                else -> {}
            }
        }
    }


    Box(Modifier.fillMaxSize()) {

        RegisterLinkUserToCompanyContent(
            companyInfo = companyInfo,
            onEmailChange = registerLinkUserToCompany::onEmailChange,
            onSubmit = registerLinkUserToCompany::inviteUserToCompany,
            snackBarHostState = snackBarHostState
        )

        if (companyInfo.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

