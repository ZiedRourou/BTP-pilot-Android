package com.example.btppilot.presentation.screens.register.stepThree.linkUserToCompany

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import com.example.btppilot.presentation.screens.register.component.HeaderRegister
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun RegisterLinkUserToCompanyPreview() {
    BtpPilotTheme {
        RegisterLinkUserToCompanyContent(
            companyEmail = "state.companyEmail",
            onEmailChange = {},
            onSubmit = {}
        )
    }
}

@Composable
fun RegisterLinkUserToCompanyScreen(
    navController: NavController,
    registerLinkUserToCompany: RegisterLinkUserToCompany
) {

    RegisterLinkUserToCompanyContent(
        companyEmail = "state.companyEmail",
        onEmailChange = {},
        onSubmit = {}
    )
}

@Composable
fun RegisterLinkUserToCompanyContent(
    companyEmail: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {

    Scaffold(
        topBar = { HeaderRegister(3) },

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
                            value = companyEmail,
                            onValueChange = onEmailChange,
                            label = "Email de votre entreprise",
                            leadingIcon = Icons.Filled.Email
                        )
                    }
                }
            }
        },
    )
}