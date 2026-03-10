package com.example.btppilot.presentation.screens.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier,
    onlyOneLine: Boolean = true,
) {

    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = {
            supportingText?.let { Text(it) }
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            if (isPassword) {
                val icon = if (passwordVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff

                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            }
        },

        visualTransformation =
        if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,

        singleLine = onlyOneLine,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun AppTextFieldMultiline(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    supportingText: String? = null,
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = 1
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = {
            supportingText?.let { Text(it) }
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },

        minLines = minLines,
        maxLines = maxLines,

        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
    )
}



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
fun AppPrimaryTitleBlue(
    text: String,
) {

    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun AppSecondaryTitle(
    text: String,
) {

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun AppLabelTitle(
    text: String,
    color: Color = MaterialTheme.colorScheme.tertiary
) {

    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color
    )
}


