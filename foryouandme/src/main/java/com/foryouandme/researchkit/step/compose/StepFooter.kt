package com.foryouandme.researchkit.step.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.R
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.MultiSourceImage
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun StepFooter(
    color: Color,
    button: ImageSource,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .fillMaxWidth()
            .height(135.dp)
            .shadow(20.dp)
            .background(color)
            .then(modifier)

    ) {
        MultiSourceImage(
            source = button,
            contentDescription = null,
            modifier = Modifier
                .size(70.dp)
                .clickable { if(isEnabled) onClick() }
                .alpha(if(isEnabled) 1f else 0.5f)
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
        )
    }
}