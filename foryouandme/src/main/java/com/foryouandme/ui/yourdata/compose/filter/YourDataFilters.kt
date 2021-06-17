package com.foryouandme.ui.yourdata.compose.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.ext.drawColoredShadow
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.topappbar.TopAppBarIcon
import com.foryouandme.ui.compose.verticalGradient

@ExperimentalComposeUiApi
@Composable
fun YourDataFilters(
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    filters: List<YourDataFilterItem>,
    onFilterClicked: (YourDataFilterItem) -> Unit = {},
    onClearClicked: () -> Unit = {},
    onSelectAllClicked: () -> Unit = {},
    onCloseClicked: () -> Unit = {},
    onSaveClicked: () -> Unit = {}
) {
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.secondaryColor.value)
    ) {
        ForYouAndMeTopAppBar(
            title = configuration.text.yourData.filterTitle,
            titleColor = configuration.theme.secondaryColor.value,
            titleAlignment = Alignment.BottomCenter,
            topAppBarAlignment = Alignment.TopCenter,
            icon = TopAppBarIcon.CloseSecondary,
            imageConfiguration = imageConfiguration,
            onBack = onCloseClicked,
            modifier =
            Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(configuration.theme.verticalGradient)
                .padding(bottom = 20.dp)
        )

        val clear = filters.firstOrNull { it.isSelected } != null

        LazyColumn(
            contentPadding = PaddingValues(vertical = 20.dp),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            item(clear) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text =
                        if (clear) configuration.text.yourData.filterClearButton
                        else configuration.text.yourData.filterSelectAllButton,
                        style = MaterialTheme.typography.body1,
                        color = configuration.theme.primaryColorEnd.value,
                        modifier =
                        Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterEnd)
                            .clickable { if (clear) onClearClicked() else onSelectAllClicked() }
                    )
                }
            }
            items(filters) {
                YourDataFilter(
                    configuration = configuration,
                    imageConfiguration = imageConfiguration,
                    item = it,
                    onClick = onFilterClicked
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .drawColoredShadow(configuration.theme.primaryTextColor.value)
                .background(configuration.theme.secondaryColor.value)
                .padding(horizontal = 20.dp),
        ) {
            ForYouAndMeButton(
                text = configuration.text.yourData.filterSaveButton,
                backgroundColor = configuration.theme.primaryColorEnd.value,
                textColor = configuration.theme.secondaryColor.value,
                onClick = onSaveClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun YourDataFiltersPreview() {
    ForYouAndMeTheme {
        YourDataFilters(
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock(),
            filters = listOf(YourDataFilterItem.mock(), YourDataFilterItem.mock())
        )
    }
}
