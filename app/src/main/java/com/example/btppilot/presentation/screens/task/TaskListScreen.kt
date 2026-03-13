package com.example.btppilot.presentation.screens.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.project.projectDetails.ItemTask


@Composable
fun TaskListScreen(
    navController: NavController,
    taskListViewModel: TaskListViewModel,
    sharedViewModel: SharedViewModel,
    snackbarHostState: SnackbarHostState
){
    val state by taskListViewModel.taskListStateFlow.collectAsState()


    TaskContent(
            tasklist =state
        )
}

@Composable
fun TaskContent(
    tasklist: TaskListViewModel.TaskState
){

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(tasklist.tasks) { item ->
                ItemTask(item)
            }
        }
    }
}