package com.example.btppilot.presentation.navigation

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
import com.example.btppilot.ui.screens2.home.HomeScreen
import com.example.btppilot.ui.screens2.home.HomeViewModel
import com.example.btppilot.ui.screens2.profile.ProfileScreen
import com.example.btppilot.ui.screens2.profile.ProfileViewModel
import com.example.btppilot.ui.screens2.project.newProject.NewProjectScreen
import com.example.btppilot.ui.screens2.project.newProject.NewProjectViewModel
import com.example.btppilot.ui.screens2.project.projectDetails.ProjectDetailsScreen
import com.example.btppilot.ui.screens2.project.projectDetails.ProjectDetailsViewModel
import com.example.btppilot.ui.screens2.task.taskList.TaskListScreen
import com.example.btppilot.ui.screens2.task.taskList.TaskListViewModel
import com.example.btppilot.ui.screens2.task.newTask.NewTaskScreen
import com.example.btppilot.ui.screens2.task.newTask.NewTaskViewModel
import com.example.btppilot.ui.screens2.team.TeamScreen
import com.example.btppilot.ui.screens2.team.TeamViewModel


@Composable
fun MainNavHost(
    rootNavController: NavHostController,
    navController: NavHostController,
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
                val homeViewModel: HomeViewModel = hiltViewModel()

                HomeScreen(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    sharedViewModel = sharedViewModel,
                )
            }

            composable(Screen.TaskList.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                val taskListViewModel: TaskListViewModel = hiltViewModel()

                TaskListScreen(
                    navController = navController,
                    taskListViewModel = taskListViewModel,
                    sharedViewModel = sharedViewModel,
                )
            }

            composable(Screen.Team.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                val teamViewModel: TeamViewModel = hiltViewModel()

                TeamScreen(
                    navController = navController,
                    teamViewModel = teamViewModel,
                    sharedViewModel = sharedViewModel,
                )
            }

            composable(Screen.Profile.route) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                val profileViewModel: ProfileViewModel = hiltViewModel()

                ProfileScreen(
                    rootNavController = rootNavController,
                    profileViewModel = profileViewModel,
                    sharedViewModel = sharedViewModel,
                )
            }

            composable(Screen.NewProject.route + "/{projectId}",
                arguments = listOf(
                    navArgument("projectId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                val newProjectViewModel: NewProjectViewModel = hiltViewModel()

                NewProjectScreen(
                    navController = navController,
                    newProjectViewModel = newProjectViewModel,
                    projectId = projectId,
                    sharedViewModel = sharedViewModel
                )
            }

            composable(
                Screen.ProjectDetail.route + "/{projectId}",
                arguments = listOf(
                    navArgument("projectId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0
                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                val projectDetailsViewModel: ProjectDetailsViewModel = hiltViewModel()

                ProjectDetailsScreen(
                    navController = navController,
                    projectDetailsViewModel = projectDetailsViewModel,
                    sharedViewModel = sharedViewModel,
                    projectId = projectId
                )
            }

            composable(
                Screen.NewTask.route + "/{projectId}/{taskId}",
                arguments = listOf(
                    navArgument("projectId") {
                        type = NavType.LongType
                    },
                    navArgument("taskId") {
                        type = NavType.LongType
                    }
                )
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(NavGraph.MainGraph.route)
                }

                val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0
                val taskId = backStackEntry.arguments?.getLong("taskId") ?: 0

                val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                val newTaskViewModel: NewTaskViewModel = hiltViewModel()

                NewTaskScreen(
                    navController = navController,
                    newTaskViewModel = newTaskViewModel,
                    projectId = projectId,
                    taskId = taskId,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}