package com.foryouandme.researchkit.step.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.ui.compose.statusbar.StatusBar

@Composable
fun QuestionPage(
    backgroundColor: Color,
    question: String,
    questionColor: Color,
    buttonImage: ImageSource,
    shadowColor: Color,
    isNextEnabled: Boolean = true,
    onNext: () -> Unit = {},
    content: LazyListScope.() -> Unit
) {
    StatusBar(color = backgroundColor)
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 55.dp) // TODO: remove padding and apply background to toolbar
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            modifier =
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                QuestionText(
                    question = question,
                    color = questionColor
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
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