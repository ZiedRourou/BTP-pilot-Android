package com.example.btppilot.data.dto.response

import com.squareup.moshi.Json


data class ProjectResponseByUserCompanyDto (
    @Json(name = "projects")
    val projects: List<ProjectResponseByUserCompanyDtoItem>,
    @Json(name = "tasksInProgress")
    val taskInProgress: Int,
)
