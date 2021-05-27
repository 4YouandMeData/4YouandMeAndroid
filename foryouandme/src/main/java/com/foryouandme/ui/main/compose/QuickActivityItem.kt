package com.foryouandme.ui.main.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.noIndicationClickable
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.verticalGradient

@Composable
fun QuickActivityItem(
    item: FeedItem.QuickActivityItem,
    configuration: Configuration,
    isLast: Boolean,
    onAnswerSelected: (FeedItem.QuickActivityItem, QuickActivityAnswer) -> Unit = { _, _ -> },
    onSubmit: (FeedItem.QuickActivityItem) -> Unit = {}
) {
    Card(
        backgroundColor = Color.Transparent,
        elevation = 20.dp,
        shape = RoundedCornerShape(10.dp),
        modifier =
        Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxSize()
                .background(configuration.theme.verticalGradient)
                .padding(horizontal = 25.dp, vertical = 25.dp)
        ) {
            Text(
                text = item.data.title.orEmpty(),
                style = MaterialTheme.typography.h2,
                color = configuration.theme.secondaryColor.value.copy(alpha = 0.5f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = item.data.description.orEmpty(),
                style = MaterialTheme.typography.body1,
                color = configuration.theme.secondaryColor.value,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .height(
                        LocalDensity.current.run { // set the height for 2 lines
                            ((MaterialTheme.typography.body1.fontSize * 4 / 3) * 2).toDp()
                        }
                    )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                QuickActivityAnswer(
                    item = item,
                    answer = item.data.answer1,
                    configuration = configuration,
                    selectedAnswer = item.selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )
                QuickActivityAnswer(
                    item = item,
                    answer = item.data.answer2,
                    configuration = configuration,
                    selectedAnswer = item.selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )
                QuickActivityAnswer(
                    item = item,
                    answer = item.data.answer3,
                    configuration = configuration,
                    selectedAnswer = item.selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                QuickActivityAnswer(
                    item = item,
                    answer = item.data.answer4,
                    configuration = configuration,
                    selectedAnswer = item.selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )
                QuickActivityAnswer(
                    item = item,
                    answer = item.data.answer5,
                    configuration = configuration,
                    selectedAnswer = item.selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )
                QuickActivityAnswer(
                    item = item,
                    answer = item.data.answer6,
                    configuration = configuration,
                    selectedAnswer = item.selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            ForYouAndMeButton(
                text =
                if (isLast) configuration.text.activity.quickActivityButtonDefault
                else configuration.text.activity.quickActivityButtonNext,
                backgroundColor = configuration.theme.secondaryColor.value,
                textColor = configuration.theme.primaryTextColor.value,
                isEnabled = item.selectedAnswer != null,
                modifier = Modifier.widthIn(min = 150.dp),
                onClick = { onSubmit(item) }
            )
        }
    }
}

@Composable
private fun RowScope.QuickActivityAnswer(
    item: FeedItem.QuickActivityItem,
    answer: QuickActivityAnswer?,
    configuration: Configuration,
    selectedAnswer: String?,
    onAnswerSelected: (FeedItem.QuickActivityItem, QuickActivityAnswer) -> Unit = { _, _ -> }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.weight(1f)
    ) {

        val isSelected = answer != null && answer.id == selectedAnswer
        val image =
            if (isSelected) answer?.selectedImage?.asImageBitmap()
            else answer?.image?.asImageBitmap()

        if (image != null)
            Image(
                bitmap = image,
                contentDescription = null,
                modifier =
                Modifier
                    .size(40.dp)
                    .noIndicationClickable { if (answer != null) onAnswerSelected(item, answer) }
            )
        else
            Spacer(modifier = Modifier.size(40.dp))

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = answer?.text.orEmpty(),
            style = MaterialTheme.typography.h3,
            color = configuration.theme.secondaryColor.value,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier =
            Modifier
                .height(
                    LocalDensity.current.run { // set the height for 2 lines
                        ((MaterialTheme.typography.body1.fontSize * 4 / 3) * 2).toDp()
                    }
                )
                .noIndicationClickable { if (answer != null) onAnswerSelected(item, answer) }
        )

    }
}

@Preview
@Composable
fun QuickActivityItemPreview() {
    ForYouAndMeTheme {
        QuickActivityItem(
            item = FeedItem.QuickActivityItem.mock(),
            configuration = Configuration.mock(),
            isLast = false
        )
    }
}