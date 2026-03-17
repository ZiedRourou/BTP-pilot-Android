package com.example.btppilot.data.dto.response.auth


import com.example.btppilot.data.dto.response.company.Company
import com.squareup.moshi.Json

data class UserCompany(
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "isActive")
    val isActive: Boolean,
    @Json(name = "startDate")
    val startDate: String,
    @Json(name = "role")
    val role: String,
    @Json(name = "companyId")
    val companyId: Int,
    @Json(name = "company")
    val company: Company
)
