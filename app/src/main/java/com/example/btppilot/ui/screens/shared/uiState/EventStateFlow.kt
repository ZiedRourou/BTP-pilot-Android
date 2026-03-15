package com.example.btppilot.ui.screens.shared.uiState

import com.example.btppilot.ui.navigation.NavGraph
import com.example.btppilot.ui.navigation.Screen


sealed class EventState {
    data class ShowMessageSnackBar(val message: String) : EventState()
    data class RedirectScreen(val screen: Screen) : EventState()
    data class RedirectScreenWithId(val route: String) : EventState()
    data class RedirectGraph(val graph: NavGraph) : EventState()
    data class PopBackStackWithRefresh(val popBack : Unit) : EventState()

}