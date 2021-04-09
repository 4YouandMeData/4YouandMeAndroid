package com.foryouandme.researchkit.step.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.preview.ComposePreview

@Composable
fun StepHeader(title: String, description: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = title, style = ForYouAndMeTheme.typography.h1)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = description, style = ForYouAndMeTheme.typography.body1)
    }
}

@Preview
@Composable
private fun StepHeaderPreview() {
    StepHeader(title = ComposePreview.title, description = ComposePreview.body)
}