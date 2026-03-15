package com.example.btppilot.presentation.screens.project.newProject



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
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.uiState.EventState


@Composable
fun NewProjectScreen(
    navController: NavController,
    newProjectViewModel: NewProjectViewModel,
    projectId : Long,
    sharedViewModel: SharedViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val newProjectStateFlow by newProjectViewModel.newProjectStateFlow.collectAsState()

    if(projectId.toInt() != 0)
        newProjectViewModel.setProjectId(projectId)

    LaunchedEffect(Unit) {
        newProjectViewModel.newProjectEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)

                is EventState.PopBackStackWithRefresh -> {
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
                onClick = newProjectViewModel::publishOrEdit,
                modifier = Modifier.padding(10.dp)
            )
        }
    ) { padding ->
        NewProjectContent(
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


