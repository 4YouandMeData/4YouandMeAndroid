package com.foryouandme.core.ext.html

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.widget.TextView


fun TextView.setHtmlText(html: String, enableLink: Boolean = true) {

    html.customizeListTags().fromHtml(CustomTagHandler())
        .also { if (enableLink) movementMethod = LinkMovementMethod.getInstance() }
        .let { setText(it, TextView.BufferType.SPANNABLE) }
}

private fun String.fromHtml(tagHandler: Html.TagHandler? = null): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY, null, tagHandler)
    else
        Html.fromHtml(this, null, tagHandler)

private fun String.customizeListTags(): String =
    this.replace("<ul", "<${CustomTagHandler.UL_TAG}")
        .replace("</ul>", "</${CustomTagHandler.UL_TAG}>")
        .replace("<ol", "<${CustomTagHandler.OL_TAG}")
        .replace("</ol>", "</${CustomTagHandler.OL_TAG}>")
        .replace("<li", "<${CustomTagHandler.LI_TAG}")
        .replace("</li>", "</${CustomTagHandler.LI_TAG}>")
