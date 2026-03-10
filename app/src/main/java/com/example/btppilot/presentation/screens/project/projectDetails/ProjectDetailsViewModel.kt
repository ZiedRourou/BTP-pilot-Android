package com.example.btppilot.presentation.screens.project.projectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.response.User
import com.example.btppilot.data.dto.response.UserProject
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.data.repository.TaskRepository
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
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
class ProjectDetailsViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository

) : ViewModel() {

    data class NewProjectState(
        val projectId: Long = 0,
        val isLoading : Boolean = false,
        val project : ProjectByIdResponseDto =
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
                    User(0,"zied", "rourou")
                ),
                    UserProject(
                        "test",
                        User(0,"zied", "rourou")
                    ),
                    UserProject(
                        "test",
                        User(0,"zied", "rourou")
                    )
                )

            ),
        val tasks : List<TasksByProjectDtoItem> = listOf()
    )

    private val _detailProjectStateFlow = MutableStateFlow(NewProjectState())
    val detailProjectStateFlow = _detailProjectStateFlow.asStateFlow()

    private val _detailProjectEventSharedFlow = MutableSharedFlow<EventState>()
    val detailProjectEventSharedFlow = _detailProjectEventSharedFlow.asSharedFlow()


    fun setProjectId(projectId: Long){
        _detailProjectStateFlow.update {
            it.copy(
                projectId = projectId
            )
        }
        fetchProject()
        fetchTasks()
    }

    private fun fetchProject(){
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

    private fun fetchTasks(){

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