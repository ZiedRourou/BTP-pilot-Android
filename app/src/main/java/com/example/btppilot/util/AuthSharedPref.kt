package com.example.btppilot.util

import android.content.Context
import android.content.SharedPreferences

class AuthSharedPref(context: Context) {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(KEY_FILENAME, Context.MODE_PRIVATE)

    fun isLogin(): Boolean = sharedPreference.getBoolean(KEY_IS_LOGIN, false)

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
    fun getToken(): String = sharedPreference.getString(KEY_TOKEN, "")!!

    fun getUserId() = sharedPreference.getInt(KEY_USERID, 0)

    fun clearLogin() {
        sharedPreference.edit()
            .clear()
            .apply()
    }

}