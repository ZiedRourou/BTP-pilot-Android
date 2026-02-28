package com.example.btppilot.presentation.navigation

import android.content.SharedPreferences
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.example.btppilot.presentation.common.UserRole

//sealed class BottomNavigation(
//    val route: String,
//    val title: String,
//    val icon: ImageVector
//) {
//    object Home : BottomNavigation("home", "Accueil", Icons.Filled.Home)
//    object Task : BottomNavigation("task", "Taches", Icons.Filled.Checklist)
//    object Team : BottomNavigation("team", "Team", Icons.Filled.Groups)
//    object Profile : BottomNavigation("profile", "Profile", Icons.Filled.Settings)
//}
//
//fun bottomItemsForRole(role: UserRole): List<BottomNavigation> {
//    return when (role) {
//        UserRole.MANAGER -> listOf(
//            BottomNavigation.Home,
//            BottomNavigation.Task,
//            BottomNavigation.Team,
//            BottomNavigation.Profile
//        )
//
//        UserRole.CLIENT -> listOf(
//            BottomNavigation.Home,
//            BottomNavigation.Task,
//            BottomNavigation.Profile
//        )
//
//        else -> {
//            listOf( BottomNavigation.Home)}
//    }
//}
//
//@Composable
//fun NavHostTest() {
//
//
//    val userRole = //recupeRole depuis sharepreferencies
//    val navigation  = when (userRole){
//        UserRole.CLIENT -> BottomNavigationUserNonOwnerCompany,
//        UserRole.MANAGER -> BottomNavigationOwnerCompany
//
//        else -> {BottomNavigationUserNonOwnerCompany}
//    }
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = navigation.Home.route) {
//        composable(MainNavigationGraph.Home.route) {
//
//        }
//
//        composable(Screen.Infos.route) {
//            val infoViewModel: InfosViewModel = hiltViewModel()
//            InfoScreen(navController, infoViewModel)
//        }
//
//
//    }
//}
