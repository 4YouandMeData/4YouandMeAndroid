package org.fouryouandme.core.entity.consent.informed

import org.fouryouandme.core.entity.page.Page

data class ConsentInfo(
    val questions: List<ConsentInfoQuestion>,
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page,
    val failurePage: Page
)