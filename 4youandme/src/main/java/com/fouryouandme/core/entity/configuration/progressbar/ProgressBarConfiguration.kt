package com.fouryouandme.core.entity.configuration.progressbar

import android.graphics.Paint
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.Gravity


fun progressDrawable(
    backgroundColor: Int,
    progressColor: Int,
): LayerDrawable {

    // Create a ShapeDrawable to generate progress bar background
    val background = ShapeDrawable()
    background.paint.style = Paint.Style.FILL
    background.paint.color = backgroundColor

    // Initialize a new shape drawable to draw progress bar progress
    val progress = ShapeDrawable()
    progress.paint.style = Paint.Style.FILL
    progress.paint.color = progressColor

    // Initialize a ClipDrawable to generate progress on progress bar
    val clipDrawable = ClipDrawable(progress, Gravity.START, ClipDrawable.HORIZONTAL)

    // Initialize a new LayerDrawable to hold progress bar all states
    val layer = LayerDrawable(arrayOf(background, clipDrawable))

    // Set the ids for different layers on layer drawable
    layer.setId(0, android.R.id.background)
    layer.setId(1, android.R.id.progress)

    return layer

}