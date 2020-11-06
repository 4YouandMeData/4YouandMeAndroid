package com.foryouandme.auth.signup.info

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.auth.AuthSectionFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.configuration.HEXGradient
import com.foryouandme.core.ext.*
import com.foryouandme.core.ext.html.setHtmlText
import kotlinx.android.synthetic.main.sign_up_info.*

class SignUpInfoFragment : AuthSectionFragment<SignUpInfoViewModel>(R.layout.sign_up_info) {

    override val viewModel: SignUpInfoViewModel by lazy {
        viewModelFactory(
            this,
            getFactory { SignUpInfoViewModel(navigator, injector.analyticsModule()) }
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuration {
            applyConfiguration(it)
            setupView()
        }

    }

    override fun onResume() {
        super.onResume()

        startCoroutineAsync { viewModel.logScreenViewed() }

    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            logo.setImageResource(imageConfiguration.logo())

            sign_up.setImageResource(imageConfiguration.nextStep())
            sign_up.setOnClickListener {
                startCoroutineAsync { viewModel.enterPhone(authNavController()) }
            }

            sign_up_text.setOnClickListener {
                startCoroutineAsync { viewModel.enterPhone(authNavController()) }
            }

            sign_up_later.setImageResource(imageConfiguration.nextStep())
            sign_up_later.setOnClickListener {
                startCoroutineAsync { viewModel.signUpLater(authNavController()) }
            }

            sign_up_later_text.setOnClickListener {
                startCoroutineAsync { viewModel.signUpLater(authNavController()) }
            }

            toolbar.showBackButton(imageConfiguration) {
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