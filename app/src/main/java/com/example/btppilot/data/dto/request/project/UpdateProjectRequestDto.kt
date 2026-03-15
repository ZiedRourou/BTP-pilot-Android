package com.example.btppilot.data.dto.request.project


import com.squareup.moshi.Json
data class UpdateProjectRequestDto(

    @Json(name = "name")
    val name: String?= null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "status")
    val status: String? = null,

    @Json(name = "priority")
    val priority: String? = null,

    @Json(name = "plannedStartDate")
    val plannedStartDate: String?  = null,

    @Json(name = "plannedEndDate")
    val plannedEndDate: String?  = null,

    @Json(name = "managerId")
    val managerId: Int?  = null,

    @Json(name = "clientIds")
    val clientIds: List<Int>? = null,

    @Json(name = "employeeIds")
    val employeeIds: List<Int>?  = null,

    @Json(name = "isActive")
    val isActive: Boolean?  = null
)