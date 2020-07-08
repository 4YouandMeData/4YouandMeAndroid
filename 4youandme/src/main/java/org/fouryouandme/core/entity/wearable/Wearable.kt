package org.fouryouandme.core.entity.wearable

import org.fouryouandme.core.entity.page.Page

data class Wearable(
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page
)