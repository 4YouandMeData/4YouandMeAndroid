package com.foryouandme.ui.auth.onboarding.step.consent.informed.page

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.show
import com.foryouandme.core.ext.showBackSecondaryButton
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.databinding.ConsentInfoPageBinding
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoAbort
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoPageToConsentInfoModalPage
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoSectionFragment
import com.foryouandme.ui.auth.onboarding.step.consent.informed.ConsentInfoStateUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ConsentInfoPageFragment : ConsentInfoSectionFragment(R.layout.consent_info_page) {

    private val args: ConsentInfoPageFragmentArgs by navArgs()

    private val binding: ConsentInfoPageBinding?
        get() = view?.let { ConsentInfoPageBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    ConsentInfoStateUpdate.ConsentInfo -> applyData()
                    else -> Unit
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpView()
        applyData()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()
        applyData()
    }

    private fun setUpView() {

        consentInfoFragment().binding?.toolbar?.apply {

            show()

            showBackSecondaryButton(imageConfiguration) { back() }

        }

    }

    private fun applyData() {

        val viewBinding = binding
        val configuration = configuration
        val consentInfo = viewModel.state.consentInfo

        if (viewBinding != null && configuration != null && consentInfo != null) {
            setStatusBar(configuration.theme.secondaryColor.color())

            viewBinding.root.setBackgroundColor(configuration.theme.secondaryColor.color())

            consentInfoFragment()
                .showAbort(
                    configuration.theme.primaryColorEnd.color(),
                    ConsentInfoAbort.FromPage(args.id)
                )

            consentInfo.pages.firstOrNull { it.id == args.id }
                ?.let { data ->
                    viewBinding.page.applyData(
                        configuration = configuration,
                        page = data,
                        pageType = EPageType.INFO,
                        action1 = {
                            if (it == null) question(false)
                            else page(it.id, false)
                        },
                        extraStringAction = { web(it) },
                        extraPageAction = { modalPage(it.id) }
                    )
                }
        }
    }

    private fun modalPage(id: String) {
        navigator.navigateTo(
            consentInfoNavController(),
            ConsentInfoPageToConsentInfoModalPage(id)
        )
    }

}