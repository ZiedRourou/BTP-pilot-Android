package com.example.btppilot.data.dto.request.updateTask


import com.squareup.moshi.Json

data class UpdateTaskDto(
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "estimationHours")
    val estimationHours: Int? = null,
    @Json(name = "plannedStartDate")
    val plannedStartDate: String? = null,
    @Json(name = "plannedEndDate")
    val plannedEndDate: String? = null,
    @Json(name = "doneEndDate")
    val doneEndDate: String? = null,
    @Json(name = "status")
    val status: String? = null,
    @Json(name = "priority")
    val priority: String? = null,
    @Json(name = "assignedUserIds")
    val assignedUserIds: List<Int>? = null,
)