package org.fouryouandme.auth.screening.questions

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.screening.*
import kotlinx.android.synthetic.main.screening_questions.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningFragment
import org.fouryouandme.auth.screening.ScreeningStateUpdate
import org.fouryouandme.auth.screening.ScreeningViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.*


class ScreeningQuestionsFragment : BaseFragment<ScreeningViewModel>(
    R.layout.screening_questions
) {

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ScreeningQuestionViewHolder.factory { viewModel.answer(it) }
        )
    }

    override val viewModel: ScreeningViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                ScreeningViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is ScreeningStateUpdate.Questions -> applyQuestions(it.questions)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().screening }
            .map {
                applyConfiguration(it.first)
                showQuestions(it.first, it.second)
            }
    }

    private fun showQuestions(configuration: Configuration, screening: Screening): Unit {

        if (adapter.itemCount <= 0)
            applyQuestions(screening.questions.map { it.toItem(configuration) })
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        (requireParentFragment().requireParentFragment() as? ScreeningFragment)
            .toOption()
            .map { it.showAbort(configuration, configuration.theme.primaryColorEnd.color()) }

        root.setBackgroundColor(configuration.theme.secondaryColor.color())
        footer.setBackgroundColor(configuration.theme.secondaryColor.color())

    }

    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map {
                it.showBackSecondaryButton(imageConfiguration)
                { viewModel.back(findNavController()) }
            }

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        next.background =
            button(
                requireContext().resources,
                requireContext().imageConfiguration.signUpNextStepSecondary()
            )
        next.setOnClickListener { viewModel.validate(findNavController()) }
    }

    private fun applyQuestions(questions: List<ScreeningQuestionItem>): Unit {

        adapter.submitList(questions)

        next.isEnabled =
            questions.fold(
                true,
                { acc, item -> acc && item.answer.isDefined() }
            )

    }
}