package com.foryouandme.data.datasource.network

import com.foryouandme.entity.order.Order

val Order.value
    get() = when (this) {
        Order.Descending -> "from desc"
        Order.Ascending -> "from asc"
    }