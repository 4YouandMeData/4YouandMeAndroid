package com.foryouandme.ui.auth.onboarding.step.integration

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.arch.navigation.action.openApp
import com.foryouandme.core.arch.navigation.action.playStoreAction
import com.foryouandme.core.ext.find
import com.foryouandme.entity.page.PageRef
import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.ui.auth.onboarding.step.OnboardingStepNavController
import kotlinx.coroutines.flow.onEach

abstract class IntegrationSectionFragment(
    contentLayoutId: Int
) : BaseFragment(contentLayoutId) {

    val viewModel: IntegrationViewModel by viewModels(ownerProducer = { integrationFragment() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    if (back().not()) requireActivity().finish()
                }

            }
        )

    }

    /* --- navigation --- */

    fun authNavController(): AuthNavController = integrationFragment().authNavController()

    fun integrationFragment(): IntegrationFragment = find()

    fun integrationNavController(): IntegrationNavController =
        IntegrationNavController(findNavController())

    fun onboardingStepNavController(): OnboardingStepNavController =
        integrationFragment().onboardingStepNavController()

    fun back(): Boolean =
        if (navigator.back(integrationNavController()).not())
            if (navigator.back(onboardingStepNavController()).not())
                if (navigator.back(authNavController()).not())
                    navigator.back(rootNavController())
                else true
            else true
        else true

    fun nextPage(
        page: PageRef?,
        fromWelcome: Boolean = false
    ) {

        if (page == null)
            navigator.navigateTo(
                integrationNavController(),
                if (fromWelcome) IntegrationWelcomeToIntegrationSuccess
                else IntegrationPageToIntegrationSuccess
            )
        else
            navigator.navigateTo(
                integrationNavController(),
                if (fromWelcome) IntegrationWelcomeToIntegrationPage(page.id)
                else IntegrationPageToIntegrationPage(page.id)
            )

    }

    fun handleSpecialLink(specialLinkAction: SpecialLinkAction) {

        when (specialLinkAction) {
            is SpecialLinkAction.OpenApp ->
                openApp(specialLinkAction.app.packageName).invoke(requireContext())
            is SpecialLinkAction.Download ->
                playStoreAction(specialLinkAction.app.packageName).invoke(requireContext())
        }

    }

    fun pageToLogin(link: String, nextPage: PageRef?) {
        navigator.navigateTo(
            integrationNavController(),
            IntegrationPageToIntegrationLogin(link, nextPage?.id)
        )
    }

    fun handleLogin(nextPageId: String?) {

        if (nextPageId == null)
            navigator.navigateTo(
                integrationNavController(),
                IntegrationLoginToIntegrationSuccess
            )
        else
            navigator.navigateTo(
                integrationNavController(),
                IntegrationLoginToIntegrationPage(nextPageId)
            )

    }

}