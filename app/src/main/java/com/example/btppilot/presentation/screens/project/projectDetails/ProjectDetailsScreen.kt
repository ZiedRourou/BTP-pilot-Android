package com.example.btppilot.presentation.screens.project.projectDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.btppilot.data.dto.response.UserProject
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.presentation.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.home.HeaderScaffoldHome
import com.example.btppilot.presentation.screens.test.DropdownPriority
import com.example.btppilot.presentation.screens.uiState.EventState
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.ProjectAndTakPriorities
import com.example.btppilot.util.ProjectStatus
import com.example.btppilot.util.TaskStatus
import com.example.btppilot.util.arrayPriorities
import com.example.btppilot.util.isoToFrenchDate


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun ProjectDetailsScreenPreview() {

    val snackbarHostState = remember { SnackbarHostState() }

    BtpPilotTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = { HeaderScaffoldHome(userName = "test") },

            ) { padding ->
            ProjectDetailsContent(
                paddingValues = padding,
                projectState = ProjectDetailsViewModel.DetailsProjectState(),
                onClickDelete = {},
                onClickEdit = {},
                onClickAddTask = { },
                onSelectPriority = {}
            )
        }
    }
}


@Composable
fun ProjectDetailsScreen(
    navController: NavController,
    projectDetailsViewModel: ProjectDetailsViewModel,
    projectId: Long
) {

    projectDetailsViewModel.setProjectId(projectId)
    val projectState by projectDetailsViewModel.detailProjectStateFlow.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        projectDetailsViewModel.detailProjectEventSharedFlow.collect { event ->
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { HeaderScaffoldHome(userName = "test") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text("+", fontSize = 25.sp, color = Color.White)
            }
        },
    ) { padding ->
        ProjectDetailsContent(
            paddingValues = padding,
            projectState = projectState,
            onClickDelete = projectDetailsViewModel::confirmDelete,
            onClickEdit = projectDetailsViewModel::onEditProject,
            onClickAddTask = projectDetailsViewModel::redirectAddTask,
            onSelectPriority = projectDetailsViewModel::onSelectPriority,
        )
        if (projectState.showDialog)
            ConfirmDeleteProjectDialog(
                onConfirm = projectDetailsViewModel::deleteProject,
                onDismiss = projectDetailsViewModel::closeDialogDelete
            )
    }
}

@Composable
fun ProjectDetailsContent(
    paddingValues: PaddingValues,
    projectState: ProjectDetailsViewModel.DetailsProjectState,
    onClickDelete: () -> Unit,
    onClickEdit: () -> Unit,
    onClickAddTask: () -> Unit,
    onSelectPriority: (ProjectAndTakPriorities) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(15.dp)
            .fillMaxSize()
    ) {

        ProjectDetailsStatusAndPriority(
            projectState = projectState,
            status = ProjectStatus.valueOf(projectState.project.status),
            onSelectPriority = onSelectPriority
        )

        ProjectTitleRow(
            projectState.project.name,
            onClickDelete, onClickEdit
        )

        AssignedTeamCard(
            date = projectState.project.plannedEndDate,
            members = projectState.project.userProjects
        )
        Spacer(modifier = Modifier.height(20.dp))

        AppSecondaryTitle(text = "Description")

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = projectState.project.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        AppSecondaryTitle(text = "Tâches")

        Spacer(modifier = Modifier.height(8.dp))

        AppPrimaryButton(text = "ajouter une tache", onClick = onClickAddTask)
        Spacer(modifier = Modifier.height(8.dp))

        when {
            projectState.tasks.isEmpty() -> {

                Text(
                    text = "Aucune tâche",
                    modifier = Modifier.padding(16.dp)
                )

            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(projectState.tasks) { item ->
                        ItemTask(item)
                    }

                }

            }
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
                text = "Supprimer le projet",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Text(
                text = "Voulez-vous vraiment supprimer le projet ? Cette action est irréversible."
            )
        },

        confirmButton = {

            TextButton(
                onClick = onConfirm
            ) {
                Text(
                    text = "Supprimer",
                    color = Color.Red
                )
            }

        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Annuler")
            }
        }
    )
}



@Composable
fun ItemTask(
    taskState: TasksByProjectDtoItem
) {

    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondary
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            TaskStatusAndPriority(
                TaskStatus.valueOf(taskState.status),
                ProjectAndTakPriorities.valueOf(taskState.priority)
            )

            Text(
                text = taskState.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = taskState.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun AssignedTeamCard(
    members: List<UserProject>,
    date: String
) {

    val cardHeight = 140.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        DueDateCard(
            date = date,
            modifier = Modifier
                .weight(1f)
                .height(cardHeight)
        )

        TeamCard(
            teamName = "L'équipe",
            members = members,
            modifier = Modifier
                .weight(1f)
                .height(cardHeight)
        )
    }
}

@Composable
fun DueDateCard(
    date: String,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp)
            .height(85.dp)
    ) {

        Text(
            text = "date de fin de projet",
            color = Color.LightGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = isoToFrenchDate(date),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TeamCard(
    teamName: String,
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
            text = "ÉQUIPE",
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
                    text = teamName,
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
                        text = "Membres de l'équipe",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )

                    Spacer(Modifier.height(16.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.heightIn(max = 350.dp)
                    ) {

                        items(members) { member ->

                            TeamMemberRow(member)
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = { showDialog = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Fermer")
                    }
                }
            }
        }
    }
}
@Composable
fun TeamMemberRow(member: UserProject) {

    val roleColor = when (member.projectRole) {
        "MANAGER" -> Color(0xFF4CAF50)
        "CLIENT" -> Color(0xFF2196F3)
        else -> Color(0xFFFF9800)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                RoundedCornerShape(12.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "${member.user.firstName} ${member.user.lastName}",
            color = Color.White
        )

        Text(
            text = member.projectRole,
            color = Color.White,
            modifier = Modifier
                .background(roleColor, RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun ProjectTitleRow(
    title: String,
    onClickDelete: () -> Unit,
    onClickEdit: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

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

@Composable
fun ProjectDetailsStatusAndPriority(
    projectState: ProjectDetailsViewModel.DetailsProjectState,
    status: ProjectStatus,
    onSelectPriority: (ProjectAndTakPriorities) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        DropdownPriority(
            priorities = arrayPriorities,
            selectedPriority = projectState.selectPriority,
            onSelectPriority = onSelectPriority
        )

        Spacer(Modifier.width(12.dp))

//        StatusPriorityBadge(
//            text = status.label,
//            background = status.color.copy(alpha = 0.15f),
//            border = status.color,
//            textColor = status.color
//        )

        Spacer(Modifier.width(8.dp))
//
//        StatusPriorityBadge(
//            text = projectState.selectPriority.label,
//            background = projectState.selectPriority.color.copy(alpha = 0.15f),
//            border = projectState.selectPriority.color,
//            textColor = projectState.selectPriority.color
//        )
    }
}


@Composable
fun TaskStatusAndPriority(
    status: TaskStatus,
    priority: ProjectAndTakPriorities,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        StatusPriorityBadge(
            text = status.label,
            background = status.color.copy(alpha = 0.15f),
            border = status.color,
            textColor = status.color,
            15.sp
        )

        Spacer(modifier = Modifier.width(8.dp))


        StatusPriorityBadge(
            text = priority.label,
            background = priority.color.copy(alpha = 0.15f),
            border = priority.color,
            textColor = priority.color,
            15.sp
        )
    }
}

@Composable
fun StatusPriorityBadge(
    text: String,
    background: Color,
    border: Color,
    textColor: Color,
    fontSize: TextUnit = 20.sp
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(background)
            .border(
                width = 1.dp,
                color = border,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold
        )
    }
}