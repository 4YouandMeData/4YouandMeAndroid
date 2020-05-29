package org.fouryouandme.auth.screening.questions

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import arrow.core.extensions.fx
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.screening_questions.*
import org.fouryouandme.R
import org.fouryouandme.auth.screening.ScreeningStateUpdate
import org.fouryouandme.auth.screening.ScreeningViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackSecondaryButton


class ScreeningQuestionsFragment : BaseFragment<ScreeningViewModel>(
    R.layout.screening_questions
) {

    private val adapter: StableDroidAdapter<DroidItem> by lazy {
        screeningAdapter { viewModel.answer(it) }
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

        root.setBackgroundColor(configuration.theme.secondaryColor.color())
        footer.setBackgroundColor(configuration.theme.secondaryColor.color())

        abort.text = configuration.text.onboarding.onboardingAbortButton
        abort.setTextColor(configuration.theme.primaryColorEnd.color())
        abort.setOnClickListener { showAbortAlert(configuration) }


    }

    private fun setupView(): Unit {

        toolbar.showBackSecondaryButton(imageConfiguration) { viewModel.back(findNavController()) }

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

    private fun showAbortAlert(configuration: Configuration) {

        AlertDialog.Builder(requireContext())
            .setTitle(configuration.text.onboarding.onboardingAbortTitle)
            .setMessage(configuration.text.onboarding.onboradingAbortMessage)
            .setPositiveButton(configuration.text.onboarding.onboardingAbortConfirm)
            { _, _ -> viewModel.abort(rootNavController())}
            .setNegativeButton(configuration.text.onboarding.onboardingAbortCancel, null)
            .show()

    }
}