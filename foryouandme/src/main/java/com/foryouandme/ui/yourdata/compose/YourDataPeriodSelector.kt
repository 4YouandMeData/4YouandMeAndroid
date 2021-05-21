package com.foryouandme.ui.yourdata.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.ext.noIndicationClickable
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.foryouandme.entity.yourdata.YourDataPeriod.*
import com.foryouandme.ui.compose.ForYouAndMeTheme

@Composable
fun YourDataPeriodSelector(
    configuration: Configuration,
    period: YourDataPeriod,
    onPeriodSelected: (YourDataPeriod) -> Unit = {},
    padding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        Text(
            text = configuration.text.yourData.dataPeriodTitle,
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            modifier =
            Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            PeriodButton(
                configuration = configuration,
                period = Week,
                currentPeriod = period,
                onPeriodSelected = onPeriodSelected
            )
            PeriodButton(
                configuration = configuration,
                period = Month,
                currentPeriod = period,
                onPeriodSelected = onPeriodSelected
            )
            PeriodButton(
                configuration = configuration,
                period = Year,
                currentPeriod = period,
                onPeriodSelected = onPeriodSelected
            )
        }
    }
}

@Composable
private fun RowScope.PeriodButton(
    configuration: Configuration,
    period: YourDataPeriod,
    currentPeriod: YourDataPeriod,
    onPeriodSelected: (YourDataPeriod) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .height(45.dp)
            .weight(1f)
            .background(
                getBackgroundColor(configuration, period, currentPeriod),
                getShape(period)
            )
            .noIndicationClickable { if (period != currentPeriod) onPeriodSelected(period) }
    ) {
        Text(
            text =
            when (period) {
                Week -> configuration.text.yourData.periodWeek
                Month -> configuration.text.yourData.periodMonth
                Year -> configuration.text.yourData.periodYear
            },
            style = MaterialTheme.typography.body1,
            color = getTextColor(configuration, period, currentPeriod),
            textAlign = TextAlign.Center,
        )
    }
}

private fun getTextColor(
    configuration: Configuration,
    period: YourDataPeriod,
    selectedPeriod: YourDataPeriod
): Color =
    if (selectedPeriod == period) configuration.theme.secondaryColor.value
    else configuration.theme.primaryTextColor.value

private fun getBackgroundColor(
    configuration: Configuration,
    period: YourDataPeriod,
    selectedPeriod: YourDataPeriod
): Color =
    if (selectedPeriod == period) configuration.theme.activeColor.value
    else configuration.theme.secondaryColor.value

private fun getShape(period: YourDataPeriod): Shape =
    when (period) {
        Week -> RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
        Month -> RectangleShape
        Year -> RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
    }

@Preview
@Composable
private fun YourDataPeriodSelector() {
    ForYouAndMeTheme {
        YourDataPeriodSelector(configuration = Configuration.mock(), period = Week)
    }
}