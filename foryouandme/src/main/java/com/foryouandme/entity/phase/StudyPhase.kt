package com.foryouandme.entity.phase

import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.page.Page

data class StudyPhase(
    val id: String,
    val name: String,
    val faq: Page?
) {

    companion object {

        fun mock(): StudyPhase =
            StudyPhase(id = "id", name = Mock.name, faq = null)

    }

}