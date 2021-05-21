package com.foryouandme.entity.yourdata

import com.foryouandme.entity.mock.Mock

data class UserDataAggregation(
    val id: String,
    val type: String,
    val title: String,
    val color: String?,
    val data: List<Float?>,
    val xLabels: List<String>,
    val yLabels: List<String>
) {

    companion object {

        fun mock(): UserDataAggregation =
            UserDataAggregation(
                id = "id",
                type = "type",
                title = Mock.title,
                color = null,
                data = listOf(1f, 2f, 3f),
                xLabels = listOf("x1", "x2", "x3"),
                yLabels = listOf("y1", "y2", "y3")
            )

    }

}