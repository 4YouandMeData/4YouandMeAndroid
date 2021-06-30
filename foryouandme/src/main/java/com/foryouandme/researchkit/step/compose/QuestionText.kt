package com.foryouandme.researchkit.step.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun QuestionText(
    question: String,
    color: Color
) {
    Text(
        text = question,
        style = MaterialTheme.typography.h1,
        color = color,
        textAlign = TextAlign.Center,
        modifier =
        Modifier.fillMaxWidth()
    )
}