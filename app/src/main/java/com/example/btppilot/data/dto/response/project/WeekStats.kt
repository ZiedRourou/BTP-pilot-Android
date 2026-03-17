package com.example.btppilot.data.dto.response.project


import com.squareup.moshi.Json

data class WeekStats(
    @Json(name = "late")
    val late: Int,
    @Json(name = "inProgress")
    val inProgress: Int,
    @Json(name = "todo")
    val todo: Int
)