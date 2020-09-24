package org.fouryouandme.aboutyou

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import arrow.core.toOption
import kotlinx.android.synthetic.main.about_you_menu.*
import kotlinx.android.synthetic.main.about_you_menu.root
import org.fouryouandme.R
import org.fouryouandme.aboutyou.AboutYouFragment
import org.fouryouandme.aboutyou.AboutYouNavController
import org.fouryouandme.aboutyou.AboutYouStateUpdate
import org.fouryouandme.aboutyou.AboutYouViewModel
import org.fouryouandme.aboutyou.menu.AboutYouMenuNavController
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.*

open class AboutYouSectionFragment(contentLayoutId: Int) :
    BaseFragment<AboutYouViewModel>(contentLayoutId) {
    override val viewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            aboutYouFragment(),
            getFactory {
                AboutYouViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule()
                )
            }
        )
    }

    fun aboutYouFragment() : AboutYouFragment = find()

    fun aboutYouNavController() = aboutYouFragment().aboutYouNavController()
}