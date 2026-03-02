package com.example.btppilot.presentation.screens.register.stepThree.linkUserToCompany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.InviteUserCompanyRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.util.Resource
import com.example.btppilot.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterLinkUserToCompanyViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    data class CompanyInfo(

        val email: String = "",
        val emailError: String? = null,
        val selectedRole: UserRole? = UserRole.CLIENT,
        val isLoading: Boolean = false
    )

    sealed class InviteEvent {
        object NavigateToHome : InviteEvent()
        data class ShowError(val message: String) : InviteEvent()
    }

    private val _companyInfo = MutableStateFlow(CompanyInfo())
    val companyInfo = _companyInfo.asStateFlow()

    private val _inviteEvent = MutableSharedFlow<InviteEvent>()
    val inviteEvent = _inviteEvent.asSharedFlow()


    fun onEmailChange(value: String) {
        _companyInfo.value = _companyInfo.value.copy(
            email = value,
            emailError = null
        )
    }

    fun setUserRoleCompanyLink(role:String){
        _companyInfo.value = _companyInfo.value.copy(
            selectedRole = UserRole.valueOf(role)
        )
    }


    fun inviteUserToCompany() {
        val currentState = _companyInfo.value

        val validation = validateInviteData(
            inviteData = InviteUserCompanyRequestDto(
                email = currentState.email,
                roleCompany = currentState.selectedRole!!,
            ),
        )

        if (validation.emailError != null) {
            _companyInfo.value = currentState.copy(
                emailError = validation.emailError,
            )
            return
        }

        viewModelScope.launch {

            _companyInfo.value = _companyInfo.value.copy(isLoading = true)

            val result = withContext(Dispatchers.IO) {
                authRepository.inviteUserToCompany(
                    InviteUserCompanyRequestDto(
                        email = companyInfo.value.email,
                        roleCompany = companyInfo.value.selectedRole!!
                    )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _companyInfo.value = _companyInfo.value.copy(isLoading = false)
                    _inviteEvent.emit(InviteEvent.NavigateToHome)
                }
                is Resource.Error -> {
                    _companyInfo.value =
                        _companyInfo.value.copy(isLoading = false)

                    val message = when (result.code) {

                        409 -> "vous etes déja lié a l'entreprise"
                        400 -> "Informations invalides"
                        404 -> "Entreprise non trouvé"
                        else -> result.message ?: "Erreur inconnue"
                    }

                    _inviteEvent.emit(InviteEvent.ShowError(message))
                }

                is Resource.Loading -> {
                    _companyInfo.value = _companyInfo.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun validateInviteData(
        inviteData: InviteUserCompanyRequestDto
    ): InviteValidationResult {

        var emailError: String? = null

        when {
            inviteData.email.isBlank() ->
                emailError = "Email requis"

            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(inviteData.email)
                .matches() ->
                emailError = "Email invalide"
        }

        return InviteValidationResult(emailError)
    }

    data class InviteValidationResult(
        val emailError: String?,
    )
}