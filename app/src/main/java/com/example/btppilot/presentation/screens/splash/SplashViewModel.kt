package com.example.btppilot.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.presentation.screens.navigation.Screen
import com.example.btppilot.util.AuthSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref
) : ViewModel() {
    private val _mainScreenRoute = MutableSharedFlow<String>()
    val mainScreenRouteSF = _mainScreenRoute.asSharedFlow()

    init {
        authSharedPref.clearLogin()
        switchNavigation()
    }

    private fun switchNavigation() {
        viewModelScope.launch {

            delay(1500)

            if (authSharedPref.isLogin()) {
                _mainScreenRoute.emit(Screen.Home.route)
            } else {
                _mainScreenRoute.emit(Screen.Login.route)
            }
        }
    }
}