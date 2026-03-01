package com.example.btppilot.presentation.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.navigation.Screen


@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel
) {

    LaunchedEffect(Unit) {
        splashViewModel.mainScreenRouteSF.collect { route ->
            navController.navigate(route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }
    SplashContent()

}