package com.example.btppilot.ui.screens.auth.register.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp


@Composable
fun BottomBarRegister(
    text: String = "Suivant",
    onClick: () -> Unit
) {
    Button(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .padding(30.dp)
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .height(50.dp)
            .shadow(10.dp)
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.background
        )
    }
}