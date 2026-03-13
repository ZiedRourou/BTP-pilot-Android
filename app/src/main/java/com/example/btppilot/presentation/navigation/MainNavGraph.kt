package com.example.btppilot.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.home.HomeScreen
import com.example.btppilot.presentation.screens.home.HomeViewModel
import com.example.btppilot.presentation.screens.profile.ProfileScreen
import com.example.btppilot.presentation.screens.profile.ProfileViewModel
import com.example.btppilot.presentation.screens.project.newProject.NewProjectScreen
import com.example.btppilot.presentation.screens.project.newProject.NewProjectViewModel
import com.example.btppilot.presentation.screens.project.projectDetails.ProjectDetailsScreen
import com.example.btppilot.presentation.screens.project.projectDetails.ProjectDetailsViewModel
import com.example.btppilot.presentation.screens.task.TaskListScreen
import com.example.btppilot.presentation.screens.task.TaskListViewModel
import com.example.btppilot.presentation.screens.task.newTask.NewTaskScreen
import com.example.btppilot.presentation.screens.task.newTask.NewTaskViewModel
import com.example.btppilot.presentation.screens.team.TeamScreen
import com.example.btppilot.presentation.screens.team.TeamViewModel


@Composable
fun MainNavHost(
    rootNavController: NavHostController,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = NavGraph.MainGraph.route
    ) {
        navigation(
            route = NavGraph.MainGraph.route,
            startDestination = Screen.Home.route
        ) {

            composable(Screen.Home.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                val vm: HomeViewModel = hiltViewModel()

                HomeScreen(
                    navController = navController,
                    homeViewModel = vm,
                    sharedViewModel = sharedViewModel,
                    snackbarHostState = snackbarHostState
                )
            }

            composable(Screen.TaskList.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                val vm: TaskListViewModel = hiltViewModel()

                TaskListScreen(
                    navController = navController,
                    taskListViewModel = vm,
                    sharedViewModel = sharedViewModel,
                    snackbarHostState = snackbarHostState
                )
            }

            composable(Screen.Team.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                val vm: TeamViewModel = hiltViewModel()

                TeamScreen(
                    navController = navController,
                    teamViewModel = vm,
                    sharedViewModel = sharedViewModel,
                    snackbarHostState = snackbarHostState
                )
            }

            composable(Screen.Profile.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                val vm: ProfileViewModel = hiltViewModel()

                ProfileScreen(
                    rootNavController = rootNavController,
                    profileViewModel = vm,
                    sharedViewModel = sharedViewModel,
                    snackbarHostState = snackbarHostState
                )
            }

            composable(Screen.NewProject.route + "/{projectId}",
            arguments = listOf(
                navArgument("projectId") {
                    type = NavType.LongType
                }
            )
            ) {
                val projectId = it.arguments?.getLong("projectId") ?: 0
                val vm: NewProjectViewModel = hiltViewModel()

                NewProjectScreen(navController, vm, projectId)
            }

            composable(
                Screen.ProjectDetail.route + "/{projectId}",
                arguments = listOf(
                    navArgument("projectId") {
                        type = NavType.LongType
                    }
                )
            ) {

                val projectId = it.arguments?.getLong("projectId") ?: 0
                val vm: ProjectDetailsViewModel = hiltViewModel()

                ProjectDetailsScreen(
                    navController,
                    vm,
                    projectId
                )
            }

            composable(
                Screen.NewTask.route + "/{projectId}",
                arguments = listOf(
                    navArgument("projectId") {
                        type = NavType.LongType
                    }
                )
            ) {

                val projectId = it.arguments?.getLong("projectId") ?: 0
                val vm: NewTaskViewModel = hiltViewModel()

                NewTaskScreen(
                    navController,
                    vm,
                    projectId
                )
            }
        }
    }
}