package com.example.btppilot.presentation.screens.register.stepOneAndTwo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.navigation.Screen
import com.example.btppilot.presentation.screens.register.component.RegisterContent
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun RegisterPreview() {
    BtpPilotTheme {
    }
}


@Composable
fun RegisterStepOneScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel
) {

    val userInfo by registerViewModel.registerInfo.collectAsState()

    LaunchedEffect(Unit) {
        registerViewModel.registerEvent.collect { event ->
            when (event) {
                RegisterViewModel.RegisterEvent.NavigateToUserInfo -> {
                    navController.navigate(Screen.RegisterUserInfo.route)
                }

                else -> {}
            }
        }
    }


    RegisterContent(
        selectedRole = userInfo.selectedRole,
        onRoleSelected = registerViewModel::selectRole,
        onClickNextStep = { registerViewModel.goToNextStep() }
    )


}
