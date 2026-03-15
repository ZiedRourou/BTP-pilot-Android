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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.presentation.screens.project.component.AppDateField
import com.example.btppilot.presentation.screens.project.component.AppFieldProject
import com.example.btppilot.presentation.screens.project.component.AppLabelTextFieldNewProject
import com.example.btppilot.presentation.screens.project.component.AppSelectPriorityRadioBtnField
import com.example.btppilot.presentation.screens.project.component.AppSelectStatusRadioBtnField
import com.example.btppilot.presentation.screens.project.component.AppSelectUserMultiField
import com.example.btppilot.presentation.screens.project.component.AppSelectUserRadioBtnFieldProject
import com.example.btppilot.presentation.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.UserRole

@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NewProjectScreenPreview() {

    val snackbarHostState = remember { SnackbarHostState() }

    BtpPilotTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                ProjectAndTaskEditorTopBar("Nouveau chantier")
            },
            bottomBar = {
                AppPrimaryButton(
                    text = "Enregistrer",
                    onClick = {},
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .padding(bottom = 20.dp)
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
                onClientListChange = {},
                onStatusChange = {}
            )
        }
    }
}


@Composable
fun NewProjectContent(
    paddingValues: PaddingValues,
    projectData: NewProjectViewModel.NewProjectState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onManagerChange: (UserCompany) -> Unit,
    onClientListChange: (List<UserCompany>) -> Unit,
    onEmployeeListChange: (List<UserCompany>) -> Unit,
    onBeginDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onPriorityChange: (ProjectAndTakPriorities) -> Unit,
    onStatusChange: (ProjectStatus) -> Unit,
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
            supportingText = projectData.descriptionError,
            isError = projectData.descriptionError != null,
            singleLine = false,
            minLines = 4,
            maxLines = 4
        )

        AppSelectPriorityRadioBtnField(
            label = "Priorité",
            options = projectData.projectPriorities,
            selectedOption = projectData.selectedPriority,
            onSelectionChange = onPriorityChange
        )
        AppSelectStatusRadioBtnField(
            label = "Status",
            options = projectData.projectStatus ,
            selectedOption = projectData.selectedStatus,
            onSelectionChange = onStatusChange
        )

        AppLabelTextFieldNewProject(image = Icons.Outlined.PersonAddAlt, title = "Manager")

        if (projectData.userOption.isNotEmpty()) {
            AppSelectUserRadioBtnFieldProject(
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
    }
}




