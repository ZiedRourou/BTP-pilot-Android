package com.example.btppilot.presentation.screens.register.stepThree.company

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import com.example.btppilot.presentation.component.AppButtonRegisterScaffold
import com.example.btppilot.presentation.screens.navigation.Screen
import com.example.btppilot.presentation.screens.register.component.HeaderRegister
import com.example.btppilot.presentation.screens.register.component.RegisterLinkUserToCompanyContent
import com.example.btppilot.presentation.screens.register.stepThree.linkUserToCompany.RegisterLinkUserToCompanyViewModel
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.UserRole



@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun RegisterPreview() {


    val fakeState = RegisterCompanyViewModel.NewCompanyInfo(
        siret = "John",
        name = "Doe",
        activity = "john.doe@email.com",
    )

    BtpPilotTheme {
        RegisterCompanyContent(
            companyInfo = fakeState,
            onSiretChange ={} ,
            onNameChange = {},
            onActivityChange = {},
            onSubmit = { /*TODO*/ },
        )
    }
}

@Composable
fun RegisterCompanyScreen(
    navController: NavController,
    registerViewModel: RegisterCompanyViewModel,
    userRole: String
) {


    val companyInfo by registerViewModel.companyInfo.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        registerViewModel.inviteEvent.collect { event ->
            when (event) {
                is RegisterCompanyViewModel.RegisterCompanyEvent.ShowError ->
                    snackBarHostState.showSnackbar(event.message)

                RegisterCompanyViewModel.RegisterCompanyEvent.NavigateToHome -> {
                    navController.navigate(Screen.Home.route)
                }

                else -> {}
            }
        }
    }


    Box(Modifier.fillMaxSize()) {

        RegisterCompanyContent(
            companyInfo = companyInfo,
            onSiretChange = registerViewModel::onSiretChange,
            onActivityChange = registerViewModel::onActivityChange,
            onNameChange = registerViewModel::onNameChange,
            onSubmit = registerViewModel::createCompany,
//            snackBarHostState = snackBarHostState
        )

        if (companyInfo.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
fun RegisterCompanyContent(
    companyInfo: RegisterCompanyViewModel.NewCompanyInfo,
    onSiretChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onActivityChange: (String) -> Unit,
    onSubmit: () -> Unit,
//    snackBarHostState: SnackbarHostState
) {

    Scaffold(
        topBar = { HeaderRegister(3) },
        bottomBar = {
            AppButtonRegisterScaffold(
                onClick = onSubmit
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
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

                    OutlinedTextField(
                        value = companyInfo.name,
                        onValueChange = onNameChange,
                        label = {
                            Text(
                                "Nom de votre entreprise",
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },

                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )


                    OutlinedTextField(
                        value = companyInfo.siret,
                        onValueChange = onSiretChange,
                        label = {
                            Text(
                                "Siret",
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },

                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = companyInfo.activity,
                        onValueChange = onActivityChange,
                        label = {
                            Text(
                                "Votre secteur d'activité",
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        },

                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
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
        },
    )
}