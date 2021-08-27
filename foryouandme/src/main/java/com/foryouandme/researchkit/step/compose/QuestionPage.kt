package com.foryouandme.researchkit.step.compose

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.source.ImageSource
import com.foryouandme.entity.source.MultiSourceImage
import com.foryouandme.ui.compose.statusbar.StatusBar

@Composable
fun QuestionPageLazy(
    backgroundColor: Color,
    question: String,
    questionColor: Color,
    buttonImage: ImageSource,
    isNextEnabled: Boolean = true,
    image: ImageSource? = null,
    lazyListState: LazyListState = rememberLazyListState(),
    onNext: () -> Unit = {},
    content: LazyListScope.() -> Unit,
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
            state = lazyListState,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            modifier =
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (image != null) {
                item {
                    MultiSourceImage(
                        source = image,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
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
            isEnabled = isNextEnabled,
            onClick = onNext
        )
    }
}

@Composable
fun QuestionPage(
    backgroundColor: Color,
    question: String,
    questionColor: Color,
    buttonImage: ImageSource,
    isNextEnabled: Boolean = true,
    image: ImageSource? = null,
    scrollState: ScrollState = rememberScrollState(),
    onNext: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    StatusBar(color = backgroundColor)
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 55.dp) // TODO: remove padding and apply background to toolbar
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            if (image != null) {

                MultiSourceImage(
                    source = image,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
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
            isEnabled = isNextEnabled,
            onClick = onNext
        )
    }
}