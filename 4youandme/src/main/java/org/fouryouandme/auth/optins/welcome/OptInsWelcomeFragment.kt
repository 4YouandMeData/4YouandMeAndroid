package org.fouryouandme.auth.optins.welcome

import android.os.Bundle
import android.view.View
import arrow.core.None
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import kotlinx.android.synthetic.main.opt_ins.*
import kotlinx.android.synthetic.main.opt_ins_welcome.*
import org.fouryouandme.R
import org.fouryouandme.auth.optins.OptInsError
import org.fouryouandme.auth.optins.OptInsLoading
import org.fouryouandme.auth.optins.OptInsStateUpdate
import org.fouryouandme.auth.optins.OptInsViewModel
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackSecondaryButton
import org.fouryouandme.core.view.page.EPageType

class OptInsWelcomeFragment : BaseFragment<OptInsViewModel>(R.layout.opt_ins_welcome) {

    override val viewModel: OptInsViewModel by lazy {
        viewModelFactory(
            requireParentFragment(),
            getFactory {
                OptInsViewModel(
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
                    is OptInsStateUpdate.Initialization ->
                        applyConfiguration(it.configuration, it.optIns)
                }
            }

        viewModel.loadingLiveData()
            .observeEvent {
                when (it.task) {
                    OptInsLoading.Initialization ->
                        loading.setVisibility(it.active, false)
                }
            }

        viewModel.errorLiveData()
            .observeEvent {
                when (it.cause) {
                    OptInsError.Initialization ->
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

        root.setBackgroundColor(configuration.theme.secondaryColor.color())

        page.applyData(
            configuration = configuration,
            page = Page(
                "opt_ins_welcome",
                optIns.title,
                optIns.description,
                None,
                None,
                None,
                None,
                None,
                None,
                None
            ),
            pageType = EPageType.SUCCESS,
            action1 = {},
            externalAction = {}
        )

    }

}