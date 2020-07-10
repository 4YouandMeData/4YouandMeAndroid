package org.fouryouandme.auth.signup.later

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.sign_up_later.*
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
import org.fouryouandme.core.ext.setStatusBar

class SignUpLaterFragment : BaseFragment<SignUpLaterViewModel>(R.layout.sign_up_later) {

    override val viewModel: SignUpLaterViewModel by lazy {
        viewModelFactory(this, getFactory { SignUpLaterViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent {

                when (it) {
                    is SignUpLaterStateUpdate.Initialization -> applyConfiguration(it.configuration)
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

        back.setOnClickListener { viewModel.back(findNavController()) }
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        description.setTextColor(configuration.theme.secondaryColor.color())
        description.text = configuration.text.signUpLater.body

        divider.setBackgroundColor(configuration.theme.primaryColorEnd.color())

        back.setTextColor(configuration.theme.primaryColorEnd.color())
        back.text = configuration.text.signUpLater.confirmButton
        back.background =
            button(configuration.theme.secondaryColor.color())

    }
}