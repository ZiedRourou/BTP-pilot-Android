package com.example.btppilot.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btppilot.R
import com.example.btppilot.ui.components.AppLabelTitle
import com.example.btppilot.ui.components.AppPrimaryButton
import com.example.btppilot.ui.components.AppPrimaryTitle
import com.example.btppilot.ui.components.AppSecondaryTitle
import com.example.btppilot.ui.components.AppTextField
import com.example.btppilot.ui.theme.BtpPilotTheme


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun LoginPreview() {
    BtpPilotTheme {
//        LoginContent()
    }
}


@Composable
fun LoginContent(
    modifier: Modifier,
    onLoginClick: (String, String) -> Unit = { _, _ -> }
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(48.dp))

        Image(
            painter = painterResource(R.drawable.logo_pilot_btp),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppPrimaryTitle(text = "PilotBtp")
        AppSecondaryTitle(text = "Votre chantier entre de bonne main")

        Spacer(modifier = Modifier.height(40.dp))

        AppLabelTitle(text = "Connexion")

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            isError = emailError,
            supportingText = if (emailError) "Entre un email valide" else null,
            leadingIcon = Icons.Filled.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppTextField(
            value = password,
            onValueChange = { password = it },
            label = "Mot de passe",
            isError = passwordError,
            isPassword = true,
            leadingIcon = Icons.Filled.Lock
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                "Mot de passe oublié ?",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        AppPrimaryButton(
            text = "Connexion",
            onClick = { onLoginClick(email, password) }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                "Nouveau ici ? Je m'inscris",
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 10.dp
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}