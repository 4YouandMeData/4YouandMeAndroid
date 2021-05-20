package com.foryouandme.researchkit.step.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.MultiSourceImage
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.R

@Composable
fun StepFooter(
    color: Color,
    button: ImageSource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        elevation = 20.dp,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .then(modifier)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
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
}

@Preview
@Composable
fun StepFooter() {
    ForYouAndMeTheme {
        StepFooter(
            color = Color.White,
            button = ImageSource.AndroidResource(R.drawable.error)
        )
    }
}