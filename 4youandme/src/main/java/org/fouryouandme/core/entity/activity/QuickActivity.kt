package org.fouryouandme.core.entity.activity

data class QuickActivity(
    val id: String,
    val title: String,
    val description: String,
    val button: String,
    val answer1: QuickActivityAnswer,
    val answer2: QuickActivityAnswer,
    val answer3: QuickActivityAnswer,
    val answer4: QuickActivityAnswer,
    val answer5: QuickActivityAnswer,
    val answer6: QuickActivityAnswer
)