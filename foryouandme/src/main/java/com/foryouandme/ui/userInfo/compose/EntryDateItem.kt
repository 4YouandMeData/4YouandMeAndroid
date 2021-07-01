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

    val dialog = remember { MaterialDialog() }
    dialog.build(backgroundColor = configuration.theme.secondaryColor.value) {
        datepicker(
            colors =
            DatePickerDefaults.colors(
                headerBackgroundColor = configuration.theme.primaryColorStart.value,
                headerTextColor = configuration.theme.secondaryColor.value,
                activeTextColor = configuration.theme.secondaryColor.value,
                activeBackgroundColor = configuration.theme.primaryColorStart.value,
                inactiveBackgroundColor = Color.Transparent,
                inactiveTextColor = configuration.theme.primaryTextColor.value,
            )
        ) {
            onDateSelected(
                item,
                LocalDate.of(it.year, it.monthValue, it.dayOfMonth)
            )
        }
        buttons {
            val style =
                MaterialTheme.typography.body1
                    .copy(color = configuration.theme.primaryTextColor.value)
            positiveButton("Ok", textStyle = style)
            negativeButton("Cancel", textStyle = style)
        }
    }

    ForYouAndMeReadOnlyTextField(
        value = item.value?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).orEmpty(),
        label = item.name,
        placeholder = null,
        labelColor = configuration.theme.fourthTextColor.value,
        placeholderColor = configuration.theme.fourthTextColor.value,
        cursorColor = configuration.theme.primaryTextColor.value,
        textColor = configuration.theme.primaryTextColor.value,
        indicatorColor = configuration.theme.fourthTextColor.value,
        trailingIcon = {
            Image(
                painter =
                painterResource(
                    id = if (isEditable) imageConfiguration.entryWrong()
                    else imageConfiguration.entryValid()
                ),
                contentDescription = null,
                colorFilter = ColorFilter.tint(configuration.theme.primaryTextColor.value),
                modifier = Modifier.size(40.dp)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        onClick = { if (isEditable) dialog.show() }
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