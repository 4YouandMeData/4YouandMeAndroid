package org.fouryouandme.yourdata.items

import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.your_data_graph_item.*
import org.fouryouandme.R
import org.fouryouandme.core.cases.yourdata.YourDataPeriod
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.yourdata.UserDataAggregation
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.NumberFormat

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

    override fun bind(t: YourDataGraphItem, position: Int) {

        root.setBackgroundColor(t.configuration.theme.fourthColor.color())

        title.text = t.userData.title
        title.setTextColor(t.configuration.theme.primaryTextColor.color())

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
            legend.form = Legend.LegendForm.NONE
            legend.textColor =
                t.userData.color?.let { HEXColor(it).color() }
                    ?: t.configuration.theme.primaryColorEnd.color()
            legend.xOffset = -10.0f
            legend.isEnabled = true

        }

        configureXAxis(t.configuration)

        //val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(currentFilter, valueList)

        val input =
            t.userData.data.mapNotNull { it }

        val media = input.average()
        val entry =
            input.mapIndexed { index, item ->
                Entry(index.toFloat(), item.toFloat())
            }

        configureYAxis(t.configuration, t.userData, media.toFloat())

        val end = ZonedDateTime.now().minusDays(1)
        val start =
            when (t.period) {
                YourDataPeriod.Day -> end.minusHours(24)
                YourDataPeriod.Week -> end.minusDays(7)
                YourDataPeriod.Month -> end.minusMonths(1)
                YourDataPeriod.Year -> end.minusYears(1)
            }

        val dateFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        val formattedPeriodStart = start.format(dateFormat)
        val formattedPeriodEnd = end.format(dateFormat)

        val set1 = LineDataSet(entry, "$formattedPeriodStart - $formattedPeriodEnd")
        set1.color =
            t.userData.color?.let { HEXColor(it).color() }
                ?: t.configuration.theme.primaryColorEnd.color()
        set1.setDrawIcons(false)
        set1.setDrawValues(false)
        set1.setDrawCircles(true)
        set1.setDrawCircleHole(true)

        set1.color = t.configuration.theme.primaryTextColor.color()
        set1.setCircleColor(
            t.userData.color?.let { HEXColor(it).color() }
                ?: t.configuration.theme.primaryColorEnd.color()
        )
        set1.lineWidth = 2f
        set1.circleRadius = 6f
        set1.circleHoleColor = t.configuration.theme.secondaryColor.color()
        set1.circleHoleRadius = 5f
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        val formatter: NumberFormat = NumberFormat.getNumberInstance()
        formatter.minimumFractionDigits = 1
        formatter.maximumFractionDigits = 1

        val lineData = LineData(set1)
        chart.data = lineData
        chart.notifyDataSetChanged()
        chart.invalidate()
    }

    private fun configureXAxis(configuration: Configuration) {

        chart.xAxis.apply {

            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            axisLineColor = configuration.theme.primaryTextColor.color()
            textColor = configuration.theme.primaryTextColor.color()
            yOffset = 10f
            labelCount = 4
            axisMinimum = 0f
            axisMaximum = 4f

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
        ll1.lineColor =
            data.color?.let { HEXColor(it).color() }
                ?: configuration.theme.primaryColorEnd.color()

        chart.axisRight.apply {

            removeAllLimitLines()
            setCenterAxisLabels(false)
            axisLineColor = configuration.theme.primaryTextColor.color()
            textColor = configuration.theme.primaryTextColor.color()
            setDrawGridLines(false)
            setDrawLimitLinesBehindData(true)
            addLimitLine(ll1)
            axisMaximum = 200f
            axisMinimum = 0f

        }

    }

    override val containerView: View? = itemView

    companion object {

        fun factory(): ViewHolderFactory =
            ViewHolderFactory(
                { YourDataGraphViewHolder(it) },
                { _, item -> item is YourDataGraphItem }
            )

    }
}