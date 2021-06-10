package com.foryouandme.ui.yourdata.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.YourData
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun YourDataHeader(
    configuration: Configuration,
    yourData: YourData
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
        Modifier
            .fillMaxWidth()
            .background(configuration.theme.secondaryColor.value)
            .padding(start = 20.dp, top = 40.dp, end = 20.dp, bottom = 20.dp)
    ) {
        Text(
            text = yourData.title.orEmpty(),
            style = MaterialTheme.typography.h1,
            color = configuration.theme.primaryTextColor.value,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = yourData.body.orEmpty(),
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        RatingBar(
            rating = yourData.stars ?: 0f,
            startColor = Color(0xffFFCA00),
            backgroundColor = Color(0xffE1E1E1),
            startSpacing = 15.dp,
            modifier = Modifier.height(25.dp)
        )
    }
}

@Preview
@Composable
fun YourDataHeaderPreview() {
    ForYouAndMeTheme {
        YourDataHeader(
            configuration = Configuration.mock(),
            yourData = YourData.mock()
        )
    }
}