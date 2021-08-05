package com.foryouandme.researchkit.step.choosemany.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.layout.RelocationRequester
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.step.choosemany.ChooseManyAnswer
import com.foryouandme.ui.compose.textfield.EntryText
import kotlinx.coroutines.CoroutineScope

data class ChooseManyAnswerData(
    val answer: ChooseManyAnswer,
    val otherText: String?,
    val isSelected: Boolean
)

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun ChooseManyAnswerItem(
    data: ChooseManyAnswerData,
    onAnswerClicked: (ChooseManyAnswerData) -> Unit = {},
    onTextChanged: (ChooseManyAnswerData, String) -> Unit = { _, _ -> },
    onTextFocused: () -> Unit = {}
) {

    val coroutineScope = rememberCoroutineScope()
    val relocationRequester = remember { RelocationRequester() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = data.isSelected,
                onCheckedChange = { onAnswerClicked(data) },
                colors =
                CheckboxDefaults.colors(
                    checkedColor = data.answer.selectedColor.getColor(),
                    uncheckedColor = data.answer.unselectedColor.getColor(),
                    checkmarkColor = data.answer.checkmarkColor.getColor()
                )
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = data.answer.text.getText(),
                color = data.answer.textColor.getColor(),
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
                labelColor = data.answer.entryColor.getColor(),
                placeholderColor = data.answer.entryColor.getColor(),
                textColor = data.answer.textColor.getColor(),
                indicatorColor = data.answer.entryColor.getColor(),
                cursorColor = data.answer.textColor.getColor(),
                label = data.answer.otherPlaceholder?.getText().orEmpty(),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .onFocusChanged { if (it.isFocused) onTextFocused() }
                    .relocationRequester(relocationRequester)
                    .onFocusEvent {
                        if (it.isFocused) {
                            coroutineScope.launchSafe {
                                relocationRequester.bringIntoView()
                            }
                        }
                    },
                onTextChanged = { onTextChanged(data, it) }
            )
        }
    }
}