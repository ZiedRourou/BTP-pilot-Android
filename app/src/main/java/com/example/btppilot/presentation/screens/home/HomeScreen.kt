package com.example.btppilot.presentation.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.User
import com.example.btppilot.data.dto.response.UserProject
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusDelayed
import com.example.btppilot.ui.theme.StatusDone
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.toShortDate


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun HomePreview() {

    val snackbarHostState = remember { SnackbarHostState() }

    BtpPilotTheme {

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { HeaderScaffoldHome(userName = "zizou") },
            bottomBar = {},
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* action */ },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text("+", fontSize = 25.sp, color = Color.White)
                }
            },
        ) { padding ->

            HomeContent(
                paddingValues = padding,
                homeState = HomeViewModel.HomeState(
                    currentDate = "test",
                    companyData = ProjectResponseByUserCompanyDto(
                        listOf(
                            ProjectResponseByUserCompanyDtoItem(
                                1,
                                "ddqsd",
                                "dsqfds",
                                "COMPLETED",
                                "HIGH",
                                "2029-02-13T00:00:00.000Z",
                                "2029-02-13T00:00:00.000Z",
                                "2029-02-13T00:00:00.000Z",
                                listOf(UserProject("hhhh", User(5, "sqdd", "sqd"))),
                                10,
                                2

                            )
                        ),
                        5
                    )

                )
            )
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val homeState by homeViewModel.homeStateFlow.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HeaderScaffoldHome(userName = homeState.currentUserName) },
        bottomBar = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* action */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", fontSize = 25.sp, color = Color.White)
            }
        },
    ) { padding ->
        HomeContent(
            paddingValues = padding,
            homeState = homeState,
        )
    }
}

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    homeState: HomeViewModel.HomeState,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(15.dp)
            .fillMaxSize()
    ) {

        HomeStatsCard(homeState)

        FilterButtons(
            optionList = homeState.projectStatus,
            selected = ProjectStatus.ALL,
            onSelectedChange = { }
        )

        ProjectList(
            items = homeState
        )

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
fun ProjectList(
    items: HomeViewModel.HomeState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.companyData.projects) { item ->
                ItemHome(item)
            }
        }
    }
}

@Composable
fun FilterButtons(
    optionList: List<ProjectStatus>,
    selected: ProjectStatus,
    onSelectedChange: (ProjectStatus) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {

        Text(
            text = "Mes chantiers",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {

            items(optionList) { status ->

                val isSelected = status == selected

                Button(
                    onClick = { onSelectedChange(status) },
                    modifier = Modifier.width(120.dp), // largeur fixe pour uniformité
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (isSelected)
                            Color.White
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text(
                        text = status.label,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ItemHome(item: ProjectResponseByUserCompanyDtoItem) {
    val progress = ((item.tasksCompleted.toFloat() / item.countTask.coerceAtLeast(1)) * 100).toInt()

    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = ProjectStatus.valueOf(item.status).label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .background(ProjectStatus.valueOf(item.status).color, RoundedCornerShape(10.dp))
                    .padding(5.dp),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = item.name, style = MaterialTheme.typography.headlineSmall)

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
                    item.plannedStartDate.toShortDate(),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(modifier = Modifier.width(5.dp))
                Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null)
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    item.plannedEndDate.toShortDate(),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Progression des taches",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = progress.toString() + "%",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            GenericProgressBar<Any>(
                progress = progress,
                maxProgress = 100,
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.tertiary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Task,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "${item.tasksCompleted} / ${item.countTask}",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Text(
                    text = ProjectPriorities.valueOf(item.priority).label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            ProjectPriorities.valueOf(item.priority).color,
                            RoundedCornerShape(5.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.SupervisorAccount,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        item.userProjects.size.toString(),
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}


@Composable
fun HomeStatsCard(
    homeState: HomeViewModel.HomeState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Event, null, tint = StatusInProgress, modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            homeState.currentDate,
            style = MaterialTheme.typography.labelSmall,
            color = StatusInProgress
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Chantiers actifs",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = StatusDone,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = homeState.companyData.projects.size.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }

        Divider(
            modifier = Modifier
                .height(50.dp)
                .width(1.dp),
            color = Color.White.copy(alpha = 0.25f)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = "Tâches en retard",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Filled.WarningAmber,
                    contentDescription = null,
                    tint = StatusDelayed,
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = homeState.companyData.taskInProgress.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}
//
//@Composable
//fun TestRowText() {
//    Scaffold(
//        topBar = {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.SpaceEvenly
//            ) {
////                HeaderScaffoldHome()
//            }
//        },
//
//
//        content = {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(it),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Top
//            ) {
////                ContentHome()
//            }
//        },
//        bottomBar = {
//            //nav bar plus tard
//        },
//        floatingActionButtonPosition = FabPosition.End,
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {},
//                containerColor = MaterialTheme.colorScheme.primary,
//            ) {
//                Text(
//                    text = "+",
//                    fontSize = 25.sp,
//                    color = MaterialTheme.colorScheme.background
//                )
//            }
//        },
//    )
//
//}
//

//
//@Composable
//fun ContentHome() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(10.dp),
//        verticalArrangement = Arrangement.SpaceBetween,
//        horizontalAlignment = Alignment.Start
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Icon(
//                imageVector = Icons.Filled.Event,
//                contentDescription = null,
//                tint = StatusInProgress,
//                modifier = Modifier.size(15.dp)
//            )
//            Spacer(modifier = Modifier.width(10.dp))
//            Text(
//                text = "Mardi 15 janvier",
//                style = MaterialTheme.typography.labelSmall,
//                color = StatusInProgress
//            )
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(10.dp)
//                .border(0.dp, Color.Transparent, RoundedCornerShape(10.dp))
//                .background(
//                    MaterialTheme.colorScheme.secondary,
//                    shape = RoundedCornerShape(12.dp)
//                ),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Absolute.SpaceAround
//        ) {
//
//            Column(
//                Modifier.padding(10.dp)
//            ) {
//                Text(
//                    text = "Chantier Actifs",
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = MaterialTheme.colorScheme.background
//                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.CheckCircle,
//                        contentDescription = null,
//                        tint = StatusDone,
//                        modifier = Modifier.size(25.dp)
//                    )
//                    Spacer(modifier = Modifier.width(5.dp))
//
//                    Text(
//                        text = "7",
//                        color = MaterialTheme.colorScheme.background,
//                        style = MaterialTheme.typography.headlineLarge
//                    )
//                }
//            }
//            Divider(
//                modifier = Modifier
//                    .height(70.dp)
//                    .width(1.dp),
//                color = Color.Gray.copy(alpha = 0.4f)
//            )
//
//            Column(
//                Modifier.padding(10.dp)
//            ) {
//                Text(
//                    text = "Taches en retards",
//                    style = MaterialTheme.typography.headlineSmall,
//                    color = MaterialTheme.colorScheme.background
//                )
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.WarningAmber,
//                        contentDescription = null,
//                        tint = StatusDelayed,
//                        modifier = Modifier.size(25.dp)
//                    )
//
//                    Text(
//                        text = "7",
//                        color = MaterialTheme.colorScheme.background,
//                        style = MaterialTheme.typography.headlineLarge
//                    )
//                }
//            }
//        }
//        Row {
//            Text(text = "Mes chantiers", style = MaterialTheme.typography.labelLarge)
//        }
//
//        FilterButtons()
//        Spacer(modifier = Modifier.height(5.dp))
//        val chantierList = List(20) { it }
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(chantierList) {
//                ItemHome()
//            }
//        }
//
//    }
//}
//
//@Composable
//fun FilterButtons() {
//
//    var selectedIndex by remember { mutableStateOf(0) }
//
//    val items = listOf("Tous", "En retard", "En cours")
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        items.forEachIndexed { index, title ->
//
//            val isSelected = index == selectedIndex
//
//            Button(
//                onClick = { selectedIndex = index },
//                modifier = Modifier.weight(1f),
//                shape = RoundedCornerShape(50),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor =
//                    if (isSelected)
//                        MaterialTheme.colorScheme.primary // jaune actif
//                    else
//                        MaterialTheme.colorScheme.secondaryContainer,
//
//                    contentColor =
//                    if (isSelected)
//                        Color.White
//                    else
//                        MaterialTheme.colorScheme.onSecondaryContainer
//                )
//            ) {
//                Text(title, style = MaterialTheme.typography.bodyLarge)
//            }
//        }
//    }
//}
//
//@Composable
//fun ItemHome() {
//    Surface(
//        shape = RoundedCornerShape(12.dp),
//        border = BorderStroke(2.dp, Color.Gray),
//        modifier = Modifier.fillMaxWidth(),
//        color = MaterialTheme.colorScheme.secondary
//    ) {
//
//        Column(
//            modifier = Modifier.padding(16.dp),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.Start
//        ) {
//
//            Text(
//                text = "EN RETARD",
//                style = MaterialTheme.typography.labelSmall,
//                modifier = Modifier
//                    .background(MaterialTheme.colorScheme.error, RoundedCornerShape(10.dp))
//                    .padding(5.dp)
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(
//                text = "Residence Prado",
//                style = MaterialTheme.typography.headlineSmall
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Event,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.tertiary,
//                    modifier = Modifier.size(15.dp)
//                )
//                Spacer(modifier = Modifier.width(5.dp))
//
//                Text(
//                    text = "10/02/2025",
//                    color = MaterialTheme.colorScheme.tertiary,
//                    style = MaterialTheme.typography.labelMedium
//                )
//                Spacer(modifier = Modifier.width(5.dp))
//
//                Image(
//                    painter = painterResource(id = R.drawable.arrow),
//                    contentDescription = null,
//                )
//                Spacer(modifier = Modifier.width(5.dp))
//
//                Text(
//                    text = "18/02/2025",
//                    color = MaterialTheme.colorScheme.tertiary,
//                    style = MaterialTheme.typography.labelMedium
//                )
//                Spacer(modifier = Modifier.width(5.dp))
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//            Column() {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "Progression des taches",
//                        color = MaterialTheme.colorScheme.tertiary,
//                        style = MaterialTheme.typography.labelSmall
//                    )
//                    Text(
//                        text = "65%",
//                        color = MaterialTheme.colorScheme.primary,
//                        style = MaterialTheme.typography.labelSmall
//                    )
//                }
//                Spacer(modifier = Modifier.height(8.dp))
////                GenericProgressBar(
////                    progress = 2,
////                    maxProgress = 10,
////                    modifier = Modifier.fillMaxWidth(),
////                    backgroundColor = MaterialTheme.colorScheme.tertiary,
////                    textColor = MaterialTheme.colorScheme.secondary
////                )
//                Spacer(modifier = Modifier.height(10.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Row {
//                        Icon(
//                            imageVector = Icons.Filled.Task,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.size(15.dp)
//                        )
//
//                        Spacer(modifier = Modifier.width(5.dp))
//
//                        Text(
//                            text = "15/20",
//                            color = MaterialTheme.colorScheme.tertiary,
//                            style = MaterialTheme.typography.labelSmall
//                        )
//                    }
//
//                    Text(
//                        text = "EN RETARD",
//                        style = MaterialTheme.typography.labelSmall,
//                        modifier = Modifier
//                            .background(
//                                MaterialTheme.colorScheme.tertiary,
//                                RoundedCornerShape(5.dp)
//                            )
//                            .padding(5.dp)
//                    )
//                    Row {
//                        Icon(
//                            imageVector = Icons.Filled.SupervisorAccount,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.size(15.dp)
//                        )
//                        Spacer(modifier = Modifier.width(5.dp))
//                        Text(
//                            text = "15/20",
//                            color = MaterialTheme.colorScheme.tertiary,
//                            style = MaterialTheme.typography.labelSmall
//                        )
//                    }
//
//                }
//            }
//        }
//    }
//
//}
//
@Composable
fun <Dp> GenericProgressBar(
    progress: Int,
    maxProgress: Int,
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
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

