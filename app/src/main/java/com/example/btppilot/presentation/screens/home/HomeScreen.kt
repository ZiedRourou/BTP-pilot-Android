package com.example.btppilot.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusDelayed
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.toShortDate


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun HomePreview() {

//    val snackbarHostState = remember { SnackbarHostState() }

//    BtpPilotTheme {

//        Scaffold(
//            snackbarHost = { SnackbarHost(snackbarHostState) },
//            topBar = { HeaderScaffoldHome(userName = "zizou") },
//            bottomBar = {},
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = { /* action */ },
//                    containerColor = MaterialTheme.colorScheme.primary
//                ) {
//                    Text("+", fontSize = 25.sp, color = Color.White)
//                }
//            },
//        ) { padding ->

//            HomeContent(
//                paddingValues = padding,
//                homeState = HomeViewModel.HomeState(
//                    currentDate = "test",
//                    companyData = ProjectResponseByUserCompanyDto(
//                        listOf(
//                            ProjectResponseByUserCompanyDtoItem(
//                                1,
//                                "ddqsd",
//                                "dsqfds",
//                                "COMPLETED",
//                                "HIGH",
//                                "2029-02-13T00:00:00.000Z",
//                                "2029-02-13T00:00:00.000Z",
//                                "2029-02-13T00:00:00.000Z",
//                                listOf(UserProject("hhhh", User(5, "sqdd", "sqd"))),
//                                10,
//                                2
//
//                            )
//                        ),
//                        5
//                    )
//
//                ),
//                onClick = {}
//            )
//        }
//    }
}

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    homeViewModel: HomeViewModel,
    snackbarHostState: SnackbarHostState
) {
    val homeState by homeViewModel.homeStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.homeEventSharedFlow.collect { event ->
            when (event) {
                is EventState.ShowMessageSnackBar ->
                    snackbarHostState.showSnackbar(event.message)

                is EventState.RedirectScreen ->
                    navController.navigate(event.screen.route)
                is EventState.RedirectScreenWithId ->
                    navController.navigate(event.route)
                else -> {}
            }
        }
    }

        HomeContent(
            paddingValues = paddingValues,
            homeState = homeState,
            onClick = homeViewModel::onClickProject
        )
}

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    homeState: HomeViewModel.HomeState,
    onClick: (Int) -> Unit
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
            selected = homeState.selectedFilter,
            onSelectedChange = { }
        )

        ProjectList(
            items = homeState,
            onClick = onClick
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
    items: HomeViewModel.HomeState,
    onClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.companyData.projects) { item ->
            ItemHome(item, onClick)
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
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {

            items(optionList) { status ->

                FilterButton(
                    text = status.label,
                    isSelected = status == selected,
                    onClick = { onSelectedChange(status) }
                )
            }
        }
    }
}

@Composable
fun ItemHome(
    item: ProjectResponseByUserCompanyDtoItem,
    onClick : (Int) -> Unit
) {
    val progress = ((item.tasksCompleted.toFloat() / item.countTask.coerceAtLeast(1)) * 100).toInt()

    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary,
        onClick = {onClick(item.id)}
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


            TasksProgressBar(
                progress = progress,
                maxProgress = 100
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
                    text = ProjectAndTakPriorities.valueOf(item.priority).label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            ProjectAndTakPriorities.valueOf(item.priority).color,
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
    CurrentDate(homeState.currentDate)
    Spacer(modifier = Modifier.height(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        StatCard(
            title = "Chantiers Actifs",
            icon = Icons.Filled.CheckCircle,
            stat = homeState.companyData.projects.size,
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(
            modifier = Modifier.height(50.dp),
            color = Color.White.copy(alpha = 0.25f)
        )

        StatCard(
            title = "Tâches en retard",
            icon = Icons.Filled.WarningAmber,
            stat = homeState.companyData.taskInProgress,
            modifier = Modifier.weight(1f),
            tint = StatusDelayed
        )
    }
}




