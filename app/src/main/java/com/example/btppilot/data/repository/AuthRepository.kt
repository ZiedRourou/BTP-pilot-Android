package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.requ.RegisterRequestDto
import com.example.btppilot.data.dto.request.InviteUserCompanyRequestDto
import com.example.btppilot.data.dto.request.LoginRequestDto
import com.example.btppilot.data.dto.response.AuthResponseDto
import com.example.btppilot.data.dto.response.InviteUserCompanyResponseDto
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import com.squareup.moshi.Moshi
import java.lang.Exception
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val api: ApiInterface,
    private val moshi: Moshi,
    private val authSharedPref: AuthSharedPref
) {


    data class ApiError(
        val statusCode: Int?,
        val message: Any?,
        val error: String?
    )

    suspend fun loginUser(
        login: LoginRequestDto
    ): Resource<AuthResponseDto> {


        val response = api.loginUser(login)

        if (response.isSuccessful) {
            response.body()?.let { userCredential ->
                return Resource.Success(
                    data = userCredential,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code(),
                message = when (response.code()) {
                    401 -> "Utilisateur inconnu"
                    else -> "erreur serveur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }

    suspend fun registerUser(
        registerInfo: RegisterRequestDto
    ): Resource<AuthResponseDto> {

        val response = api.registerUser(registerInfo)

        if (response.isSuccessful) {
            response.body()?.let { userCredential ->
                return Resource.Success(
                    data = userCredential,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code() ?: 400,
                message = when (response.code()) {
                    401 -> "Email déjà utilisé"
                    else -> "erreur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }


    suspend fun inviteUserToCompany(
        invitationInfo: InviteUserCompanyRequestDto,
    ): Resource<InviteUserCompanyResponseDto> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.inviteUserToCompany(bearerToken, invitationInfo)
        if (response.isSuccessful) {
            response.body()?.let { userCredential ->
                return Resource.Success(
                    data = userCredential,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code(),
                message = when (response.code()) {
                    401 -> "déjà lié a cet entreprise"
                    else -> "erreur"
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }
}
