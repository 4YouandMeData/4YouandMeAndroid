package com.foryouandme.ui.main.compose.items

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.entity.configuration.Configuration
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

@Composable
fun DateItem(
    item: FeedItem.DateItem,
    configuration: Configuration
) {
    Text(
        text = getDateText(item),
        style = MaterialTheme.typography.body1,
        color = configuration.theme.primaryTextColor.value
    )
}

private fun getDateText(item: FeedItem.DateItem): String =
    catchToNull {
        item.date.format(DateTimeFormatter.ofPattern("MMMM dd yyyy", Locale.US))
    }.orEmpty()