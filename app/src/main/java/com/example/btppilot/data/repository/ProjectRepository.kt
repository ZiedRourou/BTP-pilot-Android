package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.NewCompanyRequestDto
import com.example.btppilot.data.dto.request.ProjectRequestDto
import com.example.btppilot.data.dto.response.NewCompanyResponseDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDtoItem
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
import com.example.btppilot.data.dto.response.project.ProjectResponseDto
import com.example.btppilot.data.dto.response.project.team.UserProjectDtoItem
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


    suspend fun newProject(
        project : ProjectRequestDto
    ): Resource<ProjectResponseDto> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
    val companyId = authSharedPref.getCompanyId()
        val response =
            api.postProject(bearerToken ,companyId,project )

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

    suspend fun fetchProjectById(
        projectId: Int,
    ): Resource<ProjectByIdResponseDto> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"

        val response =
            api.getProjectById(bearerToken ,projectId)

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
                message = "erreur"

            )
        return Resource.Error(400, "Erreur serveur ")
    }


    suspend fun updateProject(
        projectId: Int,
        project : ProjectRequestDto
    ): Resource<Unit> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.updateProject(bearerToken,projectId,project )

        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(
                    data = null,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code() ?: 400,
                message = when (response.code()) {
                    401 -> "erreur"
                    else -> "erreur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }


    suspend fun deleteProject(
        projectId: Int,
    ): Resource<Unit> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.deleteProject(bearerToken,projectId)

        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(
                    data = null,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code() ?: 400,
                message = when (response.code()) {
                    401 -> "erreur"
                    else -> "erreur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }

    suspend fun getUsersOfProject(
        projectId: Int,
    ): Resource<List<UserProjectDtoItem>> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.getUsersProject(bearerToken,projectId)

        if (response.isSuccessful) {
            response.body()?.let {users ->
                return Resource.Success(
                    data = users,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code() ?: 400,
                message = when (response.code()) {
                    401 -> "erreur"
                    else -> "erreur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }
}
