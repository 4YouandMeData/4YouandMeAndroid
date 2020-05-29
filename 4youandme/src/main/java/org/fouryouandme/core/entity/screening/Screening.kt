package org.fouryouandme.core.entity.screening

import org.fouryouandme.core.entity.page.Page

data class Screening(
    val questions: List<ScreeningQuestion>? = null,
    val pages: List<Page>? = null,
    val welcomePage: Page? = null,
    val successPage: Page? = null,
    val failurePage: Page? = null
)