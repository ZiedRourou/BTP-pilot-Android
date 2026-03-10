package com.example.btppilot.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.btppilot.presentation.screens.home.HomeScreen
import com.example.btppilot.presentation.screens.home.HomeViewModel
import com.example.btppilot.presentation.screens.auth.login.LoginScreen
import com.example.btppilot.presentation.screens.auth.login.LoginViewModel
import com.example.btppilot.presentation.screens.auth.register.userRoleInCompany.RegisterStepOneScreen
import com.example.btppilot.presentation.screens.auth.register.userInfo.RegisterSecondStepScreen
import com.example.btppilot.presentation.screens.auth.register.RegisterSharedViewModel
import com.example.btppilot.presentation.screens.auth.register.company.RegisterCompanyScreen
import com.example.btppilot.presentation.screens.auth.register.company.RegisterCompanyViewModel
import com.example.btppilot.presentation.screens.auth.register.inviteUserToCompany.RegisterLinkUserToCompanyViewModel
import com.example.btppilot.presentation.screens.auth.register.inviteUserToCompany.RegisterLinkUserToCompanyScreen
import com.example.btppilot.presentation.screens.auth.splash.SplashScreen
import com.example.btppilot.presentation.screens.auth.splash.SplashViewModel
import com.example.btppilot.presentation.screens.project.newProject.NewProjectScreen
import com.example.btppilot.presentation.screens.project.newProject.NewProjectViewModel
import com.example.btppilot.presentation.screens.project.projectDetails.ProjectDetailsScreen
import com.example.btppilot.presentation.screens.project.projectDetails.ProjectDetailsViewModel
import com.example.btppilot.presentation.screens.task.TaskListScreen
import com.example.btppilot.presentation.screens.task.TaskListViewModel

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object MainGraph : Screen("main_graph")
    object Splash : Screen("splash")
    object Login : Screen("login")

    object RegisterGraph : Screen("register_graph")
    object RegisterRole : Screen("register_role")
    object RegisterUserInfo : Screen("register_user_info")
    object RegisterOwnerCompany : Screen("register_owner_company")
    object RegisterInviteCompany : Screen("register_invite_company")

    object Home : Screen("home", "Accueil", Icons.Filled.Home)
    object NewProject : Screen(route = "new_project", icon = Icons.Filled.Home)
    object ProjectDetail : Screen(route = "project_detail")

    object TaskList : Screen("_list", "Taches", Icons.Filled.Checklist)
    object Team : Screen("team", "Team", Icons.Filled.Groups)
    object Profile : Screen("profile", "Profile", Icons.Filled.Settings)
}


@Composable
fun NavGraphApp(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(Screen.Splash.route) {
            val splashViewModel: SplashViewModel = hiltViewModel()
            SplashScreen(navController, splashViewModel)
        }

        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }

        navigation(
            route = Screen.MainGraph.route,
            startDestination = Screen.Home.route
        ) {

            composable(Screen.Home.route) {

                val mainViewModel: MainViewModel = hiltViewModel()
                val homeViewModel: HomeViewModel = hiltViewModel()

                MainScaffold(
                    navController = navController,
                    mainViewModel = mainViewModel
                ) { padding, snackbarHostState ->

                    HomeScreen(
                        navController = navController,
                        homeViewModel = homeViewModel,
                        paddingValues = padding,
                        snackbarHostState = snackbarHostState
                    )
                }
            }

            composable(Screen.TaskList.route) {
                val taskListViewModel : TaskListViewModel = hiltViewModel()

                TaskListScreen(
                    paddingValues = ,
                    navController = ,
                    taskListViewModel = ,
                    snackbarHostState =
                )
            }

            composable(Screen.NewProject.route) {

                val vm: NewProjectViewModel = hiltViewModel()

                NewProjectScreen(navController, vm)
            }

            composable(
                Screen.ProjectDetail.route + "/{projectId}",
                arguments = listOf(
                    navArgument("projectId") { type = NavType.LongType }
                )
            ) { backStackEntry ->

                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0
                val vm: ProjectDetailsViewModel = hiltViewModel()

                ProjectDetailsScreen(navController, vm, projectId)
            }

        }

        navigation(
            route = Screen.RegisterGraph.route,
            startDestination = Screen.RegisterUserInfo.route
        ) {

            composable(Screen.RegisterUserInfo.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.RegisterGraph.route)
                }

                val registerViewModel: RegisterSharedViewModel = hiltViewModel(parentEntry)

                RegisterSecondStepScreen(
                    navController = navController,
                    registerViewModel = registerViewModel
                )
            }

            composable(Screen.RegisterRole.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.RegisterGraph.route)
                }

                val registerViewModel: RegisterSharedViewModel = hiltViewModel(parentEntry)

                RegisterStepOneScreen(
                    navController = navController,
                    registerViewModel = registerViewModel
                )
            }

            composable(
                Screen.RegisterInviteCompany.route + "/{roleLinkCompany}",
                arguments = listOf(
                    navArgument(name = "roleLinkCompany") {
                        type = NavType.StringType
                    }
                )) { backStackEntry ->

                val role = backStackEntry.arguments?.getString("roleLinkCompany") ?: ""
                val registerViewModel: RegisterLinkUserToCompanyViewModel = hiltViewModel()

                RegisterLinkUserToCompanyScreen(
                    navController = navController,
                    registerLinkUserToCompany = registerViewModel,
                    userRole = role
                )
            }

            composable(
                Screen.RegisterOwnerCompany.route
            ) { backStackEntry ->

                val registerViewModel: RegisterCompanyViewModel = hiltViewModel()

                RegisterCompanyScreen(
                    navController = navController,
                    registerViewModel = registerViewModel,
                )
            }
        }

//        composable(Screen.Home.route) {
//            val homeViewModel: HomeViewModel = hiltViewModel()
//            HomeScreen(navController, homeViewModel)
//        }
//        composable(Screen.NewProject.route) {
//            val newProjectViewModel: NewProjectViewModel = hiltViewModel()
//            NewProjectScreen(navController, newProjectViewModel)
//        }
//        composable(Screen.ProjectDetail.route + "/{projectId}",
//            arguments = listOf(
//                navArgument(name = "projectId") {
//                    type = NavType.LongType
//                }
//            )
//        ) {backStackEntry->
//            val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0
//
//            val projectDetailsViewModel : ProjectDetailsViewModel = hiltViewModel()
//            ProjectDetailsScreen(navController, projectDetailsViewModel, projectId = projectId)
//        }

    }
}
