package com.example.btppilot.presentation.screens.test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.presentation.screens.project.projectDetails.ProjectDetailsViewModel
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.arrayPriorities


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun LoginPreview() {
    BtpPilotTheme {
        DropdownPriority(priorities =
        arrayPriorities,
            selectedPriority =ProjectAndTakPriorities.MEDIUM ,
            onSelectPriority ={}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownPriority(
    priorities: List<ProjectAndTakPriorities>,
    selectedPriority: ProjectAndTakPriorities,
    onSelectPriority: (ProjectAndTakPriorities) -> Unit,
    enable : Boolean = false
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        TextField(
            modifier = Modifier
                .menuAnchor()
                .width(160.dp),
            value = selectedPriority.label,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                if (enable)
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            enabled = enable,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = selectedPriority.color.copy(alpha = 0.50f)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            priorities.forEach { priority ->

                DropdownMenuItem(
                    text = {
                        Text(
                            priority.label,
                            color = priority.color
                        )
                           },
                    onClick = {
                        onSelectPriority(priority)
                        expanded = false
                    },
                )
            }
        }
    }
}