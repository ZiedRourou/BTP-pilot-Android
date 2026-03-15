package com.example.btppilot.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NewTaskListScreenPreview() {


    BtpPilotTheme {

        Scaffold(
            topBar = { HeaderMainSreen(userName = "userInfo.userFirstname") },
        ) { paddingValues ->


            ProfileContent(paddingValues = paddingValues) {
            }
        }

    }
}


@Composable
fun ProfileContent(
    paddingValues: PaddingValues,
    logout: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(25.dp)
            .fillMaxSize()
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Outlined.PrivacyTip, null)
                Text("Configurer la collecte de vos données")
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Outlined.Flag, null)
                Text("Configurer la collecte de vos données")
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Outlined.Password, null)
                Text("Modifier vos informations")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = logout,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(10.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Se déconnecter",
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
