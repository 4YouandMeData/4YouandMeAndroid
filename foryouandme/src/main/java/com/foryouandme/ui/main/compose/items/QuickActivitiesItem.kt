package com.foryouandme.ui.main.compose.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun QuickActivitiesItem(
    item: FeedItem.QuickActivitiesItem,
    configuration: Configuration,
    onAnswerSelected: (FeedItem.QuickActivityItem, QuickActivityAnswer) -> Unit = { _, _ -> },
    onSubmit: (FeedItem.QuickActivityItem) -> Unit = {}
) {
    val quickActivity = item.items.getOrNull(item.selectedIndex)
    if (quickActivity != null)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            QuickActivityItem(
                item = quickActivity,
                configuration = configuration,
                isLast = item.selectedIndex == item.items.lastIndex,
                onAnswerSelected = onAnswerSelected,
                onSubmit = onSubmit
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = getCountText(item, configuration),
                color = configuration.theme.primaryTextColor.value,
                style = MaterialTheme.typography.body1
            )
        }
}

private fun getCountText(
    item: FeedItem.QuickActivitiesItem,
    configuration: Configuration
): String {

    val groupCount = configuration.text.activity.quickActivitiesTotalNumber.toInt()
    val totalCount = item.items.size
    val currentQuestionNumber = groupCount - ((totalCount - 1) % groupCount)
    return "${currentQuestionNumber}/${groupCount}"

}

@Preview
@Composable
private fun QuickActivitiesItemPreview() {
    ForYouAndMeTheme {
        QuickActivitiesItem(
            item = FeedItem.QuickActivitiesItem.mock(),
            configuration = Configuration.mock()
        )
    }
}