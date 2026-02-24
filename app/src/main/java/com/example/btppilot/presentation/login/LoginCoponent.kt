package com.example.btppilot.presentation.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btppilot.R

import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun GreetingPreview() {
    BtpPilotTheme {
        LoginContent()
    }
}

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = viewModel()

    LoginContent{ email, password ->
        viewModel.login(email, password)
    }
}


@Composable
fun LoginContent(
    onLoginClick: (String, String) -> Unit = { _, _ -> }
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(R.drawable.logo_pilot_btp),
            contentDescription = "",
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "PilotBtp",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Votre chantier entre de bonne main",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Connexion",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            isError = emailError,
            supportingText = {
                if (emailError) {
                    Text(
                        "Entre un email valide",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            label = {
                Text(
                    text = "Email",
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            leadingIcon = {
                Icon(
                    painterResource(android.R.drawable.ic_dialog_email),
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    "Mot de passe",
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            leadingIcon = {
                Icon(
                    painterResource(android.R.drawable.ic_lock_idle_lock),
                    contentDescription = ""
                )
            },

            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                "Mot de passe oublié ?",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onLoginClick(email,password)
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(10.dp)
        ) {
            Text(
                "Connexion",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.background
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Surface(
            shape = RoundedCornerShape(50),
        ) {
            Text(
                "Nouveau ici ? Je m'inscris",
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}
