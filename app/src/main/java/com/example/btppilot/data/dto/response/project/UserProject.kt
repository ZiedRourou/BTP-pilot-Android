package com.example.btppilot.data.dto.response.project


import com.example.btppilot.data.dto.response.user.User
import com.squareup.moshi.Json

data class UserProject(
    @Json(name = "projectRole")
    val projectRole: String,
    @Json(name = "user")
    val user: User
)