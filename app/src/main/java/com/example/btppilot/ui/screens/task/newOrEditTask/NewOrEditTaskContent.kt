package com.example.btppilot.ui.screens.task.newOrEditTask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.ui.screens.project.component.AppDateField
import com.example.btppilot.ui.screens.project.component.AppFieldProject
import com.example.btppilot.ui.screens.project.component.AppLabelTextFieldNewProject
import com.example.btppilot.ui.screens.project.component.AppSelectPriorityRadioBtnField
import com.example.btppilot.ui.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.ui.screens.shared.component.AppPrimaryButton
import com.example.btppilot.ui.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.TaskStatus


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NewTaskScreenPreview() {

    val snackbarHostState = remember { SnackbarHostState() }

    BtpPilotTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                ProjectAndTaskEditorTopBar("Nouvelle tache")
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
            NewOrEditTaskContent(
                paddingValues = padding,
                taskData = NewOrEditTaskViewModel.NewTaskState(),
                onTitleChange = { },
                onDescriptionChange = { },
                onUsersChange = { },
                onBeginDateChange = { },
                onEndDateChange = { },
                onPriorityChange = { },
                onStatusChange = { },
                onHoursChange = { },
                onProjectChange = { },
            )
        }
    }
}

@Composable
fun NewOrEditTaskContent(
    paddingValues: PaddingValues,
    taskData: NewOrEditTaskViewModel.NewTaskState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUsersChange: (List<UserProject>) -> Unit,
    onBeginDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onPriorityChange: (ProjectAndTakPriorities) -> Unit,
    onStatusChange: (TaskStatus) -> Unit,
    onHoursChange: (String) -> Unit,
    onProjectChange: (ProjectResponseByUserCompanyDtoItem) -> Unit

) {
    val scrollState = rememberScrollState()

    LoadingOverlay(isVisible = taskData.isLoading)
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
        if ((!taskData.isEditorMode) && taskData.project != null && taskData.selectedProject != null)
            AppSelectProjectRadioBtnField(
                label = "Projet concerné",
                options = taskData.project,
                selectedOption = taskData.selectedProject,
                onSelectionChange = onProjectChange
            )
        AppFieldProject(
            label = "Titre de la tache",
            value = taskData.title,
            onValueChange = onTitleChange,
            supportingText = taskData.titleError,
            isError = taskData.titleError != null,
            singleLine = true
        )

        AppFieldProject(
            label = "Description de la tache",
            value = taskData.description,
            onValueChange = onDescriptionChange,
            supportingText = taskData.descriptionError,
            isError = taskData.descriptionError != null,
            singleLine = false,
            minLines = 4,
            maxLines = 4
        )

        if (taskData.selectedProject != null)
            AppSelectUserMultiFieldTask(
                label = "Membres sur la tache ",
                options = taskData.selectedProject.userProjects,
                selectedUsers = taskData.selectUser,
                onSelectionChange = onUsersChange,
                isError = taskData.selectedUserError != null,
                supportingText = taskData.selectedUserError
            )

        AppDateField(
            label = "Date début",
            selectedDate = taskData.dateBegin,
            onDateSelected = onBeginDateChange,

        )
        AppDateField(
            label = "date fin ",
            selectedDate = taskData.dateEnd,
            onDateSelected = onEndDateChange,
            isError = taskData.dateEndError != null,
            supportingText = taskData.dateEndError
        )

        AppFieldProject(
            label = "Estimation temps (Heure)",
            value = taskData.estimateHours,
            isError = taskData.estimateHoursError != null,
            supportingText = taskData.estimateHoursError,
            onValueChange = onHoursChange
        )

        AppSelectPriorityRadioBtnField(
            label = "test",
            options = taskData.taskPriorities,
            selectedOption = taskData.selectedPriority,
            onSelectionChange = onPriorityChange
        )
        AppSelectPriorityRadioBtnFieldTask(
            label = "test",
            options = taskData.taskStatus,
            selectedOption = taskData.selectedStatus,
            onSelectionChange = onStatusChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectUserMultiFieldTask(
    label: String,
    options: List<UserProject>,
    selectedUsers: List<UserProject>,
    onSelectionChange: (List<UserProject>) -> Unit,
    supportingText: String? = null,
    isError: Boolean = false,
) {

    var expanded by remember { mutableStateOf(false) }

    val displayText = if (selectedUsers.isEmpty()) {
        "Sélectionner"
    } else {
        selectedUsers.joinToString { "${it.user.firstName} ${it.user.lastName}" }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = StatusInProgress,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = displayText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent

                ), isError = isError,
                supportingText = {
                    supportingText?.let {
                        Text(
                            text = it,
                            color = if (isError)
                                MaterialTheme.colorScheme.error
                            else
                                Color.Gray
                        )
                    }
                },
                )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                options.forEach { option ->

                    val isSelected = selectedUsers.any { it.user.id == option.user.id }

                    DropdownMenuItem(
                        text = {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null
                                )

                                Spacer(Modifier.width(5.dp))

                                Text("${option.user.firstName} ${option.user.lastName}")
                            }
                        },
                        onClick = {

                            val newList =
                                if (isSelected)
                                    selectedUsers.filter { it.user.id != option.user.id }
                                else
                                    selectedUsers + option

                            onSelectionChange(newList)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectPriorityRadioBtnFieldTask(
    label: String,
    options: ArrayList<TaskStatus>,
    selectedOption: TaskStatus,
    onSelectionChange: (TaskStatus) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = StatusInProgress,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = selectedOption.label,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = selectedOption.color.copy(alpha = 0.35F),
                    focusedContainerColor = selectedOption.color.copy(alpha = 0.35F),
                    focusedTextColor = selectedOption.color,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                RadioButton(
                                    selected = option == selectedOption,
                                    onClick = null
                                )

                                Spacer(Modifier.width(5.dp))

                                Text(text = option.label, color = option.color)
                            }
                        },
                        onClick = {
                            onSelectionChange(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectProjectRadioBtnField(
    label: String,
    options: List<ProjectResponseByUserCompanyDtoItem>,
    selectedOption: ProjectResponseByUserCompanyDtoItem,
    onSelectionChange: (ProjectResponseByUserCompanyDtoItem) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = StatusInProgress,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = selectedOption.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),

                )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                RadioButton(
                                    selected = option == selectedOption,
                                    onClick = null
                                )

                                Spacer(Modifier.width(5.dp))

                                Text(text = option.name)
                            }
                        },
                        onClick = {
                            onSelectionChange(option)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

