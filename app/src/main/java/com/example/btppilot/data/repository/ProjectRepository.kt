package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.NewCompanyRequestDto
import com.example.btppilot.data.dto.response.NewCompanyResponseDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import javax.inject.Inject

class ProjectRepository  @Inject constructor(
    private val api: ApiInterface,
    private val authSharedPref: AuthSharedPref
) {


    suspend fun fetchProjectsByUser(
        companyId: Int,
    ): Resource<ProjectResponseByUserCompanyDto> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"

        val response =
            api.getProjectByUserCompany(bearerToken ,companyId)

        if (response.isSuccessful) {
            response.body()?.let { projectsData ->
                return Resource.Success(
                    data = projectsData,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code() ?: 400,
                message = when (response.code()) {
                    401 -> "Email déjà utilisé"
                    else -> "erreur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }
}
