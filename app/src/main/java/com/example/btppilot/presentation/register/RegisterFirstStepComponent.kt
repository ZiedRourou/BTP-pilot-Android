package com.example.btppilot.presentation.register

import com.example.btppilot.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.btppilot.ui.theme.BtpPilotTheme

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun RegisterStepOnePreview() {
    BtpPilotTheme {
        RegisterFirstStep()
    }
}

@Composable
fun StepRegister(
    step: Int,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row (){
            for (i in 1..3) {
                val color = if (i <= step)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outlineVariant

                Box(
                    modifier = Modifier
                        .size(width = 32.dp, height = 4.dp)
                        .background(color, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.width(5.dp))
            }
        }

        Text(
            text = "Etape $step sur 3",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun HeaderRegister(

) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_pilot_btp),
            contentDescription = "Logo",
            modifier = Modifier.size(60.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Votre app ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Gestionnaire")
                    }
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "de chantier",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(5.dp),
                    tint = Color.Black
                )

            Column() {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = " ",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun RegisterFirstStep() {

    var selectedRole by remember { mutableStateOf("Owner") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        HeaderRegister()

        StepRegister(1)

        Text(
            text = "Sélectionner votre role",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Sélectionnez votre role au seins de votre entreprise.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )


        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            RoleCard(
                title = "Chef d'entreprise",
                description = "Gérer les projets, et superviser vos équipes",
                icon = Icons.Default.Build,
                isSelected = selectedRole == "Owner",
                onClick = { selectedRole = "Owner" }
            )
            RoleCard(
                title = "Employé",
                description = "Suivez vos tâches et mettez à jour l'avancement du projet en deux clics",
                icon = Icons.Default.Email,
                isSelected = selectedRole == "Employee",
                onClick = { selectedRole = "Employee" }
            )
            RoleCard(
                title = "Client",
                description = "Suivez l'avancement de vos prestataire en un coup d'oeil",
                icon = Icons.Default.Person,
                isSelected = selectedRole == "Client",
                onClick = { selectedRole = "Client" }
            )
        }

        Button(
            onClick = {

            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(10.dp)
        ) {
            Text(
                "Suivant",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}






