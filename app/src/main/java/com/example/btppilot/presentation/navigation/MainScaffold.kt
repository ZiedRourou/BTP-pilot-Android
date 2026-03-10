package com.example.btppilot.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.btppilot.presentation.screens.home.HeaderScaffoldHome
import com.example.btppilot.util.UserRole

@Composable
fun MainScaffold(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    content: @Composable (PaddingValues, SnackbarHostState) -> Unit
) {

    val state by mainViewModel.mainStateFlow.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(

        topBar = {
            HeaderScaffoldHome(userName = state.firstname)
        },
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = homeViewModel::redirectAddProject,
//                containerColor = MaterialTheme.colorScheme.primary
//            ) {
//                Text("+", fontSize = 25.sp, color = Color.White)
//            }
//        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                role = state.role
            )
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { padding ->

        content(padding, snackbarHostState)

    }
}