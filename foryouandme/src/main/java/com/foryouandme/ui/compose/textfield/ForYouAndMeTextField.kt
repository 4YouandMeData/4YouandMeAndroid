package com.foryouandme.ui.compose.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.entity.mock.Mock
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun ForYouAndMeTextField(
    value: String,
    label: String?,
    description: String?,
    placeholder: String?,
    isEditable: Boolean,
    labelColor: Color,
    placeholderColor: Color,
    textColor: Color,
    indicatorColor: Color,
    cursorColor: Color,
    modifier: Modifier = Modifier,
    maxCharacter: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    trailingIcon: @Composable () -> Unit = {},
    onTextChanged: (String) -> Unit = { }
) {
    TextField(
        value = value,
        label = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (label != null)
                    Text(
                        text = label,
                        style = MaterialTheme.typography.body1,
                        color = labelColor,
                    )
                if(description != null)
                    Text(
                        text = description,
                        style = MaterialTheme.typography.body1,
                        color = labelColor,
                    )
            }
        },
        placeholder = {
            if (placeholder != null)
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.body1,
                    color = placeholderColor
                )
        },
        enabled = isEditable,
        singleLine = true,
        trailingIcon = trailingIcon,
        textStyle =
        MaterialTheme
            .typography
            .body1
            .copy(color = textColor),
        colors =
        TextFieldDefaults.textFieldColors(
            textColor = textColor,
            backgroundColor = Color.Transparent,
            placeholderColor = placeholderColor.copy(alpha = 0.6f),
            focusedIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            focusedLabelColor = labelColor,
            unfocusedLabelColor = labelColor,
            cursorColor = cursorColor,
        ),
        onValueChange = {
            when {
                maxCharacter == null -> onTextChanged(it)
                it.length <= maxCharacter -> onTextChanged(it)
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}


@Composable
fun ForYouAndMeReadOnlyTextField(
    value: String,
    label: String?,
    description: String?,
    placeholder: String?,
    labelColor: Color,
    placeholderColor: Color,
    textColor: Color,
    indicatorColor: Color,
    cursorColor: Color,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable () -> Unit = {},
    onClick: () -> Unit,
) {
    Box {
        ForYouAndMeTextField(
            value = value,
            label = label,
            description = description,
            placeholder = placeholder,
            labelColor = labelColor,
            placeholderColor = placeholderColor,
            textColor = textColor,
            indicatorColor = indicatorColor,
            cursorColor = cursorColor,
            isEditable = false,
            trailingIcon = trailingIcon,
            modifier = modifier,
        )
        Box(
            modifier =
            Modifier
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
            value = Mock.name,
            label = Mock.title,
            description = Mock.body,
            placeholder = "",
            isEditable = true,
            labelColor = Color.White,
            placeholderColor = Color.White,
            textColor = Color.White,
            indicatorColor = Color.White,
            cursorColor = Color.White
        )
    }
}