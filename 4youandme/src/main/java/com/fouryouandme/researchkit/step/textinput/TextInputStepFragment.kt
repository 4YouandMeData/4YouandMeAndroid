package com.fouryouandme.researchkit.step.textinput

import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.view.isVisible
import com.fouryouandme.R
import com.fouryouandme.core.entity.configuration.background.shadow
import com.fouryouandme.core.ext.evalOnMain
import com.fouryouandme.core.ext.startCoroutineAsync
import com.fouryouandme.researchkit.result.SingleAnswerResult
import com.fouryouandme.researchkit.step.StepFragment
import com.fouryouandme.researchkit.utils.applyImage
import com.fouryouandme.researchkit.utils.applyImageAsButton
import kotlinx.android.synthetic.main.step_text_input.*
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

            text_input.setTextColor(step.textColor)
            text_input.setHintTextColor(step.placeholderColor)

            shadow.background = shadow(step.shadowColor)

            step.placeholder?.let { text_input.hint = it }
            step.maxCharacters?.let { text_input.filters += InputFilter.LengthFilter(it) }

            button.applyImageAsButton(step.buttonImage)
            button.setOnClickListener {
                startCoroutineAsync {

                    viewModel.addResult(

                        SingleAnswerResult(
                            step.identifier,
                            start,
                            ZonedDateTime.now(),
                            step.questionId,
                            text_input.text.toString()
                        )

                    )

                    next()

                }
            }

        }

}