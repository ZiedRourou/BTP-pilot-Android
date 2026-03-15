package com.example.btppilot.presentation.screens.auth.register.company

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.presentation.screens.auth.register.sharedComponent.BottomBarRegister
import com.example.btppilot.presentation.screens.auth.register.sharedComponent.HeaderRegister
import com.example.btppilot.presentation.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.presentation.screens.shared.component.AppTextField
import com.example.btppilot.presentation.screens.shared.component.AppTextFieldMultiline
import com.example.btppilot.presentation.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun RegisterPreview() {

    BtpPilotTheme {
        Scaffold(
            topBar = { HeaderRegister(3) },
            bottomBar = {
                BottomBarRegister(
                    text = "Valider",
                    onClick = {}
                )
            },
        ) { padding ->

            RegisterCompanyContent(
                paddingValues = padding,
                companyInfo = RegisterCompanyViewModel.NewCompanyInfo(),
                onSiretChange = {},
                onActivityChange = {},
                onNameChange = {},
            )
        }
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
                .padding(5.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            AppSecondaryTitle(
                text = "Les Informations de votre entreprise")

            Spacer(modifier = Modifier.height(20.dp))

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
    }
}