package com.example.btppilot.ui.screens2.task.taskList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.task.UpdateTaskDto
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.data.repository.TaskRepository
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.shared.uiState.EventState
import com.example.btppilot.util.Resource
import com.example.btppilot.util.TaskStatus
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
    private val taskRepository: TaskRepository
) : ViewModel() {


    data class TaskState(
        val tasksFiltered: List<TasksByProjectDtoItem> = listOf(),
        val isLoading: Boolean = false,

        val taskStatus: List<TaskStatus> = listOf(
            TaskStatus.ALL,
            TaskStatus.TO_DO,
            TaskStatus.DONE,
            TaskStatus.IN_PROGRESS,
        ),
        val selectedFilter: TaskStatus = TaskStatus.ALL,
    )

    private val _taskListStateFlow = MutableStateFlow(TaskState())
    val taskListStateFlow = _taskListStateFlow.asStateFlow()
    private val _taskListCopyStateFlow =
        MutableStateFlow(listOf<TasksByProjectDtoItem>())
    private val _taskListEventSharedFlow = MutableSharedFlow<EventState>()
    val taskEventState = _taskListEventSharedFlow.asSharedFlow()

    init {
        fetchTasks()
    }

    fun redirectAddTask() {
        viewModelScope.launch {
            _taskListEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.NewTask.route + "/0/0")
            )
        }
    }

    fun filterProject(status: TaskStatus) {
        _taskListStateFlow.update {
            it.copy(
                selectedFilter = status,
                tasksFiltered =
                _taskListCopyStateFlow.value.filter {
                    if (status != TaskStatus.ALL)
                        it.status == status.name
                    else true
                }
            )
        }
    }

    fun updateTask(status: TaskStatus, task: TasksByProjectDtoItem) {
        viewModelScope.launch {
            _taskListStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                taskRepository.updateTask(
                    task.id,
                    taskRequestDto = UpdateTaskDto(
                        status = status.name
                    )
                )
            }

            when (result) {

                is Resource.Success -> {
                    _taskListStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    fetchTasks()
                    _taskListEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar("Statut mis a jour ")
                    )
                }

                is Resource.Error -> {

                    _taskListStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _taskListEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    fun redirectEditTask(taskId: Int) {
        viewModelScope.launch {
            _taskListEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.NewTask.route + "/${0}/${taskId.toLong()}")
            )
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            _taskListStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                taskRepository.deleteTask(taskId)
            }

            when (result) {

                is Resource.Success -> {
                    _taskListStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    fetchTasks()
                    _taskListEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar("Tache supprimé ")
                    )
                }

                is Resource.Error -> {

                    _taskListStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _taskListEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
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
                            tasksFiltered = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    _taskListCopyStateFlow.value = result.data ?: emptyList()
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