package com.example.btppilot.presentation.screens.auth.register.inviteUserToCompany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.InviteUserCompanyRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.util.AuthSharedPref
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
        val selectedRole: UserRole? = UserRole.CLIENT,
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

    fun setUserRoleCompanyLink(role: String) {
        _companyInfoInviteStateFlow.value = _companyInfoInviteStateFlow.value.copy(
            selectedRole = UserRole.valueOf(role)
        )
    }


    fun inviteUserToCompany() {

        if(!validateInviteData())
            return

        viewModelScope.launch {

            _companyInfoInviteStateFlow.value = _companyInfoInviteStateFlow.value.copy(isLoading = true)
            delay(2000)

            val result = withContext(Dispatchers.IO) {
                authRepository.inviteUserToCompany(
                    InviteUserCompanyRequestDto(
                        email = companyInfoInviteStateFlow.value.email,
                        roleCompany = companyInfoInviteStateFlow.value.selectedRole!!
                    )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _companyInfoInviteStateFlow.value = _companyInfoInviteStateFlow.value.copy(isLoading = false)
                    _companyInfoInviteEventSharedFlow.emit(EventState.RedirectScreen(Screen.MainGraph))
                    authSharedPref.saveCompanyInfo(
                        companyId = result.data?.companyId ?: 0,
                        role = result.data?.role ?: ""
                    )
                }

                is Resource.Error -> {
                    _companyInfoInviteStateFlow.value =
                        _companyInfoInviteStateFlow.value.copy(isLoading = false)

                    when (result.code) {

                        409 -> "vous etes déja lié a l'entreprise"
                        400 -> "Informations invalides"
                        404 -> "Entreprise non trouvé"
                        else -> result.message ?: "Erreur inconnue"
                    }.let {
                        _companyInfoInviteEventSharedFlow.emit(EventState.ShowMessageSnackBar(it))
                    }

                }

            }
        }
    }

    private fun validateInviteData(
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