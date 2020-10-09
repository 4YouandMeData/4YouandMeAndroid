package org.fouryouandme.auth.screening.questions

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_questions.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningSectionFragment
import org.fouryouandme.auth.screening.ScreeningStateUpdate
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.*


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
            applyConfiguration(configuration())
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

            next.background =
                button(
                    requireContext().resources,
                    requireContext().imageConfiguration.signUpNextStepSecondary()
                )

            next.setOnClickListener {
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

            next.isEnabled =
                questions.fold(
                    true,
                    { acc, item -> acc && item.answer != null }
                )

        }
}