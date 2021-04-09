package com.foryouandme.ui.compose

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.foryouandme.R

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
        ),
        h2 =
        TextStyle(
            fontFamily = Helvetica,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
        ),
        h3 =
        TextStyle(
            fontFamily = Helvetica,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
        ),
        body1 =
            TextStyle(
                fontFamily = Helvetica,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            ),
    )