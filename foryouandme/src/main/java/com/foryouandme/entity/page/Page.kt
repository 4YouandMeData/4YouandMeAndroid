package com.foryouandme.entity.page

data class Page(
    val id: String,
    val title: String,
    val body: String,
    val image: String?,
    val link1: PageRef?,
    val link1Label: String?,
    val link2: PageRef?,
    val link2Label: String?,
    val externalLinkLabel: String?,
    val externalLinkUrl: String?,
    val specialLinkLabel: String?,
    val specialLinkValue: String?,
    val linkModalLabel: String?,
    val linkModalValue: PageRef?
) {

    fun asList(pages: List<Page>): MutableList<Page> {

        var page = pages.firstOrNull { it.id == link1?.id }

        val items = mutableListOf(this)

        while (page != null) {

            items.add(page)
            page = pages.firstOrNull { it.id == page?.link1?.id }

        }

        return items

    }

}


data class PageRef(val id: String)