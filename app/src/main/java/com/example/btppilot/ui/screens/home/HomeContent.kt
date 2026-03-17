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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.CheckCircle
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.user.User
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.ui.screens.home.component.CurrentDate
import com.example.btppilot.ui.screens.home.component.FilterButton
import com.example.btppilot.ui.screens.home.component.PriorityBadgeDropdown
import com.example.btppilot.ui.screens.home.component.TasksProgressBar
import com.example.btppilot.ui.screens.shared.component.AppPrimaryTitle
import com.example.btppilot.ui.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.ui.screens.shared.component.AppTitleDescription
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusDelayed
import com.example.btppilot.ui.theme.StatusDone
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.ui.theme.StatusTodo
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.formatStrDateToShortDate
import com.example.btppilot.util.formatStrDateToWordFrDate


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
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
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
                onPriorityChange = { _, _ -> Unit },
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
           stat = homeState
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
                    onClickEditProject = onClickEditProject,
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
            text = stringResource(R.string.my_projects),
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

                AppTitleDescription(text = item.plannedStartDate.formatStrDateToShortDate())
                Spacer(modifier = Modifier.width(10.dp))
                Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))

                AppTitleDescription(text = item.plannedEndDate.formatStrDateToShortDate())
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AppTitleDescription(text = stringResource(R.string.task_progression))
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
            Text(text = stringResource(R.string.no_project_to_display))
        }
    }
}


@Composable
fun HomeStatsCard(
  stat : HomeViewModel.HomeState
) {
    CurrentDate(stat.currentDate)

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            AppSecondaryTitle(
                text = stringResource(R.string.active_projects),
                color = MaterialTheme.colorScheme.background
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = StatusDone,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(15.dp))

                AppPrimaryTitle(
                    text = stat.activeProjectStat.toString(),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }

        VerticalDivider(
            modifier = Modifier.height(50.dp),
            color = Color.White.copy(alpha = 0.25f)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppSecondaryTitle(
                    text = "Taches ",
                    color = MaterialTheme.colorScheme.background
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.this_week),
                    color = Color.LightGray,
                    style = MaterialTheme.typography.labelSmall
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = null,
                    tint = StatusDelayed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(R.string.task_late), color = Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stat.taskLateWeek.toString(), color = Color.White)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = null,
                    tint = StatusInProgress,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(R.string.task_in_progress), color = Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stat.taskInProgressWeek.toString(), color = Color.White)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Icon(
                    imageVector = Icons.Filled.Circle,
                    contentDescription = null,
                    tint = StatusTodo,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(R.string.task_to_do), color = Color.White)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stat.taskTodoWeek.toString(), color = Color.White)
            }
        }
    }
}

