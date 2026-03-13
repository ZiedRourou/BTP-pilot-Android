package com.example.btppilot.presentation.screens.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.presentation.screens.shared.SharedViewModel
import com.example.btppilot.presentation.screens.project.component.AppFieldProject

@Composable
fun TeamScreen(
    navController: NavController,
    teamViewModel: TeamViewModel,
    sharedViewModel: SharedViewModel,
    snackbarHostState: SnackbarHostState
) {
    val state by teamViewModel.teamStateFlow.collectAsState()

    TeamContent(
        userListState = state
    )
}

@Composable
fun TeamContent(
    userListState: TeamViewModel.TeamState
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
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
        }

        AppFieldProject(label = "Inviter une personne", value ="dd" , onValueChange ={} )

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
                text = user.role,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = user.user.firstName + " . "+ user.user.lastName,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}