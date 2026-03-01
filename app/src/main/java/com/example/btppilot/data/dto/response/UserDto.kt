package com.example.btppilot.data.dto.response


import com.squareup.moshi.Json

data class UserDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String,
    @Json(name = "email")
    val email: String,
    @Json(name = "avatarUrl")
    val avatarUrl: String?,
    @Json(name = "isActive")
    val isActive: Boolean,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "updatedAt")
    val updatedAt: String?,
    @Json(name = "role")
    val role: String
)

