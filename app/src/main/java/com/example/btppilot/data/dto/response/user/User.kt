package com.example.btppilot.data.dto.response.user


import com.squareup.moshi.Json

data class User(
    @Json(name = "id")
    val id: Int,
    @Json(name = "firstName")
    val firstName: String,
    @Json(name = "lastName")
    val lastName: String
)