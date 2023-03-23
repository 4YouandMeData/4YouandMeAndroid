package com.foryouandme.ui.compose.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.foryouandme.entity.mock.Mock
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun AlertDialog(
    title: String?,
    message: String,
    positiveButton: String,
    negativeButton: String,
    onPositiveClicked: () -> Unit,
    onNegativeClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = onPositiveClicked) {
                Text(
                    text = positiveButton,
                    fontSize = 15.sp,
                    color = Color(color = 0xFF00B1FF)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onNegativeClicked) {
                Text(
                    text = negativeButton,
                    fontSize = 15.sp,
                    color = Color(color = 0xFF00B1FF)
                )
            }
        },
        title =
        if (title == null) null
        else {
            {
                Text(
                    text = title,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        text = {
            Text(
                text = message,
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        },
        backgroundColor = Color.White,
        contentColor = Color.Black
    )
}

@Preview
@Composable
private fun PhaseSwitchAlertPreview() {
    ForYouAndMeTheme {
        AlertDialog(
            positiveButton = "Yes",
            negativeButton = "No",
            title = Mock.title,
            message = Mock.body,
            onPositiveClicked = {},
            onNegativeClicked = {}
        )
    }
}

@Preview
@Composable
private fun PhaseSwitchAlertLongTextPreview() {
    ForYouAndMeTheme {
        AlertDialog(
            positiveButton = Mock.button,
            negativeButton = Mock.button,
            title = Mock.title,
            message = Mock.body,
            onPositiveClicked = {},
            onNegativeClicked = {}
        )
    }
}