package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.auth.RegisterRequestDto
import com.example.btppilot.data.dto.request.user.InviteUserCompanyRequestDto
import com.example.btppilot.data.dto.request.auth.LoginRequestDto
import com.example.btppilot.data.dto.response.auth.AuthResponseDto
import com.example.btppilot.data.dto.response.user.InviteUserCompanyResponseDto
import com.example.btppilot.data.local.AuthSharedPref
import com.example.btppilot.util.Resource
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val api: ApiInterface,
    private val authSharedPref: AuthSharedPref
) {

    suspend fun loginUser(
        loginInfo: LoginRequestDto
    ): Resource<AuthResponseDto> {

        val response = api.loginUser(loginInfo)

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
                    409 -> "Cet email est déjà utilisé"
                    400 -> "Informations invalides"
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
            api.attachUserToCompany(bearerToken, invitationInfo)
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
                    409 -> "vous etes déja lié a l'entreprise"
                    400 -> "Informations invalides"
                    404 -> "Entreprise non trouvé"
                    else -> "erreur serveur "
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }

    suspend fun inviteUserToMyCompany(
        invitationInfo: InviteUserCompanyRequestDto,
    ): Resource<Unit> {

        val token = authSharedPref.getToken()
        val companyId = authSharedPref.getCompanyId()
        val bearerToken = "Bearer $token"
        val response =
            api.inviteUserToMyCompany(bearerToken, companyId, invitationInfo)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(
                    data = null,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code(),
                message = when (response.code()) {
                    409 -> "déja lié a l'entreprise"
                    400 -> "Informations invalides"
                    404 -> "Entreprise non trouvé"
                    else -> "erreur serveur "
                }
            )
        return Resource.Error(400, "Erreur serveur ")
    }
}
