package com.example.btppilot.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.remember


import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.btppilot.ui.screens.auth.login.LoginScreen
import com.example.btppilot.ui.screens.auth.login.LoginViewModel
import com.example.btppilot.ui.screens.auth.register.RegisterSharedViewModel
import com.example.btppilot.ui.screens.auth.register.company.RegisterCompanyScreen
import com.example.btppilot.ui.screens.auth.register.company.RegisterCompanyViewModel
import com.example.btppilot.ui.screens.auth.register.inviteUserToCompany.RegisterLinkUserToCompanyScreen
import com.example.btppilot.ui.screens.auth.register.inviteUserToCompany.RegisterLinkUserToCompanyViewModel
import com.example.btppilot.ui.screens.auth.register.userInfo.RegisterUserInfoScreen
import com.example.btppilot.ui.screens.auth.register.userRoleInCompany.RegisterStepOneScreen
import com.example.btppilot.ui.screens.auth.splash.SplashScreen
import com.example.btppilot.ui.screens.auth.splash.SplashViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {

    navigation(
        route = NavGraph.AuthGraph.route,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route,

            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                )
            }

        ) {

            val splashViewModel: SplashViewModel = hiltViewModel()

            SplashScreen(
                navController = navController,
                splashViewModel = splashViewModel
            )
        }

        composable(
            Screen.Login.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(500)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(500)
                )
            }
        ) {

            val loginViewModel: LoginViewModel = hiltViewModel()

            LoginScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )

        }

        navigation(
            route = NavGraph.RegisterGraph.route,
            startDestination = Screen.RegisterUserInfo.route
        ) {

            composable(
                Screen.RegisterUserInfo.route,

                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(500)
                    )
                }
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.RegisterGraph.route)
                }

                val registerSharedViewModel: RegisterSharedViewModel = hiltViewModel(parentEntry)

                RegisterUserInfoScreen(
                    navController = navController,
                    registerViewModel = registerSharedViewModel
                )
            }
            composable(
                Screen.RegisterRole.route,

                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(500)
                    )
                }
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.RegisterGraph.route)
                }

                val registerSharedViewModel: RegisterSharedViewModel = hiltViewModel(parentEntry)

                RegisterStepOneScreen(
                    navController = navController,
                    registerViewModel = registerSharedViewModel
                )
            }

            composable(
                Screen.RegisterInviteCompany.route + "/{roleLinkCompany}",
                arguments = listOf(
                    navArgument("roleLinkCompany") {
                        type = NavType.StringType
                    }
                ),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(500)
                    )
                }
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.RegisterGraph.route)
                }
                val roleEnum = backStackEntry.arguments?.getString("roleLinkCompany") ?: ""

                val registerLinkUserToCompanyViewModel: RegisterLinkUserToCompanyViewModel =
                    hiltViewModel()

                RegisterLinkUserToCompanyScreen(
                    navController = navController,
                    registerLinkUserToCompany = registerLinkUserToCompanyViewModel,
                    roleEnum=roleEnum
                )
            }

            composable(
                Screen.RegisterOwnerCompany.route,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(500)
                    )
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(500)
                    )
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(500)
                    )
                }
            ) {

                val registerCompanyViewModel: RegisterCompanyViewModel = hiltViewModel()

                RegisterCompanyScreen(
                    navController = navController,
                    registerViewModel = registerCompanyViewModel
                )
            }
        }
    }
}