package com.foryouandme.ui.userInfo.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.textfield.EntryDate
import com.foryouandme.ui.compose.textfield.ForYouAndMeReadOnlyTextField
import com.foryouandme.ui.dialog.MaterialDialog
import com.foryouandme.ui.dialog.buttons
import com.foryouandme.ui.dialog.datetime.date.DatePickerColors
import com.foryouandme.ui.dialog.datetime.date.DatePickerDefaults
import com.foryouandme.ui.dialog.datetime.date.datepicker
import com.google.accompanist.pager.ExperimentalPagerApi
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

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