package com.example.btppilot.presentation.home

import android.util.Patterns
import android.widget.Button
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btppilot.R
import com.example.btppilot.presentation.common.AppScaffold
import com.example.btppilot.presentation.login.LoginViewModel
import com.example.btppilot.presentation.register.HeaderRegister
import com.example.btppilot.presentation.register.RoleCard
import com.example.btppilot.presentation.register.StepRegister
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.PriorityHigh
import com.example.btppilot.ui.theme.PriorityLow
import com.example.btppilot.ui.theme.PriorityMedium
import com.example.btppilot.ui.theme.PriorityUrgent
import com.example.btppilot.ui.theme.StatusDelayed
import com.example.btppilot.ui.theme.StatusDone
import com.example.btppilot.ui.theme.StatusInProgress
enum class HomeFilter { ALL, LATE, IN_PROGRESS }

enum class ChantierStatus { LATE, IN_PROGRESS, DONE }

data class HomeStats(
    val activeChantiers: Int,
    val lateTasks: Int
)

data class HomeItemUi(
    val status: ChantierStatus,
    val title: String,
    val startDate: String,
    val endDate: String,
    val progress: Int,        // 0..100
    val tasksDone: Int,
    val tasksTotal: Int,
    val peopleCount: Int,
    val priorityLabel: String // ex: "HAUTE", "MOYENNE"
)

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun GreetingPreview() {
    BtpPilotTheme {
        TestScreen()
    }
}

@Composable
fun TestScreen() {

    HomeScreen()
}

@Composable
fun HomeScreen() {
    var selectedFilter by remember { mutableStateOf(HomeFilter.ALL) }

    val stats = HomeStats(activeChantiers = 7, lateTasks = 7)

    val list = List(20) {
        HomeItemUi(
            status = ChantierStatus.LATE,
            title = "Residence Prado",
            startDate = "10/02/2025",
            endDate = "18/02/2025",
            progress = 65,
            tasksDone = 15,
            tasksTotal = 20,
            peopleCount = 4,
            priorityLabel = "HAUTE"
        )
    }

    AppScaffold(


        topBar = { HeaderScaffoldHome(userName = "zizou") },
        onTabClick = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* action */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", fontSize = 25.sp, color = Color.White)
            }
        },

    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            ContentHome(
                dateLabel = "Mardi 15 janvier",
                stats = stats,
                selectedFilter = selectedFilter,
                onFilterChange = {
                    selectedFilter = it
                                 },
                items = list
            )
        }
    }
}

@Composable
fun HeaderScaffoldHome(userName: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_pilot_btp),
            contentDescription = null,
            Modifier
                .clip(CircleShape)
                .size(60.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(text = "Salut !")
            Text(text = userName, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun HomeStatsCard(stats: HomeStats) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .background(
                MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceAround
    ) {

        Column(Modifier.padding(10.dp)) {
            Text(
                text = "Chantier Actifs",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.background
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = StatusDone,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stats.activeChantiers.toString(),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        Divider(
            modifier = Modifier
                .height(70.dp)
                .width(1.dp),
            color = Color.Gray.copy(alpha = 0.4f)
        )

        Column(Modifier.padding(10.dp)) {
            Text(
                text = "Taches en retards",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.background
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.WarningAmber,
                    contentDescription = null,
                    tint = StatusDelayed,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stats.lateTasks.toString(),
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}

@Composable
fun FilterButtons(
    selected: HomeFilter,
    onSelectedChange: (HomeFilter) -> Unit
) {
    val items = listOf(
        HomeFilter.ALL to "Tous",
        HomeFilter.LATE to "En retard",
        HomeFilter.IN_PROGRESS to "En cours"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (filter, title) ->
            val isSelected = filter == selected

            Button(
                onClick = { onSelectedChange(filter) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = if (isSelected) Color.White
                    else MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun ItemHome(item: HomeItemUi) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.Gray),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {

            val statusText = when (item.status) {
                ChantierStatus.LATE -> "EN RETARD"
                ChantierStatus.IN_PROGRESS -> "EN COURS"
                ChantierStatus.DONE -> "TERMINE"
            }


            val statusBg = when (item.status) {
                ChantierStatus.LATE -> StatusDelayed
                ChantierStatus.IN_PROGRESS -> StatusInProgress
                ChantierStatus.DONE -> StatusDone
            }

            val priorityBg = when (item.priorityLabel.uppercase()) {
                "LOW", "BASSE" -> PriorityLow
                "MEDIUM", "MOYENNE" -> PriorityMedium
                "HIGH", "HAUTE" -> PriorityHigh
                "URGENT" -> PriorityUrgent
                else -> MaterialTheme.colorScheme.tertiary
            }

            Text(
                text = statusText,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .background(statusBg, RoundedCornerShape(10.dp))
                    .padding(5.dp),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = item.title, style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Event,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(item.startDate, color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.width(5.dp))
                Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(item.endDate, color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Progression des taches", color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.labelSmall)
                Text("${item.progress}%", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(8.dp))

            GenericProgressBar(
                progress = ((item.tasksDone.toFloat() / item.tasksTotal) * 100).toInt(),
                maxProgress = 100,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Task, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text("${item.tasksDone}/${item.tasksTotal}", color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.labelSmall)
                }

                Text(
                    text = item.priorityLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .background(priorityBg, RoundedCornerShape(5.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.SupervisorAccount, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(item.peopleCount.toString(), color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}


@Composable
fun ContentHome(
    dateLabel: String,
    stats: HomeStats,
    selectedFilter: HomeFilter,
    onFilterChange: (HomeFilter) -> Unit,
    items: List<HomeItemUi>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Event, null, tint = StatusInProgress, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(10.dp))
            Text(dateLabel, style = MaterialTheme.typography.labelSmall, color = StatusInProgress)
        }

        HomeStatsCard(stats)

        Text(text = "Mes chantiers", style = MaterialTheme.typography.labelLarge)

        Spacer(modifier = Modifier.height(8.dp))

        FilterButtons(
            selected = selectedFilter,
            onSelectedChange = onFilterChange
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                ItemHome(item)
            }
        }
    }
}


@Composable
fun TestRowText() {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                HeaderScaffoldHome()
            }
        },


        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ContentHome()
            }
        },
        bottomBar = {
            //nav bar plus tard
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    text = "+",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.background
                )
            }
        },
    )

}


@Composable
fun HeaderScaffoldHome() {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_pilot_btp),
            contentDescription = null,
            Modifier
                .clip(CircleShape)
                .size(60.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column {
            Text(text = "Salut")
            Text(text = "zizou ", style = MaterialTheme.typography.headlineSmall)
        }
    }
}


@Composable
fun ContentHome() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.Event,
                contentDescription = null,
                tint = StatusInProgress,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Mardi 15 janvier",
                style = MaterialTheme.typography.labelSmall,
                color = StatusInProgress
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp))
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {

            Column(
                Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Chantier Actifs",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.background
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = StatusDone,
                        modifier = Modifier.size(25.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = "7",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .height(70.dp)
                    .width(1.dp),
                color = Color.Gray.copy(alpha = 0.4f)
            )

            Column(
                Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Taches en retards",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.background
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Filled.WarningAmber,
                        contentDescription = null,
                        tint = StatusDelayed,
                        modifier = Modifier.size(25.dp)
                    )

                    Text(
                        text = "7",
                        color = MaterialTheme.colorScheme.background,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
        Row {
            Text(text = "Mes chantiers", style = MaterialTheme.typography.labelLarge)
        }

        FilterButtons()
        Spacer(modifier = Modifier.height(5.dp))
        val chantierList = List(20) { it }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chantierList) {
                ItemHome()
            }
        }

    }
}

@Composable
fun FilterButtons() {

    var selectedIndex by remember { mutableStateOf(0) }

    val items = listOf("Tous", "En retard", "En cours")

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        items.forEachIndexed { index, title ->

            val isSelected = index == selectedIndex

            Button(
                onClick = { selectedIndex = index },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                    if (isSelected)
                        MaterialTheme.colorScheme.primary // jaune actif
                    else
                        MaterialTheme.colorScheme.secondaryContainer,

                    contentColor =
                    if (isSelected)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun ItemHome() {
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(2.dp, Color.Gray),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "EN RETARD",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.error, RoundedCornerShape(10.dp))
                    .padding(5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Residence Prado",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Event,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "10/02/2025",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(5.dp))

                Image(
                    painter = painterResource(id = R.drawable.arrow),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = "18/02/2025",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(5.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Progression des taches",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = "65%",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                GenericProgressBar(
                    progress = 2,
                    maxProgress = 10,
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    textColor = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Task,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "15/20",
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Text(
                        text = "EN RETARD",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(5.dp)
                            )
                            .padding(5.dp)
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Filled.SupervisorAccount,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "15/20",
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                }
            }
        }
    }

}

@Composable
fun GenericProgressBar(
    progress: Int,
    maxProgress: Int,
    label: String = "$progress/$maxProgress",
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textColorOnProgress: Color = Color.White,
    cornerRadius: Dp = 24.dp
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .drawWithContent {
                with(drawContext.canvas.nativeCanvas) {
                    val checkpoint = saveLayer(null, null)
                    drawContent()
                    drawRect(
                        color = progressColor,
                        size = Size((size.width * progress) / maxProgress, size.height),
                        blendMode = BlendMode.SrcOut
                    )

                    restoreToCount(checkpoint)
                }
            }
            .padding(1.dp)
            .height(5.dp),
        contentAlignment = Alignment.Center
    ) {

    }
}

