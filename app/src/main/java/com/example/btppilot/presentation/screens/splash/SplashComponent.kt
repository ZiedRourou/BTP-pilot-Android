package com.example.btppilot.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.R
import com.example.btppilot.presentation.component.AppPrimaryTitleYellow
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun SplashPreview() {
    BtpPilotTheme {
        SplashContent()
    }
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
        AppPrimaryTitleYellow(text = "BtpPilot")

    }
}
