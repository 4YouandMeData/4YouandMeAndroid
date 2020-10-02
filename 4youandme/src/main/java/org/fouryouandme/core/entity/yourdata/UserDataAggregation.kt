package org.fouryouandme.core.entity.yourdata

data class UserDataAggregation(
    val id: String,
    val type: String,
    val title: String,
    val color: String?,
    val data: List<Int?>,
    val xLabels: List<String>,
    val yLabels: List<String>
)