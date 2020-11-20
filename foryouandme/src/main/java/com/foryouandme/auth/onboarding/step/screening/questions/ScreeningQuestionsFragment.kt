package com.foryouandme.auth.onboarding.step.screening.questions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.auth.onboarding.step.screening.ScreeningSectionFragment
import com.foryouandme.auth.onboarding.step.screening.ScreeningStateUpdate
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.button.button
import com.foryouandme.core.entity.screening.Screening
import com.foryouandme.core.ext.*
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_questions.*


class ScreeningQuestionsFragment : ScreeningSectionFragment(
    R.layout.screening_questions
) {

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ScreeningQuestionViewHolder.factory { startCoroutineAsync { viewModel.answer(it) } }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) {
                when (it) {
                    is ScreeningStateUpdate.Questions ->
                        startCoroutineAsync { applyQuestions(it.questions) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screeningAndConfiguration { config, state ->
            setupView()
            applyConfiguration(config)
            showQuestions(config, state.screening)
        }
    }

    private suspend fun showQuestions(configuration: Configuration, screening: Screening): Unit =
        evalOnMain {

            if (adapter.itemCount <= 0)
                applyQuestions(screening.questions.map { it.toItem(configuration) })
        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.secondaryColor.color())

            screeningFragment().showAbort(
                configuration,
                configuration.theme.primaryColorEnd.color()
            )

            root.setBackgroundColor(configuration.theme.secondaryColor.color())
            footer.setBackgroundColor(configuration.theme.secondaryColor.color())

        }

    private suspend fun setupView(): Unit =
        evalOnMain {

            screeningFragment().toolbar.showBackSecondaryButton(imageConfiguration)
            {
                startCoroutineAsync {
                    viewModel.back(
                        screeningNavController(),
                        authNavController(),
                        rootNavController()
                    )
                }
            }

            recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            recycler_view.adapter = adapter

            action_1.background =
                button(
                    requireContext().resources,
                    requireContext().imageConfiguration.nextStepSecondary()
                )

            action_1.setOnClickListener {
                startCoroutineAsync {
                    viewModel.validate(
                        rootNavController(),
                        screeningNavController()
                    )
                }
            }
        }

    private suspend fun applyQuestions(questions: List<ScreeningQuestionItem>): Unit =
        evalOnMain {

            adapter.submitList(questions)

            action_1.isEnabled =
                questions.fold(
                    true,
                    { acc, item -> acc && item.answer != null }
                )

        }
}