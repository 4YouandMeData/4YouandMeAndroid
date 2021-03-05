package com.foryouandme.researchkit.step.range

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.databinding.StepRangeBinding
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.researchkit.result.SingleIntAnswerResult
import com.foryouandme.researchkit.skip.isInOptionalRange
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.utils.applyImage
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.ZonedDateTime

@AndroidEntryPoint
class RangeStepFragment : StepFragment(R.layout.step_range) {

    private val binding: StepRangeBinding?
        get() = view?.let { StepRangeBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel.getStepByIndexAs<RangeStep>(indexArg())?.let { applyData(it) }

    }

    private fun applyData(step: RangeStep) {

        val viewBinding = binding

        val start = ZonedDateTime.now()

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        step.image?.let { viewBinding?.icon?.applyImage(it) }
        viewBinding?.icon?.isVisible = step.image != null

        viewBinding?.question?.text = step.question(requireContext())
        viewBinding?.question?.setTextColor(step.questionColor)

        viewBinding?.value?.text = step.minValue.toString()
        viewBinding?.value?.setTextColor(step.valueColor)

        viewBinding?.slider?.progressTintList = ColorStateList.valueOf(step.progressColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) viewBinding?.slider?.min = 0
        viewBinding?.slider?.max = step.maxValue - step.minValue
        viewBinding?.slider?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                val progress = if (i < 0) 0 else i
                binding?.value?.text = (progress + step.minValue).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        viewBinding?.minLabel?.text = step.minDisplayValue ?: step.minValue.toString()
        viewBinding?.minLabel?.setTextColor(step.minDisplayColor)

        viewBinding?.maxLabel?.text = step.maxDisplayValue ?: step.maxValue.toString()
        viewBinding?.maxLabel?.setTextColor(step.maxDisplayColor)

        viewBinding?.shadow?.background = shadow(step.shadowColor)

        viewBinding?.button?.applyImage(step.buttonImage)
        viewBinding?.button?.setOnClickListener {

            val answer = binding?.slider?.progress?.let { it + step.minValue }

            answer?.let {
                addResult(

                    SingleIntAnswerResult(
                        step.identifier,
                        start,
                        ZonedDateTime.now(),
                        step.questionId,
                        it
                    )

                )
            }

            checkSkip(step)

        }

    }


    private fun checkSkip(step: RangeStep) {

        val skip = step.skips.firstOrNull()

        val value = binding?.slider?.progress?.let { it + step.minValue }

        if (
            skip != null &&
            value != null &&
            isInOptionalRange(value, skip.min, skip.max)
        ) skipTo(skip.stepId)
        else next()

    }

}