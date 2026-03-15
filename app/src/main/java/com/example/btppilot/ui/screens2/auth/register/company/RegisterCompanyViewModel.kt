package com.example.btppilot.ui.screens2.auth.register.company

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.data.dto.request.company.NewCompanyRequestDto
import com.example.btppilot.data.repository.CompanyRepository
import com.example.btppilot.presentation.navigation.NavGraph
import com.example.btppilot.presentation.screens.shared.uiState.EventState
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import com.example.btppilot.util.UserRole
import com.example.btppilot.util.isSiretValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterCompanyViewModel @Inject constructor(
    private val companyRepository: CompanyRepository,
    private val authSharedPref: AuthSharedPref
) : ViewModel() {

    data class NewCompanyInfo(
        val siret: String = "",
        val name: String = "",
        val activity: String = "",

        val siretError: String? = null,
        val nameError: String? = null,
        val activityError: String? = null,

        val isLoading: Boolean = false
    )


    private val _companyRegisterStateFlow = MutableStateFlow(NewCompanyInfo())
    val companyRegisterStateFlow = _companyRegisterStateFlow.asStateFlow()

    private val _companyRegisterEventSharedFlow = MutableSharedFlow<EventState>()
    val companyRegisterEventSharedFlow = _companyRegisterEventSharedFlow.asSharedFlow()


    fun onNameChange(value: String) {
        _companyRegisterStateFlow.value = _companyRegisterStateFlow.value.copy(
            name = value,
            nameError = if(value.length >80) "Nom trop long" else null
        )
    }

    fun onSiretChange(value: String) {
        _companyRegisterStateFlow.value = _companyRegisterStateFlow.value.copy(
            siret = value,
            siretError =  if(value.length >=15) "Le Siret doit contenir 14 chiffres" else null
        )
    }

    fun onActivityChange(value: String) {
        _companyRegisterStateFlow.value = _companyRegisterStateFlow.value.copy(
            activity = value,
            activityError = if(value.length >100) "Description trop longue" else null
        )
    }


    fun createCompany() {

        if(!validateCompanyDataAndSetError())
            return

        viewModelScope.launch {

            _companyRegisterStateFlow.value = _companyRegisterStateFlow.value.copy(isLoading = true)
            delay(1000)

            val result = withContext(Dispatchers.IO) {
                companyRepository.createCompanyUser(
                    NewCompanyRequestDto(
                        siret = companyRegisterStateFlow.value.siret,
                        name = companyRegisterStateFlow.value.name,
                        activity = companyRegisterStateFlow.value.activity
                    )
                )
            }

            when (result) {

                is Resource.Success -> {
                    _companyRegisterStateFlow.value =
                        _companyRegisterStateFlow.value.copy(isLoading = false)

                    _companyRegisterEventSharedFlow.emit(
                        EventState.RedirectGraph(NavGraph.MainGraph)
                    )

                    authSharedPref.saveCompanyInfo(
                        companyId = result.data?.id ?: 0,
                        role = UserRole.OWNER.name
                    )
                }

                is Resource.Error -> {

                    _companyRegisterStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

                    _companyRegisterEventSharedFlow.emit(
                        EventState.ShowMessageSnackBar(result.message)
                    )
                }
            }
        }
    }

    private fun validateCompanyDataAndSetError(
    ): Boolean {

        val currentData = companyRegisterStateFlow.value

        _companyRegisterStateFlow.update {
            it.copy(
                siretError = when {
                    currentData.siret.isBlank() -> "Siret requis"
                    !currentData.siret.isSiretValid() -> "Siret invalide (14 chiffres)"
                    else -> null
                },
                nameError = when {
                    currentData.name.isBlank() -> "Nom requis"
                    else -> null
                },
                activityError = when {
                    currentData.activity.isBlank() -> "Activité requis"
                    else -> null
                }
            )
        }

        companyRegisterStateFlow.value.let {
            if (it.siretError.isNullOrEmpty() && it.nameError.isNullOrEmpty() && it.activityError.isNullOrEmpty())
                return true
        }
        return false

    }
}