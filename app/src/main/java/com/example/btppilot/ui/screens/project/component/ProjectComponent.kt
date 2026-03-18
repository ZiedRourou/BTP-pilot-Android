package com.example.btppilot.ui.screens.project.component

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.ui.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import java.lang.reflect.Array.set
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun ProjectAndTaskEditorTopBar(
    title: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.LightGray
        )
    }
}


@Composable
fun AppFieldProject(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    supportingText: Int? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 3,
    placeholder: String = label,
) {

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

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            minLines = if (singleLine) 1 else minLines,
            maxLines = if (singleLine) 1 else maxLines,
            isError = isError,
            placeholder = {
                Text(text = placeholder)
            },
            supportingText = {
                supportingText?.let {
                    Text(
                        text = stringResource(it),
                        color = if (isError)
                            MaterialTheme.colorScheme.error
                        else
                            Color.Gray
                    )
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


@Composable
fun AppLabelTextFieldNewProject(
    image: ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        Icon(
            imageVector = image,
            contentDescription = null,
            tint = StatusInProgress
        )
        Spacer(modifier = Modifier.width(5.dp))
        AppSecondaryTitle(text = title)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectUserMultiField(
    label: String,
    options: List<UsersOfCompanyItem>,
    selectedUsers: List<UserCompany>,
    onSelectionChange: (List<UserCompany>) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    val displayText = if (selectedUsers.isEmpty()) {
        "Sélectionner"
    } else {
        selectedUsers.joinToString { "${it.firstName} " }
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

                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                options.forEach { option ->

                    val isSelected = selectedUsers.any { it.id == option.user.id }

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

                                Text(option.user.firstName)
                            }
                        },
                        onClick = {

                            val newList =
                                if (isSelected)
                                    selectedUsers.filter { it.id != option.user.id }
                                else
                                    selectedUsers + option.user

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
fun AppSelectUserRadioBtnFieldProject(
    label: String,
    options: List<UsersOfCompanyItem>,
    selectedOption: UserCompany,
    onSelectionChange: (UserCompany) -> Unit
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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = selectedOption.firstName,
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
                                    selected = option.user == selectedOption,
                                    onClick = null
                                )

                                Spacer(Modifier.width(5.dp))

                                Text(option.user.firstName)
                            }
                        },
                        onClick = {
                            onSelectionChange(option.user)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DateRangeField(

    startDate: String,
    endDate: String,
    onRangeSelected: (String, String) -> Unit

) {

    var showModal by remember { mutableStateOf(false) }

    val formatter = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

    val text = if (startDate.isNotEmpty() && endDate.isNotEmpty())
        "$startDate - $endDate"
    else ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(
            text = "Périodes",
            style = MaterialTheme.typography.labelMedium,
            color = StatusInProgress,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        TextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(
                    onClick = { showModal = true }
                ) {
                    Icon(Icons.Default.DateRange, null, tint = Color.White)
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
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

    if (showModal) {

        DateRangePickerModal(

            onDateRangeSelected = { startMillis, endMillis ->

                val start =
                    formatter.format(Date(startMillis))

                val end =
                    formatter.format(Date(endMillis))

                onRangeSelected(start, end)
            },

            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(

    initialStartDate: Long? = null,
    initialEndDate: Long? = null,

    onDateRangeSelected: (Long, Long) -> Unit,
    onDismiss: () -> Unit

) {

    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartDate,
        initialSelectedEndDateMillis = initialEndDate,

        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis() - 86400000
            }
        }
    )

    val start = dateRangePickerState.selectedStartDateMillis
    val end = dateRangePickerState.selectedEndDateMillis

    DatePickerDialog(
        onDismissRequest = onDismiss,

        confirmButton = {

            TextButton(
                enabled = start != null && end != null,
                onClick = {
                    onDateRangeSelected(start!!, end!!)
                    onDismiss()
                }
            ) {
                Text(stringResource(id = R.string.btn_txt_validate))
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    ) {

        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(text = stringResource(R.string.select_range_date))
            },
            headline = {},
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 480.dp)
                .padding(16.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectPriorityRadioBtnField(
    label: String,
    options: ArrayList<ProjectAndTakPriorities>,
    selectedOption: ProjectAndTakPriorities,
    onSelectionChange: (ProjectAndTakPriorities) -> Unit
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
fun AppSelectStatusRadioBtnField(
    label: String,
    options: ArrayList<ProjectStatus>,
    selectedOption: ProjectStatus,
    onSelectionChange: (ProjectStatus) -> Unit
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
                        }
                    )
                }
            }
        }
    }
}