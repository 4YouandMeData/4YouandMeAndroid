package org.fouryouandme.auth.welcome

import android.os.Bundle
import kotlinx.android.synthetic.main.welcome.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.Theme
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator

class WelcomeFragment : BaseFragment<WelcomeViewModel>(R.layout.welcome) {

    override val viewModel: WelcomeViewModel by lazy {
        viewModelFactory(this, getFactory { WelcomeViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {

                when (it) {
                    is WelcomeStateUpdate.Initialization -> applyConfiguration(it.configuration)
                }
            }

        viewModel.initialize()
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()
    }
}