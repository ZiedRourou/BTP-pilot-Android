package com.example.btppilot.presentation.screens.auth.register.userRoleInCompany

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.auth.register.component.HeaderRegister
import com.example.btppilot.presentation.screens.auth.register.RegisterSharedViewModel
import com.example.btppilot.presentation.screens.auth.register.component.BottomBarRegister
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryTitleBlue
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.util.UserRole


@Composable
fun RegisterStepOneScreen(
    navController: NavController,
    registerViewModel: RegisterSharedViewModel
) {

    val userInfo by registerViewModel.registerUserInfoStateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val userRole = userInfo.selectedRole.toString()

    LaunchedEffect(Unit) {
        registerViewModel.registerUserEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)

                is EventState.RedirectScreen ->
                    if (event.screen is Screen.RegisterInviteCompany)
                        navController.navigate(event.screen.route + "/$userRole")
                    else
                        navController.navigate(event.screen.route)
                else -> {}

            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HeaderRegister(step = 2) },
        bottomBar = {
            BottomBarRegister(onClick = registerViewModel::goToCompanyInfo)
        },
    ) { padding ->

        RegisterContent(
            modifier = Modifier.padding(padding),
            selectedRole = userInfo.selectedRole,
            onRoleSelected = registerViewModel::selectRole,
        )
    }
}


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun RegisterStepOnePreview() {
    BtpPilotTheme {
        RegisterContent(
            modifier = Modifier,
            selectedRole = UserRole.COLLABORATOR,
            onRoleSelected = {},
        )
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
        color = contentColor
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp),
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

        }
    }
}
