package com.example.btppilot.ui.screens.auth.register.userRoleInCompany

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.ui.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.ui.screens.auth.register.component.HeaderRegister
import com.example.btppilot.ui.screens.shared.component.AppPrimaryTitle
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.UserRole


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun RegisterUserRolePreview() {
    BtpPilotTheme {
        Scaffold(
            topBar = { HeaderRegister(step = 2) },
            bottomBar = {
                BottomBarRegister(onClick = { })
            },
        ) { padding ->

            RegisterContent(
                modifier = Modifier.padding(padding),
                selectedRole = UserRole.COLLABORATOR,
                onRoleSelected = {},
            )
        }
    }
}


@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    selectedRole: UserRole?,
    onRoleSelected: (UserRole) -> Unit,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        AppPrimaryTitle(
            text = "Sélectionnez votre rôle",
            color = MaterialTheme.colorScheme.secondary,
            textStyle = MaterialTheme.typography.headlineMedium
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            RoleCard(
                title = "Chef d’entreprise",
                description =
                "Créez et pilotez vos projets, coordonnez vos équipes et suivez l’avancement des chantiers",
                icon = Icons.Default.Build,
                isSelected = selectedRole == UserRole.OWNER,
                onClick = {
                    onRoleSelected(UserRole.OWNER)
                }
            )

            RoleCard(
                title = "Employé",
                description =
                "Consultez vos tâches, mettez à jour leur progression et collaborez sur les projets",
                icon = Icons.Default.Email,
                isSelected = selectedRole == UserRole.COLLABORATOR,
                onClick = {
                    onRoleSelected(UserRole.COLLABORATOR)
                }
            )

            RoleCard(
                title = "Client",
                description =
                "Suivez l’avancement des travaux et échangez avec votre prestataire",
                icon = Icons.Default.Person,
                isSelected = selectedRole == UserRole.CLIENT,
                onClick = {
                    onRoleSelected(UserRole.CLIENT)
                }
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
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, borderColor),
        color = contentColor,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(5.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(10.dp))
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
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
