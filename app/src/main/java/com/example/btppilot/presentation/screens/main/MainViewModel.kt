package com.example.btppilot.presentation.screens.main

import androidx.lifecycle.ViewModel
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    data class currentUserInfo(
        val firstname: String = "",
        val role: UserRole = UserRole.COLLABORATOR
    )

    private val _mainStateFlow = MutableStateFlow(currentUserInfo())
    val mainStateFlow: StateFlow<currentUserInfo> = _mainStateFlow

  init {
      _mainStateFlow.update {
          it.copy(
              firstname = authSharedPref.getUserName() ?: "ZIZOU",
              role = UserRole.valueOf(authSharedPref.getUserRole() ?: UserRole.COLLABORATOR.name)
          )
      }
  }
}