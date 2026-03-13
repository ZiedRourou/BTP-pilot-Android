package com.example.btppilot.presentation.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.btppilot.presentation.navigation.BottomNavigationBar
import com.example.btppilot.presentation.navigation.MainNavHost
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.home.HeaderScaffoldHome
import com.example.btppilot.util.UserRole

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