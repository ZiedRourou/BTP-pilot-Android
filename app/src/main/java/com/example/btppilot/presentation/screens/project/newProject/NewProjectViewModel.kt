package com.example.btppilot.presentation.screens.project.newProject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.ProjectRequestDto
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.repository.CompanyRepository
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.Resource
import com.example.btppilot.util.toIsoDate
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
class NewProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val authSharedPref: AuthSharedPref,
    private val companyRepository: CompanyRepository
) : ViewModel() {

    data class NewProjectState(

        val title: String = "",
        val description: String = "",
        val userOption : List<UsersOfCompanyItem> = listOf(),

        val manager: UserCompany = UserCompany(0,"","", ""),

        val dateBegin: String =   "%02d/%02d/%04d".format(
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

        val selectedPriority: ProjectAndTakPriorities = ProjectAndTakPriorities.LOW,

        val titleError: String? = null,
        val dateEndError: String? = null,

        val isLoading : Boolean = false
    )

    private val _newProjectStateFlow = MutableStateFlow(NewProjectState())
    val newProjectStateFlow = _newProjectStateFlow.asStateFlow()

    private val _newProjectEventSharedFlow = MutableSharedFlow<EventState>()
    val newProjectEventSharedFlow = _newProjectEventSharedFlow.asSharedFlow()


    init {
        getUserCompany()
    }


    fun onTitleChange(title: String) {
        _newProjectStateFlow.value = _newProjectStateFlow.value.copy(
            title = title,
        )
    }

    fun onDescriptionChange(description: String) {
        _newProjectStateFlow.value = _newProjectStateFlow.value.copy(
            description = description,
        )
    }

    fun onManagerChange(user: UserCompany) {
        _newProjectStateFlow.value = _newProjectStateFlow.value.copy(
            manager = user,
        )
    }

    fun onBeginDateChange(beginDate: String) {
        _newProjectStateFlow.value = _newProjectStateFlow.value.copy(
            dateBegin = beginDate,
        )
    }

    fun onEndDateChange(endDate: String) {
        _newProjectStateFlow.value = _newProjectStateFlow.value.copy(
            dateEnd = endDate,
        )
    }

    fun onPriorityChange(priority: ProjectAndTakPriorities) {
        _newProjectStateFlow.value = _newProjectStateFlow.value.copy(
            selectedPriority = priority,
        )
    }

    private fun getUserCompany(){

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
                        _newProjectStateFlow.update {
                            it.copy(
                                userOption = users,
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
    fun publishProject(){

        if(!validateDataProject())
            return

        viewModelScope.launch {
            _newProjectStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.newProject(
                    newProjectStateFlow.value.let {
                        ProjectRequestDto(
                            name = it.title,
                            description = it.description,
                            status = ProjectStatus.PLANNED.name,
                            priority = it.selectedPriority.name,
                            plannedStartDate = toIsoDate(it.dateBegin),
                            plannedEndDate = toIsoDate(it.dateEnd),
                            userId = it.manager.id,
                            isActive = true
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
                       EventState.RedirectScreen(Screen.Home)
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

    private fun validateDataProject() : Boolean{

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

                dateEndError = when {
                    start == null || end == null -> "Format de date invalide"

                    start.after(end) ->"Veuillez sélectionner une période postérieure"

                    else -> null
                }
            )
        }

        newProjectStateFlow.value.let {
            if(it.titleError.isNullOrEmpty() && it.dateEndError.isNullOrEmpty())
                return true
        }
        return false
    }
}