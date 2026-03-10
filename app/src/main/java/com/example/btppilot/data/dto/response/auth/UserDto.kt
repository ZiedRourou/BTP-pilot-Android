package com.example.btppilot.data.dto.response.auth

import com.example.btppilot.data.dto.response.auth.UserCompany

data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val avatarUrl: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String?,
    val role: String,
    val userCompanies: List<UserCompany>? = null
)

