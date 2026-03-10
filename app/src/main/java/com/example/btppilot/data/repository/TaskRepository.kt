package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val api: ApiInterface,
    private val authSharedPref: AuthSharedPref
) {


    suspend fun getTasksOfProject(
        projectId: Int,
    ): Resource<List<TasksByProjectDtoItem>> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"

        val response =
            api.getTasksByProjectId(bearerToken, projectId)

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

}