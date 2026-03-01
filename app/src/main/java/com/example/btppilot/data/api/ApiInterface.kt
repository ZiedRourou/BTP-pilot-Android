package com.example.btppilot.data.api


import com.example.btppilot.data.dto.requ.RegisterRequestDto
import com.example.btppilot.data.dto.request.LoginRequestDto
import com.example.btppilot.data.dto.response.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {

    @POST(ApiRoutes.AUTH_LOGIN)
    suspend fun loginUser(@Body login : LoginRequestDto): Response<AuthResponseDto>

    @POST(ApiRoutes.AUTH_REGISTER)
    suspend fun registerUser(@Body login : RegisterRequestDto): Response<AuthResponseDto>

}