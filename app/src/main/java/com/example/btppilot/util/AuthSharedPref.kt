package com.example.btppilot.util

import android.content.Context
import android.content.SharedPreferences

class AuthSharedPref (context: Context){

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences(KEY_FILENAME, Context.MODE_PRIVATE)

    fun isLogin(): Boolean = sharedPreference.getBoolean(KEY_IS_LOGIN, false)

    fun setLogin(token: String, userId: Int) {
        if (token.isNotEmpty()) {
            sharedPreference.edit().putBoolean(KEY_IS_LOGIN, true).apply()
            sharedPreference.edit().putString(KEY_TOKEN, token).apply()
            sharedPreference.edit().putInt(KEY_USERID, userId).apply()
        }
    }

    fun getToken(): String = sharedPreference.getString(KEY_TOKEN, "")!!

    fun getUserId() = sharedPreference.getInt(KEY_USERID, 0)

    fun clearLogin() {
        sharedPreference.edit().remove(KEY_IS_LOGIN).apply()
        sharedPreference.edit().remove(KEY_TOKEN).apply()
    }

}