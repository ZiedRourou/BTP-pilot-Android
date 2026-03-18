package com.example.btppilot.ui.screens.task.newOrEditTask

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Engineering
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.ui.screens.home.ProjectsNotFound
import com.example.btppilot.ui.screens.project.component.AppFieldProject
import com.example.btppilot.ui.screens.project.component.AppLabelTextFieldNewProject
import com.example.btppilot.ui.screens.project.component.AppSelectPriorityRadioBtnField
import com.example.btppilot.ui.screens.project.component.DateRangeField
import com.example.btppilot.ui.screens.project.component.ProjectAndTaskEditorTopBar
import com.example.btppilot.ui.screens.shared.component.AppPrimaryButton
import com.example.btppilot.ui.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.TaskStatus
import com.example.btppilot.util.UserRole
import com.example.btppilot.util.toHourMinuteDisplay


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
                onProjectChange = { },
                onDurationSelected = { heure, min -> }
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
    onProjectChange: (ProjectResponseByUserCompanyDtoItem) -> Unit,
    onDurationSelected: (Int, Int) -> Unit

) {
    val scrollState = rememberScrollState()
    var showPicker by remember { mutableStateOf(false) }

    LoadingOverlay(isVisible = taskData.isLoading)
    if (taskData.project.isNullOrEmpty())
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {
            ProjectsNotFoundNewTask()
        }
    else
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {

            AppLabelTextFieldNewProject(
                image = Icons.Outlined.Info,
                title = stringResource(id = R.string.informations)
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray
            )
            if (!taskData.isEditorMode)
                AppSelectProjectRadioBtnField(
                    label = stringResource(R.string.project_concerned),
                    options = taskData.project,
                    selectedOption = taskData.selectedProject ?: taskData.project.first(),
                    onSelectionChange = onProjectChange,
                    isError = taskData.projectError!=null,
                    supportingText = taskData.projectError
                )
            AppFieldProject(
                label = stringResource(R.string.task_title),
                value = taskData.title,
                onValueChange = onTitleChange,
                supportingText = taskData.titleError,
                isError = taskData.titleError != null,
                singleLine = true
            )

            AppFieldProject(
                label = stringResource(R.string.task_desc),
                value = taskData.description,
                onValueChange = onDescriptionChange,
                supportingText = taskData.descriptionError,
                isError = taskData.descriptionError != null,
                singleLine = false,
                minLines = 4,
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(10.dp))

            AppLabelTextFieldNewProject(
                image = Icons.Outlined.WarningAmber,
                title = stringResource(id = R.string.status_and_priority)
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray
            )
            AppSelectPriorityRadioBtnField(
                label = stringResource(id =R.string.priority ),
                options = taskData.taskPriorities,
                selectedOption = taskData.selectedPriority,
                onSelectionChange = onPriorityChange
            )
            AppSelectPriorityRadioBtnFieldTask(
                label = stringResource( R.string.status),
                options = taskData.taskStatus,
                selectedOption = taskData.selectedStatus,
                onSelectionChange = onStatusChange
            )
            Spacer(modifier = Modifier.height(10.dp))


            AppLabelTextFieldNewProject(
                image = Icons.Outlined.Engineering,
                title = stringResource(id = R.string.members)
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray
            )
            AppSelectUserMultiFieldTask(
                label = stringResource(R.string.assignation),
                options = taskData.selectedProject?.userProjects?.filter { it.projectRole!= UserRole.CLIENT.name }
                    ?: taskData.project.first().userProjects.filter { it.projectRole!= UserRole.CLIENT.name },
                selectedUsers = taskData.selectUser,
                onSelectionChange = onUsersChange,
                isError = taskData.selectedUserError != null,
                supportingText = taskData.selectedUserError
            )


            AppLabelTextFieldNewProject(
                image = Icons.Outlined.CalendarToday,
                title = stringResource(R.string.date_and_hour)
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.LightGray
            )
            DateRangeField(
                startDate = taskData.dateBegin,
                endDate = taskData.dateEnd,
            ) { start, end ->

                onBeginDateChange(start)
                onEndDateChange(end)
            }

            Spacer(modifier = Modifier.height(10.dp))

            DurationField(
                value = taskData.estimateHours.toHourMinuteDisplay(),
                onClick = { showPicker = true }
            )

            if (showPicker) {
                DurationTimePickerDialog(
                    onConfirm = { h, m ->
                        onDurationSelected(h, m)
                        showPicker = false
                    },
                    onDismiss = { showPicker = false }
                )
            }
        }
}

@Composable
fun DurationField(
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(
            text = stringResource(R.string.hour_estimation),
            style = MaterialTheme.typography.labelMedium,
            color = StatusInProgress,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = onClick) {
                    Icon(Icons.Default.AccessTime, null, tint = Color.White)
                }
            },
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(12.dp),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorTextColor = Color.White,

                errorContainerColor = MaterialTheme.colorScheme.secondary,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,

                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray
            )
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DurationTimePickerDialog(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {

    val state = rememberTimePickerState(
        initialHour = initialHours,
        initialMinute = initialMinutes,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(state.hour, state.minute)
                }
            ) {
                Text(stringResource(id = R.string.btn_txt_validate))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.estimation_hour_dure)) },
        text = {

            TimePicker(
                state = state
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectUserMultiFieldTask(
    label: String,
    options: List<UserProject>,
    selectedUsers: List<UserProject>,
    onSelectionChange: (List<UserProject>) -> Unit,
    supportingText: Int? = null,
    isError: Boolean = false,
) {

    var expanded by remember { mutableStateOf(false) }

    val displayText = if (selectedUsers.isEmpty()) {
        "Sélectionner"
    } else {
        selectedUsers.joinToString { "${it.user.firstName}" }
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

                ),
                isError = isError,
                supportingText = {
                    supportingText?.let {
                        Text(
                            text = stringResource(id = it),
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

                                Text("${option.user.firstName} ")
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
    onSelectionChange: (ProjectResponseByUserCompanyDtoItem) -> Unit,
    isError: Boolean,
    supportingText: Int?
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
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    errorContainerColor = MaterialTheme.colorScheme.secondary,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorTextColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray
                ),
                isError = isError,
                supportingText = {
                    supportingText?.let {
                        Text(
                            text = stringResource(id = it),
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

@Composable
fun ProjectsNotFoundNewTask(
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        color = MaterialTheme.colorScheme.secondary,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.task_list_nor_project))
        }
    }
}

