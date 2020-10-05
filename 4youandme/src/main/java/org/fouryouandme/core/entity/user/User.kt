package org.fouryouandme.core.entity.user

data class User(
    val id: String,
    val email: String?,
    val phoneNumber: String?,
    val daysInStudy: Int,
    val identities: List<String>,
    val onBoardingCompleted: Boolean,
    val token: String
)