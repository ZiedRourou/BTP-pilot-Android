package com.example.btppilot.ui.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.btppilot.ui.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.ui.screens.shared.component.AppPrimaryTitle
import com.example.btppilot.ui.theme.StatusDone
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.arrayProjectStatus


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
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))

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

@Composable
fun PriorityBadgeDropdown(
    enableEdit: Boolean = false,
    selected: ProjectStatus,
    onSelect: (ProjectStatus) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Surface(
        onClick = { if (enableEdit) expanded = true },
        shape = RoundedCornerShape(50),
        color = selected.color.copy(alpha = 0.18f),
        modifier = Modifier
            .wrapContentWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = selected.label,
                style = MaterialTheme.typography.labelMedium,
                color = selected.color,
            )

            Spacer(Modifier.width(4.dp))

            if (enableEdit)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = selected.color,
                    modifier = Modifier.size(18.dp)
                )
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary)
    ) {

        arrayProjectStatus.filter { it.label != selected.label }.forEach { priority ->

            DropdownMenuItem(
                text = {
                    Text(
                        priority.label,
                        color = priority.color,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                onClick = {
                    onSelect(priority)
                    expanded = false
                }
            )
        }
    }
}