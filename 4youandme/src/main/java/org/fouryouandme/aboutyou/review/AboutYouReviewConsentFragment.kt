package org.fouryouandme.aboutyou.review

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.StableDroidAdapter
import kotlinx.android.synthetic.main.about_you_review_consent.*
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouSectionFragment
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.auth.consent.review.info.ConsentReviewHeaderViewHolder
import org.fouryouandme.auth.consent.review.info.ConsentReviewPageViewHolder
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

class AboutYouReviewConsentFragment :
    AboutYouSectionFragment<AboutYouReviewConsentViewModel>(R.layout.about_you_review_consent) {

    override val viewModel: AboutYouReviewConsentViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                AboutYouReviewConsentViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule(),
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

        aboutYouViewModel.stateLiveData()
            .observeEvent(AboutYouReviewConsentFragment::class.java.simpleName) {
                when (it) {
                    is AboutYouStateUpdate.Initialization -> initialize()
                }
            }

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
                            suspend {
                                viewModel.initialize(
                                    rootNavController(),
                                    aboutYouViewModel.state().configuration
                                )
                            }
                        }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        if (aboutYouViewModel.isInitialized())
            initialize()
    }

    private fun initialize(): Unit {

        startCoroutineAsync {

            if (viewModel.isInitialized().not()) {
                viewModel.initialize(
                    rootNavController(),
                    aboutYouViewModel.state().configuration
                )
            }

            evalOnMain {
                applyConfiguration(aboutYouViewModel.state().configuration)
                applyItems(viewModel.state().items)
            }

        }

    }

    private fun setupView(): Unit {

        toolbar.showBackButton(imageConfiguration) {
            startCoroutineAsync {
                aboutYouViewModel.back(aboutYouSectionNavController(), aboutYouNavController())
            }
        }

        recycler_view.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        recycler_view.adapter = adapter
    }

    private fun applyConfiguration(configuration: Configuration) {

        setStatusBar(configuration.theme.primaryColorStart.color())

        toolbar.setBackgroundColor(configuration.theme.primaryColorStart.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())
    }

    private fun applyItems(items: List<DroidItem<Any>>): Unit {

        adapter.submitList(items)
    }
}