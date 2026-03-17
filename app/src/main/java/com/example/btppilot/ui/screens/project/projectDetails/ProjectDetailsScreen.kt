package com.example.btppilot.ui.screens.project.projectDetails


import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.btppilot.ui.screens.shared.SharedViewModel
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens.shared.eventState.EventState


@Composable
fun ProjectDetailsScreen(
    navController: NavController,
    projectDetailsViewModel: ProjectDetailsViewModel,
    sharedViewModel: SharedViewModel,
    projectId: Long
) {

    projectDetailsViewModel.setProjectId(projectId)
    val projectState by projectDetailsViewModel.detailProjectStateFlow.collectAsState()
    val isEnableToEditProject =
        sharedViewModel.isAuthorizedToEditDetailsProject(projectState.project)
    val isEnableToEditTaskStatus = sharedViewModel.isAuthorizedToEditStatus()
    val snackbarHostState = remember { SnackbarHostState() }
    val userInfo by sharedViewModel.userInfoStateFlow.collectAsState()
    val context = LocalContext.current
    val refresh by sharedViewModel.refreshTaskStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        if (refresh) {
            projectDetailsViewModel.fetchTasks()
            sharedViewModel.resetTaskRefresh()
        }
    }

    LaunchedEffect(Unit) {
        projectDetailsViewModel.detailProjectEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(context.getString(event.message))

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route)

                is EventState.RedirectScreenWithId ->
                    navController.navigate(event.route)

                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HeaderMainSreen(userName = userInfo.userFirstname) },
    ) { padding ->

        ProjectDetailsContent(
            paddingValues = padding,
            projectState = projectState,
            onClickDelete = projectDetailsViewModel::confirmDelete,
            onClickEdit = projectDetailsViewModel::onEditProject,
            onClickAddTask = projectDetailsViewModel::redirectAddTask,
            isEnableToEditProject = isEnableToEditProject,
            isEnableToEditStatus = isEnableToEditTaskStatus,
            changeStatus = projectDetailsViewModel::updateTask,
            redirectEditTask = projectDetailsViewModel::redirectEditTask,
            deleteTask = projectDetailsViewModel::deleteTask
        )
        if (projectState.showDialog)
            ConfirmDeleteProjectDialog(
                onConfirm = projectDetailsViewModel::deleteProject,
                onDismiss = projectDetailsViewModel::closeDialogDelete
            )
    }
}
