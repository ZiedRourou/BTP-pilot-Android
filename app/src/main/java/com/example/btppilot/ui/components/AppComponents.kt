package com.example.btppilot.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    supportingText: String? = null,
    leadingIcon: Painter? = null,
    isPassword: Boolean = false
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = {
            supportingText?.let {
                Text(it)
            }
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = null
                )
            }
        },
        visualTransformation =
        if (isPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(10.dp)
    ) {
        Text(text)
    }
}