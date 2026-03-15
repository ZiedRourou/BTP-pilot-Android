package com.example.btppilot.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.btppilot.ui.screens.main.MainScreen

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Splash : Screen("splash")
    object Login : Screen("login")

    object RegisterRole : Screen("register_role")
    object RegisterUserInfo : Screen("register_user_info")
    object RegisterOwnerCompany : Screen("register_owner_company")
    object RegisterInviteCompany : Screen("register_invite_company")

    object Home : Screen("home", "Accueil", Icons.Filled.Home)

    object NewProject : Screen("new_project")
    object UpdateProject : Screen("new_project")
    object ProjectDetail : Screen("project_detail")

    object TaskList : Screen("task_list", "Taches", Icons.Filled.Checklist)
    object NewTask : Screen("new_task", "Taches", Icons.Filled.Checklist)

    object Team : Screen("team", "Team", Icons.Filled.Groups)
    object Profile : Screen("profile", "Profile", Icons.Filled.Settings)
}


sealed class NavGraph(
    val route: String
) {
    object MainGraph : NavGraph("main_graph")
    object RegisterGraph : NavGraph("register_graph")
    object AuthGraph : NavGraph("auth_graph")
}

@Composable
fun RootNavGraph(
    rootNavController: NavHostController
) {

    NavHost(
        navController = rootNavController,
        startDestination = NavGraph.AuthGraph.route
    ) {

        authNavGraph(rootNavController)


        composable(
            route = NavGraph.MainGraph.route,
        ) {
            MainScreen(
                rootNavController = rootNavController
            )
        }
    }
}