package com.example.btppilot.presentation.screens.home

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.User
import com.example.btppilot.data.dto.response.UserProject
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusDelayed
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.arrayPriorities
import com.example.btppilot.util.toShortDate


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun HomePreview() {

    BtpPilotTheme {
        Scaffold(
            topBar = { HeaderScaffoldHome(userName = "eirf") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text("+", fontSize = 25.sp, color = Color.White)
                }
            },

            ) { paddingValues ->
            HomeContent(
                paddingValues = paddingValues,
                homeState = HomeViewModel.HomeState(
                    projectFilteredList =
                    listOf(
                        ProjectResponseByUserCompanyDtoItem(
                            0, "ee", "hhh",
                            ProjectStatus.PLANNED.name,
                            ProjectAndTakPriorities.HIGH.name,
                            "2026-03-13T22:59:00.000Z",
                            "2026-03-13T22:59:00.000Z", "gggg",
                            listOf(
                                UserProject("Manager", User(2, "sqd", "qsd"))
                            ),
                            2,
                            6,

                            )
                    )
                ),
                onClick = { },
                onFilterClick = {},
                isAuthorizedToEdit = {false},
            )
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    snackbarHostState: SnackbarHostState
) {
    val homeState by homeViewModel.homeStateFlow.collectAsState()
    val userInfo by sharedViewModel.userInfoStateFlow.collectAsState()

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

    Scaffold(
        topBar = { HeaderScaffoldHome(userName = userInfo.userFirstname) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = homeViewModel::redirectAddProject,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", fontSize = 25.sp, color = Color.White)
            }
        },

        ) { paddingValues ->
        HomeContent(
            paddingValues = paddingValues,
            homeState = homeState,
            onClick = homeViewModel::onClickProject,
            onFilterClick = homeViewModel::filterProject,
            isAuthorizedToEdit = sharedViewModel::isAuthorizedToEditProject
        )
    }

}

@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    homeState: HomeViewModel.HomeState,
    onClick: (Int) -> Unit,
    onFilterClick: (ProjectStatus) -> Unit,
    isAuthorizedToEdit: (ProjectResponseByUserCompanyDtoItem) -> Boolean
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(20.dp)
            .fillMaxSize()
    ) {

        HomeStatsCard(
            homeState.currentDate,
            homeState.activeProjectStat,
            homeState.taskStatusTodoOrInProgressStat
        )

        FilterButtons(
            optionList = homeState.projectStatus,
            selected = homeState.selectedFilter,
            onSelectedChange = onFilterClick
        )

        ProjectList(
            items = homeState.projectFilteredList,
            onClick = onClick,
            isAuthorizedToEdit = isAuthorizedToEdit
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
    items: List<ProjectResponseByUserCompanyDtoItem>,
    onClick: (Int) -> Unit,
    isAuthorizedToEdit: (ProjectResponseByUserCompanyDtoItem) -> Boolean
) {

    if (items.isNotEmpty())
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                ItemHome(item, onClick, isAuthorizedToEdit)
            }
        }
    else
        ProjectsNotFound()

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
    onClick: (Int) -> Unit,
    isAuthorizedToEdit: (ProjectResponseByUserCompanyDtoItem) -> Boolean

) {
    val canEdit = isAuthorizedToEdit(item)

    val progress = ((item.tasksCompleted.toFloat() / item.countTask.coerceAtLeast(1)) * 100).toInt()

    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary,
        onClick = { onClick(item.id) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                PriorityBadgeDropdown(
                    enableEdit = canEdit,
                    selected = ProjectAndTakPriorities.LOW,
                    onSelect = {}
                )

                if(canEdit)
                    IconButton(onClick = { onClick(item.id) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            tint = MaterialTheme.colorScheme.background,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
            }

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
fun ProjectsNotFound(
) {

    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        color = MaterialTheme.colorScheme.secondary,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Aucun projet disponible")

        }
    }
}


@Composable
fun HomeStatsCard(
    currentDate: String,
    projectStat: Int,
    tasStat: Int
) {
    CurrentDate(currentDate)

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
            stat = projectStat,
            modifier = Modifier.weight(1f)
        )

        VerticalDivider(
            modifier = Modifier.height(50.dp),
            color = Color.White.copy(alpha = 0.25f)
        )

        StatCard(
            title = "Tâches en retard",
            icon = Icons.Filled.WarningAmber,
            stat = tasStat,
            modifier = Modifier.weight(1f),
            tint = StatusDelayed
        )
    }
}


@Composable
fun PriorityBadgeDropdown(
    enableEdit: Boolean = false,
    selected: ProjectAndTakPriorities,
    onSelect: (ProjectAndTakPriorities) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }

    Surface(
        onClick = { if (enableEdit) expanded = true },
        shape = RoundedCornerShape(50),
        color = selected.color.copy(alpha = 0.18f),
        modifier = Modifier
            .wrapContentWidth()
            .animateContentSize()
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

        arrayPriorities.forEach { priority ->

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


