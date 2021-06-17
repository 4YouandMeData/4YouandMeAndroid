package com.foryouandme.ui.yourdata.compose.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.YourDataFilter
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ImageButton

data class YourDataFilterItem(
    val filter: YourDataFilter,
    val isSelected: Boolean
) {

    companion object {

        fun mock(): YourDataFilterItem =
            YourDataFilterItem(
                filter = YourDataFilter.mock(),
                isSelected = true
            )

    }

}

@ExperimentalComposeUiApi
@Composable
fun YourDataFilter(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    item: YourDataFilterItem,
    onClick: (YourDataFilterItem) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = item.filter.name,
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(20.dp))
        ImageButton(
            painter =
            painterResource(
                id =
                if (item.isSelected) imageConfiguration.checkBoxOn()
                else imageConfiguration.checkBoxOff()
            ),
            onClick = { onClick(item) },
            modifier = Modifier.size(24.dp)
        )
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
private fun YourDataFilterPreview() {
    ForYouAndMeTheme {
        YourDataFilter(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            item = YourDataFilterItem.mock()
        )
    }
}