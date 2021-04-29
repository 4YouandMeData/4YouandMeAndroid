package com.foryouandme.ui.compose.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.preview.ComposePreview

@Composable
fun ForYouAndMeButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    disabledTextColor: Color = textColor,
    disabledBackgroundColor: Color = backgroundColor.copy(alpha = 0.5f),
    isEnabled: Boolean = true,
    style: TextStyle = MaterialTheme.typography.button,
    onClick: () -> Unit = { },
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
            Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    if (isEnabled) backgroundColor
                    else disabledBackgroundColor,
                    RoundedCornerShape(22.dp)
                )
                .let { if (isEnabled) it.clickable { onClick() } else it },
        ) {
            Text(
                text = text,
                style = style,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isEnabled) textColor else disabledTextColor,
                modifier = Modifier.padding(ButtonDefaults.ContentPadding)
            )
        }
    }
}

@Preview
@Composable
private fun ForYouAndMeButtonPreview() {
    ForYouAndMeTheme {
        ForYouAndMeButton(
            text = ComposePreview.button,
            backgroundColor = Color.Red,
            disabledBackgroundColor = Color.Gray,
            textColor = Color.White,
            disabledTextColor = Color.White,
        )
    }
}

@Preview
@Composable
private fun ForYouAndMeButtonDisabledPreview() {
    ForYouAndMeTheme {
        ForYouAndMeButton(
            text = ComposePreview.button,
            backgroundColor = Color.Red,
            textColor = Color.White,
            isEnabled = false
        )
    }
}