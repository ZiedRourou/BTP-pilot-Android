package com.example.btppilot.presentation.screens.register.stepThree.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.InviteUserCompanyRequestDto
import com.example.btppilot.data.dto.request.NewCompanyRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.data.repository.CompanyRepository
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
class RegisterCompanyViewModel @Inject constructor(
    private val companyRepository: CompanyRepository
) : ViewModel() {
    data class NewCompanyInfo(
        val siret: String = "",
        val name: String = "",
        val activity: String ="",

        val siretError: String? = "",
        val nameError: String? = "",
        val activityError: String? ="",

        val isLoading: Boolean = false
    )

    sealed class RegisterCompanyEvent {
        object NavigateToHome : RegisterCompanyEvent()
        data class ShowError(val message: String) : RegisterCompanyEvent()
    }

    private val _companyInfo = MutableStateFlow(NewCompanyInfo())
    val companyInfo = _companyInfo.asStateFlow()

    private val _inviteEvent = MutableSharedFlow<RegisterCompanyEvent>()
    val inviteEvent = _inviteEvent.asSharedFlow()


    fun onNameChange(value: String) {
        _companyInfo.value = _companyInfo.value.copy(
            name = value,
            nameError = null
        )
    }

    fun onSiretChange(value: String) {
        _companyInfo.value = _companyInfo.value.copy(
            siret = value,
            siretError = null
        )
    }
    fun onActivityChange(value: String) {
        _companyInfo.value = _companyInfo.value.copy(
            activity = value,
            activityError = null
        )
    }



    fun createCompany() {
        val currentState = _companyInfo.value

        val validation = validateCompanyData(
            companyData = NewCompanyRequestDto(
                siret = currentState.siret,
                name = currentState.name,
                activity = currentState.activity
            ),
        )

        if (validation.siretError != null) {
            _companyInfo.value = currentState.copy(
                siretError = validation.siretError,
            )
            return
        }

        viewModelScope.launch {

            _companyInfo.value = _companyInfo.value.copy(isLoading = true)

            val result = withContext(Dispatchers.IO) {
                companyRepository.createCompanyUser(
                    NewCompanyRequestDto(
                        siret = companyInfo.value.siret,
                        name = companyInfo.value.name,
                        activity = companyInfo.value.activity
                    )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _companyInfo.value = _companyInfo.value.copy(isLoading = false)
                    _inviteEvent.emit(RegisterCompanyEvent.NavigateToHome)
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

                    _inviteEvent.emit(RegisterCompanyEvent.ShowError(message))
                }

                is Resource.Loading -> {
                    _companyInfo.value = _companyInfo.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun validateCompanyData(
        companyData: NewCompanyRequestDto
    ): CompanyValidationResult {

        var siretError: String? = null
        var nameError: String? = null
        var activityError: String? = null

        when {
//            inviteData.email.isBlank() ->
//                emailError = "Email requis"
//
//            !android.util.Patterns.EMAIL_ADDRESS
//                .matcher(inviteData.email)
//                .matches() ->
//                emailError = "Email invalide"
        }

        return CompanyValidationResult(
            siretError, nameError, activityError
        )
    }

    data class CompanyValidationResult(
        val siretError: String?,
        val nameError: String?,
        val activityError: String?
    )
}