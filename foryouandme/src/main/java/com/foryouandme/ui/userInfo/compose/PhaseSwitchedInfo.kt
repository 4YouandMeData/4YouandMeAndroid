package com.foryouandme.ui.userInfo.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.button.ForYouAndMeButton
import com.foryouandme.ui.compose.dialog.AlertDialog

@Composable
fun PhaseSwitchedInfo(
    configuration: Configuration,
    onPositiveClicked: () -> Unit,
    onNegativeClicked: () -> Unit
) {
   AlertDialog(
       title = null,
       message = configuration.text.profile.phase.switchDescription,
       positiveButton = configuration.text.profile.phase.switchYes,
       negativeButton = configuration.text.profile.phase.switchNo,
       onPositiveClicked = onPositiveClicked,
       onNegativeClicked = onNegativeClicked
   )
}

@Preview
@Composable
private fun PhaseSwitchedInfoPreview() {
    ForYouAndMeTheme {
        PhaseSwitchedInfo(
            configuration = Configuration.mock(),
            onPositiveClicked = {},
            onNegativeClicked = {}
        )
    }
}