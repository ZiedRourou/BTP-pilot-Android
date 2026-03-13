package com.example.btppilot.util

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import java.util.Calendar
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


fun toIsoDate(date: String): String {
    val input = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val output = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    output.timeZone = TimeZone.getTimeZone("UTC")

    val parsed = input.parse(date)!!
    val calendar = Calendar.getInstance()
    calendar.time = parsed

    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 0)

    return output.format(calendar.time)
}

fun isoToFrenchDate(date: String?): String {

    if (date.isNullOrBlank()) return ""

    return try {

        val inputFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat =
            SimpleDateFormat("d MMMM yyyy", Locale.FRENCH)

        val parsedDate = inputFormat.parse(date)

        outputFormat.format(parsedDate!!)

    } catch (e: Exception) {
        ""
    }
}

fun isoToUiDate(isoDate: String): String {
    return try {
        val apiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        apiFormat.timeZone = TimeZone.getTimeZone("UTC")

        val uiFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val parsedDate = apiFormat.parse(isoDate)
        parsedDate?.let { uiFormat.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}

fun uiDateToIso(uiDate: String): String {
    return try {
        val uiFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val apiFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        apiFormat.timeZone = TimeZone.getTimeZone("UTC")

        val parsedDate = uiFormat.parse(uiDate)
        parsedDate?.let { apiFormat.format(it) } ?: ""
    } catch (e: Exception) {
        ""
    }
}