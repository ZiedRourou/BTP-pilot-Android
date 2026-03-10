package com.example.btppilot.presentation.screens.project.component

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.presentation.screens.component.AppLabelTitle
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import java.util.Calendar


@Composable
fun ProjectAndTaskEditorTopBar(
    title: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
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

//            TextButton(
//                onClick = onCancel,
//            ) {
//                AppLabelTitle(text = "Annuler", color = StatusInProgress)
//            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )

//            TextButton(
//                onClick = onSave,
//
//                ) {
//                AppLabelTitle(text = "Enregister", color = StatusInProgress)
//            }

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
    supportingText: String? = null,
    isError: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 3
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
                Text("e.g. Foundation Reinforcement")
            },
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

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
        AppLabelTitle(text = title)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectUserRadioBtnField(
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
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            TextField(
                value = "${selectedOption.firstName} ${selectedOption.lastName} ",
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
fun AppDateField(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    isError: Boolean = false,
    supportingText: String? = null
) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dialog = DatePickerDialog(
        context,
        { _, y, m, d ->
            onDateSelected("%02d/%02d/%04d".format(m + 1, d, y))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = System.currentTimeMillis()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = StatusInProgress
        )

        TextField(
            value = selectedDate,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { dialog.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
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
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,

                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
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

                                Text(option.label)
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