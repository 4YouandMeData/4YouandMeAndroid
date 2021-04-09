package com.foryouandme.ui.aboutyou.review

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foryouandme.R
import com.foryouandme.ui.aboutyou.AboutYouSectionFragmentOld
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.ConsentReviewHeaderViewHolder
import com.foryouandme.ui.auth.onboarding.step.consent.review.info.ConsentReviewPageViewHolder
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.*
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.about_you_review_consent.*

class AboutYouReviewConsentFragment :
    AboutYouSectionFragmentOld<AboutYouReviewConsentViewModel>(R.layout.about_you_review_consent) {

    override val viewModel: AboutYouReviewConsentViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                AboutYouReviewConsentViewModel(
                    navigator,
                    injector.consentReviewModule()
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
                    is AboutYouReviewConsentStateUpdate.Initialization -> {
                        applyItems(it.items)
                    }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent {
                loading.setVisibility(it.active, false)
            }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    AboutYouReviewConsentError.Initialization ->
                        error.setError(it.error) {
                            configuration {
                                viewModel.initialize(
                                    rootNavController(),
                                    it
                                )
                            }
                        }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {
            setupView()

            if (viewModel.isInitialized().not()) {
                viewModel.initialize(
                    rootNavController(),
                    it
                )
            }

            applyConfiguration(it)
            applyItems(viewModel.state().items)
        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            toolbar.showBackButton(imageConfiguration) {
                startCoroutineAsync {
                    aboutYouViewModel.back(aboutYouNavController(), rootNavController())
                }
            }

            recycler_view.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            recycler_view.adapter = adapter
        }

    private suspend fun applyConfiguration(configuration: Configuration) =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

            root.setBackgroundColor(configuration.theme.secondaryColor.color())
        }

    private fun applyItems(items: List<DroidItem<Any>>): Unit {

        adapter.submitList(items)
    }
}