package com.foryouandme.ui.auth.onboarding.step.consent.review.info

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.ConsentReviewInfoBinding
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.onboarding.step.consent.review.*
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentReviewInfoFragment : ConsentReviewSectionFragment(R.layout.consent_review_info) {

    private val binding: ConsentReviewInfoBinding?
        get() = view?.let { ConsentReviewInfoBinding.bind(it) }

    private val adapter: StableDroidAdapter by lazy {
        StableDroidAdapter(
            ConsentReviewHeaderViewHolder.factory(),
            ConsentReviewPageViewHolder.factory()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when(it) {
                    ConsentReviewStateUpdate.ConsentReview -> applyItems()
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyConfiguration()
        applyItems()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyConfiguration()
    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            viewBinding.recyclerView.adapter = adapter

            viewBinding.disagree.setOnClickListener {
                viewModel.execute(ConsentReviewStateEvent.Disagree)
                navigator.navigateTo(
                    consentReviewNavController(),
                    ConsentReviewInfoToConsentReviewDisagree
                )

            }
            viewBinding.agree.setOnClickListener {
                navigator.navigateTo(
                    consentNavController(),
                    ConsentReviewToOptIns
                )
            }

        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val configuration = configuration

        if (viewBinding != null && configuration != null) {

            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            viewBinding.shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    configuration.theme.primaryTextColor
                ).drawable(0.1f)

            viewBinding.agree.text = configuration.text.onboarding.agreeButton
            viewBinding.agree.setTextColor(configuration.theme.secondaryColor.color())
            viewBinding.agree.background = button(configuration.theme.primaryColorEnd.color())

            viewBinding.disagree.text = configuration.text.onboarding.disagreeButton
            viewBinding.disagree.setTextColor(configuration.theme.primaryColorEnd.color())
            viewBinding.disagree.background = button(configuration.theme.secondaryColor.color())
        }

    }

    private fun applyItems() {

        adapter.submitList(viewModel.state.items)

    }

}