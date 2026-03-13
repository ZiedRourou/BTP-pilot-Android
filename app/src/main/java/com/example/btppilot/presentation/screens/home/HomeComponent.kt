package com.example.btppilot.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.btppilot.presentation.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryTitle
import com.example.btppilot.ui.theme.StatusDone
import com.example.btppilot.ui.theme.StatusInProgress


@Composable
fun TasksProgressBar(
    progress: Int,
    maxProgress: Int,
    modifier: Modifier = Modifier
) {

    val progressValue =
        if (maxProgress == 0) 0f else progress.toFloat() / maxProgress

    LinearProgressIndicator(
        progress = { progressValue },
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(50)),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
    )
}

@Composable
fun CurrentDate(
    dateString: String
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Event,
            contentDescription = null,
            tint = StatusInProgress,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))

        AppSecondaryTitle(
            text = dateString,
            color = StatusInProgress
        )

    }
}
@Composable
fun StatCard(
    title: String,
    icon: ImageVector,
    stat: Int,
    modifier: Modifier = Modifier,
    tint : androidx.compose.ui.graphics.Color = StatusDone
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        AppSecondaryTitle(
            text = title,
            color = MaterialTheme.colorScheme.background
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )

            AppPrimaryTitle(
                text = stat.toString(),
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}


@Composable
fun FilterButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onClick,
        modifier = modifier.width(120.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor =
            if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.8F)
            else MaterialTheme.colorScheme.secondary.copy(alpha = 0.8F),

            contentColor = MaterialTheme.colorScheme.background
        )
    ) {

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,

        )
    }
}