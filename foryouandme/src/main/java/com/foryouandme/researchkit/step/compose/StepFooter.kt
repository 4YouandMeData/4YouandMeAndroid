package com.foryouandme.researchkit.step.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.core.ext.drawColoredShadow
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.MultiSourceImage
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun StepFooter(
    color: Color,
    button: ImageSource,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .fillMaxWidth()
            .height(135.dp)
            .drawColoredShadow(shadowColor)
            .background(color)
            .then(modifier)

    ) {
        MultiSourceImage(
            source = button,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clickable { onClick() }
        )
    }
}

@Preview
@Composable
private fun StepFooterPreview() {
    ForYouAndMeTheme {
        StepFooter(
            color = Color.White,
            button = ImageSource.AndroidResource(R.drawable.error),
            shadowColor = Color.Black,
        )
    }
}