package com.example.btppilot.ui.screens.project.newOrEditProject


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PersonAddAlt
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.ui.screens.project.component.AppFieldProject
import com.example.btppilot.ui.screens.project.component.AppLabelTextFieldNewProject
import com.example.btppilot.ui.screens.project.component.AppSelectPriorityRadioBtnField
import com.example.btppilot.ui.screens.project.component.AppSelectStatusRadioBtnField
import com.example.btppilot.ui.screens.project.component.AppSelectUserMultiField
import com.example.btppilot.ui.screens.project.component.AppSelectUserRadioBtnFieldProject
import com.example.btppilot.ui.screens.project.component.DateRangeField
import com.example.btppilot.ui.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.ui.screens.shared.component.AppPrimaryButton
import com.example.btppilot.ui.screens.shared.component.LoadingOverlay
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
            NewOrEditProjectContent(
                paddingValues = padding,
                projectData = NewOrEditProjectViewModel.NewProjectState(),
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
fun NewOrEditProjectContent(
    paddingValues: PaddingValues,
    projectData: NewOrEditProjectViewModel.NewProjectState,
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

    LoadingOverlay(isVisible = projectData.isLoading)

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(20.dp)
            .verticalScroll(scrollState)
    ) {

        AppLabelTextFieldNewProject(
            image = Icons.Outlined.Info,
            title = stringResource(R.string.informations)
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray
        )

        AppFieldProject(
            label = stringResource(R.string.project_name),
            value = projectData.title,
            onValueChange = onTitleChange,
            supportingText = projectData.titleError,
            isError = projectData.titleError != null,
            singleLine = true
        )

        AppFieldProject(
            label = stringResource(R.string.desc_project),
            value = projectData.description,
            onValueChange = onDescriptionChange,
            supportingText = projectData.descriptionError,
            isError = projectData.descriptionError != null,
            singleLine = false,
            minLines = 4,
            maxLines = 4
        )
        Spacer(modifier = Modifier.height(10.dp))

        AppLabelTextFieldNewProject(
            image = Icons.Outlined.WarningAmber,
            title = stringResource(R.string.status_and_priority)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray
        )
        AppSelectPriorityRadioBtnField(
            label = stringResource(R.string.status_label),
            options = projectData.projectPriorities,
            selectedOption = projectData.selectedPriority,
            onSelectionChange = onPriorityChange
        )
        AppSelectStatusRadioBtnField(
            label = stringResource(R.string.status),
            options = projectData.projectStatus,
            selectedOption = projectData.selectedStatus,
            onSelectionChange = onStatusChange
        )
        Spacer(modifier = Modifier.height(10.dp))


        AppLabelTextFieldNewProject(
            image = Icons.Outlined.Engineering,
            title = stringResource(R.string.members)
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray
        )
        AppSelectUserRadioBtnFieldProject(
            label = stringResource(R.string.chef_project),
            options = projectData.userOption,
            selectedOption = projectData.manager,
            onSelectionChange = onManagerChange,
        )

        Spacer(modifier = Modifier.height(10.dp))
        if (projectData.clientOption.isNotEmpty()) {

            AppSelectUserMultiField(
                label = stringResource(id = R.string.client),
                options = projectData.clientOption,
                selectedUsers = projectData.clientList,
                onSelectionChange = onClientListChange
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (projectData.userOption.any { it.role == UserRole.COLLABORATOR.name }) {
            AppSelectUserMultiField(
                label = stringResource(R.string.project_teams),
                options = projectData.userOption.filter {
                    it.role == UserRole.COLLABORATOR.name &&
                            it.user.id != projectData.manager.id
                },
                selectedUsers = projectData.collaboratorList,
                onSelectionChange = onEmployeeListChange
            )
        }
        Spacer(modifier = Modifier.height(10.dp))


        AppLabelTextFieldNewProject(
            image = Icons.Outlined.CalendarMonth,
            title = "Date"
        )

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray
        )

        DateRangeField(
            startDate = projectData.dateBegin,
            endDate = projectData.dateEnd,
        ) { start, end ->

            onBeginDateChange(start)
            onEndDateChange(end)
        }
    }
}




