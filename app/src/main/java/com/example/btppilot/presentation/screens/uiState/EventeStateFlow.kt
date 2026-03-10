package com.example.btppilot.presentation.screens.uiState

import com.example.btppilot.presentation.navigation.Screen


sealed class EventState {
    data class ShowMessageSnackBar(val message: String) : EventState()
    data class RedirectScreen(val screen: Screen) : EventState()
    data class RedirectScreenWithId(val route: String) : EventState()
//    data class RedirectWithCallbackScreen(val route: String) : EventState()
//    data class PopBackStackWithResult(val result: Boolean) : EventState()
}