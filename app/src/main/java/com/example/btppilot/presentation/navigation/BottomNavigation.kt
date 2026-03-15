package com.example.btppilot.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.btppilot.util.UserRole

fun bottomNavItems(role: UserRole): List<Screen> {

    return when (role) {

        UserRole.OWNER -> listOf(
            Screen.Home,
            Screen.TaskList,
            Screen.Team,
            Screen.Profile
        )

        UserRole.COLLABORATOR -> listOf(
            Screen.Home,
            Screen.TaskList,
            Screen.Profile
        )

        UserRole.CLIENT -> listOf(
            Screen.Home,
            Screen.Profile
        )
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    role: UserRole
) {

    val itemsMenuByUserRole = bottomNavItems(role)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar (

    ){

        itemsMenuByUserRole.forEach { screen ->

            NavigationBarItem(

                icon = {
                    Icon(screen.icon!!, contentDescription = null)
                },

                label = {
                    Text(screen.title!!)
                },

                selected = currentRoute == screen.route,

                onClick = {

                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}