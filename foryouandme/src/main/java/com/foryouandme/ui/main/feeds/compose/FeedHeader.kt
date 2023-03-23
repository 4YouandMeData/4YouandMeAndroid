package com.foryouandme.ui.main.feeds.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.ui.compose.ForYouAndMeTheme
import org.threeten.bp.LocalDate

@Composable
fun FeedHeader(
    configuration: Configuration,
    user: User?
) {

    val points = user?.points

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
        Modifier
            .fillMaxWidth()
            .height(104.dp)
            .background(configuration.theme.primaryColorEnd.value)
            .padding(horizontal = 20.dp)
    ) {

        val subtitle =
            if (user?.getDeliveryStartDate(configuration) == null)
                configuration.text.tab.feedHeaderSubTitle
            else
                configuration.text.tab.feedHeaderSubTitlePhase2
                    ?: configuration.text.tab.feedHeaderSubTitle

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = configuration.text.tab.feedHeaderTitle,
                color = configuration.theme.secondaryTextColor.value,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = subtitle,
                color = configuration.theme.secondaryTextColor.value,
                style = MaterialTheme.typography.h1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        if (points != null)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = points.toString(),
                    color = configuration.theme.secondaryTextColor.value,
                    style = MaterialTheme.typography.h1,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = configuration.text.tab.feedHeaderPoints,
                    color = configuration.theme.secondaryTextColor.value.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.h3,
                    maxLines = 1,
                )
            }
    }
}

private fun User?.getDeliveryStartDate(configuration: Configuration): LocalDate? =
    catchToNull {
        val userPhase = this?.phase
        if (userPhase != null) {
            val index = configuration.text.phases.indexOf(userPhase.phase.name)
            if (index > 0) userPhase.start.toLocalDate()
            else null
        } else null
    }

@Preview
@Composable
private fun FeedHeaderPreview() {
    ForYouAndMeTheme {
        FeedHeader(configuration = Configuration.mock(), user = User.mock())
    }
}