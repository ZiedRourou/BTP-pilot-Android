package com.example.btppilot.presentation.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.LoginRequestDto
import com.example.btppilot.data.repository.AuthRepository
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.EMAIL_REGEX
import com.example.btppilot.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
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

    private val _loginUserInfoStateFlow = MutableStateFlow(LoginInfo())
    val loginUserInfoStateFlo: StateFlow<LoginInfo> = _loginUserInfoStateFlow

    private val _loginUserEventStateFlo = MutableSharedFlow<EventState>()
    val loginUserEventStateFlo = _loginUserEventStateFlo.asSharedFlow()

    fun onEmailChange(email: String) {
        _loginUserInfoStateFlow.value = _loginUserInfoStateFlow.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onPasswordChange(password: String) {
        _loginUserInfoStateFlow.value = _loginUserInfoStateFlow.value.copy(
            password = password,
            passwordError = null
        )
    }

    fun goToRegister(){
        viewModelScope.launch {
            _loginUserEventStateFlo.emit(EventState.RedirectScreen(Screen.RegisterGraph))
        }
    }

    fun login() {

        val currentState = loginUserInfoStateFlo.value

        if(!validateLoginData())
            return

        viewModelScope.launch {

            _loginUserInfoStateFlow.value = currentState.copy(isLoading = true)
            delay(2000)

            val result = withContext(Dispatchers.IO) {
                authRepository.loginUser(
                    LoginRequestDto(currentState.email, currentState.password)
                )
            }

            when (result) {

                is Resource.Success -> {
                    _loginUserInfoStateFlow.value = currentState.copy(isLoading = false)

                    result.data?.let {
                        authSharedPref.saveUserInfo(
                            token = it.accessToken,
                            userId = it.user.id,
                            firstname = it.user.firstName,
                            role = it.user.role,
                            email = it.user.email
                        )
                        authSharedPref.saveCompanyInfo(
                            companyId = it.user.userCompanies?.first()?.companyId ?: 0,
                            role = it.user.userCompanies?.first()?.role ?: ""
                        )
                    }

                    _loginUserEventStateFlo.emit(EventState.RedirectScreen(Screen.MainGraph))
                }

                is Resource.Error -> {

                    _loginUserInfoStateFlow.value = currentState.copy(isLoading = false)


                    _loginUserEventStateFlo.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    private fun validateLoginData(): Boolean {

        val currentUserInfo = loginUserInfoStateFlo.value

        _loginUserInfoStateFlow.update {
            it.copy(
                emailError = when {
                    currentUserInfo.email.isBlank() -> "Email requis"
                    !Regex(EMAIL_REGEX).matches(currentUserInfo.email) -> "Email invalide"
                    else -> null
                },
                passwordError = when {
                    currentUserInfo.password.isBlank() -> "Mot de passe requis"
                    else -> null
                }
            )
        }

        loginUserInfoStateFlo.value.let {
            if (it.emailError.isNullOrBlank() && it.passwordError.isNullOrBlank())
                return true
        }

        return false
    }
}

