package com.example.btppilot.ui.screens.task.taskList

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btppilot.ui.screens.shared.SharedViewModel
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens.shared.uiState.EventState


@Composable
fun TaskListScreen(
    navController: NavController,
    taskListViewModel: TaskListViewModel,
    sharedViewModel: SharedViewModel,
) {
    val userInfo by sharedViewModel.userInfoStateFlow.collectAsState()
    val state by taskListViewModel.taskListStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isEnableToEditTask = sharedViewModel.isAuthorizedToEditTask()

    LaunchedEffect(Unit) {
        taskListViewModel.taskEventState.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route)

                is EventState.RedirectScreenWithId ->
                    navController.navigate(event.route)

                is EventState.PopBackStackWithRefresh -> {
                    sharedViewModel.refreshTask()
                    navController.popBackStack()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HeaderMainSreen(userName = userInfo.userFirstname) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = taskListViewModel::redirectAddTask,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", fontSize = 25.sp, color = Color.White)
            }
        },
    ) { padding ->

        TaskListContent(
            paddingValues = padding,
            tasklist = state,
            isEnableToEditTask = isEnableToEditTask,
            changeStatus = taskListViewModel::updateTask,
            redirectEditTask = taskListViewModel::redirectEditTask,
            deleteTask = taskListViewModel::deleteTask,
            filterTask = taskListViewModel::filterProject
        )
    }
}

