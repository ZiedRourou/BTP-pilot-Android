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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiInterface
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()


    fun login(email: String, password: String) {

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true

            try {
                val response = apiService.authLogin(
                    AuthRequestDto(email, password)
                )

                if (response?.isSuccessful == true) {
                    Log.e("ici", "la je t'ai dis ")
                    val body = response.body()

                } else {
                    Log.e("ici2", "la je t'ai dis ")

                    _errorMessage.value = "Login failed"
                }

            } catch (e: Exception) {
                Log.e("ici3", "la je t'ai dis ")

                _errorMessage.value = "erreur"
            }

            _isLoading.value = false
        }
    }
}