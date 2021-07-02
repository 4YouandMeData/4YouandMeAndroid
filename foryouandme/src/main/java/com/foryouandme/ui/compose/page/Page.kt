package com.foryouandme.ui.compose.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.entity.page.PageRef
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.MultiSourceImage
import com.foryouandme.ui.compose.text.HtmlText

@Composable
fun Page(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    page: Page,
    pageType: EPageType,
    action1: (PageRef?) -> Unit,
    action2: ((PageRef?) -> Unit)? = null,
    extraStringAction: ((String) -> Unit)? = null,
    extraPageAction: ((PageRef) -> Unit)? = null,
    specialStringAction: ((String) -> Unit)? = null,
    specialStringPageAction: ((String, PageRef?) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(configuration.theme.secondaryColor.value)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 25.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            if (page.image != null)
                MultiSourceImage(
                    source = ImageSource.Base64(page.image),
                    contentDescription = null,
                    modifier = Modifier.size(if (pageType == EPageType.INFO) 60.dp else 100.dp)
                )
            else
                Spacer(modifier = Modifier.size(if (pageType == EPageType.INFO) 60.dp else 100.dp))
            Spacer(modifier = Modifier.height(30.dp))
            HtmlText(
                text = page.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h1,
                color = configuration.theme.primaryTextColor.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            HtmlText(
                text = page.body,
                textAlign =
                when (pageType) {
                    EPageType.INFO -> TextAlign.Start
                    EPageType.FAILURE -> TextAlign.Center
                    EPageType.SUCCESS -> TextAlign.Center
                },
                style = MaterialTheme.typography.body1,
                color = configuration.theme.primaryTextColor.value,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            when {

                extraPageAction != null && page.linkModalValue != null ->
                    Text(
                        text = page.linkModalLabel.orEmpty(),
                        style = MaterialTheme.typography.h3,
                        color = configuration.theme.primaryColorEnd.value,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { extraPageAction(page.linkModalValue) }
                    )

                extraStringAction != null && page.externalLinkUrl != null ->
                    Text(
                        text = page.externalLinkLabel.orEmpty(),
                        style = MaterialTheme.typography.h3,
                        color = configuration.theme.primaryColorEnd.value,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { extraStringAction(page.externalLinkUrl) }
                    )
            }
        }
        PageFooter(
            configuration = configuration,
            imageConfiguration = imageConfiguration,
            page = page,
            pageType = pageType,
            action1 = action1,
            action2 = action2,
            specialStringAction = specialStringAction,
            specialStringPageAction = specialStringPageAction
        )
    }
}