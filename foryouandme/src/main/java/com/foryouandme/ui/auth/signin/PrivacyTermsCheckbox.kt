package com.foryouandme.ui.auth.signin

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration

@Composable
fun PrivacyTermsCheckbox(
    isChecked: Boolean,
    configuration: Configuration,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
    onPrivacyClicked: () -> Unit = {},
    onTermsClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Transparent,
                uncheckedColor = configuration.theme.secondaryColor.value,
                checkmarkColor = configuration.theme.secondaryColor.value
            )
        )
        Spacer(modifier = Modifier.width(10.dp))
        val annotatedText =
            getPrivacyTermsAnnotatedString(
                configuration.text.phoneVerification.legal,
                configuration.text.phoneVerification.legalPrivacyPolicy,
                configuration.text.phoneVerification.legalTermsOfService,
            )
        ClickableText(
            text = annotatedText,
            style =
            MaterialTheme.typography.body1
                .copy(color = configuration.theme.secondaryColor.value),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                annotatedText.getStringAnnotations(tag = "privacy", start = it, end = it)
                    .firstOrNull()?.let { onPrivacyClicked() }
                annotatedText.getStringAnnotations(tag = "terms", start = it, end = it)
                    .firstOrNull()?.let { onTermsClicked() }
            }
        )
    }
}

private enum class EAppendableType {

    PRIVACY,
    TERMS

}

private fun getPrivacyTermsAnnotatedString(
    text: String,
    privacy: String,
    terms: String,
): AnnotatedString {

    val privacyIndex = text.indexOf(privacy)
    val termsIndex = text.indexOf(terms)

    val privacySplit =
        if (privacyIndex >= 0) text.split(privacy)
        else listOf(text)
    val split =
        if (termsIndex >= 0) privacySplit.flatMap { it.split(terms) }
        else privacySplit

    val firstSpanType =
        if (privacyIndex < 0 && termsIndex < 0) null
        else if (privacyIndex > 0 && termsIndex < 0) EAppendableType.PRIVACY
        else if (privacyIndex < 0 && termsIndex > 0) EAppendableType.TERMS
        else if (privacyIndex > termsIndex) EAppendableType.TERMS
        else EAppendableType.PRIVACY

    val secondSpanType =
        if (privacyIndex < 0 || termsIndex < 0) null
        else if (privacyIndex > termsIndex) EAppendableType.PRIVACY
        else EAppendableType.TERMS

    return buildAnnotatedString {

        append(split.getOrElse(0) { "" })
        if (firstSpanType != null)
            append(firstSpanType, privacy, terms)
        append(split.getOrElse(1) { "" })
        if (secondSpanType != null)
            append(secondSpanType, privacy, terms)
        append(split.getOrElse(2) { "" })
    }

}

private fun AnnotatedString.Builder.append(
    appendableType: EAppendableType,
    privacy: String,
    terms: String
) {

    withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
        when (appendableType) {
            EAppendableType.PRIVACY -> {
                pushStringAnnotation(tag = "privacy", annotation = "privacy")
                append(privacy)
                pop()
            }
            EAppendableType.TERMS -> {
                pushStringAnnotation(tag = "terms", annotation = "terms")
                append(terms)
                pop()
            }
        }
    }

}