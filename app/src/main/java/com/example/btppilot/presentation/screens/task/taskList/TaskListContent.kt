package com.example.btppilot.presentation.screens.task.taskList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.presentation.screens.home.component.FilterButton
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.shared.component.HeaderMainSreen
import com.example.btppilot.presentation.screens.task.component.ItemTask
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.TaskStatus


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NewTaskListScreenPreview() {

    val snackbarHostState = remember { SnackbarHostState() }

    BtpPilotTheme {


        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { HeaderMainSreen(userName = "userInfo") },

            ) { padding ->

            TaskListContent(
                paddingValues = padding,
                tasklist = TaskListViewModel.TaskState(),
                isEnableToEditTask =true ,
                redirectEditTask = {},
                changeStatus = {taskStatus, tasksByProjectDtoItem ->  },
                deleteTask ={},
                filterTask = {}
            )
        }
    }
}


@Composable
fun TaskListContent(
    paddingValues: PaddingValues,
    tasklist: TaskListViewModel.TaskState,
    isEnableToEditTask: Boolean,
    redirectEditTask : (Int) -> Unit,
    changeStatus: (TaskStatus, TasksByProjectDtoItem) -> Unit,
    deleteTask : (Int)-> Unit,
    filterTask : (TaskStatus) -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        FilterStatusTaskList(
            optionList = tasklist.taskStatus,
            selected = tasklist.selectedFilter,
            onSelectedChange =filterTask
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(tasklist.tasksFiltered) { item ->
                ItemTask(
                    taskState = item,
                    canEdit = isEnableToEditTask,
                    redirectEditTask = redirectEditTask ,
                    changeStatus = changeStatus ,
                    deleteTask = deleteTask
                )
            }

        }

    }
}



@Composable
fun FilterStatusTaskList(
    optionList: List<TaskStatus>,
    selected: TaskStatus,
    onSelectedChange: (TaskStatus) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {

        Text(
            text = "Mes Taches",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {

            items(optionList) { status ->

                FilterButton(
                    text = status.label,
                    isSelected = status == selected,
                    onClick = { onSelectedChange(status) }
                )
            }
        }
    }
}