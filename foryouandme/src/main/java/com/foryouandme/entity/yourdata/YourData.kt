package com.foryouandme.entity.yourdata

import com.foryouandme.entity.mock.Mock

data class YourData(
    val id: String,
    val title: String?,
    val body: String?,
    val stars: Float?
) {

    companion object {

        fun mock(): YourData =
            YourData(
                id = "id",
                title = Mock.title,
                body = Mock.body,
                stars = 4f
            )

    }

}