package org.fouryouandme.aboutyou

import androidx.navigation.fragment.findNavController
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator

class AboutYouFragment : BaseFragment<AboutYouViewModel>(R.layout.about_you) {

    override val viewModel: AboutYouViewModel by lazy {
        viewModelFactory(
            this,
            getFactory {
                AboutYouViewModel(
                    navigator,
                    IORuntime,
                    injector.configurationModule()
                )
            }
        )
    }

    fun aboutYouNavController() : AboutYouNavController = AboutYouNavController(findNavController())
}