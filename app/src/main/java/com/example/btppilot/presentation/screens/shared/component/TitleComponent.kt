package com.example.btppilot.presentation.screens.shared.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.time.format.TextStyle

@Composable
fun AppPrimaryTitle(
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineLarge
) {
    Text(
        text = text,
        style = textStyle,
        color = color
    )
}

@Composable
fun AppTitleDescription(
    text: String,
    color: Color = MaterialTheme.colorScheme.tertiary,

    ) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color
    )
}

@Composable
fun AppSecondaryTitle(
    text: String,
    color: Color = MaterialTheme.colorScheme.tertiary
) {

    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = color
    )
}
