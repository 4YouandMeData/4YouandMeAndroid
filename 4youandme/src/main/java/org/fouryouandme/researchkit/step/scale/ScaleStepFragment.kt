package org.fouryouandme.researchkit.step.scale

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.step_scale.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.background.shadow
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.result.SingleAnswerResult
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.utils.applyImage
import org.threeten.bp.ZonedDateTime

class ScaleStepFragment : StepFragment(R.layout.step_scale) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            val step =
                viewModel.getStepByIndexAs<ScaleStep>(indexArg())

            step?.let { applyData(it) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun applyData(
        step: ScaleStep
    ): Unit =

        evalOnMain {
            val start = ZonedDateTime.now()

            root.setBackgroundColor(step.backgroundColor)

            step.image?.let { icon.applyImage(it) }
            icon.isVisible = step.image != null

            question.text = step.question(requireContext())
            question.setTextColor(step.questionColor)

            value.text = "0"

            slider.progressTintList = ColorStateList.valueOf(step.progressColor)
            slider.min = step.minValue
            slider.max = step.maxValue
            slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                    // Display the current progress of SeekBar
                    value.text = i.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            min_label.text = "${step.minValue}% likely"
            max_label.text = "${step.minValue}% likely"

            shadow.background = shadow(step.shadowColor)

            button.applyImage(step.buttonImage)
            button.setOnClickListener {
                startCoroutineAsync {
                    viewModel.addResult(
                        SingleAnswerResult(
                            step.identifier,
                            start,
                            ZonedDateTime.now(),
                            step.questionId,
                            slider.progress.toString()
                        )
                    )
                    next()
                }
            }

        }

}