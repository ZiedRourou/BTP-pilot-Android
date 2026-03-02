package com.example.btppilot.data.repository

import com.example.btppilot.data.api.ApiInterface
import com.example.btppilot.data.dto.request.NewCompanyRequestDto
import com.example.btppilot.data.dto.response.NewCompanyResponseDto
import com.example.btppilot.util.AuthSharedPref
import com.example.btppilot.util.Resource
import com.squareup.moshi.Moshi
import javax.inject.Inject


class CompanyRepository @Inject constructor(
    private val api: ApiInterface,
    private val moshi: Moshi,
    private val authSharedPref: AuthSharedPref
) {


    suspend fun createCompanyUser(
        companyData: NewCompanyRequestDto,
    ): Resource<NewCompanyResponseDto> {

        return try {
            val token = authSharedPref.getToken()
            val bearerToken = "Bearer $token"
            val response =
                api.createNewUserCompany(bearerToken ,companyData)

            if (response.isSuccessful) {

                response.body()?.let { body ->
                    Resource.Success(body)
                } ?: Resource.Error("Réponse vide du serveur")

            }else {

                val errorCode = response.code()

                val errorJson = response.errorBody()?.string()

                val adapter = moshi.adapter(AuthRepository.ApiError::class.java)
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


    suspend fun createCompanyUser2(
        companyData: NewCompanyRequestDto,
    ): Resource<NewCompanyResponseDto> {

            val token = authSharedPref.getToken()
            val bearerToken = "Bearer $token"
            val response =
                api.createNewUserCompany(bearerToken ,companyData)

        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(code = response.code())
    }
}
