package com.fouryouandme.auth.signup.later

import android.os.Bundle
import android.view.View
import com.fouryouandme.R
import com.fouryouandme.auth.AuthSectionFragment
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.entity.configuration.Configuration
import com.fouryouandme.core.entity.configuration.HEXGradient
import com.fouryouandme.core.entity.configuration.button.button
import com.fouryouandme.core.ext.*
import com.fouryouandme.core.ext.html.setHtmlText
import kotlinx.android.synthetic.main.sign_up_later.*

class SignUpLaterFragment : AuthSectionFragment<SignUpLaterViewModel>(R.layout.sign_up_later) {

    override val viewModel: SignUpLaterViewModel by lazy {
        viewModelFactory(this, getFactory { SignUpLaterViewModel(navigator) })
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