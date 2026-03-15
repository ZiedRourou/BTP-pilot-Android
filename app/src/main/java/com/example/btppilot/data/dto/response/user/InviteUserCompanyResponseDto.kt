package com.example.btppilot.data.dto.response.user


data class InviteUserCompanyResponseDto(

    val userId: Int,
    val isActive: Boolean,
    val startDate: String,
    val role: String,
    val companyId: Int
)