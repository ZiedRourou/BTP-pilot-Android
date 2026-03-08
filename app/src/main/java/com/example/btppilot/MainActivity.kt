package com.example.btppilot

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.btppilot.presentation.navigation.NavGraphApp
import com.example.btppilot.presentation.navigation.Screen

import com.example.btppilot.ui.theme.BtpPilotTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
        @RequiresApi(Build.VERSION_CODES.R)
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContent {
//            BtpPilotTheme {
//                NavGraphApp()
//            }
//        }

            setContent {

                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()

                val currentRoute = backStackEntry?.destination?.route

                val window = this.window

                LaunchedEffect(currentRoute) {

                    if (currentRoute == Screen.Splash.route) {

                        WindowCompat.setDecorFitsSystemWindows(window, false)
                        window.insetsController?.hide(
                            WindowInsets.Type.systemBars()
                        )

                    } else {

                        window.insetsController?.show(
                            WindowInsets.Type.systemBars()
                        )
                        WindowCompat.setDecorFitsSystemWindows(window, true)
                    }
                }
                BtpPilotTheme{

                    NavGraphApp(navController)
                }
            }

    }


}

