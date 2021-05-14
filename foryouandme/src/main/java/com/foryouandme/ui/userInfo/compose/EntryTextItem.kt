package com.foryouandme.ui.userInfo.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.textfield.ForYouAndMeTextField

@Composable
fun EntryTextItem(
    item: EntryItem.Text,
    isEditable: Boolean,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onTextChanged: (EntryItem.Text, String) -> Unit = { _, _ -> }
) {
    ForYouAndMeTextField(
        value = item.value,
        label = item.name,
        placeholder = item.name,
        isEditable = isEditable,
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
        onTextChanged = { onTextChanged(item, it) },
    )
}

@Preview
@Composable
private fun EntryTextItemPreview() {
    ForYouAndMeTheme {
        EntryTextItem(
            item = EntryItem.Text.mock(),
            isEditable = true,
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}