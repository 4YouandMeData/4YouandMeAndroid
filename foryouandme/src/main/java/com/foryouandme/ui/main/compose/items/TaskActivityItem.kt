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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.text.HtmlText
import com.foryouandme.ui.compose.verticalGradient

@Composable
fun TaskActivityItem(
    item: FeedItem.TaskActivityItem,
    configuration: Configuration,
    onStartClicked: (FeedItem.TaskActivityItem) -> Unit = {}
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
                    painter = rememberImagePainter(item.data.image),
                    contentDescription = null,
                    modifier = Modifier.size(57.dp)
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
            if (item.data.activityType != null) {
                Spacer(modifier = Modifier.height(20.dp))
                ForYouAndMeButton(
                    text = item.data.button ?: configuration.text.activity.activityButtonDefault,
                    backgroundColor = configuration.theme.secondaryColor.value,
                    textColor = configuration.theme.primaryTextColor.value,
                    onClick = { onStartClicked(item) }
                )
            }
        }
    }
}