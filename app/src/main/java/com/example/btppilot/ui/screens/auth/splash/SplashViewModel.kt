package com.example.btppilot.ui.screens.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.ui.navigation.NavGraph
import com.example.btppilot.ui.navigation.Screen
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.data.local.AuthSharedPref
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
        switchNavigation()
    }

    private fun switchNavigation() {
        viewModelScope.launch {

            delay(3000)

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