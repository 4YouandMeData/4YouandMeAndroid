package com.foryouandme.ui.main.compose.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.text.HtmlText
import com.foryouandme.ui.compose.verticalGradient

@Composable
fun FeedRewardItem(
    item: FeedItem.FeedRewardItem,
    configuration: Configuration,
    onStartClicked: (FeedAction) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        elevation = 20.dp,
        backgroundColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
            Modifier
                .fillMaxWidth()
                .background(item.data.gradient?.brush ?: configuration.theme.verticalGradient)
                .padding(horizontal = 25.dp, vertical = 30.dp)
        ) {
            if (item.data.image != null)
                Image(
                    bitmap = item.data.image.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.width(57.dp)
                )
            if (item.data.title != null) {
                Spacer(modifier = Modifier.height(15.dp))
                HtmlText(
                    text = item.data.title,
                    color = configuration.theme.secondaryColor.value,
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (item.data.description != null) {
                Spacer(modifier = Modifier.height(5.dp))
                HtmlText(
                    text = item.data.description,
                    color = configuration.theme.secondaryColor.value,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (item.data.action != null) {
                Spacer(modifier = Modifier.height(20.dp))
                ForYouAndMeButton(
                    text =
                    item.data.taskActionButtonLabel ?: configuration.text.feed.rewardButtonDefault,
                    backgroundColor = configuration.theme.secondaryColor.value,
                    textColor = configuration.theme.primaryTextColor.value,
                    onClick = { onStartClicked(item.data.action) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun FeedRewardItemPreview() {
    ForYouAndMeTheme {
        FeedRewardItem(
            item = FeedItem.FeedRewardItem.mock(),
            configuration = Configuration.mock()
        )
    }
}