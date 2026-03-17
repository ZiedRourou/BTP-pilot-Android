package com.example.btppilot.ui.screens.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.auth.RegisterRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.ui.navigation.Screen
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.data.local.AuthSharedPref
import com.example.btppilot.util.Resource
import com.example.btppilot.util.UserRole
import com.example.btppilot.util.isConfirmPasswordValid
import com.example.btppilot.util.isEmailValid
import com.example.btppilot.util.isStrongPassword
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
class RegisterSharedViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    data class RegisterState(
        val selectedRole: UserRole = UserRole.COLLABORATOR,

        val firstName: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",

        val firstNameError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null,

        val isLoading: Boolean = false

    )


    private val _registerUserInfoStateFlow = MutableStateFlow(RegisterState())
    val registerUserInfoStateFlow = _registerUserInfoStateFlow.asStateFlow()

    private val _registerUserEventSharedFlow = MutableSharedFlow<EventState>()
    val registerUserEventSharedFlow = _registerUserEventSharedFlow.asSharedFlow()


    fun selectRole(role: UserRole) {
        _registerUserInfoStateFlow.value =
            _registerUserInfoStateFlow.value.copy(selectedRole = role)
    }

    fun goToCompanyInfo() {
        val route = when (_registerUserInfoStateFlow.value.selectedRole) {

            UserRole.OWNER -> Screen.RegisterOwnerCompany

            UserRole.COLLABORATOR-> Screen.RegisterInviteCompany
            UserRole.CLIENT -> Screen.RegisterInviteCompany
        }


        viewModelScope.launch {
            if (route is Screen.RegisterInviteCompany)
                _registerUserEventSharedFlow.emit(EventState.RedirectScreenWithId(route.route +"/"+ registerUserInfoStateFlow.value.selectedRole.name ))
                else
            _registerUserEventSharedFlow.emit(EventState.RedirectScreen(route))
        }
    }


    fun onFirstNameChange(value: String) {
        _registerUserInfoStateFlow.value = _registerUserInfoStateFlow.value.copy(
            firstName = value,
            firstNameError = null
        )
    }

    fun onEmailChange(value: String) {
        _registerUserInfoStateFlow.value = _registerUserInfoStateFlow.value.copy(
            email = value,
            emailError = null
        )
    }

    fun onPasswordChange(value: String) {
        _registerUserInfoStateFlow.value = _registerUserInfoStateFlow.value.copy(
            password = value,
            passwordError = null
        )
    }

    fun onConfirmPasswordChange(value: String) {
        _registerUserInfoStateFlow.value = _registerUserInfoStateFlow.value.copy(
            confirmPassword = value,
            confirmPasswordError = null
        )
    }

    fun registerUser() {

        if (!validateRegisterDataAndSetError())
            return

        viewModelScope.launch {
            _registerUserInfoStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }
            delay(1000)

            val result = withContext(Dispatchers.IO) {
                authRepository.registerUser(
                    RegisterRequestDto(
                        firstName = registerUserInfoStateFlow.value.firstName,
                        email = registerUserInfoStateFlow.value.email,
                        password = registerUserInfoStateFlow.value.password,
                        role = "USER"
                    )
                )
            }
            when (result) {

                is Resource.Success -> {
                    _registerUserInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    result.data?.let {
                        authSharedPref.saveUserInfo(
                            token = it.accessToken,
                            userId = it.user.id,
                            firstname = it.user.firstName,
                            role = it.user.role,
                            email = it.user.email
                        )
                    }

                    viewModelScope.launch {
                        _registerUserEventSharedFlow.emit(
                            EventState.RedirectScreen(Screen.RegisterRole)
                        )
                    }

                }

                is Resource.Error -> {

                    _registerUserInfoStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _registerUserEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )

                }

            }
        }
    }


    private fun validateRegisterDataAndSetError(): Boolean {

        val currentUserInfo = registerUserInfoStateFlow.value

        _registerUserInfoStateFlow.update {
            it.copy(
                firstNameError = when {
                    currentUserInfo.firstName.isBlank() -> "Prénom requis"
                    currentUserInfo.firstName.length < 3 -> "Minimum 3 caractères"
                    currentUserInfo.firstName.length > 20 -> "Maximum 20 caractères"
                    else -> null
                },

                emailError = when {
                    currentUserInfo.email.isBlank() -> "Email requis"
                    !currentUserInfo.email.isEmailValid() -> "Email invalide"
                    else -> null
                },

                passwordError = when {
                    currentUserInfo.password.isBlank() -> "Mot de passe requis"
                    !currentUserInfo.password.isStrongPassword() -> "8 caractères, majuscule, minuscule, chiffre et symbole requis"
                    else -> null
                },
                confirmPasswordError = when {
                    !currentUserInfo.password.isConfirmPasswordValid(currentUserInfo.confirmPassword) -> "Les mots de passe ne correspondent pas"
                    else -> null
                }
            )
        }

        registerUserInfoStateFlow.value.let {
            if (
                it.emailError.isNullOrBlank()
                && it.passwordError.isNullOrBlank()
                && it.confirmPasswordError.isNullOrBlank()
                && it.firstNameError.isNullOrBlank()
            )
                return true
        }

        return false
    }
}