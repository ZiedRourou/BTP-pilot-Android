package com.example.btppilot.ui.screens.project.projectDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.btppilot.R
import com.example.btppilot.data.dto.response.user.User
import com.example.btppilot.data.dto.response.project.UserProject
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.ui.screens.shared.component.AppPrimaryButton
import com.example.btppilot.ui.screens.shared.component.AppPrimaryTitle
import com.example.btppilot.ui.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.ui.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens.shared.component.LoadingOverlay
import com.example.btppilot.ui.screens.task.component.ItemTask
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.TaskStatus
import com.example.btppilot.util.UserProjectRole
import com.example.btppilot.util.formatStrDateToWordFrDate


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun ProjectDetailsScreenPreview() {

    val snackbarHostState = remember { SnackbarHostState() }

    BtpPilotTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { HeaderMainSreen(userName = "test") },

            ) { padding ->
            ProjectDetailsContent(
                paddingValues = padding,
                projectState =
                ProjectDetailsViewModel.DetailsProjectState(
                  project =   ProjectByIdResponseDto(
                        id = 0,
                        name = "sqdSQDSSSSSSSSSSSSSSSSSSSSSS",
                        description = "DDDDDDDDDSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSDqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq",
                        status = ProjectStatus.PLANNED.name,
                        priority = ProjectAndTakPriorities.HIGH.name,
                        plannedEndDate = "",
                        plannedStartDate = "",
                        currentEndDate = null,
                        companyId = 0,
                        userProjects = listOf(
                            UserProject(
                                "test",
                                User(0, "zied", "rourou")
                            ),
                            UserProject(
                                "test",
                                User(0, "zied", "rourou")
                            ),
                            UserProject(
                                "test",
                                User(0, "zied", "rourou")
                            )
                        )

                    ),
                ),
                onClickDelete = {},
                onClickEdit = {},
                onClickAddTask = { },
                isEnableToEditProject = true,
                isEnableToEditStatus =true,
                redirectEditTask = {},
                changeStatus = { taskStatus, tasksByProjectDtoItem -> Unit  },
                deleteTask = {}
            )
        }
    }
}

@Composable
fun ProjectDetailsContent(
    paddingValues: PaddingValues,
    projectState: ProjectDetailsViewModel.DetailsProjectState,
    onClickDelete: () -> Unit,
    onClickEdit: () -> Unit,
    onClickAddTask: () -> Unit,
    isEnableToEditProject: Boolean,
    isEnableToEditStatus:  Boolean,
    redirectEditTask : (Int) -> Unit,
    changeStatus: (TaskStatus, TasksByProjectDtoItem) -> Unit,
    deleteTask : (Int)-> Unit
) {

    LoadingOverlay(isVisible = projectState.isLoading)
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .padding(25.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {

            ProjectDetailsStatusAndPriority(
                status = ProjectStatus.valueOf(projectState.project.status),
                priority = ProjectAndTakPriorities.valueOf(projectState.project.priority),
                onClickEdit = onClickEdit,
                onClickDelete = onClickDelete,
                isEnableToEditProject = isEnableToEditProject
            )

            AppPrimaryTitle(
                text = projectState.project.name,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(20.dp))
            AssignedTeamCard(
                date = projectState.project.plannedEndDate,
                members = projectState.project.userProjects
            )
            Spacer(modifier = Modifier.height(20.dp))

            AppSecondaryTitle(text = stringResource(R.string.description))
            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = projectState.project.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AppSecondaryTitle(text = stringResource(R.string.tasks))

                if (isEnableToEditProject)
                    IconButton(
                        onClick = onClickAddTask,
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.background,
                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.background
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null
                        )
                    }
            }
        }

        if (projectState.tasks.isEmpty()) {

            item {
                TasksNotFound()
            }

        } else {

            items(projectState.tasks) { item ->
                ItemTask(
                    taskState = item,
                    canEditTask = isEnableToEditProject,
                    canEditStatus = isEnableToEditStatus,
                    changeStatus = changeStatus,
                    redirectEditTask = redirectEditTask,
                    deleteTask = deleteTask
                )
            }
        }
    }
}


@Composable
fun TasksNotFound(
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
            Text(text = stringResource(R.string.no_tasks_to_display))
        }
    }
}

@Composable
fun ConfirmDeleteProjectDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text(
                text = stringResource(R.string.delete_project),
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Text(
                text = stringResource(R.string.confirm_delete_project)
            )
        },

        confirmButton = {

            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = Color.Red
                )
            }

        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(id = R.string.cancel))
            }
        }
    )
}




@Composable
fun AssignedTeamCard(
    members: List<UserProject>,
    date: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
                .height(80.dp)
                .weight(1F)
        ) {

            Text(
                text = stringResource(R.string.end_project),
                color = Color.LightGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = date.formatStrDateToWordFrDate(),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        TeamCard(
            members = members,
            modifier = Modifier
                .weight(1f)
        )
    }
}


@Composable
fun TeamCard(
    members: List<UserProject>,
    modifier: Modifier = Modifier
) {

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.team),
            color = Color.LightGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = StatusInProgress
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = stringResource(R.string.team),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Filled.Visibility,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }

    if (showDialog) {

        Dialog(onDismissRequest = { showDialog = false }) {

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.secondary
            ) {

                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .width(300.dp)
                ) {

                    Text(
                        text = stringResource(R.string.team_member),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    val manager = members.firstOrNull { it.projectRole == UserProjectRole.MANAGER.name }
                    val clients = members.filter { it.projectRole == UserProjectRole.CLIENT.name }
                    val collaborator = members.filter { it.projectRole == UserProjectRole.EMPLOYEE.name }

                    Spacer(Modifier.height(16.dp))
                    Text(text = "Manager ")
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "${manager?.user?.firstName}")


                    if (clients.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        Text(text = "Client ")
                        Spacer(modifier = Modifier.height(5.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.heightIn(max = 350.dp)
                        ) {
                            items(items = clients) { member ->
                                Text(text = " ${member.user.firstName}  ")
                            }
                        }
                    }
                    if (collaborator.isNotEmpty()) {
                        Spacer(Modifier.height(16.dp))
                        Text(text = stringResource(id = R.string.collaborator))
                        Spacer(modifier = Modifier.height(5.dp))
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.heightIn(max = 350.dp)
                        ) {
                            items(collaborator) { member ->
                                Text(text = " ${member.user.firstName} ")
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    AppPrimaryButton(text = stringResource(R.string.close),
                        onClick = { showDialog = false })
                }
            }
        }
    }
}

@Composable
fun ProjectDetailsStatusAndPriority(
    status: ProjectStatus,
    priority: ProjectAndTakPriorities,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    isEnableToEditProject: Boolean

) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),

        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {

            Text(
                text = stringResource(R.string.status_uppercase),
                style = MaterialTheme.typography.labelSmall,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = status.label,
                style = MaterialTheme.typography.labelLarge,
                color = status.color,
                modifier = Modifier
                    .background(
                        status.color.copy(alpha = 0.25f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {

            Text(
                text = stringResource(R.string.priority_upper),
                style = MaterialTheme.typography.labelSmall,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = priority.label,
                style = MaterialTheme.typography.labelLarge,
                color = priority.color,
                modifier = Modifier
                    .background(
                        priority.color.copy(alpha = 0.25f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
        if (isEnableToEditProject) {
            IconButton(onClick = onClickEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = StatusInProgress
                )
            }

            IconButton(onClick = onClickDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}



