package com.foryouandme.ui.auth.signup.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.core.ext.showBackButton
import com.foryouandme.databinding.SignUpInfoBinding
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.ui.auth.AuthSectionFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpInfoFragment : AuthSectionFragment(R.layout.sign_up_info) {

    private val viewModel: SignUpInfoViewModel by viewModels()

    private val binding: SignUpInfoBinding?
        get() = view?.let { SignUpInfoBinding.bind(it) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        applyConfiguration()

    }

    override fun onConfigurationChange() {
        super.onConfigurationChange()

        applyConfiguration()

    }

    override fun onResume() {
        super.onResume()

        viewModel.execute(SignUpInfoStateEvent.ScreenViewed)

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.logo.setImageResource(imageConfiguration.logo())

            viewBinding.signUp.setImageResource(imageConfiguration.nextStep())

            viewBinding.signUpLater.setImageResource(imageConfiguration.nextStep())
            viewBinding.signUpLater.setOnClickListener {
                navigator.navigateTo(authNavController(), SignUpInfoToSignUpLater)
            }

            viewBinding.signUpLaterText.setOnClickListener {
                navigator.navigateTo(authNavController(), SignUpInfoToSignUpLater)
            }

            viewBinding.toolbar.showBackButton(imageConfiguration) { back() }

        }

    }

    private fun applyConfiguration() {

        val viewBinding = binding
        val config = configuration

        if (viewBinding != null && config != null) {

            setStatusBar(config.theme.primaryColorStart.color())

            viewBinding.root.background =
                HEXGradient.from(
                    config.theme.primaryColorStart,
                    config.theme.primaryColorEnd
                ).drawable()

            viewBinding.title.setTextColor(config.theme.secondaryColor.color())
            viewBinding.title.setHtmlText(config.text.intro.title, true)

            viewBinding.description.setTextColor(config.theme.secondaryColor.color())
            viewBinding.description.setHtmlText(config.text.intro.body, true)

            viewBinding.divider.setBackgroundColor(config.theme.primaryColorEnd.color())

            viewBinding.signUpText.setTextColor(config.theme.secondaryColor.color())
            viewBinding.signUpText.text = config.text.intro.login

            viewBinding.signUpLaterText.setTextColor(config.theme.secondaryColor.color())
            viewBinding.signUpLaterText.text = config.text.intro.back

            viewBinding.signUp.setOnClickListener {
                if (config.pinCodeLogin)
                    navigator.navigateTo(authNavController(), SignUpInfoToPinCode)
                else
                    navigator.navigateTo(authNavController(), SignUpInfoToEnterPhone)
            }

            viewBinding.signUpText.setOnClickListener {
                if (config.pinCodeLogin)
                    navigator.navigateTo(authNavController(), SignUpInfoToPinCode)
                else
                    navigator.navigateTo(authNavController(), SignUpInfoToEnterPhone)
            }

        }

    }

}