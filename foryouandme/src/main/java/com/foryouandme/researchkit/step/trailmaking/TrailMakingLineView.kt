package com.foryouandme.researchkit.step.trailmaking

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.foryouandme.core.ext.dpToPx

class TrailMakingLineView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private val paint = Paint()

    private var lines: List<Pair<TrailMakingPoint, TrailMakingPoint>> = emptyList()

    init {

        setWillNotDraw(false)
        paint.isAntiAlias = true
        paint.strokeWidth = 4.dpToPx().toFloat()
        paint.style = Paint.Style.FILL
        paint.strokeJoin = Paint.Join.MITER

    }

    fun setLineColor(color: Int) {
        paint.color = color
    }

    fun addLine(line: Pair<TrailMakingPoint, TrailMakingPoint>) {
        lines =
            lines.toMutableList()
                .also { it.add(line) }
                .distinctBy { "${it.first.name}_${it.second.name}" }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {

        lines.forEach { (startPoint, endPoint) ->

            canvas!!.drawLine(
                startPoint.x.toFloat(),
                startPoint.y.toFloat(),
                endPoint.x.toFloat(),
                endPoint.y.toFloat(),
                paint
            )

        }

    }

}