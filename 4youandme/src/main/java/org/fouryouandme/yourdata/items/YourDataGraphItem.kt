package org.fouryouandme.yourdata.items

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.DroidViewHolder
import com.giacomoparisi.recyclerdroid.core.ViewHolderFactory
import com.giacomoparisi.recyclerdroid.core.compare
import com.github.mikephil.charting.components.AxisBase
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
import org.fouryouandme.core.entity.configuration.Configuration
import java.text.NumberFormat
import kotlin.random.Random

data class YourDataGraphItem(
    val configuration: Configuration,
    val id: String,
    val title: String
) : DroidItem<Unit> {

    override fun areTheSame(other: DroidItem<Any>): Boolean =
        other.compare<YourDataGraphItem> {
            it.id == id
        }

    override fun getPayload(other: DroidItem<Any>): List<Unit> = emptyList()

    override fun haveTheSameContent(other: DroidItem<Any>): Boolean =
        other.compare<YourDataGraphItem> {
            it == this
        }
}

class YourDataGraphViewHolder(parent: ViewGroup) :
    DroidViewHolder<YourDataGraphItem, Unit>(parent, R.layout.your_data_graph_item),
    LayoutContainer {

    override fun bind(t: YourDataGraphItem, position: Int) {
        root.setBackgroundColor(t.configuration.theme.fourthColor.color())

        title.text = t.title
        title.setTextColor(Color.BLACK)

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
            legend.textColor = Color.BLACK
            legend.xOffset = -10.0f
            legend.isEnabled = true
        }

        configureXAxis()

        //val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(currentFilter, valueList)

        val entry = arrayListOf<Entry>()
        var media = 0f
        for (x in 0..4) {
            var chartValues = 0f
            chartValues = Random.nextDouble(0.0, 200.0).toFloat()
            entry.add(Entry(x.toFloat(), chartValues))

            if (chartValues != 0f) {
                media += chartValues
            }
        }

        media /= 4
        configureYAxis(media)

        val set1 = LineDataSet(entry, "cucucucucucucuc")
        set1.color = Color.BLACK
        set1.setDrawIcons(false)
        set1.setDrawValues(false)
        set1.setDrawCircles(true)
        set1.setDrawCircleHole(true)

        set1.color = Color.RED
        set1.setCircleColor(Color.RED)
        set1.lineWidth = 2f
        set1.circleRadius = 6f
        set1.circleHoleColor = Color.WHITE
        set1.circleHoleRadius = 5f
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        val formatter: NumberFormat = NumberFormat.getNumberInstance()
        formatter.minimumFractionDigits = 1
        formatter.maximumFractionDigits = 1

        val lineData = LineData(set1)
        chart.data = lineData
    }

    private fun configureXAxis() {

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            axisLineColor = Color.GRAY
            textColor = Color.GRAY
            yOffset = 10f
            labelCount = 4
            axisMinimum = 0f
            axisMaximum = 4f
        }
    }

    private fun configureYAxis(media: Float) {

        val ll1 =
            LimitLine(media, "")
        ll1.lineWidth = 2f
        ll1.enableDashedLine(40f, 40f, 0f)
        ll1.lineColor = Color.GRAY

        chart.axisRight.apply {
            removeAllLimitLines()
            setCenterAxisLabels(false)
            axisLineColor = Color.GRAY
            textColor = Color.GRAY
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