package com.foryouandme.ui.userInfo.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.dialog.AlertDialog

@Composable
fun PhaseSwitchAlert(
    configuration: Configuration,
    onPositiveClicked: () -> Unit,
    onNegativeClicked: () -> Unit
) {
    AlertDialog(
        title = configuration.text.profile.userInfo.permanentAlertTitle,
        message = configuration.text.profile.userInfo.permanentAlertMessage,
        positiveButton = configuration.text.profile.userInfo.permanentAlertConfirm,
        negativeButton = configuration.text.profile.userInfo.permanentAlertCancel,
        onPositiveClicked = onPositiveClicked,
        onNegativeClicked = onNegativeClicked
    )
}

@Preview
@Composable
private fun PhaseSwitchAlertPreview() {
    ForYouAndMeTheme {
        PhaseSwitchAlert(
            configuration = Configuration.mock(),
            onPositiveClicked = {},
            onNegativeClicked = {}
        )
    }
}