package com.example.btppilot.presentation.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.AuthRequestDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiInterface
) : ViewModel() {

    sealed class LoginEvent {
        data class ShowError(val message: String) : LoginEvent()
    }
    data class LoginUiState(
        val isLoading: Boolean = false
    )
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _event =  MutableSharedFlow<LoginEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    fun login(email: String, password: String) {

        viewModelScope.launch {

            _uiState.value = LoginUiState(isLoading = true)

            try {

                val response = withContext(Dispatchers.IO) {
                    apiService.authLogin(
                        AuthRequestDto(email, password)
                    )
                }

                if (response?.isSuccessful != true) {
                    _event.emit(
                        LoginEvent.ShowError(
                            "Email ou mot de passe incorrect"
                        )
                    )
                }

            } catch (e: Exception) {

                _event.emit(
                    LoginEvent.ShowError("Erreur réseau")
                )

            } finally {
                _uiState.value =
                    _uiState.value.copy(isLoading = false)
            }
        }
    }
}