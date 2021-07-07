package com.foryouandme.researchkit.step.choosemany.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.getColor
import com.foryouandme.core.ext.getText
import com.foryouandme.researchkit.step.choosemany.ChooseManyAnswer
import com.foryouandme.researchkit.step.chooseone.ChooseOneAnswer
import com.foryouandme.ui.compose.textfield.EntryText

data class ChooseManyAnswerData(
    val answer: ChooseManyAnswer,
    val otherText: String?,
    val isSelected: Boolean
)

@ExperimentalAnimationApi
@Composable
fun ChooseManyAnswerItem(
    data: ChooseManyAnswerData,
    onAnswerClicked: (ChooseManyAnswerData) -> Unit = {},
    onTextChanged: (ChooseManyAnswerData, String) -> Unit = { _, _ -> },
    onTextFocused: () -> Unit = {}
) {
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
                    .onFocusChanged { if (it.isFocused) onTextFocused() },
                onTextChanged = { onTextChanged(data, it) }
            )
        }
    }
}