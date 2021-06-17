package com.foryouandme.entity.yourdata

import com.foryouandme.entity.mock.Mock

data class YourDataFilter(
    val id: String,
    val name: String
) {

    companion object {

        fun mock(): YourDataFilter =
            YourDataFilter(
                id = "id",
                name = Mock.name
            )

    }

}