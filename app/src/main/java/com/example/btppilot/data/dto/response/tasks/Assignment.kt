package com.example.btppilot.data.dto.response.tasks


import com.example.btppilot.data.dto.response.User
import com.squareup.moshi.Json

data class Assignment(
    @Json(name = "taskId")
    val taskId: Int,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "assignedAt")
    val assignedAt: String,
    @Json(name = "user")
    val user: User
)