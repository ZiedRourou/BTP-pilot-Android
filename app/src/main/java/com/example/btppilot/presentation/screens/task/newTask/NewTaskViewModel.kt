package com.example.btppilot.presentation.screens.task.newTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.response.project.team.UserProjectDtoItem
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.util.Resource
import com.squareup.moshi.Json
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    data class NewTaskState(
        val title: String = "",
        val description: String = "",

        val users : List<UserProjectDtoItem> = listOf(),
        val estimateHours : String = "",
        val plannedStartDate: String = "",
        val plannedEndDate: String = "",
        val status : String = "",
        val priority : String = "",
        val projectId: Long = 0,
        val isLoading: Boolean = false

    )

    private val _newTaskStateFlow = MutableStateFlow(NewTaskState())
    val newTaskStateFlow = _newTaskStateFlow.asStateFlow()


    fun setProjectId(projectId: Long) {
        _newTaskStateFlow.update {
            it.copy(
                projectId = projectId
            )
        }
        fetchUserProject()
    }

    private fun fetchUserProject() {
        viewModelScope.launch {
            _newTaskStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                projectRepository.getUsersOfProject(newTaskStateFlow.value.projectId.toInt())
            }
            when (result) {

                is Resource.Success -> {
                    _newTaskStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _newTaskStateFlow.update {
                        it.copy(
                            users = result.data ?: emptyList(),
                        )
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
}