package com.foryouandme.ui.auth.signin

import android.content.Context
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.foryouandme.R
import com.giacomoparisi.spandroid.Span
import com.giacomoparisi.spandroid.SpanDroid

private enum class ESpanType {

    PRIVACY,
    TERMS

}

fun getPrivacyTermsSpan(
    text: String,
    privacy: String,
    terms: String,
    color: Int,
    context: Context,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit
): SpannableString {

    val privacyIndex = if(privacy.isEmpty()) -1 else text.indexOf(privacy)
    val termsIndex = if(terms.isEmpty()) -1 else text.indexOf(terms)

    val privacySplit =
        if (privacyIndex >= 0) text.split(privacy)
        else listOf(text)
    val split =
        if (termsIndex >= 0) privacySplit.flatMap { it.split(terms) }
        else privacySplit

    val firstSpanType =
        if (privacyIndex < 0 && termsIndex < 0) null
        else if (privacyIndex > 0 && termsIndex < 0) ESpanType.PRIVACY
        else if (privacyIndex < 0 && termsIndex > 0) ESpanType.TERMS
        else if (privacyIndex > termsIndex) ESpanType.TERMS
        else ESpanType.PRIVACY

    val secondSpanType =
        if (privacyIndex < 0 || termsIndex < 0) null
        else if (privacyIndex > termsIndex) ESpanType.PRIVACY
        else ESpanType.TERMS

    val span =
        SpanDroid
            .span()
            .append(
                split.getOrElse(0) { "" },
                Span.Typeface(R.font.helvetica, context),
            )

    if (firstSpanType != null)
        span.append(firstSpanType, privacy, terms, color, context, onPrivacyClick, onTermsClick)

    span.append(
        split.getOrElse(1) { "" },
        Span.Typeface(R.font.helvetica, context)
    )

    if (secondSpanType != null)
        span.append(secondSpanType, privacy, terms, color, context, onPrivacyClick, onTermsClick)

    span.append(
        split.getOrElse(2) { "" },
        Span.Typeface(R.font.helvetica, context)
    )

    return span.toSpannableString()

}

private fun SpanDroid.append(
    spanType: ESpanType,
    privacy: String,
    terms: String,
    color: Int,
    context: Context,
    onPrivacyClick: () -> Unit,
    onTermsClick: () -> Unit
): SpanDroid =
    append(
        when (spanType) {
            ESpanType.PRIVACY -> privacy
            ESpanType.TERMS -> terms
        },
        Span.Click {
            when (spanType) {
                ESpanType.PRIVACY -> onPrivacyClick()
                ESpanType.TERMS -> onTermsClick()
            }
        },
        Span.Typeface(R.font.helvetica, context),
        Span.Custom(ForegroundColorSpan(color)),
        Span.Underline
    )