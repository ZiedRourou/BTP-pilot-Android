package com.example.btppilot.data.dto.response.tasks


import com.squareup.moshi.Json

data class TasksByProjectDtoItem(
    @Json(name = "id")
    val id: Int,
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
    @Json(name = "isClientTicket")
    val isClientTicket: Boolean,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "priority")
    val priority: String,
    @Json(name = "projectId")
    val projectId: Int,
    @Json(name = "createdById")
    val createdById: Int,
    @Json(name = "userId")
    val userId: Int? = null,
    @Json(name = "assignments")
    val assignments: List<Assignment>
)