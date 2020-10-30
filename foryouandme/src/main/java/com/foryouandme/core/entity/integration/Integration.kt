package com.foryouandme.core.entity.integration

import com.foryouandme.core.entity.page.Page

data class Integration(
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page
)