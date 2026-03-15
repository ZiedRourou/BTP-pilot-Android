package com.example.btppilot.ui.screens2.task.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.presentation.screens.shared.component.AppTitleDescription
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusDelayed
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.TaskStatus
import com.example.btppilot.util.arrayPriorities
import com.example.btppilot.util.arrayTaskStatus
import com.example.btppilot.util.toShortDate


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NewTaskScreenPreview() {

    BtpPilotTheme {
        ItemTask(
            taskState = TasksByProjectDtoItem(
                id = 0,
                title = "test",
                description = "test2",
                doneEndDate = "05/072019",
                estimationHours = 2,
                plannedEndDate = "hdhdhd",
                status = "TO_DO",
                priority = "MEDIUM",
                plannedStartDate = "bbbbb",
                projectId = 8,
                createdAt = "jdd",
                userId = 5,
                assignments = listOf(),
                createdById = 4,
                isClientTicket = false
            ),
            true,
            redirectEditTask = {},
            changeStatus = { TaskStatus, TaskByPro -> Unit },

            deleteTask = {}
        )
    }
}

@Composable
fun ItemTask(
    taskState: TasksByProjectDtoItem,
    canEdit: Boolean,
    redirectEditTask: (Int) -> Unit,
    changeStatus: (TaskStatus, TasksByProjectDtoItem) -> Unit,
    deleteTask: (Int) -> Unit
) {

    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TaskStatusEditDropdown(
                            itemTask = taskState,
                            selected = TaskStatus.valueOf(taskState.status),
                            onSelect = changeStatus,
                            enableEdit = canEdit
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        TaskPriorityDropdown(
                            selected = ProjectAndTakPriorities.valueOf(taskState.priority),
                        )
                    }
                }
                if (canEdit) {

                    Column {
                        Row(
                            modifier = Modifier.height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { redirectEditTask(taskState.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    tint = StatusInProgress,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            IconButton(onClick = { deleteTask(taskState.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    tint = StatusDelayed,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                }

            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = taskState.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = taskState.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Text(
                    text = "TEMPS ESTIMÉ : ",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = " ${taskState.estimationHours} H")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Event,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))

                AppTitleDescription(text = taskState.plannedStartDate.toShortDate())
                Spacer(modifier = Modifier.width(10.dp))
                Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))

                AppTitleDescription(text = taskState.plannedEndDate.toShortDate())
            }
            Spacer(modifier = Modifier.width(30.dp))
            
        }
    }
}


@Composable
fun TaskStatusEditDropdown(
    itemTask: TasksByProjectDtoItem,
    enableEdit: Boolean = false,
    selected: TaskStatus,
    onSelect: (TaskStatus, TasksByProjectDtoItem) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Surface(
        onClick = { if (enableEdit) expanded = true },
        shape = RoundedCornerShape(50),
        color = selected.color.copy(alpha = 0.18f),
        modifier = Modifier
            .wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = selected.label,
                style = MaterialTheme.typography.labelMedium,
                color = selected.color,
            )

            Spacer(Modifier.width(4.dp))

            if (enableEdit)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = selected.color,
                    modifier = Modifier.size(18.dp)
                )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
    ) {

        arrayTaskStatus.filter { it.label != selected.label }.forEach { priority ->

            DropdownMenuItem(
                text = {
                    Text(
                        priority.label,
                        color = priority.color,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                onClick = {
                    onSelect(priority, itemTask)
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun TaskPriorityDropdown(
    selected: ProjectAndTakPriorities,
) {

    Surface(
        shape = RoundedCornerShape(50),
        color = selected.color.copy(alpha = 0.18f),
        modifier = Modifier.wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = selected.label,
                style = MaterialTheme.typography.labelMedium,
                color = selected.color,
            )

            Spacer(Modifier.width(4.dp))

        }
    }

}