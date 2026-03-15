package com.example.btppilot.data.dto.request.user

import com.example.btppilot.util.UserRole


data class InviteUserCompanyRequestDto (
    val email: String,
    val roleCompany: UserRole,
)
