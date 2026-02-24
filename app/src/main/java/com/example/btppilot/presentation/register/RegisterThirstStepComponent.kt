package com.example.btppilot.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.ui.theme.BtpPilotTheme

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun Register3Preview() {
    BtpPilotTheme {
//        RegisterThirstStep()
        RegisterThirstStepNotOwner()
    }
}

@Composable
fun RegisterThirstStep() {
    var companyName by remember { mutableStateOf("") }
    var siret by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        HeaderRegister()


       StepRegister(step = 3)


        Text(
            text = "Votre entreprise",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )



        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = {
                Text(
                    "Nom de votre entreprise",
                    color = MaterialTheme.colorScheme.tertiary
                )
            },

            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = siret,
            onValueChange = { siret = it },
            label = {
                Text(
                    "Siret",
                    color = MaterialTheme.colorScheme.tertiary
                )
            },

            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )


        Button(
            onClick = {},
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(10.dp)
        ) {
            Text(
                "Valider",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
fun RegisterThirstStepNotOwner(){
    var emailCompany by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        HeaderRegister()
        StepRegister(step = 3)


        OutlinedTextField(
            value = emailCompany,
            onValueChange = { emailCompany = it },
            label = {
                Text(
                    "Email de votre entreprise",
                    color = MaterialTheme.colorScheme.tertiary
                )
            },

            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )


        Button(
            onClick = {},
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(10.dp)
        ) {
            Text(
                "Valider",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}
