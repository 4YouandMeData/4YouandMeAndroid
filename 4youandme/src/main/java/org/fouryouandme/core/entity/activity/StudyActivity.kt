package org.fouryouandme.core.entity.activity

import arrow.core.Option
import org.fouryouandme.core.entity.configuration.HEXGradient

sealed class StudyActivity

data class QuickActivity(
    val id: String,
    val title: Option<String>,
    val description: Option<String>,
    val button: Option<String>,
    val gradient: Option<HEXGradient>,
    val answer1: Option<QuickActivityAnswer>,
    val answer2: Option<QuickActivityAnswer>,
    val answer3: Option<QuickActivityAnswer>,
    val answer4: Option<QuickActivityAnswer>,
    val answer5: Option<QuickActivityAnswer>,
    val answer6: Option<QuickActivityAnswer>
) : StudyActivity()

data class QuickActivityAnswer(
    val id: String,
    val text: Option<String>,
    val image: Option<String>,
    val selectedImage: Option<String>
)

data class TaskActivity(
    val id: String,
    val title: Option<String>,
    val description: Option<String>,
    val button: Option<String>,
    val gradient: Option<HEXGradient>,
    val image: Option<String>,
    val activityType: Option<String>
) : StudyActivity()