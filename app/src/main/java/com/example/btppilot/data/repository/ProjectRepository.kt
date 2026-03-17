package com.example.btppilot.data.repository

import com.example.btppilot.R
import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.project.NewProjectRequestDto
import com.example.btppilot.data.dto.request.project.UpdateProjectRequestDto
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDto
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
import com.example.btppilot.data.local.AuthSharedPref
import com.example.btppilot.util.Resource
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val api: ApiInterface,
    private val authSharedPref: AuthSharedPref
) {


    suspend fun fetchProjectsByUser(
    ): Resource<ProjectResponseByUserCompanyDto> {

        val companyId = authSharedPref.getCompanyId()
        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"

        val response =
            api.getProjectByUserCompany(bearerToken, companyId)

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
                    401 -> R.string.email_already_used
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
    }


    suspend fun newProject(
        project: NewProjectRequestDto
    ): Resource<Unit> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val companyId = authSharedPref.getCompanyId()
        val response =
            api.postProject(bearerToken, companyId, project)

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
                    401 -> R.string.email_already_used
                    else ->R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
    }

    suspend fun fetchProjectById(
        projectId: Int,
    ): Resource<ProjectByIdResponseDto> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"

        val response =
            api.getProjectById(bearerToken, projectId)

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
                message =  R.string.error_server

            )
        return Resource.Error(400, R.string.error_server)
    }


    suspend fun updateProject(
        projectId: Int,
        project: UpdateProjectRequestDto
    ): Resource<Unit> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.updateProject(bearerToken, projectId, project)

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

                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
    }


    suspend fun deleteProject(
        projectId: Int,
    ): Resource<Unit> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.deleteProject(bearerToken, projectId)

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
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
    }


}
