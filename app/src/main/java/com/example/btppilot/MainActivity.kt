package com.example.btppilot

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.compose.rememberNavController
import com.example.btppilot.presentation.navigation.RootNavGraph

import com.example.btppilot.ui.theme.BtpPilotTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

       
        setContent {
            BtpPilotTheme {
                RootNavGraph(rootNavController = rememberNavController())
            }
        }
    }
}

