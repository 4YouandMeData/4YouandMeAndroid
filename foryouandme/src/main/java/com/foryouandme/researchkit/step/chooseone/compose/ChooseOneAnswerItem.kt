package com.foryouandme.researchkit.step.chooseone.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.RelocationRequester
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.getText
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.step.chooseone.ChooseOneAnswer
import com.foryouandme.ui.compose.textfield.EntryText

data class ChooseOneAnswerData(
    val answer: ChooseOneAnswer,
    val otherText: String?,
    val isSelected: Boolean,
    val selectedColor: Color,
    val unselectedColor: Color,
    val textColor: Color,
)

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ChooseOneAnswerItem(
    data: ChooseOneAnswerData,
    onAnswerClicked: (ChooseOneAnswerData) -> Unit = {},
    onTextChanged: (ChooseOneAnswerData, String) -> Unit = { _, _ -> }
) {

    val coroutineScope = rememberCoroutineScope()
    val relocationRequester = remember { RelocationRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(
                selected = data.isSelected,
                onClick = { onAnswerClicked(data) },
                colors =
                RadioButtonDefaults.colors(
                    selectedColor = data.selectedColor,
                    unselectedColor = data.unselectedColor
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = data.answer.text.getText(),
                color = data.textColor,
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAnswerClicked(data) }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        AnimatedVisibility(
            visible = data.otherText != null && data.isSelected,
            enter = expandIn(),
            exit = shrinkOut()
        ) {
            EntryText(
                text = data.otherText.orEmpty(),
                labelColor = data.textColor.copy(alpha = 0.5f),
                placeholderColor = data.textColor.copy(alpha = 0.5f),
                textColor = data.textColor,
                indicatorColor = data.textColor.copy(alpha = 0.5f),
                cursorColor = data.textColor,
                label = data.answer.otherPlaceholder?.getText().orEmpty(),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .relocationRequester(relocationRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launchSafe {
                                relocationRequester.bringIntoView()
                            }
                        }
                    }
                ,
                onTextChanged = { onTextChanged(data, it) }
            )
        }
    }
}