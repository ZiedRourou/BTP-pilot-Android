package com.example.btppilot.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.R
import com.example.btppilot.data.dto.request.project.UpdateProjectRequestDto
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.ui.navigation.Screen
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.data.local.AuthSharedPref
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.Resource
import com.example.btppilot.util.formatDateInFrWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref,
    private val projectRepository: ProjectRepository,

    ) : ViewModel() {

    data class HomeState(

        val activeProjectStat: Int = 0,
        val taskTodoWeek :Int = 0,
        val taskLateWeek :Int = 0,
        val taskInProgressWeek :Int = 0,

        val projectFilteredList: List<ProjectResponseByUserCompanyDtoItem> = listOf(),

        val isLoading: Boolean = false,
        val currentUserName: String = "",
        val currentDate: String = Date().formatDateInFrWord(),

        val projectStatus: List<ProjectStatus> = listOf(
            ProjectStatus.ALL,
            ProjectStatus.IN_PROGRESS,
            ProjectStatus.PLANNED,
            ProjectStatus.FINISH
        ),
        val selectedFilter: ProjectStatus = ProjectStatus.ALL,

        )

    private val _projectListCopyStateFlow =
        MutableStateFlow(listOf<ProjectResponseByUserCompanyDtoItem>())

    private val _homeStateFlow = MutableStateFlow(HomeState())
    val homeStateFlow = _homeStateFlow.asStateFlow()

    private val _homeEventSharedFlow = MutableSharedFlow<EventState>(
        extraBufferCapacity = 1
    )
    val homeEventSharedFlow = _homeEventSharedFlow.asSharedFlow()


    init {
        fetchProjectUser()
        getCurrentUserInfo()
    }

    fun changeProjectPriority(
        projectId: Int,
        newStatus: ProjectStatus
    ) {
        updateProject(projectId, newStatus)

    }

    fun filterProject(status: ProjectStatus) {
        _homeStateFlow.update {
            it.copy(
                selectedFilter = status,
                projectFilteredList =
                _projectListCopyStateFlow.value.filter {
                    if (status != ProjectStatus.ALL)
                        it.status == status.name
                    else true
                }
            )
        }
    }

    fun onClickEditProject(
        projectId: Int,
    ) {
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                EventState.RedirectScreenWithId( Screen.NewProject.route + "/${projectId.toLong()}")
            )
        }
    }

    fun onClickViewProject(
        projectId: Int,
    ) {
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                EventState.RedirectScreenWithId( Screen.ProjectDetail.route + "/${projectId.toLong()}")
            )
        }
    }


    fun redirectAddProject() {
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                EventState.RedirectScreenWithId(
                    Screen.NewProject.route + "/0"
                )
            )
        }
    }

    private fun getCurrentUserInfo() {
        _homeStateFlow.update {
            it.copy(
                currentUserName = authSharedPref.getUserName() ?: ""
            )
        }
    }

     fun fetchProjectUser() {

        viewModelScope.launch {

            _homeStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = withContext(Dispatchers.IO) {
                projectRepository.fetchProjectsByUser()
            }
            when (result) {

                is Resource.Success -> {
                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    result.data?.let { projectData ->
                        _homeStateFlow.update {
                            it.copy(
                                activeProjectStat = projectData.projects.filter { it.status != ProjectStatus.FINISH.name }.size,
                                taskInProgressWeek = projectData.weekStats.inProgress,
                                taskLateWeek = projectData.weekStats.late,
                                taskTodoWeek = projectData.weekStats.todo,
                                projectFilteredList = projectData.projects
                            )
                        }
                        _projectListCopyStateFlow.value = projectData.projects
                    }
                }

                is Resource.Error -> {

                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _homeEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }

    }

    private fun updateProject(
        projectId: Int,
        newStatus: ProjectStatus
    ) {

        viewModelScope.launch {
            _homeStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = withContext(Dispatchers.IO) {
                projectRepository.updateProject(
                    projectId = projectId,
                    UpdateProjectRequestDto(
                        status = newStatus.name,
                    )

                )
            }
            when (result) {

                is Resource.Success -> {
                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _homeEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(R.string.status_updated)
                    )
                    fetchProjectUser()
                }

                is Resource.Error -> {

                    _homeStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _homeEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }
}