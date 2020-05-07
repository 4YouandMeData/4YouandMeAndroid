package org.fouryouandme.auth.welcome

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.welcome.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state().configuration.map { applyConfiguration(it) }
        setupView()
    }

    private fun setupView() {

        logo.setImageResource(imageConfiguration.logo())
        welcome_image.setImageResource(imageConfiguration.welcome())

    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        start.background =
            button(configuration.theme.secondaryColor.color())
        start.text = configuration.text.welcomeStartButton
        start.setTextColor(configuration.theme.primaryColorEnd.color())
        start.isAllCaps = false
    }
}