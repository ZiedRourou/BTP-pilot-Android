package com.example.btppilot.ui.screens.auth.register.inviteUserToCompany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.user.InviteUserCompanyRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.ui.navigation.NavGraph
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.data.local.AuthSharedPref
import com.example.btppilot.util.Resource
import com.example.btppilot.util.UserRole
import com.example.btppilot.util.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterLinkUserToCompanyViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authSharedPref: AuthSharedPref
) : ViewModel() {
    data class CompanyInfo(

        val email: String = "",
        val emailError: String? = null,
        val selectedRole: UserRole = UserRole.CLIENT,
        val isLoading: Boolean = false
    )

    private val _companyInfoInviteStateFlow = MutableStateFlow(CompanyInfo())
    val companyInfoInviteStateFlow = _companyInfoInviteStateFlow.asStateFlow()

    private val _companyInfoInviteEventSharedFlow = MutableSharedFlow<EventState>()
    val companyInfoInviteEventSharedFlow = _companyInfoInviteEventSharedFlow.asSharedFlow()


    fun onEmailChange(value: String) {
        _companyInfoInviteStateFlow.value = _companyInfoInviteStateFlow.value.copy(
            email = value,
            emailError = null
        )
    }

    fun setRole(role : String){
        _companyInfoInviteStateFlow.update {
            it.copy(
                selectedRole = UserRole.valueOf(role)
            )
        }
    }
    fun inviteUserToCompany() {

        if (!validateInviteDataAndSetError())
            return

        viewModelScope.launch {

            _companyInfoInviteStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            delay(1000)

            val result = withContext(Dispatchers.IO) {
                authRepository.inviteUserToCompany(
                    InviteUserCompanyRequestDto(
                        email = companyInfoInviteStateFlow.value.email,
                        roleCompany = companyInfoInviteStateFlow.value.selectedRole
                    )
                )
            }

            when (result) {

                is Resource.Success -> {
                    _companyInfoInviteStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    authSharedPref.saveCompanyInfo(
                        companyId = result.data?.companyId ?: 0,
                        role = result.data?.role ?: ""
                    )
                    _companyInfoInviteEventSharedFlow.emit(
                        EventState.RedirectGraph(NavGraph.MainGraph)
                    )

                }

                is Resource.Error -> {
                    _companyInfoInviteStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _companyInfoInviteEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )

                }

            }
        }
    }

    private fun validateInviteDataAndSetError(
    ): Boolean {

        val currentData = companyInfoInviteStateFlow.value

        _companyInfoInviteStateFlow.update {
            it.copy(
                emailError = when {
                    currentData.email.isBlank() -> "Email requis"
                    !currentData.email.isEmailValid() -> "Email invalide"
                    else -> null
                }
            )
        }
        return companyInfoInviteStateFlow.value.emailError.isNullOrBlank()
    }
}