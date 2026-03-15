package com.example.btppilot.ui.screens2.task.newTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.task.TaskRequestDto
import com.example.btppilot.data.dto.request.task.UpdateTaskDto
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.user.User
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.data.repository.TaskRepository
import com.example.btppilot.presentation.screens.shared.uiState.EventState
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.Resource
import com.example.btppilot.util.TaskStatus
import com.example.btppilot.util.isoToUiDate
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
class NewTaskViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    data class NewTaskState(
        val title: String = "",
        val description: String = "",
        val userOption: List<UsersOfCompanyItem> = listOf(),
        val estimateHours: String = "",

        val project: List<ProjectResponseByUserCompanyDtoItem>? = null,

        val selectedProject: ProjectResponseByUserCompanyDtoItem? = null,
        val dateBegin: String = "%02d/%02d/%04d".format(
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        ),
        val selectUser: List<UserProject> = listOf(),
        val dateEnd: String = "%02d/%02d/%04d".format(
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            Calendar.getInstance().get(Calendar.YEAR)
        ),

        val taskPriorities: ArrayList<ProjectAndTakPriorities> = arrayListOf(
            ProjectAndTakPriorities.LOW,
            ProjectAndTakPriorities.MEDIUM,
            ProjectAndTakPriorities.HIGH
        ),
        val taskStatus: ArrayList<TaskStatus> = arrayListOf(
            TaskStatus.TO_DO,
            TaskStatus.DONE,
            TaskStatus.IN_PROGRESS
        ),

        val taskId: Long = 0,
        val isEditorMode: Boolean = false,
        val selectedPriority: ProjectAndTakPriorities = ProjectAndTakPriorities.LOW,
        val selectedStatus: TaskStatus = TaskStatus.TO_DO,

        val titleError: String? = null,
        val descriptionError: String? = null,
        val dateEndError: String? = null,
        val estimateHoursError: String? = null,
        val selectedUserError: String? = null,

        val isLoading: Boolean = false,
        val projectUpdate : Int =0
    )

    private val _newTaskStateFlow = MutableStateFlow(NewTaskState())
    val newTaskStateFlow = _newTaskStateFlow.asStateFlow()

    private val _newTaskEventSharedFlow = MutableSharedFlow<EventState>()
    val newTaskEventSharedFlow = _newTaskEventSharedFlow.asSharedFlow()


    fun setProjectId(projectId: Long) {
        if(!newTaskStateFlow.value.isEditorMode){
            _newTaskStateFlow.update {
                it.copy(
                    selectedProject = newTaskStateFlow.value.project?.find { it.id == projectId.toInt() }
                )
            }
            fetchProjectUser()
        }
    }

    fun setTaskId(taskId: Long) {
        _newTaskStateFlow.update {
            it.copy(
                taskId = taskId,
                isEditorMode = true
            )
        }
        fetchTaskDetails()
    }


    fun onTitleChange(title: String) {
        _newTaskStateFlow.update {
            it.copy(title = title)
        }
    }

    fun onDescriptionChange(description: String) {
        _newTaskStateFlow.update {
            it.copy(description = description)
        }
    }

    fun onHoursChange(hour: String) {

        if (!hour.matches(Regex("^\\d*\$"))) return

        _newTaskStateFlow.update {
            it.copy(
                estimateHours = hour,
                estimateHoursError =
                if (hour.isNotEmpty() && hour.toIntOrNull() == null)
                    "Nombre entier requis"
                else null
            )
        }
    }


    fun onBeginDateChange(beginDate: String) {
        _newTaskStateFlow.update {
            it.copy(
                dateBegin = beginDate
            )
        }
    }

    fun onEndDateChange(endDate: String) {
        _newTaskStateFlow.update {
            it.copy(
                dateEnd = endDate
            )
        }
    }

    fun onPriorityChange(priority: ProjectAndTakPriorities) {
        _newTaskStateFlow.update {
            it.copy(
                selectedPriority = priority
            )
        }
    }

    fun onStatusChange(status: TaskStatus) {
        _newTaskStateFlow.update {
            it.copy(
                selectedStatus = status
            )
        }
    }

    fun onClientListChange(list: List<UserProject>) {
        _newTaskStateFlow.update {
            it.copy(
                selectUser = list
            )
        }
    }

    fun onProjectChange(project: ProjectResponseByUserCompanyDtoItem) {
        _newTaskStateFlow.update {
            it.copy(
                selectedProject = project
            )
        }
    }

    private fun publishTask() {
        viewModelScope.launch {
            viewModelScope.launch {
                _newTaskStateFlow.update {
                    it.copy(
                        isLoading = true
                    )
                }
                val hoursDouble = newTaskStateFlow.value.estimateHours
                    .replace(",", ".")
                    .toDouble()
                val currentData = newTaskStateFlow.value

                val result = withContext(Dispatchers.IO) {
                    taskRepository.postNewTask(
                        projectId = currentData.selectedProject!!.id,
                        taskRequestDto = TaskRequestDto(
                            title = currentData.title,
                            description = currentData.description,
                            priority = currentData.selectedPriority.name,
                            status = currentData.selectedStatus.name,
                            plannedStartDate = uiDateToIso(currentData.dateBegin),
                            plannedEndDate = uiDateToIso(currentData.dateEnd),
                            estimationHours = hoursDouble.toInt(),
                            assignedUserIds = currentData.selectUser.map { user -> user.user.id },
                            doneEndDate = uiDateToIso(currentData.dateEnd),
                        )
                    )
                }

                when (result) {

                    is Resource.Success -> {
                        _newTaskStateFlow.update {
                            it.copy(
                                isLoading = false
                            )
                        }

                        _newTaskEventSharedFlow.emit(
                            EventState.PopBackStackWithRefresh(Unit)
                        )
                    }

                    is Resource.Error -> {

                        _newTaskStateFlow.update {
                            it.copy(
                                isLoading = false
                            )
                        }

                        _newTaskEventSharedFlow.emit(
                            EventState.ShowMessageSnackBar(result.message)
                        )
                    }
                }
            }

        }
    }

    fun publishOrEdit() {
        if (!validateDataTask()) return

        if (newTaskStateFlow.value.isEditorMode)
            updateTask()
        else
            publishTask()

    }

    private fun updateTask() {
        viewModelScope.launch {
            _newTaskStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            val hoursDouble = newTaskStateFlow.value.estimateHours
                .replace(",", ".")
                .toDouble()
            val currentData = newTaskStateFlow.value

            val result = withContext(Dispatchers.IO) {
                taskRepository.updateTask(
                    currentData.taskId.toInt(),
                    taskRequestDto = UpdateTaskDto(
                        title = currentData.title,
                        description = currentData.description,
                        priority = currentData.selectedPriority.name,
                        status = currentData.selectedStatus.name,
                        plannedStartDate = uiDateToIso(currentData.dateBegin),
                        plannedEndDate = uiDateToIso(currentData.dateEnd),
                        estimationHours = hoursDouble.toInt(),
                        assignedUserIds = currentData.selectUser.map { user -> user.user.id },
                        doneEndDate = uiDateToIso(currentData.dateEnd),
                    )
                )
            }

            when (result) {

                is Resource.Success -> {
                    _newTaskStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newTaskEventSharedFlow.emit(
                        EventState.PopBackStackWithRefresh(Unit)
                    )
                }

                is Resource.Error -> {

                    _newTaskStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newTaskEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }


    }

    private fun fetchTaskDetails() {

        viewModelScope.launch {
            _newTaskStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                taskRepository.getTaskById(newTaskStateFlow.value.taskId.toInt())
            }
            when (result) {

                is Resource.Success -> {
                    result.data?.let { task ->
                        _newTaskStateFlow.update {
                            it.copy(
                                isLoading = false,
                                title = task.title,
                                description = task.description,
                                selectedStatus = TaskStatus.valueOf(task.status),
                                selectedPriority = ProjectAndTakPriorities.valueOf(task.priority),
                                dateBegin = isoToUiDate(task.plannedStartDate),
                                dateEnd = isoToUiDate(task.plannedEndDate),
                                estimateHours = task.estimationHours.toString(),
                                selectUser = task.assignments.map {
                                    UserProject(
                                        "",
                                        User(
                                            it.userId,
                                            it.user.firstName,
                                            it.user.lastName
                                        )
                                    )
                                },
                                projectUpdate = task.projectId
                            )
                        }
                    }
                }

                is Resource.Error -> {

                    _newTaskStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                }
            }
        }
    }

    private fun fetchProjectUser() {

        viewModelScope.launch {
            _newTaskStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.fetchProjectsByUser()
            }
            when (result) {

                is Resource.Success -> {
                    _newTaskStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    result.data?.let { projectData ->
                        _newTaskStateFlow.update {
                            it.copy(
                                project = projectData.projects
                            )
                        }
                    }
                }

                is Resource.Error -> {

                    _newTaskStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newTaskEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }

    }

    private fun validateDataTask(): Boolean {

        val currentData = newTaskStateFlow.value
        val format = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val start = format.parse(currentData.dateBegin)
        val end = format.parse(currentData.dateEnd)

        val hoursInt = currentData.estimateHours.toIntOrNull()

        _newTaskStateFlow.update {

            it.copy(
                titleError =
                when {
                    currentData.title.isBlank() -> "Titre requis"
                    currentData.title.length < 2 -> "Minimum 2 caractères"
                    else -> null
                },

                descriptionError =
                when {
                    currentData.description.isBlank() -> "Description requise"
                    currentData.description.length < 2 -> "Minimum 2 caractères"
                    else -> null
                },

                estimateHoursError =
                when {
                    currentData.estimateHours.isEmpty() -> "Estimation requise"
                    hoursInt == null -> "Nombre entier requis"
                    hoursInt <= 0 -> "Doit être > 0"
                    else -> null
                },

                dateEndError = when {
                    start.after(end) -> "corriger les dates "
                    else -> null
                },
                selectedUserError = when {
                    currentData.selectUser.isEmpty() -> "Sélectionner un membre affecté a la tache"
                    else -> null
                }
            )
        }

        newTaskStateFlow.value.let {
            if (it.titleError.isNullOrEmpty()
                && it.descriptionError.isNullOrEmpty()
                && it.estimateHoursError.isNullOrEmpty()
                && it.dateEndError.isNullOrEmpty()
                && it.selectedUserError.isNullOrEmpty()
            )
                return true
        }
        return false
    }

}