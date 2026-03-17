package com.example.btppilot.ui.screens.auth.register.inviteUserToCompany

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.R
import com.example.btppilot.ui.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.ui.screens.auth.register.component.HeaderRegister
import com.example.btppilot.ui.screens.shared.component.AppPrimaryTitleBlue
import com.example.btppilot.ui.screens.shared.component.AppTextField
import com.example.btppilot.ui.screens.shared.component.AppTitleDescription
import com.example.btppilot.ui.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme

@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun RegisterLinkUserToCompanyPreview() {
    BtpPilotTheme {
        Scaffold(
            topBar = { HeaderRegister(3) },
            bottomBar = {
                BottomBarRegister(
                    text = stringResource(id = R.string.btn_txt_validate),
                    onClick = {}
                )
            },
        ) { padding ->

            RegisterLinkUserToCompanyContent(
                paddingValues = padding,
                companyInfo = RegisterLinkUserToCompanyViewModel.CompanyInfo(),
                onEmailChange = {}
            )
        }
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
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Filled.BusinessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    AppPrimaryTitleBlue(text = stringResource(R.string.txt_join_company))

                }

                Spacer(modifier = Modifier.height(20.dp))

                AppTitleDescription(
                    text = stringResource(R.string.txt_enter_email_company)
                )

                Spacer(modifier = Modifier.height(20.dp))

                AppTextField(
                    value = companyInfo.email,
                    onValueChange = onEmailChange,
                    isError = companyInfo.emailError != null,
                    supportingText = companyInfo.emailError,
                    label = stringResource(R.string.txt_email_of_your_company),
                    leadingIcon = Icons.Filled.Email
                )
            }
        }
    }
}
