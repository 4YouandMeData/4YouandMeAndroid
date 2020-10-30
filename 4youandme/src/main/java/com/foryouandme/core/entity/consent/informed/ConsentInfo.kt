package com.foryouandme.core.entity.consent.informed

import com.foryouandme.core.entity.page.Page

data class ConsentInfo(
    val minimumAnswer: Int,
    val questions: List<ConsentInfoQuestion>,
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page,
    val failurePage: Page
)