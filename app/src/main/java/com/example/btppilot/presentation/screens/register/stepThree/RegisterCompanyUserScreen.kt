package com.example.btppilot.presentation.screens.register.stepThree

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.presentation.screens.navigation.Screen
import com.example.btppilot.presentation.screens.register.RegisterViewModel
import com.example.btppilot.presentation.screens.register.component.HeaderRegister
//
//@Composable
//fun RegisterThirdStepNotOwnerScreen(
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
//                RegisterViewModel.RegisterEvent.ShowUserInfoError -> {
//                    snackbarHostState.showSnackbar(
//                        "Veuillez entrer l'email de l'entreprise"
//                    )
//                }
//
//                else -> {}
//            }
//        }
//    }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { padding ->
//        Box(modifier = Modifier.padding(padding)){
//            RegisterThirdStepNotOwnerContent(
//                companyEmail = state.companyEmail,
//                onEmailChange = registerViewModel::updateCompanyInvite,
//                onSubmit = registerViewModel::submitRegister
//            )
//        }
//
//    }
//}
@Composable
fun RegisterThirdStepNotOwnerContent(
    companyEmail: String,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit
) {

    Scaffold(
        topBar = { HeaderRegister(3) },

        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = onSubmit,
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
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            OutlinedTextField(
                value = companyEmail,
                onValueChange = onEmailChange,
                label = { Text("Email de votre entreprise") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}