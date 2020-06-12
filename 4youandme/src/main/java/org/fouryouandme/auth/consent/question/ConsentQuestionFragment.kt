package org.fouryouandme.auth.consent.question

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.toOption
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.consent.*
import kotlinx.android.synthetic.main.consent_question.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.ConsentViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.consent.Consent
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.removeBackButton

class ConsentQuestionFragment : BaseFragment<ConsentViewModel>(R.layout.consent_question) {

    private val args: ConsentQuestionFragmentArgs by navArgs()

    override val viewModel: ConsentViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentViewModel(navigator, IORuntime) })
    }

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(ConsentAnswerViewHolder.factory { })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().consent }
            .map { applyData(it.first, it.second) }
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

    }

    private fun applyData(configuration: Configuration, consent: Consent): Unit {

        if (adapter.itemCount <= 0)
            consent.questions
                .getOrNull(args.index)
                .toOption()
                .map { question ->
                    adapter.submitList(question.answers.all.map { it.toItem(configuration) })
                }

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()


        question.setTextColor(configuration.theme.secondaryColor.color())
        question.text =
            consent.questions
                .getOrNull(args.index)
                .toOption()
                .map { it.text }
                .getOrElse { "" }

    }

}