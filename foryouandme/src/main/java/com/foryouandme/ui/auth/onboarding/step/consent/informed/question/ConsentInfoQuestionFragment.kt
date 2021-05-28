package com.foryouandme.ui.auth.onboarding.step.consent.informed.question

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.removeBackButton
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.ConsentInfoQuestionBinding
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoAbort
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoStateEvent
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoStateUpdate
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentInfoQuestionFragment :
    ConsentInfoSectionFragment(R.layout.consent_info_question) {

    private val args: ConsentInfoQuestionFragmentArgs by navArgs()

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ConsentAnswerViewHolder.factory {
                viewModel.execute(ConsentInfoStateEvent.Answer(args.index, it.answer.id))
            }
        )
    }

    private val binding: ConsentInfoQuestionBinding?
        get() = view?.let { ConsentInfoQuestionBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    ConsentInfoStateUpdate.ConsentInfo -> applyData()
                    is ConsentInfoStateUpdate.Questions -> applyAnswer()
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        applyData()
        if (adapter.itemCount <= 0) applyAnswer()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setUpView() {

        val viewBinding = binding

        if (viewBinding != null) {

            consentInfoFragment().binding?.toolbar?.removeBackButton()

            viewBinding.recyclerView.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL,
                    false
                )
            viewBinding.recyclerView.adapter = adapter

            viewBinding.action1.background = button(resources, imageConfiguration.nextStep())
            viewBinding.action1.setOnClickListener {
                viewModel.execute(ConsentInfoStateEvent.NextQuestion(args.index))
            }
        }
    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val consentInfo = viewModel.state.consentInfo

        if (viewBinding != null && configuration != null && consentInfo != null) {

            setStatusBar(configuration.theme.primaryColorStart.color())

            viewBinding.root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            consentInfoFragment()
                .showAbort(
                    configuration.theme.secondaryColor.color(),
                    ConsentInfoAbort.FromQuestion(
                        consentInfo.questions.getOrNull(args.index)?.id.orEmpty()
                    )
                )

            viewBinding.question.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.question.setHtmlText(
                consentInfo.questions
                    .getOrNull(args.index)
                    ?.text
                    .orEmpty(),
                true
            )

            viewBinding.shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    configuration.theme.primaryColorEnd
                ).drawable()

        }
    }

    private fun applyAnswer() {

        val answers = viewModel.getAnswers(args.index)
        val viewBinding = binding

        if (answers != null && viewBinding != null) {

            adapter.submitList(answers)

            viewBinding.action1.isEnabled =
                answers.fold(
                    false,
                    { acc, answer -> acc || answer.isSelected }
                )

        }

    }

}