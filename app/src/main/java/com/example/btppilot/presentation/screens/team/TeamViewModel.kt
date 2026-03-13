package com.example.btppilot.presentation.screens.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.repository.CompanyRepository
import com.example.btppilot.data.repository.ProjectRepository
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {

    data class TeamState(
        val userList: List<UsersOfCompanyItem> = listOf(),
        val isLoading: Boolean = false,
    )

    private val _teamStateFlow = MutableStateFlow(TeamState())
    val teamStateFlow = _teamStateFlow.asStateFlow()



    init {
        getUserCompany()
    }


    private fun getUserCompany() {

        viewModelScope.launch {
            _teamStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = withContext(Dispatchers.IO) {
                companyRepository.getUsersOfCompany()
            }
            when (result) {

                is Resource.Success -> {
                    _teamStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    result.data?.let { users ->
                        _teamStateFlow.update {
                            it.copy(
                                userList = users,
                            )
                        }
                    }
                }

                is Resource.Error -> {

                    _teamStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                }
            }
        }
    }

}