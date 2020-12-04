package com.foryouandme.data.datasource.network

sealed class Order(val value: String) {

    object Descending : Order("from desc")

    object Ascending : Order("from asc")

}