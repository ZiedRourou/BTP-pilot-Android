package com.example.btppilot.data.dto.response

data class NewCompanyResponseDto (
    val id: Int,
    val name: String,
    val siret: String,
    val isActive: Boolean,
    val activity: String,
    val createdAt : String,
    val updatedAt : String
)
