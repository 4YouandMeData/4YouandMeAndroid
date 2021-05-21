package com.foryouandme.ui.yourdata.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.yourdata.UserDataAggregation
import com.foryouandme.entity.yourdata.YourDataPeriod
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*
import kotlin.math.min

@Composable
fun YourDataChart(
    configuration: Configuration,
    userData: UserDataAggregation,
    period: YourDataPeriod,
    padding: PaddingValues
) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        Text(
            text = userData.title,
            style = MaterialTheme.typography.body1,
            color = configuration.theme.primaryTextColor.value,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            elevation = 5.dp,
            backgroundColor = configuration.theme.secondaryColor.value,
            modifier =
            Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                AndroidView(
                    { ctx ->
                        val chart = LineChart(ctx)
                        setupChart(chart, configuration, userData, period)
                        chart
                    },
                    update = { setupChart(it, configuration, userData, period) },
                    modifier =
                    Modifier.fillMaxSize()
                )
            }
        }
    }
}

private fun setupChart(
    chart: LineChart,
    configuration: Configuration,
    userData: UserDataAggregation,
    period: YourDataPeriod
) {
    chart.apply {

        description.isEnabled = false
        isDragEnabled = false
        setScaleEnabled(false)
        setPinchZoom(false)
        axisRight.isEnabled = true
        axisLeft.isEnabled = false
        axisLeft.setDrawGridLines(false)
        setDrawGridBackground(false)
        extraBottomOffset = 20.0f
        legend.form = Legend.LegendForm.LINE
        legend.textSize = 14f
        legend.textColor =
            userData.color?.let { HEXColor(it).color() }
                ?: configuration.theme.primaryColorEnd.color()
        legend.xOffset = -10.0f
        legend.isEnabled = true

    }

    configureXAxis(chart, configuration, userData, period)

    val input =
        userData.data.mapNotNull { it }

    val media = input.average()
    val entry =
        userData.data.mapIndexed { index, item ->
            Entry(index.toFloat(), item ?: 0f)
        }

    configureYAxis(chart, configuration, userData, media.toFloat())

    val end = ZonedDateTime.now()
    val start =
        when (period) {
            YourDataPeriod.Week -> end.minusDays(7)
            YourDataPeriod.Month -> end.minusMonths(1)
            YourDataPeriod.Year -> end.minusYears(1)
        }

    val dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    val formattedPeriodStart = start.format(dateFormat)
    val formattedPeriodEnd = end.format(dateFormat)

    val set1 = LineDataSet(entry, "$formattedPeriodStart - $formattedPeriodEnd")
    set1.color =
        userData.color?.let { HEXColor(it).color() }
            ?: configuration.theme.primaryColorEnd.color()
    set1.setDrawIcons(false)
    set1.setDrawValues(false)
    set1.color = userData.color?.let { HEXColor(it).color() }
        ?: configuration.theme.primaryColorEnd.color()
    set1.lineWidth = 2f
    set1.axisDependency = YAxis.AxisDependency.RIGHT

    if (period == YourDataPeriod.Week) {
        set1.setDrawCircles(true)
        set1.setDrawCircleHole(true)
        drawCircles(set1, userData.color, configuration)
    } else {
        set1.setDrawCircles(false)
        set1.setDrawCircleHole(false)
    }

    val lineData = LineData(set1)
    chart.data = lineData
    chart.notifyDataSetChanged()
    chart.invalidate()
}

private fun drawCircles(set: LineDataSet, color: String?, configuration: Configuration) {
    set.setCircleColor(color?.let { HEXColor(it).color() }
        ?: configuration.theme.primaryColorEnd.color())

    set.circleRadius = 6f
    set.circleHoleColor = configuration.theme.secondaryColor.color()
    set.circleHoleRadius = 5f
}

private fun configureXAxis(
    chart: LineChart,
    configuration: Configuration,
    data: UserDataAggregation,
    period: YourDataPeriod
) {

    chart.xAxis.apply {

        removeAllLimitLines()
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        setDrawLimitLinesBehindData(true)
        axisLineColor = data.color?.let { HEXColor(it).color() }
            ?: configuration.theme.primaryColorEnd.color()
        textColor = configuration.theme.primaryTextColor.color()
        yOffset = 10f
        xOffset = 100f
        valueFormatter = IndexAxisValueFormatter(getFormattedXAxisLabels(data, period))

        when (period) {
            YourDataPeriod.Week ->
                labelCount = min(data.xLabels.size - 1, 7)
            YourDataPeriod.Month ->
                if (data.xLabels.size < 6)
                    labelCount = data.xLabels.size
            YourDataPeriod.Year ->
                if (data.xLabels.size < 6)
                    labelCount = data.xLabels.size
        }

    }
}


private fun configureYAxis(
    chart: LineChart,
    configuration: Configuration,
    data: UserDataAggregation,
    media: Float
) {

    val ll1 = LimitLine(media, "")
    ll1.lineWidth = 2f
    ll1.enableDashedLine(40f, 40f, 0f)
    ll1.lineColor = configuration.theme.fourthTextColor.color()

    chart.axisRight.apply {

        removeAllLimitLines()
        setCenterAxisLabels(true)
        axisLineColor = configuration.theme.primaryTextColor.color()
        textColor = configuration.theme.primaryTextColor.color()
        setDrawGridLines(false)
        setDrawLimitLinesBehindData(true)
        setDrawZeroLine(true)
        addLimitLine(ll1)

        if (data.yLabels.isEmpty().not()) {
            axisMinimum = 0f
            axisMaximum = (data.yLabels.size).toFloat()
            valueFormatter = IndexAxisValueFormatter(data.yLabels)
        } else {
            axisMinimum = data.data.fold(0f) { prev, next -> min(prev, next ?: 0f) }
        }

    }

}

private fun getFormattedXAxisLabels(
    data: UserDataAggregation,
    period: YourDataPeriod
): List<String> {

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())

    return when (period) {

        is YourDataPeriod.Week -> getFormattedXAxisLabelsWeek(
            data,
            formatter
        )
        is YourDataPeriod.Month -> getFormattedXAxisLabelsMonth(
            data,
            formatter
        )
        is YourDataPeriod.Year -> getFormattedXAxisLabelsYear(
            data,
            formatter
        )
    }
}

private fun getFormattedXAxisLabelsWeek(
    data: UserDataAggregation,
    formatter: DateTimeFormatter
): List<String> =
    data.xLabels.map {
        catchToNull { LocalDate.parse(it, formatter) }
            ?.dayOfWeek
            ?.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            .orEmpty()
    }

private fun getFormattedXAxisLabelsMonth(
    data: UserDataAggregation,
    formatter: DateTimeFormatter
): List<String> =
    data.xLabels.map {
        "${
            catchToNull { LocalDate.parse(it, formatter) }
                ?.dayOfMonth
                ?.toString()
                .orEmpty()
        } ${
            catchToNull { LocalDate.parse(it, formatter) }
                ?.month
                ?.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                .orEmpty()
        }"
    }

private fun getFormattedXAxisLabelsYear(
    data: UserDataAggregation,
    formatter: DateTimeFormatter
): List<String> =
    data.xLabels.map {
        catchToNull {
            LocalDate
                .parse("01-$it", formatter)
                .format(DateTimeFormatter.ofPattern("MMM"))
        }.orEmpty()
    }
