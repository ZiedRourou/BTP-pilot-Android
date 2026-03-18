package com.example.btppilot.ui.screens.shared.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


@Composable
fun AppTextFieldMultiline(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    supportingText: Int? = null,
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
            supportingText?.let { Text(stringResource(it)) }
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
fun AppPrimaryTitleBlue(
    text: String,
) {

    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.secondary
    )
}




