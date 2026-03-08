package com.example.btppilot.data.dto.response


import com.squareup.moshi.Json

data class UserProject(
    @Json(name = "projectRole")
    val projectRole: String,
    @Json(name = "user")
    val user: User
)