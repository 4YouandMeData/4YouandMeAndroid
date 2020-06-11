package org.fouryouandme.auth.consent.welcome

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.consent.*
import kotlinx.android.synthetic.main.consent_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.ConsentError
import org.fouryouandme.auth.consent.ConsentStateUpdate
import org.fouryouandme.auth.consent.ConsentViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.Consent
import org.fouryouandme.core.ext.*
import kotlin.error

class ConsentWelcomeFragment : BaseFragment<ConsentViewModel>(R.layout.consent_welcome) {

    override val viewModel: ConsentViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory { ConsentViewModel(navigator, IORuntime) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {
                when (it) {
                    is ConsentStateUpdate.Initialization ->
                        applyData(it.configuration, it.consent)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active) }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    ConsentError.Initialization ->
                        error.setError(it.error)
                        { viewModel.initialize(rootNavController()) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().consent }
            .fold(
                {
                    viewModel.initialize(rootNavController())
                    page.isVisible = false
                },
                { applyData(it.first, it.second) }
            )
    }

    private fun setupView(): Unit {

        loading.setLoader(imageConfiguration.loading())

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.hide() }
    }

    private fun applyData(configuration: Configuration, consent: Consent): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        requireParentFragment()
            .requireParentFragment()
            .consent_root
            .toOption()
            .map { it.setBackgroundColor(configuration.theme.secondaryColor.color()) }

        page.isVisible = true
        page.applyData(configuration, null, false, consent.welcomePage)
        { it.fold({}, { viewModel.page(findNavController(), it.id, true) }) }

    }
}