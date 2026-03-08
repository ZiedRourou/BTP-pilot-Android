package com.example.btppilot.util

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.util.Date
import java.util.Locale


fun String.isStrongPassword(): Boolean {
    return Regex(STRONG_PASSWORD_REGEX).matches(this)
}

fun String.isEmailValid(): Boolean {
    return Regex(EMAIL_REGEX).matches(this)
}

fun String.isConfirmPasswordValid(password: String): Boolean {
    return this == password
}

fun String.isSiretValid() : Boolean{
    return Regex(SIRET_REGEX).matches(this)
}

fun Date.formatDateFr(): String {
    return  SimpleDateFormat("EEEE d MMM yyyy", Locale.FRENCH).format(this).toString()
}

fun String?.toShortDate(): String {

    if (this.isNullOrBlank()) return ""

    return try {

        val inputFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",
            Locale.getDefault()
        )

        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )

        val date = inputFormat.parse(this)
        outputFormat.format(date!!)

    } catch (e: Exception) {
        ""
    }
}