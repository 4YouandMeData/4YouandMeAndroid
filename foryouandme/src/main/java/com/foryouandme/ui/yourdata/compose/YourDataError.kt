package com.foryouandme.ui.yourdata.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.core.ext.getText
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.error.getMessage

@Composable
fun YourDataError(
    configuration: Configuration,
    error: ForYouAndMeException,
    padding: PaddingValues,
    onRetryClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(padding)) {
        Text(
            text = stringResource(id = R.string.ERROR_title),
            style = MaterialTheme.typography.h1,
            color = configuration.theme.primaryTextColor.value,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = error.getMessage(configuration).getText(),
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        ForYouAndMeButton(
            text = configuration.text.error.buttonRetry,
            backgroundColor = configuration.theme.primaryColorEnd.value,
            textColor = configuration.theme.secondaryColor.value,
            onClick = onRetryClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}