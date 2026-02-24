package com.example.btppilot

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PilotBtpApp : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}
