package com.foryouandme.ui.userInfo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton

@Composable
fun PhaseSwitchAlert(
    configuration: Configuration,
    onPositiveClicked: () -> Unit,
    onNegativeClicked: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 20.dp,
        backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxWidth()
                .background(configuration.theme.secondaryColor.value)
                .padding(horizontal = 25.dp, vertical = 30.dp)
        ) {
            Text(
                text = configuration.text.profile.phase.switchDescription,
                color = configuration.theme.primaryTextColor.value,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            ForYouAndMeButton(
                text = configuration.text.profile.phase.switchYes,
                backgroundColor = configuration.theme.primaryColorStart.value,
                textColor = configuration.theme.secondaryTextColor.value,
                onClick = { onPositiveClicked() }
            )
            Spacer(modifier = Modifier.height(20.dp))
            ForYouAndMeButton(
                text = configuration.text.profile.phase.switchNo,
                backgroundColor = configuration.theme.primaryColorStart.value,
                textColor = configuration.theme.secondaryTextColor.value,
                onClick = { onNegativeClicked() }
            )
        }
    }
}

@Preview
@Composable
private fun FeedAlertItemPreview() {
    ForYouAndMeTheme {
        PhaseSwitchAlert(
            configuration = Configuration.mock(),
            onPositiveClicked = {},
            onNegativeClicked = {}
        )
    }
}