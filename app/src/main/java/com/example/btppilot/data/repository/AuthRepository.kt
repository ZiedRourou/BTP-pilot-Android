package com.example.btppilot.data.repository

import com.example.btppilot.R
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
                    401 -> R.string.user_unknow
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
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
                    409 -> R.string.email_already_used
                    400 -> R.string.info_invalid
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
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
                    409 ->R.string.already_link_to_company
                    400 ->  R.string.info_invalid
                    404 ->  R.string.company_not_found
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
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
                    409 ->R.string.already_link_to_company
                    400 ->  R.string.info_invalid
                    404 ->  R.string.info_invalid
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400,  R.string.error_server)
    }
}
