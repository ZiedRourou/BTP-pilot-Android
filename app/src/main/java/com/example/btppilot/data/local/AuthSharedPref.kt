package com.example.btppilot.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.btppilot.util.KEY_COMPANY_Id
import com.example.btppilot.util.KEY_EMAIL
import com.example.btppilot.util.KEY_FILENAME
import com.example.btppilot.util.KEY_FIRSTNAME
import com.example.btppilot.util.KEY_IS_ATTACHED_TO_COMPANY
import com.example.btppilot.util.KEY_IS_LOGIN
import com.example.btppilot.util.KEY_ROLE_ID
import com.example.btppilot.util.KEY_TOKEN
import com.example.btppilot.util.KEY_USERID
import com.example.btppilot.util.KEY__USER_ROLE_COMPANY

class AuthSharedPref(context: Context) {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(KEY_FILENAME, Context.MODE_PRIVATE)

    fun isLogin(): Boolean = sharedPreference.getBoolean(KEY_IS_LOGIN, false)

    fun isAttachedToCompany(): Boolean = sharedPreference.getBoolean(KEY_IS_ATTACHED_TO_COMPANY, false)

    fun saveUserInfo(
        token: String,
        userId: Int,
        firstname: String,
        role: String,
        email : String
    ) {
        sharedPreference.edit()
            .putBoolean(KEY_IS_LOGIN, true)
            .putString(KEY_TOKEN, token)
            .putInt(KEY_USERID, userId)
            .putString(KEY_ROLE_ID, role)
            .putString(KEY_FIRSTNAME, firstname)
            .putString(KEY_EMAIL, email)
            .apply()
    }
    fun saveCompanyInfo(
        companyId: Int,
        role: String,
    ) {
        sharedPreference.edit()
            .putBoolean(KEY_IS_ATTACHED_TO_COMPANY, true)
            .putString(KEY__USER_ROLE_COMPANY, role)
            .putInt(KEY_COMPANY_Id, companyId)
            .apply()
    }
    fun getToken(): String = sharedPreference.getString(KEY_TOKEN, "")!!

//    fun getUserId() = sharedPreference.getInt(KEY_USERID, 0)
fun getUserId(): Int {

    val all = sharedPreference.all
    Log.d("PREF_DEBUG", all.toString())

    return sharedPreference.getInt(KEY_USERID, 0)
}
    fun getUserName() = sharedPreference.getString(KEY_FIRSTNAME, "")
    fun getUserRole() = sharedPreference.getString(KEY__USER_ROLE_COMPANY, "")

    fun getCompanyId() = sharedPreference.getInt(KEY_COMPANY_Id, 0)

    fun clearLogin() {
        sharedPreference.edit()
            .remove(KEY_IS_LOGIN)
            .remove(KEY_TOKEN)
            .remove(KEY_USERID)
            .remove(KEY_ROLE_ID)
            .remove(KEY_FIRSTNAME)
            .remove(KEY_EMAIL).apply()
    }
    fun clearCompany(){
        sharedPreference.edit().clear().apply()
    }

}