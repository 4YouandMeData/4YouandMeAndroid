package com.foryouandme.ui.compose.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.mock.Mock
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun EntryText(
    text: String,
    imageConfiguration: ImageConfiguration,
    labelColor: Color,
    placeholderColor: Color,
    textColor: Color,
    indicatorColor: Color,
    cursorColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isEditable: Boolean = true,
    isValid: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onTextChanged: (String) -> Unit = { },
) {

    var focusState by remember { mutableStateOf<FocusState?>(null) }

    ForYouAndMeTextField(
        value = text,
        label = placeholder,
        placeholder = placeholder,
        isEditable = isEditable,
        labelColor = labelColor,
        placeholderColor = placeholderColor,
        textColor = textColor,
        indicatorColor = indicatorColor,
        cursorColor = cursorColor,
        keyboardOptions = keyboardOptions,
        trailingIcon = {
            val focus = focusState
            if (focus != null && focus.isFocused.not()) {
                Image(
                    painter =
                    painterResource(
                        id =
                        when {
                            focus.isFocused.not() && isValid.not() ->
                                imageConfiguration.entryWrong()
                            else ->
                                imageConfiguration.entryValid()
                        }
                    ),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(iconColor),
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        modifier = modifier.onFocusChanged { focusState = it },
        onTextChanged = onTextChanged,
    )
}

@Preview
@Composable
private fun EntryTextItemPreview() {
    ForYouAndMeTheme {
        EntryText(
            text = Mock.name,
            placeholder = Mock.title,
            labelColor = Color.Black,
            placeholderColor = Color.Black,
            cursorColor = Color.Black,
            textColor = Color.Black,
            indicatorColor = Color.Black,
            iconColor = Color.Black,
            imageConfiguration = ImageConfiguration.mock(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}