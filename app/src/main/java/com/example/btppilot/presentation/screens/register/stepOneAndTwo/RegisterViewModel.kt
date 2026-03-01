package com.example.btppilot.presentation.screens.register.stepOneAndTwo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.requ.RegisterRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.util.AuthSharedPref
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
class RegisterViewModel @Inject constructor(
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

    sealed class RegisterEvent {
        object NavigateToUserInfo : RegisterEvent()
        object NavigateToOwnerCompany : RegisterEvent()
        object NavigateToInviteCompany : RegisterEvent()
        data class ShowError(val message: String) : RegisterEvent()
    }

    private val _registerInfo = MutableStateFlow(RegisterState())
    val registerInfo = _registerInfo.asStateFlow()

    private val _registerEvent = MutableSharedFlow<RegisterEvent>()
    val registerEvent = _registerEvent.asSharedFlow()


    fun selectRole(role: UserRole) {
        _registerInfo.value = _registerInfo.value.copy(selectedRole = role)
    }

    fun goToNextStep() {
        viewModelScope.launch {
            _registerEvent.emit(RegisterEvent.NavigateToUserInfo)
        }
    }

    fun onFirstNameChange(value: String) {
        _registerInfo.value = _registerInfo.value.copy(
            firstName = value,
            firstNameError = null
        )
    }

    fun onEmailChange(value: String) {
        _registerInfo.value = _registerInfo.value.copy(
            email = value,
            emailError = null
        )
    }

    fun onPasswordChange(value: String) {
        _registerInfo.value = _registerInfo.value.copy(
            password = value,
            passwordError = null
        )
    }

    fun onConfirmPasswordChange(value: String) {
        _registerInfo.value = _registerInfo.value.copy(
            confirmPassword = value,
            confirmPasswordError = null
        )
    }

    fun registerUser() {
        val currentState = _registerInfo.value

        val validation = validateRegisterData(
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
                            _registerEvent.emit(RegisterEvent.NavigateToOwnerCompany)

                        UserRole.COLLABORATOR,
                        UserRole.CLIENT ->
                            _registerEvent.emit(RegisterEvent.NavigateToInviteCompany)

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
                        RegisterEvent.ShowError(message)
                    )
                }

                is Resource.Loading -> {
                    _registerInfo.value = _registerInfo.value.copy(isLoading = true)
                }
            }
        }
    }



    private fun validateRegisterData(
        contact: RegisterRequestDto,
        confirmPassword: String
    ): RegisterValidationResult {

        var firstNameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null

        when {
            contact.firstName.isBlank() ->
                firstNameError = "Prénom requis"

            contact.firstName.length < 3 ->
                firstNameError = "Minimum 3 caractères"

            contact.firstName.length > 20 ->
                firstNameError = "Maximum 20 caractères"
        }


        when {
            contact.email.isBlank() ->
                emailError = "Email requis"

            contact.email.length > 30 ->
                emailError = "Email trop long"

            !android.util.Patterns.EMAIL_ADDRESS
                .matcher(contact.email)
                .matches() ->
                emailError = "Email invalide"
        }

        val strongPasswordRegex =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}\$")

        when {
            contact.password.isBlank() ->
                passwordError = "Mot de passe requis"

            !strongPasswordRegex.matches(contact.password) ->
                passwordError =
                    "8 caractères, majuscule, minuscule, chiffre et symbole requis"
        }

        if (contact.password != confirmPassword) {
            confirmPasswordError =
                "Les mots de passe ne correspondent pas"
        }

        return RegisterValidationResult(
            firstNameError,
            emailError,
            passwordError,
            confirmPasswordError
        )
    }

    data class RegisterValidationResult(

        val firstNameError: String?,
        val emailError: String?,
        val passwordError: String?,
        val confirmPasswordError: String?
    )
}