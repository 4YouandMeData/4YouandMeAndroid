package org.fouryouandme.core.entity.screening

import org.fouryouandme.core.entity.page.Page

data class Screening(
    val minimumAnswer: Int,
    val questions: List<ScreeningQuestion>,
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page,
    val failurePage: Page
)