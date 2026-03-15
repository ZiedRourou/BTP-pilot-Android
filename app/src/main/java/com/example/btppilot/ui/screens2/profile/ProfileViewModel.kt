package com.example.btppilot.ui.screens2.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.presentation.navigation.NavGraph
import com.example.btppilot.presentation.navigation.Screen
import com.example.btppilot.presentation.screens.shared.uiState.EventState
import com.example.btppilot.util.AuthSharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authSharedPref: AuthSharedPref
) :ViewModel(){


    private val _profileEventSharedFlow = MutableSharedFlow<EventState>()
    val profileEventSharedFlow = _profileEventSharedFlow.asSharedFlow()

    fun logout(){
        authSharedPref.clearCompany()
        viewModelScope.launch{
            _profileEventSharedFlow.emit(
                EventState.RedirectScreen(Screen.Login)
            )
        }
    }

}