package com.example.btppilot.presentation.screens.register.stepThree

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.navigation.Screen
import com.example.btppilot.presentation.screens.register.RegisterViewModel
import com.example.btppilot.presentation.screens.register.component.HeaderRegister
//
//@Composable
//fun RegisterThirdStepOwnerScreen(
//    navController: NavController,
//    registerViewModel: RegisterViewModel
//) {
//
//    val state by registerViewModel.registerInfo.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    LaunchedEffect(Unit) {
//        registerViewModel.registerEvent.collect { event ->
//
//            when (event) {
//
//                RegisterViewModel.RegisterEvent.RegisterSuccess -> {
//                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.RegisterGraph.route) {
//                            inclusive = true
//                        }
//                    }
//                }
//
//                else -> {}
//            }
//        }
//    }
//
//    RegisterThirdStepOwnerContent(
//        companyName = state.companyName,
//        siret = state.siret,
//        activity = state.activity,
//        onChange = registerViewModel::updateOwnerCompany,
//        onSubmit = registerViewModel::submitRegister
//    )
//}


@Composable
fun RegisterThirdStepOwnerContent(
    companyName: String,
    siret: String,
    activity: String,
    onChange: (String, String, String) -> Unit,
    onSubmit: () -> Unit
) {

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                HeaderRegister(3)
            }
        },

        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = onSubmit,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Valider")
                }
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Filled.BusinessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "Les informations de votre entreprise",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(16.dp))

                // ✅ COMPANY NAME
                OutlinedTextField(
                    value = companyName,
                    onValueChange = {
                        onChange(it, siret, activity)
                    },
                    label = { Text("Nom de votre entreprise") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // ✅ SIRET
                OutlinedTextField(
                    value = siret,
                    onValueChange = {
                        onChange(companyName, it, activity)
                    },
                    label = { Text("Siret") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // ✅ ACTIVITY
                OutlinedTextField(
                    value = activity,
                    onValueChange = {
                        onChange(companyName, siret, it)
                    },
                    label = { Text("Secteur d'activité") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color.Gray),
                color = MaterialTheme.colorScheme.surface
            ) {

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.AdminPanelSettings,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Vous allez devenir administrateur de votre entreprise",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}