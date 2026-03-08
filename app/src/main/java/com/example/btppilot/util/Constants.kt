package com.example.btppilot.util

import androidx.compose.ui.graphics.Color
import com.example.btppilot.ui.theme.PriorityHigh
import com.example.btppilot.ui.theme.PriorityLow
import com.example.btppilot.ui.theme.PriorityMedium
import com.example.btppilot.ui.theme.StatusDone
import com.example.btppilot.ui.theme.StatusInProgress
import com.example.btppilot.ui.theme.StatusTodo

const val KEY_FILENAME = "sharePreferencesAuth"
const val KEY_IS_LOGIN = "isLogin"
const val KEY_IS_ATTACHED_TO_COMPANY = "isAttachedToCompany"
const val KEY_TOKEN = "token"
const val KEY_COMPANY_Id = "compnay_id"
const val KEY__USER_ROLE_COMPANY = "role_company_id"
const val KEY_USERID = "userId"
const val KEY_ROLE_ID = "userId"
const val KEY_FIRSTNAME = "firstname"
const val KEY_EMAIL = "firstname"

enum class UserRole {
    OWNER,
    COLLABORATOR,
    CLIENT
}

const val EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
const val STRONG_PASSWORD_REGEX =
    "^(?=(.*[a-z]){3,})(?=(.*[A-Z]){1,})(?=(.*[0-9]){1,})(?=(.*[!@#]){1,}).{8,}"
const val SIRET_REGEX = "^\\d{14}\$"

enum class ProjectStatus(
    val label: String,
    val color : Color
) {
    PLANNED("Planifié", StatusTodo),
    IN_PROGRESS("En cours", StatusInProgress),
    COMPLETED("Terminé", StatusDone),
    FINISH("Clôturé", StatusDone),
    ALL("Tous", StatusTodo)
}

enum class ProjectPriorities(
    val label: String,
    val color : Color
) {
    LOW("Faible", PriorityLow),
    MEDIUM("Moyen", PriorityMedium),
    HIGH("Important", PriorityHigh),
}

