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

fun String.isSiretValid(): Boolean {
    return Regex(SIRET_REGEX).matches(this)
}

fun Double.toHourMinuteDisplay(): String {

    val hour = this.toInt()
    val minute = ((this - hour) * 60).toInt()

    return "%02dh %02d".format(hour, minute)
}

fun Date.formatDateInFrWord(): String {
    return SimpleDateFormat("EEEE d MMM yyyy", Locale.FRENCH).format(this).toString()
}

fun String.formatStrDateToWordFrDate(): String {
    if (this.isNotEmpty()) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.FRENCH)
        val date = inputFormat.parse(this)
        val outputFormat = SimpleDateFormat("EEEE d MMM yyyy", Locale.FRENCH)
        return outputFormat.format(date!!)
    }
    return ""
}

fun String.formatStrDateToShortDate(): String {
    if (this.isNotEmpty()) {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.FRENCH)
        val date = inputFormat.parse(this)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
        return outputFormat.format(date!!)
    }
    return ""
}

fun String.formatStrDateToIsoAPi(): String {
    if (this.isNotEmpty()) {
        val input = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
        val date = input.parse(this)
        val output = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.FRENCH)
        output.timeZone = TimeZone.getTimeZone("UTC")

        return output.format(date!!)
    }
    return ""
}

