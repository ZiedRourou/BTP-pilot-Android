package com.example.btppilot.ui.screens.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.user.User
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.ui.screens.home.component.CurrentDate
import com.example.btppilot.ui.screens.home.component.FilterButton
import com.example.btppilot.ui.screens.home.component.PriorityBadgeDropdown
import com.example.btppilot.ui.screens.home.component.StatCard
import com.example.btppilot.ui.screens.home.component.TasksProgressBar
import com.example.btppilot.ui.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.ui.screens.shared.component.AppTitleDescription
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusDelayed
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.toShortDate


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun HomePreview() {

    BtpPilotTheme {
        Scaffold(
            topBar = { HeaderMainSreen(userName = "eirf") },
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
                            "2026-03-13T22:59:00.000Z", "2026-03-13T22:59:00.000Z",
                            listOf(
                                UserProject("Manager", User(2, "sqd", "qsd"))
                            ),
                            2,
                            6,

                            )
                    )
                ),
                onFilterClick = {},
                isAuthorizedToEdit = { true },
                onPriorityChange = { _, _ -> Unit  },
                onClickEditProject = {},
                onClickViewProject = {}
            )
        }
    }
}


@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    homeState: HomeViewModel.HomeState,
    onClickViewProject: (Int) -> Unit,
    onClickEditProject: (Int) -> Unit,
    onFilterClick: (ProjectStatus) -> Unit,
    isAuthorizedToEdit: (ProjectResponseByUserCompanyDtoItem) -> Boolean,
    onPriorityChange: (Int, ProjectStatus) -> Unit

) {
    LoadingOverlay(isVisible = homeState.isLoading)

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

        FilterStatusProjectHome(
            optionList = homeState.projectStatus,
            selected = homeState.selectedFilter,
            onSelectedChange = onFilterClick
        )

        ProjectList(
            items = homeState.projectFilteredList,
            onClickEditProject = onClickEditProject,
            onClickViewProject = onClickViewProject,
            isAuthorizedToEdit = isAuthorizedToEdit,
            onPriorityChange = onPriorityChange
        )
    }
}


@Composable
fun ProjectList(
    items: List<ProjectResponseByUserCompanyDtoItem>,
    onClickViewProject: (Int) -> Unit,
    onClickEditProject: (Int) -> Unit,
    isAuthorizedToEdit: (ProjectResponseByUserCompanyDtoItem) -> Boolean,
    onPriorityChange: (Int, ProjectStatus) -> Unit

) {

    if (items.isNotEmpty())
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                ProjectItemHome(
                    item = item,
                    onClickEditProject=onClickEditProject,
                    onClickViewProject = onClickViewProject,
                    isAuthorizedToEdit = isAuthorizedToEdit,
                    onPriorityChange = onPriorityChange
                )
            }
        }
    else
        ProjectsNotFound()

}

@Composable
fun FilterStatusProjectHome(
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
fun ProjectItemHome(
    item: ProjectResponseByUserCompanyDtoItem,
    onClickViewProject: (Int) -> Unit,
    onClickEditProject: (Int) -> Unit,
    isAuthorizedToEdit: (ProjectResponseByUserCompanyDtoItem) -> Boolean,
    onPriorityChange: (Int, ProjectStatus) -> Unit

) {
    val canEdit = isAuthorizedToEdit(item)

    val progress = ((item.tasksCompleted.toFloat() / item.countTask.coerceAtLeast(1)) * 100).toInt()

    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary,
        onClick = { onClickViewProject(item.id) }
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
                    selected = ProjectStatus.valueOf(item.status),
                    onSelect = { newPriority ->
                        onPriorityChange(item.id, newPriority)
                    }
                )

                if (canEdit)
                    IconButton(onClick = { onClickEditProject(item.id) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            tint = StatusInProgress,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AppSecondaryTitle(
                text = item.name,
                color = MaterialTheme.colorScheme.background
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
                Spacer(modifier = Modifier.width(10.dp))

                AppTitleDescription(text = item.plannedStartDate.toShortDate())
                Spacer(modifier = Modifier.width(10.dp))
                Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))

                AppTitleDescription(text = item.plannedEndDate.toShortDate())
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AppTitleDescription(text = "Progression des taches")
                AppTitleDescription(
                    text = "$progress%",
                    color = MaterialTheme.colorScheme.primary
                )

            }

            Spacer(modifier = Modifier.height(10.dp))


            TasksProgressBar(
                progress = progress,
                maxProgress = 100
            )

            Spacer(modifier = Modifier.height(15.dp))

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
                            ProjectAndTakPriorities.valueOf(item.priority).color.copy(alpha = 0.5F),
                            RoundedCornerShape(5.dp)
                        )
                        .padding(7.dp)
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

