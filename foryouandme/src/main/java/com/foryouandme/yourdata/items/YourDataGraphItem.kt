package com.foryouandme.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.foryouandme.R
import com.foryouandme.core.cases.yourdata.YourDataPeriod
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.HEXColor
import com.foryouandme.core.entity.yourdata.UserDataAggregation
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.holder.DroidViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_graph_item.*
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

data class YourDataGraphItem(
    val configuration: Configuration,
    val userData: UserDataAggregation,
    val period: YourDataPeriod
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<YourDataGraphItem> {
            it.userData.id == userData.id &&
                    it.period == it.period
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<YourDataGraphItem> {
            it.userData == this.userData &&
                    it.period == it.period
        }
}

class YourDataGraphViewHolder(parent: ViewGroup) :
    DroidViewHolder<YourDataGraphItem, Unit>(parent, R.layout.your_data_graph_item),
    LayoutContainer {

    override fun bind(item: YourDataGraphItem, position: Int) {

        root.setBackgroundColor(item.configuration.theme.fourthColor.color())

        title.text = item.userData.title
        title.setTextColor(item.configuration.theme.primaryTextColor.color())

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
                item.userData.color?.let { HEXColor(it).color() }
                    ?: item.configuration.theme.primaryColorEnd.color()
            legend.xOffset = -10.0f
            legend.isEnabled = true

        }

        configureXAxis(item.configuration, item.userData, item.period)

        val input =
            item.userData.data.mapNotNull { it }

        val media = input.average()
        val entry =
            item.userData.data.mapIndexed { index, item ->
                Entry(index.toFloat(), item?.toFloat() ?: 0f)
            }

        configureYAxis(item.configuration, item.userData, media.toFloat())

        val end = ZonedDateTime.now()
        val start =
            when (item.period) {
                YourDataPeriod.Week -> end.minusDays(7)
                YourDataPeriod.Month -> end.minusMonths(1)
                YourDataPeriod.Year -> end.minusYears(1)
            }

        val dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val formattedPeriodStart = start.format(dateFormat)
        val formattedPeriodEnd = end.format(dateFormat)

        val set1 = LineDataSet(entry, "$formattedPeriodStart - $formattedPeriodEnd")
        set1.color =
            item.userData.color?.let { HEXColor(it).color() }
                ?: item.configuration.theme.primaryColorEnd.color()
        set1.setDrawIcons(false)
        set1.setDrawValues(false)
        set1.color = item.userData.color?.let { HEXColor(it).color() }
            ?: item.configuration.theme.primaryColorEnd.color()
        set1.lineWidth = 2f
        set1.axisDependency = YAxis.AxisDependency.RIGHT

        if (item.period == YourDataPeriod.Week) {
            set1.setDrawCircles(true)
            set1.setDrawCircleHole(true)
            drawCircles(set1, item.userData.color, item.configuration)
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
        configuration: Configuration,
        data: UserDataAggregation,
        period: YourDataPeriod
    ) {

        chart.xAxis.apply {

            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            axisLineColor = data.color?.let { HEXColor(it).color() }
                ?: configuration.theme.primaryColorEnd.color()
            textColor = configuration.theme.primaryTextColor.color()
            yOffset = 10f
            valueFormatter = IndexAxisValueFormatter(getFormattedXAxisLabels(data, period))

            if (period == YourDataPeriod.Year) labelCount = data.xLabels.size
        }
    }


    private fun configureYAxis(
        configuration: Configuration,
        data: UserDataAggregation,
        media: Float
    ) {

        val ll1 =
            LimitLine(media, "")
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
                axisMaximum = (data.yLabels.size).toFloat()
                axisMinimum = 0f
                valueFormatter = IndexAxisValueFormatter(data.yLabels)
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
            LocalDate.parse(it, formatter)
                .dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }

    private fun getFormattedXAxisLabelsMonth(
        data: UserDataAggregation,
        formatter: DateTimeFormatter
    ): List<String> =
        data.xLabels.map {
            "${
                LocalDate.parse(it, formatter)
                    .dayOfMonth
            } ${
                LocalDate.parse(it, formatter)
                    .month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
            }"
        }

    private fun getFormattedXAxisLabelsYear(
        data: UserDataAggregation,
        formatter: DateTimeFormatter
    ): List<String> =
        data.xLabels.map {
            try {
                LocalDate.parse("01-$it", formatter)
                    .month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
            } catch (e: Exception) {
                e.printStackTrace().toString()
            }
        }

    override val containerView: View? = itemView

    companion object {

        fun factory(): DroidViewHolderFactory =
            DroidViewHolderFactory(
                { YourDataGraphViewHolder(it) },
                { _, item -> item is YourDataGraphItem }
            )

    }
}