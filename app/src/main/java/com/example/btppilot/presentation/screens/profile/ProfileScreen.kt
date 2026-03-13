package com.example.btppilot.presentation.screens.profile

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.uiState.EventState

@Composable
fun ProfileScreen(
    rootNavController: NavController,
    profileViewModel: ProfileViewModel,
    sharedViewModel: SharedViewModel,
    snackbarHostState: SnackbarHostState
) {

    LaunchedEffect(Unit) {
        profileViewModel.profileEventSharedFlow.collect() { event ->
            if (event is EventState.RedirectScreen)
                rootNavController.navigate(Screen.Login.route){
                    popUpTo(0)
                }

        }
    }

    ProfileContent(
        logout = profileViewModel::logout
    )
}

@Composable
fun ProfileContent(
    logout: () -> Unit
) {

    AppPrimaryButton(text = "Se Déconnecter", onClick = logout)

}