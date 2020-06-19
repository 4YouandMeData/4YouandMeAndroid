package org.fouryouandme.core.entity.consent.review

import org.fouryouandme.core.entity.page.Page

data class ConsentReview(
    val title: String,
    val body: String,
    val disagreeModalBody: String,
    val disagreeModalButton: String,
    val pages: List<Page>,
    val welcomePage: Page
)