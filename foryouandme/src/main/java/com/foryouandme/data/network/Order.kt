package com.foryouandme.data.network

sealed class Order(val value: String) {

    object Descending : Order("from desc")

    object Ascending : Order("from asc")

}