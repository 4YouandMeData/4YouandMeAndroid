package com.foryouandme.researchkit.step.video.compose

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.mock.Mock
import com.foryouandme.researchkit.step.video.RecordingState
import com.foryouandme.researchkit.step.video.RecordingState.RecordingPause
import com.foryouandme.researchkit.step.video.RecordingState.ReviewPause
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton

@Composable
fun VideoStepInfo(
    recordingState: RecordingState,
    backgroundColor: Color,
    title: String,
    titleColor: Color,
    closeImage: Int,
    timeImage: Int,
    recordTimeSeconds: Long,
    maxRecordTimeSeconds: Long,
    timeColor: Color,
    progressColor: Color,
    progressBackgroundColor: Color,
    infoTitle: String,
    infoTitleColor: Color,
    infoBody: String,
    infoBodyColor: Color,
    reviewButton: String,
    submitButton: String,
    buttonColor: Color,
    buttonTextColor: Color,
    onCloseClicked: () -> Unit = {},
    onReviewClicked: () -> Unit = {},
    onSubmitClicked: () -> Unit = {}
) {

    val time = remember(recordTimeSeconds, maxRecordTimeSeconds) {
        getTimeLabel(recordTimeSeconds, maxRecordTimeSeconds)
    }

    val progress = remember(recordTimeSeconds, maxRecordTimeSeconds) {
        getProgress(recordTimeSeconds, maxRecordTimeSeconds)
    }

    if (
        recordingState is RecordingPause ||
        recordingState is ReviewPause
    )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(30.dp, 30.dp))
                .padding(20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (recordingState is RecordingPause) title else "",
                    color = titleColor,
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 15.dp)
                )
                Image(
                    painter = painterResource(id = closeImage),
                    contentDescription = null,
                    modifier =
                    Modifier
                        .size(30.dp, 30.dp)
                        .clickable { onCloseClicked() }
                )
            }
            if (recordingState is RecordingPause) {
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = timeImage),
                        contentDescription = null,
                        modifier =
                        Modifier
                            .size(25.dp, 25.dp)
                            .clickable { onCloseClicked() }
                    )
                    Text(
                        text = time,
                        color = timeColor,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                LinearProgressIndicator(
                    progress = progress,
                    color = progressColor,
                    backgroundColor = progressBackgroundColor,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = infoTitle,
                    color = infoTitleColor,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = infoBody,
                    color = infoBodyColor,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = time,
                    color = timeColor,
                    style = MaterialTheme.typography.h1,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            ForYouAndMeButton(
                text =
                when (recordingState) {
                    ReviewPause -> submitButton
                    RecordingPause -> reviewButton
                    else -> ""
                },
                backgroundColor = buttonColor,
                textColor = buttonTextColor,
                onClick = {
                    when (recordingState) {
                        ReviewPause -> onSubmitClicked()
                        RecordingPause -> onReviewClicked()
                        else -> {
                        }
                    }
                },
                isEnabled =
                when (recordingState) {
                    RecordingPause -> recordTimeSeconds > 0
                    else -> true
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
}

private fun getTimeLabel(
    recordTimeSeconds: Long,
    maxRecordTimeSeconds: Long
): String {
    val currentRecordTime =
        DateUtils.formatElapsedTime(recordTimeSeconds)
    val maxRecordTime =
        DateUtils.formatElapsedTime(maxRecordTimeSeconds)

    return "$currentRecordTime/$maxRecordTime"
}

private fun getProgress(
    recordTimeSeconds: Long,
    maxRecordTimeSeconds: Long
): Float =
    recordTimeSeconds.toFloat() / maxRecordTimeSeconds.toFloat()

@Preview
@Composable
private fun VideoStepRecordingInfoPreview() {
    ForYouAndMeTheme {
        VideoStepInfo(
            recordingState = RecordingPause,
            backgroundColor = Color.White,
            title = Mock.title,
            titleColor = Color.Black,
            closeImage = 0,
            timeImage = 0,
            recordTimeSeconds = 30,
            maxRecordTimeSeconds = 120,
            timeColor = Color.Black,
            progressColor = Color.Red,
            progressBackgroundColor = Color.Yellow,
            infoTitle = Mock.time,
            infoTitleColor = Color.Black,
            infoBody = Mock.body,
            infoBodyColor = Color.Black,
            reviewButton = Mock.button,
            submitButton = Mock.button,
            buttonColor = Color.Red,
            buttonTextColor = Color.White
        )
    }
}