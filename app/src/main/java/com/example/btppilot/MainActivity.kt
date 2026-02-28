package com.example.btppilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.btppilot.presentation.navigation.NavGraphApp

import com.example.btppilot.ui.theme.BtpPilotTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BtpPilotTheme {
                NavGraphApp()
            }
        }
    }


}

