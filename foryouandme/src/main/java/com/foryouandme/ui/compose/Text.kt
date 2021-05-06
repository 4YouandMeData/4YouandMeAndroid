package com.foryouandme.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foryouandme.R
import com.foryouandme.entity.mock.Mock

val Helvetica = FontFamily(
    Font(R.font.helvetica, FontWeight.Light),
    Font(R.font.helvetica, FontWeight.Normal),
    Font(R.font.helvetica_bold, FontWeight.Bold),
    Font(R.font.helvetica_bold, FontWeight.Black)
)

val ForYouAndMeTypography =
    Typography(
        h1 =
        TextStyle(
            fontFamily = Helvetica,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 30.sp
        ),
        h2 =
        TextStyle(
            fontFamily = Helvetica,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 29.4.sp
        ),
        h3 =
        TextStyle(
            fontFamily = Helvetica,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 16.6.sp
        ),
        body1 =
        TextStyle(
            fontFamily = Helvetica,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 21.sp
        ),
        button =
        TextStyle(
            fontFamily = Helvetica,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 29.4.sp
        )
    )

@Preview(device = Devices.PIXEL_4_XL, showSystemUi = true)
@Composable
private fun TypographyPreview() {
    ForYouAndMeTheme {
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(15.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = Mock.body,
                style = MaterialTheme.typography.h1
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = Mock.body,
                style = MaterialTheme.typography.h2
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = Mock.body,
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = Mock.body,
                style = MaterialTheme.typography.body1
            )
        }
    }
}
