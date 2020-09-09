package org.fouryouandme.core.entity.page

import arrow.core.Option

data class Page(
    val id: String,
    val title: String,
    val body: String,
    val image: Option<String>,
    val link1: Option<Page>,
    val link1Label: Option<String>,
    val link2: Option<Page>,
    val link2Label: Option<String>,
    val externalLinkLabel: Option<String>,
    val externalLinkUrl: Option<String>,
    val specialLinkLabel: Option<String>,
    val specialLinkValue: Option<String>,
    val linkModalLabel: Option<String>,
    val linkModalValue: Option<Page>
)