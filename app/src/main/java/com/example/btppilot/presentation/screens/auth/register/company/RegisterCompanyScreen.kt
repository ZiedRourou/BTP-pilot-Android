package com.example.btppilot.presentation.screens.auth.register.company

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.presentation.screens.auth.register.component.HeaderRegister
import com.example.btppilot.presentation.screens.component.AppTextField
import com.example.btppilot.presentation.screens.component.AppTextFieldMultiline
import com.example.btppilot.presentation.screens.component.LoadingOverlay
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun RegisterPreview() {


    val fakeState = RegisterCompanyViewModel.NewCompanyInfo(
        siret = "John",
        name = "Doe",
        activity = "john.doe@email.com",
    )
//
//    BtpPilotTheme {
//        RegisterCompanyContent(
//            companyInfo = fakeState,
//            onSiretChange = {},
//            onNameChange = {},
//            onActivityChange = {},
//        )
//    }
}

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

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route){
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
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


@Composable
fun RegisterCompanyContent(
    paddingValues: PaddingValues,
    companyInfo: RegisterCompanyViewModel.NewCompanyInfo,
    onSiretChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onActivityChange: (String) -> Unit,
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
                Text(
                    text = "Les Informations de votre entreprise",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            AppTextField(
                value = companyInfo.name,
                onValueChange = onNameChange,
                label = "Nom de votre entreprise",
                isError = companyInfo.nameError != null,
                supportingText = companyInfo.nameError,
                leadingIcon = Icons.Filled.BusinessCenter,
            )
            AppTextField(
                value = companyInfo.siret,
                onValueChange = onSiretChange,
                label = "Siret",
                isError = companyInfo.siretError != null,
                supportingText = companyInfo.siretError,
                leadingIcon = Icons.Filled.Pin
            )

            AppTextFieldMultiline(
                value = companyInfo.activity,
                onValueChange = onActivityChange,
                label = "Votre secteur d'activité",
                isError = companyInfo.activityError != null,
                supportingText = companyInfo.activityError
                    ?: "Indiquez le domaine principal de votre entreprise (ex : électricité générale, plomberie-chauffage, climatisation …).",
                minLines = 3,
                maxLines = 3,
                leadingIcon = Icons.Filled.Construction
            )

        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, Color.Gray),
            color = MaterialTheme.colorScheme.surface,
        ) {

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.AdminPanelSettings,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(30.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Column() {
                    Text(
                        text = "Vous allez devenir administrateur de votre entreprise ",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}