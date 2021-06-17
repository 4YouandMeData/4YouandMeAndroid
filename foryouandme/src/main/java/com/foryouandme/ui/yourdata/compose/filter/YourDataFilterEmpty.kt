package com.foryouandme.ui.yourdata.compose.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.button.ForYouAndMeButton

@Composable
fun YourDataFilterEmpty(
    configuration: Configuration,
    padding: PaddingValues,
    onFilterButtonClicked: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(padding)) {
        Text(
            text = configuration.text.yourData.emptyFilterMessage,
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        ForYouAndMeButton(
            text = configuration.text.yourData.emptyFilterButton,
            backgroundColor = configuration.theme.primaryColorEnd.value,
            textColor = configuration.theme.secondaryColor.value,
            onClick = onFilterButtonClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}