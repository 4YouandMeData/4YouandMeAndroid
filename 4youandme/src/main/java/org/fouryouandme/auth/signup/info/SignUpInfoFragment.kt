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
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.ext.html.setHtmlText

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

        sign_up.setImageResource(imageConfiguration.signUpNextStep())
        sign_up.setOnClickListener { viewModel.enterPhone(findNavController()) }

        sign_up_text.setOnClickListener { viewModel.enterPhone(findNavController()) }

        sign_up_later.setImageResource(imageConfiguration.signUpNextStep())
        sign_up_later.setOnClickListener { viewModel.signUpLater(findNavController()) }

        sign_up_later_text.setOnClickListener { viewModel.signUpLater(findNavController()) }

        toolbar.showBackButton(imageConfiguration) { viewModel.back(findNavController()) }
    }

    private fun applyConfiguration(configuration: Configuration): Unit {

        setStatusBar(configuration.theme.primaryColorStart.color())

        root.background =
            HEXGradient.from(
                configuration.theme.primaryColorStart,
                configuration.theme.primaryColorEnd
            ).drawable()

        title.setTextColor(configuration.theme.secondaryColor.color())
        title.setHtmlText(configuration.text.intro.title, true)

        description.setTextColor(configuration.theme.secondaryColor.color())
        description.setHtmlText(configuration.text.intro.body, true)

        divider.setBackgroundColor(configuration.theme.primaryColorEnd.color())

        sign_up_text.setTextColor(configuration.theme.secondaryColor.color())
        sign_up_text.text = configuration.text.intro.login

        sign_up_later_text.setTextColor(configuration.theme.secondaryColor.color())
        sign_up_later_text.text = configuration.text.intro.back

    }
}