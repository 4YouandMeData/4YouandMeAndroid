package org.fouryouandme.core.entity.page

data class Page(
    val title: String,
    val body: String,
    val image: String,
    val link1Label: String,
    val link2Label: String,
    val externalLinkLabel: String,
    val externalLinkUrl: String
)