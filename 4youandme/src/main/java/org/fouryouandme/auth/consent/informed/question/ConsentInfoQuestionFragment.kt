package org.fouryouandme.auth.consent.informed.question

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.toOption
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.consent_info.*
import kotlinx.android.synthetic.main.consent_info_question.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoFragment
import org.fouryouandme.auth.consent.informed.ConsentInfoStateUpdate
import org.fouryouandme.auth.consent.informed.ConsentInfoViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.consent.informed.ConsentInfo
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton

class ConsentInfoQuestionFragment :
    BaseFragment<ConsentInfoViewModel>(R.layout.consent_info_question) {

    private val args: ConsentInfoQuestionFragmentArgs by navArgs()

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    IORuntime
                )
            })
    }

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ConsentAnswerViewHolder.factory {
                viewModel.answer(args.index, it.answer.id)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observe(this,
                Observer { event ->
                    when (event.peekContent()) {
                        is ConsentInfoStateUpdate.Questions ->
                            viewModel.getAnswers(args.index).map { applyAnswer(it) }
                    }
                }
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().consentInfo }
            .map { applyData(it.first, it.second) }

        if (adapter.itemCount <= 0)
            viewModel.getAnswers(args.index).map { applyAnswer(it) }
    }

    fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.removeBackButton() }

        recycler_view.layoutManager =
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        recycler_view.adapter = adapter

        next.background = button(resources, imageConfiguration.signUpNextStep())
        next.setOnClickListener { viewModel.nextQuestion(findNavController(), args.index) }
    }

    private fun applyData(configuration: Configuration, consentInfo: ConsentInfo): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        (requireParentFragment().requireParentFragment() as? ConsentInfoFragment)
            .toOption()
            .map { it.showAbort(configuration, configuration.theme.secondaryColor.color()) }

        question.setTextColor(configuration.theme.secondaryColor.color())
        question.text =
            consentInfo.questions
                .getOrNull(args.index)
                .toOption()
                .map { it.text }
                .getOrElse { "" }

        shadow.background =
            HEXGradient.from(
                HEXColor.transparent(),
                configuration.theme.primaryColorEnd
            ).drawable()

    }

    private fun applyAnswer(answers: List<ConsentAnswerItem>): Unit {

        adapter.submitList(answers)

        next.isEnabled =
            answers.fold(
                false,
                { acc, answer -> acc || answer.isSelected }
            )
    }

}