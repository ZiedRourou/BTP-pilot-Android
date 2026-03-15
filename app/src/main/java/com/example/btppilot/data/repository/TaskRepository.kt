package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.task.TaskRequestDto
import com.example.btppilot.data.dto.request.task.UpdateTaskDto
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import com.example.btppilot.data.local.AuthSharedPref
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

    suspend fun getTasksOfCompany(

    ): Resource<List<TasksByProjectDtoItem>> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val companyId = authSharedPref.getCompanyId()
        val response =
            api.fetchTasksByCompany(bearerToken, companyId)

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

    suspend fun postNewTask(
        projectId: Int,
        taskRequestDto: TaskRequestDto
    ): Resource<TasksByProjectDtoItem> {
        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"

        val response =
            api.postNewTask(bearerToken, projectId, taskRequestDto)

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

    suspend fun getTaskById(
        taskId: Int
    ): Resource<TasksByProjectDtoItem> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.fetchTaskByID(bearerToken, taskId)

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

    suspend fun updateTask(
        taskId: Int,
        taskRequestDto: UpdateTaskDto
    ): Resource<Unit> {
        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"

        val response =
            api.updateTask(bearerToken, taskId, taskRequestDto)

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
                    401 -> "Email déjà utilisé"
                    else -> "erreur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }

    suspend fun deleteTask(
        taskId: Int,
    ): Resource<Unit> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.deleteTask(bearerToken, taskId)

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

}