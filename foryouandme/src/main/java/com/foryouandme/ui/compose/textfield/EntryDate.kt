package com.foryouandme.ui.compose.textfield

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.dialog.MaterialDialog
import com.foryouandme.ui.dialog.buttons
import com.foryouandme.ui.dialog.datetime.date.DatePickerDefaults
import com.foryouandme.ui.dialog.datetime.date.datepicker
import com.google.accompanist.pager.ExperimentalPagerApi
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun EntryDate(
    value: LocalDate?,
    isEditable: Boolean,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration?,
    placeholder: String? = null,
    label: String? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit = { }
) {
    EntryDateImpl(
        value = value,
        isEditable = isEditable,
        entryDateColors = EntryDateDefaults.colors(configuration),
        imageConfiguration = imageConfiguration,
        placeholder = placeholder,
        label = label,
        minDate = minDate,
        maxDate = maxDate,
        onDateSelected = onDateSelected
    )
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
fun EntryDate(
    value: LocalDate?,
    isEditable: Boolean,
    entryDateColors: EntryDateColors,
    placeholder: String? = null,
    label: String? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit = { }
) {
    EntryDateImpl(
        value = value,
        isEditable = isEditable,
        entryDateColors = entryDateColors,
        placeholder = placeholder,
        label = label,
        minDate = minDate,
        maxDate = maxDate,
        onDateSelected = onDateSelected
    )
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Composable
private fun EntryDateImpl(
    value: LocalDate?,
    isEditable: Boolean,
    entryDateColors: EntryDateColors,
    imageConfiguration: ImageConfiguration? = null,
    placeholder: String? = null,
    label: String? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit = { }
) {

    val dialog = remember { MaterialDialog() }
    dialog.build(backgroundColor = entryDateColors.datePickerBackgroundColor) {
        datepicker(
            colors =
            DatePickerDefaults.colors(
                headerBackgroundColor = entryDateColors.datePickerHeaderBackgroundColor,
                headerTextColor = entryDateColors.datePickerHeaderTextColor,
                activeTextColor = entryDateColors.datePickerActiveTextColor,
                activeBackgroundColor = entryDateColors.datePickerActiveBackgroundColor,
                inactiveBackgroundColor = Color.Transparent,
                inactiveTextColor = entryDateColors.datePickerInactiveTextColor,
            ),
            minDate = minDate ?: LocalDate.of(1900, 1, 1),
            maxDate = maxDate ?: LocalDate.of(2100, 1, 1)
        ) {
            onDateSelected(LocalDate.of(it.year, it.monthValue, it.dayOfMonth))
        }
        buttons {
            val style =
                MaterialTheme.typography.body1
                    .copy(color = entryDateColors.datePickerButtonsColor)
            positiveButton("Ok", textStyle = style)
            negativeButton("Cancel", textStyle = style)
        }
    }

    ForYouAndMeReadOnlyTextField(
        value = value?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")).orEmpty(),
        label = label,
        placeholder = placeholder,
        labelColor = entryDateColors.labelColor,
        placeholderColor = entryDateColors.placeholderColor,
        cursorColor = entryDateColors.cursorColor,
        textColor = entryDateColors.textColor,
        indicatorColor = entryDateColors.indicatorColor,
        trailingIcon = {
            if (
                imageConfiguration != null &&
                entryDateColors.iconColor != null
            )
                Image(
                    painter =
                    painterResource(
                        id = if (isEditable) imageConfiguration.entryWrong()
                        else imageConfiguration.entryValid()
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(entryDateColors.iconColor),
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
        EntryDate(
            value = Mock.date,
            isEditable = true,
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}

data class EntryDateColors(
    val labelColor: Color,
    val placeholderColor: Color,
    val textColor: Color,
    val indicatorColor: Color,
    val cursorColor: Color,
    val datePickerBackgroundColor: Color,
    val datePickerHeaderBackgroundColor: Color,
    val datePickerHeaderTextColor: Color,
    val datePickerActiveTextColor: Color,
    val datePickerActiveBackgroundColor: Color,
    val datePickerInactiveTextColor: Color,
    val datePickerButtonsColor: Color,
    val iconColor: Color?
)

object EntryDateDefaults {

    fun colors(configuration: Configuration): EntryDateColors =
        EntryDateColors(
            labelColor = configuration.theme.fourthTextColor.value,
            placeholderColor = configuration.theme.fourthTextColor.value,
            textColor = configuration.theme.primaryTextColor.value,
            indicatorColor = configuration.theme.fourthTextColor.value,
            cursorColor = configuration.theme.primaryTextColor.value,
            datePickerBackgroundColor = configuration.theme.secondaryColor.value,
            datePickerHeaderBackgroundColor = configuration.theme.primaryColorStart.value,
            datePickerHeaderTextColor = configuration.theme.secondaryColor.value,
            datePickerActiveTextColor = configuration.theme.secondaryColor.value,
            datePickerActiveBackgroundColor = configuration.theme.primaryColorStart.value,
            datePickerInactiveTextColor = configuration.theme.primaryTextColor.value,
            datePickerButtonsColor = configuration.theme.primaryTextColor.value,
            iconColor = configuration.theme.fourthTextColor.value,
        )

}