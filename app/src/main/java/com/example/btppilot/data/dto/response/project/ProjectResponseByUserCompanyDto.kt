package com.example.btppilot.data.dto.response.project

import com.squareup.moshi.Json


data class ProjectResponseByUserCompanyDto (
    @Json(name = "projects")
    val projects: List<ProjectResponseByUserCompanyDtoItem>,
    @Json(name = "weekStats")
    val weekStats: WeekStats
)
