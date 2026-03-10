package com.example.btppilot.data.dto.request


import com.squareup.moshi.Json

data class ProjectRequestDto(
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "priority")
    val priority: String,
    @Json(name = "plannedStartDate")
    val plannedStartDate: String,
    @Json(name = "plannedEndDate")
    val plannedEndDate: String,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "isActive")
    val isActive: Boolean
)