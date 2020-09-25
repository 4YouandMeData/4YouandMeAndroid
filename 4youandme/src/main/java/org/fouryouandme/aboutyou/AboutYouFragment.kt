package org.fouryouandme.aboutyou

import android.os.Bundle
import android.view.View
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.injector
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize()

        }
    }
}