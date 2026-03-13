package com.example.btppilot.presentation.screens.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.data.repository.TaskRepository
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
   private  val taskRepository: TaskRepository
): ViewModel() {


    data class TaskState(
        val tasks: List<TasksByProjectDtoItem> = listOf(),
        val isLoading: Boolean = false
    )

    private val _taskListStateFlow = MutableStateFlow(TaskState())
    val taskListStateFlow = _taskListStateFlow.asStateFlow()

   init {
       fetchTasks()
   }


    private fun fetchTasks() {

        viewModelScope.launch {
            _taskListStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                taskRepository.getTasksOfCompany()
            }
            when (result) {

                is Resource.Success -> {
                    _taskListStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _taskListStateFlow.update {
                        it.copy(
                            tasks = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {

                    _taskListStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                }
            }
        }
    }

}