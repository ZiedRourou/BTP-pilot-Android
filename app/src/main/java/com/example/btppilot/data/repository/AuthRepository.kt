package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.requ.RegisterRequestDto
import com.example.btppilot.data.dto.request.LoginRequestDto
import com.example.btppilot.data.dto.response.AuthResponseDto
import com.example.btppilot.util.Resource
import com.squareup.moshi.Moshi
import java.lang.Exception
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val api: ApiInterface,
    private val moshi: Moshi
) {

    data class ApiError(
        val statusCode: Int?,
        val message: Any?,
        val error: String?
    )

    suspend fun loginUser(
        login: LoginRequestDto
    ): Resource<AuthResponseDto> {

        return try {

            val response = api.loginUser(login)

            if (response.isSuccessful) {

                response.body()?.let { body ->
                    Resource.Success(body)
                } ?: Resource.Error("Réponse vide du serveur")

            } else {

                val errorCode = response.code()

                val errorJson = response.errorBody()?.string()

                val adapter = moshi.adapter(ApiError::class.java)
                val apiError = adapter.fromJson(errorJson ?: "")

                val message = when (val msg = apiError?.message) {
                    is String -> msg
                    is List<*> -> msg.joinToString("\n")
                    else -> "Erreur serveur"
                }

                Resource.Error(
                    message = message,
                    code = errorCode
                )
            }

        } catch (e: Exception) {
            Resource.Error("Erreur de connexion au serveur")
        }
    }

    suspend fun registerUser(
        registerInfo: RegisterRequestDto
    ): Resource<AuthResponseDto> {

        return try {

            val response = api.registerUser(registerInfo)

            if (response.isSuccessful) {

                response.body()?.let { body ->
                    Resource.Success(body)
                } ?: Resource.Error("Réponse vide du serveur")

            }else {

                val errorCode = response.code()

                val errorJson = response.errorBody()?.string()

                val adapter = moshi.adapter(ApiError::class.java)
                val apiError = adapter.fromJson(errorJson ?: "")

                val message = when (val msg = apiError?.message) {
                    is String -> msg
                    is List<*> -> msg.joinToString("\n")
                    else -> "Erreur serveur"
                }

                Resource.Error(
                    message = message,
                    code = errorCode
                )
            }
        } catch (e: Exception) {
            Resource.Error("Erreur de connexion au serveur")
        }
    }
}
