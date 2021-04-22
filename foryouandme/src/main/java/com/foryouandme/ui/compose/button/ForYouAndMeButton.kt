package com.foryouandme.ui.compose.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
                .background(backgroundColor, RoundedCornerShape(22.dp))
                .clickable { onClick() },
        ) {
            Text(
                text = text,
                style = style,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textColor,
                modifier = Modifier.padding(ButtonDefaults.ContentPadding)
            )
        }
    }
}

@Preview
@Composable
private fun FourBooksButtonPreview() {
    ForYouAndMeTheme {
        ForYouAndMeButton(
            text = ComposePreview.button,
            backgroundColor = Color.Red,
            textColor = Color.White
        )
    }
}