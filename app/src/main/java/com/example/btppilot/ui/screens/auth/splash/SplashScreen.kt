package com.example.btppilot.ui.screens.auth.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.R
import com.example.btppilot.ui.navigation.NavGraph
import com.example.btppilot.ui.navigation.Screen
import com.example.btppilot.ui.screens.shared.component.AppPrimaryTitle
import com.example.btppilot.ui.screens.shared.eventState.EventState


@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel
) {

    LaunchedEffect(Unit) {
        splashViewModel.mainScreenRouteSF.collect { event ->
            when (event) {
                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                is EventState.RedirectGraph ->
                    navController.navigate(event.graph.route) {
                        popUpTo(NavGraph.AuthGraph.route){inclusive= true}
                    }

                else -> {}
            }
        }
    }
    SplashContent()
}


@Composable
fun SplashContent(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_pilot_btp),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
        )
        Spacer(modifier = Modifier.height(40.dp))
        AppPrimaryTitle(text = "BtpPilot")

    }
}
