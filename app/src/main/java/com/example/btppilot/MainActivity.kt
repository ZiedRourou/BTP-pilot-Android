package com.example.btppilot

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
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
                RootNavGraph(authNavController = rememberNavController())
            }
        }
    }
}

