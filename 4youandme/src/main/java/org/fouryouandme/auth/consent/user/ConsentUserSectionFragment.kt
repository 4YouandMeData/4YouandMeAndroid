package org.fouryouandme.auth.consent.user

import androidx.navigation.fragment.findNavController
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

abstract class ConsentUserSectionFragment(
    contentLayoutId: Int
) : BaseFragment<ConsentUserViewModel>(contentLayoutId) {

    override val viewModel: ConsentUserViewModel by lazy {
        viewModelFactory(
            consentUserFragment(),
            getFactory {
                ConsentUserViewModel(
                    navigator,
                    injector.consentUserModule()
                )
            }
        )
    }

    fun authNavController(): AuthNavController = consentUserFragment().authNavController()

    fun consentUserFragment(): ConsentUserFragment = find()

    fun consentUserNavController(): ConsentUserNavController =
        ConsentUserNavController(findNavController())

    fun consentUserAndConfiguration(block: suspend (Configuration, ConsentUserState) -> Unit) {

        configuration { config ->


            if (viewModel.isInitialized().not())
                viewModel.initialize(rootNavController()).orNull()?.let { block(config, it) }
            else
                block(config, viewModel.state())

        }

    }
}