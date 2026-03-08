package com.example.btppilot.presentation.navigation

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
