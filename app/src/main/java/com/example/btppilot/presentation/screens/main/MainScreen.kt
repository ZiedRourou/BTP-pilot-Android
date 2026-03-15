package com.example.btppilot.presentation.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.btppilot.presentation.navigation.BottomNavigationBar
import com.example.btppilot.presentation.navigation.MainNavHost

@Composable
fun MainScreen(
    rootNavController: NavHostController
) {

    val mainViewModel: MainViewModel = hiltViewModel()
    val state by mainViewModel.mainStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val mainNavController = rememberNavController()

    Scaffold(

        bottomBar = {
            BottomNavigationBar(
                navController = mainNavController,
                role = state.role
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { padding ->

        Column(modifier = androidx.compose.ui.Modifier.padding(padding)) {
            MainNavHost(
                rootNavController = rootNavController,
                navController = mainNavController,
                snackbarHostState = snackbarHostState
            )
        }
    }

}