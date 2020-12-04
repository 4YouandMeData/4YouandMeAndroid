package com.foryouandme.entity.integration

import com.foryouandme.entity.page.Page

data class Integration(
    val pages: List<Page>,
    val welcomePage: Page,
    val successPage: Page
)