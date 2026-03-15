package com.example.btppilot.ui.screens.shared.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp


@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(10.dp)
    ) {
        Text(
            text,
            color = MaterialTheme.colorScheme.background,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
