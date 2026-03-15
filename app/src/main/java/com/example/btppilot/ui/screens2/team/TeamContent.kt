package com.example.btppilot.ui.screens2.team

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.RoundaboutLeft
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.ui.screens2.project.component.AppFieldProject
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryButton
import com.example.btppilot.presentation.screens.shared.component.AppPrimaryTitleBlue
import com.example.btppilot.presentation.screens.shared.component.AppSecondaryTitle
import com.example.btppilot.presentation.screens.shared.component.AppTextField
import com.example.btppilot.presentation.screens.shared.component.AppTitleDescription
import com.example.btppilot.presentation.screens.shared.component.HeaderMainSreen
import com.example.btppilot.ui.screens2.task.taskList.TaskListContent
import com.example.btppilot.ui.screens2.task.taskList.TaskListViewModel
import com.example.btppilot.ui.theme.BtpPilotTheme
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.util.UserProjectRole
import com.example.btppilot.util.UserRole
import com.example.btppilot.util.UserRoleInCompany


@Preview(showBackground = true, apiLevel = 33)
@Composable
private fun NewTaskListScreenPreview() {


    BtpPilotTheme {

        Scaffold(
            topBar = { HeaderMainSreen(userName = "userInfo.userFirstname") },
        ) { paddingValues ->

            TeamContent(
                paddingValues,
                userListState = TeamViewModel.TeamState(),
                onEmailChange = {},
                inviteUser = {},
                onRoleChange = {}
            )
        }

    }
}

@Composable
fun TeamContent(
    paddingValues: PaddingValues,
    userListState: TeamViewModel.TeamState,
    onEmailChange: (String) -> Unit,
    onRoleChange: (UserRole) -> Unit,
    inviteUser: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(20.dp)
            .fillMaxSize()
    ) {
        if (userListState.userList.isEmpty()) {
            NoTeamToDisplay()
        }
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            items(userListState.userList) { item ->
                TeamItem(item)
            }


            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(2.dp, Color.Gray),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {


                        Spacer(modifier = Modifier.width(10.dp))
                        AppSecondaryTitle(text = "Inviter un client ou employé a votre entreprise")

                        Spacer(modifier = Modifier.height(20.dp))

                        AppTextField(
                            value = userListState.email,
                            onValueChange = onEmailChange,
                            isError = userListState.emailError != null,
                            supportingText = userListState.emailError,
                            label = "Email de votre invité",
                            leadingIcon = Icons.Filled.Email
                        )

                        AppSelectUserRoleRadioBtnFieldTeam(
                            options = listOf(UserRole.CLIENT, UserRole.COLLABORATOR),
                            selectedOption = userListState.selectedRole,
                            onSelectionChange = onRoleChange
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        AppPrimaryButton(
                            text = "Inviter",
                            onClick = inviteUser,
                            modifier = Modifier
                                .padding(horizontal = 50.dp)
                                .padding(vertical = 10.dp)
                        )

                    }
                }
            }
        }


    }
}

@Composable
fun NoTeamToDisplay() {
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
            Text(text = "Aucun Utilisateur lié a votre entreprise")
        }
    }
}

@Composable
fun TeamItem(
    user: UsersOfCompanyItem
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

            Text(
                text = user.user.firstName + " . " + user.user.lastName,
                style = MaterialTheme.typography.titleMedium
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Text(
                    text = UserRoleInCompany.valueOf(user.role).label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Task,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    val random1 = (0..8).random()
                    Text(
                        text = "$random1 / ${(random1..40).random() }",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }




        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSelectUserRoleRadioBtnFieldTeam(
    options: List<UserRole>,
    selectedOption: UserRole,
    onSelectionChange: (UserRole) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {


            OutlinedTextField(
                shape = RoundedCornerShape(12.dp),
                value = selectedOption.name,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                label = { Text("Role de votre invité") },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                options.forEach { option ->

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                RadioButton(
                                    selected = option == selectedOption,
                                    onClick = null
                                )

                                Spacer(Modifier.width(5.dp))

                                Text(option.name)
                            }
                        },
                        onClick = {
                            onSelectionChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
