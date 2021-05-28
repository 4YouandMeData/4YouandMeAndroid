package com.foryouandme.ui.main.feeds.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton

@Composable
fun FeedEmpty(configuration: Configuration) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = configuration.text.tab.feedEmptyTitle,
            style = MaterialTheme.typography.h1,
            color = configuration.theme.primaryTextColor.value,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = configuration.text.tab.feedEmptySubTitle,
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun FeedEmptyPreview() {
    ForYouAndMeTheme {
        FeedEmpty(configuration = Configuration.mock())
    }
}