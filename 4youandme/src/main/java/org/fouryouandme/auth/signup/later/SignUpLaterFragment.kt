package org.fouryouandme.auth.signup.later

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.sign_up_later.*
import org.fouryouandme.R
import org.fouryouandme.auth.AuthSectionFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.*
import org.fouryouandme.core.ext.html.setHtmlText

class SignUpLaterFragment : AuthSectionFragment<SignUpLaterViewModel>(R.layout.sign_up_later) {

    override val viewModel: SignUpLaterViewModel by lazy {
        viewModelFactory(this, getFactory { SignUpLaterViewModel(navigator, IORuntime) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {
            applyConfiguration(it)
            setupView()
        }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            logo.setImageResource(imageConfiguration.logo())

            back.setOnClickListener {
                startCoroutineAsync { authViewModel.back(authNavController(), rootNavController()) }
            }
        }

    private suspend fun applyConfiguration(configuration: Configuration): Unit =
        evalOnMain {

            setStatusBar(configuration.theme.primaryColorStart.color())

            root.background =
                HEXGradient.from(
                    configuration.theme.primaryColorStart,
                    configuration.theme.primaryColorEnd
                ).drawable()

            description.setTextColor(configuration.theme.secondaryColor.color())
            description.setHtmlText(configuration.text.signUpLater.body, true)

            divider.setBackgroundColor(configuration.theme.primaryColorEnd.color())

            back.setTextColor(configuration.theme.primaryColorEnd.color())
            back.text = configuration.text.signUpLater.confirmButton
            back.background =
                button(configuration.theme.secondaryColor.color())

        }
}