package com.example.btppilot.presentation.screens.shared.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppPrimaryTitle(
    text: String,
    color: Color = MaterialTheme.colorScheme.primary

) {

    Text(
        text = text,
        style = MaterialTheme.typography.headlineLarge,
        color = color
    )
}

@Composable
fun AppTitleDescription(
    text: String,
) {

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun AppSecondaryTitle(
    text: String,
    color: Color = MaterialTheme.colorScheme.tertiary
) {

    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color
    )
}
