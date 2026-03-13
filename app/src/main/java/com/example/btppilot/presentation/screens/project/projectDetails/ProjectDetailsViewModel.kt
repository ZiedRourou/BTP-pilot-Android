package com.example.btppilot.presentation.screens.project.projectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.response.User
import com.example.btppilot.data.dto.response.UserProject
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.data.repository.TaskRepository
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
        val selectPriority: ProjectAndTakPriorities= ProjectAndTakPriorities.MEDIUM,
        val tasks: List<TasksByProjectDtoItem> = listOf(),
        val showDialog : Boolean = false
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

    fun onSelectPriority(priority : ProjectAndTakPriorities){
        _detailProjectStateFlow.update {
            it.copy(
                selectPriority = priority
            )
        }
    }

    fun confirmDelete(){
        _detailProjectStateFlow.update {
            it.copy(
                showDialog = true
            )
        }
    }
    fun closeDialogDelete(){
        _detailProjectStateFlow.update {
            it.copy(
                showDialog = false
            )
        }
    }

    fun deleteProject() {
        _detailProjectStateFlow.update {
            it.copy(
                showDialog = false
            )
        }

        viewModelScope.launch {
            _detailProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.deleteProject(detailProjectStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {
                    _detailProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _detailProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar("article supprimé")
                    )
                    delay(2000)
                    _detailProjectEventSharedFlow.emit(
                        EventState.RedirectScreen(Screen.Home)
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

    fun onEditProject() {
        viewModelScope.launch {
            _detailProjectEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.UpdateProject.route + "/${detailProjectStateFlow.value.projectId}")
            )
        }
    }

    fun redirectAddTask(){

        viewModelScope.launch {
            _detailProjectEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.NewTask.route + "/${detailProjectStateFlow.value.projectId}")
            )
        }
    }


    private fun fetchProject() {
        viewModelScope.launch {
            _detailProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.fetchProjectById(detailProjectStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {
                    _detailProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

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

    private fun fetchTasks() {

        viewModelScope.launch {
            _detailProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                taskRepository.getTasksOfProject(detailProjectStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {
                    _detailProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _detailProjectStateFlow.update {
                        it.copy(
                            tasks = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
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
}