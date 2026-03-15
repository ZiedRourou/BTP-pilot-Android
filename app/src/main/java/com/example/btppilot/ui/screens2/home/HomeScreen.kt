package com.example.btppilot.ui.screens2.home

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.shared.component.HeaderMainSreen
import com.example.btppilot.presentation.screens.shared.uiState.EventState


@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
) {
    val homeState by homeViewModel.homeStateFlow.collectAsState()
    val userInfo by sharedViewModel.userInfoStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val refresh by sharedViewModel.refreshProjectStateFlow.collectAsState()

    if (refresh) {
        homeViewModel.fetchProjectUser()
        sharedViewModel.resetRefreshProject()
    }

    LaunchedEffect(Unit) {

        homeViewModel.homeEventSharedFlow.collect { event ->
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
        topBar = { HeaderMainSreen(userName = userInfo.userFirstname) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = homeViewModel::redirectAddProject,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", fontSize = 25.sp, color = Color.White)
            }
        },

        ) { paddingValues ->
        HomeContent(
            paddingValues = paddingValues,
            homeState = homeState,
            onClickEditProject = homeViewModel::onClickEditProject,
            onClickViewProject = homeViewModel::onClickViewProject,
            onFilterClick = homeViewModel::filterProject,
            isAuthorizedToEdit = sharedViewModel::isAuthorizedToEditProject,
            onPriorityChange = homeViewModel::changeProjectPriority
        )
    }

}



