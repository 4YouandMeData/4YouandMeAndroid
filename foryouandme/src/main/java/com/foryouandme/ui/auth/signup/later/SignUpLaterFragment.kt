package com.foryouandme.ui.auth.signup.later

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.setStatusBar
import com.foryouandme.databinding.SignUpLaterBinding
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.ui.auth.AuthSectionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.sign_up_later.*

@AndroidEntryPoint
class SignUpLaterFragment : AuthSectionFragment(R.layout.sign_up_later) {

    private val viewModel: SignUpLaterViewModel by viewModels()

    private val binding: SignUpLaterBinding?
        get() = view?.let { SignUpLaterBinding.bind(it) }

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

        viewModel.execute(SignUpLaterStateEvent.ScreenViewed)

    }

    private fun setupView() {

        binding?.logo?.setImageResource(imageConfiguration.logo())
        back.setOnClickListener { back() }

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

            viewBinding.description.setTextColor(config.theme.secondaryColor.color())
            viewBinding.description.setHtmlText(config.text.signUpLater.body, true)

            viewBinding.divider.setBackgroundColor(config.theme.primaryColorEnd.color())

            viewBinding.back.setTextColor(config.theme.primaryColorEnd.color())
            viewBinding.back.text = config.text.signUpLater.confirmButton
            viewBinding.back.background =
                button(config.theme.secondaryColor.color())
        }

    }

}