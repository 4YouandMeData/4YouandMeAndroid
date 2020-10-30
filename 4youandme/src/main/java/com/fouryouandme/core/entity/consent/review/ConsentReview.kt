package com.fouryouandme.core.entity.consent.review

import com.fouryouandme.core.entity.page.Page

data class ConsentReview(
    val title: String,
    val body: String,
    val pagesSubtitle: String,
    val disagreeModalBody: String,
    val disagreeModalButton: String,
    val pages: List<Page>,
    val welcomePage: Page
)