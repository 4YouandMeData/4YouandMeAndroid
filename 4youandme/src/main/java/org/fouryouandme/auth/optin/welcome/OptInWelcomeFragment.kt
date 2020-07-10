package org.fouryouandme.auth.optin.welcome

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.opt_in.*
import kotlinx.android.synthetic.main.opt_in_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.optin.OptInError
import org.fouryouandme.auth.optin.OptInLoading
import org.fouryouandme.auth.optin.OptInStateUpdate
import org.fouryouandme.auth.optin.OptInViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.view.page.EPageType

class OptInWelcomeFragment : BaseFragment<OptInViewModel>(R.layout.opt_in_welcome) {

    override val viewModel: OptInViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                OptInViewModel(
                    navigator,
                    IORuntime
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEventPeek {
                when (it) {
                    is OptInStateUpdate.Initialization ->
                        applyConfiguration(it.configuration, it.optIns)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    OptInLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    OptInError.Initialization ->
                        error.setError(it.error) { viewModel.initialize(rootNavController()) }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        Option.fx { !viewModel.state().configuration to !viewModel.state().optIns }
            .fold(
                { viewModel.initialize(rootNavController()) },
                { applyConfiguration(it.first, it.second) }
            )
    }

    private fun setupView(): Unit {

        requireParentFragment()
            .requireParentFragment()
            .toolbar
            .toOption()
            .map {
                it.showBackSecondaryButton(imageConfiguration) {
                    viewModel.sectionBack(rootNavController())
                }
            }

    }

    private fun applyConfiguration(configuration: Configuration, optIns: OptIns): Unit {

        setStatusBar(configuration.theme.secondaryColor.color())

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration = configuration,
            page = optIns.welcomePage,
            pageType = EPageType.SUCCESS,
            action1 = { viewModel.permission(findNavController()) },
            externalAction = {}
        )

    }

}