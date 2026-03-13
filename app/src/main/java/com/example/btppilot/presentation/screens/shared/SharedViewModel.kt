package com.example.btppilot.presentation.screens.shared

import androidx.lifecycle.ViewModel
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.UserProjectRole
import com.example.btppilot.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref
) : ViewModel() {
    data class UserInfoState(
        val userFirstname: String = "",
        val userRole: UserRole = UserRole.CLIENT,
        val userProjectRole: UserProjectRole = UserProjectRole.CLIENT
    )

    private val _userInfoStateFlow = MutableStateFlow(UserInfoState())
    val userInfoStateFlow = _userInfoStateFlow.asStateFlow()

    init {
        setUserInfo()
    }

    private fun setUserInfo(){
        _userInfoStateFlow.update {
            it.copy(
                userFirstname = authSharedPref.getUserName() ?: UserRole.COLLABORATOR.name,
                userRole = UserRole.valueOf(authSharedPref.getUserRole()?: UserRole.CLIENT.name)
            )
        }
    }

    fun isAuthorizedToEditProject(project: ProjectResponseByUserCompanyDtoItem): Boolean {
        val currentUserId = authSharedPref.getUserId()

        if (userInfoStateFlow.value.userRole == UserRole.OWNER) {
            return true
        }

        return project.userProjects.firstOrNull {
            it.projectRole == UserProjectRole.MANAGER.name
                    && it.user.id == currentUserId
        } != null
    }

}

