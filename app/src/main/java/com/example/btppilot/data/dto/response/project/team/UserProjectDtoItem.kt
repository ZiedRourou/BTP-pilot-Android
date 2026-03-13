package com.example.btppilot.data.dto.response.project.team


import com.squareup.moshi.Json

data class UserProjectDtoItem(
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "projectId")
    val projectId: Int,
    @Json(name = "projectRole")
    val projectRole: String,
    @Json(name = "assignedAt")
    val assignedAt: String
)