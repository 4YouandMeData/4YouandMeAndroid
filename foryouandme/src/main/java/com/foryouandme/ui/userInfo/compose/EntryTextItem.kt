package com.foryouandme.ui.userInfo.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.textfield.EntryText

@Composable
fun EntryTextItem(
    item: EntryItem.Text,
    isEditable: Boolean,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onTextChanged: (EntryItem.Text, String) -> Unit = { _, _ -> }
) {
    EntryText(
        text = item.value,
        placeholder = item.name,
        labelColor = configuration.theme.fourthTextColor.value,
        placeholderColor = configuration.theme.fourthTextColor.value,
        cursorColor = configuration.theme.primaryTextColor.value,
        textColor = configuration.theme.primaryTextColor.value,
        indicatorColor = configuration.theme.fourthTextColor.value,
        iconColor = configuration.theme.primaryTextColor.value,
        imageConfiguration = imageConfiguration,
        isEditable = isEditable,
        isValid = isEditable,
        onTextChanged = { onTextChanged(item, it) },
        modifier = Modifier.fillMaxWidth()
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