package com.example.btppilot.data.dto.response


import com.squareup.moshi.Json

data class AuthResponseDto(
    val user: UserDto,
    val accessToken: String
)
