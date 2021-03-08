package com.foryouandme.researchkit.step.trailmaking

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.FrameLayout

class TrailMakingLineView(context: Context) : FrameLayout(
    context
) {

    private val paint = Paint()

    private var start: TrailMakingPoint? = null
    private var end: TrailMakingPoint? = null

    fun setPoints(startPoint: TrailMakingPoint, endPoint: TrailMakingPoint) {
        start = startPoint
        end = endPoint
    }

    override fun onDraw(canvas: Canvas?) {

        val startPoint = start
        val endPoint = end

        if (startPoint != null && endPoint != null)
            canvas!!.drawLine(
                startPoint.x.toFloat(),
                startPoint.y.toFloat(),
                endPoint.x.toFloat(),
                endPoint.y.toFloat(),
                paint
            )
    }

}