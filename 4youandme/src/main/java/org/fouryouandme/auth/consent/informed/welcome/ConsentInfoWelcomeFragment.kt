package org.fouryouandme.auth.consent.informed.welcome

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.consent_info.*
import kotlinx.android.synthetic.main.consent_info_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.consent.informed.ConsentInfoError
import org.fouryouandme.auth.consent.informed.ConsentInfoFragment
import org.fouryouandme.auth.consent.informed.ConsentInfoStateUpdate
import org.fouryouandme.auth.consent.informed.ConsentInfoViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.informed.ConsentInfo
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.hide
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.view.page.EPageType

class ConsentInfoWelcomeFragment :
    BaseFragment<ConsentInfoViewModel>(R.layout.consent_info_welcome) {

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                ConsentInfoViewModel(
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
                    is ConsentInfoStateUpdate.Initialization ->
                        applyData(it.configuration, it.consentInfo)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent { loading.setVisibility(it.active, false) }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    ConsentInfoError.Initialization ->
                        error.setError(it.error)
                        { viewModel.initialize(rootNavController()) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().consentInfo }
            .fold(
                {
                    viewModel.initialize(rootNavController())
                    page.isVisible = false
                },
                { applyData(it.first, it.second) }
            )
    }

    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map { it.hide() }
    }

    private fun applyData(configuration: Configuration, consentInfo: ConsentInfo): Unit {

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        (requireParentFragment().requireParentFragment() as? ConsentInfoFragment)
            .toOption()
            .map { it.hideAbort() }

        requireParentFragment()
            .requireParentFragment()
            .consent_info_root
            .toOption()
            .map { it.setBackgroundColor(configuration.theme.secondaryColor.color()) }

        page.isVisible = true
        page.applyData(
            configuration = configuration,
            page = consentInfo.welcomePage,
            pageType = EPageType.INFO,
            action1 = { option ->
                option.fold(
                    { viewModel.question(findNavController(), true) },
                    { viewModel.page(findNavController(), it.id, true) })
            },
            externalAction = { viewModel.web(rootNavController(), it) }
        )
    }
}