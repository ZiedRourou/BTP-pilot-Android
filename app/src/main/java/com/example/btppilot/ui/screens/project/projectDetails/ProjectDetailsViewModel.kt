package com.example.btppilot.ui.screens.project.projectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.R
import com.example.btppilot.data.dto.request.task.UpdateTaskDto
import com.example.btppilot.data.dto.response.user.User
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.data.repository.TaskRepository
import com.example.btppilot.ui.navigation.Screen
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
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
class ProjectDetailsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository

) : ViewModel() {

    data class DetailsProjectState(
        val projectId: Long = 0,
        val isLoading: Boolean = false,
        val project: ProjectByIdResponseDto =
            ProjectByIdResponseDto(
                id = 0,
                name = "",
                description = "",
                status = ProjectStatus.PLANNED.name,
                priority = ProjectAndTakPriorities.HIGH.name,
                plannedEndDate = "",
                plannedStartDate = "",
                currentEndDate = null,
                companyId = 0,
                userProjects = listOf(
                    UserProject(
                        "test",
                        User(0, "zied", "rourou")
                    ),
                    UserProject(
                        "test",
                        User(0, "zied", "rourou")
                    ),
                    UserProject(
                        "test",
                        User(0, "zied", "rourou")
                    )
                )

            ),
        val selectPriority: ProjectAndTakPriorities = ProjectAndTakPriorities.MEDIUM,
        val tasks: List<TasksByProjectDtoItem> = listOf(),
        val showDialog: Boolean = false
    )

    private val _detailProjectStateFlow = MutableStateFlow(DetailsProjectState())
    val detailProjectStateFlow = _detailProjectStateFlow.asStateFlow()

    private val _detailProjectEventSharedFlow = MutableSharedFlow<EventState>()
    val detailProjectEventSharedFlow = _detailProjectEventSharedFlow.asSharedFlow()


    fun setProjectId(projectId: Long) {
        _detailProjectStateFlow.update {
            it.copy(
                projectId = projectId
            )
        }
        fetchProject()
        fetchTasks()
    }

    fun confirmDelete() {
        _detailProjectStateFlow.update {
            it.copy(
                showDialog = true
            )
        }
    }

    fun closeDialogDelete() {
        _detailProjectStateFlow.update {
            it.copy(
                showDialog = false
            )
        }
    }

    fun deleteProject() {
        _detailProjectStateFlow.update {
            it.copy(
                showDialog = false,
            )
        }


        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                projectRepository.deleteProject(detailProjectStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {

                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(R.string.project_delete),
                    )
                    _detailProjectEventSharedFlow.emit(
                        EventState.RedirectScreen(Screen.Home)
                    )
                }

                is Resource.Error -> {
                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    fun onEditProject() {
        viewModelScope.launch {
            _detailProjectEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.UpdateProject.route + "/${detailProjectStateFlow.value.projectId}")
            )
        }
    }

    fun redirectAddTask() {
        viewModelScope.launch {
            _detailProjectEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.NewTask.route + "/${detailProjectStateFlow.value.projectId}/0")
            )
        }
    }

    fun redirectEditTask(taskId: Int) {
        viewModelScope.launch {
            _detailProjectEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.NewTask.route + "/${0}/${taskId.toLong()}")
            )
        }
    }


    private fun fetchProject() {
        viewModelScope.launch {

            val result = withContext(Dispatchers.IO) {
                projectRepository.fetchProjectById(detailProjectStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {

                    result.data?.let { project ->
                        _detailProjectStateFlow.update {
                            it.copy(
                                project = project,
                                selectPriority = ProjectAndTakPriorities.valueOf(project.priority)
                            )
                        }
                    }
                }

                is Resource.Error -> {

                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    fun updateTask(status: TaskStatus, task: TasksByProjectDtoItem) {
        viewModelScope.launch {
            _detailProjectStateFlow.update {
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
                    _detailProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                fetchTasks()
                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(R.string.status_updated)
                    )
                }

                is Resource.Error -> {

                    _detailProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    fun deleteTask(taskId: Int){
        viewModelScope.launch {
            _detailProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                taskRepository.deleteTask(taskId)
            }

            when (result) {

                is Resource.Success -> {
                    _detailProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    fetchTasks()
                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(R.string.task_deleted)
                    )
                }

                is Resource.Error -> {

                    _detailProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }


     fun fetchTasks() {

        viewModelScope.launch {

            val result = withContext(Dispatchers.IO) {
                taskRepository.getTasksOfProject(detailProjectStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {

                    _detailProjectStateFlow.update {
                        it.copy(
                            tasks = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {

                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }
}