package com.foryouandme.data.ext

import java.text.SimpleDateFormat
import java.util.*

fun getTimestampUTC(): String =

    Date(System.currentTimeMillis()).toTimestampUTC()

fun getTimestampDateUTC(): Date =
    Date(System.currentTimeMillis())


fun Date.toTimestampUTC(): String {

    val format = "yyyy-MM-dd HH:mm:ss.SSS"
    val sdf = SimpleDateFormat(format, Locale.getDefault())

    return sdf.format(this)

}

fun Date.minusDays(days: Int): Date {

    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, -days)
    return calendar.time

}

