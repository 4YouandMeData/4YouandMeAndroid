package com.foryouandme.researchkit.step.trailmaking

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.foryouandme.databinding.StepTrailMakingPointBinding

class TrailMakingPointView(context: Context) : FrameLayout(context) {

    private val binding: StepTrailMakingPointBinding =
        StepTrailMakingPointBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    var text: CharSequence
        get() = binding.dot.text
        set(value) {
            binding.dot.text = value
        }

    fun setTextColor(color: Int) {

        binding.dot.setTextColor(color)

    }

    fun setCircleBackgroundColor(color: Int) {

        binding.dot.backgroundTintList = ColorStateList.valueOf(color)

    }

}