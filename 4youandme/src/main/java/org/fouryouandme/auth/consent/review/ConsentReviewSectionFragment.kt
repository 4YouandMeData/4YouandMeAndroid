package org.fouryouandme.auth.consent.review

import androidx.navigation.fragment.findNavController
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

abstract class ConsentReviewSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ConsentReviewViewModel>(contentLayoutId) {

    override val viewModel: ConsentReviewViewModel by lazy {
        viewModelFactory(
            consentReviewFragment(),
            getFactory {
                ConsentReviewViewModel(
                    navigator,
                    injector.consentReviewModule()
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