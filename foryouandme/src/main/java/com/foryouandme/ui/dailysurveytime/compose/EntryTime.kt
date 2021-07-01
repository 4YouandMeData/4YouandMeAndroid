package com.foryouandme.ui.dailysurveytime.compose

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
import com.foryouandme.entity.mock.Mock
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.textfield.ForYouAndMeReadOnlyTextField
import com.foryouandme.ui.dialog.MaterialDialog
import com.foryouandme.ui.dialog.buttons
import com.foryouandme.ui.dialog.datetime.date.DatePickerColors
import com.foryouandme.ui.dialog.datetime.time.TimePickerDefaults
import com.foryouandme.ui.dialog.datetime.time.timepicker
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun EntryTime(
    time: LocalTime,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    modifier: Modifier = Modifier,
    onTimeSelected: (LocalTime) -> Unit = { }
) {

    val dialog = remember { MaterialDialog() }
    dialog.build(
        backgroundColor = configuration.theme.secondaryColor.value
    ) {
        timepicker(
            initialTime = time,
            title = "    ",
            colors =
            TimePickerDefaults.colors(
                activeBackgroundColor = configuration.theme.primaryColorStart.value,
                inactiveBackgroundColor = configuration.theme.deactiveColor.value,
                activeTextColor = configuration.theme.secondaryColor.value,
                inactiveTextColor = configuration.theme.primaryTextColor.value,
                selectorColor = configuration.theme.primaryColorStart.value,
                selectorTextColor = configuration.theme.secondaryColor.value,
                borderColor = configuration.theme.primaryTextColor.value
            )
        ) { onTimeSelected(it) }
        buttons {
            val style =
                MaterialTheme.typography.body1
                    .copy(color = configuration.theme.primaryTextColor.value)
            positiveButton("Ok", textStyle = style)
            negativeButton("Cancel", textStyle = style)
        }
    }

    ForYouAndMeReadOnlyTextField(
        value = time.format(DateTimeFormatter.ofPattern("HH:mm")).orEmpty(),
        label = null,
        placeholder = null,
        configuration = configuration,
        trailingIcon = {
            Image(
                painter = painterResource(imageConfiguration.entryWrong()),
                contentDescription = null,
                colorFilter = ColorFilter.tint(configuration.theme.primaryTextColor.value),
                modifier = Modifier.size(40.dp)
            )
        },
        modifier = Modifier.fillMaxWidth().then(modifier),
        onClick = { dialog.show() }
    )
}

@Preview
@Composable
private fun EntryDateItemPreview() {
    ForYouAndMeTheme {
        EntryTime(
            time = Mock.localTime,
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}