package com.example.btppilot.presentation.screens.project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btppilot.R
//import com.example.btppilot.presentation.screens.home.ContentHome
//import com.example.btppilot.presentation.screens.home.FilterButtons
//import com.example.btppilot.presentation.screens.home.GenericProgressBar
//import com.example.btppilot.presentation.screens.home.HeaderScaffoldHome
//import com.example.btppilot.presentation.screens.home.ItemHome
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.Primary
import com.example.btppilot.ui.theme.Secondary
import com.example.btppilot.ui.theme.StatusDone
import com.example.btppilot.ui.theme.StatusInProgress

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun ProjectPreview() {
    BtpPilotTheme {
        TestScreen()
    }
}

@Composable
fun TestScreen() {
//    ItemHome()
//    HeaderScaffoldHome()
//    ContentHome()
    ProjectScreen()
}

@Composable
fun ProjectScreen() {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
//                HeaderScaffoldHome()
            }
        },


        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                contentTask()
            }
        },
        bottomBar = {
            //nav bar plus tard
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    text = "+",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
    )

}


@Composable
fun contentTask() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(text = "Taches : ", style = MaterialTheme.typography.labelMedium)

            Button(
                onClick = { },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StatusInProgress.copy(alpha = 0.4f),
                    contentColor = Color.White
                )
            ) {
                Text("Total", style = MaterialTheme.typography.bodyLarge)
            }
            Button(
                onClick = { },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Secondary.copy(alpha = 0.4f),

                    contentColor =
                    Color.White
                )
            ) {
                Text("test", style = MaterialTheme.typography.bodyLarge)
            }
            Button(
                onClick = { },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary.copy(alpha = 0.4f),
                    contentColor = Color.White
                ),
            ) {
                Text("test", style = MaterialTheme.typography.bodyLarge)
            }
            Button(
                onClick = { },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(
                    containerColor = StatusDone.copy(alpha = 0.4f),

                    contentColor =
                    Color.White
                )
            ) {
                Text("test", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Divider(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth(),
            color = Color.Black
        )
//        ItemHome()
        ItemProject()
    }
}

@Composable
fun ItemProject() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.Gray),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(
                    text = "Priorité : Haute",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error, RoundedCornerShape(10.dp))
                        .padding(5.dp)
                )
                Text(
                    text = "A faire le 25 octobre ",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error, RoundedCornerShape(10.dp))
                        .padding(5.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Residence Prado",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Residence Prado blablablablablablablablablablablablablablablablablablablablablablablablablablablabla",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }


}