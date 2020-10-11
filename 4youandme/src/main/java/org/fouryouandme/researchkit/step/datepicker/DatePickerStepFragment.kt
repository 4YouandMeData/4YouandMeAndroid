package org.fouryouandme.researchkit.step.datepicker

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.step_date.*
import kotlinx.android.synthetic.main.step_picker.button
import kotlinx.android.synthetic.main.step_picker.icon
import kotlinx.android.synthetic.main.step_picker.question
import kotlinx.android.synthetic.main.step_picker.root
import kotlinx.android.synthetic.main.step_picker.shadow
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.background.shadow
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.result.SingleAnswerResult
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.utils.applyImage
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import java.util.*

class DatePickerStepFragment : StepFragment(R.layout.step_date) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            val step =
                viewModel.getStepByIndexAs<DatePickerStep>(indexArg())

            step?.let { applyData(it) }
        }
    }

    private suspend fun applyData(
        step: DatePickerStep
    ): Unit =

        evalOnMain {
            val start = ZonedDateTime.now()

            root.setBackgroundColor(step.backgroundColor)

            step.image?.let { icon.applyImage(it) }
            icon.isVisible = step.image != null

            question.text = step.question(requireContext())
            question.setTextColor(step.questionColor)

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
                            getFormattedSelectedDate()
                        )
                    )

                    next()
                }
            }

        }

    private fun getFormattedSelectedDate(): String {

        val year = date_picker.year
        val month = date_picker.month
        val day = date_picker.dayOfMonth

        val date =
            ZonedDateTime.of(
                year,
                month + 1,  // +1 because Android starts to count months from 0
                day,
                0,
                0,
                0,
                0,
                ZoneOffset.UTC
            )

        return date.toString()
    }
}