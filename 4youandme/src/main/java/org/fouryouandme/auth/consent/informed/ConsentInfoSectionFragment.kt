package org.fouryouandme.auth.consent.informed

import androidx.navigation.fragment.findNavController
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

abstract class ConsentInfoSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ConsentInfoViewModel>(contentLayoutId) {

    override val viewModel: ConsentInfoViewModel by lazy {
        viewModelFactory(
            consentInfoFragment(),
            getFactory {
                ConsentInfoViewModel(
                    navigator,
                    IORuntime,
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