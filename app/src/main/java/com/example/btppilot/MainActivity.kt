package com.example.btppilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.btppilot.presentation.login.LoginScreen
import com.example.btppilot.presentation.splash.SplashScreen

import com.example.btppilot.ui.theme.BtpPilotTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BtpPilotTheme {
//                SplashScreen()
                LoginScreen()
            }
        }
    }


}

