package com.foryouandme.ui.aboutyou.userInfo.compose

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
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.datetime.datepicker.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.datepicker.datepicker
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun EntryDateItem(
    item: EntryItem.Date,
    isEditable: Boolean,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onDateSelected: (EntryItem.Date, LocalDate) -> Unit = { _, _ -> }
) {

    val dialog = remember { MaterialDialog() }
    dialog.build {
        datepicker(colors = ForYouAndMeDatePickerColors(configuration)) {
            onDateSelected(item, LocalDate.of(it.year, it.monthValue, it.dayOfMonth))
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
        configuration = configuration,
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

class ForYouAndMeDatePickerColors(
    private val configuration: Configuration
) : DatePickerColors {

    override val headerBackgroundColor: Color
        get() = configuration.theme.primaryColorStart.value

    override val headerTextColor: Color
        get() = configuration.theme.secondaryColor.value

    @Composable
    override fun backgroundColor(active: Boolean): State<Color> =
        rememberUpdatedState(
            if (active) configuration.theme.primaryColorStart.value
            else configuration.theme.secondaryColor.value
        )

    @Composable
    override fun textColor(active: Boolean): State<Color> =
        rememberUpdatedState(
            if (active) configuration.theme.secondaryColor.value
            else configuration.theme.primaryTextColor.value
        )

}

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