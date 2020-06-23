package org.fouryouandme.auth.consent.review.info

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import arrow.core.extensions.fx
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.consent_review_info.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.review.ConsentReviewError
import org.fouryouandme.auth.consent.review.ConsentReviewStateUpdate
import org.fouryouandme.auth.consent.review.ConsentReviewViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator

class ConsentReviewInfoFragment : BaseFragment<ConsentReviewViewModel>(R.layout.consent_review_info) {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                ConsentReviewViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ConsentReviewHeaderViewHolder.factory(),
            ConsentReviewPageViewHolder.factory()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is ConsentReviewStateUpdate.Initialization -> {
                        applyConfiguration(it.configuration)
                        applyItems(it.items)
                    }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active, false) }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    ConsentReviewError.Initialization ->
                        error.setError(it.error) { viewModel.initialize(rootNavController()) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to viewModel.state().items }
            .fold(
                { viewModel.initialize(rootNavController()) },
                {
                    applyConfiguration(it.first)
                    applyItems(it.second)
                }
            )
    }

    private fun setupView(): Unit {

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

        disagree.setOnClickListener { viewModel.disagree(findNavController()) }
        agree.setOnClickListener { viewModel.consentUser(rootNavController()) }

    }

    private fun applyConfiguration(
        configuration: Configuration
    ): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        shadow.background =
            HEXGradient.from(
                HEXColor.transparent(),
                configuration.theme.primaryTextColor
            ).drawable(0.1f)

        agree.text = configuration.text.onboarding.agreeButton
        agree.setTextColor(configuration.theme.secondaryColor.color())
        agree.background =
            button(configuration.theme.primaryColorEnd.color())

        disagree.text = configuration.text.onboarding.disagreeButton
        disagree.setTextColor(configuration.theme.primaryColorEnd.color())
        disagree.background =
            button(configuration.theme.secondaryColor.color())
    }

    private fun applyItems(items: List<DroidItem>): Unit {

        adapter.submitList(items)

    }
}