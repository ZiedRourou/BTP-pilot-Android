package com.example.btppilot.data.api


import com.example.btppilot.data.dto.requ.RegisterRequestDto
import com.example.btppilot.data.dto.request.InviteUserCompanyRequestDto
import com.example.btppilot.data.dto.auth.LoginRequestDto
import com.example.btppilot.data.dto.request.NewCompanyRequestDto
import com.example.btppilot.data.dto.request.newTask.TaskRequestDto
import com.example.btppilot.data.dto.request.project.NewProjectRequestDto
import com.example.btppilot.data.dto.request.project.UpdateProjectRequestDto
import com.example.btppilot.data.dto.request.updateTask.UpdateTaskDto
import com.example.btppilot.data.dto.response.auth.AuthResponseDto
import com.example.btppilot.data.dto.response.InviteUserCompanyResponseDto
import com.example.btppilot.data.dto.response.NewCompanyResponseDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDto
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
import com.example.btppilot.data.dto.response.project.ProjectResponseDto
import com.example.btppilot.data.dto.response.project.team.UserProjectDtoItem
import com.example.btppilot.data.dto.response.tasks.TasksByProjectDtoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @POST(ApiRoutes.BTP_API_AUTH_LOGIN)
    suspend fun loginUser(
        @Body login: LoginRequestDto
    ): Response<AuthResponseDto>

    @POST(ApiRoutes.AUTH_REGISTER)
    suspend fun registerUser(@Body login: RegisterRequestDto): Response<AuthResponseDto>

    @POST(ApiRoutes.INVITE_USER_TO_COMPANY)
    suspend fun inviteUserToCompany(
        @Header("Authorization") authorization: String,
        @Body invitationInfo: InviteUserCompanyRequestDto,
    ): Response<InviteUserCompanyResponseDto>

    @POST(ApiRoutes.CREATE_COMPANY)
    suspend fun createNewUserCompany(
        @Header("Authorization") authorization: String,
        @Body companyData: NewCompanyRequestDto,
    ): Response<NewCompanyResponseDto>

    @GET(ApiRoutes.GET_POST_PROJECTS_COMPANY)
    suspend fun getProjectByUserCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
    ): Response<ProjectResponseByUserCompanyDto>

    @POST(ApiRoutes.GET_POST_PROJECTS_COMPANY)
    suspend fun postProject(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
        @Body project: NewProjectRequestDto
    ): Response<ProjectResponseDto>

    @GET(ApiRoutes.GET_USER_COMPANY)
    suspend fun getUsersCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
    ): Response<List<UsersOfCompanyItem>>

    @GET(ApiRoutes.GET_PROJECT_BY_ID)
    suspend fun getProjectById(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
    ): Response<ProjectByIdResponseDto>

    @GET(ApiRoutes.GET_TASK_BY_PROJECT_ID)
    suspend fun getTasksByProjectId(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
    ): Response<List<TasksByProjectDtoItem>>

    @PATCH(ApiRoutes.GET_PROJECT_BY_ID)
    suspend fun updateProject(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
        @Body project: UpdateProjectRequestDto
    ): Response<Unit>

    @DELETE(ApiRoutes.GET_PROJECT_BY_ID)
    suspend fun deleteProject(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
    ): Response<Unit>

    @GET(ApiRoutes.GET_TASK_COMPANY)
    suspend fun fetchsTasksByCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
    ): Response<List<TasksByProjectDtoItem>>


    @POST(ApiRoutes.GET_TASK_BY_PROJECT_ID)
    suspend fun postNewTask(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
        @Body task: TaskRequestDto
    ): Response<TasksByProjectDtoItem>


    @GET(ApiRoutes.GET_TASK)
    suspend fun fetchTaskByID(
        @Header("Authorization") authorization: String,
        @Path("taskId") taskId: Int,
    ): Response<TasksByProjectDtoItem>

    @PATCH(ApiRoutes.GET_TASK)
    suspend fun updateTask(
        @Header("Authorization") authorization: String,
        @Path("taskId") taskId: Int,
        @Body task: UpdateTaskDto
    ): Response<Unit>

    @DELETE(ApiRoutes.GET_TASK)
    suspend fun deleteTask(
        @Header("Authorization") authorization: String,
        @Path("taskId") taskId: Int,
    ): Response<Unit>


    @POST(ApiRoutes.GET_USER_COMPANY)
    suspend fun inviteUserToMyCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
        @Body invitationInfo: InviteUserCompanyRequestDto,
    ): Response<Unit>

}