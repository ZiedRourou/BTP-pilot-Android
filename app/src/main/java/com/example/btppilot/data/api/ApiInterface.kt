package com.example.btppilot.data.api


import com.example.btppilot.data.dto.request.auth.RegisterRequestDto
import com.example.btppilot.data.dto.request.user.InviteUserCompanyRequestDto
import com.example.btppilot.data.dto.request.auth.LoginRequestDto
import com.example.btppilot.data.dto.request.company.NewCompanyRequestDto
import com.example.btppilot.data.dto.request.task.TaskRequestDto
import com.example.btppilot.data.dto.request.project.NewProjectRequestDto
import com.example.btppilot.data.dto.request.project.UpdateProjectRequestDto
import com.example.btppilot.data.dto.request.task.UpdateTaskDto
import com.example.btppilot.data.dto.response.auth.AuthResponseDto
import com.example.btppilot.data.dto.response.company.Company
import com.example.btppilot.data.dto.response.user.InviteUserCompanyResponseDto
import com.example.btppilot.data.dto.response.project.ProjectResponseByUserCompanyDto
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.dto.response.project.ProjectByIdResponseDto
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

    @POST(ApiRoutes.API_AUTH_LOGIN_ROUTE)
    suspend fun loginUser(
        @Body login: LoginRequestDto
    ): Response<AuthResponseDto>

    @POST(ApiRoutes.API_AUTH_REGISTER_ROUTE)
    suspend fun registerUser(@Body login: RegisterRequestDto): Response<AuthResponseDto>

    @POST(ApiRoutes.API_INVITE_USER_TO_COMPANY_ROUTE)
    suspend fun attachUserToCompany(
        @Header("Authorization") authorization: String,
        @Body invitationInfo: InviteUserCompanyRequestDto,
    ): Response<InviteUserCompanyResponseDto>

    @POST(ApiRoutes.API_COMPANY_ROUTE)
    suspend fun createNewCompany(
        @Header("Authorization") authorization: String,
        @Body companyData: NewCompanyRequestDto,
    ): Response<Company>

    @GET(ApiRoutes.API_PROJECT_COMPANY_ROUTE)
    suspend fun getProjectByUserCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
    ): Response<ProjectResponseByUserCompanyDto>

    @POST(ApiRoutes.API_PROJECT_COMPANY_ROUTE)
    suspend fun postProject(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
        @Body project: NewProjectRequestDto
    ): Response<Unit>

    @GET(ApiRoutes.API_USER_COMPANY_ROUTE)
    suspend fun getUsersCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
    ): Response<List<UsersOfCompanyItem>>

    @GET(ApiRoutes.API_PROJECT_ROUTE)
    suspend fun getProjectById(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
    ): Response<ProjectByIdResponseDto>

    @GET(ApiRoutes.API_TASK_PROJECT_ROUTE)
    suspend fun getTasksByProjectId(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
    ): Response<List<TasksByProjectDtoItem>>

    @PATCH(ApiRoutes.API_PROJECT_ROUTE)
    suspend fun updateProject(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
        @Body project: UpdateProjectRequestDto
    ): Response<Unit>

    @DELETE(ApiRoutes.API_PROJECT_ROUTE)
    suspend fun deleteProject(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
    ): Response<Unit>

    @GET(ApiRoutes.API_TASK_COMPANY_ROUTE)
    suspend fun fetchTasksByCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
    ): Response<List<TasksByProjectDtoItem>>

    @POST(ApiRoutes.API_TASK_PROJECT_ROUTE)
    suspend fun postNewTask(
        @Header("Authorization") authorization: String,
        @Path("projectId") projectId: Int,
        @Body task: TaskRequestDto
    ): Response<Unit>

    @GET(ApiRoutes.API_TASK_ROUTE)
    suspend fun fetchTaskByID(
        @Header("Authorization") authorization: String,
        @Path("taskId") taskId: Int,
    ): Response<TasksByProjectDtoItem>

    @PATCH(ApiRoutes.API_TASK_ROUTE)
    suspend fun updateTask(
        @Header("Authorization") authorization: String,
        @Path("taskId") taskId: Int,
        @Body task: UpdateTaskDto
    ): Response<Unit>

    @DELETE(ApiRoutes.API_TASK_ROUTE)
    suspend fun deleteTask(
        @Header("Authorization") authorization: String,
        @Path("taskId") taskId: Int,
    ): Response<Unit>


    @POST(ApiRoutes.API_USER_COMPANY_ROUTE)
    suspend fun inviteUserToMyCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
        @Body invitationInfo: InviteUserCompanyRequestDto,
    ): Response<Unit>

}