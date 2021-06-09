package com.foryouandme.researchkit.step.date

import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.databinding.StepDateBinding
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.entity.source.applyImage
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

@AndroidEntryPoint
class DatePickerStepFragment : StepFragment(R.layout.step_date) {

    private val binding: StepDateBinding?
        get() = view?.let { StepDateBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel.getStepByIndexAs<DatePickerStep>(indexArg())?.let { applyData(it) }

    }

    private fun applyData(step: DatePickerStep) {

        val viewBinding = binding

        val start = ZonedDateTime.now()

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        viewBinding?.datePicker?.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

        step.minDate?.let { viewBinding?.datePicker?.minDate = it }
        step.maxDate?.let { viewBinding?.datePicker?.maxDate = it }

        step.image?.let { viewBinding?.icon?.applyImage(it) }
        viewBinding?.icon?.isVisible = step.image != null

        viewBinding?.question?.text = step.question(requireContext())
        viewBinding?.question?.setTextColor(step.questionColor)

        viewBinding?.shadow?.background = shadow(step.shadowColor)

        viewBinding?.button?.applyImage(step.buttonImage)
        viewBinding?.button?.setOnClickListener {

            addResult(

                SingleStringAnswerResult(
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

    private fun getFormattedSelectedDate(): String {

        val viewBinding = binding

        val year = viewBinding?.datePicker?.year
        val month = viewBinding?.datePicker?.month
        val day = viewBinding?.datePicker?.dayOfMonth

        return if (year != null && month != null && day != null) {
            val date =
                LocalDate.of(
                    year,
                    month + 1,  // +1 because Android starts to count months from 0
                    day
                )

            date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        } else ""

    }
}