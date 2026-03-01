package com.example.btppilot.presentation.screens.register.component

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.presentation.component.AppButtonRegisterScaffold
import com.example.btppilot.presentation.component.AppPrimaryTitleBlue

import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.UserRole

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun RegisterStepOnePreview() {
    BtpPilotTheme {
        RegisterContent(
            selectedRole = UserRole.COLLABORATOR,
            onRoleSelected = {},
            onClickNextStep = {}
        )
    }
}



@Composable
fun RegisterContent(
    selectedRole: UserRole?,
    onRoleSelected: (UserRole) -> Unit,
    onClickNextStep: () -> Unit,

) {
    Scaffold(
        topBar = { HeaderRegister(step = 1) },
        bottomBar = {
            AppButtonRegisterScaffold(
                onClick = {onClickNextStep()}
            )
        },
        content = { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                AppPrimaryTitleBlue(text = "Sélectionnez votre rôle")

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    RoleCard(
                        title = "Chef d'entreprise",
                        description = "Gérer les projets et superviser vos équipes",
                        icon = Icons.Default.Build,
                        isSelected = selectedRole == UserRole.OWNER,
                        onClick = {
                            onRoleSelected(UserRole.OWNER)
                        }
                    )

                    RoleCard(
                        title = "Employé",
                        description = "Suivez vos tâches et mettez à jour l'avancement",
                        icon = Icons.Default.Email,
                        isSelected = selectedRole == UserRole.COLLABORATOR,
                        onClick = {
                            onRoleSelected(UserRole.COLLABORATOR)
                        }
                    )

                    RoleCard(
                        title = "Client",
                        description = "Suivez l'avancement de vos prestataires",
                        icon = Icons.Default.Person,
                        isSelected = selectedRole == UserRole.CLIENT,
                        onClick = {
                            onRoleSelected(UserRole.CLIENT)
                        }
                    )
                }
            }
        }
    )

}


@Composable
fun HeaderRegister(
    step: Int
) {
    Column {

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_pilot_btp),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Votre app ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary
                            )
                        ) {
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

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 1..3) {

                val color =
                    if (i <= step)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outlineVariant

                Box(
                    modifier = Modifier
                        .size(width = 32.dp, height = 4.dp)
                        .background(color, RoundedCornerShape(2.dp))
                )

                if (i != 3) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Étape $step sur 3",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
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
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
//        modifier = Modifier.background(contentColor),
        color = contentColor
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
            Spacer(modifier = Modifier.width(5.dp))
            Column() {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(5.dp))
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
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}










