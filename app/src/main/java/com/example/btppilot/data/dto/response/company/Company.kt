package com.example.btppilot.data.dto.response.company


import com.squareup.moshi.Json

data class Company(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "siret")
    val siret: String,
    @Json(name = "isActive")
    val isActive: Boolean,
    @Json(name = "activity")
    val activity: String,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "updatedAt")
    val updatedAt: String
)