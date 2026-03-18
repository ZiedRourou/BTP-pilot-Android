package com.example.btppilot.ui.screens.task.newOrEditTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ktx.R
import com.example.btppilot.data.dto.request.task.TaskRequestDto
import com.example.btppilot.data.dto.request.task.UpdateTaskDto
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.user.User
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.data.repository.TaskRepository
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.Resource
import com.example.btppilot.util.TaskStatus
import com.example.btppilot.util.formatStrDateToIsoAPi
import com.example.btppilot.util.formatStrDateToShortDate

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
class NewOrEditTaskViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    data class NewTaskState(
        val title: String = "",
        val description: String = "",
        val userOption: List<UsersOfCompanyItem> = listOf(),
        val estimateHours: Double = 1.0,

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

        val titleError: Int? = null,
        val descriptionError: Int? = null,
        val selectedUserError: Int? = null,
        val projectError: Int? = null,

        val isLoading: Boolean = false,
        val projectUpdate: Int = 0
    )

    private val _newTaskStateFlow = MutableStateFlow(NewTaskState())
    val newTaskStateFlow = _newTaskStateFlow.asStateFlow()

    private val _newTaskEventSharedFlow = MutableSharedFlow<EventState>()
    val newTaskEventSharedFlow = _newTaskEventSharedFlow.asSharedFlow()

    init {
        fetchProjectUser()
    }

    fun setProjectId(projectId: Long) {
        if (!newTaskStateFlow.value.isEditorMode) {
            _newTaskStateFlow.update {
                it.copy(
                    selectedProject = newTaskStateFlow.value.project?.find { it.id == projectId.toInt() }
                )
            }
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


    fun onDurationSelected(hour: Int, minute: Int) {
        val decimalMinutes = hour + (minute / 60.0)
        _newTaskStateFlow.update {
            it.copy(
                estimateHours = decimalMinutes
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
            _newTaskStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val currentData = newTaskStateFlow.value

            val result = withContext(Dispatchers.IO) {
                taskRepository.postNewTask(
                    projectId = currentData.selectedProject?.id ?: 0,
                    taskRequestDto = TaskRequestDto(
                        title = currentData.title,
                        description = currentData.description,
                        priority = currentData.selectedPriority.name,
                        status = currentData.selectedStatus.name,
                        plannedStartDate = currentData.dateBegin.formatStrDateToIsoAPi(),
                        plannedEndDate = currentData.dateEnd.formatStrDateToIsoAPi(),
                        estimationHours = currentData.estimateHours,
                        assignedUserIds = currentData.selectUser.map { user -> user.user.id },
                        doneEndDate = currentData.dateEnd.formatStrDateToIsoAPi(),
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

            val currentData = newTaskStateFlow.value

            val result = withContext(Dispatchers.IO) {
                taskRepository.updateTask(
                    currentData.taskId.toInt(),
                    taskRequestDto = UpdateTaskDto(
                        title = currentData.title,
                        description = currentData.description,
                        priority = currentData.selectedPriority.name,
                        status = currentData.selectedStatus.name,
                        plannedStartDate = currentData.dateBegin.formatStrDateToIsoAPi(),
                        plannedEndDate = currentData.dateEnd.formatStrDateToIsoAPi(),
                        estimationHours = currentData.estimateHours,
                        assignedUserIds = currentData.selectUser.map { user -> user.user.id },
                        doneEndDate = currentData.dateEnd.formatStrDateToIsoAPi(),
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
                                dateBegin = task.plannedStartDate.formatStrDateToShortDate(),
                                dateEnd = task.plannedEndDate.formatStrDateToShortDate(),
                                estimateHours = task.estimationHours,
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
                                projectUpdate = task.projectId,
                                selectedProject = newTaskStateFlow.value.project?.first { it.id == task.projectId }
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
                                project = projectData.projects,

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


        _newTaskStateFlow.update {

            it.copy(
                titleError =
                when {
                    currentData.title.isBlank() -> com.example.btppilot.R.string.title_required
                    currentData.title.length < 2 -> com.example.btppilot.R.string.min_char_2
                    else -> null
                },

                descriptionError =
                when {
                    currentData.description.isBlank() -> com.example.btppilot.R.string.description_required
                    currentData.description.length < 2 -> com.example.btppilot.R.string.min_char_2
                    else -> null
                },

                projectError = when {
                    currentData.selectedProject == null -> com.example.btppilot.R.string.select_project
                    else -> null
                },


                selectedUserError = when {
                    currentData.selectUser.isEmpty() -> com.example.btppilot.R.string.select_user
                    else -> null
                }
            )
        }

        newTaskStateFlow.value.let {
            if (it.titleError == null
                && it.descriptionError == null
                && it.selectedUserError == null
                && it.projectError == null
            )
                return true
        }
        return false
    }

}