package com.foryouandme.researchkit.step.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.source.ImageSource

@Composable
fun QuestionPage(
    backgroundColor: Color,
    question: String,
    questionColor: Color,
    buttonImage: ImageSource,
    shadowColor: Color,
    isNextEnabled: Boolean = true,
    onNext: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            QuestionText(
                question = question,
                color = questionColor
            )
            Spacer(modifier = Modifier.height(20.dp))
            content()
        }
        StepFooter(
            color = backgroundColor,
            button = buttonImage,
            shadowColor = shadowColor,
            isEnabled = isNextEnabled,
            onClick = onNext
        )
    }
}