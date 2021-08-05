package com.foryouandme.entity.studyinfo

import com.foryouandme.entity.page.Page

data class StudyInfo(
    val informationPage: Page?,
    val faqPage: Page?,
    val rewardPage: Page?
) {

    companion object {

        fun mock(): StudyInfo =
            StudyInfo(
                informationPage = Page.mock(),
                faqPage = Page.mock(),
                rewardPage = Page.mock()
            )

    }

}