package org.fouryouandme.core.entity.page

import arrow.core.Option

data class Page(
    val id: String,
    val title: String,
    val body: String,
    val image: String,
    val link1: Option<Page>,
    val link1Label: Option<String>,
    val link2: Option<Page>,
    val link2Label: Option<String>,
    val externalLinkLabel: String,
    val externalLinkUrl: String
)