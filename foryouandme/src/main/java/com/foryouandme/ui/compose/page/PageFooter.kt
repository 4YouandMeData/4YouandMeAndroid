package com.foryouandme.ui.compose.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.drawColoredShadow
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.entity.page.Page
import com.foryouandme.entity.page.PageRef
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton

@Composable
fun PageFooter(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    page: Page,
    pageType: EPageType,
    action1: (PageRef?) -> Unit,
    action2: ((PageRef?) -> Unit)?,
    specialStringAction: ((String) -> Unit)? = null,
    specialStringPageAction: ((String, PageRef?) -> Unit)? = null
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        Modifier
            .fillMaxWidth()
            .height(135.dp)
            .drawColoredShadow(configuration.theme.primaryTextColor.value)
            .background(configuration.theme.secondaryColor.value)
            .padding(horizontal = 25.dp),
    ) {
        if (page.link2Label != null && action2 != null)
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(page.link2Label)
                    }
                },
                style = MaterialTheme.typography.h3,
                color = configuration.theme.fourthTextColor.value,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable { action2(page.link2) }
            )
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

            when {

                page.specialLinkValue != null && specialStringAction != null ->
                    DoubleButton(
                        page = page,
                        firstButtonText = page.specialLinkLabel.orEmpty(),
                        configuration = configuration,
                        firstButtonClick = { specialStringAction(page.specialLinkValue) },
                        secondButtonClick = action1
                    )
                page.externalLinkUrl != null && specialStringPageAction != null ->
                    DoubleButton(
                        page = page,
                        firstButtonText = page.specialLinkLabel.orEmpty(),
                        configuration = configuration,
                        firstButtonClick = {
                            specialStringPageAction(page.externalLinkUrl, page.link1)
                        },
                        secondButtonClick = action1
                    )
                page.link1Label == null ->
                    ImageButton(
                        page = page,
                        pageType = pageType,
                        imageConfiguration = imageConfiguration,
                        onClick = action1
                    )
                else ->
                    SingleButton(
                        page = page,
                        configuration = configuration,
                        onClick = action1
                    )
            }
        }
    }
}

@Composable
private fun SingleButton(
    page: Page,
    configuration: Configuration,
    onClick: (PageRef?) -> Unit
) {
    ForYouAndMeButton(
        text = page.link1Label ?: configuration.text.onboarding.integration.nextDefault,
        backgroundColor = configuration.theme.primaryColorEnd.value,
        textColor = configuration.theme.secondaryColor.value,
        onClick = { onClick(page.link1) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ImageButton(
    page: Page,
    pageType: EPageType,
    imageConfiguration: ImageConfiguration,
    onClick: (PageRef?) -> Unit
) {
    Image(
        painter =
        painterResource(
            id =
            when (pageType) {
                EPageType.INFO -> imageConfiguration.nextStepSecondary()
                EPageType.FAILURE -> imageConfiguration.previousStepSecondary()
                EPageType.SUCCESS -> imageConfiguration.nextStepSecondary()
            }
        ),
        contentDescription = null,
        modifier = Modifier
            .size(70.dp)
            .clickable { onClick(page.link1) }
    )
}

@Composable
private fun DoubleButton(
    page: Page,
    firstButtonText: String,
    configuration: Configuration,
    firstButtonClick: () -> Unit,
    secondButtonClick: (PageRef?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        ForYouAndMeButton(
            text = firstButtonText,
            backgroundColor = configuration.theme.primaryColorEnd.value,
            textColor = configuration.theme.secondaryColor.value,
            onClick = { firstButtonClick() },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(20.dp))
        ForYouAndMeButton(
            text = page.link1Label ?: configuration.text.onboarding.integration.nextDefault,
            backgroundColor = configuration.theme.secondaryColor.value,
            textColor = configuration.theme.primaryColorEnd.value,
            onClick = { secondButtonClick(page.link1) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
private fun PageFooterImagePreview() {
    ForYouAndMeTheme {
        PageFooter(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            page = Page.mock(),
            pageType = EPageType.INFO,
            action1 = { },
            action2 = { }
        )
    }
}

@Preview
@Composable
private fun PageFooterButtonPreview() {
    ForYouAndMeTheme {
        PageFooter(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            page = Page.mock(link1Label = Mock.button),
            pageType = EPageType.INFO,
            action1 = { },
            action2 = { }
        )
    }
}

@Preview
@Composable
private fun PageFooterButtonAction2Preview() {
    ForYouAndMeTheme {
        PageFooter(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            page = Page.mock(link1Label = Mock.button, link2Label = Mock.button),
            pageType = EPageType.INFO,
            action1 = { },
            action2 = { }
        )
    }
}

@Preview
@Composable
private fun PageFooterDoubleButtonSpecialLinkPreview() {
    ForYouAndMeTheme {
        PageFooter(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            page =
            Page.mock(
                link1Label = Mock.button,
                specialLinkLabel = Mock.button,
                specialLinkValue = ""
            ),
            pageType = EPageType.INFO,
            action1 = { },
            action2 = { },
            specialStringAction = { },
        )
    }
}

@Preview
@Composable
private fun PageFooterDoubleButtonExternalLinkPreview() {
    ForYouAndMeTheme {
        PageFooter(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            page =
            Page.mock(
                link1Label = Mock.button,
                specialLinkLabel = Mock.button,
                externalLinkUrl = ""
            ),
            pageType = EPageType.INFO,
            action1 = { },
            action2 = { },
            specialStringPageAction = { _, _ -> },
        )
    }
}