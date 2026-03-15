package com.example.btppilot.data.dto.request.task


import com.squareup.moshi.Json

data class TaskRequestDto(
    @Json(name = "title")
    val title: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "estimationHours")
    val estimationHours: Int,
    @Json(name = "plannedStartDate")
    val plannedStartDate: String,
    @Json(name = "plannedEndDate")
    val plannedEndDate: String,
    @Json(name = "doneEndDate")
    val doneEndDate: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "priority")
    val priority: String,
    @Json(name = "assignedUserIds")
    val assignedUserIds: List<Int>
)