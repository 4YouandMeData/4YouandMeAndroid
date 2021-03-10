package com.foryouandme.researchkit.step.trailmaking

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
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

    fun setLines(lines: List<Pair<TrailMakingPoint, TrailMakingPoint>>, color: Int) {
        this.lines = lines
        paint.color = color
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