package com.foryouandme.ui.aboutyou

import androidx.navigation.fragment.findNavController
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.injector
import com.foryouandme.core.ext.navigator

abstract class AboutYouSectionFragmentOld<T : BaseViewModel<*, *, *, *>>(contentLayoutId: Int) :
    BaseFragmentOld<T>(contentLayoutId) {

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