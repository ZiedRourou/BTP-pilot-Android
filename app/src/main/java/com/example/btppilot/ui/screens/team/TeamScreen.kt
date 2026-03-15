package com.example.btppilot.ui.screens.team

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.btppilot.ui.screens.shared.SharedViewModel
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens.shared.uiState.EventState

@Composable
fun TeamScreen(
    navController: NavController,
    teamViewModel: TeamViewModel,
    sharedViewModel: SharedViewModel,
) {
    val state by teamViewModel.teamStateFlow.collectAsState()
    val userInfo by sharedViewModel.userInfoStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        teamViewModel.teamEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route)

                is EventState.RedirectScreenWithId ->
                    navController.navigate(event.route)
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HeaderMainSreen(userName = userInfo.userFirstname) },
    ) { paddingValues ->

        TeamContent(
            paddingValues ,
            userListState = state,
            onRoleChange = teamViewModel::onRoleChange,
            inviteUser = teamViewModel::inviteUser,
            onEmailChange = teamViewModel::onEmailChange
        )
    }

}
