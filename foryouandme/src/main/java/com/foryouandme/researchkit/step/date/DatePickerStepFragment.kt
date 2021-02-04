package com.foryouandme.researchkit.step.date

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.databinding.StepDateBinding
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.utils.applyImage
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

        viewModel.getStepByIndexAs<DatePickerStep>(indexArg())?.let { applyData(it) }

    }

    private fun applyData(step: DatePickerStep) {

        val viewBinding = binding

        val start = ZonedDateTime.now()

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

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