package com.fouryouandme.core.entity.page

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
) {

    fun asList(): MutableList<Page> {

        var page = this

        val items = mutableListOf(this)

        while (page.link1 != null) {

            items.add(page.link1!!)

            page = page.link1!!

        }

        return items

    }


}