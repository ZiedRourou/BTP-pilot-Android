package com.example.btppilot.ui.screens.task.newOrEditTask

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.ui.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.ui.screens.shared.SharedViewModel
import com.example.btppilot.ui.screens.shared.component.AppPrimaryButton
import com.example.btppilot.ui.screens.shared.uiState.EventState

@Composable
fun NewOrEditTaskScreen(
    navController: NavController,
    newTaskViewModel: NewOrEditTaskViewModel,
    projectId: Long,
    taskId : Long,
    sharedViewModel: SharedViewModel
){


    if (taskId.toInt() != 0)
        newTaskViewModel.setTaskId(taskId)
    if (projectId.toInt() != 0)
        newTaskViewModel.setProjectId(projectId)

    val snackbarHostState = remember { SnackbarHostState() }
    val newTaskStateFlow by newTaskViewModel.newTaskStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        newTaskViewModel.newTaskEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)
                is EventState.PopBackStackWithRefresh ->
                {
                    sharedViewModel.refreshTask()
                    navController.popBackStack()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { ProjectAndTaskEditorTopBar("Nouveau chantier") },
        bottomBar = {
            AppPrimaryButton(
                text = "Enregistrer",
                onClick = newTaskViewModel::publishOrEdit,
                modifier = Modifier.padding(10.dp)
            )
        }
    ) { padding ->
        NewOrEditTaskContent(
            paddingValues = padding,
            taskData = newTaskStateFlow,
            onPriorityChange = newTaskViewModel::onPriorityChange,
            onStatusChange = newTaskViewModel::onStatusChange,
            onEndDateChange = newTaskViewModel::onEndDateChange,
            onBeginDateChange = newTaskViewModel::onBeginDateChange,
            onDescriptionChange = newTaskViewModel::onDescriptionChange,
            onTitleChange = newTaskViewModel::onTitleChange,
            onHoursChange = newTaskViewModel::onHoursChange,
            onProjectChange = newTaskViewModel::onProjectChange,
            onUsersChange = newTaskViewModel::onClientListChange
        )
    }
}

