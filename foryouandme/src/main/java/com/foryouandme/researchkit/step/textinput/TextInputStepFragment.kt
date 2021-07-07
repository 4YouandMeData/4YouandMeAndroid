package com.foryouandme.researchkit.step.textinput

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.ext.hideKeyboard
import com.foryouandme.databinding.StepTextInputBinding
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.entity.source.applyImage
import com.foryouandme.entity.source.applyImageAsButton
import com.foryouandme.researchkit.step.textinput.compose.TextInputPage
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.ZonedDateTime

@AndroidEntryPoint
class TextInputStepFragment : StepFragment() {

    private val viewModel: TextInputViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                TextInputPage(
                    viewModel = viewModel,
                    onNext = {
                        if (it != null) addResult(it)
                        next()
                    }
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<TextInputStep>(indexArg())
        if(step != null) viewModel.execute(TextInputAction.SetStep(step))

    }

    /*private fun applyData(step: TextInputStep) {

        val viewBinding = binding

        val start = ZonedDateTime.now()

        binding?.root?.setBackgroundColor(step.backgroundColor)

        step.image?.let { viewBinding?.icon?.applyImage(it) }
        viewBinding?.icon?.isVisible = step.image != null

        viewBinding?.question?.text = step.question(requireContext())
        viewBinding?.question?.setTextColor(step.questionColor)

        viewBinding?.textInput?.setTextColor(step.textColor)
        viewBinding?.textInput?.setHintTextColor(step.placeholderColor)

        viewBinding?.shadow?.background = shadow(step.shadowColor)

        step.placeholder?.let { viewBinding?.textInput?.hint = it }
        step.maxCharacters?.let {
            if (viewBinding != null)
                viewBinding.textInput.filters += InputFilter.LengthFilter(it)
        }

        viewBinding?.button?.applyImageAsButton(step.buttonImage)
        viewBinding?.button?.setOnClickListener {

            addResult(

                SingleStringAnswerResult(
                    step.identifier,
                    start,
                    ZonedDateTime.now(),
                    step.questionId,
                    binding?.textInput?.text.toString()
                )

            )

            hideKeyboard()

            next()

        }

    }*/

}