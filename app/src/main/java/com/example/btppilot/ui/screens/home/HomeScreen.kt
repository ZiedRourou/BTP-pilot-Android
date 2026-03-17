package com.example.btppilot.ui.screens.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.ui.screens.shared.SharedViewModel
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.util.UserRole


@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
) {
    val homeState by homeViewModel.homeStateFlow.collectAsState()
    val userInfo by sharedViewModel.userInfoStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val refresh by sharedViewModel.refreshProjectStateFlow.collectAsState()

    if (refresh) {
        homeViewModel.fetchProjectUser()
        sharedViewModel.resetRefreshProject()
    }

    LaunchedEffect(Unit) {

        homeViewModel.homeEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(context.getString(event.message))

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
            if (userInfo.userRole == UserRole.OWNER)
                FloatingActionButton(
                    onClick = homeViewModel::redirectAddProject,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
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



