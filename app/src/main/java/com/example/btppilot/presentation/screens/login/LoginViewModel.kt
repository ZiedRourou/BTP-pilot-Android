package com.example.btppilot.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.LoginRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref,
    private val authRepository: AuthRepository,
) : ViewModel() {

    data class LoginInfo(
        val email: String = "",
        val password: String = "",
        val emailError: String? = null,
        val passwordError: String? = null,
        val isLoading: Boolean = false
    )

    sealed class LoginEvent {
        data class ShowError(val message: String) : LoginEvent()
        data object LoginSuccess : LoginEvent()
    }

    private val _loginUserInfo = MutableStateFlow(LoginInfo())
    val loginUserInfo: StateFlow<LoginInfo> = _loginUserInfo

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()


    fun onEmailChange(email: String) {
        _loginUserInfo.value = _loginUserInfo.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onPasswordChange(password: String) {
        _loginUserInfo.value = _loginUserInfo.value.copy(
            password = password,
            passwordError = null
        )
    }


    fun login() {

        val currentState = _loginUserInfo.value

        val validation = validateLoginData(
            LoginRequestDto(currentState.email, currentState.password)
        )

        if (validation.emailError != null || validation.passwordError != null) {

            _loginUserInfo.value = currentState.copy(
                emailError = validation.emailError,
                passwordError = validation.passwordError
            )
            return
        }

        viewModelScope.launch {

            _loginUserInfo.value = currentState.copy(isLoading = true)

            val result = withContext(Dispatchers.IO) {
                authRepository.loginUser(
                    LoginRequestDto(currentState.email, currentState.password)
                )
            }
            when (result) {

                is Resource.Success -> {
                    _loginUserInfo.value = currentState.copy(isLoading = false)

                    result.data?.let {
                        authSharedPref.saveUserInfo(
                            token = it.accessToken,
                            userId = it.user.id,
                            firstname = it.user.firstName,
                            role = it.user.role,
                            email = it.user.email
                        )
                    }

                    _event.emit(LoginEvent.LoginSuccess)
                }

                is Resource.Error -> {

                    _loginUserInfo.value = currentState.copy(isLoading = false)

                    val message = when (result.code) {

                        401 -> "email ou mot de passe invalide"
                        400 -> "Informations invalides"

                        else -> result.message ?: "Erreur inconnue"
                    }

                    _event.emit(
                        LoginEvent.ShowError(message)
                    )
                }

                is Resource.Loading -> {
                    _loginUserInfo.value = currentState.copy(isLoading = true)
                }
            }
        }
    }

    private fun validateLoginData(contact: LoginRequestDto): LoginValidationResult {

        var emailError: String? = null
        var passwordError: String? = null

        if (contact.email.isBlank()) {
            emailError = "Veuillez entrer votre email"
        } else {
            val emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
            if (!emailRegex.matches(contact.email)) {
                emailError = "Email invalide"
            }
        }

        if (contact.password.isBlank()) {
            passwordError = "Veuillez entrer votre mot de passe"
        }

        return LoginValidationResult(
            emailError,
            passwordError
        )
    }

    data class LoginValidationResult(
        val emailError: String?,
        val passwordError: String?
    )

}

