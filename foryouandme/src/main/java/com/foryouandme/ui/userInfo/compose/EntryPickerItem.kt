package com.foryouandme.ui.userInfo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.foryouandme.ui.dialog.listItemsSingleChoice
import com.foryouandme.ui.dialog.title
import kotlin.math.max

@Composable
fun EntryPickerItem(
    item: EntryItem.Picker,
    isEditable: Boolean,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onValueSelected: (EntryItem.Picker, EntryItem.Picker.Value) -> Unit = { _, _ -> }
) {

    val dialog = remember { MaterialDialog() }
    dialog.build(backgroundColor = configuration.theme.secondaryColor.value) {

        title(text = item.name, color = configuration.theme.primaryTextColor.value)

        listItemsSingleChoice(
            list = item.values.map { it.name },
            selectedTextColor = configuration.theme.primaryTextColor.value,
            radioButtonColors =
            RadioButtonDefaults.colors(
                selectedColor = configuration.theme.primaryColorStart.value,
                unselectedColor = configuration.theme.primaryTextColor.value
            ),
            initialSelection = max(item.values.indexOf(item.value), 0)
        ) {
            val value = item.values.getOrNull(it)
            if (value != null)
                onValueSelected(item, value)
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
        value = item.value?.name.orEmpty(),
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

@Preview
@Composable
private fun EntryPickerItemPreview() {
    ForYouAndMeTheme {
        EntryPickerItem(
            item = EntryItem.Picker.mock(),
            isEditable = true,
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}