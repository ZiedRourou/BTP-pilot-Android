package com.example.btppilot.data.api


import com.example.btppilot.data.dto.requ.RegisterRequestDto
import com.example.btppilot.data.dto.request.InviteUserCompanyRequestDto
import com.example.btppilot.data.dto.request.LoginRequestDto
import com.example.btppilot.data.dto.request.NewCompanyRequestDto
import com.example.btppilot.data.dto.response.AuthResponseDto
import com.example.btppilot.data.dto.response.InviteUserCompanyResponseDto
import com.example.btppilot.data.dto.response.NewCompanyResponseDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDto
import com.example.btppilot.data.dto.response.ProjectResponseByUserCompanyDtoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {

    @POST(ApiRoutes.AUTH_LOGIN)
    suspend fun loginUser(@Body login: LoginRequestDto): Response<AuthResponseDto>

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

    @GET(ApiRoutes.GET_PROJECTS_COMPANY)
    suspend fun getProjectByUserCompany(
        @Header("Authorization") authorization: String,
        @Path("id") companyId: Int,
    ): Response<ProjectResponseByUserCompanyDto>

}