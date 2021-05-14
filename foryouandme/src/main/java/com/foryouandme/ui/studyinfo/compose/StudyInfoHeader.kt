package com.foryouandme.ui.studyinfo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

@Composable
fun StudyInfoHeader(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = imageConfiguration.logoStudySecondary()),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
        Text(
            text = configuration.text.tab.studyInfoTitle,
            color = configuration.theme.secondaryColor.value,
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
        )
    }
}

@Preview
@Composable
private fun StudyInfoHeaderPreview() {
    ForYouAndMeTheme(Configuration.mock().toData()) {
        StudyInfoHeader(
            it,
            ImageConfiguration.mock(),
            Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}