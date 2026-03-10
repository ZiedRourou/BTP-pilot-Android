package com.example.btppilot.data.dto.response.company


import com.squareup.moshi.Json

data class UserCompany(
    @Json(name = "id")
    val id: Int,
    @Json(name = "email")
    val email: String,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String
)