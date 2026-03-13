package com.example.btppilot.presentation.screens.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.presentation.navigation.NavGraph
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.uiState.EventState
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

    private val _mainScreenRoute = MutableSharedFlow<EventState>()
    val mainScreenRouteSF = _mainScreenRoute.asSharedFlow()

    init {
//        authSharedPref.clearLogin()
//        authSharedPref.clearCompany()
        switchNavigation()
    }

    private fun switchNavigation() {
        viewModelScope.launch {

            delay(1500)

            when {
                authSharedPref.isLogin() && authSharedPref.isAttachedToCompany() ->
                    _mainScreenRoute.emit(EventState.RedirectGraph(NavGraph.MainGraph))

                authSharedPref.isLogin() && !authSharedPref.isAttachedToCompany() ->
                    _mainScreenRoute.emit(EventState.RedirectScreen(Screen.RegisterRole))

                else -> _mainScreenRoute.emit(EventState.RedirectScreen(Screen.Login))
            }
        }
    }
}