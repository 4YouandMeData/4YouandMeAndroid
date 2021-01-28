package com.foryouandme.entity.order

sealed class Order {

    object Descending : Order()

    object Ascending : Order()

}