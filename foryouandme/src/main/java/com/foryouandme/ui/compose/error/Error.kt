package com.foryouandme.ui.compose.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.core.ext.getText
import com.foryouandme.core.ext.noIndicationClickable
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.button.ForYouAndMeButton

@Composable
fun Error(
    error: ForYouAndMeException,
    configuration: Configuration?,
    modifier: Modifier = Modifier,
    retry: () -> Unit = {}
) {
    Column(
        modifier =
        modifier
            .background(Color.White)
            .padding(25.dp)
            .noIndicationClickable { },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = stringResource(id = R.string.ERROR_title),
            color = Color(0xff303740),
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = error.getMessage(configuration).getText(),
            color = Color(0xff303740),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(100.dp))
        ForYouAndMeButton(
            text = stringResource(id = R.string.ERROR_retry),
            backgroundColor = Color(0xff140F26),
            textColor = Color.White,
            onClick = retry,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    Error(
        error = ForYouAndMeException.MissingConfiguration,
        configuration = null,
        modifier = Modifier.fillMaxSize()
    )
}