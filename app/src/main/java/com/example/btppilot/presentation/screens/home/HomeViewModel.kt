package com.example.btppilot.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.Resource
import com.example.btppilot.util.formatDateFr
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

        val activeProjectStat :Int = 0,
        val taskStatusTodoOrInProgressStat : Int = 0,
        val projectFilteredList : List<ProjectResponseByUserCompanyDtoItem> = listOf(),

        val isLoading: Boolean = false,
        val currentUserName : String = "",
        val currentDate: String = Date().formatDateFr(),

        val projectStatus: List<ProjectStatus> = listOf(
            ProjectStatus.ALL,
            ProjectStatus.PLANNED,
            ProjectStatus.IN_PROGRESS,
            ProjectStatus.COMPLETED,
        ),
        val selectedFilter : ProjectStatus = ProjectStatus.ALL
    )

    private val _projectListCopyStateFlow = MutableStateFlow(listOf <ProjectResponseByUserCompanyDtoItem>())

    private val _userInfoStateFlow = MutableStateFlow(authSharedPref.getUserName())
    val userInfoStateFlow = _userInfoStateFlow.asStateFlow()

    private val _homeStateFlow = MutableStateFlow(HomeState())
    val homeStateFlow = _homeStateFlow.asStateFlow()

    private val _homeEventSharedFlow = MutableSharedFlow<EventState>()
    val homeEventSharedFlow = _homeEventSharedFlow.asSharedFlow()


    init {
        fetchProjectUser()
        getCurrentUserInfo()
    }

    fun filterProject(status : ProjectStatus){
        _homeStateFlow.update {
            it.copy(
                selectedFilter = status,
                projectFilteredList =
                _projectListCopyStateFlow.value.filter {
                    if(status != ProjectStatus.ALL )
                        it.status == status.label
                    else true
                }
            )
        }
    }
    fun onClickProject(projectId : Int) {
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                EventState.RedirectScreenWithId(Screen.ProjectDetail.route + "/$projectId")
            )
        }
    }


    fun redirectAddProject(){
        viewModelScope.launch {
            _homeEventSharedFlow.emit(
                EventState.RedirectScreenWithId(
                    Screen.NewProject.route + "/0"
                )
            )
        }
    }

    private fun getCurrentUserInfo(){
        _homeStateFlow.update {
            it.copy(
                currentUserName =  authSharedPref.getUserName() ?: ""
            )
        }
    }

    private fun fetchProjectUser() {

        viewModelScope.launch {
            _homeStateFlow.value =
                _homeStateFlow.value.copy(isLoading = true)

            val result = withContext(Dispatchers.IO) {
                projectRepository.fetchProjectsByUser(
                    authSharedPref.getCompanyId()
                )
            }
            when (result) {

                is Resource.Success -> {
                    _homeStateFlow.value =
                        _homeStateFlow.value.copy(isLoading = false)

                    result.data?.let { projectData ->
                        _homeStateFlow.update {
                            it.copy(
                                activeProjectStat = projectData.projects.size,
                                taskStatusTodoOrInProgressStat = projectData.taskInProgress,
                                projectFilteredList = projectData.projects
                            )
                        }
                        _projectListCopyStateFlow.value= projectData.projects
                    }
                }

                is Resource.Error -> {

                    _homeStateFlow.value =
                        _homeStateFlow.value.copy(isLoading = false)

                    _homeEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }

    }
}