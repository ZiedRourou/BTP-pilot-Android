package com.example.btppilot.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class UserRole { WORKER, MANAGER, CLIENT }

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Filled.Home)
    data object Tasks : Screen("tasks", "Tasks", Icons.Filled.Checklist)
    data object Team : Screen("team", "Team", Icons.Filled.Group)
    data object Options : Screen("options", "Options", Icons.Filled.Settings)

    data object Chantiers : Screen("chantiers", "Chantiers", Icons.Filled.Build)
    data object Reports : Screen("reports", "Rapports", Icons.Filled.Assessment)
}


fun bottomTabsFor(role: UserRole): List<Screen> = when (role) {
    UserRole.WORKER -> listOf(
        Screen.Home,
        Screen.Tasks,
        Screen.Team,
        Screen.Options
    )
    UserRole.MANAGER -> listOf(
        Screen.Home,
        Screen.Chantiers,
        Screen.Team,
        Screen.Options
    )
    UserRole.CLIENT -> listOf(
        Screen.Home,
        Screen.Reports,
        Screen.Team,
        Screen.Options
    )
}



@Composable
fun AppScaffold(
    role: UserRole = UserRole.CLIENT,
    currentRoute: String? = Screen.Home.route,
    onTabClick: (Screen) -> Unit,
    floatingActionButton: @Composable (() -> Unit)? = null,
    topBar: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { topBar?.invoke() },
        floatingActionButton = { floatingActionButton?.invoke() },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            RoleBottomBar(
                role = role,
                currentRoute = currentRoute,
                onTabClick = onTabClick,
            )
        },
        content = content
    )
}

@Composable
fun RoleBottomBar(
    role: UserRole,
    currentRoute: String?,
    onTabClick: (Screen) -> Unit,
) {
    val tabs = remember(role) { bottomTabsFor(role) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 10.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            tabs.forEach { dest ->
                val selected = currentRoute == dest.route

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabClick(dest) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = dest.icon,
                        contentDescription = dest.label,
                        tint = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF64748B),
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = dest.label,
                        fontSize = 12.sp,
                        color = if (selected) MaterialTheme.colorScheme.primary else Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

