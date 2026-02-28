package com.example.btppilot.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.btppilot.presentation.login.LoginScreen
import com.example.btppilot.presentation.login.LoginViewModel
import com.example.btppilot.presentation.splash.SplashScreen
import com.example.btppilot.presentation.splash.SplashViewModel

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Splash : Screen("splash")
    object Login : Screen("Login")
    object Register : Screen("Register")
    object Home : Screen("home", "Accueil", Icons.Filled.Home)
    object Task : Screen("task", "Taches", Icons.Filled.Checklist)
    object Team : Screen("team", "Team", Icons.Filled.Groups)
    object Profile : Screen("profile", "Profile", Icons.Filled.Settings)
}


@Composable
fun NavGraphApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }

//        composable(Screen.Home.route) {
//            val homeViewModel: InfosViewModel = hiltViewModel()
//            InfoScreen(navController, infoViewModel)
//        }

    }

}