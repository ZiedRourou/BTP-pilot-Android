package com.example.btppilot.presentation.screens.register.stepThree.linkUserToCompany

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.requ.RegisterRequestDto
import com.example.btppilot.presentation.screens.register.stepOneAndTwo.RegisterViewModel
import com.example.btppilot.util.Resource
import com.example.btppilot.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterLinkUserToCompany @Inject constructor(

): ViewModel() {
    data class CompanyInfo(

        val email: String = "",
        val emailError: String? = null,

        val isLoading: Boolean = false
    )

    private val _companyInfo = MutableStateFlow(CompanyInfo())
    val companyInfo = _companyInfo.asStateFlow()


    fun onEmailChange(value: String) {
        _companyInfo.value = _companyInfo.value.copy(
            email = value,
            emailError = null
        )
    }


    fun inviteUserToCompany() {
        val currentState = _companyInfo.value

        val validation = validateInviteData(
            contact = RegisterRequestDto(
                firstName = currentState.firstName,
                email = currentState.email,
                password = currentState.password,
                role = "USER"
            ),
            confirmPassword = currentState.confirmPassword
        )

        if (
            validation.firstNameError != null ||
            validation.emailError != null ||
            validation.passwordError != null ||
            validation.confirmPasswordError != null
        ) {

            _registerInfo.value = currentState.copy(
                firstNameError = validation.firstNameError,
                emailError = validation.emailError,
                passwordError = validation.passwordError,
                confirmPasswordError = validation.confirmPasswordError
            )

            return
        }


        viewModelScope.launch {

            _registerInfo.value = _registerInfo.value.copy(isLoading = true)

            val result = withContext(Dispatchers.IO) {
                authRepository.registerUser(
                    RegisterRequestDto(
                        firstName = registerInfo.value.firstName,
                        email = registerInfo.value.email,
                        password = registerInfo.value.password,
                        role = "USER"
                    )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _registerInfo.value = _registerInfo.value.copy(isLoading = false)

                    result.data?.let {
                        authSharedPref.saveUserInfo(
                            token = it.accessToken,
                            userId = it.user.id,
                            firstname = it.user.firstName,
                            role = it.user.role,
                            email = it.user.email
                        )
                    }

                    when (_registerInfo.value.selectedRole) {

                        UserRole.OWNER ->
                            _registerEvent.emit(RegisterViewModel.RegisterEvent.NavigateToOwnerCompany)

                        UserRole.COLLABORATOR,
                        UserRole.CLIENT ->
                            _registerEvent.emit(RegisterViewModel.RegisterEvent.NavigateToInviteCompany)

                        null -> {}
                    }
                }


                is Resource.Error -> {

                    _registerInfo.value =
                        _registerInfo.value.copy(isLoading = false)

                    val message = when (result.code) {

                        409 -> "Cet email est déjà utilisé"

                        400 -> "Informations invalides"

                        else -> result.message ?: "Erreur inconnue"
                    }

                    _registerEvent.emit(
                        RegisterViewModel.RegisterEvent.ShowError(message)
                    )
                }

                is Resource.Loading -> {
                    _registerInfo.value = _registerInfo.value.copy(isLoading = true)
                }
            }
        }
    }

}