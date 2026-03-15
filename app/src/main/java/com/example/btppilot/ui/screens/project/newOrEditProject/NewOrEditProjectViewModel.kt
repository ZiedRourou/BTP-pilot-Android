package com.example.btppilot.ui.screens.project.newOrEditProject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.project.NewProjectRequestDto
import com.example.btppilot.data.dto.request.project.UpdateProjectRequestDto
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.repository.CompanyRepository
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.ui.screens.shared.uiState.EventState
import com.example.btppilot.data.local.AuthSharedPref
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.Resource
import com.example.btppilot.util.UserProjectRole
import com.example.btppilot.util.UserRole
import com.example.btppilot.util.isoToUiDate
import com.example.btppilot.util.toIsoDate
import com.example.btppilot.util.uiDateToIso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NewOrEditProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val authSharedPref: AuthSharedPref,
    private val companyRepository: CompanyRepository
) : ViewModel() {

    data class NewProjectState(

        val title: String = "",
        val description: String = "",
        val userOption: List<UsersOfCompanyItem> = listOf(),
        val clientOption: List<UsersOfCompanyItem> = listOf(),

        val manager: UserCompany = UserCompany(0, "", "", ""),
        val clientList: List<UserCompany> = listOf(),
        val collaboratorList: List<UserCompany> = listOf(),

        val dateBegin: String = "%02d/%02d/%04d".format(
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        ),
        val dateEnd: String = "%02d/%02d/%04d".format(
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        ),

        val projectPriorities: ArrayList<ProjectAndTakPriorities> = arrayListOf(
            ProjectAndTakPriorities.LOW,
            ProjectAndTakPriorities.MEDIUM,
            ProjectAndTakPriorities.HIGH
        ),
        val projectStatus: ArrayList<ProjectStatus> = arrayListOf(
            ProjectStatus.COMPLETED,
            ProjectStatus.IN_PROGRESS,
            ProjectStatus.FINISH,
            ProjectStatus.PLANNED,
        ),

        val selectedPriority: ProjectAndTakPriorities = ProjectAndTakPriorities.LOW,
        val selectedStatus: ProjectStatus = ProjectStatus.PLANNED,

        val titleError: String? = null,
        val descriptionError: String? = null,
        val dateEndError: String? = null,

        val projectId: Long = 0,
        val isLoading: Boolean = false,
        val isEditorMode: Boolean = false
    )

    private val _newProjectStateFlow = MutableStateFlow(NewProjectState())
    val newProjectStateFlow = _newProjectStateFlow.asStateFlow()

    private val _newProjectEventSharedFlow = MutableSharedFlow<EventState>()
    val newProjectEventSharedFlow = _newProjectEventSharedFlow.asSharedFlow()


    init {
        getUserCompany()
    }

    fun setProjectId(projectId: Long) {
        _newProjectStateFlow.update {
            it.copy(
                projectId = projectId,
                isEditorMode = true
            )
        }
        fetchProject()
    }

    fun onTitleChange(title: String) {
        _newProjectStateFlow.update {
            it.copy(
                title = title
            )
        }

    }

    fun onDescriptionChange(description: String) {
        _newProjectStateFlow.update {
            it.copy(
                description = description
            )
        }
    }

    fun onManagerChange(user: UserCompany) {
        _newProjectStateFlow.update {
            it.copy(
                manager = user
            )
        }
    }

    fun onBeginDateChange(beginDate: String) {
        _newProjectStateFlow.update {
            it.copy(
                dateBegin = beginDate
            )
        }
    }

    fun onEndDateChange(endDate: String) {
        _newProjectStateFlow.update {
            it.copy(
                dateEnd = endDate,
            )
        }
    }

    fun onPriorityChange(priority: ProjectAndTakPriorities) {
        _newProjectStateFlow.update {
            it.copy(
                selectedPriority = priority,
            )
        }
    }

    fun onStatusChange(status: ProjectStatus) {
        _newProjectStateFlow.update {
            it.copy(
                selectedStatus = status,
            )
        }
    }

    fun onClientListChange(list: List<UserCompany>) {
        _newProjectStateFlow.update {
            it.copy(
                clientList = list            )
        }
    }

    fun onEmployeeListChange(list: List<UserCompany>) {
        _newProjectStateFlow.update {
            it.copy(
                collaboratorList = list
            )
        }
    }

    private fun fetchProject() {
        viewModelScope.launch {
            _newProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.fetchProjectById(newProjectStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {
                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    result.data?.let { project ->

                        val managerUser = project.userProjects
                            .firstOrNull { it.projectRole == UserProjectRole.MANAGER.name }

                        val clientUsers = project.userProjects
                            .filter { it.projectRole == UserProjectRole.CLIENT.name }

                        val employeeUsers = project.userProjects
                            .filter { it.projectRole == UserProjectRole.EMPLOYEE.name }

                        _newProjectStateFlow.update { state ->

                            state.copy(
                                title = project.name,
                                description = project.description,
                                dateBegin = isoToUiDate(project.plannedStartDate),
                                dateEnd = isoToUiDate(project.plannedEndDate),

                                selectedPriority = ProjectAndTakPriorities.valueOf(project.priority),
                                selectedStatus = ProjectStatus.valueOf(project.status),

                                manager = managerUser?.let {
                                    UserCompany(
                                        id = it.user.id,
                                        email = "",
                                        firstName = it.user.firstName,
                                        lastName = it.user.lastName
                                    )
                                } ?: state.manager,

                                clientList = clientUsers.map {
                                    UserCompany(
                                        id = it.user.id,
                                        email = "",
                                        firstName = it.user.firstName,
                                        lastName = it.user.lastName
                                    )
                                },

                                collaboratorList = employeeUsers.map {
                                    UserCompany(
                                        id = it.user.id,
                                        email = "",
                                        firstName = it.user.firstName,
                                        lastName = it.user.lastName
                                    )
                                }
                            )
                        }
                    }
                }

                is Resource.Error -> {

                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }


    private fun getUserCompany() {

        viewModelScope.launch {
            _newProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                companyRepository.getUsersOfCompany()
            }
            when (result) {

                is Resource.Success -> {
                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    result.data?.let { users ->
                        _newProjectStateFlow.update { it ->
                            it.copy(
                                userOption = users.filter { it.role != UserRole.CLIENT.name },
                                clientOption = users.filter { it.role == UserRole.CLIENT.name },
                                manager = users.first().user
                            )
                        }
                    }
                }

                is Resource.Error -> {

                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    fun publishOrEdit() {

        if (!validateDataProject())
            return
        if (newProjectStateFlow.value.isEditorMode)
            updateProject()
        else
            publishProject()
    }

    private fun updateProject() {

        viewModelScope.launch {
            _newProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.updateProject(
                    projectId = newProjectStateFlow.value.projectId.toInt(),
                    newProjectStateFlow.value.let {
                        UpdateProjectRequestDto(
                            name = it.title,
                            description = it.description,
                            status = it.selectedStatus.name,
                            priority = it.selectedPriority.name,
                            plannedStartDate = uiDateToIso(it.dateBegin),
                            plannedEndDate = uiDateToIso(it.dateEnd),
                            managerId = it.manager.id,
                            isActive = true,
                            clientIds = it.clientList.map { user -> user.id },
                            employeeIds = it.collaboratorList.map { user -> user.id },
                        )
                    }
                )
            }
            when (result) {

                is Resource.Success -> {
                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newProjectEventSharedFlow.emit(
                        EventState.PopBackStackWithRefresh(Unit)
                    )
                }

                is Resource.Error -> {

                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    private fun publishProject() {

        viewModelScope.launch {
            _newProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.newProject(
                    newProjectStateFlow.value.let {
                        NewProjectRequestDto(
                            name = it.title,
                            description = it.description,
                            status = it.selectedStatus.name,
                            priority = it.selectedPriority.name,
                            plannedStartDate = toIsoDate(it.dateBegin),
                            plannedEndDate = toIsoDate(it.dateEnd),
                            managerId = it.manager.id,
                            isActive = true,
                            clientIds = it.clientList.map { user -> user.id },
                            employeeIds = it.collaboratorList.map { user -> user.id }
                        )
                    }

                )
            }
            when (result) {

                is Resource.Success -> {
                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newProjectEventSharedFlow.emit(
                        EventState.PopBackStackWithRefresh(Unit)
                    )
                }

                is Resource.Error -> {

                    _newProjectStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newProjectEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }

    }

    private fun validateDataProject(): Boolean {

        val currentData = newProjectStateFlow.value
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val start = format.parse(currentData.dateBegin)
        val end = format.parse(currentData.dateEnd)


        _newProjectStateFlow.update {
            it.copy(
                titleError = when {
                    currentData.title.trim().isEmpty() ->
                        "Titre du projet requis"

                    currentData.title.length < 2 ->
                        "Le titre doit contenir au moins 2 caractères"

                    else -> null
                },
                descriptionError = when {
                    currentData.description.trim().isEmpty() -> "Description du projet requis"
                    currentData.description.length < 2 -> "minimum 2 caractères"
                    else -> null
                },

                dateEndError = when {
                    start == null || end == null -> "Format de date invalide"

                    start.after(end) -> "Veuillez sélectionner une période postérieure"

                    else -> null
                }
            )
        }

        newProjectStateFlow.value.let {
            if (it.titleError.isNullOrEmpty()
                && it.dateEndError.isNullOrEmpty()
                && it.descriptionError.isNullOrEmpty()
            )
                return true
        }
        return false
    }
}