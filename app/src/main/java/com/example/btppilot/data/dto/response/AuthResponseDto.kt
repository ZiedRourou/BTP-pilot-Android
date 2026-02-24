package com.example.btppilot.data.dto.response


import com.squareup.moshi.Json

data class AuthResponseDto(
    @Json(name = "status")
    val status: Int,
    @Json(name = "id")
    val userId: Int,
    @Json(name = "token")
    val token: String?
)