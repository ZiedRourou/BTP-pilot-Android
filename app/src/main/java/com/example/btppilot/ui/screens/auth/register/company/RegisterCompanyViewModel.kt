package com.example.btppilot.ui.screens.auth.register.company

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.btppilot.R
import com.example.btppilot.data.dto.request.company.NewCompanyRequestDto
import com.example.btppilot.data.repository.CompanyRepository
import com.example.btppilot.ui.navigation.NavGraph
import com.example.btppilot.ui.screens.shared.eventState.EventState
import com.example.btppilot.data.local.AuthSharedPref
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

        val siretError: Int? = null,
        val nameError: Int? = null,
        val activityError: Int? = null,

        val isLoading: Boolean = false
    )


    private val _companyRegisterStateFlow = MutableStateFlow(NewCompanyInfo())
    val companyRegisterStateFlow = _companyRegisterStateFlow.asStateFlow()

    private val _companyRegisterEventSharedFlow = MutableSharedFlow<EventState>()
    val companyRegisterEventSharedFlow = _companyRegisterEventSharedFlow.asSharedFlow()


    fun onNameChange(value: String) {
        _companyRegisterStateFlow.value = _companyRegisterStateFlow.value.copy(
            name = value,
            nameError = if (value.length > 80) R.string.name_to_long else null
        )
    }

    fun onSiretChange(value: String) {

        if (!value.matches(Regex("^\\d*\$"))) return

        val messageError = when {
            value.length > 14 -> R.string.siret_14_number_required
            value.length == 14 -> null
            else -> null
        }

        _companyRegisterStateFlow.update {
            it.copy(
                siret = value,
                siretError = messageError
            )
        }
    }


    fun onActivityChange(value: String) {
        _companyRegisterStateFlow.value = _companyRegisterStateFlow.value.copy(
            activity = value,
            activityError = if (value.length > 100) R.string.desc_comp_too_long else null
        )
    }


    fun createCompany() {

        if (!validateCompanyDataAndSetError())
            return

        viewModelScope.launch {
            _companyRegisterStateFlow.update {
                it.copy(
                    isLoading = true
                )
            }

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
                    _companyRegisterStateFlow.update {
                        it.copy(
                            isLoading = false
                        )
                    }

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
                    currentData.siret.isBlank() ->R.string.siret_required
                    !currentData.siret.isSiretValid() -> R.string.siret_invalid_format
                    else -> null
                },
                nameError = when {
                    currentData.name.isBlank() -> R.string.name_required
                    else -> null
                },
                activityError = when {
                    currentData.activity.isBlank() -> R.string.activit_required
                    else -> null
                }
            )
        }

        companyRegisterStateFlow.value.let {
            if (it.siretError == null && it.nameError == null  && it.activityError == null )
                return true
        }
        return false

    }
}