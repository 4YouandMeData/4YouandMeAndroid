package com.foryouandme.ui.userInfo.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.textfield.EntryDate
import com.google.accompanist.pager.ExperimentalPagerApi
import org.threeten.bp.LocalDate

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun EntryDateItem(
    item: EntryItem.Date,
    isEditable: Boolean,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onDateSelected: (EntryItem.Date, LocalDate) -> Unit = { _, _ -> }
) {
    EntryDate(
        value = item.value,
        isEditable = isEditable,
        configuration = configuration,
        imageConfiguration = imageConfiguration,
        label = item.name,
        description = item.description,
        onDateSelected = { onDateSelected(item, it) }
    )
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Preview
@Composable
private fun EntryDateItemPreview() {
    ForYouAndMeTheme {
        EntryDateItem(
            item = EntryItem.Date.mock(),
            isEditable = true,
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}