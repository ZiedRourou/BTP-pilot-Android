package com.example.btppilot.data.dto.response


import com.example.btppilot.data.dto.response.auth.UserDto
import com.squareup.moshi.Json

data class AuthResponseDto(
    @Json(name = "user")
    val user: UserDto,
    @Json(name = "access_token")
    val accessToken: String
)
