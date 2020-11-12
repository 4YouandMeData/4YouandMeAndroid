package com.foryouandme.auth.consent.review

import androidx.navigation.fragment.findNavController
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseDialogFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator

abstract class ConsentReviewSectionDialogFragment : BaseDialogFragment<ConsentReviewViewModel>() {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            consentReviewFragment(),
            getFactory {
                ConsentReviewViewModel(
                    navigator,
                    injector.consentReviewModule(),
                    injector.analyticsModule()
                )
            }
        )
    }

    fun authNavController(): AuthNavController = consentReviewFragment().authNavController()

    fun consentReviewFragment(): ConsentReviewFragment = find()

    fun consentReviewNavController(): ConsentReviewNavController =
        ConsentReviewNavController(findNavController())

    fun consentReviewAndConfiguration(block: suspend (Configuration, ConsentReviewState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), config).orNull()
                    ?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}