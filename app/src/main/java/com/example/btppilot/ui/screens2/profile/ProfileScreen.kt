package com.example.btppilot.ui.screens2.profile

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.shared.component.HeaderMainSreen
import com.example.btppilot.presentation.screens.shared.uiState.EventState

@Composable
fun ProfileScreen(
    rootNavController: NavController,
    profileViewModel: ProfileViewModel,
    sharedViewModel: SharedViewModel,
) {

    val userInfo by sharedViewModel.userInfoStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.profileEventSharedFlow.collect() { event ->
            if (event is EventState.RedirectScreen)
                rootNavController.navigate(Screen.Login.route){
                    popUpTo(0)
                }

        }
    }

    Scaffold(
        topBar = {HeaderMainSreen(userName = userInfo.userFirstname)},

    ) {padding ->

        ProfileContent(
            padding,
            logout = profileViewModel::logout
        )
    }
}

