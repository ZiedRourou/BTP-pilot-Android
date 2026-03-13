package com.example.btppilot.presentation.screens.project.newProject


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.project.component.AppDateField
import com.example.btppilot.presentation.screens.project.component.AppFieldProject
import com.example.btppilot.presentation.screens.project.component.AppLabelTextFieldNewProject
import com.example.btppilot.presentation.screens.project.component.AppSelectPriorityRadioBtnField
import com.example.btppilot.presentation.screens.project.component.AppSelectUserMultiField
import com.example.btppilot.presentation.screens.project.component.AppSelectUserRadioBtnField
import com.example.btppilot.presentation.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.UserRole


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NewProjectScreenPreview() {

    val snackbarHostState = remember { SnackbarHostState() }

    BtpPilotTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                ProjectAndTaskEditorTopBar("Nouveau chantier") },
            bottomBar = {
                AppPrimaryButton(
                    text = "Enregistrer",
                    onClick = {},
                    modifier = Modifier.padding(5.dp)
                )
            },
        ) { padding ->
            NewProjectContent(
                paddingValues = padding,
                projectData = NewProjectViewModel.NewProjectState(),
                onTitleChange = {},
                onDescriptionChange = {},
                onBeginDateChange = {},
                onEndDateChange = {},
                onManagerChange = {},
                onPriorityChange = {},
                onEmployeeListChange = {},
                onClientListChange = {}
            )
        }
    }
}

@Composable
fun NewProjectScreen(
    navController: NavController,
    newProjectViewModel: NewProjectViewModel,
    projectId : Long
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

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route)
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
            onClientListChange =newProjectViewModel::onClientListChange,
            onEmployeeListChange = newProjectViewModel::onEmployeeListChange
        )
    }
}


@Composable
fun NewProjectContent(
    paddingValues: PaddingValues,
    projectData: NewProjectViewModel.NewProjectState,
    onTitleChange : (String) -> Unit,
    onDescriptionChange : (String) -> Unit,
    onManagerChange : (UserCompany) -> Unit,
    onClientListChange : (List<UserCompany>) -> Unit,
    onEmployeeListChange : (List<UserCompany>) -> Unit,
    onBeginDateChange : (String) -> Unit,
    onEndDateChange : (String) -> Unit,
    onPriorityChange : (ProjectAndTakPriorities) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(20.dp)
            .verticalScroll(scrollState)
    ) {

        AppLabelTextFieldNewProject(
            image = Icons.Outlined.Info,
            title = "Informations"
        )

        AppFieldProject(
            label = "Nom du chantier",
            value = projectData.title,
            onValueChange = onTitleChange,
            supportingText = projectData.titleError,
            isError = projectData.titleError != null,
            singleLine = true
        )

        AppFieldProject(
            label = "Description du chantier",
            value = projectData.description,
            onValueChange = onDescriptionChange,
            singleLine = false,
            minLines = 4,
            maxLines = 4
        )

        AppLabelTextFieldNewProject(image = Icons.Outlined.PersonAddAlt, title = "Manager")

        if (projectData.userOption.isNotEmpty()){
            AppSelectUserRadioBtnField(
                label = "Chef de chantier",
                options = projectData.userOption,
                selectedOption = projectData.manager,
                onSelectionChange = onManagerChange,
            )
            Spacer(modifier = Modifier.height(10.dp))
            AppSelectUserMultiField(
                label = "Clients",
                options = projectData.clientOption,
                selectedUsers = projectData.clientList,
                onSelectionChange = onClientListChange
            )
            Spacer(modifier = Modifier.height(10.dp))

            AppSelectUserMultiField(
                label = "L'équipe du projet",
                options = projectData.userOption.filter { it.role == UserRole.COLLABORATOR.name },
                selectedUsers = projectData.collaboratorList,
                onSelectionChange = onEmployeeListChange
            )

        }


        AppLabelTextFieldNewProject(image = Icons.Outlined.CalendarMonth, title = "Date")


        AppDateField(
            label = "Date début du chantier",
            selectedDate = projectData.dateBegin,
            onDateSelected = onBeginDateChange,

            )

        AppDateField(
            label = "Date fin du chantier",
            selectedDate = projectData.dateEnd,
            onDateSelected = onEndDateChange,
            isError = projectData.dateEndError != null,
            supportingText = projectData.dateEndError
        )

        AppSelectPriorityRadioBtnField(
            label = "Priorité",
            options = projectData.projectPriorities,
            selectedOption = ProjectAndTakPriorities.LOW,
            onSelectionChange = onPriorityChange
        )
    }
}
