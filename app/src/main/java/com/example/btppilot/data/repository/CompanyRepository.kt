package com.example.btppilot.data.repository

import com.example.btppilot.R
import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.company.NewCompanyRequestDto
import com.example.btppilot.data.dto.response.company.Company
import com.example.btppilot.data.dto.response.company.UsersOfCompanyItem
import com.example.btppilot.data.local.AuthSharedPref
import com.example.btppilot.util.Resource
import javax.inject.Inject


class CompanyRepository @Inject constructor(
    private val api: ApiInterface,
    private val authSharedPref: AuthSharedPref
) {

    suspend fun createCompanyUser(
        companyData: NewCompanyRequestDto,
    ): Resource<Company> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val response =
            api.createNewCompany(bearerToken, companyData)

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
                    409 -> R.string.already_link_to_company
                    400 -> R.string.info_invalid
                    404 -> R.string.company_not_found
                    else -> R.string.error_server
                }
            )
        return Resource.Error(400, R.string.error_server)
    }


    suspend fun getUsersOfCompany(
    ): Resource<List<UsersOfCompanyItem>> {

        val token = authSharedPref.getToken()
        val bearerToken = "Bearer $token"
        val companyId = authSharedPref.getCompanyId()
        val response = api.getUsersCompany(bearerToken, companyId)

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
                message =R.string.error_server

            )
        return Resource.Error(400, R.string.error_server)
    }
}
