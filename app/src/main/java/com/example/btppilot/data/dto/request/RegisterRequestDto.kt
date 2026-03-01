package com.example.btppilot.data.dto.requ

import com.example.btppilot.util.UserRole


data class RegisterRequestDto (
    val firstName: String,
    val lastName: String? = "test",
    val email: String,
    val password: String,
    val role: String,
)