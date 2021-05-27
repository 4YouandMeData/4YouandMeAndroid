package com.foryouandme.ui.compose.error

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.getText
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton

@Composable
fun ErrorItem(
    error: ForYouAndMeException,
    configuration: Configuration,
    retry: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier =
        Modifier
            .fillMaxSize()
            .height(140.dp)
            .padding(20.dp)
    ) {
        Text(
            text = error.getMessage(configuration).getText(),
            color = configuration.theme.primaryTextColor.value,
            style = MaterialTheme.typography.h3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(30.dp))
        ForYouAndMeButton(
            text = configuration.text.error.buttonRetry,
            backgroundColor = configuration.theme.primaryColorStart.value,
            textColor = configuration.theme.secondaryColor.value,
            modifier = Modifier.fillMaxWidth(),
            onClick = retry
        )
    }
}

@Preview
@Composable
private fun ErrorItem() {
    ForYouAndMeTheme {
        ErrorItem(
            error = ForYouAndMeException.Unknown,
            configuration = Configuration.mock()
        )
    }
}