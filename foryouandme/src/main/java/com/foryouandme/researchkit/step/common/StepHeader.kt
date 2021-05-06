package com.foryouandme.researchkit.step.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.entity.mock.Mock

@Composable
fun StepHeader(title: String, description: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = title, style = MaterialTheme.typography.h1)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = description, style = MaterialTheme.typography.body1)
    }
}

@Preview
@Composable
private fun StepHeaderPreview() {
    ForYouAndMeTheme {
        StepHeader(title = Mock.title, description = Mock.body)
    }
}