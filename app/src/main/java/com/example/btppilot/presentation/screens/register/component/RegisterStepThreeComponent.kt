package com.example.btppilot.presentation.screens.register.component

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.presentation.component.AppButtonRegisterScaffold
import com.example.btppilot.presentation.component.AppPrimaryTitleBlue
import com.example.btppilot.presentation.component.AppSecondaryTitle
import com.example.btppilot.presentation.component.AppTextField
import com.example.btppilot.presentation.screens.register.stepThree.linkUserToCompany.RegisterLinkUserToCompanyViewModel
import com.example.btppilot.ui.theme.BtpPilotTheme

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun Register3Preview() {
    BtpPilotTheme {
//        RegisterThirstStep()
    }
}


@Composable
fun RegisterThirstStep() {

    var companyName by remember { mutableStateOf("") }
    var siret by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                HeaderRegister(3)
            }
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

//                Surface(
//                    shape = RoundedCornerShape(12.dp),
//                    border = BorderStroke(2.dp, Color.Gray),
//                    color = Color.White,
//                ) {
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
                        value = companyName,
                        onValueChange = { companyName = it },
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
                        value = siret,
                        onValueChange = { siret = it },
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
                        value = activity,
                        onValueChange = { activity = it },
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
                            modifier = Modifier.padding(5.dp).size(30.dp),
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
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {


                Button(
                    onClick = {},
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(10.dp)
                ) {
                    Text(
                        "Valider",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        },
    )
}

@Composable
fun RegisterLinkUserToCompanyContent(
    companyInfo: RegisterLinkUserToCompanyViewModel.CompanyInfo,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    snackBarHostState: SnackbarHostState
) {

    Scaffold(
        topBar = { HeaderRegister(3) },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            AppButtonRegisterScaffold(
                text = "Valider",
                onClick = {onSubmit()}
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

                        AppSecondaryTitle(
                            text = "Entrez l'email de votre entreprise afin de faire une demande d'invitation")

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
        },
    )
}
