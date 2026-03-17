package com.example.btppilot.ui.screens.project.newOrEditProject



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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.R
import com.example.btppilot.ui.screens.shared.component.AppPrimaryButton
import com.example.btppilot.ui.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.ui.screens.shared.SharedViewModel
import com.example.btppilot.ui.screens.shared.eventState.EventState


@Composable
fun NewOrEditProjectScreen(
    navController: NavController,
    newProjectViewModel: NewOrEditProjectViewModel,
    projectId : Long,
    sharedViewModel: SharedViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val newProjectStateFlow by newProjectViewModel.newProjectStateFlow.collectAsState()
    val context = LocalContext.current

    if(projectId.toInt() != 0)
        newProjectViewModel.setProjectId(projectId)

    LaunchedEffect(Unit) {
        newProjectViewModel.newProjectEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(context.getString(event.message))

                is EventState.PopBackStackWithRefresh -> {
                    sharedViewModel.refreshProject()
                    navController.popBackStack()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { ProjectAndTaskEditorTopBar(stringResource(R.string.new_project)) },
        bottomBar = {
            AppPrimaryButton(
                text = stringResource(R.string.save),
                onClick = newProjectViewModel::publishOrEdit,
                modifier = Modifier.padding(10.dp)
            )
        }
    ) { padding ->
        NewOrEditProjectContent(
            paddingValues = padding,
            projectData = newProjectStateFlow,
            onTitleChange = newProjectViewModel::onTitleChange,
            onDescriptionChange = newProjectViewModel::onDescriptionChange,
            onBeginDateChange = newProjectViewModel::onBeginDateChange,
            onEndDateChange = newProjectViewModel::onEndDateChange,
            onManagerChange = newProjectViewModel::onManagerChange,
            onPriorityChange = newProjectViewModel::onPriorityChange,
            onStatusChange =  newProjectViewModel::onStatusChange,
            onClientListChange =newProjectViewModel::onClientListChange,
            onEmployeeListChange = newProjectViewModel::onEmployeeListChange
        )
    }
}


