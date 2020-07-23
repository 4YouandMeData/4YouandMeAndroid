package org.fouryouandme.main

import android.graphics.Color
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

internal class MenuSpannable : MetricAffectingSpan() {
    var color = Color.RED
    var size = 40
    override fun updateMeasureState(p: TextPaint) {
        p.color = color
        p.textSize = size.toFloat()
        /* p.setText --- whatever --- */
    }

    override fun updateDrawState(tp: TextPaint) {
        tp.color = color
        tp.textSize = size.toFloat()
        /* tp.setText --- whatever --- */
    }

    private fun setSelected(selected: Boolean) {
        if (selected) {
            color = Color.RED
            size = 40
        } else {
            color = Color.BLUE
            size = 20
        }
    }

    init {
        setSelected(false)
    }
}