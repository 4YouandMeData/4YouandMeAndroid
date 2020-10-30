package com.fouryouandme.aboutyou

import androidx.navigation.fragment.findNavController
import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.user.User
import com.fouryouandme.core.ext.find
import com.fouryouandme.core.ext.injector
import com.fouryouandme.core.ext.navigator

abstract class AboutYouSectionFragment<T : BaseViewModel<*, *, *, *>>(contentLayoutId: Int) :
    BaseFragment<T>(contentLayoutId) {

    val aboutYouViewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            aboutYouFragment(),
            getFactory {
                AboutYouViewModel(
                    navigator,
                    injector.authModule()
                )
            }
        )
    }

    fun aboutYouFragment(): AboutYouFragment = find()

    fun aboutYouNavController(): AboutYouNavController = AboutYouNavController(findNavController())

    fun refreshUserAndConfiguration(block: suspend (Configuration, User) -> Unit): Unit =
        configuration { config ->

            if (aboutYouViewModel.isInitialized())
                aboutYouViewModel.refreshUser(rootNavController(), true)
                    .map { block(config, it.user) }
            else
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