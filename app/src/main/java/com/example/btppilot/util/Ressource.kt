package com.example.btppilot.util

sealed class Resource<T>(
    val code: Int,
    val data: T? = null,
    val message : String = ""
) {
    class Success<T>(data: T?, code: Int) : Resource<T>(code = code, data = data)
    class Error<T>(code: Int, message: String) : Resource<T>(code = code,message= message)
}

