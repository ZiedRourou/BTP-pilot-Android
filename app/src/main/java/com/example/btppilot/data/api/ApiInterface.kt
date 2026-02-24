package com.example.btppilot.data.api


import com.example.btppilot.data.dto.request.AuthRequestDto
import com.example.btppilot.data.dto.response.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {


    @POST(ApiRoutes.AUTH_LOGIN)
    suspend fun authLogin(
        @Body registerDto: AuthRequestDto
    ): Response<AuthResponseDto>?

}