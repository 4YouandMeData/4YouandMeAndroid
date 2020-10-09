package org.fouryouandme.core.entity.studyinfo

import org.fouryouandme.core.entity.page.Page

data class StudyInfo(
    val informationPage: Page,
    val faqPage: Page,
    val rewardPage: Page
)