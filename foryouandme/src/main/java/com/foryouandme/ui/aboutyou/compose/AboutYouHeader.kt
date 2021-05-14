package com.foryouandme.ui.aboutyou.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon

@Composable
fun AboutYouHeader(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {

    Box(
        modifier = modifier
    ) {
        ForYouAndMeTopAppBar(
            icon = TopAppBarIcon.CloseSecondary,
            imageConfiguration = imageConfiguration,
            onBack = onBack
        )
        Image(
            painter = painterResource(id = imageConfiguration.logoStudySecondary()),
            contentDescription = null,
            modifier = Modifier.width(65.dp).align(Alignment.Center)
        )
        Text(
            text = configuration.text.profile.title,
            color = configuration.theme.secondaryColor.value,
            style = MaterialTheme.typography.h1,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 30.dp)
        )
    }
}

@Preview
@Composable
private fun AboutYouHeaderPreview() {
    ForYouAndMeTheme(Configuration.mock().toData()) {
        AboutYouHeader(
            it,
            ImageConfiguration.mock(),
            Modifier.fillMaxWidth().height(200.dp)
        )
    }
}