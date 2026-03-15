package com.example.btppilot.ui.screens2.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.btppilot.presentation.navigation.BottomNavigationBar
import com.example.btppilot.presentation.navigation.MainNavHost
import com.example.btppilot.presentation.screens.shared.SharedViewModel

@Composable
fun MainScreen(
    rootNavController: NavHostController
) {

    val sharedViewModel: SharedViewModel = hiltViewModel()
    val userInfoState by sharedViewModel.userInfoStateFlow.collectAsState()
    val mainNavController = rememberNavController()

    Scaffold(

        bottomBar = {
            BottomNavigationBar(
                navController = mainNavController,
                role = userInfoState.userRole
            )
        },

    ) { padding ->

        Column(modifier = androidx.compose.ui.Modifier.padding(padding)) {
            MainNavHost(
                rootNavController = rootNavController,
                navController = mainNavController,
            )
        }
    }

}