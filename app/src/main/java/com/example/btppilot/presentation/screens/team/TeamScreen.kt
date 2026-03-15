package com.example.btppilot.presentation.screens.team

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.project.component.AppFieldProject
import com.example.btppilot.presentation.screens.shared.component.HeaderMainSreen
import com.example.btppilot.presentation.screens.task.taskList.TaskListContent
import com.example.btppilot.presentation.screens.uiState.EventState

@Composable
fun TeamScreen(
    navController: NavController,
    teamViewModel: TeamViewModel,
    sharedViewModel: SharedViewModel,
    snackbarHostState: SnackbarHostState
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
