package org.fouryouandme.auth.signup.info

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.sign_up_info.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.imageConfiguration
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.showBackButton

class SignUpInfoFragment : BaseFragment<SignUpInfoViewModel>(R.layout.sign_up_info) {

    override val viewModel: SignUpInfoViewModel by lazy {
        viewModelFactory(this, getFactory { SignUpInfoViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {

                when (it) {
                    is SignUpInfoStateUpdate.Initialization -> applyConfiguration(it.configuration)
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

        sign_up.setImageResource(imageConfiguration.signUp())
        sign_up_later.setImageResource(imageConfiguration.signUp())

        toolbar.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        title.setTextColor(configuration.theme.secondaryColor.color())
        title.text = configuration.text.introTitle

        description.setTextColor(configuration.theme.secondaryColor.color())
        description.text = configuration.text.introBody

        sign_up_text.setTextColor(configuration.theme.secondaryColor.color())
        sign_up_text.text = configuration.text.introLogin

        sign_up_later_text.setTextColor(configuration.theme.secondaryColor.color())
        sign_up_later_text.text = configuration.text.introBack

    }
}