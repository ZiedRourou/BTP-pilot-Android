package com.example.btppilot.ui.screens2.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.R
import com.example.btppilot.presentation.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryTitle
import com.example.btppilot.presentation.screens.shared.component.AppTitleDescription
import com.example.btppilot.presentation.screens.shared.component.AppTextField
import com.example.btppilot.presentation.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun LoginPreview() {
    BtpPilotTheme {
        LoginContent(
            modifier = Modifier,
            loginInfoState = LoginViewModel.LoginInfo(
                email = "",
                password = "",
                isLoading = false,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    loginInfoState: LoginViewModel.LoginInfo,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {

    LoadingOverlay(isVisible = loginInfoState.isLoading)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp).padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(R.drawable.logo_pilot_btp),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        AppPrimaryTitle(text = "PilotBtp")
        AppTitleDescription(text = "Votre chantier entre de bonnes mains")

        AppSecondaryTitle(text = "Connexion")

        AppTextField(
            value = loginInfoState.email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = loginInfoState.emailError != null,
            supportingText = loginInfoState.emailError,
            leadingIcon = Icons.Filled.Email,
            modifier = Modifier.width(330.dp)
        )

        AppTextField(
            value = loginInfoState.password,
            onValueChange = onPasswordChange,
            label = "Mot de passe",
            isError = loginInfoState.passwordError != null,
            supportingText = loginInfoState.passwordError,
            isPassword = true,
            leadingIcon = Icons.Filled.Lock,
            modifier = Modifier.width(330.dp)
        )

//        Box(
//            modifier = Modifier.fillMaxWidth(),
//            contentAlignment = Alignment.CenterEnd
//        ) {
//            Text(
//                "Mot de passe oublié ?",
//                style = MaterialTheme.typography.labelMedium,
//                color = MaterialTheme.colorScheme.primary
//            )
//        }

        AppPrimaryButton(
            text = "Connexion",
            onClick = onLoginClick,
            modifier = Modifier.width(250.dp)
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = Color.LightGray,
            modifier = Modifier.clickable { onRegisterClick() },
        ) {
            Text(
                "Nouveau ici ? Je m'inscris",
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}