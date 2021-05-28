package com.foryouandme.ui.auth.onboarding.step.screening.questions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.*
import com.foryouandme.databinding.ScreeningQuestionsBinding
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.onboarding.step.screening.*
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_questions.*
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ScreeningQuestionsFragment : ScreeningSectionFragment(
    R.layout.screening_questions
) {

    private val binding: ScreeningQuestionsBinding?
        get() = view?.let { ScreeningQuestionsBinding.bind(it) }

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ScreeningQuestionViewHolder.factory {
                viewModel.execute(ScreeningStateEvent.Answer(it))
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is ScreeningStateUpdate.Screening -> applyQuestions()
                    is ScreeningStateUpdate.Questions -> applyQuestions()
                    else -> Unit
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    ScreeningQuestionsToScreeningSuccess,
                    ScreeningQuestionsToScreeningFailure ->
                        navigator.navigateTo(screeningNavController(), it)
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyConfiguration()
        if(adapter.itemCount <= 0) applyQuestions()


    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyConfiguration()
    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val configuration = configuration

        if (viewBinding != null && configuration != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            screeningFragment().showAbort(configuration.theme.primaryColorEnd.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())
            viewBinding.footer.setBackgroundColor(configuration.theme.secondaryColor.color())

        }

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            screeningFragment()
                .binding
                ?.toolbar
                ?.showBackSecondaryButton(imageConfiguration) { back() }

            viewBinding.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            viewBinding.recyclerView.adapter = adapter

            viewBinding.action1.background =
                button(
                    requireContext().resources,
                    imageConfiguration.nextStepSecondary()
                )

            viewBinding.action1.setOnClickListener {
                viewModel.execute(ScreeningStateEvent.Validate)
            }

        }
    }

    private fun applyQuestions() {

        val questions = viewModel.state.questions

        adapter.submitList(questions)

        binding?.action1?.isEnabled =
            questions.fold(
                true,
                { acc, item -> acc && item.answer != null }
            )

    }

}