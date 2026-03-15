package com.example.btppilot.ui.screens2.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.user.InviteUserCompanyRequestDto
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.data.repository.CompanyRepository
import com.example.btppilot.presentation.screens.shared.uiState.EventState
import com.example.btppilot.util.Resource
import com.example.btppilot.util.UserRole
import com.example.btppilot.util.isEmailValid
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
class TeamViewModel @Inject constructor(
    private val companyRepository: CompanyRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    data class TeamState(
        val userList: List<UsersOfCompanyItem> = listOf(),
        val isLoading: Boolean = false,

        val email: String = "",
        val emailError: String? = null,
        val selectedRole: UserRole = UserRole.CLIENT,
    )

    private val _teamStateFlow = MutableStateFlow(TeamState())
    val teamStateFlow = _teamStateFlow.asStateFlow()

    private val _teamEventSharedFlow = MutableSharedFlow<EventState>()
    val teamEventSharedFlow = _teamEventSharedFlow.asSharedFlow()


    init {
        getUserCompany()
    }

    fun onEmailChange(value: String) {
        _teamStateFlow.update {
            it.copy(
                email = value,
                emailError = null
            )
        }
    }

    fun inviteUser() {
        if (!validateInviteDataAndSetError())
            return
        inviteUserToCompany()
    }

    fun onRoleChange(value: UserRole) {
        _teamStateFlow.update {
            it.copy(
                selectedRole = value
            )
        }
    }

    fun inviteUserToCompany() {

        if (!validateInviteDataAndSetError())
            return

        viewModelScope.launch {

            _teamStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = withContext(Dispatchers.IO) {
                authRepository.inviteUserToMyCompany(
                    InviteUserCompanyRequestDto(
                        email = teamStateFlow.value.email,
                        roleCompany = teamStateFlow.value.selectedRole
                    )
                )
            }

            when (result) {

                is Resource.Success -> {
                    _teamStateFlow.update {
                        it.copy(
                            isLoading = false,
                            email = "",
                        )
                    }

                    getUserCompany()
                    _teamEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar("utilisateur invité")
                    )

                }

                is Resource.Error -> {
                    _teamStateFlow.update {
                        it.copy(
                            isLoading = false,
                        )
                    }

                    _teamEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }

            }
        }
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
                                userList = users.filter { it.role != UserRole.OWNER.name },
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

    private fun validateInviteDataAndSetError(
    ): Boolean {

        val currentData = teamStateFlow.value

        _teamStateFlow.update {
            it.copy(
                emailError = when {
                    currentData.email.isBlank() -> "Email requis"
                    !currentData.email.isEmailValid() -> "Email invalide"
                    else -> null
                }
            )
        }
        return teamStateFlow.value.emailError.isNullOrBlank()
    }
}