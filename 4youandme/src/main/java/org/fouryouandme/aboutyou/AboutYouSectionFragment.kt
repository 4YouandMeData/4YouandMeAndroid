package org.fouryouandme.aboutyou

import androidx.navigation.fragment.findNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

abstract class AboutYouSectionFragment<T : BaseViewModel<*, *, *, *, *>>(contentLayoutId: Int) :
    BaseFragment<T>(contentLayoutId) {

    val aboutYouViewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            aboutYouFragment(),
            getFactory {
                AboutYouViewModel(
                    navigator,
                    IORuntime,
                    injector.authModule()
                )
            }
        )
    }

    fun aboutYouFragment(): AboutYouFragment = find()

    fun aboutYouNavController(): AboutYouNavController = AboutYouNavController(findNavController())

    fun refreshUserAndConfiguration(block: suspend (Configuration, User) -> Unit): Unit =
        configuration { config ->

            aboutYouViewModel.initialize(rootNavController(), true)
                .map { block(config, it.user) }

        }

    fun userAndConfiguration(block: suspend (Configuration, User) -> Unit): Unit =
        configuration { config ->

            if (aboutYouViewModel.isInitialized())
                block(config, aboutYouViewModel.state().user)
            else
                aboutYouViewModel.initialize(rootNavController(), false)
                    .map { block(config, it.user) }

        }

}