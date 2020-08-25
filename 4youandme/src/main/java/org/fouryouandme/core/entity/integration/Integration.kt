package org.fouryouandme.core.entity.integration

import org.fouryouandme.core.entity.page.Page

data class Integration(
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page
)