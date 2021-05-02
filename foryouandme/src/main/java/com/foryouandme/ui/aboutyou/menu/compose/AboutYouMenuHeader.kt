package com.foryouandme.ui.aboutyou.menu.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.imageConfiguration
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun AboutYouMenuHeader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f)
    ) {
        Image(
            painter = painterResource(id = imageConfiguration.logoStudySecondary()),
            contentDescription = null,
            modifier = Modifier.width(65.dp)
        )
    }
}

@Preview
@Composable
private fun AboutYouMenuHeaderPreview() {
    ForYouAndMeTheme {
        AboutYouMenuHeader()
    }
}