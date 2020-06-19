package org.fouryouandme.auth.consent.review

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Option
import arrow.core.extensions.fx
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.consent_review.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.entity.consent.review.ConsentReview
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator

class ConsentReviewFragment : BaseFragment<ConsentReviewViewModel>(R.layout.consent_review) {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { ConsentReviewViewModel(navigator, IORuntime) }
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
            .observeEvent { loading.setVisibility(it.active) }

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

        loading.setLoader(imageConfiguration.loading())

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter

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

        agree.text = configuration.text.onboarding.onboardingAgreeButton
        agree.setTextColor(configuration.theme.secondaryColor.color())
        agree.background =
            button(configuration.theme.primaryColorEnd.color())

        disagree.text = configuration.text.onboarding.onboardingDisagreeButton
        disagree.setTextColor(configuration.theme.primaryColorEnd.color())
        disagree.background =
            button(configuration.theme.secondaryColor.color())
    }

    private fun applyItems(items: List<DroidItem>): Unit {

        adapter.submitList(items)

    }
}