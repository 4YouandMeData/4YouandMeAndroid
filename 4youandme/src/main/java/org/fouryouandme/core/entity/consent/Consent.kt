package org.fouryouandme.core.entity.consent

import org.fouryouandme.core.entity.page.Page

data class Consent(
    val questions: List<ConsentQuestion>,
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page,
    val failurePage: Page
)