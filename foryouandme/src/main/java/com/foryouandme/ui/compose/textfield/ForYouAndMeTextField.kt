package com.foryouandme.ui.compose.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun ForYouAndMeTextField(
    value: String,
    label: String?,
    placeholder: String?,
    isEditable: Boolean,
    configuration: Configuration,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable () -> Unit = {},
    onTextChanged: (String) -> Unit = { }
) {
    TextField(
        value = value,
        label = {
            if (label != null)
                Text(
                    text = label,
                    style = MaterialTheme.typography.body1,
                    color = configuration.theme.fourthTextColor.value
                )
        },
        placeholder = {
            if (placeholder != null)
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.body1,
                    color = configuration.theme.fourthTextColor.value
                )
        },
        enabled = isEditable,
        singleLine = true,
        trailingIcon = trailingIcon,
        textStyle =
        MaterialTheme
            .typography
            .body1
            .copy(color = configuration.theme.primaryTextColor.value),
        colors =
        TextFieldDefaults.textFieldColors(
            textColor = configuration.theme.primaryTextColor.value,
            backgroundColor = Color.Transparent,
            placeholderColor = configuration.theme.primaryTextColor.value.copy(alpha = 0.6f),
            focusedIndicatorColor = configuration.theme.primaryTextColor.value,
            unfocusedIndicatorColor = configuration.theme.primaryTextColor.value,
            focusedLabelColor = configuration.theme.primaryTextColor.value,
            unfocusedLabelColor = configuration.theme.primaryTextColor.value,
            cursorColor = configuration.theme.primaryTextColor.value,
        ),
        onValueChange = onTextChanged,
        modifier = modifier
    )
}


@Composable
fun ForYouAndMeReadOnlyTextField(
    value: String,
    label: String?,
    placeholder: String?,
    configuration: Configuration,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    Box {
        ForYouAndMeTextField(
            value = value,
            label = label,
            placeholder = placeholder,
            isEditable = false,
            trailingIcon = trailingIcon,
            configuration = configuration,
            modifier = modifier,
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = onClick),
        )
    }
}

@Preview
@Composable
private fun ForYouAndMeTextFieldPreview() {
    ForYouAndMeTheme {
        ForYouAndMeTextField(
            value = Mock.body,
            label = Mock.name,
            placeholder = Mock.name,
            isEditable = true,
            configuration = Configuration.mock(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}