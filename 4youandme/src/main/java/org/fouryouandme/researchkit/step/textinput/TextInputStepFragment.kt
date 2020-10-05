package org.fouryouandme.researchkit.step.textinput

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.step_text_input.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.background.shadow
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.result.SingleAnswerResult
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.utils.applyImage
import org.fouryouandme.researchkit.utils.applyImageAsButton
import org.threeten.bp.ZonedDateTime

class TextInputStepFragment : StepFragment(R.layout.step_text_input) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            val step =
                viewModel.getStepByIndexAs<TextInputStep>(indexArg())

            step?.let { applyData(it) }
        }
    }

    private suspend fun applyData(
        step: TextInputStep
    ): Unit =

        evalOnMain {
            val start = ZonedDateTime.now()

            root.setBackgroundColor(step.backgroundColor)

            step.image?.let { icon.applyImage(it) }
            icon.isVisible = step.image != null

            question.text = step.question(requireContext())
            question.setTextColor(step.questionColor)

            shadow.background = shadow(step.shadowColor)

            button.applyImageAsButton(step.buttonImage)
            button.setOnClickListener {
                startCoroutineAsync {
                    viewModel.addResult(
                        SingleAnswerResult(
                            step.identifier,
                            start,
                            ZonedDateTime.now(),
                            step.questionId,
                            text_input.text.toString() ?: ""
                        )
                    )
                    next()
                }
            }

        }

}