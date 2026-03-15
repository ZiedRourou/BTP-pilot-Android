package com.example.btppilot.data.dto.response.project


import com.squareup.moshi.Json

data class ProjectByIdResponseDto(
    @Json(name = "id")
    val id: Int,
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
    @Json(name = "currentEndDate")
    val currentEndDate: String? = null,
    @Json(name = "companyId")
    val companyId: Int,
    @Json(name = "userProjects")
    val userProjects: List<UserProject>
)