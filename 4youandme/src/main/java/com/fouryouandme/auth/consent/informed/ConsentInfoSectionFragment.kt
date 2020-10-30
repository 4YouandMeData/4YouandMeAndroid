package com.fouryouandme.auth.consent.informed

import androidx.navigation.fragment.findNavController
import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.ext.find
import com.fouryouandme.core.ext.injector
import com.fouryouandme.core.ext.navigator

abstract class ConsentInfoSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ConsentInfoViewModel>(contentLayoutId) {

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            consentInfoFragment(),
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    injector.consentInfoModule(),
                    injector.answerModule()
                )
            }
        )
    }

    fun authNavController(): AuthNavController = consentInfoFragment().authNavController()

    fun consentInfoFragment(): ConsentInfoFragment = find()

    fun consentInfoNavController(): ConsentInfoNavController =
        ConsentInfoNavController(findNavController())

    fun consentInfoAndConfiguration(block: suspend (Configuration, ConsentInfoState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController(), config).orNull()
                    ?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}