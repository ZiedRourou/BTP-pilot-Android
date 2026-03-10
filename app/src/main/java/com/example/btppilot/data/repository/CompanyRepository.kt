package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.NewCompanyRequestDto
import com.example.btppilot.data.dto.response.NewCompanyResponseDto
import com.example.btppilot.data.dto.response.company.UserCompany
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import javax.inject.Inject


class CompanyRepository @Inject constructor(
    private val api: ApiInterface,
    private val authSharedPref: AuthSharedPref
) {


    suspend fun createCompanyUser(
        companyData: NewCompanyRequestDto,
    ): Resource<NewCompanyResponseDto> {

            val token = authSharedPref.getToken()
            val bearerToken = "Bearer $token"
            val response =
                api.createNewUserCompany(bearerToken ,companyData)

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


    suspend fun getUsersOfCompany(
    ): Resource<List<UsersOfCompanyItem>> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val companyId = authSharedPref.getCompanyId()
        val response = api.getUsersCompany(bearerToken , companyId )

        if (response.isSuccessful) {
            response.body()?.let { users ->
                return Resource.Success(
                    data = users,
                    code = response.code()
                )
            }
        } else
            return Resource.Error(
                code = response.code() ?: 400,
                message = "erreur"

            )
        return Resource.Error(400, "Erreur serveur ")
    }
}
