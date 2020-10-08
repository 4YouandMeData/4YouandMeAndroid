package org.fouryouandme.core.entity.page

data class Page(
    val id: String,
    val title: String,
    val body: String,
    val image: String?,
    val link1: Page?,
    val link1Label: String?,
    val link2: Page?,
    val link2Label: String?,
    val externalLinkLabel: String?,
    val externalLinkUrl: String?,
    val specialLinkLabel: String?,
    val specialLinkValue: String?,
    val linkModalLabel: String?,
    val linkModalValue: Page?
)