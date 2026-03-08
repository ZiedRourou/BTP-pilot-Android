package com.example.btppilot.presentation.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.R
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.component.AppLabelTitle
import com.example.btppilot.presentation.screens.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.component.AppPrimaryTitleYellow
import com.example.btppilot.presentation.screens.component.AppSecondaryTitle
import com.example.btppilot.presentation.screens.component.AppTextField
import com.example.btppilot.presentation.screens.component.LoadingOverlay
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme


@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val userLoginInfo by loginViewModel.loginUserInfoStateFlo.collectAsState()

    LaunchedEffect(Unit) {
        loginViewModel.loginUserEventStateFlo.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LoginContent(
            modifier = Modifier.padding(padding),
            loginInfo = userLoginInfo,
            onEmailChange = loginViewModel::onEmailChange,
            onPasswordChange = loginViewModel::onPasswordChange,
            onLoginClick = loginViewModel::login,
            onRegisterClick = {
                navController.navigate(Screen.RegisterGraph.route)
            }
        )
    }
}



@Preview(showBackground = true, apiLevel = 33)
@Composable
fun LoginPreview() {
    BtpPilotTheme {
        LoginContent(
            modifier = Modifier,
            loginInfo = LoginViewModel.LoginInfo(
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
    loginInfo: LoginViewModel.LoginInfo,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {

    LoadingOverlay(isVisible = loginInfo.isLoading)
    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(R.drawable.logo_pilot_btp),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppPrimaryTitleYellow(text = "PilotBtp")
        AppSecondaryTitle(text = "Votre chantier entre de bonnes mains")

        Spacer(modifier = Modifier.height(40.dp))

        AppLabelTitle(text = "Connexion")

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = loginInfo.email,
            onValueChange = onEmailChange,
            label = "Email",
            isError = loginInfo.emailError != null,
            supportingText = loginInfo.emailError,
            leadingIcon = Icons.Filled.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = loginInfo.password,
            onValueChange = onPasswordChange,
            label = "Mot de passe",
            isError = loginInfo.passwordError != null,
            supportingText = loginInfo.passwordError,
            isPassword = true,
            leadingIcon = Icons.Filled.Lock
        )

        Spacer(modifier = Modifier.height(8.dp))

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

        Spacer(modifier = Modifier.height(32.dp))

        AppPrimaryButton(
            text = "Connexion",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(40.dp))

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                "Nouveau ici ? Je m'inscris",
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
                    .clickable { onRegisterClick() },
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}