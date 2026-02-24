package com.example.btppilot.data.dto.request

import com.squareup.moshi.Json

data class AuthRequestDto (
    @Json(name = "email")
    val email: String,
    @Json(name = "password")
    val password: String,
)
