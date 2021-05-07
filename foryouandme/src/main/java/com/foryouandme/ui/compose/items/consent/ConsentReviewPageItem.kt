package com.foryouandme.ui.compose.items.consent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.ui.compose.text.HtmlText

data class ConsentReviewPageItem(
    val id: String,
    val configuration: Configuration,
    val title: String,
    val body: String
) {

    companion object {

        fun fromPage(page: Page, configuration: Configuration): ConsentReviewPageItem =
            ConsentReviewPageItem(
                page.id,
                configuration,
                page.title,
                page.body
            )

    }

}

@Composable
fun ConsentReviewPageItem(
    item: ConsentReviewPageItem,
    configuration: Configuration
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = configuration.theme.deactiveColor.value,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp,  horizontal = 15.dp)
        ) {
            HtmlText(
                text = item.title,
                color = configuration.theme.primaryTextColor.value,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.fillMaxWidth()
            )
            HtmlText(
                text = item.body,
                color = configuration.theme.primaryTextColor.value,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}