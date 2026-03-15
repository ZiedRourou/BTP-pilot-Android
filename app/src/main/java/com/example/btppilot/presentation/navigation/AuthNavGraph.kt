package com.example.btppilot.presentation.navigation

import androidx.compose.runtime.remember


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.btppilot.presentation.screens.auth.login.LoginScreen
import com.example.btppilot.presentation.screens.auth.login.LoginViewModel
import com.example.btppilot.presentation.screens.auth.register.RegisterSharedViewModel
import com.example.btppilot.presentation.screens.auth.register.company.RegisterCompanyScreen
import com.example.btppilot.presentation.screens.auth.register.company.RegisterCompanyViewModel
import com.example.btppilot.presentation.screens.auth.register.inviteUserToCompany.RegisterLinkUserToCompanyScreen
import com.example.btppilot.presentation.screens.auth.register.inviteUserToCompany.RegisterLinkUserToCompanyViewModel
import com.example.btppilot.presentation.screens.auth.register.userInfo.RegisterUserInfoScreen
import com.example.btppilot.presentation.screens.auth.register.userRoleInCompany.RegisterStepOneScreen
import com.example.btppilot.presentation.screens.auth.splash.SplashScreen
import com.example.btppilot.presentation.screens.auth.splash.SplashViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {

    navigation(
        route = NavGraph.AuthGraph.route,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {

            val splashViewModel: SplashViewModel = hiltViewModel()

            SplashScreen(
                navController = navController,
                splashViewModel = splashViewModel
            )
        }

        composable(Screen.Login.route) {

            val vm: LoginViewModel = hiltViewModel()

            LoginScreen(
                navController = navController,
                loginViewModel = vm
            )
        }

        navigation(
            route = NavGraph.RegisterGraph.route,
            startDestination = Screen.RegisterUserInfo.route
        ) {

            composable(Screen.RegisterUserInfo.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.RegisterGraph.route)
                }

                val vm: RegisterSharedViewModel = hiltViewModel(parentEntry)

                RegisterUserInfoScreen(
                    navController = navController,
                    registerViewModel = vm
                )
            }
            composable(Screen.RegisterRole.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.RegisterGraph.route)
                }

                val vm: RegisterSharedViewModel = hiltViewModel(parentEntry)

                RegisterStepOneScreen(
                    navController = navController,
                    registerViewModel = vm
                )
            }

            composable(
                Screen.RegisterInviteCompany.route + "/{roleLinkCompany}",
                arguments = listOf(
                    navArgument("roleLinkCompany") {
                        type = NavType.StringType
                    }
                )
            ) {

                val vm: RegisterLinkUserToCompanyViewModel = hiltViewModel()

                RegisterLinkUserToCompanyScreen(
                    navController = navController,
                    registerLinkUserToCompany = vm
                )
            }

            composable(Screen.RegisterOwnerCompany.route) {

                val vm: RegisterCompanyViewModel = hiltViewModel()

                RegisterCompanyScreen(
                    navController = navController,
                    registerViewModel = vm
                )
            }
        }
    }
}