package com.example.btppilot.data.dto.response.company


import com.squareup.moshi.Json

data class UsersOfCompanyItem(
    @Json(name = "role")
    val role: String,
    @Json(name = "user")
    val user: UserCompany
)