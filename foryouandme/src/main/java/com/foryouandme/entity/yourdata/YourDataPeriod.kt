package com.foryouandme.entity.yourdata

sealed class YourDataPeriod(val value: String) {

    object Week : YourDataPeriod("last_week")
    object Month : YourDataPeriod("last_month")
    object Year : YourDataPeriod("last_year")

}